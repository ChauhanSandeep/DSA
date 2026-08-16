#!/usr/bin/env python3
"""
serve.py — local dev server for the DSA weekend-review dashboard.

Why this exists
---------------
Opening `Tracking/site/index.html` directly (file://) requires the browser's
File System Access API to persist grades, which prompts the user for a file
handle on every fresh session. That friction defeats the "grade six problems
on Saturday morning" ritual.

This server removes the friction entirely:
    * Serves `Tracking/site/` statically.
    * Exposes `POST /api/grade` that applies an SM-2 update to a single
      problem in `Tracking/data/state.json` (atomic rewrite).
    * Rebuilds the site immediately after each grade so the queue reflects
      the new `nextDue` on next dashboard reload.

No auth, no CORS surface, no external deps — stdlib only. Bind is
127.0.0.1 by default so nothing is exposed off-machine.

Usage
-----
    python Tracking/scripts/serve.py          # http://127.0.0.1:8787
    python Tracking/scripts/serve.py --port 9000
    python Tracking/scripts/serve.py --no-rebuild   # skip auto-rebuild on grade
"""

from __future__ import annotations

import argparse
import http.server
import json
import os
import subprocess
import sys
import tempfile
import threading
import time
from datetime import date, timedelta
from http import HTTPStatus
from pathlib import Path
from urllib.parse import urlparse, parse_qs


REPO_ROOT = Path(__file__).resolve().parents[2]
DATA_DIR = REPO_ROOT / "Tracking" / "data"
STATE_JSON = DATA_DIR / "state.json"
CYCLE_JSON = DATA_DIR / "cycle.json"
SITE_DIR = REPO_ROOT / "Tracking" / "site"
BUILD_SCRIPT = REPO_ROOT / "Tracking" / "scripts" / "build.py"
JAVA_ROOT = (REPO_ROOT / "src" / "main" / "java").resolve()

# Weekly goal increment for "load more" — MUST match build.py WEEKLY_GOAL.
WEEKLY_GOAL = 10

sys.path.insert(0, str(Path(__file__).resolve().parent))
from _queue import coming_saturday  # noqa: E402

# SM-2 formula constants — MUST match app.js and Tracking/README.md.
MAX_INTERVAL_DAYS = 180
MIN_INTERVAL_DAYS = 3
MIN_EASE = 1.3
MAX_EASE = 3.0

# Difficulty-weighted intervals: harder problems come back sooner, easier ones
# stretch out. Keyed by rating band (falls back to Leetcode difficulty).
# MUST match app.js and Tracking/README.md.
DIFFICULTY_INTERVAL_FACTOR = {
    "Very Hard": 0.5,
    "Hard": 0.65,
    "Medium": 1.0,
    "Easy": 1.35,
    "Unknown": 1.0,
}


def effective_difficulty(entry: dict) -> str:
    band = (entry.get("rating") or {}).get("band")
    if band:
        return band
    return entry.get("difficulty") or "Unknown"

# --------------------------------------------------------------------------
# SM-2 update — server-side authoritative copy. Duplicated from the JS in
# app.js on purpose; if they drift, the "authoritative" one is the JS since
# it computes the value the user sees toasted. Keep them in sync manually.
# --------------------------------------------------------------------------

def apply_sm2(sm2: dict, grade: str, today: date, difficulty: str = "Unknown") -> dict:
    sm2 = dict(sm2 or {})
    sm2.setdefault("easeFactor", 2.5)
    sm2.setdefault("intervalDays", 0)
    sm2.setdefault("repetitions", 0)
    r = sm2["repetitions"]

    if grade == "blank":
        sm2["repetitions"] = 0
        sm2["intervalDays"] = 3
        sm2["easeFactor"] -= 0.20
    elif grade == "hint":
        sm2["intervalDays"] = max(3, round(sm2["intervalDays"] * 1.3))
        sm2["repetitions"] += 1
        sm2["easeFactor"] -= 0.15
    elif grade == "solved":
        if r == 0:   sm2["intervalDays"] = 7
        elif r == 1: sm2["intervalDays"] = 14
        else:        sm2["intervalDays"] = round(sm2["intervalDays"] * sm2["easeFactor"])
        sm2["repetitions"] += 1
    elif grade == "trivial":
        if r == 0:   sm2["intervalDays"] = 14
        elif r == 1: sm2["intervalDays"] = 30
        else:        sm2["intervalDays"] = round(sm2["intervalDays"] * sm2["easeFactor"] * 1.3)
        sm2["repetitions"] += 1
        sm2["easeFactor"] += 0.15
    else:
        raise ValueError(f"Unknown grade: {grade!r}")

    # Difficulty weighting: shorten hard, stretch easy — then clamp.
    factor = DIFFICULTY_INTERVAL_FACTOR.get(difficulty, 1.0)
    sm2["intervalDays"] = max(MIN_INTERVAL_DAYS, round(sm2["intervalDays"] * factor))
    sm2["easeFactor"] = max(MIN_EASE, min(MAX_EASE, sm2["easeFactor"]))
    sm2["intervalDays"] = min(sm2["intervalDays"], MAX_INTERVAL_DAYS)
    sm2["lastReviewed"] = today.isoformat()
    sm2["nextDue"] = (today + timedelta(days=sm2["intervalDays"])).isoformat()
    sm2["lastGrade"] = grade
    return sm2


# --------------------------------------------------------------------------
# State access — atomic read/modify/write.
# --------------------------------------------------------------------------

_state_lock = threading.Lock()


def load_state() -> dict:
    return json.loads(STATE_JSON.read_text())


def atomic_write_state(state: dict) -> None:
    tmp = STATE_JSON.with_suffix(".json.tmp")
    tmp.write_text(json.dumps(state, indent=2, ensure_ascii=False) + "\n")
    os.replace(tmp, STATE_JSON)


def grade_problem(task: str, grade: str) -> dict:
    with _state_lock:
        state = load_state()
        if task not in state["problems"]:
            raise KeyError(task)
        entry = state["problems"][task]
        new_sm2 = apply_sm2(entry.get("sm2", {}), grade, date.today(),
                            effective_difficulty(entry))
        entry["sm2"] = new_sm2
        history = entry.setdefault("history", [])
        history.append({"date": date.today().isoformat(), "grade": grade})
        atomic_write_state(state)
        return {"task": task, "sm2": new_sm2, "grade": grade}


# --------------------------------------------------------------------------
# Source editing — read / write the problem's actual .java file so small
# fixes spotted during revision can be applied straight from the dashboard.
# Writes are atomic and confined to src/main/java; a timestamped backup is
# dropped in the OS temp dir. git remains the primary safety net.
# --------------------------------------------------------------------------

def _resolve_java_path(task: str) -> tuple[str, Path]:
    """Map a task to its .java path, refusing anything outside JAVA_ROOT."""
    state = load_state()
    if task not in state["problems"]:
        raise KeyError(task)
    rel = state["problems"][task]["javaFile"]
    path = (REPO_ROOT / rel).resolve()
    if JAVA_ROOT != path and JAVA_ROOT not in path.parents:
        raise ValueError(f"refusing path outside java source root: {rel}")
    return rel, path


def read_source(task: str) -> dict:
    rel, path = _resolve_java_path(task)
    if not path.exists():
        raise FileNotFoundError(rel)
    return {"task": task, "javaFile": rel, "source": path.read_text(encoding="utf-8")}


def save_source(task: str, source: str) -> dict:
    with _state_lock:
        rel, path = _resolve_java_path(task)
        if not path.exists():
            raise FileNotFoundError(rel)

        # Timestamped backup outside the repo so it never pollutes git.
        backup_dir = Path(tempfile.gettempdir()) / "dsa-source-backups"
        backup_dir.mkdir(exist_ok=True)
        stamp = time.strftime("%Y%m%d-%H%M%S")
        backup = backup_dir / f"{task}.{stamp}.java.bak"
        backup.write_text(path.read_text(encoding="utf-8"), encoding="utf-8")

        # Normalise to exactly one trailing newline, then atomic replace.
        normalised = source.rstrip("\n") + "\n"
        tmp = path.with_suffix(path.suffix + ".tmp")
        tmp.write_text(normalised, encoding="utf-8")
        os.replace(tmp, path)
        return {"task": task, "javaFile": rel, "bytes": len(normalised),
                "backup": str(backup)}


def load_more_batch() -> dict:
    """Raise this week's goal by WEEKLY_GOAL so the user can keep going.

    Called when the user has met the current weekly goal and wants more work.
    The goal is keyed by the coming review Saturday; a stale week resets to a
    single WEEKLY_GOAL before the increment. The subsequent rebuild re-renders
    the dashboard with the higher target.
    """
    with _state_lock:
        review_iso = coming_saturday(date.today()).isoformat()
        cycle = None
        if CYCLE_JSON.exists():
            try:
                cycle = json.loads(CYCLE_JSON.read_text())
            except Exception:
                cycle = None
        if not cycle or cycle.get("weekOf") != review_iso:
            cycle = {"weekOf": review_iso, "target": WEEKLY_GOAL}
        new_target = int(cycle.get("target", WEEKLY_GOAL)) + WEEKLY_GOAL
        payload = {"weekOf": review_iso, "target": new_target}
        tmp = CYCLE_JSON.with_suffix(".json.tmp")
        tmp.write_text(json.dumps(payload, indent=2) + "\n")
        os.replace(tmp, CYCLE_JSON)
        return {"target": new_target, "added": WEEKLY_GOAL}


def rebuild_site() -> tuple[bool, str]:
    result = subprocess.run(
        [sys.executable, str(BUILD_SCRIPT)],
        capture_output=True, text=True,
    )
    if result.returncode == 0:
        return True, result.stdout.strip()
    return False, result.stderr.strip() or result.stdout.strip()


# --------------------------------------------------------------------------
# HTTP handler
# --------------------------------------------------------------------------

class Handler(http.server.SimpleHTTPRequestHandler):
    server_version = "DSA-Tracker/1.0"

    def __init__(self, *args, auto_rebuild: bool = True, **kwargs):
        self.auto_rebuild = auto_rebuild
        super().__init__(*args, directory=str(SITE_DIR), **kwargs)

    # Suppress noisy default logging; keep only errors + API calls.
    def log_message(self, fmt: str, *args) -> None:
        msg = fmt % args
        if msg.startswith(('"GET /assets/', '"GET /problems/', '"GET /patterns/',
                            '"GET / ', '"GET /index.html')):
            return
        sys.stderr.write(f"[{self.log_date_time_string()}] {msg}\n")

    def send_json(self, status: int, payload: dict) -> None:
        body = json.dumps(payload).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(body)

    def do_POST(self) -> None:
        parsed = urlparse(self.path)
        if parsed.path == "/api/load-more":
            self.handle_load_more()
            return
        if parsed.path == "/api/save-source":
            self.handle_save_source()
            return
        if parsed.path != "/api/grade":
            self.send_json(HTTPStatus.NOT_FOUND, {"error": "not found"})
            return

        try:
            length = int(self.headers.get("Content-Length") or 0)
            body = self.rfile.read(length).decode("utf-8") if length else ""
            payload = json.loads(body or "{}")
            task = str(payload.get("task", "")).strip()
            grade = str(payload.get("grade", "")).strip()
            if not task or grade not in {"trivial", "solved", "hint", "blank"}:
                self.send_json(HTTPStatus.BAD_REQUEST, {
                    "error": "task and grade (trivial|solved|hint|blank) required"
                })
                return

            result = grade_problem(task, grade)
        except KeyError as e:
            self.send_json(HTTPStatus.NOT_FOUND, {"error": f"unknown task: {e.args[0]}"})
            return
        except Exception as e:
            self.send_json(HTTPStatus.INTERNAL_SERVER_ERROR, {"error": str(e)})
            return

        rebuild_message = None
        if self.auto_rebuild:
            ok, msg = rebuild_site()
            rebuild_message = msg
            if not ok:
                sys.stderr.write(f"[rebuild failed] {msg}\n")

        self.send_json(HTTPStatus.OK, {
            "ok": True,
            "task": result["task"],
            "grade": result["grade"],
            "sm2": result["sm2"],
            "rebuilt": self.auto_rebuild,
        })

    def handle_load_more(self) -> None:
        try:
            result = load_more_batch()
        except Exception as e:
            self.send_json(HTTPStatus.INTERNAL_SERVER_ERROR, {"error": str(e)})
            return

        if self.auto_rebuild:
            ok, msg = rebuild_site()
            if not ok:
                sys.stderr.write(f"[rebuild failed] {msg}\n")

        self.send_json(HTTPStatus.OK, {
            "ok": True,
            "target": result["target"],
            "added": result["added"],
            "rebuilt": self.auto_rebuild,
        })

    def handle_save_source(self) -> None:
        try:
            length = int(self.headers.get("Content-Length") or 0)
            body = self.rfile.read(length).decode("utf-8") if length else ""
            payload = json.loads(body or "{}")
            task = str(payload.get("task", "")).strip()
            source = payload.get("source")
            if not task or not isinstance(source, str) or not source.strip():
                self.send_json(HTTPStatus.BAD_REQUEST, {
                    "error": "task and non-empty source required"
                })
                return
            result = save_source(task, source)
        except KeyError as e:
            self.send_json(HTTPStatus.NOT_FOUND, {"error": f"unknown task: {e.args[0]}"})
            return
        except (ValueError, FileNotFoundError) as e:
            self.send_json(HTTPStatus.BAD_REQUEST, {"error": str(e)})
            return
        except Exception as e:
            self.send_json(HTTPStatus.INTERNAL_SERVER_ERROR, {"error": str(e)})
            return

        if self.auto_rebuild:
            ok, msg = rebuild_site()
            if not ok:
                sys.stderr.write(f"[rebuild failed] {msg}\n")

        self.send_json(HTTPStatus.OK, {
            "ok": True,
            "task": result["task"],
            "javaFile": result["javaFile"],
            "bytes": result["bytes"],
            "rebuilt": self.auto_rebuild,
        })

    def do_GET(self) -> None:
        parsed = urlparse(self.path)
        if parsed.path == "/api/ping":
            self.send_json(HTTPStatus.OK, {"ok": True})
            return
        if parsed.path == "/api/source":
            self.handle_read_source(parse_qs(parsed.query))
            return
        return super().do_GET()

    def handle_read_source(self, query: dict) -> None:
        task = (query.get("task") or [""])[0].strip()
        if not task:
            self.send_json(HTTPStatus.BAD_REQUEST, {"error": "task required"})
            return
        try:
            result = read_source(task)
        except KeyError as e:
            self.send_json(HTTPStatus.NOT_FOUND, {"error": f"unknown task: {e.args[0]}"})
            return
        except (ValueError, FileNotFoundError) as e:
            self.send_json(HTTPStatus.BAD_REQUEST, {"error": str(e)})
            return
        except Exception as e:
            self.send_json(HTTPStatus.INTERNAL_SERVER_ERROR, {"error": str(e)})
            return
        self.send_json(HTTPStatus.OK, {"ok": True, **result})


def make_handler_class(auto_rebuild: bool):
    class BoundHandler(Handler):
        def __init__(self, *a, **kw):
            super().__init__(*a, auto_rebuild=auto_rebuild, **kw)
    return BoundHandler


# --------------------------------------------------------------------------
# Entry point
# --------------------------------------------------------------------------

def ensure_site_exists() -> bool:
    """Rebuild the site on every startup so the queue always reflects the
    current state.json (grades, new syncs, week rollover). build.py is a pure
    render of state.json — it never mutates your progress — so rebuilding is
    idempotent no matter how often you launch the server in a week.

    If the build fails but a prior site exists, serve the stale copy rather
    than block the ritual; only refuse to start when there is nothing to serve.
    """
    site_exists = SITE_DIR.exists() and (SITE_DIR / "index.html").exists()
    print("Rebuilding site from state.json...")
    ok, msg = rebuild_site()
    if ok:
        return True
    if site_exists:
        print(f"build failed — serving existing site:\n{msg}", file=sys.stderr)
        return True
    print(f"build failed:\n{msg}", file=sys.stderr)
    return False


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--port", type=int, default=8787)
    parser.add_argument("--bind", default="127.0.0.1")
    parser.add_argument("--no-rebuild", action="store_true",
                        help="skip auto-rebuild after each grade")
    args = parser.parse_args()

    if not STATE_JSON.exists():
        print(f"ERROR: {STATE_JSON.relative_to(REPO_ROOT)} missing. "
              f"Run sync.py first.", file=sys.stderr)
        return 1
    if not ensure_site_exists():
        return 1

    handler_cls = make_handler_class(auto_rebuild=not args.no_rebuild)
    with http.server.ThreadingHTTPServer((args.bind, args.port), handler_cls) as httpd:
        url = f"http://{args.bind}:{args.port}/"
        print()
        print(f"DSA weekend-review dashboard serving at:")
        print(f"    {url}")
        print()
        print(f"  · dashboard   {url}index.html")
        print(f"  · state       {STATE_JSON.relative_to(REPO_ROOT)}")
        print(f"  · auto-rebuild = {not args.no_rebuild}")
        print()
        print("Ctrl+C to stop.")
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nStopped.")
    return 0


if __name__ == "__main__":
    sys.exit(main())

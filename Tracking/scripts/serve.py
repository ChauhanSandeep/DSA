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
import threading
import time
from datetime import date, timedelta
from http import HTTPStatus
from pathlib import Path
from urllib.parse import urlparse


REPO_ROOT = Path(__file__).resolve().parents[2]
DATA_DIR = REPO_ROOT / "Tracking" / "data"
STATE_JSON = DATA_DIR / "state.json"
SITE_DIR = REPO_ROOT / "Tracking" / "site"
BUILD_SCRIPT = REPO_ROOT / "Tracking" / "scripts" / "build.py"

# SM-2 formula constants — MUST match app.js and Tracking/README.md.
MAX_INTERVAL_DAYS = 180
MIN_EASE = 1.3
MAX_EASE = 3.0

# --------------------------------------------------------------------------
# SM-2 update — server-side authoritative copy. Duplicated from the JS in
# app.js on purpose; if they drift, the "authoritative" one is the JS since
# it computes the value the user sees toasted. Keep them in sync manually.
# --------------------------------------------------------------------------

def apply_sm2(sm2: dict, grade: str, today: date) -> dict:
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
        new_sm2 = apply_sm2(entry.get("sm2", {}), grade, date.today())
        entry["sm2"] = new_sm2
        history = entry.setdefault("history", [])
        history.append({"date": date.today().isoformat(), "grade": grade})
        atomic_write_state(state)
        return {"task": task, "sm2": new_sm2, "grade": grade}


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

    def do_GET(self) -> None:
        parsed = urlparse(self.path)
        if parsed.path == "/api/ping":
            self.send_json(HTTPStatus.OK, {"ok": True})
            return
        return super().do_GET()


def make_handler_class(auto_rebuild: bool):
    class BoundHandler(Handler):
        def __init__(self, *a, **kw):
            super().__init__(*a, auto_rebuild=auto_rebuild, **kw)
    return BoundHandler


# --------------------------------------------------------------------------
# Entry point
# --------------------------------------------------------------------------

def ensure_site_exists() -> bool:
    if SITE_DIR.exists() and (SITE_DIR / "index.html").exists():
        return True
    print("site/ not built yet — running build.py once...")
    ok, msg = rebuild_site()
    if not ok:
        print(f"build failed:\n{msg}", file=sys.stderr)
        return False
    return True


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

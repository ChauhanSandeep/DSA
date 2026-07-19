#!/usr/bin/env python3
"""
review_log.py — emit a Markdown summary of grades added since a base commit.

Reads the CURRENT Tracking/data/state.json (working copy) and, given a base
git ref via --base, compares each problem's history[] to detect newly added
grade entries. Prints a compact Markdown comment intended for the pinned
"Study log" GitHub issue.

The base ref defaults to HEAD^ (the previous commit), which is what the
log-review workflow triggers on: a push that modifies state.json.

Usage:
    python Tracking/scripts/review_log.py
    python Tracking/scripts/review_log.py --base HEAD^
    python Tracking/scripts/review_log.py --base origin/master~1
"""

from __future__ import annotations

import argparse
import json
import subprocess
import sys
from collections import Counter
from datetime import date
from pathlib import Path


REPO_ROOT = Path(__file__).resolve().parents[2]
STATE_JSON = REPO_ROOT / "Tracking" / "data" / "state.json"
STATE_REL = "Tracking/data/state.json"

GRADE_EMOJI = {
    "trivial": "⭐",
    "solved":  "✅",
    "hint":    "🟡",
    "blank":   "🔴",
}
GRADE_ORDER = ["trivial", "solved", "hint", "blank"]


def load_state_from_ref(ref: str) -> dict | None:
    try:
        result = subprocess.run(
            ["git", "show", f"{ref}:{STATE_REL}"],
            cwd=REPO_ROOT,
            capture_output=True,
            check=True,
            text=True,
        )
    except subprocess.CalledProcessError:
        # Base ref doesn't have state.json (probably the first ever commit
        # touching it). Treat as empty baseline.
        return None
    return json.loads(result.stdout)


def load_state_from_disk() -> dict:
    return json.loads(STATE_JSON.read_text())


def diff_histories(before: dict | None, after: dict) -> list[dict]:
    """Return list of {task, problemName, pattern, difficulty, grade, date}
    entries that appear in `after` but not in `before`.
    """
    before_by_task: dict[str, set[tuple[str, str]]] = {}
    if before:
        for task_id, entry in before.get("problems", {}).items():
            before_by_task[task_id] = {
                (h.get("date", ""), h.get("grade", ""))
                for h in entry.get("history", [])
            }

    added: list[dict] = []
    for task_id, entry in after.get("problems", {}).items():
        prev = before_by_task.get(task_id, set())
        for h in entry.get("history", []):
            key = (h.get("date", ""), h.get("grade", ""))
            if key not in prev:
                added.append({
                    "task": task_id,
                    "problemName": entry.get("problemName") or task_id,
                    "pattern": entry.get("pattern", "Uncategorized"),
                    "difficulty": entry.get("difficulty", "Unknown"),
                    "grade": h.get("grade", "unknown"),
                    "date": h.get("date", ""),
                    "javaFile": entry.get("javaFile", ""),
                })
    added.sort(key=lambda a: (a["date"], a["task"]))
    return added


def format_comment(added: list[dict]) -> str:
    if not added:
        return ""

    counts: Counter[str] = Counter(a["grade"] for a in added)
    by_date: dict[str, list[dict]] = {}
    for a in added:
        by_date.setdefault(a["date"], []).append(a)

    header_summary = " · ".join(
        f"{GRADE_EMOJI.get(g, '?')} {counts.get(g, 0)}"
        for g in GRADE_ORDER
        if counts.get(g, 0)
    )
    lines: list[str] = []
    lines.append(f"### 📓 Review log — {header_summary} ({len(added)} total)")
    lines.append("")

    for review_date in sorted(by_date):
        pretty = date.fromisoformat(review_date).strftime("%a %d %b %Y") \
            if review_date else "unknown"
        lines.append(f"**{pretty}**")
        for a in by_date[review_date]:
            emoji = GRADE_EMOJI.get(a["grade"], "•")
            lines.append(
                f"- {emoji} `{a['grade']}` **{a['problemName']}** "
                f"— {a['difficulty']} · {a['pattern']}"
            )
        lines.append("")
    return "\n".join(lines) + "\n"


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--base", default="HEAD^",
                        help="git ref for the baseline state.json (default: HEAD^)")
    args = parser.parse_args()

    if not STATE_JSON.exists():
        print("ERROR: state.json missing.", file=sys.stderr)
        return 1

    before = load_state_from_ref(args.base)
    after = load_state_from_disk()

    added = diff_histories(before, after)
    if not added:
        # Nothing meaningful to log; exit with a sentinel so the workflow
        # can skip commenting.
        print("", end="")
        return 78 if "--exit-empty" in sys.argv else 0

    sys.stdout.write(format_comment(added))
    return 0


if __name__ == "__main__":
    sys.exit(main())

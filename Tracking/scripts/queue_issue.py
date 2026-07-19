#!/usr/bin/env python3
"""
queue_issue.py — emits a Markdown body for the weekly review issue.

Reads Tracking/data/state.json, picks the queue for the *coming Saturday*
using the same picker logic as build.py, and prints a checklist to stdout.
The GitHub Action pipes this into `gh issue create --body-file -`.

Also prints a stat line the Action can capture via GITHUB_OUTPUT to build
the issue title (e.g. "Weekend review — 2026-07-25 · 6 due").

Usage:
    python Tracking/scripts/queue_issue.py                # print body to stdout
    python Tracking/scripts/queue_issue.py --title-only   # print just the title
"""

from __future__ import annotations

import argparse
import json
import sys
from datetime import date, datetime, timedelta
from pathlib import Path


REPO_ROOT = Path(__file__).resolve().parents[2]
STATE_JSON = REPO_ROOT / "Tracking" / "data" / "state.json"

QUEUE_SIZE = 6
DIFFICULTY_RANK = {"Hard": 3, "Medium": 2, "Easy": 1, "Unknown": 0}
GITHUB_BLOB = "https://github.com/ChauhanSandeep/DSA/blob/master/"


def coming_saturday(today: date) -> date:
    days_to_saturday = (5 - today.weekday()) % 7
    return today + timedelta(days=days_to_saturday)


def pick_queue(state: dict, target: date, size: int) -> list[dict]:
    due = []
    for entry in state["problems"].values():
        next_due_str = entry.get("sm2", {}).get("nextDue")
        if not next_due_str:
            continue
        if entry.get("flags", {}).get("skip"):
            continue
        if date.fromisoformat(next_due_str) <= target:
            due.append(entry)
    due.sort(key=lambda e: (
        e["sm2"]["nextDue"],
        -DIFFICULTY_RANK.get(e.get("difficulty", "Unknown"), 0),
        e["sm2"].get("easeFactor", 2.5),
    ))
    return due[:size]


def format_body(state: dict, review_day: date, queue: list[dict]) -> str:
    total_due = sum(
        1 for entry in state["problems"].values()
        if entry.get("sm2", {}).get("nextDue")
        and date.fromisoformat(entry["sm2"]["nextDue"]) <= review_day
    )
    lines: list[str] = []
    lines.append(f"# Weekend review — {review_day.strftime('%A, %d %b %Y')}")
    lines.append("")
    lines.append(f"**{total_due} problem(s) due** · showing top {len(queue)} · queue size cap = {QUEUE_SIZE}")
    lines.append("")
    lines.append("Grade each attempt from the local dashboard:")
    lines.append("```")
    lines.append("python Tracking/scripts/build.py && open Tracking/site/index.html")
    lines.append("```")
    lines.append("Keyboard: `1` trivial · `2` solved · `3` hint · `4` blank")
    lines.append("")
    lines.append("---")
    lines.append("")

    for idx, problem in enumerate(queue, 1):
        name = problem.get("problemName") or problem["task"]
        pattern = problem.get("pattern", "Uncategorized")
        diff = problem.get("difficulty", "Unknown")
        badges = []
        if problem.get("isBlind75"):
            badges.append("Blind 75")
        if problem.get("isNC150"):
            badges.append("NC150")
        badge_str = " · ".join(badges)
        badge_suffix = f" · {badge_str}" if badge_str else ""

        leetcode_url = problem.get("leetcodeUrl") or ""
        source_url = GITHUB_BLOB + problem["javaFile"]

        header = f"- [ ] **{idx}. {name}** — {diff} · {pattern}{badge_suffix}"
        lines.append(header)

        link_parts = []
        if leetcode_url:
            link_parts.append(f"[Leetcode]({leetcode_url})")
        link_parts.append(f"[repo source]({source_url})")

        sm2 = problem.get("sm2", {})
        reps = sm2.get("repetitions", 0)
        ease = sm2.get("easeFactor", 2.5)
        interval = sm2.get("intervalDays", 0)
        last = sm2.get("lastReviewed") or "never"
        link_parts.append(f"reps `{reps}` · ease `{ease:.2f}` · interval `{interval}d` · last `{last}`")
        lines.append("  " + " · ".join(link_parts))
        lines.append("")

    lines.append("---")
    lines.append("")
    lines.append("_This issue is auto-updated every Saturday. "
                 "Committing changes to `Tracking/data/state.json` will "
                 "log a summary to the pinned Study log issue._")
    return "\n".join(lines) + "\n"


def format_title(review_day: date, queue_size: int) -> str:
    return f"Weekend review — {review_day.isoformat()} · {queue_size} problems"


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--title-only", action="store_true")
    args = parser.parse_args()

    if not STATE_JSON.exists():
        print("ERROR: state.json missing. Run sync.py first.", file=sys.stderr)
        return 1

    state = json.loads(STATE_JSON.read_text())
    review_day = coming_saturday(date.today())
    queue = pick_queue(state, review_day, QUEUE_SIZE)

    if args.title_only:
        print(format_title(review_day, len(queue)))
        return 0

    sys.stdout.write(format_body(state, review_day, queue))
    return 0


if __name__ == "__main__":
    sys.exit(main())

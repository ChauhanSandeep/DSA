#!/usr/bin/env python3
"""
_queue.py — shared queue-picker used by build.py, queue_issue.py, report.py.

Keeping this in one place prevents drift between the local dashboard's
queue, the weekly-nudge issue's queue, and the CLI report's queue.
"""

from __future__ import annotations

from datetime import date
from typing import Iterable


DIFFICULTY_RANK = {"Hard": 3, "Medium": 2, "Easy": 1, "Unknown": 0}


def _is_reviewable(entry: dict) -> bool:
    return not entry.get("flags", {}).get("skip", False)


def pick_queue(state: dict, target: date, size: int) -> list[dict]:
    """Return the SM-2 due queue for `target`, backfilled from pinned anchors.

    Order:
      1. Everything with nextDue <= target, sorted by (nextDue asc,
         difficulty desc, easeFactor asc), truncated to `size`.
      2. If still short, fill with pinned anchors whose flag says pinned,
         ordered by (lastReviewed asc, difficulty desc). Prevents the
         queue from ever being empty during a low-activity stretch.
    """
    entries: Iterable[dict] = list(state["problems"].values())

    due: list[dict] = []
    for entry in entries:
        if not _is_reviewable(entry):
            continue
        next_due_str = entry.get("sm2", {}).get("nextDue")
        if not next_due_str:
            continue
        if date.fromisoformat(next_due_str) <= target:
            due.append(entry)

    due.sort(key=lambda e: (
        e["sm2"]["nextDue"],
        -DIFFICULTY_RANK.get(e.get("difficulty", "Unknown"), 0),
        e["sm2"].get("easeFactor", 2.5),
    ))
    queue: list[dict] = due[:size]

    if len(queue) < size:
        already = {e["task"] for e in queue}
        anchors = [e for e in entries
                   if e.get("flags", {}).get("pinned")
                   and _is_reviewable(e)
                   and e["task"] not in already]
        anchors.sort(key=lambda e: (
            e["sm2"].get("lastReviewed") or "0000-00-00",
            -DIFFICULTY_RANK.get(e.get("difficulty", "Unknown"), 0),
        ))
        for entry in anchors:
            if len(queue) >= size:
                break
            queue.append(entry)

    return queue


def count_due(state: dict, target: date) -> int:
    count = 0
    for entry in state["problems"].values():
        if not _is_reviewable(entry):
            continue
        next_due_str = entry.get("sm2", {}).get("nextDue")
        if not next_due_str:
            continue
        if date.fromisoformat(next_due_str) <= target:
            count += 1
    return count


def coming_saturday(today: date) -> date:
    from datetime import timedelta
    days_to_saturday = (5 - today.weekday()) % 7
    return today + timedelta(days=days_to_saturday)

#!/usr/bin/env python3
"""
_queue.py — shared queue-picker used by build.py, queue_issue.py, report.py.

Keeping this in one place prevents drift between the local dashboard's
queue, the weekly-nudge issue's queue, and the CLI report's queue.
"""

from __future__ import annotations

from datetime import date


DIFFICULTY_RANK = {"Very Hard": 4, "Hard": 3, "Medium": 2, "Easy": 1, "Unknown": 0}

# The "revision" (depth) lane is a *soft cap*, not a fixed count: take up to
# MAX_REVIEW_SLOTS problems you've already seen, but only as many as are
# actually due. Early on — when few problems have been reviewed — this lands
# around 2; as your reviewed backlog grows it climbs to the cap of 4. The
# "new" (breadth) lane always fills the remaining slots, so of a 10-item queue
# you get roughly 8 new / 2 revision early, drifting to 6 new / 4 revision.
MAX_REVIEW_SLOTS = 4


def effective_difficulty(entry: dict) -> str:
    """The difficulty we schedule by: prefer the rating band (which the badges
    show and which includes 'Very Hard'), else the raw Leetcode difficulty."""
    band = (entry.get("rating") or {}).get("band")
    if band:
        return band
    return entry.get("difficulty") or "Unknown"


def difficulty_rank(entry: dict) -> int:
    return DIFFICULTY_RANK.get(effective_difficulty(entry), 0)


def _is_reviewable(entry: dict) -> bool:
    return not entry.get("flags", {}).get("skip", False)


def _is_due(entry: dict, target: date) -> bool:
    next_due_str = entry.get("sm2", {}).get("nextDue")
    return bool(next_due_str) and date.fromisoformat(next_due_str) <= target


def _has_been_reviewed(entry: dict) -> bool:
    return bool(entry.get("sm2", {}).get("lastReviewed"))


def pick_queue(state: dict, target: date, size: int,
               review_slots: int | None = None) -> list[dict]:
    """Return the weekly queue for `target` using two hardest-first lanes.

    Lanes:
      * REVISION (depth): problems already reviewed and now due, ordered by
        (difficulty desc, nextDue asc, easeFactor asc) — old *hard* problems
        resurface first. Soft-capped at MAX_REVIEW_SLOTS and filled only by
        what's actually due (≈2 early, up to 4 once you have a review backlog).
      * NEW (breadth): never-reviewed problems now due, ordered by
        (difficulty desc, nextDue asc) — hardest new ground first. Takes every
        remaining slot, so it absorbs whatever the revision lane leaves.

    The two lanes cross-backfill when one underflows, and pinned anchors fill
    any final gap so Saturday always has `size` problems.
    """
    if review_slots is None:
        review_slots = min(MAX_REVIEW_SLOTS, size)
    new_slots = size - review_slots

    entries = [e for e in state["problems"].values() if _is_reviewable(e)]
    due = [e for e in entries if _is_due(e, target)]

    review_pool = [e for e in due if _has_been_reviewed(e)]
    new_pool = [e for e in due if not _has_been_reviewed(e)]

    review_pool.sort(key=lambda e: (
        -difficulty_rank(e),
        e["sm2"]["nextDue"],
        e["sm2"].get("easeFactor", 2.5),
    ))
    new_pool.sort(key=lambda e: (
        -difficulty_rank(e),
        e["sm2"]["nextDue"],
    ))

    queue: list[dict] = review_pool[:review_slots] + new_pool[:new_slots]

    # Cross-backfill: a short lane borrows the other lane's surplus so we
    # always hit `size` when enough problems are due overall.
    if len(queue) < size:
        taken = {e["task"] for e in queue}
        surplus = [e for e in (review_pool + new_pool) if e["task"] not in taken]
        for entry in surplus:
            if len(queue) >= size:
                break
            queue.append(entry)

    # Final fallback: pinned anchors during quiet stretches.
    if len(queue) < size:
        taken = {e["task"] for e in queue}
        anchors = [e for e in entries
                   if e.get("flags", {}).get("pinned") and e["task"] not in taken]
        anchors.sort(key=lambda e: (
            e["sm2"].get("lastReviewed") or "0000-00-00",
            -difficulty_rank(e),
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

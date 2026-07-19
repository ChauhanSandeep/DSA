#!/usr/bin/env python3
"""
curate_core.py — pick the ~50 anchor problems and pin them.

Purpose
-------
For a 1-year hibernation phase you don't need to keep 500+ problems warm.
You need ~50 problems that anchor each pattern well enough that everything
else can be re-derived from those anchors. This script produces a
defensible first-cut of that anchor set and pins it in state.json so the
weekly queue picker prefers those problems when the SM-2 due-list is thin.

Selection algorithm
-------------------
Only NC-matched problems in the repo are considered (source == "neetcode").
For each NeetCode pattern, we pick a small number of "anchors":

    NC-pattern quota (defaults, tunable via CORE_PATTERN_QUOTA below):
        Arrays & Hashing        4
        Trees                   4
        Graphs                  4
        1-D Dynamic Programming 4
        2-D Dynamic Programming 3
        Sliding Window          3
        Two Pointers            3
        Backtracking            3
        Binary Search           3
        Stack                   3
        Linked List             3
        Heap / Priority Queue   3
        Greedy                  2
        Intervals               2
        Advanced Graphs         2
        Bit Manipulation        2
        Math & Geometry         2
        Tries                   2
                        -----------
                        total ~52

Within each pattern, pick problems by this ranking (best first):
    1. isBlind75 (they're already the canonical anchors)
    2. difficulty Hard > Medium > Easy (harder pins the pattern harder)
    3. lower leetcodeNumber (older, more foundational)
    4. deterministic tiebreak on task_id

Manual override
---------------
You can hand-edit Tracking/core.md — replace or delete task_ids in the
"Core anchors" table — and re-run this script. It respects the current
core.md if present:

    * If core.md exists and contains a section labeled "Core anchors" with
      a table listing task_ids, those become the anchor set (verbatim).
    * The auto-quota selection only runs when core.md is missing or empty.

Either way, flags.pinned in state.json is set to `true` for anchors and
`false` for everyone else, so the queue picker can rely on the flag.

Usage
-----
    # First run — generates the anchor set + core.md.
    python Tracking/scripts/curate_core.py

    # Re-run after manually editing core.md — re-syncs flags.pinned.
    python Tracking/scripts/curate_core.py

    # Preview only — no writes.
    python Tracking/scripts/curate_core.py --dry-run
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from collections import defaultdict
from datetime import date
from pathlib import Path


REPO_ROOT = Path(__file__).resolve().parents[2]
DATA_DIR = REPO_ROOT / "Tracking" / "data"
STATE_JSON = DATA_DIR / "state.json"
CORE_MD = REPO_ROOT / "Tracking" / "core.md"

# Pattern quotas. Sum ≈ 52, adjust freely.
CORE_PATTERN_QUOTA: dict[str, int] = {
    "Arrays & Hashing":         4,
    "Trees":                    4,
    "Graphs":                   4,
    "1-D Dynamic Programming":  4,
    "2-D Dynamic Programming":  3,
    "Sliding Window":           3,
    "Two Pointers":             3,
    "Backtracking":             3,
    "Binary Search":            3,
    "Stack":                    3,
    "Linked List":              3,
    "Heap / Priority Queue":    3,
    "Greedy":                   2,
    "Intervals":                2,
    "Advanced Graphs":          2,
    "Bit Manipulation":         2,
    "Math & Geometry":          2,
    "Tries":                    2,
}

DIFFICULTY_RANK = {"Hard": 3, "Medium": 2, "Easy": 1, "Unknown": 0}


# ---------------------------------------------------------------------------
# Anchor selection
# ---------------------------------------------------------------------------

def rank_key(entry: dict) -> tuple:
    """Lower = better. Bool tricks: `not isBlind75` puts True first."""
    return (
        not entry.get("isBlind75"),                    # Blind75 first
        -DIFFICULTY_RANK.get(entry.get("difficulty", "Unknown"), 0),  # Hard first
        entry.get("leetcodeNumber") or 10_000,          # older = smaller
        entry.get("task", ""),                          # deterministic
    )


def auto_select_anchors(state: dict) -> list[str]:
    by_pattern: dict[str, list[dict]] = defaultdict(list)
    for entry in state["problems"].values():
        if entry.get("source") != "neetcode":
            continue
        by_pattern[entry["pattern"]].append(entry)

    selected: list[str] = []
    used_urls: set[str] = set()
    for pattern, quota in CORE_PATTERN_QUOTA.items():
        candidates = sorted(by_pattern.get(pattern, []), key=rank_key)
        picked_here = 0
        for entry in candidates:
            if picked_here >= quota:
                break
            url = entry.get("leetcodeUrl", "")
            # Skip if we already picked another implementation of this problem.
            if url and url in used_urls:
                continue
            selected.append(entry["task"])
            if url:
                used_urls.add(url)
            picked_here += 1
    return selected


# ---------------------------------------------------------------------------
# core.md read / write
# ---------------------------------------------------------------------------

CORE_TABLE_HEADER_RE = re.compile(r"^\|\s*Pattern\s*\|", re.IGNORECASE)


def read_core_md_task_ids() -> list[str] | None:
    if not CORE_MD.exists():
        return None
    lines = CORE_MD.read_text().splitlines()
    task_ids: list[str] = []
    inside_table = False
    for line in lines:
        if CORE_TABLE_HEADER_RE.match(line):
            inside_table = True
            continue
        if inside_table:
            if not line.strip().startswith("|"):
                inside_table = False
                continue
            # Skip separator line: | --- | --- |
            if set(line.strip()) <= set("|-: "):
                continue
            cells = [c.strip() for c in line.strip().strip("|").split("|")]
            # Expected columns: Pattern | Task | Problem name | Difficulty | Badges
            if len(cells) >= 2 and cells[1]:
                # Task is rendered as `task_id` (inline code) — strip backticks.
                task_id = cells[1].strip("`").strip()
                if task_id:
                    task_ids.append(task_id)
    return task_ids or None


def write_core_md(state: dict, task_ids: list[str]) -> None:
    problems = state["problems"]
    picked = [problems[t] for t in task_ids if t in problems]
    picked.sort(key=lambda e: (e["pattern"], rank_key(e)))

    by_pattern: dict[str, list[dict]] = defaultdict(list)
    for entry in picked:
        by_pattern[entry["pattern"]].append(entry)

    lines: list[str] = []
    lines.append("# Core anchors — DSA hibernation set\n")
    lines.append(
        "A curated ~50-problem list that anchors each NeetCode pattern. "
        "These are the problems the weekly queue picker prefers when nothing "
        "else is due — the goal is that during a low-activity year you touch "
        "every anchor at least once every ~6 months, keeping the pattern "
        "warm enough to re-derive everything else on top of it.\n"
    )
    lines.append(f"_Total: {len(picked)} problems across {len(by_pattern)} patterns. "
                 f"Generated {date.today().isoformat()} by `curate_core.py`._\n")
    lines.append("## How to edit\n")
    lines.append(
        "- Edit rows in the **Core anchors** table below (add / remove / swap `task` ids).\n"
        "- Re-run `python Tracking/scripts/curate_core.py` to sync `flags.pinned` in `state.json`.\n"
        "- Deleting `core.md` and re-running restores the auto-selected default.\n"
    )
    lines.append("## Core anchors\n")
    lines.append("| Pattern | Task | Problem | Difficulty | Badges |")
    lines.append("| --- | --- | --- | --- | --- |")

    for pattern in sorted(by_pattern):
        for entry in by_pattern[pattern]:
            badges = []
            if entry.get("isBlind75"): badges.append("Blind75")
            if entry.get("isNC150"):   badges.append("NC150")
            lines.append(
                f"| {pattern} | `{entry['task']}` | "
                f"[{entry.get('problemName') or entry['task']}]"
                f"({entry.get('leetcodeUrl','')}) | "
                f"{entry.get('difficulty','Unknown')} | "
                f"{' · '.join(badges) or '—'} |"
            )
    lines.append("")

    lines.append("## Notes\n")
    lines.append(
        "- Auto-selection ranks within a pattern by: Blind75 first, then "
        "difficulty (Hard > Medium > Easy), then leetcode number (older = "
        "more foundational), then task id.\n"
        "- Pattern quotas live at the top of `Tracking/scripts/curate_core.py`.\n"
        "- Problems outside NeetCode (source == \"extras\") are never auto-selected; "
        "you can still pin them manually by adding rows here.\n"
    )
    CORE_MD.write_text("\n".join(lines) + "\n")


# ---------------------------------------------------------------------------
# state.json update
# ---------------------------------------------------------------------------

def apply_pinned_flags(state: dict, anchors: set[str]) -> tuple[int, int]:
    """Return (pinned_count, unpinned_count) after applying anchors."""
    pinned, unpinned = 0, 0
    for task_id, entry in state["problems"].items():
        flags = entry.setdefault("flags", {"pinned": False, "skip": False})
        want = task_id in anchors
        if flags.get("pinned") != want:
            flags["pinned"] = want
        if want:
            pinned += 1
        else:
            unpinned += 1
    return pinned, unpinned


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--dry-run", action="store_true",
                        help="print selection, don't write core.md or state.json")
    args = parser.parse_args()

    if not STATE_JSON.exists():
        print("ERROR: state.json missing. Run sync.py first.", file=sys.stderr)
        return 1

    state = json.loads(STATE_JSON.read_text())

    # Respect a hand-edited core.md if it lists task_ids; otherwise auto-pick.
    from_md = read_core_md_task_ids()
    if from_md:
        source = "core.md (manual override)"
        anchor_ids = from_md
    else:
        source = "auto-selection (NC-only pattern quotas)"
        anchor_ids = auto_select_anchors(state)

    # Warn about invalid task_ids.
    known = set(state["problems"])
    unknown = [t for t in anchor_ids if t not in known]
    valid = [t for t in anchor_ids if t in known]
    if unknown:
        print(f"WARNING: {len(unknown)} task id(s) in core.md not in state.json:")
        for t in unknown:
            print(f"    {t}")

    by_pattern_pick: dict[str, int] = defaultdict(int)
    for t in valid:
        by_pattern_pick[state["problems"][t]["pattern"]] += 1

    print(f"Source: {source}")
    print(f"Anchors: {len(valid)}")
    print("Per-pattern:")
    for pattern in sorted(by_pattern_pick):
        print(f"  {by_pattern_pick[pattern]:2d}  {pattern}")

    if args.dry_run:
        print("\n[dry-run] no files written.")
        return 0

    pinned, unpinned = apply_pinned_flags(state, set(valid))
    STATE_JSON.write_text(json.dumps(state, indent=2, ensure_ascii=False) + "\n")
    write_core_md(state, valid)

    print()
    print(f"wrote {CORE_MD.relative_to(REPO_ROOT)}")
    print(f"updated {STATE_JSON.relative_to(REPO_ROOT)} — "
          f"{pinned} pinned, {unpinned} unpinned.")
    return 0


if __name__ == "__main__":
    sys.exit(main())

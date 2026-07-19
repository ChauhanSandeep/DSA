#!/usr/bin/env python3
"""
sync.py — keep Tracking/data/state.json in step with the repo.

What it does
------------
1. Walks src/main/java/**/*.java.
2. For each file, extracts the Leetcode URL slug from the leading Javadoc.
   (URL is the only reliable join key — Javadoc styles vary across the repo.)
3. Joins to Tracking/data/neetcode.json by URL slug to pick up
   pattern / difficulty / NC150 / Blind75 metadata.
4. Files with a URL that isn't in NeetCode go to extras.json with a
   directory-derived pattern.
5. Files with no URL are treated as helpers and reported to stderr.
6. Writes / updates state.json:
   - Preserves SM-2 fields (easeFactor, intervalDays, repetitions, history)
     for problems that already exist.
   - Adds new problems with a staggered `nextDue`:
       * NC-matched problems  → spread across 10 weeks
       * Repo extras          → spread across 26 weeks
       * Round-robin by pattern within each week slot.
   - Removes problems whose Java file no longer exists (prints them so you
     can spot renames).

Idempotency
-----------
Re-running with no repo changes is a no-op (touches nothing on disk beyond
rewriting formatted JSON, but content is stable).

Usage
-----
    python Tracking/scripts/sync.py
    python Tracking/scripts/sync.py --dry-run
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from collections import defaultdict, deque
from dataclasses import dataclass, field
from datetime import date, timedelta
from pathlib import Path


REPO_ROOT = Path(__file__).resolve().parents[2]
JAVA_ROOT = REPO_ROOT / "src" / "main" / "java"
DATA_DIR = REPO_ROOT / "Tracking" / "data"
NEETCODE_JSON = DATA_DIR / "neetcode.json"
STATE_JSON = DATA_DIR / "state.json"
EXTRAS_JSON = DATA_DIR / "extras.json"
MANUAL_MAP_JSON = DATA_DIR / "manual-map.json"

# How many characters of the file to scan for the Leetcode URL.
# The class Javadoc always sits above the class declaration.
HEAD_BYTES = 6000

# Initial-seed staggering horizons (in weeks).
NC_STAGGER_WEEKS = 10
EXTRAS_STAGGER_WEEKS = 26

# Default SM-2 seed for brand-new problems.
DEFAULT_EASE = 2.5

LEETCODE_URL_RE = re.compile(
    r"leetcode\.com/problems/([a-z0-9][a-z0-9\-]*)",
    re.IGNORECASE,
)


# ---------------------------------------------------------------------------
# Directory → pattern heuristics for repo extras (non-NC problems).
# Order matters: first match wins. Longest prefix first.
# ---------------------------------------------------------------------------
DIR_PATTERN_RULES: list[tuple[str, str]] = [
    ("arrays/slidingwindow",            "Sliding Window"),
    ("arrays/twopointer",               "Two Pointers"),
    ("arrays/binarysearch",             "Binary Search"),
    ("arrays/hashing",                  "Arrays & Hashing"),
    ("arrays/prefixsum",                "Arrays & Hashing"),
    ("arrays/matrix",                   "Arrays & Hashing"),
    ("arrays/intervals",                "Intervals"),
    ("arrays/greedy",                   "Greedy"),
    ("arrays/sorting",                  "Arrays & Hashing"),
    ("arrays",                          "Arrays & Hashing"),
    ("strings/twopointers",             "Two Pointers"),
    ("strings/hashmap",                 "Arrays & Hashing"),
    ("strings/heap",                    "Heap / Priority Queue"),
    ("strings/greedy",                  "Greedy"),
    ("strings/sorting",                 "Arrays & Hashing"),
    ("strings/stack",                   "Stack"),
    ("strings/patternmatching",         "Strings"),
    ("strings",                         "Strings"),
    ("stacksandqueues/monotonicstack",  "Stack"),
    ("stacksandqueues/calculator",      "Stack"),
    ("stacksandqueues/design",          "Design"),
    ("stacksandqueues",                 "Stack"),
    ("trees/segmenttree",               "Advanced Graphs"),
    ("trees/binarysearchtree",          "Trees"),
    ("trees/traversal",                 "Trees"),
    ("trees/dfs",                       "Trees"),
    ("trees/bfs",                       "Trees"),
    ("trees",                           "Trees"),
    ("graphs",                          "Graphs"),
    ("linkedlist",                      "Linked List"),
    ("dynamicprogramming/2d",           "2-D Dynamic Programming"),
    ("dynamicprogramming/1d",           "1-D Dynamic Programming"),
    ("dynamicprogramming",              "1-D Dynamic Programming"),
    ("backtrack",                       "Backtracking"),
    ("Recursion",                       "Recursion"),
    ("bitwiseoperation",                "Bit Manipulation"),
    ("heap",                            "Heap / Priority Queue"),
    ("priorityqueue",                   "Heap / Priority Queue"),
    ("intervals",                       "Intervals"),
    ("greedy",                          "Greedy"),
    ("math",                            "Math & Geometry"),
    ("design",                          "Design"),
    ("trie",                            "Tries"),
    ("Multithreading",                  "Concurrency"),
]


# ---------------------------------------------------------------------------
# Data models
# ---------------------------------------------------------------------------

@dataclass
class RepoProblem:
    task: str                 # class name (== file stem)
    java_file: str            # path relative to repo root
    slug: str | None          # leetcode slug or None if no URL found
    leetcode_url: str | None
    directory_pattern: str    # best-guess pattern from directory


@dataclass
class SyncCounts:
    scanned: int = 0
    skipped_no_url: int = 0
    matched_nc: int = 0
    extras: int = 0
    preserved: int = 0
    added: int = 0
    removed: list[str] = field(default_factory=list)


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

def load_json(path: Path) -> dict | list | None:
    if not path.exists():
        return None
    return json.loads(path.read_text())


def write_json(path: Path, payload) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, indent=2, ensure_ascii=False) + "\n")


def extract_leetcode_slug(text: str) -> str | None:
    match = LEETCODE_URL_RE.search(text)
    if match:
        return match.group(1).lower()
    return None


def directory_pattern_for(java_file_rel: str) -> str:
    # java_file_rel is like "src/main/java/arrays/hashing/TwoSum.java"
    # Strip the src/main/java/ prefix, look at the directory segments.
    stripped = java_file_rel
    prefix = "src/main/java/"
    if stripped.startswith(prefix):
        stripped = stripped[len(prefix):]
    for prefix_rule, pattern in DIR_PATTERN_RULES:
        if stripped.startswith(prefix_rule + "/") or stripped == prefix_rule:
            return pattern
    return "Uncategorized"


def scan_repo() -> list[RepoProblem]:
    problems: list[RepoProblem] = []
    for java_path in sorted(JAVA_ROOT.rglob("*.java")):
        rel = java_path.relative_to(REPO_ROOT).as_posix()
        try:
            head = java_path.read_text(encoding="utf-8", errors="replace")[:HEAD_BYTES]
        except Exception:
            continue

        slug = extract_leetcode_slug(head)
        leetcode_url = f"https://leetcode.com/problems/{slug}/" if slug else None
        problems.append(RepoProblem(
            task=java_path.stem,
            java_file=rel,
            slug=slug,
            leetcode_url=leetcode_url,
            directory_pattern=directory_pattern_for(rel),
        ))
    return problems


def apply_manual_overrides(
    problems: list[RepoProblem], manual_map: dict
) -> None:
    by_class = (manual_map or {}).get("byJavaClass", {})
    for problem in problems:
        override_url = by_class.get(problem.task)
        if override_url:
            slug_match = LEETCODE_URL_RE.search(override_url)
            if slug_match:
                problem.slug = slug_match.group(1).lower()
                problem.leetcode_url = override_url


def stagger_next_due(
    new_entries: list[dict],
    horizon_weeks: int,
    start: date,
) -> None:
    """Assigns nextDue to entries, round-robin by pattern across `horizon_weeks`.

    Week 0 is the coming Saturday from `start` (or start itself if start is Sat).
    Every slot is exactly one week apart.
    """
    if not new_entries:
        return

    # Group by pattern preserving deterministic order.
    grouped: dict[str, list[dict]] = defaultdict(list)
    order: list[str] = []
    for entry in new_entries:
        pattern = entry["pattern"]
        if pattern not in grouped:
            order.append(pattern)
        grouped[pattern].append(entry)

    # Round-robin flatten: one from each pattern, repeat until all placed.
    queues = {p: deque(grouped[p]) for p in order}
    flattened: list[dict] = []
    while any(queues[p] for p in order):
        for p in order:
            if queues[p]:
                flattened.append(queues[p].popleft())

    # Compute first Saturday >= start.
    weekday = start.weekday()  # Mon=0 .. Sun=6, Sat=5
    days_to_saturday = (5 - weekday) % 7
    first_slot = start + timedelta(days=days_to_saturday)

    for index, entry in enumerate(flattened):
        week_offset = index % horizon_weeks
        entry["sm2"]["nextDue"] = (
            first_slot + timedelta(weeks=week_offset)
        ).isoformat()


def build_state(
    repo_problems: list[RepoProblem],
    neetcode_by_slug: dict[str, dict],
    existing_state: dict | None,
    today: date,
) -> tuple[dict, dict, SyncCounts]:
    counts = SyncCounts()
    existing_problems = (existing_state or {}).get("problems", {})

    state_problems: dict[str, dict] = {}
    extras_problems: list[dict] = []
    new_state_entries: list[dict] = []
    new_extras_entries: list[dict] = []

    # ------------------------------------------------------------------
    # Disambiguate duplicate class names (e.g. PalindromePairs exists in
    # two packages). We key state.json by `task_id`, which equals the
    # class name if unique, or "<ClassName>__<lastPkgSegment>" otherwise.
    # The display fields (`task`, `problemName`) are unaffected.
    # ------------------------------------------------------------------
    seen_class_names: dict[str, int] = defaultdict(int)
    for p in repo_problems:
        if p.slug:
            seen_class_names[p.task] += 1

    def task_id_for(repo_problem: RepoProblem) -> str:
        if seen_class_names[repo_problem.task] <= 1:
            return repo_problem.task
        # Terminal package segment as a suffix disambiguator.
        rel = repo_problem.java_file
        parts = rel.split("/")
        # parts[-1] is the file, parts[-2] is the immediate directory.
        pkg_hint = parts[-2] if len(parts) >= 2 else "dup"
        return f"{repo_problem.task}__{pkg_hint}"

    for repo_problem in repo_problems:
        counts.scanned += 1

        if not repo_problem.slug:
            counts.skipped_no_url += 1
            continue

        task_id = task_id_for(repo_problem)
        nc_entry = neetcode_by_slug.get(repo_problem.slug)
        preserved = existing_problems.get(task_id)

        # Base metadata from repo + NC (or extras) join.
        if nc_entry:
            counts.matched_nc += 1
            base = {
                "task": task_id,
                "className": repo_problem.task,
                "javaFile": repo_problem.java_file,
                "leetcodeUrl": nc_entry["leetcodeUrl"],
                "leetcodeNumber": nc_entry.get("leetcodeNumber"),
                "problemName": nc_entry["name"],
                "pattern": nc_entry["pattern"],
                "difficulty": nc_entry["difficulty"],
                "isNC150": nc_entry.get("isNC150", False),
                "isBlind75": nc_entry.get("isBlind75", False),
                "source": "neetcode",
            }
        else:
            counts.extras += 1
            base = {
                "task": task_id,
                "className": repo_problem.task,
                "javaFile": repo_problem.java_file,
                "leetcodeUrl": repo_problem.leetcode_url,
                "leetcodeNumber": None,
                "problemName": repo_problem.task,
                "pattern": repo_problem.directory_pattern,
                "difficulty": "Unknown",
                "isNC150": False,
                "isBlind75": False,
                "source": "extras",
            }
            extras_problems.append({
                "task": task_id,
                "className": repo_problem.task,
                "javaFile": repo_problem.java_file,
                "leetcodeUrl": repo_problem.leetcode_url,
                "pattern": repo_problem.directory_pattern,
            })

        if preserved:
            counts.preserved += 1
            entry = {
                **base,
                "sm2": preserved.get("sm2", _fresh_sm2(today)),
                "history": preserved.get("history", []),
                "userNotes": preserved.get("userNotes", ""),
                "flags": preserved.get("flags", {"pinned": False, "skip": False}),
            }
            entry["sm2"] = _ensure_sm2_shape(entry["sm2"], today)
        else:
            counts.added += 1
            entry = {
                **base,
                "sm2": _fresh_sm2(today),
                "history": [],
                "userNotes": "",
                "flags": {"pinned": False, "skip": False},
            }
            (new_state_entries if nc_entry else new_extras_entries).append(entry)

        state_problems[task_id] = entry

    # Detect removals (previously in state, no longer in repo).
    for task_id in existing_problems:
        if task_id not in state_problems:
            counts.removed.append(task_id)

    # Apply staggering to freshly added entries only.
    stagger_next_due(new_state_entries, NC_STAGGER_WEEKS, today)
    stagger_next_due(new_extras_entries, EXTRAS_STAGGER_WEEKS, today)

    state_payload = {
        "schemaVersion": 1,
        "generatedAt": today.isoformat(),
        "problems": state_problems,
    }
    extras_payload = {
        "generatedAt": today.isoformat(),
        "problems": sorted(extras_problems, key=lambda p: p["task"]),
    }
    return state_payload, extras_payload, counts


def _fresh_sm2(today: date) -> dict:
    return {
        "easeFactor": DEFAULT_EASE,
        "intervalDays": 0,
        "repetitions": 0,
        "lastReviewed": None,
        "nextDue": today.isoformat(),
        "lastGrade": None,
    }


def _ensure_sm2_shape(sm2: dict, today: date) -> dict:
    defaults = _fresh_sm2(today)
    for key, value in defaults.items():
        sm2.setdefault(key, value)
    return sm2


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--dry-run", action="store_true",
                        help="print counts and diffs, don't write files")
    args = parser.parse_args()

    neetcode_payload = load_json(NEETCODE_JSON)
    if not neetcode_payload:
        print(f"ERROR: {NEETCODE_JSON.relative_to(REPO_ROOT)} missing. "
              f"Run scrape_neetcode.py first.", file=sys.stderr)
        return 1

    neetcode_by_slug: dict[str, dict] = {
        # `id` in neetcode.json is the slug.
        p["id"].lower(): p for p in neetcode_payload["problems"]
    }

    manual_map = load_json(MANUAL_MAP_JSON) or {}
    existing_state = load_json(STATE_JSON)

    repo_problems = scan_repo()
    apply_manual_overrides(repo_problems, manual_map)

    state, extras, counts = build_state(
        repo_problems=repo_problems,
        neetcode_by_slug=neetcode_by_slug,
        existing_state=existing_state,
        today=date.today(),
    )

    print("Sync summary")
    print("------------")
    print(f"  .java files scanned      : {counts.scanned}")
    print(f"  skipped (no Leetcode URL): {counts.skipped_no_url}")
    print(f"  matched to NeetCode      : {counts.matched_nc}")
    print(f"  repo extras (URL, no NC) : {counts.extras}")
    print(f"  preserved SM-2 state     : {counts.preserved}")
    print(f"  newly added              : {counts.added}")
    print(f"  removed since last sync  : {len(counts.removed)}")
    for name in counts.removed:
        print(f"      - {name}")

    if args.dry_run:
        print("\n[dry-run] no files written.")
        return 0

    write_json(STATE_JSON, state)
    write_json(EXTRAS_JSON, extras)

    # Seed an empty manual-map.json on first run so users know it exists.
    if not MANUAL_MAP_JSON.exists():
        write_json(MANUAL_MAP_JSON, {"byJavaClass": {}})

    print(f"\nwrote {STATE_JSON.relative_to(REPO_ROOT)}")
    print(f"wrote {EXTRAS_JSON.relative_to(REPO_ROOT)}")
    return 0


if __name__ == "__main__":
    sys.exit(main())

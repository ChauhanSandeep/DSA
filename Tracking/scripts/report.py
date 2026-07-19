#!/usr/bin/env python3
"""
report.py — coverage report for the DSA tracker.

Reads Tracking/data/{neetcode.json, state.json, extras.json} and prints:

* NeetCode coverage (NC150, Blind75, NC-all).
* NeetCode problems NOT in the repo (candidates to solve).
* Repo extras grouped by pattern.
* Repo files skipped (helpers / no Leetcode URL) — quick sanity check.
* Initial-seed staggering distribution: nextDue counts per week
  (so you can see the ramp-up profile).
* Weekly-queue picker preview: which 6 problems are due this Saturday.

Read-only. Never writes.

Usage:
    python Tracking/scripts/report.py
    python Tracking/scripts/report.py --missing        # extra: list all NC misses
    python Tracking/scripts/report.py --skipped        # extra: list skipped files
"""

from __future__ import annotations

import argparse
import json
import sys
from collections import Counter, defaultdict
from datetime import date, datetime, timedelta
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from _queue import pick_queue, coming_saturday, DIFFICULTY_RANK  # noqa: E402


REPO_ROOT = Path(__file__).resolve().parents[2]
DATA_DIR = REPO_ROOT / "Tracking" / "data"
NEETCODE_JSON = DATA_DIR / "neetcode.json"
STATE_JSON = DATA_DIR / "state.json"
EXTRAS_JSON = DATA_DIR / "extras.json"
JAVA_ROOT = REPO_ROOT / "src" / "main" / "java"

QUEUE_SIZE = 6


def load_json(path: Path) -> dict:
    if not path.exists():
        print(f"ERROR: {path.relative_to(REPO_ROOT)} missing.", file=sys.stderr)
        sys.exit(1)
    return json.loads(path.read_text())


def parse_date(iso: str | None) -> date | None:
    if not iso:
        return None
    return datetime.fromisoformat(iso).date()


# ---------------------------------------------------------------------------
# Sections
# ---------------------------------------------------------------------------

def print_header(title: str) -> None:
    print()
    print(title)
    print("=" * len(title))


def coverage_section(neetcode: dict, state: dict) -> None:
    nc_problems = neetcode["problems"]
    nc_by_url = {p["leetcodeUrl"]: p for p in nc_problems}

    repo_nc_urls = {
        entry["leetcodeUrl"]
        for entry in state["problems"].values()
        if entry.get("source") == "neetcode" and entry.get("leetcodeUrl")
    }

    total_nc = len(nc_problems)
    matched = len(repo_nc_urls & nc_by_url.keys())

    nc150_total = sum(1 for p in nc_problems if p["isNC150"])
    nc150_matched = sum(
        1 for url in repo_nc_urls if nc_by_url[url]["isNC150"]
    )
    blind_total = sum(1 for p in nc_problems if p["isBlind75"])
    blind_matched = sum(
        1 for url in repo_nc_urls if nc_by_url[url]["isBlind75"]
    )

    print_header("NeetCode coverage")
    print(f"  Blind 75      : {blind_matched:3d} / {blind_total:3d} "
          f"({blind_matched * 100 // blind_total}%)")
    print(f"  NeetCode 150  : {nc150_matched:3d} / {nc150_total:3d} "
          f"({nc150_matched * 100 // nc150_total}%)")
    print(f"  NeetCode All  : {matched:3d} / {total_nc:3d} "
          f"({matched * 100 // total_nc}%)")


def missing_nc_section(neetcode: dict, state: dict, show_all: bool) -> None:
    nc_problems = neetcode["problems"]
    repo_nc_urls = {
        entry["leetcodeUrl"]
        for entry in state["problems"].values()
        if entry.get("source") == "neetcode"
    }

    missing = [p for p in nc_problems if p["leetcodeUrl"] not in repo_nc_urls]
    by_pattern: dict[str, list[dict]] = defaultdict(list)
    for problem in missing:
        by_pattern[problem["pattern"]].append(problem)

    print_header(f"NeetCode problems NOT in repo ({len(missing)} total)")
    for pattern in sorted(by_pattern, key=lambda p: -len(by_pattern[p])):
        entries = by_pattern[pattern]
        nc150_count = sum(1 for p in entries if p["isNC150"])
        print(f"  {len(entries):3d}  {pattern}  (NC150: {nc150_count})")
        if show_all:
            for p in sorted(entries, key=lambda x: not x["isNC150"]):
                marker = "★" if p["isNC150"] else " "
                print(f"       {marker} {p['difficulty']:6s} {p['name']}")


def extras_section(state: dict, extras: dict) -> None:
    extras_problems = extras["problems"]
    by_pattern = Counter(p["pattern"] for p in extras_problems)

    print_header(f"Repo extras — {len(extras_problems)} problems outside NeetCode")
    for pattern, count in by_pattern.most_common():
        print(f"  {count:3d}  {pattern}")


def skipped_section(state: dict, show_all: bool) -> None:
    tracked_files = {entry["javaFile"] for entry in state["problems"].values()}
    tracked_files_set = set(tracked_files)

    all_java: list[Path] = sorted(JAVA_ROOT.rglob("*.java"))
    skipped: list[str] = []
    for path in all_java:
        rel = path.relative_to(REPO_ROOT).as_posix()
        if rel not in tracked_files_set:
            skipped.append(rel)

    print_header(f"Repo files not tracked ({len(skipped)}) — helpers / drivers / non-problems")
    by_dir = Counter(rel.rsplit("/", 1)[0] for rel in skipped)
    for directory, count in by_dir.most_common():
        print(f"  {count:3d}  {directory}")
    if show_all:
        print()
        for rel in skipped:
            print(f"    {rel}")


def stagger_section(state: dict, today: date) -> None:
    counts: Counter[str] = Counter()
    for entry in state["problems"].values():
        due = parse_date(entry["sm2"]["nextDue"])
        if due is None:
            continue
        counts[due.isoformat()] += 1

    print_header("Initial nextDue distribution (per date)")
    for iso in sorted(counts):
        due_date = date.fromisoformat(iso)
        weekday = due_date.strftime("%a")
        bar = "▮" * min(counts[iso], 60)
        rel = (due_date - today).days
        print(f"  {iso}  ({weekday})  +{rel:>4}d  {counts[iso]:4d}  {bar}")


def queue_preview_section(state: dict, today: date) -> None:
    review_day = coming_saturday(today)
    queue = pick_queue(state, review_day, QUEUE_SIZE)
    due_count = sum(
        1 for e in state["problems"].values()
        if e.get("sm2", {}).get("nextDue")
        and date.fromisoformat(e["sm2"]["nextDue"]) <= review_day
        and not e.get("flags", {}).get("skip")
    )

    print_header(f"Queue preview for {review_day.isoformat()} "
                 f"({review_day.strftime('%A')})")
    print(f"  {due_count} problems due · picking top {QUEUE_SIZE}"
          f"{' (backfilled from pinned anchors)' if due_count < QUEUE_SIZE else ''}\n")
    for entry in queue:
        pattern = entry["pattern"][:24]
        diff = entry.get("difficulty", "Unknown")
        markers = []
        if entry.get("isNC150"): markers.append("★")
        if entry.get("flags", {}).get("pinned"): markers.append("📌")
        marker_str = "".join(markers) or " "
        print(f"  {marker_str:3s} {entry['task']:35s}  {diff:6s}  {pattern}")


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--missing", action="store_true",
                        help="List every NeetCode problem missing from the repo")
    parser.add_argument("--skipped", action="store_true",
                        help="List every skipped file")
    args = parser.parse_args()

    neetcode = load_json(NEETCODE_JSON)
    state = load_json(STATE_JSON)
    extras = load_json(EXTRAS_JSON)

    today = date.today()

    coverage_section(neetcode, state)
    missing_nc_section(neetcode, state, args.missing)
    extras_section(state, extras)
    skipped_section(state, args.skipped)
    stagger_section(state, today)
    queue_preview_section(state, today)
    print()
    return 0


if __name__ == "__main__":
    sys.exit(main())

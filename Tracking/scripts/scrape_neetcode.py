#!/usr/bin/env python3
"""
scrape_neetcode.py — one-shot scraper for the NeetCode problem taxonomy.

Pulls the canonical JSON that powers neetcode.io directly from the
neetcode-gh/leetcode GitHub repo (which is the site's own source of truth),
normalizes it into our schema, and writes Tracking/data/neetcode.json.

Run this once, then commit the output. Only re-run when you want to refresh
the taxonomy (e.g., new problems added to NeetCode 250). The output is
considered frozen between runs.

Usage:
    python Tracking/scripts/scrape_neetcode.py
"""

from __future__ import annotations

import json
import sys
import urllib.request
from datetime import date
from pathlib import Path

SOURCE_URL = (
    "https://raw.githubusercontent.com/neetcode-gh/leetcode/main/"
    ".problemSiteData.json"
)
LEETCODE_BASE = "https://leetcode.com/problems/"

REPO_ROOT = Path(__file__).resolve().parents[2]
OUTPUT_PATH = REPO_ROOT / "Tracking" / "data" / "neetcode.json"

# Non-DSA patterns to exclude (irrelevant for a Java DSA tracker).
EXCLUDED_PATTERNS = {"JavaScript"}


def fetch_source() -> list[dict]:
    print(f"Fetching {SOURCE_URL} ...")
    with urllib.request.urlopen(SOURCE_URL, timeout=30) as response:
        payload = response.read().decode("utf-8")
    problems = json.loads(payload)
    if not isinstance(problems, list):
        raise RuntimeError("Unexpected payload shape — expected a JSON array.")
    print(f"  fetched {len(problems)} raw entries")
    return problems


def normalize(raw: list[dict]) -> list[dict]:
    normalized: list[dict] = []
    for entry in raw:
        pattern = entry.get("pattern", "").strip()
        if pattern in EXCLUDED_PATTERNS:
            continue

        link = entry.get("link", "").strip().strip("/")
        if not link:
            continue

        leetcode_url = f"{LEETCODE_BASE}{link}/"
        code_slug = entry.get("code", "")
        leetcode_number = None
        if code_slug and code_slug[:4].isdigit():
            leetcode_number = int(code_slug[:4])

        normalized.append(
            {
                "id": link,
                "name": entry.get("problem", "").strip(),
                "leetcodeUrl": leetcode_url,
                "leetcodeNumber": leetcode_number,
                "pattern": pattern,
                "difficulty": entry.get("difficulty", "").strip(),
                "isNC150": bool(entry.get("neetcode150")),
                "isBlind75": bool(entry.get("blind75")),
            }
        )

    normalized.sort(key=lambda p: (p["pattern"], p["leetcodeNumber"] or 0))
    return normalized


def write_output(problems: list[dict]) -> None:
    OUTPUT_PATH.parent.mkdir(parents=True, exist_ok=True)

    payload = {
        "generated_at": date.today().isoformat(),
        "source": "https://github.com/neetcode-gh/leetcode/blob/main/.problemSiteData.json",
        "excludedPatterns": sorted(EXCLUDED_PATTERNS),
        "counts": {
            "total": len(problems),
            "nc150": sum(1 for p in problems if p["isNC150"]),
            "blind75": sum(1 for p in problems if p["isBlind75"]),
        },
        "problems": problems,
    }

    OUTPUT_PATH.write_text(json.dumps(payload, indent=2) + "\n")
    print(f"  wrote {OUTPUT_PATH.relative_to(REPO_ROOT)}")


def print_summary(problems: list[dict]) -> None:
    by_pattern: dict[str, int] = {}
    for p in problems:
        by_pattern[p["pattern"]] = by_pattern.get(p["pattern"], 0) + 1

    print()
    print(f"NeetCode taxonomy snapshot ({len(problems)} problems):")
    print(f"  NC150   : {sum(1 for p in problems if p['isNC150']):3d}")
    print(f"  Blind75 : {sum(1 for p in problems if p['isBlind75']):3d}")
    print(f"  patterns: {len(by_pattern)}")
    print()
    for pattern, count in sorted(by_pattern.items(), key=lambda kv: -kv[1]):
        print(f"    {count:3d}  {pattern}")


def main() -> int:
    try:
        raw = fetch_source()
    except Exception as exc:
        print(f"ERROR: could not fetch NeetCode source: {exc}", file=sys.stderr)
        return 1

    problems = normalize(raw)
    write_output(problems)
    print_summary(problems)
    return 0


if __name__ == "__main__":
    sys.exit(main())

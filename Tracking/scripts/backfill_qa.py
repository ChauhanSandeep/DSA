#!/usr/bin/env python3
"""
backfill_qa.py — populate state.json[*].qa from the legacy sheet + Javadocs.

Content priority (highest first):
    1. "user"      — existing entries with qa.source == "user" are never touched.
    2. "sheet"     — parsed from Tracking/Problems.html rows keyed by class name.
    3. "javadoc"   — auto-extracted from the Java file's Javadoc.
    4. "missing"   — nothing found; the card will render a "(no summary yet)"
                    placeholder.

Idempotent. Safe to re-run. Prints coverage stats.

Usage:
    python Tracking/scripts/backfill_qa.py
    python Tracking/scripts/backfill_qa.py --dry-run
"""

from __future__ import annotations

import argparse
import html
import json
import re
import sys
from collections import Counter
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from _javadoc import (  # noqa: E402
    extract_javadoc_blocks,
    parse_class_javadoc,
    parse_method_javadoc,
)


REPO_ROOT = Path(__file__).resolve().parents[2]
DATA_DIR = REPO_ROOT / "Tracking" / "data"
STATE_JSON = DATA_DIR / "state.json"
LEGACY_SHEET = REPO_ROOT / "Tracking" / "Problems.html"


# ---------------------------------------------------------------------------
# Sheet parsing
# ---------------------------------------------------------------------------

TR_RE = re.compile(r"<tr[^>]*>(.*?)</tr>", re.DOTALL | re.IGNORECASE)
TD_RE = re.compile(r"<t[dh][^>]*>(.*?)</t[dh]>", re.DOTALL | re.IGNORECASE)
TAG_RE = re.compile(r"<[^>]+>")


def clean_cell(raw: str) -> str:
    text = TAG_RE.sub("", raw)
    text = html.unescape(text)
    text = text.strip()
    text = re.sub(r"[ \t]+", " ", text)
    text = re.sub(r"\n{3,}", "\n\n", text)
    return text


def parse_sheet(path: Path) -> dict[str, dict]:
    """Return { className: {problem, answer, difficulty, notes} } from
    the exported Google Sheet HTML.

    Column layout observed:
        col 1 : class name (Task)
        col 3 : Problem Statement
        col 4 : Answer
        col 5 : Link
        col 6 : Difficulty
        col 7 : Last Revised Date
        col 8 : Notes
    (col 2 is a spacer.)
    """
    if not path.exists():
        return {}

    html_text = path.read_text(encoding="utf-8", errors="replace")
    rows = TR_RE.findall(html_text)

    entries: dict[str, dict] = {}
    for row in rows:
        cells_raw = TD_RE.findall(row)
        if len(cells_raw) < 9:
            continue
        cells = [clean_cell(c) for c in cells_raw]
        task = cells[1]
        if not task or task.lower() == "task":
            continue
        problem = cells[3]
        answer = cells[4]
        difficulty = cells[6] if len(cells) > 6 else ""
        notes = cells[8] if len(cells) > 8 else ""

        # First occurrence wins if duplicates exist in the sheet.
        entries.setdefault(task, {
            "problem": problem,
            "answer": answer,
            "difficulty": difficulty,
            "notes": notes,
        })
    return entries


# ---------------------------------------------------------------------------
# Javadoc extraction (fallback path)
# ---------------------------------------------------------------------------

def extract_qa_from_source(source: str) -> tuple[str | None, str | None, str | None, str]:
    """Return (problem, answer, complexity, source_tag).

    source_tag is one of: 'javadoc-teach' (rich Intuition available),
    'javadoc' (first-paragraph fallback), or 'missing' (nothing found).
    """
    blocks = extract_javadoc_blocks(source)
    if not blocks:
        return None, None, None, "missing"

    cls = parse_class_javadoc(blocks[0])
    problem_text = cls.get("problem")
    if problem_text:
        # Split "Title\n\nBody..." pattern — keep the body only.
        parts = problem_text.split("\n\n", 1)
        problem_text = parts[1].strip() if len(parts) == 2 else problem_text.strip()

    # Method-level teaching text.
    answer_text: str | None = None
    complexity_text: str | None = None
    source_tag = "javadoc"

    if len(blocks) > 1:
        method = parse_method_javadoc(blocks[1])
        intuition = (method.get("intuition") or "").strip()
        first_para = (method.get("first_paragraph") or "").strip()

        if intuition and len(intuition) >= 180:
            answer_text = intuition
            source_tag = "javadoc-teach"
        elif intuition:
            answer_text = intuition
        elif first_para:
            answer_text = first_para

        time_line = (method.get("time") or "").strip()
        space_line = (method.get("space") or "").strip()
        if time_line and space_line:
            complexity_text = f"Time: {time_line} · Space: {space_line}"
        elif time_line:
            complexity_text = f"Time: {time_line}"
        elif space_line:
            complexity_text = f"Space: {space_line}"

    if not problem_text and not answer_text:
        return None, None, None, "missing"

    return problem_text, answer_text, complexity_text, source_tag


# ---------------------------------------------------------------------------
# Backfill
# ---------------------------------------------------------------------------

def truncate(s: str, limit: int) -> str:
    if not s:
        return s
    if len(s) <= limit:
        return s
    return s[: limit - 1].rstrip() + "…"


def backfill_qa(state: dict, sheet: dict[str, dict]) -> Counter[str]:
    counts: Counter[str] = Counter()

    for task_id, entry in state["problems"].items():
        qa = entry.get("qa") or {}
        if qa.get("source") == "user":
            counts["user"] += 1
            continue

        class_name = entry.get("className", task_id)
        sheet_row = sheet.get(class_name)
        if sheet_row and (sheet_row["problem"] or sheet_row["answer"]):
            entry["qa"] = {
                "problem": sheet_row["problem"] or None,
                "answer": sheet_row["answer"] or None,
                "complexity": None,
                "source": "sheet",
            }
            counts["sheet"] += 1
            continue

        # Javadoc fallback
        java_path = REPO_ROOT / entry["javaFile"]
        problem_txt = answer_txt = complexity_txt = None
        source_tag = "missing"
        if java_path.exists():
            try:
                source = java_path.read_text(encoding="utf-8", errors="replace")
                problem_txt, answer_txt, complexity_txt, source_tag = \
                    extract_qa_from_source(source)
            except Exception:
                pass

        if problem_txt or answer_txt:
            entry["qa"] = {
                "problem": problem_txt,
                "answer": answer_txt,
                "complexity": complexity_txt,
                "source": source_tag,
            }
            counts[source_tag] += 1
        else:
            entry["qa"] = {
                "problem": None,
                "answer": None,
                "complexity": None,
                "source": "missing",
            }
            counts["missing"] += 1

    return counts


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--dry-run", action="store_true",
                        help="print counts and preview, don't write state.json")
    parser.add_argument("--sample", type=int, default=0,
                        help="print N random sample cards after backfill")
    args = parser.parse_args()

    if not STATE_JSON.exists():
        print("ERROR: state.json missing. Run sync.py first.", file=sys.stderr)
        return 1

    state = json.loads(STATE_JSON.read_text())

    print(f"Parsing legacy sheet: {LEGACY_SHEET.relative_to(REPO_ROOT)}")
    sheet = parse_sheet(LEGACY_SHEET)
    print(f"  {len(sheet)} sheet rows with class-name keys")
    if not sheet:
        print("  (sheet missing or empty — will use Javadoc fallback for everything)")

    counts = backfill_qa(state, sheet)
    total = sum(counts.values())

    print()
    print(f"Backfill coverage over {total} problems:")
    for source in ("user", "sheet", "javadoc-teach", "javadoc", "missing"):
        n = counts.get(source, 0)
        pct = (n * 100 // total) if total else 0
        print(f"  {source:8s}  {n:4d}  ({pct:3d}%)")

    if args.dry_run:
        print("\n[dry-run] state.json not written.")
    else:
        STATE_JSON.write_text(json.dumps(state, indent=2, ensure_ascii=False) + "\n")
        print(f"\nwrote {STATE_JSON.relative_to(REPO_ROOT)}")

    if args.sample:
        import random
        random.seed(7)
        sampled = random.sample(list(state["problems"].values()), min(args.sample, len(state["problems"])))
        print()
        print(f"--- {len(sampled)} sample cards ---")
        for entry in sampled:
            qa = entry.get("qa") or {}
            print(f"\n[{qa.get('source','?')}] {entry['task']} ({entry.get('problemName') or entry['task']})")
            print(f"  problem: {truncate(qa.get('problem') or '(none)', 120)}")
            print(f"  answer : {truncate(qa.get('answer') or '(none)', 120)}")
    return 0


if __name__ == "__main__":
    sys.exit(main())

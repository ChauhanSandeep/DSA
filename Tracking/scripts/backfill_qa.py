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

JAVADOC_RE = re.compile(r"/\*\*(.*?)\*/", re.DOTALL)
CLASS_DECL_RE = re.compile(
    r"^\s*public\s+(?:final\s+|abstract\s+)?(?:class|interface|enum)\s+\w+",
    re.MULTILINE,
)
INTUITION_MARKERS = [
    r"🧠\s*Intuition[:：]?",
    r"Intuition[:：]",
    r"---\s*Intuition\s*---",
    r"Approach[:：]",
    r"---\s*Approach\s*---",
]
PROBLEM_MARKERS = [
    r"✅\s*Problem[:：]?\s*",
    r"Problem[:：]\s*",
    r"---\s*Problem\s+Description\s*---\s*",
]
COMPLEXITY_RE = re.compile(
    r"Time(?:\s+[Cc]omplexity)?[:：]\s*([^\n]+).*?Space(?:\s+[Cc]omplexity)?[:：]\s*([^\n]+)",
    re.DOTALL,
)


def clean_javadoc(raw: str) -> str:
    lines = []
    for line in raw.splitlines():
        s = line.strip()
        if s.startswith("*"):
            s = s[1:].lstrip(" ")
        lines.append(s)
    while lines and not lines[0]:
        lines.pop(0)
    while lines and not lines[-1]:
        lines.pop()
    return "\n".join(lines).strip()


def first_paragraph(text: str) -> str:
    text = text.strip()
    if not text:
        return ""
    # Split on blank line.
    para = re.split(r"\n\s*\n", text, maxsplit=1)[0]
    return para.strip()


def extract_after_marker(text: str, markers: list[str], stop_re: str = r"(?:\n\s*\n|\n\s*[🔗🏷🧪🚧🔍🔁📌✅🧠]|\n\s*(?:Time|Space|Example|Algorithm|Complexity|Constraints|Follow[- ]?up|Related)\s*[:：])") -> str | None:
    for m in markers:
        found = re.search(m, text)
        if not found:
            continue
        after = text[found.end():]
        stop = re.search(stop_re, after)
        chunk = after[: stop.start()] if stop else after
        chunk = chunk.strip()
        if chunk:
            # Return up to first paragraph if multi-paragraph.
            return first_paragraph(chunk)
    return None


def extract_qa_from_source(source: str) -> tuple[str | None, str | None, str | None]:
    """Return (problem, answer, complexity) from Java source's Javadocs."""
    docs = JAVADOC_RE.findall(source)
    if not docs:
        return None, None, None

    class_doc = clean_javadoc(docs[0])
    method_doc = clean_javadoc(docs[1]) if len(docs) > 1 else ""

    # Problem
    problem = extract_after_marker(class_doc, PROBLEM_MARKERS)
    if not problem:
        # Fallback: first paragraph of the class Javadoc, minus a title line.
        cd_lines = class_doc.splitlines()
        # Skip a leading title-ish line (single line, no period)
        if cd_lines and len(cd_lines[0]) < 80 and "." not in cd_lines[0]:
            body = "\n".join(cd_lines[1:]).lstrip()
        else:
            body = class_doc
        problem = first_paragraph(body) or None

    # Answer
    answer = extract_after_marker(method_doc, INTUITION_MARKERS)
    if not answer and method_doc:
        # Fallback: first paragraph of the method Javadoc.
        answer = first_paragraph(method_doc) or None

    # Complexity — best effort from either doc
    combined = f"{method_doc}\n{class_doc}"
    m = COMPLEXITY_RE.search(combined)
    complexity = None
    if m:
        t, s = m.group(1).strip(), m.group(2).strip()
        complexity = f"Time: {t} · Space: {s}"

    return problem, answer, complexity


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
        if java_path.exists():
            try:
                source = java_path.read_text(encoding="utf-8", errors="replace")
                problem_txt, answer_txt, complexity_txt = extract_qa_from_source(source)
            except Exception:
                pass

        if problem_txt or answer_txt:
            entry["qa"] = {
                "problem": problem_txt,
                "answer": answer_txt,
                "complexity": complexity_txt,
                "source": "javadoc",
            }
            counts["javadoc"] += 1
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
    for source in ("user", "sheet", "javadoc", "missing"):
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

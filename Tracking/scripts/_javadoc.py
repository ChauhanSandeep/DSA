#!/usr/bin/env python3
"""
_javadoc.py — parsers for class + method Javadocs following AGENT.md.

Two entry points:

    parse_class_javadoc(text)  -> dict
        Splits the CLEANED class-Javadoc text (no leading `*`) into the
        canonical AGENT.md sections plus a fallback `body` for anything
        we couldn't classify. Returns None for any section that doesn't
        appear. Robust against files that only partially conform:
        missing sections become None; anything before the first heading
        lands in `preamble`; anything after the last recognised heading
        lands in `tail`.

    parse_method_javadoc(text) -> dict
        Extracts Intuition (or the first prose paragraph if no explicit
        heading), Time, Space. Method-level fields.

Both accept RAW Javadoc text OR the interior of a Javadoc block; they
strip `*` prefixes themselves.

None of this module does I/O. Everything is text -> dict.
"""

from __future__ import annotations

import re
from typing import Iterable


# ---------------------------------------------------------------------------
# Cleaning
# ---------------------------------------------------------------------------

JAVADOC_INTERIOR_RE = re.compile(r"/\*\*(.*?)\*/", re.DOTALL)


def _strip_stars(raw: str) -> str:
    lines: list[str] = []
    for line in raw.splitlines():
        s = line.strip()
        if s.startswith("*"):
            s = s[1:]
            if s.startswith(" "):
                s = s[1:]
        lines.append(s)
    while lines and not lines[0].strip():
        lines.pop(0)
    while lines and not lines[-1].strip():
        lines.pop()
    return "\n".join(lines)


def extract_javadoc_blocks(source: str) -> list[str]:
    """Return each `/** ... */` interior in the given Java source, cleaned
    (star-prefixes stripped)."""
    return [_strip_stars(m.group(1)) for m in JAVADOC_INTERIOR_RE.finditer(source)]


# ---------------------------------------------------------------------------
# Class-Javadoc parser
# ---------------------------------------------------------------------------

# Heading regexes: match the "Section:" tokens per AGENT.md, at the very start
# of a line (case-insensitive). Note `Approach` doesn't have a colon in the
# actual usage — it's the header row of the approach table.
_HEADINGS: list[tuple[str, re.Pattern[str]]] = [
    ("problem",     re.compile(r"^Problem\s*:\s*", re.IGNORECASE)),
    ("leetcode",    re.compile(r"^(?:Leetcode|Link)\s*:\s*", re.IGNORECASE)),
    ("rating",      re.compile(r"^Rating\s*:\s*", re.IGNORECASE)),
    ("pattern",     re.compile(r"^Pattern\s*:\s*", re.IGNORECASE)),
    ("example",     re.compile(r"^Example\s*:\s*", re.IGNORECASE)),
    ("followups",   re.compile(r"^Follow[- ]?ups?\s*:\s*", re.IGNORECASE)),
    ("related",     re.compile(r"^Related\s*:\s*", re.IGNORECASE)),
    ("approach",    re.compile(r"^Approach\b", re.IGNORECASE)),
]


def _find_heading_line(line: str) -> tuple[str, str] | None:
    """Return (name, remaining_after_marker) if this line starts a section.
    remaining_after_marker is the same line minus the "Name:" prefix."""
    for name, pattern in _HEADINGS:
        m = pattern.match(line)
        if m:
            return name, line[m.end():]
    return None


def parse_class_javadoc(text: str) -> dict:
    """Split class Javadoc text into canonical sections.

    Returns a dict with keys:
        problem, leetcode, rating, pattern, example, followups, related,
        approach, preamble, tail
    Every key holds the raw text of the section (multi-line, dedented)
    or None if absent. `preamble` catches leading text before any heading
    (rare) and `tail` catches trailing prose not attached to a section.
    """
    text = _strip_stars(text)

    sections: dict[str, list[str]] = {name: None for name, _ in _HEADINGS}
    sections["preamble"] = None
    sections["tail"] = None

    current_key: str | None = None
    current_buf: list[str] = []
    preamble_buf: list[str] = []

    def flush() -> None:
        nonlocal current_key, current_buf
        if current_key is not None:
            joined = "\n".join(current_buf).rstrip()
            if joined:
                sections[current_key] = joined
            current_buf = []

    lines = text.splitlines()
    for line in lines:
        heading = _find_heading_line(line.lstrip())
        if heading:
            name, rest = heading
            flush()
            current_key = name
            if rest.strip():
                current_buf.append(rest.rstrip())
        else:
            if current_key is None:
                if line.strip():
                    preamble_buf.append(line.rstrip())
            else:
                # Dedent by 2 spaces if all non-empty section lines start with them;
                # otherwise keep as-is. Keeps `Input:` / `Output:` block visually
                # aligned but doesn't force indentation everywhere.
                current_buf.append(line.rstrip())

    flush()
    if preamble_buf:
        sections["preamble"] = "\n".join(preamble_buf).strip()

    # `problem` frequently continues onto multiple lines; keep it as-is but
    # normalize the leading blank line (some files put the title on the
    # Problem: line and the body on the next).
    for key in ("problem", "example", "followups", "related", "pattern",
                "leetcode", "rating", "approach"):
        if sections.get(key):
            sections[key] = _dedent_block(sections[key])

    return sections


def _dedent_block(s: str) -> str:
    """Remove a common leading whitespace prefix from non-empty lines."""
    lines = s.splitlines()
    non_empty = [ln for ln in lines if ln.strip()]
    if not non_empty:
        return s
    common = min(
        (len(ln) - len(ln.lstrip(" "))) for ln in non_empty
    )
    if common == 0:
        return s
    return "\n".join(ln[common:] if len(ln) >= common else ln for ln in lines).strip("\n")


# ---------------------------------------------------------------------------
# Sub-parsers for specific sections
# ---------------------------------------------------------------------------

LEETCODE_URL_RE = re.compile(r"(https?://leetcode\.com/problems/[a-z0-9\-]+/?)", re.IGNORECASE)


def parse_leetcode_section(section: str | None) -> dict:
    """From a Leetcode section like
        'https://leetcode.com/problems/two-sum/ (Easy)'
    return {url, difficulty} (either can be None)."""
    if not section:
        return {"url": None, "difficulty": None}
    url_match = LEETCODE_URL_RE.search(section)
    url = url_match.group(1) if url_match else None
    diff = None
    diff_match = re.search(r"\(\s*(Easy|Medium|Hard)\s*\)", section, re.IGNORECASE)
    if diff_match:
        diff = diff_match.group(1).title()
    return {"url": url, "difficulty": diff}


# `Rating:` line forms observed in the corpus:
#   Rating: zerotrac 1685 (Q3, weekly-263)
#   Rating: acceptance 61.6% (Medium) - no contest Elo (pre-contest problem)
#   Rating: not available
_RATING_ELO_RE = re.compile(
    r"zerotrac\s+(?P<elo>\d{3,5})"
    r"(?:\s*\((?P<meta>[^)]*)\))?",
    re.IGNORECASE,
)
_RATING_ACC_RE = re.compile(
    r"acceptance\s+(?P<acc>[\d.]+)\s*%"
    r"(?:\s*\((?P<band>Easy|Medium|Hard)\))?",
    re.IGNORECASE,
)


def parse_rating_section(section: str | None) -> dict:
    """Return {elo, acceptance, label, meta, difficulty} — any of which can
    be None. `label` is a short human-readable rendering."""
    out: dict = {"elo": None, "acceptance": None, "label": None,
                 "meta": None, "difficulty": None}
    if not section:
        return out
    line = section.strip()
    if line.lower().startswith("not available") or "n/a" in line.lower():
        out["label"] = "not available"
        return out

    elo_m = _RATING_ELO_RE.search(line)
    if elo_m:
        try:
            out["elo"] = int(elo_m.group("elo"))
        except (TypeError, ValueError):
            pass
        if elo_m.group("meta"):
            out["meta"] = elo_m.group("meta").strip()

    acc_m = _RATING_ACC_RE.search(line)
    if acc_m:
        try:
            out["acceptance"] = float(acc_m.group("acc"))
        except (TypeError, ValueError):
            pass
        if acc_m.group("band"):
            out["difficulty"] = acc_m.group("band").title()

    parts: list[str] = []
    if out["elo"] is not None:
        parts.append(f"zerotrac {out['elo']}")
        if out["meta"]:
            parts.append(out["meta"])
    if out["acceptance"] is not None:
        parts.append(f"acceptance {out['acceptance']:.1f}%")
        if out["difficulty"]:
            parts.append(out["difficulty"])
    if parts:
        out["label"] = " · ".join(parts)
    return out


# Elo band cutoffs and acceptance-band cutoffs per PLAN_JAVADOC_UPGRADE decisions.
ELO_BANDS = [(1400, "Easy"), (1800, "Medium"), (2200, "Hard"), (10000, "Very Hard")]
ACC_BANDS = [(40.0, "Hard"), (60.0, "Medium"), (100.0, "Easy")]


def rating_band(rating: dict, fallback: str | None = None) -> str:
    """Derive a coarse band label from a parsed rating dict.

    Priority: elo > acceptance > explicit difficulty in rating > fallback.
    """
    if rating.get("elo") is not None:
        for cutoff, name in ELO_BANDS:
            if rating["elo"] < cutoff:
                return name
    if rating.get("acceptance") is not None:
        acc = rating["acceptance"]
        for cutoff, name in ACC_BANDS:
            if acc < cutoff:
                return name
    if rating.get("difficulty"):
        return rating["difficulty"]
    return fallback or "Unknown"


# ---------------------------------------------------------------------------
# Follow-ups parser
# ---------------------------------------------------------------------------

FOLLOWUP_NUMBER_RE = re.compile(r"^\s*(\d+)\.\s+(.*)$")


def parse_followups(section: str | None) -> list[dict]:
    """Return a list of {question, answer} pairs, in order.

    A follow-up is a numbered bullet whose first line is the question, followed
    by zero or more indented lines that form the answer. Blank lines terminate
    the current item.
    """
    if not section:
        return []
    items: list[dict] = []
    current: dict | None = None
    for line in section.splitlines():
        m = FOLLOWUP_NUMBER_RE.match(line)
        if m:
            if current:
                items.append(current)
            current = {"question": m.group(2).strip(), "answer": ""}
        elif current is not None:
            if not line.strip():
                if current["answer"]:
                    current["answer"] += "\n"
                continue
            addition = line.strip()
            current["answer"] += (" " if current["answer"] else "") + addition
    if current:
        items.append(current)
    return items


# ---------------------------------------------------------------------------
# Related parser
# ---------------------------------------------------------------------------

RELATED_ITEM_RE = re.compile(r"([0-9A-Za-z][A-Za-z0-9\-'/ &+]*?)\s*\((\d{1,5})\)")


def parse_related(section: str | None) -> list[dict]:
    """Return [{name, number}, ...] from a `Related:` section like
        'Subsets (78), Combination Sum II (40), Permutations II (47).'
    """
    if not section:
        return []
    # Split on commas or periods first so multi-item lines don't merge.
    fragments = re.split(r"[,·.]", section)
    out: list[dict] = []
    for frag in fragments:
        m = RELATED_ITEM_RE.search(frag)
        if not m:
            continue
        name = m.group(1).strip()
        try:
            num = int(m.group(2))
        except ValueError:
            continue
        if name:
            out.append({"name": name, "number": num})
    return out


# ---------------------------------------------------------------------------
# Example parser
# ---------------------------------------------------------------------------

def parse_example(section: str | None) -> dict:
    """From an Example block like
        Input:  nums = [2,7,11,15], target = 9
        Output: [0,1]
        Why:    nums[0] + nums[1] = 2 + 7 = 9.
    return {input, output, why} — any can be None if not present.
    """
    if not section:
        return {"input": None, "output": None, "why": None}
    fields = {"input": None, "output": None, "why": None}
    # Extract by regex over the whole block.
    for key, pattern in (
        ("input",  re.compile(r"Input\s*:\s*(.*?)(?=(?:\n\s*(?:Output|Why|Explanation)\s*:|\Z))", re.DOTALL | re.IGNORECASE)),
        ("output", re.compile(r"Output\s*:\s*(.*?)(?=(?:\n\s*(?:Input|Why|Explanation)\s*:|\Z))", re.DOTALL | re.IGNORECASE)),
        ("why",    re.compile(r"(?:Why|Explanation)\s*:\s*(.*?)\Z", re.DOTALL | re.IGNORECASE)),
    ):
        m = pattern.search(section)
        if m:
            text = _collapse_ws(m.group(1))
            fields[key] = text or None
    return fields


def _collapse_ws(s: str) -> str:
    return " ".join(s.split()).strip()


# ---------------------------------------------------------------------------
# Method Javadoc parser
# ---------------------------------------------------------------------------

_METHOD_HEADINGS = [
    ("intuition",   re.compile(r"^Intuition\s*:?\s*", re.IGNORECASE)),
    ("algorithm",   re.compile(r"^Algorithm\s*:?\s*", re.IGNORECASE)),
    ("time",        re.compile(r"^Time\s*:\s*", re.IGNORECASE)),
    ("space",       re.compile(r"^Space\s*:\s*", re.IGNORECASE)),
    ("returns",     re.compile(r"^@return\s+", re.IGNORECASE)),
    ("param",       re.compile(r"^@param\s+", re.IGNORECASE)),
]


def parse_method_javadoc(text: str) -> dict:
    """Return {intuition, algorithm, time, space, first_paragraph}."""
    text = _strip_stars(text)

    out: dict = {"intuition": None, "algorithm": None,
                 "time": None, "space": None, "first_paragraph": None}

    lines = text.splitlines()
    current = None
    buf: list[str] = []

    def flush() -> None:
        nonlocal current, buf
        if current is not None and buf:
            out[current] = _clean_multiline("\n".join(buf))
            buf = []
        elif current is not None:
            buf = []

    for line in lines:
        matched = None
        for name, pattern in _METHOD_HEADINGS:
            m = pattern.match(line.lstrip())
            if m:
                matched = (name, line.lstrip()[m.end():])
                break
        if matched:
            flush()
            current, rest = matched
            if rest.strip():
                buf.append(rest.rstrip())
        else:
            if current is None:
                # collecting first paragraph
                if line.strip():
                    buf.append(line.rstrip())
                elif buf:
                    # blank line ends the first paragraph
                    if out["first_paragraph"] is None:
                        out["first_paragraph"] = _collapse_ws("\n".join(buf))
                        buf = []
            else:
                buf.append(line.rstrip())
    flush()

    # If we never hit a heading but did buffer prose, that's the first paragraph.
    if out["first_paragraph"] is None and buf:
        out["first_paragraph"] = _collapse_ws("\n".join(buf))

    # Compact multi-line Time/Space to one line.
    for k in ("time", "space"):
        if out[k]:
            out[k] = _collapse_ws(out[k])

    return out


def _clean_multiline(s: str) -> str:
    lines = [ln.rstrip() for ln in s.splitlines()]
    while lines and not lines[0].strip():
        lines.pop(0)
    while lines and not lines[-1].strip():
        lines.pop()
    # Collapse runs of blank lines to at most one.
    cleaned: list[str] = []
    prev_blank = False
    for ln in lines:
        if not ln.strip():
            if prev_blank:
                continue
            prev_blank = True
        else:
            prev_blank = False
        cleaned.append(ln)
    return "\n".join(cleaned)

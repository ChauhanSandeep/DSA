#!/usr/bin/env python3
"""
build.py — regenerate the local study dashboard from state.json + Java source.

What it emits under Tracking/site/:

    index.html                   dashboard: this weekend's queue + stats + patterns
    problems/<Task>.html         one page per tracked problem
    patterns/<Pattern>.html      per-pattern problem list
    assets/styles.css            copied from site-src/
    assets/app.js                copied from site-src/

Design notes:
- No runtime network. state.json is inlined into index.html as window.__STATE__.
- No syntax-highlighting library. Java code renders as plain <pre><code>.
- No framework. Vanilla HTML + one CSS + one JS file.
- Robust across the repo's mixed Javadoc styles (emoji per WARP.md, plain
  text, dashed sections, etc.) by extracting the raw class Javadoc and
  showing it as pre-wrapped text. No fragile section parsing.
- The site is safe to delete and rebuild at any time.

Usage:
    python Tracking/scripts/build.py

Idempotent: rerun after any repo change or sync.
"""

from __future__ import annotations

import html
import json
import re
import shutil
import sys
from collections import defaultdict
from datetime import date, datetime, timedelta
from pathlib import Path

# Local module — lives beside this file.
sys.path.insert(0, str(Path(__file__).resolve().parent))
from _queue import pick_queue, coming_saturday, DIFFICULTY_RANK  # noqa: E402


REPO_ROOT = Path(__file__).resolve().parents[2]
DATA_DIR = REPO_ROOT / "Tracking" / "data"
SITE_SRC_DIR = REPO_ROOT / "Tracking" / "site-src"
SITE_DIR = REPO_ROOT / "Tracking" / "site"

STATE_JSON = DATA_DIR / "state.json"
NEETCODE_JSON = DATA_DIR / "neetcode.json"

QUEUE_SIZE = 6
DIFFICULTY_CLASS = {
    "Easy": "diff-easy",
    "Medium": "diff-medium",
    "Hard": "diff-hard",
    "Unknown": "diff-unknown",
}

# Path of state.json relative to site/ pages (for a hint in the UI).
STATE_JSON_REL_FROM_SITE = "../data/state.json"


# ---------------------------------------------------------------------------
# Javadoc extraction
# ---------------------------------------------------------------------------

JAVADOC_BLOCK_RE = re.compile(r"/\*\*(.*?)\*/", re.DOTALL)
CLASS_DECL_RE = re.compile(r"^\s*public\s+(?:final\s+|abstract\s+)?(?:class|interface|enum)\s+\w+", re.MULTILINE)


def extract_javadoc(source: str) -> tuple[str | None, str]:
    """Return (class_javadoc, code_body).

    class_javadoc is the cleaned text content of the first /** ... */ block
    (leading `*` prefixes stripped). code_body is everything from the class
    declaration onward — the "solution" that stays hidden behind reveal.
    """
    first_doc_match = JAVADOC_BLOCK_RE.search(source)
    doc_text: str | None = None
    if first_doc_match:
        raw = first_doc_match.group(1)
        lines = []
        for line in raw.splitlines():
            stripped = line.strip()
            if stripped.startswith("*"):
                stripped = stripped[1:].lstrip(" ")
            lines.append(stripped)
        # Trim empty leading/trailing lines.
        while lines and not lines[0]:
            lines.pop(0)
        while lines and not lines[-1]:
            lines.pop()
        doc_text = "\n".join(lines)

    class_match = CLASS_DECL_RE.search(source)
    code_body = source[class_match.start():] if class_match else source

    return doc_text, code_body


# ---------------------------------------------------------------------------
# HTML rendering helpers
# ---------------------------------------------------------------------------

def esc(text: str) -> str:
    return html.escape(text or "", quote=True)


def base_layout(title: str, body: str, back_link: str = "") -> str:
    back = ""
    if back_link:
        back = f'<a href="{back_link}">← back</a>'
    return f"""<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>{esc(title)}</title>
  <link rel="stylesheet" href="{{root}}assets/styles.css">
</head>
<body>
  <div class="container">
    <header class="site-header">
      <h1><a href="{{root}}index.html" style="color:inherit">🧠 DSA · Weekend review</a></h1>
      <nav>
        {back}
        <a href="{{root}}index.html">Queue</a>
        <a href="{{root}}patterns/index.html">Patterns</a>
      </nav>
    </header>
{body}
    <div class="footer">
      Regenerate with <code>python Tracking/scripts/build.py</code> ·
      state at <code>{STATE_JSON_REL_FROM_SITE}</code>
    </div>
  </div>
  <script src="{{root}}assets/app.js"></script>
</body>
</html>
"""


def badges_for_problem(problem: dict) -> str:
    parts = []
    diff = problem.get("difficulty", "Unknown")
    parts.append(f'<span class="badge {DIFFICULTY_CLASS.get(diff, "diff-unknown")}">{esc(diff)}</span>')
    if problem.get("isNC150"):
        parts.append('<span class="badge nc150">NC150</span>')
    if problem.get("isBlind75"):
        parts.append('<span class="badge blind75">Blind 75</span>')
    if problem.get("flags", {}).get("pinned"):
        parts.append('<span class="badge pinned" title="Core anchor">📌 Core</span>')
    parts.append(f'<span class="badge pattern">{esc(problem.get("pattern", "Uncategorized"))}</span>')
    return "".join(parts)


def format_next_due(iso: str | None) -> str:
    if not iso:
        return "—"
    due = date.fromisoformat(iso)
    today = date.today()
    delta = (due - today).days
    if delta == 0:
        return "today"
    if delta == 1:
        return "tomorrow"
    if delta < 0:
        return f"{-delta} d overdue"
    if delta < 7:
        return f"in {delta} d"
    if delta < 14:
        return "next week"
    return due.isoformat()


# ---------------------------------------------------------------------------
# Page renderers
# ---------------------------------------------------------------------------

def render_dashboard(state: dict, today: date) -> str:
    review_day = coming_saturday(today)
    queue = pick_queue(state, review_day, QUEUE_SIZE)
    all_problems = list(state["problems"].values())

    total = len(all_problems)
    nc150 = sum(1 for p in all_problems if p.get("isNC150"))
    reviewed_ever = sum(1 for p in all_problems if p["sm2"].get("lastReviewed"))
    due_this_week = sum(
        1 for p in all_problems
        if p["sm2"].get("nextDue") and date.fromisoformat(p["sm2"]["nextDue"]) <= review_day
    )

    stats = f"""
    <div class="stats">
      <div class="stat"><div class="value">{total}</div><div class="label">tracked problems</div></div>
      <div class="stat"><div class="value">{nc150}</div><div class="label">NC150 in repo</div></div>
      <div class="stat"><div class="value">{due_this_week}</div><div class="label">due this Saturday</div></div>
      <div class="stat"><div class="value">{reviewed_ever}</div><div class="label">reviewed at least once</div></div>
    </div>
    """

    pending_banner = """
    <div id="pending-banner" class="pending-banner hidden">
      <span><strong><span class="count">0</span></strong> unsaved review(s). Save to <code>Tracking/data/state.json</code> before committing.</span>
      <button data-action="save-all">Save all</button>
    </div>
    """

    if queue:
        queue_html_parts = []
        for index, problem in enumerate(queue, 1):
            task = problem["task"]
            queue_html_parts.append(f"""
      <div class="queue-item">
        <div class="index">{index:02d}</div>
        <div>
          <div class="title"><a href="problems/{esc(task)}.html">{esc(problem.get("problemName") or task)}</a></div>
          <div class="meta">{esc(task)} · next due {esc(format_next_due(problem["sm2"].get("nextDue")))}</div>
        </div>
        <div class="badges">{badges_for_problem(problem)}</div>
      </div>""")
        queue_html = "\n".join(queue_html_parts)
    else:
        queue_html = '<p class="dim">Nothing due this Saturday — the queue picker will fill from least-recently-reviewed on the actual review day.</p>'

    review_day_str = review_day.strftime("%A, %d %b %Y")

    body = f"""
    {pending_banner}
    {stats}

    <h2>Queue for {esc(review_day_str)}</h2>
    <div class="queue">{queue_html}</div>

    <h2>Actions</h2>
    <p class="dim">
      Grade problems from their individual pages (keys <code>1/2/3/4</code>).
      Changes are persisted to <code>{STATE_JSON_REL_FROM_SITE}</code> via the
      File System Access API, or you can
      <a href="#" data-action="download-state">download state.json</a>
      manually if your browser doesn't support it.
    </p>

    <h2>Patterns</h2>
    <div class="pattern-grid" id="pattern-grid"></div>
    """

    # Inject state as JS BEFORE app.js runs. Do it via a dedicated <script>
    # placed just before the app.js include tag isn't possible with base_layout;
    # instead we append an inline script that sets window.__STATE__ and then
    # rely on DOMContentLoaded ordering (app.js also waits for DOMContentLoaded).
    inline_state = json.dumps(state, ensure_ascii=False)
    inline = f"""
    <script>window.__STATE__ = {inline_state};</script>
    <script>
      // Render pattern grid client-side using the injected state.
      document.addEventListener("DOMContentLoaded", function () {{
        var grid = document.getElementById("pattern-grid");
        if (!grid) return;
        var counts = {{}};
        var problems = window.__STATE__.problems;
        Object.keys(problems).forEach(function (task) {{
          var p = problems[task];
          var pattern = p.pattern || "Uncategorized";
          counts[pattern] = (counts[pattern] || 0) + 1;
        }});
        var ordered = Object.keys(counts).sort(function (a, b) {{
          return counts[b] - counts[a];
        }});
        ordered.forEach(function (pattern) {{
          var slug = pattern.toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");
          var card = document.createElement("div");
          card.className = "pattern-card";
          card.innerHTML =
            '<a href="patterns/' + slug + '.html">' + pattern + '</a>' +
            '<div class="count">' + counts[pattern] + ' problems</div>';
          grid.appendChild(card);
        }});
      }});
    </script>
    """

    body_with_state = body + inline
    html_doc = base_layout(f"Weekend review — {review_day_str}", body_with_state)
    return html_doc.replace("{root}", "")


def render_problem_page(problem: dict, today: date) -> str:
    task = problem["task"]
    java_file = REPO_ROOT / problem["javaFile"]

    try:
        source = java_file.read_text(encoding="utf-8", errors="replace")
    except FileNotFoundError:
        source = "// Source file not found: " + problem["javaFile"]

    class_doc, code_body = extract_javadoc(source)

    doc_html = (
        f'<pre class="doc">{esc(class_doc)}</pre>'
        if class_doc
        else '<p class="dim">(no class Javadoc found)</p>'
    )

    sm2 = problem.get("sm2", {})
    meta_bits = [
        f'{esc(problem.get("pattern", "Uncategorized"))}',
        f'difficulty: {esc(problem.get("difficulty", "Unknown"))}',
    ]
    if problem.get("leetcodeUrl"):
        meta_bits.append(f'<a href="{esc(problem["leetcodeUrl"])}" target="_blank" rel="noopener">Leetcode ↗</a>')
    github_url = f'https://github.com/ChauhanSandeep/DSA/blob/master/{problem["javaFile"]}'
    meta_bits.append(f'<a href="{esc(github_url)}" target="_blank" rel="noopener">source ↗</a>')
    meta_bits.append(f'next due: {esc(format_next_due(sm2.get("nextDue")))}')
    meta_bits.append(f'ease: {sm2.get("easeFactor", 2.5):.2f}')
    meta_bits.append(f'reps: {sm2.get("repetitions", 0)}')

    body = f"""
    <div class="problem-header" data-task="{esc(task)}">
      <div class="title">{esc(problem.get("problemName") or task)}</div>
      <div class="meta">
        {badges_for_problem(problem)}
      </div>
      <div class="meta" style="margin-top:8px">{" · ".join(meta_bits)}</div>
    </div>

    <h2>Problem</h2>
    {doc_html}

    <details class="reveal">
      <summary>Reveal my solution</summary>
      <div class="content">
        <pre class="code">{esc(code_body)}</pre>
      </div>
    </details>

    <div class="grade-bar">
      <span class="hint">grade this attempt →</span>
      <button class="grade-btn trivial" data-grade="trivial">⭐ Trivial <span class="key">1</span></button>
      <button class="grade-btn solved"  data-grade="solved">✅ Solved <span class="key">2</span></button>
      <button class="grade-btn hint"    data-grade="hint">🟡 Hint <span class="key">3</span></button>
      <button class="grade-btn blank"   data-grade="blank">🔴 Blank <span class="key">4</span></button>
    </div>
    """

    return base_layout(problem.get("problemName") or task, body, back_link="../index.html").replace("{root}", "../")


def render_pattern_index(state: dict) -> str:
    by_pattern = defaultdict(list)
    for entry in state["problems"].values():
        by_pattern[entry.get("pattern", "Uncategorized")].append(entry)

    rows = []
    for pattern in sorted(by_pattern, key=lambda p: -len(by_pattern[p])):
        slug = pattern_slug(pattern)
        rows.append(
            f'<div class="pattern-card">'
            f'<a href="{esc(slug)}.html">{esc(pattern)}</a>'
            f'<div class="count">{len(by_pattern[pattern])} problems</div>'
            f'</div>'
        )
    body = f"""
    <h2>All patterns</h2>
    <div class="pattern-grid">{"".join(rows)}</div>
    """
    return base_layout("Patterns", body, back_link="../index.html").replace("{root}", "../")


def render_pattern_page(pattern: str, entries: list[dict]) -> str:
    entries_sorted = sorted(
        entries,
        key=lambda e: (
            not e.get("isNC150"),
            not e.get("isBlind75"),
            DIFFICULTY_RANK.get(e.get("difficulty", "Unknown"), 0),
            e["task"],
        ),
    )
    rows = []
    for entry in entries_sorted:
        rows.append(f"""
      <tr>
        <td><a href="../problems/{esc(entry['task'])}.html">{esc(entry.get('problemName') or entry['task'])}</a></td>
        <td>{badges_for_problem(entry)}</td>
        <td class="mono">{esc(format_next_due(entry["sm2"].get("nextDue")))}</td>
        <td class="mono right">{entry["sm2"].get("repetitions", 0)}× · EF {entry["sm2"].get("easeFactor", 2.5):.2f}</td>
      </tr>""")
    body = f"""
    <h2>{esc(pattern)} — {len(entries_sorted)} problems</h2>
    <table class="problems-table">
      <thead>
        <tr><th>Problem</th><th>Meta</th><th>Next due</th><th>SM-2</th></tr>
      </thead>
      <tbody>{"".join(rows)}</tbody>
    </table>
    """
    return base_layout(pattern, body, back_link="index.html").replace("{root}", "../")


def pattern_slug(pattern: str) -> str:
    slug = re.sub(r"[^a-z0-9]+", "-", pattern.lower()).strip("-")
    return slug or "uncategorized"


# ---------------------------------------------------------------------------
# Build orchestration
# ---------------------------------------------------------------------------

def prepare_site_dir() -> None:
    if SITE_DIR.exists():
        shutil.rmtree(SITE_DIR)
    (SITE_DIR / "problems").mkdir(parents=True)
    (SITE_DIR / "patterns").mkdir(parents=True)
    (SITE_DIR / "assets").mkdir(parents=True)


def copy_assets() -> None:
    for name in ("styles.css", "app.js"):
        shutil.copy2(SITE_SRC_DIR / name, SITE_DIR / "assets" / name)


def write(path: Path, content: str) -> None:
    path.write_text(content, encoding="utf-8")


def build() -> int:
    if not STATE_JSON.exists():
        print("ERROR: state.json missing. Run sync.py first.", file=sys.stderr)
        return 1

    state = json.loads(STATE_JSON.read_text())
    today = date.today()

    prepare_site_dir()
    copy_assets()

    # Dashboard
    write(SITE_DIR / "index.html", render_dashboard(state, today))

    # Problem pages
    for problem in state["problems"].values():
        page = render_problem_page(problem, today)
        write(SITE_DIR / "problems" / f"{problem['task']}.html", page)

    # Pattern index + per-pattern pages
    by_pattern = defaultdict(list)
    for entry in state["problems"].values():
        by_pattern[entry.get("pattern", "Uncategorized")].append(entry)

    write(SITE_DIR / "patterns" / "index.html", render_pattern_index(state))
    for pattern, entries in by_pattern.items():
        write(
            SITE_DIR / "patterns" / f"{pattern_slug(pattern)}.html",
            render_pattern_page(pattern, entries),
        )

    print(f"Built site → {SITE_DIR.relative_to(REPO_ROOT)}")
    print(f"  problems: {len(state['problems'])}")
    print(f"  patterns: {len(by_pattern)}")
    print(f"  open: file://{SITE_DIR}/index.html")
    return 0


if __name__ == "__main__":
    sys.exit(build())

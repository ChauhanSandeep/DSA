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

import hashlib
import html
import json
import re
import shutil
import sys
from collections import defaultdict
from datetime import date, datetime, timedelta
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from _queue import (  # noqa: E402
    pick_queue,
    coming_saturday,
    DIFFICULTY_RANK,
    effective_difficulty,
    count_solved_this_week,
    week_start_iso,
)
from _javadoc import (  # noqa: E402
    extract_javadoc_blocks,
    parse_class_javadoc,
    parse_method_javadoc,
    parse_leetcode_section,
    parse_rating_section,
    parse_followups,
    parse_related,
    parse_example,
    rating_band,
)


REPO_ROOT = Path(__file__).resolve().parents[2]
DATA_DIR = REPO_ROOT / "Tracking" / "data"
SITE_SRC_DIR = REPO_ROOT / "Tracking" / "site-src"
VENDOR_DIR = REPO_ROOT / "Tracking" / "vendor"
SITE_DIR = REPO_ROOT / "Tracking" / "site"

STATE_JSON = DATA_DIR / "state.json"
NEETCODE_JSON = DATA_DIR / "neetcode.json"
CYCLE_JSON = DATA_DIR / "cycle.json"
QUEUE_SIZE = 10
WEEKLY_GOAL = 10
DIFFICULTY_CLASS = {
    "Easy": "diff-easy",
    "Medium": "diff-medium",
    "Hard": "diff-hard",
    "Unknown": "diff-unknown",
}

# Path of state.json relative to site/ pages (for a hint in the UI).
STATE_JSON_REL_FROM_SITE = "../data/state.json"

# Cache-busting version for CSS/JS links; recomputed from asset contents at
# build time so a rebuilt stylesheet is never served from a stale cache.
ASSET_VERSION = "dev"


def compute_asset_version() -> str:
    digest = hashlib.md5()
    for name in ("styles.css", "app.js"):
        asset = SITE_SRC_DIR / name
        if asset.exists():
            digest.update(asset.read_bytes())
    return digest.hexdigest()[:8]


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


SUN_ICON = (
    '<svg class="icon-sun" viewBox="0 0 24 24" fill="none" stroke="currentColor" '
    'stroke-width="2" stroke-linecap="round" stroke-linejoin="round">'
    '<circle cx="12" cy="12" r="4"/><path d="M12 2v2"/><path d="M12 20v2"/>'
    '<path d="m4.93 4.93 1.41 1.41"/><path d="m17.66 17.66 1.41 1.41"/>'
    '<path d="M2 12h2"/><path d="M20 12h2"/><path d="m6.34 17.66-1.41 1.41"/>'
    '<path d="m19.07 4.93-1.41 1.41"/></svg>'
)
MOON_ICON = (
    '<svg class="icon-moon" viewBox="0 0 24 24" fill="none" stroke="currentColor" '
    'stroke-width="2" stroke-linecap="round" stroke-linejoin="round">'
    '<path d="M12 3a6 6 0 0 0 9 9 9 9 0 1 1-9-9Z"/></svg>'
)

# Google Fonts — Inter, Atkinson Hyperlegible, JetBrains Mono. Matches Quartz.
FONT_LINKS = (
    '<link rel="preconnect" href="https://fonts.googleapis.com">'
    '<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>'
    '<link href="https://fonts.googleapis.com/css2?'
    'family=Inter:wght@400;500;600;700&'
    'family=Atkinson+Hyperlegible:wght@400;700&'
    'family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">'
)

# Inline early-run script that reads the persisted theme before CSS applies,
# so there's no light-flash-on-dark-load.
THEME_INIT = """<script>(function(){try{var t=localStorage.getItem('dsa-tracker.theme');if(t==='dark'||t==='light'){document.documentElement.setAttribute('data-theme',t);}}catch(e){}})();</script>"""

# Inline early-run script that applies the persisted text-size scale before CSS
# paints, so the page doesn't visibly resize after load. Mirrors THEME_INIT.
# The CSS default (var(--ui-scale, 1.08)) makes the baseline a bit larger.
SCALE_INIT = """<script>(function(){try{var s=parseFloat(localStorage.getItem('dsa-tracker.textScale'));if(s>=0.8&&s<=1.6){document.documentElement.style.setProperty('--ui-scale',s);}}catch(e){}})();</script>"""

# Prism.js highlighter + GitHub themes (assets copied by build).
PRISM_LINKS = (
    '<link rel="stylesheet" href="{root}assets/prism/github-light.css" '
    'media="(prefers-color-scheme: light)" id="prism-light">'
    '<link rel="stylesheet" href="{root}assets/prism/github-dark.css" '
    'media="(prefers-color-scheme: dark)" id="prism-dark">'
    '<link rel="stylesheet" href="{root}assets/prism/prism-match-braces.min.css">'
)


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
  {THEME_INIT}
  {SCALE_INIT}
  {FONT_LINKS}
  <link rel="stylesheet" href="{{root}}assets/styles.css?v={ASSET_VERSION}">
  {PRISM_LINKS}
</head>
<body>
  <div class="container">
    <header class="site-header">
      <h1><a href="{{root}}index.html" style="color:inherit">DSA · Weekend review</a></h1>
      <nav>
        {back}
        <a href="{{root}}index.html">Queue</a>
        <a href="{{root}}browse.html">Browse</a>
        <a href="{{root}}patterns/index.html">Patterns</a>
        <div class="text-size" role="group" aria-label="Text size">
          <button class="text-size-btn" aria-label="Decrease text size" title="Decrease text size" data-action="text-smaller">A<span class="text-size-minus">−</span></button>
          <button class="text-size-btn" aria-label="Increase text size" title="Increase text size" data-action="text-larger">A<span class="text-size-plus">+</span></button>
        </div>
        <button class="theme-toggle" aria-label="Toggle color theme" data-action="toggle-theme">
          {SUN_ICON}{MOON_ICON}
        </button>
      </nav>
    </header>
{body}
    <div class="footer">
      Regenerate with <code>python Tracking/scripts/build.py</code> ·
      state at <code>{STATE_JSON_REL_FROM_SITE}</code>
    </div>
  </div>
  <script src="{{root}}assets/prism/prism.js" defer></script>
  <script src="{{root}}assets/prism/prism-match-braces.min.js" defer></script>
  <script src="{{root}}assets/app.js?v={ASSET_VERSION}" defer></script>
</body>
</html>
"""


def badges_for_problem(problem: dict, root: str = "") -> str:
    """Render badges. Rating band replaces the coarse Easy/Medium/Hard chip
    when the Rating section is present in the Javadoc.
    """
    parts = []

    rating = problem.get("rating") or {}
    band = rating.get("band")
    diff = problem.get("difficulty", "Unknown")

    # Difficulty / rating chip.
    if band and band != "Unknown":
        # Map rating band to the same colour classes.
        band_slug = band.lower().replace(" ", "-")
        css_class = {
            "Easy": "diff-easy", "Medium": "diff-medium",
            "Hard": "diff-hard", "Very Hard": "diff-hard",
        }.get(band, "diff-unknown")
        title = rating.get("label") or f"{band} (from Rating)"
        parts.append(
            f'<a class="badge {css_class}" '
            f'href="{root}browse.html?band={esc(band_slug)}" '
            f'title="{esc(title)}">{esc(band)}</a>'
        )
    elif diff != "Unknown":
        diff_slug = diff.lower()
        parts.append(
            f'<a class="badge {DIFFICULTY_CLASS.get(diff, "diff-unknown")}" '
            f'href="{root}browse.html?diff={esc(diff_slug)}" '
            f'title="Filter Browse by {esc(diff)}">{esc(diff)}</a>'
        )
    else:
        parts.append(
            f'<span class="badge {DIFFICULTY_CLASS.get(diff, "diff-unknown")}">{esc(diff)}</span>'
        )

    if problem.get("isNC150"):
        parts.append(
            f'<a class="badge nc150" href="{root}browse.html?nc=1" '
            'title="Filter Browse by NeetCode 150">NC150</a>'
        )
    if problem.get("isBlind75"):
        parts.append(
            f'<a class="badge blind75" href="{root}browse.html?blind=1" '
            'title="Filter Browse by Blind 75">Blind 75</a>'
        )
    if problem.get("flags", {}).get("pinned"):
        parts.append(
            f'<a class="badge pinned" href="{root}browse.html?core=1" '
            'title="Filter Browse by core anchors">📌 Core</a>'
        )

    pattern = problem.get("pattern", "Uncategorized")
    parts.append(
        f'<a class="badge pattern" href="{root}patterns/{esc(pattern_slug(pattern))}.html" '
        f'title="All problems in {esc(pattern)}">{esc(pattern)}</a>'
    )
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

def load_cycle() -> dict | None:
    if CYCLE_JSON.exists():
        try:
            return json.loads(CYCLE_JSON.read_text())
        except Exception:
            return None
    return None


def save_cycle(week_of: str, target: int) -> None:
    CYCLE_JSON.write_text(
        json.dumps({"weekOf": week_of, "target": target}, indent=2) + "\n"
    )


def ensure_cycle(review_day: date) -> dict:
    """Return the current week's goal state, resetting when the week rolls over.

    The weekly goal defaults to WEEKLY_GOAL and only grows when the user asks
    for more (POST /api/load-more). It is keyed by the coming review Saturday,
    so a new week automatically starts a fresh goal of WEEKLY_GOAL.
    """
    week_of = review_day.isoformat()
    cycle = load_cycle()
    if cycle and cycle.get("weekOf") == week_of and cycle.get("target"):
        return cycle
    save_cycle(week_of, WEEKLY_GOAL)
    return {"weekOf": week_of, "target": WEEKLY_GOAL}


def render_batch_complete(target: int) -> str:
    """Congratulations panel + 'load more' button shown when the goal is met."""
    noun = "problem" if target == 1 else "problems"
    return f"""
      <div class="batch-complete">
        <div class="bc-emoji">🎉</div>
        <h3 class="bc-title">All {target} {noun} solved this week!</h3>
        <p class="bc-sub dim">You hit your weekly goal. Take a breath — or load
          another {WEEKLY_GOAL} if you're on a roll.</p>
        <button class="bc-load-more" data-action="load-more">Load more problems →</button>
      </div>"""


def render_queue_row(problem: dict, position: int, is_solved: bool, today: date) -> str:
    """Render one dashboard queue row.

    Solved rows stay on the page (marked with a check + grade) rather than
    disappearing, so the week's progress is visible; pending rows show the
    ordinal position and next-due meta.
    """
    task = problem["task"]
    qa = problem.get("qa") or {}
    preview = qa.get("problem") or ""
    preview_html = f'<div class="preview">{esc(preview)}</div>' if preview else ""
    sm2 = problem.get("sm2", {})

    if is_solved:
        item_class = "queue-item reviewed-today"
        index_mark = "✓"
        grade = sm2.get("lastGrade", "")
        emoji, label = GRADE_LABELS.get(grade, ("•", grade))
        suffix = " today" if reviewed_today(sm2, today) else ""
        done_meta = f'<span class="reviewed-tag">{emoji} {esc(label)}{suffix}</span>'
    else:
        item_class = "queue-item"
        index_mark = f"{position:02d}"
        done_meta = solved_status_tag(sm2)

    return f"""
      <div class="{item_class}">
        <div class="index">{index_mark}</div>
        <div>
          <div class="title"><a href="problems/{esc(task)}.html">{esc(problem.get("problemName") or task)}</a></div>
          <div class="meta">{esc(task)} · next due {esc(format_next_due(sm2.get("nextDue")))} {done_meta}</div>
          {preview_html}
        </div>
        <div class="badges">{badges_for_problem(problem, root="")}</div>
      </div>"""


def render_dashboard(state: dict, today: date) -> str:
    review_day = coming_saturday(today)
    cycle = ensure_cycle(review_day)
    target = cycle["target"]
    solved_count = count_solved_this_week(state, review_day)
    remaining = max(0, target - solved_count)
    # Pending = up to `remaining` unsolved, hardest-due problems (shrinks toward
    # zero as the goal is met). Solved-this-week problems stay listed too, marked
    # done, so the page shows the full picture of the week's progress.
    pending = pick_queue(state, review_day, QUEUE_SIZE)[:remaining]
    since = week_start_iso(review_day)
    solved_entries = [
        entry for entry in state["problems"].values()
        if not entry.get("flags", {}).get("skip")
        and entry.get("sm2", {}).get("lastReviewed")
        and entry["sm2"]["lastReviewed"] >= since
    ]
    solved_entries.sort(key=lambda entry: entry["sm2"]["lastReviewed"], reverse=True)
    all_problems = list(state["problems"].values())

    total = len(all_problems)
    nc150 = sum(1 for p in all_problems if p.get("isNC150"))
    reviewed_ever = sum(1 for p in all_problems if p["sm2"].get("lastReviewed"))
    # How many problems remain before this week's goal is met.
    due_this_week = remaining

    stats = f"""
    <div class="stats">
      <div class="stat"><div class="value">{total}</div><div class="label">tracked problems</div></div>
      <div class="stat"><div class="value">{nc150}</div><div class="label">NC150 in repo</div></div>
      <div class="stat"><div class="value">{due_this_week}</div><div class="label">left this week</div></div>
      <div class="stat"><div class="value">{reviewed_ever}</div><div class="label">reviewed at least once</div></div>
    </div>
    """

    pending_banner = """
    <div id="pending-banner" class="pending-banner hidden">
      <span><strong><span class="count">0</span></strong> unsaved review(s). Save to <code>Tracking/data/state.json</code> before committing.</span>
      <button data-action="save-all">Save all</button>
    </div>
    """

    rows = []
    for index, problem in enumerate(pending, 1):
        rows.append(render_queue_row(problem, index, is_solved=False, today=today))
    for problem in solved_entries:
        rows.append(render_queue_row(problem, 0, is_solved=True, today=today))

    completion_html = render_batch_complete(target) if remaining == 0 else ""
    if rows:
        queue_html = completion_html + "\n".join(rows)
    elif completion_html:
        queue_html = completion_html
    else:
        queue_html = '<p class="dim">Nothing due right now — enjoy the break.</p>'

    review_day_str = review_day.strftime("%A, %d %b %Y")

    # Live "time running out" countdown to this week's deadline (end of the
    # review Saturday). app.js ticks it down and escalates urgency styling.
    deadline_dt = datetime(review_day.year, review_day.month, review_day.day, 23, 59, 59)
    countdown_banner = (
        f'<div id="deadline-countdown" class="deadline-countdown" '
        f'data-deadline="{deadline_dt.isoformat()}">'
        f'<span class="dc-icon">⏳</span>'
        f'<span class="dc-text">Calculating time left…</span>'
        f'</div>'
    )

    if remaining > 0:
        queue_heading = (
            f"This week · {solved_count}/{target} solved · {remaining} left"
        )
    else:
        queue_heading = f"Weekly goal complete · {solved_count}/{target} solved"

    body = f"""
    {pending_banner}

    {countdown_banner}

    {render_momentum_panel(state, today)}

    {render_motivation_stack(state, today)}

    {stats}

    <h2>{esc(queue_heading)}</h2>
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


def compute_pattern_warmth(state: dict, today: date) -> list[dict]:
    by_pattern: dict[str, list[dict]] = defaultdict(list)
    for entry in state["problems"].values():
        by_pattern[entry.get("pattern", "Uncategorized")].append(entry)

    result: list[dict] = []
    for pattern, entries in by_pattern.items():
        min_days = None
        reviewed_count = 0
        for entry in entries:
            last = entry["sm2"].get("lastReviewed")
            if not last:
                continue
            reviewed_count += 1
            delta = (today - date.fromisoformat(last)).days
            if min_days is None or delta < min_days:
                min_days = delta
        result.append({
            "pattern": pattern,
            "count": len(entries),
            "min_days": min_days,
            "reviewed": reviewed_count,
        })
    result.sort(key=lambda p: (p["min_days"] is None, p["min_days"] or 0))
    return result


def warmth_class(min_days) -> str:
    if min_days is None: return "warmth-cold"
    if min_days <= 14:   return "warmth-hot"
    if min_days <= 45:   return "warmth-warm"
    if min_days <= 90:   return "warmth-lukewarm"
    return "warmth-cold"


def compute_weekly_dots(state: dict, today: date, weeks: int = 12) -> dict:
    grades_by_date: dict[str, int] = defaultdict(int)
    for entry in state["problems"].values():
        for h in entry.get("history", []) or []:
            d = h.get("date")
            if d:
                grades_by_date[d] = grades_by_date.get(d, 0) + 1

    weekday = today.weekday()
    if weekday <= 5:
        current_saturday = today + timedelta(days=(5 - weekday))
    else:
        current_saturday = today + timedelta(days=6)

    dots: list[dict] = []
    for i in range(weeks):
        end = current_saturday - timedelta(weeks=i)
        start = end - timedelta(days=6)
        active_count = 0
        for offset in range(7):
            d = start + timedelta(days=offset)
            active_count += grades_by_date.get(d.isoformat(), 0)
        dots.append({
            "end": end.isoformat(),
            "label": end.strftime("%d %b"),
            "active": active_count > 0,
            "count": active_count,
        })
    dots.reverse()

    streak = 0
    for dot in reversed(dots):
        if dot["active"]:
            streak += 1
        else:
            break
    total_grades = sum(d["count"] for d in dots)
    return {"dots": dots, "streak": streak, "total_grades_12w": total_grades}


# ---------------------------------------------------------------------------
# Gamification — streak, XP/level, weekly goal, achievements.
# Psychology: loss-aversion (streak), endowed-progress + goal-gradient (XP bar
# and weekly ring), collection + curiosity (achievements with a locked teaser).
# ---------------------------------------------------------------------------

GRADE_XP = {"blank": 4, "hint": 8, "solved": 12, "trivial": 16}
DIFFICULTY_XP_MULT = {
    "Very Hard": 2.0, "Hard": 1.6, "Medium": 1.2, "Easy": 1.0, "Unknown": 1.0,
}
RANK_TITLES = [
    (1, "Warming Up"), (3, "Grinder"), (5, "Problem Slayer"),
    (8, "Pattern Wizard"), (12, "Algorithm Assassin"), (18, "Interview Ready"),
    (25, "FAANG Boss"),
]
XP_PER_LEVEL_BASE = 50  # xp needed for level L = base * L^2


def _rank_title(level: int) -> str:
    title = RANK_TITLES[0][1]
    for threshold, name in RANK_TITLES:
        if level >= threshold:
            title = name
    return title


def compute_gamification(state: dict, today: date) -> dict:
    problems = list(state["problems"].values())

    # --- XP from full grade history, weighted by difficulty ---------------
    total_xp = 0.0
    total_reviews = 0
    hard_wins = 0
    reviewed_tasks = set()
    for entry in problems:
        diff = effective_difficulty(entry)
        mult = DIFFICULTY_XP_MULT.get(diff, 1.0)
        for h in entry.get("history", []) or []:
            grade = h.get("grade")
            if grade not in GRADE_XP:
                continue
            total_xp += GRADE_XP[grade] * mult
            total_reviews += 1
            reviewed_tasks.add(entry["task"])
            if grade in ("solved", "trivial") and diff in ("Hard", "Very Hard"):
                hard_wins += 1
    total_xp = int(round(total_xp))

    # --- Level curve: xp for level L = base * L^2 -------------------------
    level = int((total_xp / XP_PER_LEVEL_BASE) ** 0.5) + 1
    cur_floor = XP_PER_LEVEL_BASE * (level - 1) ** 2
    next_floor = XP_PER_LEVEL_BASE * level ** 2
    span = max(1, next_floor - cur_floor)
    level_pct = int(round((total_xp - cur_floor) / span * 100))

    # --- Streak + this-week goal -----------------------------------------
    dots = compute_weekly_dots(state, today)
    week_streak = dots["streak"]
    best_streak = 0
    run = 0
    for d in dots["dots"]:
        run = run + 1 if d["active"] else 0
        best_streak = max(best_streak, run)
    this_week_count = dots["dots"][-1]["count"] if dots["dots"] else 0
    goal = QUEUE_SIZE
    goal_pct = min(100, int(round(this_week_count / goal * 100))) if goal else 0

    # --- Achievements (id, emoji, title, desc, tier, unlocked) -----------
    # Tiers escalate bronze -> silver -> gold -> platinum by rarity/effort.
    achievements = [
        ("first_blood", "🥉", "First Blood", "Grade your first problem",
         "bronze", total_reviews >= 1),
        ("quarter", "📖", "Getting Serious", "25 reviews logged",
         "bronze", total_reviews >= 25),
        ("hard_hitter", "⚔️", "Hard Hitter", "10 hard problems solved",
         "silver", hard_wins >= 10),
        ("explorer", "🧭", "Explorer", "50 distinct problems touched",
         "silver", len(reviewed_tasks) >= 50),
        ("streak4", "🔥", "On Fire", "4-week streak",
         "silver", best_streak >= 4),
        ("century", "💯", "Century", "100 reviews logged",
         "gold", total_reviews >= 100),
        ("legend", "👑", "Legend", "500 reviews logged",
         "gold", total_reviews >= 500),
        ("streak12", "🏆", "Unstoppable", "12-week streak",
         "platinum", best_streak >= 12),
    ]

    return {
        "xp": total_xp,
        "level": level,
        "rank": _rank_title(level),
        "level_pct": level_pct,
        "xp_to_next": max(0, next_floor - total_xp),
        "week_streak": week_streak,
        "best_streak": best_streak,
        "this_week_count": this_week_count,
        "goal": goal,
        "goal_pct": goal_pct,
        "total_reviews": total_reviews,
        "achievements": achievements,
    }


def render_momentum_panel(state: dict, today: date) -> str:
    g = compute_gamification(state, today)

    if g["week_streak"] >= 1:
        streak_sub = f'best {g["best_streak"]} · keep it alive this week!'
    else:
        streak_sub = "start a streak this Saturday"
    flame = "🔥" if g["week_streak"] >= 1 else "🌱"

    # Trophy case — every achievement is a medallion; earned ones gleam with
    # their tier metal, locked ones show as dimmed empty slots (curiosity).
    earned_count = sum(1 for a in g["achievements"] if a[5])
    total_count = len(g["achievements"])
    trophy_parts = []
    for _id, emoji, title, desc, tier, unlocked in g["achievements"]:
        state_cls = "earned" if unlocked else "locked"
        face = emoji if unlocked else "🔒"
        trophy_parts.append(
            f'<div class="trophy tier-{tier} {state_cls}" '
            f'title="{esc(title)} — {esc(desc)}">'
            f'<div class="trophy-medallion"><span class="trophy-emoji">{face}</span></div>'
            f'<div class="trophy-name">{esc(title)}</div>'
            f'</div>'
        )
    trophy_html = "".join(trophy_parts)

    goal_sub = "goal smashed! 🎉" if g["goal_pct"] >= 100 else "reviews done this week"
    return f"""
    <section class="momentum-hero">
      <div class="mo-card mo-streak">
        <div class="mo-label">Weekly streak</div>
        <div class="mo-big">{flame} {g["week_streak"]}</div>
        <div class="mo-sub">{esc(streak_sub)}</div>
      </div>

      <div class="mo-card mo-level">
        <div class="mo-label">Level {g["level"]} · {esc(g["rank"])}</div>
        <div class="mo-big">{g["xp"]} <span class="mo-unit">XP</span></div>
        <div class="progress-track" style="margin-top:8px">
          <div class="progress-fill" style="width:{g["level_pct"]}%"></div>
        </div>
        <div class="mo-sub">{g["xp_to_next"]} XP to level {g["level"] + 1}</div>
      </div>

      <div class="mo-card mo-goal">
        <div class="mo-label">This week's goal</div>
        <div class="mo-ring" style="--pct:{g["goal_pct"]}">
          <span class="mo-ring-text">{g["this_week_count"]}<span class="mo-ring-sub">/{g["goal"]}</span></span>
        </div>
        <div class="mo-sub">{goal_sub}</div>
      </div>
    </section>
    <section class="achievements-bar">
      <div class="mo-label">🏆 Trophy case <span class="trophy-count">{earned_count}/{total_count}</span></div>
      <div class="trophy-case">{trophy_html}</div>
    </section>
    """


def render_motivation_stack(state: dict, today: date) -> str:
    dots = compute_weekly_dots(state, today)

    dot_html_parts = []
    for dot in dots["dots"]:
        active_cls = "on" if dot["active"] else "off"
        tooltip = f'{dot["label"]} — {dot["count"]} grade(s)' if dot["active"] else f'{dot["label"]} — no reviews'
        dot_html_parts.append(f'<div class="week-dot {active_cls}" title="{esc(tooltip)}"></div>')

    if dots["streak"] == 0:
        streak_hint = "No active streak — pick something up this Saturday."
    elif dots["streak"] == 1:
        streak_hint = "1 week active — momentum building."
    else:
        streak_hint = f'{dots["streak"]}-week streak · {dots["total_grades_12w"]} grades in the last 12 weeks'

    return f"""
    <section class="motiv-stack">

      <div class="motiv-panel">
        <div class="motiv-label">Last 12 weeks</div>
        <div class="week-dots">{"".join(dot_html_parts)}</div>
        <div class="motiv-hint">{esc(streak_hint)}</div>
      </div>
    </section>
    """


def render_qa_card(problem: dict) -> str:
    qa = problem.get("qa") or {}
    problem_text = qa.get("problem")
    answer_text = qa.get("answer")
    complexity = qa.get("complexity")
    source = qa.get("source", "missing")

    if not problem_text and not answer_text:
        return (
            '<div class="qa-card">'
            '<div class="qa-label">Quick recall</div>'
            '<div class="qa-body dim">No quick summary yet — see the full problem statement below.</div>'
            '</div>'
        )

    problem_block = ""
    if problem_text:
        problem_block = (
            '<div class="qa-section">'
            '<div class="qa-label">Problem</div>'
            f'<div class="qa-body">{esc(problem_text)}</div>'
            '</div>'
        )

    answer_block = ""
    reveal_hint = ""
    if answer_text or complexity:
        inner_parts = []
        if answer_text:
            inner_parts.append(
                '<div class="qa-label">Answer</div>'
                f'<div class="qa-body">{esc(answer_text)}</div>'
            )
        if complexity:
            inner_parts.append(
                f'<div class="qa-complexity">{esc(complexity)}</div>'
            )
        answer_block = (
            '<div class="qa-section qa-answer qa-hidden">' +
            "".join(inner_parts) +
            '</div>'
        )
        reveal_hint = (
            '<div class="qa-reveal-hint" role="button" tabindex="0">'
            '<span>▸ Reveal answer</span> <span class="mono">(Space)</span>'
            '</div>'
        )

    source_marker = ""
    if source and source != "user":
        label = {"sheet": "from legacy sheet", "javadoc": "from source Javadoc"}.get(source, source)
        source_marker = f'<div class="qa-reveal-hint dim" style="margin-top:12px" title="{esc(source)}"><span>· {esc(label)}</span></div>'

    return (
        '<div class="qa-card">'
        '<div class="qa-label" style="letter-spacing:0.12em">Quick recall</div>'
        + problem_block
        + answer_block
        + reveal_hint
        + source_marker
        + '</div>'
    )


GRADE_LABELS = {
    "trivial": ("⭐", "Nailed it"),
    "solved":  ("✅", "Passed"),
    "hint":    ("🟡", "Bar-raiser"),
    "blank":   ("🔴", "Bombed"),
}

# Compact at-a-glance markers for the dashboard queue. Solved/Trivial get a
# tick; weaker outcomes get a distinct glyph so "what's solved" reads instantly.
STATUS_MARKERS = {
    "trivial": ("✓", "Nailed it"),
    "solved":  ("✓", "Passed"),
    "hint":    ("◐", "Bar-raiser"),
    "blank":   ("✗", "Bombed"),
}


def solved_status_tag(sm2: dict) -> str:
    """Persistent status pill for a queue item, based on the last grade."""
    grade = sm2.get("lastGrade")
    if not grade:
        return '<span class="status-tag status-new" title="Not reviewed yet">○ New</span>'
    icon, label = STATUS_MARKERS.get(grade, ("•", grade))
    return (
        f'<span class="status-tag status-{esc(grade)}" '
        f'title="Last attempt: {esc(label)}">{icon} {esc(label)}</span>'
    )


def reviewed_today(sm2: dict, today: date) -> bool:
    last = sm2.get("lastReviewed")
    return bool(last) and last == today.isoformat()


def format_review_banner(problem: dict, today: date) -> str:
    """Small callout shown on a problem page when it was graded today."""
    sm2 = problem.get("sm2", {})
    if not reviewed_today(sm2, today):
        return ""
    grade = sm2.get("lastGrade", "")
    emoji, label = GRADE_LABELS.get(grade, ("•", grade or "unknown"))
    next_due = sm2.get("nextDue")
    next_pretty = "—"
    if next_due:
        try:
            d = date.fromisoformat(next_due)
            next_pretty = d.strftime("%a, %d %b %Y")
        except ValueError:
            next_pretty = next_due
    return (
        '<div class="reviewed-banner">'
        f'<span class="reviewed-banner-emoji">{emoji}</span>'
        f'<span>Reviewed today · marked <strong>{esc(label)}</strong> · '
        f'next review <strong>{esc(next_pretty)}</strong></span>'
        '</div>'
    )


def render_class_javadoc_sections(source: str, leetcode_number_index: dict) -> tuple[str, str]:
    """Render the class Javadoc as an ordered stack of themed callouts, one
    per AGENT.md section. `leetcode_number_index` maps a Leetcode number to
    the task_id so `Related:` items can become clickable links.

    Returns `(main_html, footer_html)`. `footer_html` holds the Follow-ups and
    Related callouts so the problem page can push them to the very bottom;
    everything else stays in `main_html`.
    """
    blocks = extract_javadoc_blocks(source)
    if not blocks:
        return ('<p class="dim">(no class Javadoc found)</p>', "")
    cls = parse_class_javadoc(blocks[0])

    parts: list[str] = []
    footer_parts: list[str] = []

    # Problem — the meat. Show as plain text (multi-line) with a subtle callout.
    if cls.get("problem"):
        parts.append(_callout("problem", "Problem statement", _prose_html(cls["problem"])))

    # Example — split into Input / Output / Why for a table-like look.
    if cls.get("example"):
        ex = parse_example(cls["example"])
        rows = []
        if ex.get("input"):
            rows.append(f'<div class="ex-row"><span class="ex-label">Input</span>'
                        f'<code class="ex-value">{esc(ex["input"])}</code></div>')
        if ex.get("output"):
            rows.append(f'<div class="ex-row"><span class="ex-label">Output</span>'
                        f'<code class="ex-value">{esc(ex["output"])}</code></div>')
        if ex.get("why"):
            rows.append(f'<div class="ex-row"><span class="ex-label">Why</span>'
                        f'<span class="ex-value ex-why">{esc(ex["why"])}</span></div>')
        if not rows:
            # Fall back to prose if we couldn't classify.
            rows.append(_prose_html(cls["example"]))
        parts.append(_callout("example", "Example", "".join(rows)))

    # Pattern — small typographic callout.
    if cls.get("pattern"):
        pattern_html = f'<div class="pattern-line mono">{esc(cls["pattern"])}</div>'
        parts.append(_callout("pattern", "Pattern", pattern_html))

    # Rating — chip-ish display for the granular signal.
    if cls.get("rating"):
        r = parse_rating_section(cls["rating"])
        if r["label"]:
            parts.append(_callout("rating", "Rating",
                                  f'<div class="rating-line">{esc(r["label"])}</div>'))

    # Follow-ups — numbered pairs. Rendered into the page footer so the
    # solution + grading flow comes first.
    followups = parse_followups(cls.get("followups"))
    if followups:
        rows = []
        for i, fu in enumerate(followups, 1):
            rows.append(
                f'<div class="fu-row">'
                f'<div class="fu-index">{i}.</div>'
                f'<div class="fu-body">'
                f'<div class="fu-question">{esc(fu["question"])}</div>'
                f'<div class="fu-answer">{esc(fu["answer"])}</div>'
                f'</div>'
                f'</div>'
            )
        footer_parts.append(_callout("followups", "Follow-ups", "".join(rows)))
    elif cls.get("followups"):
        footer_parts.append(_callout("followups", "Follow-ups", _prose_html(cls["followups"])))

    # Related — clickable when the Leetcode number matches a tracked problem.
    related = parse_related(cls.get("related"))
    if related:
        chips = []
        for r in related:
            task = leetcode_number_index.get(r["number"])
            label = f"{esc(r['name'])} <span class='mono dim'>({r['number']})</span>"
            if task:
                chips.append(
                    f'<a class="related-chip" href="../problems/{esc(task)}.html">'
                    f'{label}</a>'
                )
            else:
                chips.append(f'<span class="related-chip related-chip-dim">{label}</span>')
        footer_parts.append(_callout("related", "Related",
                              f'<div class="related-list">{"".join(chips)}</div>'))

    # Approach table (rare) — keep as pre for now; it's tabular.
    if cls.get("approach"):
        parts.append(_callout("approach", "Approaches",
                              f'<pre class="approach-table">{esc(cls["approach"])}</pre>'))

    main_html = "".join(parts) or '<p class="dim">(no recognisable Javadoc sections)</p>'
    return (main_html, "".join(footer_parts))


def _prose_html(text: str) -> str:
    """Escape text and preserve paragraph breaks."""
    paragraphs = re.split(r"\n\s*\n", text.strip())
    return "".join(
        f'<p>{esc(p).replace(chr(10), "<br>")}</p>' for p in paragraphs if p.strip()
    )


def _callout(kind: str, label: str, inner: str) -> str:
    return (
        f'<section class="doc-section doc-{esc(kind)}">'
        f'<div class="doc-section-label">{esc(label)}</div>'
        f'<div class="doc-section-body">{inner}</div>'
        f'</section>'
    )


def build_leetcode_number_index(state: dict) -> dict[int, str]:
    idx: dict[int, str] = {}
    for entry in state["problems"].values():
        num = entry.get("leetcodeNumber")
        if isinstance(num, int) and num not in idx:
            idx[num] = entry["task"]
    return idx


def render_problem_page(problem: dict, today: date, leetcode_index: dict[int, str],
                        week_tasks: list[str] | None = None) -> str:
    task = problem["task"]
    java_file = REPO_ROOT / problem["javaFile"]

    try:
        source = java_file.read_text(encoding="utf-8", errors="replace")
    except FileNotFoundError:
        source = "// Source file not found: " + problem["javaFile"]

    _, code_body = extract_javadoc(source)
    doc_sections_html, doc_footer_html = render_class_javadoc_sections(source, leetcode_index)

    sm2 = problem.get("sm2", {})
    rating = problem.get("rating") or {}

    # Prominent tags: difficulty band, NC150/Blind75/pinned, pattern, plus a
    # bright acceptance chip so difficulty + acceptance lead the page.
    prominent = [badges_for_problem(problem, root="../")]
    acceptance = rating.get("acceptance")
    if isinstance(acceptance, (int, float)):
        prominent.append(
            f'<span class="badge acceptance" title="Leetcode acceptance rate">'
            f'accept {acceptance:.1f}%</span>'
        )

    # Secondary, low-key line: external links + SM-2 stats. `next due` removed;
    # `reps` kept here so it stays available but visually quiet.
    secondary = []
    if problem.get("leetcodeUrl"):
        secondary.append(f'<a href="{esc(problem["leetcodeUrl"])}" target="_blank" rel="noopener">Leetcode ↗</a>')
    github_url = f'https://github.com/ChauhanSandeep/DSA/blob/master/{problem["javaFile"]}'
    secondary.append(f'<a href="{esc(github_url)}" target="_blank" rel="noopener">source ↗</a>')
    secondary.append(f'ease {sm2.get("easeFactor", 2.5):.2f}')
    secondary.append(f'reps {sm2.get("repetitions", 0)}')

    body = f"""
    <div class="problem-header" data-task="{esc(task)}">
      <div class="title">{esc(problem.get("problemName") or task)}</div>
      <div class="meta meta-tags">
        {"".join(prominent)}
      </div>
      <div class="meta meta-secondary">{" · ".join(secondary)}</div>
    </div>

    {format_review_banner(problem, today)}

    {render_qa_card(problem)}

    <h2>Full problem statement</h2>
    {doc_sections_html}

    <details class="reveal">
      <summary>Reveal my solution <span class="mono dim" style="margin-left:8px">(Enter)</span></summary>
      <div class="content">
        <div class="code-tools">
          <button class="code-edit-btn" data-action="edit-source">✏️ Edit source</button>
          <span class="code-tools-hint dim">writes straight to {esc(problem["javaFile"])} · git-backed</span>
        </div>
        <div class="code-view">
          <pre class="code language-java match-braces rainbow-braces"><code class="language-java">{esc(code_body)}</code></pre>
        </div>
        <div class="code-editor hidden">
          <textarea class="code-editor-area" spellcheck="false" wrap="off" aria-label="Edit Java source"></textarea>
          <div class="code-editor-actions">
            <button class="code-save-btn" data-action="save-source">💾 Save to file</button>
            <button class="code-cancel-btn" data-action="cancel-edit">Cancel</button>
            <span class="code-editor-status dim"></span>
          </div>
        </div>
      </div>
    </details>

    <div class="grade-bar">
      <span class="hint">grade this attempt →</span>
      <button class="grade-btn trivial" data-grade="trivial">⭐ Nailed it <span class="key">1</span></button>
      <button class="grade-btn solved"  data-grade="solved">✅ Passed <span class="key">2</span></button>
      <button class="grade-btn hint"    data-grade="hint">🟡 Bar-raiser <span class="key">3</span></button>
      <button class="grade-btn blank"   data-grade="blank">🔴 Bombed <span class="key">4</span></button>
    </div>

    {doc_footer_html}

    {render_problem_nav(task, week_tasks)}

    <script>window.__STATE__ = {{"problems": {{{json.dumps(task)}: {json.dumps(problem, ensure_ascii=False)}}}}};</script>
    """

    return base_layout(problem.get("problemName") or task, body, back_link="../index.html").replace("{root}", "../")


def render_problem_nav(task: str, week_tasks: list[str] | None) -> str:
    """Prev/Next navigation across this week's problem set.

    Only rendered when the current problem is part of the weekend queue; the
    order matches the dashboard/issue queue so navigation is predictable.
    """
    if not week_tasks or task not in week_tasks:
        return ""
    pos = week_tasks.index(task)
    total = len(week_tasks)
    prev_task = week_tasks[pos - 1] if pos > 0 else None
    next_task = week_tasks[pos + 1] if pos < total - 1 else None

    if prev_task:
        prev_html = f'<a class="problem-nav-btn prev" href="../problems/{esc(prev_task)}.html">← Prev</a>'
    else:
        prev_html = '<span class="problem-nav-btn prev disabled" aria-disabled="true">← Prev</span>'
    if next_task:
        next_html = f'<a class="problem-nav-btn next" href="../problems/{esc(next_task)}.html">Next →</a>'
    else:
        next_html = '<span class="problem-nav-btn next disabled" aria-disabled="true">Next →</span>'

    return (
        '<nav class="problem-nav" aria-label="This week\'s problem set">'
        f'{prev_html}'
        f'<span class="problem-nav-pos">{pos + 1} / {total} this week</span>'
        f'{next_html}'
        '</nav>'
    )


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


GRADE_EMOJI = {
    "trivial": "⭐",
    "solved":  "✅",
    "hint":    "🟡",
    "blank":   "🔴",
}


def format_last_reviewed(sm2: dict, today: date) -> tuple[str, str]:
    """Return (label, sort_value). sort_value is '9999-12-31' for never
    so unreviewed items sort last when ascending."""
    last = sm2.get("lastReviewed")
    if not last:
        return ("never", "0000-00-00")
    last_d = date.fromisoformat(last)
    delta = (today - last_d).days
    grade = sm2.get("lastGrade")
    emoji = GRADE_EMOJI.get(grade, "•")
    if delta == 0:   pretty = "today"
    elif delta == 1: pretty = "yesterday"
    elif delta < 7:  pretty = f"{delta}d ago"
    elif delta < 30: pretty = f"{delta // 7}w ago"
    else:            pretty = last
    return (f"{emoji} {pretty}", last)


def _classify_status_flags(sm2: dict, today: date) -> tuple[bool, bool, bool]:
    next_due = sm2.get("nextDue")
    last = sm2.get("lastReviewed")
    is_overdue = False
    is_fresh = False
    if next_due:
        try:
            is_overdue = date.fromisoformat(next_due) < today
        except ValueError:
            pass
    if last:
        try:
            is_fresh = (today - date.fromisoformat(last)).days <= 14
        except ValueError:
            pass
    is_never = not last
    return is_overdue, is_never, is_fresh


def render_browse_page(state: dict, today: date) -> str:
    entries = list(state["problems"].values())
    entries_sorted = sorted(
        entries,
        key=lambda e: (
            not e.get("isNC150"),
            not e.get("isBlind75"),
            -DIFFICULTY_RANK.get(e.get("difficulty", "Unknown"), 0),
            e["task"],
        ),
    )

    pattern_counts: dict[str, int] = defaultdict(int)
    for e in entries:
        pattern_counts[e.get("pattern", "Uncategorized")] += 1
    patterns_ordered = sorted(pattern_counts, key=lambda p: -pattern_counts[p])

    items_html: list[str] = []
    for entry in entries_sorted:
        qa = entry.get("qa") or {}
        sm2 = entry["sm2"]
        next_due_iso = sm2.get("nextDue") or "9999-12-31"
        last_label, last_sort = format_last_reviewed(sm2, today)
        next_due_pretty = format_next_due(sm2.get("nextDue"))
        is_overdue, is_never, is_fresh = _classify_status_flags(sm2, today)

        rating = entry.get("rating") or {}
        band = rating.get("band") or entry.get("difficulty", "Unknown")
        band_slug = band.lower().replace(" ", "-")
        pat_slug = pattern_slug(entry.get("pattern", "Uncategorized"))

        problem_html = ""
        if qa.get("problem"):
            problem_html = (
                '<div class="qa-mini-section">'
                '<div class="qa-mini-label">Problem</div>'
                f'<div class="qa-mini-body">{esc(qa["problem"])}</div>'
                '</div>'
            )
        answer_html = ""
        if qa.get("answer"):
            answer_html = (
                '<div class="qa-mini-section">'
                '<div class="qa-mini-label">Answer</div>'
                f'<div class="qa-mini-body">{esc(qa["answer"])}</div>'
                '</div>'
            )
        complexity_html = ""
        if qa.get("complexity"):
            complexity_html = (
                '<div class="qa-mini-section">'
                f'<div class="qa-complexity">{esc(qa["complexity"])}</div>'
                '</div>'
            )
        body_content = problem_html + answer_html + complexity_html
        if not body_content:
            body_content = '<div class="dim">No summary yet — open the full page.</div>'

        items_html.append(f"""
      <details class="pattern-item browse-item"
               data-name="{esc((entry.get('problemName') or entry['task']).lower())}"
               data-due="{esc(next_due_iso)}"
               data-last="{esc(last_sort)}"
               data-band="{esc(band_slug)}"
               data-pattern="{esc(pat_slug)}"
               data-nc="{1 if entry.get('isNC150') else 0}"
               data-blind="{1 if entry.get('isBlind75') else 0}"
               data-core="{1 if entry.get('flags', {}).get('pinned') else 0}"
               data-overdue="{1 if is_overdue else 0}"
               data-fresh="{1 if is_fresh else 0}"
               data-never="{1 if is_never else 0}">
        <summary>
          <span class="pattern-item-chevron">▸</span>
          <span class="pattern-item-title">
            <a href="problems/{esc(entry['task'])}.html">{esc(entry.get('problemName') or entry['task'])}</a>
          </span>
          <span class="pattern-item-badges">{badges_for_problem(entry, root="")}</span>
          <span class="pattern-item-col pattern-item-due"><span class="pattern-item-col-label">next</span>{esc(next_due_pretty)}</span>
          <span class="pattern-item-col pattern-item-last"><span class="pattern-item-col-label">last</span>{esc(last_label)}</span>
        </summary>
        <div class="pattern-item-body">
          {body_content}
          <a class="pattern-item-open" href="problems/{esc(entry['task'])}.html">Open full page →</a>
        </div>
      </details>""")

    def chip(facet: str, value: str, label: str, title: str = "") -> str:
        t = f' title="{esc(title)}"' if title else ""
        return (
            f'<button class="filter-chip" data-facet="{esc(facet)}" '
            f'data-value="{esc(value)}"{t}>{label}</button>'
        )

    curated_chips = "".join([
        chip("nc",    "1", "NC150",     "NeetCode 150 subset"),
        chip("blind", "1", "Blind 75",  "Blind 75 subset"),
        chip("core",  "1", "📌 Core",    "Curated anchor set"),
    ])
    diff_chips = "".join([
        chip("band", "easy",      "Easy"),
        chip("band", "medium",    "Medium"),
        chip("band", "hard",      "Hard"),
        chip("band", "very-hard", "Very Hard"),
    ])
    pattern_chip_htmls = [
        chip("pattern", pattern_slug(p),
             f"{esc(p)} <span class='dim mono' style='margin-left:4px'>{pattern_counts[p]}</span>",
             p)
        for p in patterns_ordered
    ]
    pattern_chips = "".join(pattern_chip_htmls)
    status_chips = "".join([
        chip("overdue", "1", "Overdue",      "Next due date is in the past"),
        chip("never",   "1", "Never solved", "No review recorded yet"),
        chip("fresh",   "1", "Fresh · ≤14d", "Reviewed within the last 14 days"),
    ])

    filter_bar = f"""
    <section class="browse-filters">
      <div class="filter-group">
        <span class="filter-group-label">Curated</span>
        {curated_chips}
      </div>
      <div class="filter-group">
        <span class="filter-group-label">Rating band</span>
        {diff_chips}
      </div>
      <div class="filter-group">
        <span class="filter-group-label">Status</span>
        {status_chips}
      </div>
      <div class="filter-group filter-group-wrap">
        <span class="filter-group-label">Pattern</span>
        {pattern_chips}
      </div>
      <div class="filter-actions">
        <span class="filter-summary">
          Showing <span class="browse-count">{len(entries_sorted)}</span>
          of {len(entries_sorted)}
        </span>
        <button class="chip" data-action="clear-filters">Clear all</button>
      </div>
    </section>
    """

    body = f"""
    <h2>Browse all problems</h2>
    <p class="dim" style="margin-top:-4px">Chip filters combine with AND
      across categories, OR within a category. Filter state is captured
      in the URL — bookmark useful views.</p>

    {filter_bar}

    <div class="pattern-list-controls" style="margin-top:20px">
      <div class="sort-controls">
        <span class="dim">Sort:</span>
        <button class="chip" data-sort="due" data-order="asc">Next due ↑</button>
        <button class="chip" data-sort="last" data-order="desc">Last reviewed ↓</button>
        <button class="chip" data-sort="name" data-order="asc">Name A–Z</button>
      </div>
      <div class="expand-controls">
        <button class="chip" data-action="expand-all">Expand all</button>
        <button class="chip" data-action="collapse-all">Collapse all</button>
      </div>
    </div>

    <div class="pattern-list browse-items">{"".join(items_html)}</div>
    """
    return base_layout("Browse", body).replace("{root}", "")


def _render_expandable_list(title: str, entries: list[dict], root: str = "../",
                             back_link: str = "index.html",
                             subtitle: str | None = None) -> str:
    """Shared renderer for pattern pages and tag pages.

    Emits an expandable-list layout with sort chips + Expand/Collapse-all.
    `root` is the prefix used for links inside the entries (badges, problem
    pages). Every caller lives one directory deep under site/, so root="../"
    is the correct default.
    """
    today = date.today()
    entries_sorted = sorted(
        entries,
        key=lambda e: (
            not e.get("isNC150"),
            not e.get("isBlind75"),
            -DIFFICULTY_RANK.get(e.get("difficulty", "Unknown"), 0),
            e["task"],
        ),
    )

    items = []
    for entry in entries_sorted:
        qa = entry.get("qa") or {}
        next_due_iso = entry["sm2"].get("nextDue") or "9999-12-31"
        last_label, last_sort = format_last_reviewed(entry["sm2"], today)
        next_due_pretty = format_next_due(entry["sm2"].get("nextDue"))

        problem_html = ""
        if qa.get("problem"):
            problem_html = (
                '<div class="qa-mini-section">'
                '<div class="qa-mini-label">Problem</div>'
                f'<div class="qa-mini-body">{esc(qa["problem"])}</div>'
                '</div>'
            )
        answer_html = ""
        if qa.get("answer"):
            answer_html = (
                '<div class="qa-mini-section">'
                '<div class="qa-mini-label">Answer</div>'
                f'<div class="qa-mini-body">{esc(qa["answer"])}</div>'
                '</div>'
            )
        complexity_html = ""
        if qa.get("complexity"):
            complexity_html = (
                '<div class="qa-mini-section">'
                f'<div class="qa-complexity">{esc(qa["complexity"])}</div>'
                '</div>'
            )
        body_content = problem_html + answer_html + complexity_html
        if not body_content:
            body_content = '<div class="dim">No summary yet — open the full page.</div>'

        items.append(f"""
      <details class="pattern-item"
               data-name="{esc((entry.get('problemName') or entry['task']).lower())}"
               data-due="{esc(next_due_iso)}"
               data-last="{esc(last_sort)}">
        <summary>
          <span class="pattern-item-chevron">▸</span>
          <span class="pattern-item-title">
            <a href="{root}problems/{esc(entry['task'])}.html">{esc(entry.get('problemName') or entry['task'])}</a>
          </span>
          <span class="pattern-item-badges">{badges_for_problem(entry, root=root)}</span>
          <span class="pattern-item-col pattern-item-due"><span class="pattern-item-col-label">next</span>{esc(next_due_pretty)}</span>
          <span class="pattern-item-col pattern-item-last"><span class="pattern-item-col-label">last</span>{esc(last_label)}</span>
        </summary>
        <div class="pattern-item-body">
          {body_content}
          <a class="pattern-item-open" href="{root}problems/{esc(entry['task'])}.html">Open full page →</a>
        </div>
      </details>""")

    subtitle_html = f'<div class="dim" style="margin:-8px 0 16px">{esc(subtitle)}</div>' if subtitle else ""

    body = f"""
    <h2>{esc(title)} — {len(entries_sorted)} problems</h2>
    {subtitle_html}

    <div class="pattern-list-controls">
      <div class="sort-controls">
        <span class="dim">Sort:</span>
        <button class="chip" data-sort="due" data-order="asc">Next due ↑</button>
        <button class="chip" data-sort="last" data-order="desc">Last reviewed ↓</button>
        <button class="chip" data-sort="name" data-order="asc">Name A–Z</button>
      </div>
      <div class="expand-controls">
        <button class="chip" data-action="expand-all">Expand all</button>
        <button class="chip" data-action="collapse-all">Collapse all</button>
      </div>
    </div>

    <div class="pattern-list">{"".join(items)}</div>
    """
    return base_layout(title, body, back_link=back_link).replace("{root}", root)


def render_pattern_page(pattern: str, entries: list[dict]) -> str:
    return _render_expandable_list(pattern, entries)


def render_tag_page(title: str, entries: list[dict], subtitle: str | None = None) -> str:
    return _render_expandable_list(title, entries, subtitle=subtitle)


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
    prism_src = VENDOR_DIR / "prism"
    prism_dst = SITE_DIR / "assets" / "prism"
    if prism_src.exists():
        if prism_dst.exists():
            shutil.rmtree(prism_dst)
        shutil.copytree(prism_src, prism_dst)


def write(path: Path, content: str) -> None:
    path.write_text(content, encoding="utf-8")


def build() -> int:
    if not STATE_JSON.exists():
        print("ERROR: state.json missing. Run sync.py first.", file=sys.stderr)
        return 1

    global ASSET_VERSION
    ASSET_VERSION = compute_asset_version()

    state = json.loads(STATE_JSON.read_text())
    today = date.today()

    prepare_site_dir()
    copy_assets()

    # Dashboard
    write(SITE_DIR / "index.html", render_dashboard(state, today))

    # Problem pages. Compute this week's ordered queue once so each problem
    # page can render Prev/Next across the same set the dashboard shows.
    leetcode_index = build_leetcode_number_index(state)
    review_day = coming_saturday(today)
    week_tasks = [p["task"] for p in pick_queue(state, review_day, QUEUE_SIZE)]
    for problem in state["problems"].values():
        page = render_problem_page(problem, today, leetcode_index, week_tasks)
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

    # Browse (all-problems with facet filters)
    write(SITE_DIR / "browse.html", render_browse_page(state, today))

    print(f"Built site → {SITE_DIR.relative_to(REPO_ROOT)}")
    print(f"  problems: {len(state['problems'])}")
    print(f"  patterns: {len(by_pattern)}")
    print(f"  open: file://{SITE_DIR}/index.html")
    return 0


if __name__ == "__main__":
    sys.exit(build())

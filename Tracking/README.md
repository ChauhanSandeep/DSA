# Tracking — Weekly DSA Review System

A lightweight, git-tracked, offline-first system for keeping DSA knowledge
warm over a low-activity phase. Designed for a **weekly (Saturday)** review
cadence with **spaced repetition (SM-2)** over the problems already solved
in this repository.

> **Goal:** open a browser on Saturday, be shown 6 problems to review,
> grade each in one keystroke, and never let the tracker drift from the
> code. Everything else follows from that.

**Public dashboard (read-only):** https://chauhansandeep.github.io/DSA/  
**Operational runbook:** [`Tracking/OPERATIONS.md`](./OPERATIONS.md)

---

## Source of truth

| What                                     | Where                                                                 |
| ---------------------------------------- | --------------------------------------------------------------------- |
| Problem statement, intuition, code       | `src/main/java/**/*.java` class + method Javadocs (per WARP.md)       |
| Pattern taxonomy (NeetCode All)          | `Tracking/data/neetcode.json` — frozen after one-time scrape          |
| SM-2 state per problem                   | `Tracking/data/state.json` — mutable, checked into git                |
| Repo problems outside NeetCode           | `Tracking/data/extras.json`                                           |
| Class-name → Leetcode URL overrides      | `Tracking/data/manual-map.json`                                       |
| Curated anchor set (~50 problems)        | `Tracking/core.md` (regenerable via `curate_core.py`)                 |
| Generated study dashboard                | `Tracking/site/` (build artifact — safe to regenerate anytime)        |

The legacy Google Sheet export (`Tracking/Problems.html`) is retained as
a read-only archive and is **not** used by any script.

---

## Locked design decisions

| Decision                              | Value                                                                |
| ------------------------------------- | -------------------------------------------------------------------- |
| Review cadence                        | Weekly, Saturday                                                     |
| Queue size per weekend                | 6                                                                    |
| Grading                               | 4-button: ⭐ trivial · ✅ solved · 🟡 hint · 🔴 blank (keys 1/2/3/4)  |
| Interval cap                          | 180 days                                                             |
| Ease factor range                     | [1.3, 3.0]                                                           |
| Hosting                               | Local only — open `Tracking/site/index.html`                         |
| Scripts language                      | Python 3.10+                                                         |
| History backfill from old sheet       | No                                                                   |
| Initial `nextDue` staggering          | NC-matched (~150) over 10 weeks · extras (~300) over 26 weeks        |
| Ordering within a stagger slot        | Round-robin by pattern                                               |
| Matching key (repo ↔ NeetCode)        | Leetcode URL (from `🔗 Leetcode:` line in class Javadoc)             |
| Weekly nudge                          | GitHub Action opens a Saturday issue with the queue                  |

---

## SM-2 formula (implemented in `site/assets/app.js`)

Grade → keyboard:
- ⭐ **trivial** (1) — instant recall, no thought
- ✅ **solved** (2) — clean solve, no help
- 🟡 **hint** (3) — needed to peek or got briefly stuck
- 🔴 **blank** (4) — could not solve; re-read the solution

State per problem: `{ easeFactor, intervalDays, repetitions, lastReviewed, nextDue, lastGrade }`.

On review at date `d`:
```
if grade == "blank":
    repetitions   = 0
    intervalDays  = 3
    easeFactor   -= 0.20
elif grade == "hint":
    intervalDays  = max(3, round(intervalDays * 1.3))
    repetitions  += 1
    easeFactor   -= 0.15
elif grade == "solved":
    if   repetitions == 0: intervalDays = 7
    elif repetitions == 1: intervalDays = 14
    else:                  intervalDays = round(intervalDays * easeFactor)
    repetitions  += 1
    # easeFactor unchanged
elif grade == "trivial":
    if   repetitions == 0: intervalDays = 14
    elif repetitions == 1: intervalDays = 30
    else:                  intervalDays = round(intervalDays * easeFactor * 1.3)
    repetitions  += 1
    easeFactor   += 0.15

easeFactor   = clamp(easeFactor, 1.3, 3.0)

# Difficulty weighting — harder problems recur sooner, easier ones stretch out.
# Keyed by rating band (falls back to Leetcode difficulty). Mirrored in
# app.js and serve.py.
factor       = { "Very Hard": 0.5, "Hard": 0.65, "Medium": 1.0,
                 "Easy": 1.35, "Unknown": 1.0 }[difficulty]
intervalDays = max(3, round(intervalDays * factor))
intervalDays = min(intervalDays, 180)     # 6-month cap
lastReviewed = d
nextDue      = d + intervalDays
```

---

## Weekly queue algorithm

Two hardest-first lanes balance reinforcing old hard problems against
covering new ground. The revision lane is a *soft cap* filled by what's due
(≈2 early, up to 4 once a review backlog exists); the new lane takes the rest
of a 10-item queue (so ≈8 new / 2 revision early, drifting to 6 new / 4):

```
due = [p for p in state if not p.skip and p.nextDue <= today]

review_pool = [p for p in due if p.lastReviewed]      # depth
new_pool    = [p for p in due if not p.lastReviewed]  # breadth

sort review_pool by (difficulty desc, nextDue asc, easeFactor asc)
sort new_pool    by (difficulty desc, nextDue asc)

queue = review_pool[:4] + new_pool[:6]   # 4 = MAX_REVIEW_SLOTS (soft cap)

# Cross-backfill a short lane from the other lane's surplus, then from
# pinned anchors, so Saturday always has 10 problems.
```

Difficulty is the rating band (Very Hard / Hard / Medium / Easy), falling
back to the raw Leetcode difficulty. Combined with the difficulty-weighted
intervals above, hard problems both resurface sooner *and* jump to the front
of each lane.

---

## Directory layout (target)

```
Tracking/
├─ README.md                            # this file — decisions of record
├─ Problems.html                        # legacy sheet, archive only
├─ core.md                              # curated anchor list (Phase 4)
├─ data/
│  ├─ neetcode.json                     # frozen NC taxonomy
│  ├─ state.json                        # SM-2 state (source of truth for progress)
│  ├─ extras.json                       # repo problems outside NC
│  └─ manual-map.json                   # class-name → Leetcode URL overrides
├─ scripts/
│  ├─ scrape_neetcode.py                # one-shot NeetCode taxonomy scraper
│  ├─ sync.py                           # repo ↔ state.json sync
│  ├─ curate_core.py                    # anchor selection + core.md ↔ pinned flags
│  ├─ backfill_qa.py                    # one-shot Q/A backfill from legacy sheet + Javadoc
│  ├─ build.py                          # generate site/ from data + Javadocs
│  ├─ serve.py                          # local dev server; POST /api/grade writes state.json
│  ├─ queue_issue.py                    # markdown body for weekly-nudge issue
│  ├─ review_log.py                     # diff-based grade log for study-log issue
│  ├─ report.py                         # CLI coverage + queue preview
│  └─ _queue.py                         # shared queue-picker (with pinned backfill)
├─ vendor/
│  └─ prism/                            # pinned Prism.js + github-light/dark themes
├─ site-src/                            # tracked source assets (copied to site/assets)
│  ├─ styles.css
│  └─ app.js
└─ site/                                # generated — safe to delete + rebuild
   ├─ index.html
   ├─ problems/<Task>.html
   ├─ patterns/<Pattern>.html
   └─ assets/{app.js, styles.css, prism/}
```

Related workflows under `.github/workflows/`:
- `weekly-review-nudge.yml` — Saturday cron; opens the weekend-review issue.
- `log-review.yml` — on push to `state.json`; comments on the study-log issue.

---

## Usage (once built)

```
# One-time / after any repo change
python Tracking/scripts/sync.py           # refresh state.json from repo
python Tracking/scripts/curate_core.py    # (only when the anchor set changes)
python Tracking/scripts/build.py          # regenerate site/

# Saturday morning — start the local dev server and grade
python Tracking/scripts/serve.py          # http://127.0.0.1:8787 (default port)
open http://127.0.0.1:8787/index.html     # queue of 6, keys 1/2/3/4 to grade

# When done, commit progress
git commit -am "weekend review"           # state.json changes flow into git
```

`serve.py` writes to `Tracking/data/state.json` atomically on each grade
(no browser file-picker prompts, no permission dance) and re-runs
`build.py` in the background so the dashboard reflects your new
`nextDue` on the next reload. Kill with `Ctrl+C`.

If you'd rather open the site directly via `file://` (no server), the
grading UI still works — it falls back to the File System Access API
in Chrome/Edge, or to a "download JSON" flow elsewhere. But the server
path is meaningfully lower friction and is the recommended workflow.

---

## Roll-out phases

1. **Phase 0** — commit this README (locked decisions). ✅
2. **Phase 1** — data layer: `neetcode.json` (frozen), `sync.py`, coverage `report.py`. ✅
3. **Phase 2** — static site MVP: queue + per-problem pages + SM-2 grading. ✅
4. **Phase 3** — GitHub Actions for weekly nudge and review log. ✅
5. **Phase 4** — curate `core.md` anchor set (~50 problems) + queue backfill. ✅
6. **Phase 5** — polish (dark mode already done, streak, search, sqlite migration if it grows).

The detailed plan (with schemas, effort, and risks) lives outside the repo
in the session workspace and is discussed conversationally when needed.

---

## Non-goals

- Multi-user / auth / mobile-native.
- Reinventing Anki — SM-2 is enough for a personal, weekly, small-corpus system.
- Migrating the Google Sheet forward — it stays as an archive.
- Any runtime network call from the dashboard — everything is generated at
  build time and served from disk.

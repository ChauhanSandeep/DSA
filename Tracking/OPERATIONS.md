# Operations runbook — DSA weekend-review system

Everything you need to actually *run* the tracker: weekly ritual, GitHub
integration, and the how-to for every kind of change to the underlying
`src/main/java/` corpus.

Companion to `Tracking/README.md` (which documents the design). If the
two disagree, `Tracking/README.md` is the source of truth for **decisions**
and this file is the source of truth for **procedures**.

---

## 1. Mental model in one paragraph

`src/main/java/` is content. `Tracking/data/state.json` is the *state layer*
that spaced-repetition-schedules revisits of that content. Scripts under
`Tracking/scripts/` keep the two in sync and produce a static site under
`Tracking/site/` for reviewing. Two GitHub Actions turn `state.json`
into notifications so you don't have to remember to review. That's it.

---

## 2. Weekly ritual (the happy path)

Saturday morning. Total time: ~30-45 minutes for 6 problems.

```bash
# 1. Get the latest.
cd ~/Idea/DSA
git pull

# 2. Optional but recommended if you or CI changed anything since last week.
python Tracking/scripts/sync.py

# 3. Start the local server.
python Tracking/scripts/serve.py
#    → prints "serving at http://127.0.0.1:8787/"
#    → keep this terminal open for the whole session

# 4. In your browser.
open http://127.0.0.1:8787/
#    You'll see:
#      - 3 motivation panels (anchor health / pattern warmth / weekly dots)
#      - The queue: 6 problems for today
#    Click a problem → try to recall → Space to reveal answer →
#    Enter to reveal solution → 1/2/3/4 to grade.

# 5. When done, stop the server (Ctrl+C in the serve.py terminal).

# 6. Commit progress. This is what "registers" your review with GitHub.
git add Tracking/data/state.json
git commit -m "weekend review"
git push
```

That single `git push` fans out into three automatic effects (see
section 3 for details): the study-log Action posts a summary comment,
the Pages Action redeploys the public dashboard, and next Saturday's
nudge will show the updated queue. If you grade locally but never push,
none of that fires — you just have uncommitted state on disk.

### Confirm it worked (~90 seconds after push)

```bash
# Deploy Action should be green.
gh run list --workflow=deploy-pages.yml --limit 1

# Log Action should be green and the pinned issue should have a fresh comment.
gh run list --workflow=log-review.yml --limit 1
gh issue list --label study-log --state open

# The public dashboard reflects the new state.
open https://chauhansandeep.github.io/DSA/
```

You should see: `✓ Solved today` chips on the graded queue items, this
week's dot filled in on the "Last 12 weeks" panel, and the anchor
health progress bar / warmth tiles nudged toward green.

---

## 3. GitHub integration

Three workflows under `.github/workflows/` do all the automation. All use
only the built-in `GITHUB_TOKEN` — no PATs, no secrets.

### `weekly-review-nudge.yml`

- Runs every **Saturday 02:30 UTC (08:00 IST)** on cron + supports
  `workflow_dispatch` for on-demand.
- Steps: `sync.py` → `queue_issue.py` → `gh issue create/edit`.
- Result: a GitHub issue titled *"Weekend review — YYYY-MM-DD · 6 problems"*
  labeled `review-queue`. Body has your 6 problems with Leetcode + source
  links.
- Prior week's `review-queue` issues are auto-closed with a supersession
  comment.
- You get a GitHub notification. That's your prompt to run the ritual.

### `log-review.yml`

- Triggers on every push to `master` that touches `Tracking/data/state.json`.
- Runs `review_log.py --base HEAD^` to diff old vs new history entries.
- If new grades were recorded, posts a Markdown comment on the pinned
  *"📓 Study log — DSA reviews"* issue (auto-created on first run).
- If the push had no grade diff (e.g. sync-only churn), silently exits.

### `deploy-pages.yml`

- Triggers on every push to `master` that changes anything the site
  renders: `Tracking/data/**`, `Tracking/scripts/**`,
  `Tracking/site-src/**`, `Tracking/vendor/**`, or `src/main/java/**`.
- Runs `build.py` on a GitHub runner and publishes the resulting
  `Tracking/site/` to **GitHub Pages** — a public, read-only view of
  your queue, Q/A cards, patterns, and syntax-highlighted source.
- Live at **https://chauhansandeep.github.io/DSA/**.
- The published dashboard runs in *read-only mode*: it pings
  `/api/ping` on load, finds no local server, and hides the grade bar
  + shows a banner reminding you that grading needs `serve.py` locally.

### One-time GitHub setup

Do these once when you first push these workflows:

1. Push to GitHub: `git push origin master`.
2. **Settings → Actions → General → Workflow permissions** → select
   **"Read and write permissions"**. Save. Needed by nudge + log workflows.
3. **Settings → Pages → Build and deployment → Source** → set to
   **"GitHub Actions"**. (No branch selection — the workflow itself
   uploads the artifact.)
4. Trigger `weekly-review-nudge.yml` manually via **Actions →
   Weekly review nudge → Run workflow → master**. Verify the issue
   appears under **Issues** with label `review-queue`.
5. Trigger `deploy-pages.yml` manually the same way. Verify a green
   run, then visit the URL shown on the Pages settings page
   (`https://<user>.github.io/DSA/`) — the read-only dashboard
   should load with the banner.

If any of these fail with a permissions error, revisit step 2.

### Ongoing cost

- Workflow runs are on free-tier GitHub Actions and cost <60 sec per
  push. Effectively free.
- No secrets to rotate. No PAT to expire. `GITHUB_TOKEN` is
  auto-generated per run.
- Pages URL is stable; no DNS / cert to maintain.

### Pages vs local — which to use when

| Situation | Where |
|---|---|
| Saturday morning grading ritual | Local (`serve.py`) — Pages is read-only |
| Curious what's in the queue while you're on your phone | Pages |
| Someone on your team asks about a problem you've solved | Pages (share the deep link) |
| Reviewing a specific pattern before an interview | Either — Pages has the same content |
| Editing state.json (renames, migrations) | Local — Pages can't write back |

---

## 4. Change scenarios — what to run when you touch the repo

The single rule: **after any change to `src/main/java/`, re-run `sync.py`
followed by `build.py`** (or just restart `serve.py`, which auto-rebuilds
on grade but you may want a manual rebuild to see the new queue). The
subsections below cover the nuances.

### 4a. Edit an existing problem's code or Javadoc

The most common case: you polish a `.java` file, improve the intuition
block, fix a bug in the code.

```bash
# Nothing forced -- but rebuilding shows the change.
python Tracking/scripts/build.py
```

Behavior:
- `state.json` entry for that class is **untouched** (task, sm2, history,
  qa are all preserved).
- Next `build.py` rebuild picks up the new source. If the Q/A card was
  originally auto-extracted from Javadoc (`qa.source == "javadoc"`), you
  can force a re-extraction:

    ```bash
    python Tracking/scripts/backfill_qa.py
    ```

  This re-parses Javadocs but respects the priority: `user > sheet >
  javadoc > missing`. Your sheet-sourced cards are not overwritten. Only
  javadoc-sourced entries refresh.

### 4b. Add a brand-new problem

```bash
# 1. Create the file per WARP.md conventions.
$EDITOR src/main/java/<pattern>/<Task>.java
#    Include:
#      ✅ Problem:  <one-liner>
#      🔗 Leetcode: <url>
#      🏷️ Pattern:  <NC pattern name>
#      Method Javadoc with 🧠 Intuition + Time/Space.

# 2. Re-sync. Adds the new entry to state.json with a staggered nextDue.
python Tracking/scripts/sync.py

# 3. Optional: pull the Q/A into the card. If it's a new problem that
#    was already in the legacy sheet (rare), it'll come from there.
python Tracking/scripts/backfill_qa.py

# 4. Optional: pin as an anchor if it deserves core-set status.
$EDITOR Tracking/core.md          # add a row for it
python Tracking/scripts/curate_core.py

# 5. Rebuild.
python Tracking/scripts/build.py

# 6. Commit.
git add src/main/java Tracking/data/state.json Tracking/core.md
git commit -m "add <ProblemName>"
```

`sync.py` uses the **Leetcode URL as the join key**, so as long as
`🔗 Leetcode:` is in your class Javadoc, the problem is matched against
the frozen NeetCode taxonomy. If the URL isn't in NeetCode's list, it
lands in `extras.json` with the pattern inferred from the directory.

### 4c. Rename a class (same file, different name)

**Warning:** the SM-2 state, review history, and Q/A are keyed by the
class name. A rename **without migration** loses that state.

Two safe approaches:

**Option 1 — Migrate manually (recommended for a single rename).**

```bash
# Before you rename, capture the old entry.
python -c "
import json
s = json.load(open('Tracking/data/state.json'))
print(json.dumps(s['problems']['OldClassName'], indent=2))
" > /tmp/old-entry.json

# Rename the .java file + class inside it. Run sync.
python Tracking/scripts/sync.py
#   -> reports 'removed: OldClassName' and 'added: NewClassName'.

# Copy the state fields to the new entry.
python <<'PY'
import json
s = json.load(open('Tracking/data/state.json'))
old = json.load(open('/tmp/old-entry.json'))
new_task = 'NewClassName'
for k in ('sm2', 'history', 'userNotes', 'flags', 'qa'):
    if k in old:
        s['problems'][new_task][k] = old[k]
open('Tracking/data/state.json', 'w').write(
    json.dumps(s, indent=2, ensure_ascii=False) + '\n'
)
PY

python Tracking/scripts/build.py
```

**Option 2 — Add a rename map to `sync.py`.**

If you're doing many renames, extend `sync.py` with a class-rename
dictionary (a few lines under the existing `manual-map.json` logic) so
the migration is one-shot and idempotent. Ping the plan if you get here.

### 4d. Move a file to a different directory (class name unchanged)

Zero-loss case.

```bash
git mv src/main/java/old/pkg/Foo.java src/main/java/new/pkg/Foo.java
# Update the `package` line inside Foo.java to match.
python Tracking/scripts/sync.py
#   -> reports 0 removed, 0 added; only `javaFile` is refreshed.
python Tracking/scripts/build.py
```

The class-name-based `task_id` stays put; the entry's `javaFile` field
updates, everything else preserved.

### 4e. Delete a problem

```bash
git rm src/main/java/pattern/DeadProblem.java
python Tracking/scripts/sync.py
#   -> prints 'removed: DeadProblem' and drops the entry from state.json.
```

You lose the review history for that class, which is fine because the
problem is gone. Commit both changes together.

### 4f. Duplicate class names across packages

Already handled: `sync.py` disambiguates as `<ClassName>__<lastPkgSegment>`.
See `Tracking/data/state.json` for existing examples
(`PalindromePairs__hashing`, `MergeKLists`, `InsertInterval__greedy`, …).
If you introduce a *new* collision, `sync.py` will detect it and rename
the newly-added one; existing entries keep their disambiguated task_id.

### 4g. Big repo pull with many changes

```bash
git pull
python Tracking/scripts/sync.py     # digests all repo changes at once
python Tracking/scripts/backfill_qa.py   # only if new files were added
python Tracking/scripts/build.py
```

If `sync.py` reports removals you didn't expect, someone else (or CI)
renamed/deleted files. Follow the rename or delete recipe above.

---

## 5. Hygiene rules

Small habits that keep the system healthy across a hibernation year.

- **Never hand-edit `state.json`** unless you *really* know what you're
  doing (e.g. the rename migration in 4c). Use `serve.py`, `sync.py`,
  or `curate_core.py`.
- **Never commit `Tracking/site/`** — already gitignored. If you find it
  tracked somehow, `git rm -r --cached Tracking/site` and re-check the
  gitignore.
- **Commit `state.json` whenever it changes.** Git history is your
  progress log; regular commits let the log-review Action work and
  keep `nextDue` values pushed to any machine you'd use.
- **Rebuild after every state change** you care about seeing. `serve.py`
  does this automatically for grades. For sync/curate/backfill changes,
  run `build.py` manually or restart `serve.py`.
- **Push at least weekly.** The Study log Action only fires on push,
  and the weekly nudge references the pushed state — a local-only
  Saturday burst without a push means the following Saturday's nudge
  won't reflect it.
- **Don't grade offline.** If `serve.py` is not running when you click
  a grade button, the client falls back to the FS Access API path
  (Chrome only, with a permission prompt). To avoid the prompt path
  entirely, always start `serve.py` before grading.

---

## 6. Backups and recovery

- **`state.json` is versioned in git.** Any bad edit is one
  `git checkout -- Tracking/data/state.json` away from being fixed.
- **`site/` is disposable.** Deleting it is safe:
  `rm -rf Tracking/site && python Tracking/scripts/build.py`.
- **NeetCode taxonomy drift.** `data/neetcode.json` is frozen. If you
  ever want to refresh it (e.g. NeetCode 250 gets new problems),
  re-run `python Tracking/scripts/scrape_neetcode.py`, then sync.
  `pattern` / `difficulty` / `isNC150` fields on existing entries update;
  SM-2 state is preserved.
- **Full reset (nuclear).** Wipes progress but keeps content:
  ```bash
  rm Tracking/data/state.json
  python Tracking/scripts/sync.py             # fresh seed, all staggered
  python Tracking/scripts/curate_core.py      # re-apply pinned flags
  python Tracking/scripts/backfill_qa.py      # re-populate Q/A
  python Tracking/scripts/build.py
  ```

---

## 7. Common failures and fixes

| Symptom | Cause | Fix |
|---|---|---|
| Grade button toasts "Server error: HTTP 404" | serve.py restarted since dashboard load | Refresh the page; then re-grade |
| Grade button toasts "Recorded locally" | serve.py isn't running | Start `python Tracking/scripts/serve.py`, refresh, re-grade |
| Dashboard queue looks wrong after grading | Site not rebuilt yet | Refresh; or bounce serve.py; or run `build.py` |
| serve.py crashes on grade | state.json malformed | `git checkout -- Tracking/data/state.json`; re-grade |
| Weekly-nudge issue doesn't appear on Saturday | Workflow permissions revoked | Repo Settings → Actions → Read+write; re-run workflow |
| Study log issue comment missing after push | The push had no `history[]` diff | Expected — only fires when grades change |
| `sync.py` reports many "removed" entries | Someone did a bulk rename | Migrate per 4c; or accept the SM-2 loss for that batch |
| Prism syntax highlighting looks off | Cached CSS | Hard refresh (Cmd+Shift+R); if that fails, delete `site/` and rebuild |
| A pattern page has no items | Not-yet-supported new NC pattern | Add the pattern name to `DIR_PATTERN_RULES` in `sync.py` |

---

## 8. Health checks (run these once a quarter)

```bash
# Are all Java files with URLs being tracked?
python Tracking/scripts/report.py

# Is state.json shape still sane?
python -c "
import json
s = json.load(open('Tracking/data/state.json'))
p = s['problems']
required = ['task','javaFile','pattern','difficulty','sm2','history','flags','source','qa']
missing = {k:0 for k in required}
for e in p.values():
    for k in required:
        if k not in e: missing[k]+=1
print('entries:', len(p))
print('missing per key:', {k:v for k,v in missing.items() if v})
"

# Idempotency: two consecutive syncs should report 0 added / 0 removed.
python Tracking/scripts/sync.py --dry-run
python Tracking/scripts/sync.py --dry-run
```

Anything unexpected → check the changelog (`git log Tracking/`) for a
recent change that broke assumptions.

---

## 9. Files you can safely delete/regenerate

| File / dir | Regenerable? | How |
|---|---|---|
| `Tracking/site/` | Yes | `python Tracking/scripts/build.py` |
| `Tracking/data/neetcode.json` | Yes | `python Tracking/scripts/scrape_neetcode.py` |
| `Tracking/data/extras.json` | Yes | `python Tracking/scripts/sync.py` |
| `Tracking/data/state.json` | **Progress lost** | `sync.py` re-seeds but SM-2 history goes to zero |
| `Tracking/core.md` | Yes | `python Tracking/scripts/curate_core.py` (auto-selects) |
| `Tracking/data/manual-map.json` | Yes (empty) | `sync.py` re-creates as `{"byJavaClass": {}}` |
| `Tracking/vendor/prism/` | Yes | Re-fetch from cdnjs at the same pinned version |
| `Tracking/Problems.html` | Local reference only | Re-export from the Google Sheet |

---

## 10. Signals it's working

If in six months you look at your repo and any of these are true, the
system is doing its job:

- The pinned *"📓 Study log"* issue has a comment for each weekend
  you actually reviewed.
- `state.json`'s per-problem `sm2.repetitions` is >0 for a growing
  fraction of anchors.
- The Pattern warmth heatmap has more green than red.
- Your last few `git log` entries include `"weekend review"` commits
  at ~weekly cadence.

If none of these are true after two months, either the ritual isn't
sticking or the nudge isn't being seen. Consider:
- Turning on GitHub notification email for the repo so the Saturday
  issue actually pings you.
- Reducing the queue size from 6 to 3 (edit `QUEUE_SIZE` in
  `Tracking/scripts/_queue.py`) so the weekly ask feels easier.

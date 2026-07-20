/* -----------------------------------------------------------------------------
 * app.js — client-side logic for the weekly-review dashboard.
 *
 * Responsibilities:
 *   1. Own the in-memory state (seeded from window.__STATE__ baked in at
 *      build time by build.py).
 *   2. Apply the SM-2 algorithm on grade clicks (4-button: trivial/solved/
 *      hint/blank).
 *   3. Persist updates:
 *        - primary : File System Access API — one-click write to
 *                    Tracking/data/state.json, handle cached in IndexedDB
 *                    so subsequent sessions never re-prompt.
 *        - fallback: localStorage-backed pending queue + Export JSON button.
 *   4. Provide keyboard shortcuts (1/2/3/4) on problem pages and reflect
 *      pending changes on the dashboard.
 *
 * No framework, no bundler, no runtime network calls.
 * -------------------------------------------------------------------------- */

(function () {
  "use strict";

  const STATE_STORAGE_KEY = "dsa-tracker.pending-v1";
  const HANDLE_DB = "dsa-tracker";
  const HANDLE_STORE = "handles";
  const HANDLE_KEY = "state-json";

  // Interval cap keeps SM-2 honest for weekly cadence (see Tracking/README.md).
  const MAX_INTERVAL_DAYS = 180;
  const MIN_EASE = 1.3;
  const MAX_EASE = 3.0;

  // --------------------------------------------------------------------------
  // SM-2 algorithm — exact formula documented in Tracking/README.md.
  // --------------------------------------------------------------------------
  function applySm2(currentSm2, grade, today) {
    const sm2 = Object.assign({}, currentSm2);
    sm2.easeFactor    = sm2.easeFactor    ?? 2.5;
    sm2.intervalDays  = sm2.intervalDays  ?? 0;
    sm2.repetitions   = sm2.repetitions   ?? 0;

    switch (grade) {
      case "blank":
        sm2.repetitions  = 0;
        sm2.intervalDays = 3;
        sm2.easeFactor  -= 0.20;
        break;

      case "hint":
        sm2.intervalDays = Math.max(3, Math.round(sm2.intervalDays * 1.3));
        sm2.repetitions += 1;
        sm2.easeFactor  -= 0.15;
        break;

      case "solved":
        if      (sm2.repetitions === 0) sm2.intervalDays = 7;
        else if (sm2.repetitions === 1) sm2.intervalDays = 14;
        else                            sm2.intervalDays = Math.round(sm2.intervalDays * sm2.easeFactor);
        sm2.repetitions += 1;
        break;

      case "trivial":
        if      (sm2.repetitions === 0) sm2.intervalDays = 14;
        else if (sm2.repetitions === 1) sm2.intervalDays = 30;
        else                            sm2.intervalDays = Math.round(sm2.intervalDays * sm2.easeFactor * 1.3);
        sm2.repetitions += 1;
        sm2.easeFactor  += 0.15;
        break;

      default:
        throw new Error(`Unknown grade: ${grade}`);
    }

    sm2.easeFactor   = clamp(sm2.easeFactor, MIN_EASE, MAX_EASE);
    sm2.intervalDays = Math.min(sm2.intervalDays, MAX_INTERVAL_DAYS);
    sm2.lastReviewed = isoDate(today);
    sm2.nextDue      = isoDate(addDays(today, sm2.intervalDays));
    sm2.lastGrade    = grade;
    return sm2;
  }

  function clamp(x, lo, hi) { return Math.min(Math.max(x, lo), hi); }
  function addDays(d, n)    { const c = new Date(d); c.setDate(c.getDate() + n); return c; }
  function isoDate(d)       { return d.toISOString().slice(0, 10); }

  // --------------------------------------------------------------------------
  // Pending changes — localStorage overlay used until state.json is written.
  // --------------------------------------------------------------------------
  function loadPending() {
    try { return JSON.parse(localStorage.getItem(STATE_STORAGE_KEY) || "{}"); }
    catch (_) { return {}; }
  }

  function savePending(pending) {
    localStorage.setItem(STATE_STORAGE_KEY, JSON.stringify(pending));
  }

  function clearPending() {
    localStorage.removeItem(STATE_STORAGE_KEY);
  }

  function pendingCount() {
    return Object.keys(loadPending()).length;
  }

  // --------------------------------------------------------------------------
  // Merged state = baked-in window.__STATE__ + pending overlay.
  // --------------------------------------------------------------------------
  function getMergedState() {
    const base = structuredClone(window.__STATE__ || { problems: {} });
    const pending = loadPending();
    for (const [task, patch] of Object.entries(pending)) {
      if (base.problems[task]) {
        base.problems[task] = { ...base.problems[task], ...patch };
      }
    }
    return base;
  }

  // --------------------------------------------------------------------------
  // File System Access API — one-click persistence.
  // Handle is cached in IndexedDB so it survives page reloads.
  // --------------------------------------------------------------------------
  function idb() {
    return new Promise((resolve, reject) => {
      const req = indexedDB.open(HANDLE_DB, 1);
      req.onupgradeneeded = () => {
        req.result.createObjectStore(HANDLE_STORE);
      };
      req.onsuccess = () => resolve(req.result);
      req.onerror   = () => reject(req.error);
    });
  }

  async function storeHandle(handle) {
    const db = await idb();
    return new Promise((resolve, reject) => {
      const tx = db.transaction(HANDLE_STORE, "readwrite");
      tx.objectStore(HANDLE_STORE).put(handle, HANDLE_KEY);
      tx.oncomplete = () => resolve();
      tx.onerror    = () => reject(tx.error);
    });
  }

  async function loadHandle() {
    const db = await idb();
    return new Promise((resolve) => {
      const tx = db.transaction(HANDLE_STORE, "readonly");
      const req = tx.objectStore(HANDLE_STORE).get(HANDLE_KEY);
      req.onsuccess = () => resolve(req.result || null);
      req.onerror   = () => resolve(null);
    });
  }

  async function ensureWritePermission(handle) {
    const opts = { mode: "readwrite" };
    if ((await handle.queryPermission(opts)) === "granted") return true;
    if ((await handle.requestPermission(opts)) === "granted") return true;
    return false;
  }

  async function pickStateFile() {
    if (!window.showOpenFilePicker) return null;
    const [handle] = await window.showOpenFilePicker({
      types: [{ description: "state.json", accept: { "application/json": [".json"] } }],
      excludeAcceptAllOption: false,
      multiple: false,
    });
    if (!handle) return null;
    if (!(await ensureWritePermission(handle))) return null;
    await storeHandle(handle);
    return handle;
  }

  async function writeStateFile(handle, stateObject) {
    const writable = await handle.createWritable();
    await writable.write(JSON.stringify(stateObject, null, 2) + "\n");
    await writable.close();
  }

  async function persistAllToStateFile() {
    let handle = await loadHandle();
    if (handle && !(await ensureWritePermission(handle))) handle = null;
    if (!handle) handle = await pickStateFile();
    if (!handle) throw new Error("No file handle available");

    // Read the CURRENT state.json from disk first. This is the correct
    // "source of truth" — pages only have a subset (e.g. problem pages
    // only inline this problem's entry). We merge the localStorage
    // overlay on top of the on-disk state so grades made on a problem
    // page never wipe out other entries.
    const file = await handle.getFile();
    const text = await file.text();
    let stateOnDisk;
    try {
      stateOnDisk = JSON.parse(text);
    } catch (e) {
      throw new Error("Could not parse state.json on disk: " + e.message);
    }
    if (!stateOnDisk || !stateOnDisk.problems) {
      throw new Error("state.json on disk has unexpected shape.");
    }

    const pending = loadPending();
    let applied = 0;
    for (const [task, patch] of Object.entries(pending)) {
      if (stateOnDisk.problems[task]) {
        stateOnDisk.problems[task] = { ...stateOnDisk.problems[task], ...patch };
        applied++;
      }
    }

    await writeStateFile(handle, stateOnDisk);
    clearPending();
    return applied;
  }

  // --------------------------------------------------------------------------
  // Download fallback — user picks state.json manually.
  // --------------------------------------------------------------------------
  function downloadStateJson() {
    const merged = getMergedState();
    const blob = new Blob(
      [JSON.stringify(merged, null, 2) + "\n"],
      { type: "application/json" }
    );
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "state.json";
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  }

  // --------------------------------------------------------------------------
  // Toast + banner UI helpers.
  // --------------------------------------------------------------------------
  function toast(message, kind = "info", ms = 3200) {
    const el = document.createElement("div");
    el.className = `toast ${kind}`;
    el.textContent = message;
    document.body.appendChild(el);
    setTimeout(() => el.remove(), ms);
  }

  function renderPendingBanner() {
    const banner = document.getElementById("pending-banner");
    if (!banner) return;
    const count = pendingCount();
    if (count === 0) {
      banner.classList.add("hidden");
      return;
    }
    banner.classList.remove("hidden");
    banner.querySelector(".count").textContent = String(count);
  }

  // --------------------------------------------------------------------------
  // Local server persistence (preferred when serve.py is running).
  // POST /api/grade → server updates state.json atomically and returns the
  // new SM-2 state so the toast can show the accurate next-review date.
  // --------------------------------------------------------------------------
  async function persistViaServer(task, grade) {
    if (location.protocol !== "http:" && location.protocol !== "https:") {
      return null;
    }
    try {
      const res = await fetch("/api/grade", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ task, grade }),
      });
      if (!res.ok) {
        if (res.status === 404) return null;
        const err = await res.json().catch(() => ({}));
        throw new Error(err.error || `HTTP ${res.status}`);
      }
      return await res.json();
    } catch (err) {
      if (err && err.message && err.message.includes("Failed to fetch")) return null;
      throw err;
    }
  }

  // --------------------------------------------------------------------------
  // Grading (problem pages).
  // --------------------------------------------------------------------------
  async function grade(task, gradeName) {
    // Prefer the local server if reachable — one round-trip, no browser
    // permission dance, state.json written atomically by Python.
    let serverResult = null;
    try {
      serverResult = await persistViaServer(task, gradeName);
    } catch (err) {
      toast(`Server error: ${err.message}`, "error", 4500);
      return;
    }

    if (serverResult && serverResult.ok) {
      const nextDueStr = new Date(serverResult.sm2.nextDue).toLocaleDateString(undefined, {
        weekday: "short", year: "numeric", month: "short", day: "numeric",
      });
      toast(`Saved · next review ${nextDueStr}`, "success");
      // Keep localStorage clear so the pending banner doesn't misfire.
      const pending = loadPending();
      if (pending[task]) { delete pending[task]; savePending(pending); }
      renderPendingBanner();
      return;
    }

    // ---- Fallback: no server, use in-page state + File System Access API ----
    const merged = getMergedState();
    const problem = merged.problems[task];
    if (!problem) {
      toast(`Unknown problem: ${task}. Start serve.py or reload after rebuild.`, "error", 4500);
      return;
    }

    const today = new Date();
    const newSm2 = applySm2(problem.sm2 || {}, gradeName, today);
    const history = Array.isArray(problem.history) ? problem.history.slice() : [];
    history.push({ date: isoDate(today), grade: gradeName });

    const pending = loadPending();
    pending[task] = { sm2: newSm2, history };
    savePending(pending);

    const nextDueStr = new Date(newSm2.nextDue).toLocaleDateString(undefined, {
      weekday: "short", year: "numeric", month: "short", day: "numeric",
    });

    try {
      await persistAllToStateFile();
      toast(`Saved (via file system) · next review ${nextDueStr}`, "success");
      renderPendingBanner();
      return;
    } catch (err) {
      toast(
        `Recorded locally · next review ${nextDueStr}. ` +
        `Run \`python Tracking/scripts/serve.py\` for one-click saves, ` +
        `or use "Save all" on the dashboard.`,
        "info",
        6000
      );
      renderPendingBanner();
    }
  }

  // --------------------------------------------------------------------------
  // Wire up the page.
  // --------------------------------------------------------------------------
  function initProblemPage() {
    const container = document.querySelector("[data-task]");
    if (!container) return;
    const task = container.dataset.task;

    document.querySelectorAll(".grade-btn").forEach((btn) => {
      btn.addEventListener("click", () => grade(task, btn.dataset.grade));
    });

    document.addEventListener("keydown", (e) => {
      if (e.target.closest("input, textarea")) return;
      const map = { "1": "trivial", "2": "solved", "3": "hint", "4": "blank" };
      const g = map[e.key];
      if (g) {
        e.preventDefault();
        grade(task, g);
      }
    });
  }

  function initDashboard() {
    const banner = document.getElementById("pending-banner");
    if (!banner) return;

    renderPendingBanner();

    const saveBtn = banner.querySelector("[data-action='save-all']");
    const dlBtn = document.querySelector("[data-action='download-state']");
    if (saveBtn) {
      saveBtn.addEventListener("click", async () => {
        try {
          await persistAllToStateFile();
          toast("state.json written. Run git commit when ready.", "success");
          renderPendingBanner();
        } catch (err) {
          toast(`Could not write: ${err.message}. Using download fallback.`, "error");
          downloadStateJson();
        }
      });
    }
    if (dlBtn) {
      dlBtn.addEventListener("click", downloadStateJson);
    }
  }

  document.addEventListener("DOMContentLoaded", () => {
    initReadOnlyMode();
    initProblemPage();
    initDashboard();
    initThemeToggle();
    initQaCard();
    initPatternPage();
    initBrowsePage();
  });

  // --------------------------------------------------------------------------
  // Read-only mode detection.
  // The site is deployed to GitHub Pages for browse-anywhere access, but
  // grading is only supported when serve.py is running locally. If we can't
  // reach /api/ping quickly, mark the body so CSS can hide the grade bar
  // and any "Save all" affordances, and surface a banner explaining why.
  // --------------------------------------------------------------------------
  async function initReadOnlyMode() {
    // file:// or http(s): both may or may not have serve.py behind them.
    let reachable = false;
    if (location.protocol === "http:" || location.protocol === "https:") {
      try {
        const controller = new AbortController();
        const timer = setTimeout(() => controller.abort(), 800);
        const res = await fetch("/api/ping", {
          method: "GET",
          signal: controller.signal,
          cache: "no-store",
        });
        clearTimeout(timer);
        reachable = res.ok;
      } catch (_) {
        reachable = false;
      }
    }
    if (!reachable) {
      document.body.classList.add("read-only");
      injectReadOnlyBanner();
    }
  }

  function injectReadOnlyBanner() {
    if (document.getElementById("readonly-banner")) return;
    const banner = document.createElement("div");
    banner.id = "readonly-banner";
    banner.className = "readonly-banner";
    banner.innerHTML =
      '<span class="readonly-banner-icon">🔒</span>' +
      '<span>Read-only view. Grade this weekend\'s queue locally with ' +
      '<code>python Tracking/scripts/serve.py</code>.</span>';
    const container = document.querySelector(".container");
    const header = document.querySelector(".site-header");
    if (container && header) {
      header.after(banner);
    } else if (container) {
      container.prepend(banner);
    } else {
      document.body.prepend(banner);
    }
  }

  // --------------------------------------------------------------------------

  // --------------------------------------------------------------------------
  // Theme toggle. Persists to localStorage; auto-detect via
  // prefers-color-scheme when no explicit preference is set. Data-theme
  // attribute on <html> is applied inline BEFORE this script runs to avoid
  // FOUC (see THEME_INIT in build.py).
  // --------------------------------------------------------------------------
  const THEME_KEY = "dsa-tracker.theme";

  function currentTheme() {
    const explicit = document.documentElement.getAttribute("data-theme");
    if (explicit) return explicit;
    return window.matchMedia("(prefers-color-scheme: dark)").matches
      ? "dark" : "light";
  }

  function applyTheme(theme) {
    if (theme === "dark" || theme === "light") {
      document.documentElement.setAttribute("data-theme", theme);
      localStorage.setItem(THEME_KEY, theme);
    } else {
      document.documentElement.removeAttribute("data-theme");
      localStorage.removeItem(THEME_KEY);
    }
    updatePrismTheme(theme || currentTheme());
  }

  function toggleTheme() {
    applyTheme(currentTheme() === "dark" ? "light" : "dark");
  }

  function updatePrismTheme(theme) {
    const light = document.getElementById("prism-light");
    const dark = document.getElementById("prism-dark");
    if (!light || !dark) return;
    if (theme === "dark") {
      light.media = "not all";
      dark.media = "all";
    } else if (theme === "light") {
      light.media = "all";
      dark.media = "not all";
    } else {
      light.media = "(prefers-color-scheme: light)";
      dark.media = "(prefers-color-scheme: dark)";
    }
  }

  function initThemeToggle() {
    const btn = document.querySelector("[data-action='toggle-theme']");
    if (btn) btn.addEventListener("click", toggleTheme);
    updatePrismTheme(currentTheme());
  }

  // --------------------------------------------------------------------------
  // Q/A card reveal (Space → answer, Enter → source solution).
  // --------------------------------------------------------------------------
  function initQaCard() {
    const answerBlock = document.querySelector(".qa-answer");
    const revealHint = document.querySelector(".qa-reveal-hint");
    const solution = document.querySelector("details.reveal");

    function revealAnswer() {
      if (!answerBlock) return;
      answerBlock.classList.remove("qa-hidden");
      if (revealHint) revealHint.classList.add("hidden");
    }
    function toggleSolution() {
      if (solution) solution.open = !solution.open;
    }

    if (revealHint) revealHint.addEventListener("click", revealAnswer);

    document.addEventListener("keydown", (e) => {
      if (e.target.closest("input, textarea, button")) return;
      if (e.code === "Space") {
        e.preventDefault();
        revealAnswer();
      } else if (e.key === "Enter") {
        e.preventDefault();
        toggleSolution();
      }
    });
  }

  // --------------------------------------------------------------------------
  // Pattern page: sort by data attribute + expand/collapse-all.
  // --------------------------------------------------------------------------
  function initPatternPage() {
    const list = document.querySelector(".pattern-list");
    if (!list) return;

    const items = Array.from(list.querySelectorAll(".pattern-item"));
    let currentSort = { key: "due", order: "asc" };

    function sortBy(key, order) {
      const attr = "data-" + key;
      items.sort((a, b) => {
        const va = (a.getAttribute(attr) || "").toLowerCase();
        const vb = (b.getAttribute(attr) || "").toLowerCase();
        if (va < vb) return order === "asc" ? -1 : 1;
        if (va > vb) return order === "asc" ? 1 : -1;
        return 0;
      });
      const frag = document.createDocumentFragment();
      items.forEach((el) => frag.appendChild(el));
      list.appendChild(frag);
      currentSort = { key, order };
      updateSortButtons();
    }

    function updateSortButtons() {
      document.querySelectorAll(".sort-controls .chip[data-sort]").forEach((btn) => {
        const key = btn.getAttribute("data-sort");
        btn.classList.toggle("active", key === currentSort.key);
      });
    }

    document.querySelectorAll(".sort-controls .chip[data-sort]").forEach((btn) => {
      btn.addEventListener("click", () => {
        const key = btn.getAttribute("data-sort");
        // Toggle order if already active; otherwise use the button's default.
        let order = btn.getAttribute("data-order") || "asc";
        if (currentSort.key === key) {
          order = currentSort.order === "asc" ? "desc" : "asc";
        }
        sortBy(key, order);
      });
    });

    const expandBtn = document.querySelector("[data-action='expand-all']");
    const collapseBtn = document.querySelector("[data-action='collapse-all']");
    if (expandBtn) expandBtn.addEventListener("click", () =>
      items.forEach((el) => (el.open = true)));
    if (collapseBtn) collapseBtn.addEventListener("click", () =>
      items.forEach((el) => (el.open = false)));

    // Apply the initial sort so the DOM matches the "active" chip.
    sortBy(currentSort.key, currentSort.order);
  }

  // --------------------------------------------------------------------------
  // Browse page: facet chip filter engine + URL sync.
  // Semantics:
  //   - Active chips in the same facet are OR'd (Easy or Medium).
  //   - Facets are AND'd (must satisfy every non-empty facet).
  //   - Empty facet = no constraint.
  //   - Filter state serializes to URL query params:
  //       ?nc=1&diff=hard,medium&pattern=trees
  // --------------------------------------------------------------------------
  function initBrowsePage() {
    const list = document.querySelector(".browse-items");
    if (!list) return;

    const items = Array.from(list.querySelectorAll(".browse-item"));
    const chips = Array.from(document.querySelectorAll(".filter-chip"));
    const countEl = document.querySelector(".browse-count");
    const clearBtn = document.querySelector("[data-action='clear-filters']");

    // Model: { facet: Set<value> }
    const state = {};

    // Seed from URL.
    const params = new URLSearchParams(location.search);
    ["nc", "blind", "core", "band", "diff", "pattern", "overdue", "fresh", "never"].forEach((facet) => {
      const raw = params.get(facet);
      if (raw) state[facet] = new Set(raw.split(",").filter(Boolean));
    });
    // Back-compat: old `diff=` URLs get routed to `band=`.
    if (state.diff && !state.band) {
      state.band = state.diff;
      delete state.diff;
    }

    // Reflect model on chips.
    chips.forEach((chip) => {
      const f = chip.dataset.facet;
      const v = chip.dataset.value;
      if (state[f] && state[f].has(v)) chip.classList.add("active");
    });

    function updateUrl() {
      const p = new URLSearchParams();
      Object.entries(state).forEach(([facet, values]) => {
        if (values && values.size) {
          p.set(facet, Array.from(values).join(","));
        }
      });
      const qs = p.toString();
      const url = qs ? location.pathname + "?" + qs : location.pathname;
      history.replaceState(null, "", url);
    }

    function itemMatches(item) {
      for (const [facet, values] of Object.entries(state)) {
        if (!values || !values.size) continue;
        const attr = item.dataset[facet];
        // Boolean facets (nc, blind, core, overdue, fresh, never): attr === "1".
        // Multi-value facets (diff, pattern): attr is the current value.
        if (!attr || !values.has(attr)) return false;
      }
      return true;
    }

    function applyFilters() {
      let visible = 0;
      items.forEach((item) => {
        const match = itemMatches(item);
        item.style.display = match ? "" : "none";
        if (match) visible++;
      });
      if (countEl) countEl.textContent = String(visible);
    }

    chips.forEach((chip) => {
      chip.addEventListener("click", () => {
        const f = chip.dataset.facet;
        const v = chip.dataset.value;
        if (!state[f]) state[f] = new Set();
        if (state[f].has(v)) {
          state[f].delete(v);
          chip.classList.remove("active");
        } else {
          state[f].add(v);
          chip.classList.add("active");
        }
        updateUrl();
        applyFilters();
      });
    });

    if (clearBtn) {
      clearBtn.addEventListener("click", () => {
        Object.keys(state).forEach((k) => delete state[k]);
        chips.forEach((chip) => chip.classList.remove("active"));
        updateUrl();
        applyFilters();
      });
    }

    applyFilters();
  }

  // Expose a small debug surface (visible in DevTools console).
  window.DSATracker = {
    applySm2,
    getMergedState,
    downloadStateJson,
    clearPending,
    pendingCount,
  };
})();

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

    const merged = getMergedState();
    await writeStateFile(handle, merged);
    clearPending();
    return true;
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
  // Grading (problem pages).
  // --------------------------------------------------------------------------
  async function grade(task, gradeName) {
    const merged = getMergedState();
    const problem = merged.problems[task];
    if (!problem) {
      toast(`Unknown problem: ${task}`, "error");
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

    // Try FS Access API first — one-click persistence.
    try {
      await persistAllToStateFile();
      toast(`Saved · next review ${nextDueStr}`, "success");
      renderPendingBanner();
      return;
    } catch (err) {
      // Fell through: keep the change in localStorage, tell the user how to export.
      toast(
        `Recorded locally · next review ${nextDueStr}. ` +
        `Use "Save all" on the dashboard to write state.json.`,
        "info",
        4500
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
    initProblemPage();
    initDashboard();
    initThemeToggle();
    initQaCard();
  });

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
    const dawn = document.getElementById("prism-dawn");
    const moon = document.getElementById("prism-moon");
    if (!dawn || !moon) return;
    if (theme === "dark") {
      dawn.media = "not all";
      moon.media = "all";
    } else if (theme === "light") {
      dawn.media = "all";
      moon.media = "not all";
    } else {
      dawn.media = "(prefers-color-scheme: light)";
      moon.media = "(prefers-color-scheme: dark)";
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

  // Expose a small debug surface (visible in DevTools console).
  window.DSATracker = {
    applySm2,
    getMergedState,
    downloadStateJson,
    clearPending,
    pendingCount,
  };
})();

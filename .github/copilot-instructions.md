# GitHub Copilot Instructions

These instructions apply to **all** Copilot interactions in this workspace
(chat, inline completions, edits, agent mode). The full long-form guidance
lives in [`WARP.md`](../WARP.md) at the repo root — treat that file as the
authoritative spec and follow it. The points below are the non-negotiables
Copilot must respect when generating or editing code.

## Repository context

- **Project**: `tesseract-core` — a Java repository of DSA / interview
  problem solutions, organized by topic.
- **Build**: Gradle (`build.gradle.kts`). Sources live under
  `src/main/java/<topic>/` (e.g. `arrays/`, `trees/`, `graphs/`,
  `dynamicprogramming/`, `bitwiseoperation/`, ...).
- **Entry point**: `src/main/java/Main.java`.

## Package & file conventions

- Every new `.java` file MUST start with a `package` declaration that
  matches its directory (lowercase, e.g. `package arrays;`,
  `package dynamicprogramming;`). When a directory already contains files,
  reuse the exact package name those files use — do not invent a new one.
- One public class per file; filename matches the public class name.
- Place new problems in the most appropriate existing topic directory
  under `src/main/java/`. Create a new topic directory only if none fits.

## Required file documentation

Every new algorithm/problem file MUST start with a class-level Javadoc in
this exact shape:

```java
/**
 * ✅ Problem: [Problem Name]
 *
 * [Problem description]
 *
 * 🔗 Leetcode: [URL if applicable]
 *
 * 🧠 Example:
 * Input:  [example input]
 * Output: [example output]
 *
 * 🔍 Follow-up Questions:
 * 1. [Follow-up]
 * 2. [Variation]
 */
```

Every non-trivial method MUST have a Javadoc that includes:

```java
/**
 * [What the method does / algorithm steps]
 *
 * Time Complexity:  O(...)
 * Space Complexity: O(...)
 *
 * @param ...
 * @return ...
 */
```

Every problem file SHOULD include a `public static void main(String[] args)`
demonstrating usage with at least one example.

## Naming conventions

- Iterative solution:        `methodName()`
- Recursive solution:        `methodNameRecursive()`
- Helper method:             `methodHelper()` / `helperName()`
- Alternative approaches:    `methodNameOptimized()`, `methodNameDP()`,
                             `methodNameBFS()`, etc.

## Critical coding rules (do NOT violate)

1. **Comparators — never subtract.** Integer overflow risk.
   ```java
   // ❌ WRONG
   Arrays.sort(arr, (a, b) -> a[0] - b[0]);
   // ✅ CORRECT
   Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
   ```

2. **`toArray()` — always typed.**
   ```java
   // ❌ WRONG: returns Object[]
   Object[] a = list.toArray();
   // ✅ CORRECT
   int[][]   a = list.toArray(new int[0][]);
   String[]  s = list.toArray(new String[0]);
   ```

3. **List from array — pick the right one.**
   ```java
   // Mutable (add/remove allowed)
   List<String> mutable = new ArrayList<>(Arrays.asList("a", "b"));
   // Fixed-size view (read-only structurally)
   List<String> fixed   = Arrays.asList("a", "b");
   ```

4. **Prefer modern Java idioms** (the project targets Java 8+):
   - `map.computeIfAbsent(k, x -> new ArrayList<>()).add(v);`
   - `map.merge(k, 1, Integer::sum);`
   - Streams + `Collectors.toList()` / `groupingBy` / `joining` where it
     improves clarity.
   - `Objects.equals`, `Map.Entry`, `Deque` instead of `Stack`, etc.

5. **Bitwise idioms** (see WARP.md for the full table):
   - even/odd: `(n & 1) == 0`
   - power of two: `n > 0 && (n & (n - 1)) == 0`
   - get/set/clear/toggle ith bit: `(n >> i) & 1`, `n |= (1 << i)`,
     `n &= ~(1 << i)`, `n ^= (1 << i)`.

## When generating new solutions

- Provide complexity analysis in the method Javadoc.
- Prefer the canonical pattern for the topic (two-pointers for sorted
  arrays, sliding window for substrings, DFS/BFS for trees/graphs,
  memoization/tabulation for DP, monotonic stack for next-greater-element
  style problems, etc.).
- If multiple approaches exist, implement the cleanest first; add an
  `...Optimized()` / `...DP()` variant only when it adds value.
- Keep solutions self-contained — no external dependencies beyond the JDK
  unless the file already uses one.

## When editing existing files

- Preserve the existing package name and Javadoc style, even if it differs
  slightly from the templates above (be consistent with the file).
- Do not reformat unrelated code.
- Do not remove existing `main` methods or example usages.

---

If anything here conflicts with `WARP.md`, follow `WARP.md`.


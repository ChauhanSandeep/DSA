# GitHub Copilot Instructions

> Authoritative long‑form spec: [`WARP.md`](../WARP.md). If anything below
> conflicts with `WARP.md`, follow `WARP.md`.
>
> **Audience of the generated code:** *future me, revising this problem the
> night before an interview, and a real interviewer reading the screen.*
> Optimize for: (1) fast recall of **what the problem is**, (2) the
> **intuition + steps** of the solution living next to the code that
> implements them, (3) clean, idiomatic Java that would pass a code
> review at a top company.

---

## 1. Repository context

- **Project:** `tesseract-core` — Java DSA / interview problem solutions,
  organized by topic.
- **Build:** Gradle (`build.gradle.kts`). Sources under
  `src/main/java/<topic>/` (e.g. `arrays/`, `trees/`, `graphs/`,
  `dynamicprogramming/`, `bitwiseoperation/`, ...). Subtopics may nest
  (e.g. `arrays/slidingwindow/`).
- **Entry point:** `src/main/java/Main.java`.
- **Java level:** 8+ (use modern idioms; do not require >8 features).

---

## 2. Package & file conventions

- Every `.java` file starts with a `package` declaration matching its
  directory exactly (lowercase, e.g. `package arrays.slidingwindow;`).
  **Reuse the package the directory already uses** — do not invent.
- One public class per file; filename = public class name.
- Pick the most specific existing topic directory; create a new one only
  if no fit exists.
- Imports: explicit (no wildcard `import java.util.*;`). Group JDK
  imports together; no unused imports.

---

## 3. Where docs live (important — read this once)

We deliberately split documentation between the class and the method,
and we keep each side lean:

- **Class Javadoc** = *what the problem is.* Problem statement, link,
  one example, edge cases to remember, follow-ups. **No intuition. No
  complexity. No algorithm steps.** Those belong with the code that
  implements them.
- **Method Javadoc** = *how this specific solution solves it.* Intuition,
  numbered algorithm steps, complexity, `@param` / `@return`.
- **No dry-run traces in comments.** If a step is non-obvious, a short
  inline comment on the line that does the trick is enough.
- **No "why it works" section.** If the intuition + numbered steps are
  written clearly, correctness should be self-evident; do not duplicate.

---

## 4. The "gold standard" file skeleton

Use this exact shape for every new problem file.

```java
package arrays;

import java.util.Arrays;

/**
 * ✅ Problem: Next Permutation
 *
 * Rearrange `nums` into the next lexicographically greater permutation.
 * If no greater permutation exists, return the lowest (ascending) order.
 * Must be done in-place with O(1) extra memory.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/next-permutation/   (Medium)
 * 🏷️ Pattern:  Array · Two pointers · Greedy from the right
 *
 * 🧪 Example:
 *   Input:  [1,3,5,4,2]
 *   Output: [1,4,2,3,5]
 *
 * 🚧 Edge cases to remember:
 *   - length 0 / 1        → no change
 *   - already descending  → reverse entire array
 *   - duplicates          → strict `<` / `>` keeps it correct
 *
 * 🔍 Follow-ups:
 *   1. Previous permutation? Mirror the algorithm (find first rise from right).
 *   2. k-th next permutation? Factorial number system, O(n²) or O(n log n).
 *   3. Generate ALL permutations? Backtracking — see `backtrack/Permutations`.
 *
 * 🔁 Related: Permutations (46), Permutations II (47), Permutation Sequence (60).
 */
public class NextPermutation {

    /**
     * 🧠 Intuition: make the SMALLEST possible increase at the RIGHTMOST
     * position. Scan from the right to find the first dip
     * `nums[i] < nums[i+1]` — that `i` is the only index that needs to
     * grow. Swap it with the smallest value to its right that is still
     * larger, then reverse the (descending) suffix to make the tail as
     * small as possible.
     *
     * Algorithm:
     *   1. Find pivot `i` — largest index with nums[i] < nums[i+1].
     *   2. If no pivot, array is the last permutation → reverse all.
     *   3. Else find rightmost j with nums[j] > nums[i], swap i and j.
     *   4. Reverse the suffix after i.
     *
     * Time:  O(n)
     * Space: O(1)
     *
     * @param nums array mutated in place to its next permutation
     */
    public void nextPermutation(int[] nums) {
        if (nums == null || nums.length < 2) return;

        // --- Step 1: find pivot ---------------------------------------
        int pivot = nums.length - 2;
        while (pivot >= 0 && nums[pivot] >= nums[pivot + 1]) {
            pivot--;
        }

        // --- Step 2 & 3: swap pivot with its tight upgrade ------------
        if (pivot >= 0) {
            int swapWith = nums.length - 1;
            while (nums[swapWith] <= nums[pivot]) {
                swapWith--;
            }
            swap(nums, pivot, swapWith);
        }

        // --- Step 4: minimize the suffix ------------------------------
        reverseFrom(nums, pivot + 1);
    }

    /** Reverses nums[start..end] in place. */
    private void reverseFrom(int[] nums, int start) {
        int left = start, right = nums.length - 1;
        while (left < right) {
            swap(nums, left++, right--);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        NextPermutation solver = new NextPermutation();

        int[][] cases    = { {1,2,3}, {3,2,1}, {1,1,5}, {1,3,5,4,2} };
        int[][] expected = { {1,3,2}, {1,2,3}, {1,5,1}, {1,4,2,3,5} };

        for (int i = 0; i < cases.length; i++) {
            int[] input = cases[i].clone();
            solver.nextPermutation(input);
            System.out.printf("in=%s  →  out=%s  expected=%s%n",
                Arrays.toString(cases[i]),
                Arrays.toString(input),
                Arrays.toString(expected[i]));
        }
    }
}
```

---

## 5. Class-level Javadoc — required sections

In this order, with these emoji headers:

| Section          | Required | Purpose                                          |
| ---------------- | -------- | ------------------------------------------------ |
| `✅ Problem`     | yes      | One-line title + 2–4 line plain-English summary. |
| `🔗 Leetcode`    | if any   | Full URL + difficulty in parentheses.            |
| `🏷️ Pattern`     | yes      | Topic tags + the algorithmic pattern used.       |
| `🧪 Example`     | yes      | **One** small input/output. (Pick a tricky one.) |
| `🚧 Edge cases`  | yes      | Bullet list of what the code explicitly handles. |
| `🔍 Follow-ups`  | yes      | 2–4 interview-style variations with one-line answers. |
| `🔁 Related`     | optional | Sibling problems by number/name.                 |

Explicitly **NOT** in the class Javadoc:

- ❌ Intuition (lives on the method)
- ❌ Algorithm steps (lives on the method)
- ❌ Time/Space complexity (lives on the method)
- ❌ Multiple examples or dry-run traces

---

## 6. Method-level Javadoc — required sections

Every non-trivial method (anything beyond a 3-line helper):

1. **Intuition** — 2–5 lines; the mental model in plain English.
2. **Algorithm** — numbered steps, matching the `// --- Step N` banners
   in the code. Keep each step to one line.
3. **Time** / **Space** — one line each.
4. `@param` / `@return` lines.

Explicitly **NOT** in method Javadoc:

- ❌ "Why it works" section — the intuition + steps should already
  make correctness obvious. Do not paraphrase.
- ❌ Dry-run traces — if a single line is tricky, add a brief inline
  comment on that line instead.

Trivial helpers (`swap`, `reverseFrom`, `gcd`, etc.) get only a
one-line summary, no `@param` boilerplate.

---

## 7. Naming policy (enforced)

Variables and methods read like prose. **Optimize for the reader, not
the typist.**

**Loop indices**
- `i`, `j`, `k` are fine for *pure* indices in tight loops.
- The moment an index has meaning, name it: `left`, `right`, `slow`,
  `fast`, `pivot`, `windowStart`, `windowEnd`, `row`, `col`.

**Counts / accumulators**
- `count`, `total`, `sum`, `maxLen`, `bestProfit`, `runLength`.
- ❌ `cnt`, `tot`, `ml`, `res`, `ans`, `tmp2`.

**Booleans** start with `is` / `has` / `can` / `should`:
`isPalindrome`, `hasCycle`, `canPartition`.

**Collections** are plural and typed:
`List<Integer> sortedNums`, `Map<Character, Integer> charToFreq`,
`Set<Integer> seen`, `Deque<int[]> bfsQueue`.
- ❌ `list1`, `map1`, `arr2`.

**Domain over generic**
- ✅ `nodesAtCurrentLevel`, `remainingCapacity`, `previousChar`.
- ❌ `x`, `val`, `data`, `temp` (unless truly a swap temporary).

**Methods**
- Verb-first: `findLongestSubstring`, `buildAdjacencyList`,
  `canReachEnd`.
- Iterative vs recursive vs alternative — use the project's suffix
  convention:
  - iterative:    `methodName()`
  - recursive:    `methodNameRecursive()`
  - helper:       `methodHelper()` / `dfs()` / `backtrack()`
  - alternative:  `methodNameOptimized()`, `methodNameDP()`,
                  `methodNameBFS()`, `methodNameTwoPointer()`.

**Constants**
- Replace magic numbers: `private static final int ALPHABET_SIZE = 26;`,
  `private static final int MOD = 1_000_000_007;`,
  `private static final int[][] DIRS = {{-1,0},{1,0},{0,-1},{0,1}};`.

---

## 8. Code structure rules

- **Guard clauses first.** Reject null / empty / trivial inputs in the
  first 1–3 lines before the main logic.
- **Step banners.** For multi-phase algorithms, divide the method body
  with comment banners that match the Javadoc steps:
  ```java
  // --- Step 1: build frequency map ---------------------------------
  // --- Step 2: slide the window ------------------------------------
  ```
- **Max nesting depth ≈ 3.** If you go deeper, refactor the condition
  (continue / early return) before reaching for a helper.
- **Helper extraction — interview guardrail.**
  In an interview you should not be inventing 5 helper methods. Extract
  a private helper **only if** one of these holds:
  1. The logic is reused **≥ 2 times** in the file (e.g. `swap`,
     `isValid`, `neighbors`).
  2. The logic has an obvious, well-known name that *replaces*
     reading the code (`isPalindrome`, `gcd`, `dfs`, `binarySearch`).
  3. Extracting it removes a nesting level that was pushing past depth 3.

  Otherwise keep the logic inline. **Target: ≤ 2 private helpers per
  problem file** (plus `swap`/`reverseFrom`-style one-liners which don't
  count).
- **Method length.** Aim for ≤ 40 lines for the main solution method.
  If it grows, prefer step banners + named local variables over more
  helpers.
- **Inline comments** explain *why*, not *what*. The code already says
  what.
  ```java
  // ✅ only count from sequence starts so each number is visited O(1) times
  if (!seen.contains(num - 1)) { ... }
  ```
- **No dead code, no commented-out blocks, no `System.out` debug
  prints** outside `main`.
- **Whitespace**: blank line between logical phases; no blank line
  between a comment banner and the code it describes.

---

## 9. `main()` demo conventions

Every problem file SHOULD include a `public static void main(String[] args)`:

- Cover **≥ 2 inputs**, including at least one edge case (empty, single
  element, all duplicates, max negative, etc.).
- Print in `input → output  expected=...` form so a glance during
  revision confirms correctness.
- Use `Arrays.toString` / `Arrays.deepToString` for arrays.
- Do **not** use a testing framework — keep it pure `main`.
- Keep `main` short; if the demo grows, extract a `runDemo(...)` helper.

```java
public static void main(String[] args) {
    TwoSum solver = new TwoSum();
    int[][] inputs   = { {2,7,11,15}, {3,2,4}, {3,3} };
    int[]   targets  = {            9,       6,     6 };
    int[][] expected = { {0,1},      {1,2},   {0,1} };
    for (int i = 0; i < inputs.length; i++) {
        int[] got = solver.twoSum(inputs[i], targets[i]);
        System.out.printf("nums=%s target=%d  →  %s  expected=%s%n",
            Arrays.toString(inputs[i]), targets[i],
            Arrays.toString(got), Arrays.toString(expected[i]));
    }
}
```

---

## 10. Multiple approaches — keep it interview-realistic

Default: **one solution per problem** — the one you would actually write
in an interview.

Add a **second** method only when one of these is true:

1. There are two *genuinely different* idiomatic approaches that an
   interviewer might ask you to compare (e.g. iterative DP vs
   memoized recursion, BFS vs DFS, hash map vs two-pointer on sorted
   input).
2. Coming up with the optimal solution from scratch is genuinely hard,
   so a **brute force** is worth keeping as a stepping stone to explain
   the optimization in an interview.

**Hard cap: 2 solutions per file.** No "brute → better → optimal"
triple. If you keep a brute force, it must earn its place by being the
natural starting point you'd verbalize before optimizing.

When two solutions coexist:

- The **recommended / optimal** method gets the canonical name
  (e.g. `longestConsecutive`) and appears **last** in the file (most
  memorable).
- The alternative gets a suffix (`...Brute`, `...Recursive`,
  `...Sorting`, `...DP`).
- Each method has its own full Intuition + Algorithm + Complexity
  Javadoc.
- Add a 2-row table at the top of the class Javadoc only if it adds
  clarity:
  ```
   Approach          Method                     Time         Space
   ----------------  -------------------------  -----------  ------
   Sorting           longestConsecutiveSorting  O(n log n)   O(1)
   Hash set (best)   longestConsecutive         O(n)         O(n)
  ```

---

## 11. Critical coding rules (zero tolerance)

1. **Comparators — never subtract** (overflow):
   ```java
   // ❌
   Arrays.sort(arr, (a, b) -> a[0] - b[0]);
   // ✅
   Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
   ```

2. **`toArray()` always typed:**
   ```java
   int[][]  a = list.toArray(new int[0][]);
   String[] s = list.toArray(new String[0]);
   ```

3. **List from array — be explicit:**
   ```java
   List<String> mutable = new ArrayList<>(Arrays.asList("a", "b"));
   List<String> fixed   = Arrays.asList("a", "b");   // structurally read-only
   List<String> unmod   = List.of("a", "b");          // truly immutable (Java 9+)
   ```

4. **Modern collection idioms:**
   - `map.computeIfAbsent(k, x -> new ArrayList<>()).add(v);`
   - `map.merge(k, 1, Integer::sum);`
   - `Deque<Integer>` (`ArrayDeque`) instead of `Stack`.
   - `PriorityQueue` with `Comparator.comparingInt(...)`.
   - `Objects.equals(a, b)` for null-safe equality.

5. **Bitwise idioms:**
   - even / odd:        `(n & 1) == 0`
   - power of two:      `n > 0 && (n & (n - 1)) == 0`
   - i-th bit get/set/clear/toggle:
     `(n >> i) & 1`, `n |= (1 << i)`, `n &= ~(1 << i)`, `n ^= (1 << i)`
   - lowest set bit:    `n & -n`

6. **Mid-point without overflow:** `int mid = left + (right - left) / 2;`

7. **Avoid `Integer` boxing in hot loops.** Prefer `int[]` over
   `List<Integer>` when size is known.

8. **Recursion depth.** Note risk of stack overflow when n is large;
   add an iterative variant only if recursion depth can exceed ~10⁴
   *and* the problem warrants it (counts toward the 2-solutions cap).

---

## 12. Anti-patterns checklist (self-verify before finishing)

The file must **NOT** contain any of:

- [ ] Single-letter variables outside `i,j,k` loop indices or `swap` temps.
- [ ] `tmp`, `temp`, `res`, `ans`, `arr2`, `map1`, `list1` as names.
- [ ] Methods longer than ~40 lines without step banners.
- [ ] Nesting deeper than 3 levels.
- [ ] More than 2 private helpers (excluding one-line `swap`-style utils).
- [ ] Magic numbers (`26`, `1_000_000_007`, direction arrays) inline.
- [ ] Wildcard imports.
- [ ] `Stack`, `Vector`, `Hashtable` (use `Deque`, `ArrayList`, `HashMap`).
- [ ] `a - b` comparator.
- [ ] Untyped `toArray()`.
- [ ] **Class** Javadoc containing Intuition, Algorithm steps, or Complexity.
- [ ] **Method** Javadoc containing a "Why it works" section or a dry-run trace.
- [ ] Non-trivial method without **Time** / **Space** in its Javadoc.
- [ ] A `main` that prints only the output with no input/expected label.
- [ ] More than 2 solution methods for the same problem.
- [ ] Commented-out code or stray `System.out.println("here")`.

---

## 13. When editing existing files

- **Match the file's existing style** (package name, Javadoc dialect,
  brace style, indentation), even if it diverges from this guide.
- **Do not reformat** unrelated code; keep diffs minimal and reviewable.
- **Do not delete** existing `main` methods or alternative approaches
  unless the user asks.
- If the file lacks Intuition (on a method) or Edge cases (on the
  class) and the user is asking for an improvement, *add* them rather
  than rewriting working code.
- If a class Javadoc currently has Intuition / Complexity / Dry-run,
  move that content into the method Javadoc (or drop the dry-run)
  only when the user is explicitly cleaning the file up.

---

If anything here conflicts with `WARP.md`, follow `WARP.md`.


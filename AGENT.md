# AGENT.md

This file is the authoritative guidance for **any AI agent or human contributor**
working in this repository (WARP, GitHub Copilot, Claude, Cursor, etc.). It is the
single source of truth for how problem files must be documented and structured.

If any other instruction file (e.g. `.github/copilot-instructions.md`) conflicts
with this document, **this document wins**.

The goal of every convention below is one thing: **a reader who has not touched
DSA in months should understand the problem and why the solution works in under a
minute, without re-solving it themselves.** Optimize for fast recall, not for the
person typing the code.

The canonical reference implementation of this standard is
[`src/main/java/backtrack/Subset2.java`](src/main/java/backtrack/Subset2.java) —
when in doubt, copy its shape exactly.

## Repository Overview

**tesseract-core** is a Java-based algorithmic problem-solving repository focused on technical interview preparation. It contains implementations of common algorithms and data structures organized by topic, with comprehensive documentation and multiple solution approaches.

## Common Development Commands

### Basic Compilation and Execution

```bash
# Compile all Java files in the repository
javac $(find . -name "*.java")

# Run the main entry point
java Main

# Compile and run a specific algorithm class
javac Array/BinarySearch.java && java Array.BinarySearch

# Compile and run with package structure
javac tree/Node.java Tree/TreeNode.java && java Tree.TreeNode
```

### Running Individual Algorithm Examples

Most algorithm files contain `main` methods that demonstrate usage:

```bash
# Example: Run binary search demonstration
javac Array/BinarySearch.java && java array.BinarySearch

# Example: Run tree algorithm
javac Tree/MaximumDepthOfBinaryTree.java && java Tree.MaximumDepthOfBinaryTree

# Example: Run dynamic programming solution
javac DynamicProgramming/ClimbingStairs.java && java DynamicProgramming.ClimbingStairs
```

### Package Management Script

The repository includes utility scripts for managing package declarations:

```bash
# Update package declarations in frazsheet directory (legacy)
./update_packages.sh

# Reorganization script (already executed)
./reorganize_frazsheet.sh
```

## Architecture & Code Organization

### Directory Structure

The codebase is organized by algorithmic topics:

- **`Array/`** - Array manipulation, searching, sorting problems
- **`Bitwise/`** - Bit manipulation operations and techniques  
- **`Tree/`** & **`tree/`** - Binary tree implementations and algorithms (note: dual package structure)
- **`Graph/`** - Graph algorithms (BFS, DFS, shortest path, etc.)
- **`DynamicProgramming/`** - DP solutions for optimization problems
- **`String/`** - String processing and pattern matching
- **`LinkedList/`** - Linked list operations and algorithms
- **`StackQueue/`** - Stack and queue implementations
- **`Heap/`** - Priority queue and heap-based algorithms
- **`Backtracking/`** - Recursive backtracking solutions
- **`BinarySearch/`** - Binary search variations
- **`greedy/`** - Greedy algorithm implementations
- **`Hashing/`** - Hash table and hashing techniques
- **`Maths/`** - Mathematical algorithms and number theory
- **`Recursion/`** - Recursive problem solutions
- **`Multithreading/`** - Concurrency and threading examples
- **`DesignPatterns/`** - Design pattern implementations
- **`Design/`** - System design and data structure implementations
- **`Test/`** - Utility and testing files
- **`utils/`** - Helper utilities

### Key Files

- **`Main.java`** - Repository entry point with welcome message
- **`README.md`** - Java 8 syntax cheatsheet for interviews
- **`Errors.md`** - Common coding mistakes and fixes
- **`Bitwise/README.md`** - Comprehensive bitwise operations guide
- **`update_packages.sh`** - Script to fix package declarations

## Documentation Standard (authoritative)

> No emojis anywhere in the code or Javadoc. Plain ASCII only.

> **Write like you are explaining to a colleague, not quoting a textbook.**
> Plain, warm, concrete English. Do NOT rewrite prose into terse or mathematical
> form just to look precise. **If the existing wording is already clear and good,
> keep it** — this standard is about filling gaps and fixing what is wrong, not
> reformatting working prose. When in doubt, prefer the reader's understanding
> over the author's cleverness.

We deliberately split documentation between the **class** and the **method**:

- **Class Javadoc = what the problem is.** Problem statement, link, rating,
  pattern, one tricky example, hard follow-ups, related problems. No intuition,
  no algorithm steps, no complexity — those belong with the code.
- **Method Javadoc = how this specific solution works.** Intuition, numbered
  algorithm steps, complexity, `@param` / `@return`.
- **No dry-run traces in comments.** If one line is non-obvious, add a short
  inline comment on that line instead.

### Class Javadoc — required sections (in this order)

| Section       | Required | Purpose                                                        |
| ------------- | -------- | ------------------------------------------------------------- |
| `Problem`     | yes      | One-line title + 2-4 line plain-English summary.              |
| `Leetcode`    | if any   | Full URL.                                                     |
| `Rating`      | if any   | Granular difficulty — see rating convention below.           |
| `Pattern`     | yes      | Topic tags + the algorithmic pattern, `A \| B \| C`.          |
| `Example`     | yes      | **One** small, tricky input/output, PLUS a one-line reason why that output is correct (a `Why:` line). Edge cases live in `main`, not here. |
| `Follow-ups`  | yes      | 2-4 genuinely hard interview variations, each with a one-line answer. |
| `Related`     | optional | Sibling problems by number/name.                             |
| approach table| optional | Only when there are 2 solutions; compare Method/Time/Space.   |

Explicitly **NOT** in the class Javadoc: intuition, algorithm steps, complexity,
an "edge cases to remember" list (those inputs go in `main`), or multiple
examples / dry-run traces.

### Rating convention (granular over broad)

Broad Easy/Medium/Hard labels are too coarse. Produce a numeric signal, in
priority order, and hardcode the fetched value into the file so the doc is static:

1. **zerotrac Elo** — for problems that appeared in a weekly/biweekly contest.
   Source: `zerotrac/leetcode_problem_rating/ratings.txt`. Example:
   `Rating:   zerotrac 1685 (Q3, weekly-263)`.
2. **LeetCode acceptance rate** — universal fallback for pre-contest problems
   with no Elo (via LeetCode GraphQL `stats.acRate`). Lower % ~ harder within a
   band. Example: `Rating:   acceptance 61.6% (Medium) - no contest Elo (pre-contest problem)`.
3. Keep the broad label in parentheses only as a human anchor.

### Method Javadoc — required sections

Every non-trivial method (anything beyond a 3-line helper):

1. **Intuition** — build the mental model from scratch for a reader who has
   forgotten this problem (or is seeing it for the first time). Explain WHY this
   approach is chosen, the key insight / "aha" that makes it work, and briefly
   why it is correct — in plain, concrete language. Aim for roughly 4-8 lines:
   rich enough to actually re-teach the idea, but tight, no rambling. If two
   solutions coexist, name which is the interview default here.
2. **Algorithm** — numbered steps that match the `// --- Step N` banners in code.
   Each step is a full, readable sentence a rusty reader can follow without
   re-deriving it — not a cryptic fragment. When a step is non-obvious, fold in
   the small "why" for that step. Do NOT write a step as an arrow template like
   `Select -> recurse -> un-select`; describe it in plain words ("add the
   candidate, recurse on the rest, then remove it before trying the next"). The
   arrow label belongs on the code line as an inline anchor, not in prose steps.
3. **Time** / **Space** — one line each. The **Time** line MUST include a short,
   plain-English reason for *why* it is that complexity, written for someone who
   finds Big-O hard. Not a proof — one clause. E.g.
   `Time: O(n log n) - we sort once (n log n), then a single linear scan.`
   or `Time: O(2^n * n) - there are 2^n subsets and copying each costs up to O(n).`
4. `@param` / `@return`.

**Private / helper methods** get a **concise one-line Javadoc stating only their
purpose** — what the method is for, in a single sentence. No `@param`, no
`@return`, no algorithm steps, no complexity. Truly trivial one-liners (`swap`,
`gcd`) may keep just that one-line summary.

### Inline comments

- Comment only the **critical decision points** and non-obvious tricks — not the
  obvious lines. The code already says *what*; comments say *why*.
- Use a well-known template label ONLY when the algorithm genuinely follows that
  pattern — e.g. the backtracking `select -> work -> un-select`, or
  `select -> mark(*) -> work -> unmark(*)` for graph traversals. These are recall
  anchors, not required decoration: if the code is a plain loop, a direct formula,
  or does not truly choose-then-undo, do NOT force a template label onto it. When
  in doubt, leave it out.

### Multiple solutions

- Default to **one** solution — the one you would write in an interview.
- Add a **second** only for a genuinely different idiomatic approach worth
  comparing, or a brute force worth keeping as a stepping stone. **Hard cap: 2.**
- The recommended/optimal method keeps the canonical name and appears **last**
  (most memorable); the alternative takes a suffix (`...Iterative`, `...Brute`,
  `...DP`). Each method carries its own full Javadoc.

### `main()` demo conventions

Every problem file SHOULD include a `public static void main`:

- Cover **>= 2 inputs**, including at least one edge case (empty, single element,
  all duplicates, etc.) — this is where edge cases live now.
- Print in `input -> output  expected=...` form so a glance confirms correctness.
- Use `Arrays.toString` / `Arrays.deepToString` for arrays. No test framework.

### Naming Conventions

Names read like prose; optimize for the reader, not the typist. `i, j, k` are
fine for pure loop indices, but the moment an index has meaning, name it
(`left`, `right`, `slow`, `fast`, `pivot`, `windowStart`). No `tmp`, `res`,
`ans`, `arr2`, `map1`. Booleans start with `is`/`has`/`can`. Collections are
plural and typed. `result` (or `results`) is perfectly fine for the value a
method accumulates and returns — do not rename it to something more elaborate.

- **Iterative solutions**: `methodName()`
- **Recursive solutions**: `methodNameRecursive()`
- **Helper methods**: `methodHelper()`, `dfs()`, `backtrack()`
- **Alternative approaches**: `methodNameOptimized()`, `methodNameDP()`, `methodNameIterative()`

### Java 8 Best Practices

The codebase leverages modern Java features extensively:

```java
// Safe collection operations
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
map.merge(key, 1, Integer::sum);

// Stream operations
list.stream()
    .filter(x -> x > 0)
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());

// Safe comparators (avoid overflow)
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
```

## Critical Coding Guidelines

### Comparator Safety
**❌ Never use subtraction in comparators:**
```java
// WRONG - can cause integer overflow
Arrays.sort(arr, (a, b) -> a[0] - b[0]);

// CORRECT - use built-in comparison
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
```

### Array Conversion Safety
**❌ Avoid generic `toArray()`:**
```java
// WRONG - returns Object[]
Object[] arr = list.toArray();

// CORRECT - specify type
int[][] arr = list.toArray(new int[0][]);
String[] arr = list.toArray(new String[0]);
```

### List Creation from Arrays
```java
// For mutable lists
List<String> mutableList = new ArrayList<>(Arrays.asList("a", "b", "c"));

// For fixed-size lists (read-only)
List<String> fixedList = Arrays.asList("a", "b", "c");
```

## Package Structure Guide

| Directory | Package Name | Sample Classes | Focus Area |
|-----------|--------------|----------------|-------------|
| `Array/` | `array` | `BinarySearch`, `TwoSum`, `ThreeSum` | Array algorithms |
| `Tree/` | `Tree` | `TreeNode`, `BinaryTreePaths`, `SerializeAndDeserializeBinaryTree` | Tree structures |
| `tree/` | `tree` | `Node` | Tree utilities (legacy) |
| `Graph/` | `Graph` | `BFS`, `DFS`, `AllPathsFromSourceToTarget` | Graph algorithms |
| `String/` | `String` | `LongestSubstring`, `LongestRepeatingCharacterReplacement` | String processing |
| `DynamicProgramming/` | `DynamicProgramming` | `ClimbingStairs`, `BurstBalloons` | DP solutions |
| `Bitwise/` | `Bitwise` | `MaximumXor`, `SumOfTwoIntegers` | Bit manipulation |
| `Design/` | `Design` | `LRUCache`, `DesignHashmap` | System design |

**Note**: The repository has mixed package naming conventions (`Tree` vs `tree`). When adding new files, use the existing package name for the directory.

## Quick Reference

<details>
<summary>Java 8 Collections Cheatsheet</summary>

```java
// Map operations
map.putIfAbsent(key, new ArrayList<>());
map.computeIfAbsent(key, k -> new ArrayList<>());
map.computeIfPresent(key, (k, v) -> v + 1);
map.merge(key, 1, Integer::sum);

// Stream operations
list.stream().filter(x -> x > 0).collect(Collectors.toList());
list.stream().map(String::toUpperCase).collect(Collectors.toList());
list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

// Collectors
Collectors.toMap(Item::getKey, Item::getValue, Integer::sum);
Collectors.groupingBy(Foo::getType);
Collectors.joining(", ");
```
</details>

<details>
<summary>Bitwise Operations Quick Reference</summary>

| Operation | Code | Use Case |
|-----------|------|----------|
| Check even/odd | `(num & 1) == 0` | Parity check |
| Power of 2 | `(n > 0) && ((n & (n - 1)) == 0)` | Validation |
| Count set bits | `n & (n - 1)` in loop | Bit counting |
| Get ith bit | `(num >> i) & 1` | Bit extraction |
| Set ith bit | `num \|= (1 << i)` | Bit setting |
| Clear ith bit | `num &= ~(1 << i)` | Bit clearing |
| Toggle ith bit | `num ^= (1 << i)` | Bit toggling |

</details>

## Development Workflow

1. **Adding New Algorithms**: Create in appropriate topic directory with package declaration
2. **Testing**: Each file typically includes a `main` method for demonstration
3. **Documentation**: Follow the established comment patterns with complexity analysis
4. **Package Consistency**: Use existing package names for each directory

## Common Algorithmic Patterns

- **Two Pointers**: Used extensively in `Array/` problems
- **Sliding Window**: Found in string and array problems  
- **DFS/BFS**: Core patterns in `Graph/` and `Tree/` directories
- **Dynamic Programming**: Memoization and tabulation in `DynamicProgramming/`
- **Monotonic Stack**: Referenced in `Errors.md` for next/previous element problems
- **Binary Search**: Variations in `BinarySearch/` directory with template patterns

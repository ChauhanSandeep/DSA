# Competitive Programming Complexity Reference

This is a practical, conservative cheat sheet for competitive programming and DSA problem solving.

## 1) Input Size -> Target Complexity

Use this table as your first filter before choosing an approach.


| Input size `n`    | Usually acceptable                 | Usually risky                           |
| ----------------- | ---------------------------------- | --------------------------------------- |
| `n <= 10`         | `O(n!)`, `O(2^n)`                  | -                                       |
| `n <= 20`         | `O(2^n * n)`                       | `O(3^n)` and above                      |
| `n <= 100`        | `O(n^3)`                           | `O(n^4)`                                |
| `n <= 1,000`      | `O(n^2)`                           | `O(n^3)`                                |
| `n <= 10,000`     | tuned `O(n^2)` only                | generic `O(n^2)`                        |
| `n <= 100,000`    | `O(n log n)`                       | `O(n^2)`                                |
| `n <= 1,000,000`  | `O(n)`                             | `O(n log n)` can be tight in some tasks |
| `n >= 10,000,000` | tight `O(n)` / `O(log n)` / `O(1)` | anything superlinear                    |


## 2) Most Common Time Complexities


| Complexity   | Meaning in practice                                                           | Typical use cases                      |
| ------------ | ----------------------------------------------------------------------------- | -------------------------------------- |
| `O(1)`       | Constant work per query                                                       | hash lookup, math formula              |
| `O(log n)`   | Repeatedly halving search space                                               | binary search, balanced BST ops        |
| `O(n)`       | One full pass                                                                 | scans, prefix/suffix processing        |
| `O(n log n)` | linear passes plus sorting/tree ops                                           | sorting, divide-and-conquer            |
| `O(n^2)`     | double loop over `n`                                                          | pair checks, 2D DP                     |
| `O(n^3)`     | triple loop                                                                   | Floyd-Warshall, transitions over pairs |
| `O(2^n)`     | When **you make ~2 choices at each of n levels** (include/exclude, take/skip) | subset DP, backtracking                |
| `O(n!)`      | try all permutations                                                          | permutation brute force                |


## 3) Common Algorithms and Data Structures


| Technique / Structure            | Time                             | Space                          |
| -------------------------------- | -------------------------------- | ------------------------------ |
| Binary Search                    | `O(log n)`                       | `O(1)`                         |
| Two Pointers                     | `O(n)`                           | `O(1)`                         |
| Sliding Window                   | `O(n)`                           | `O(1)` to `O(k)`               |
| Prefix Sum (build + query)       | `O(n)` + `O(1)`                  | `O(n)`                         |
| Difference Array (range updates) | `O(1)` update, `O(n)` finalize   | `O(n)`                         |
| Sorting                          | `O(n log n)`                     | typically `O(log n)` to `O(n)` |
| HashMap / HashSet lookup         | average `O(1)`                   | `O(n)`                         |
| BFS / DFS                        | `O(V + E)`                       | `O(V)`                         |
| Topological Sort                 | `O(V + E)`                       | `O(V)`                         |
| Union-Find (DSU)                 | near `O(1)` amortized            | `O(n)`                         |
| Dijkstra (heap)                  | `O((V + E) log V)`               | `O(V + E)`                     |
| Segment Tree                     | `O(log n)` query/update          | `O(n)`                         |
| Fenwick Tree (BIT)               | `O(log n)` query/update          | `O(n)`                         |
| Sparse Table (RMQ static)        | `O(1)` query, `O(n log n)` build | `O(n log n)`                   |
| KMP                              | `O(n + m)`                       | `O(m)`                         |
| Z Algorithm                      | `O(n)`                           | `O(n)`                         |


## 4) Operation Budget Sanity Check

- A common rough upper bound is around `10^8` simple operations per test file.
- Always multiply complexity by:
  - number of test cases `t`
  - hidden factors like string length `L`
  - expensive constants from heavy data structures.

Examples:

- `n = 1,000`, `O(n^2)` -> `10^6` (usually fine).
- `n = 100,000`, `O(n^2)` -> `10^10` (too slow).
- `t = 10`, `n = 200,000`, `O(n)` -> about `2 * 10^6` core ops before constants.

## 5) Strings and Hidden Multipliers

If string length is `L`, base complexity often becomes `O(base * L)`.


| Operation            | Common complexity |
| -------------------- | ----------------- |
| String comparison    | `O(L)`            |
| Hashing/building key | `O(L)`            |
| Concatenation        | often `O(L)`      |
| Substring            | often `O(L)`      |


Use builders/buffers when repeatedly appending inside loops.

## 6) Quick Decision Flow

1. Read constraints and count test cases.
2. Decide maximum acceptable complexity from `n`.
3. Match target to candidate techniques:
  - `O(n)` -> hashing, two pointers, sliding window, prefix sums
  - `O(n log n)` -> sorting + scan, binary search on answer, trees/heaps
  - `O(n^2)` -> only if `n` is small enough
4. Check memory limits before finalizing.
5. Validate edge cases and worst-case input pattern.

## 7) Red Flags (Your Solution Is Likely Too Slow)

- Nested loops over `n = 1e5`.
- Re-sorting inside a loop.
- Recomputing the same subproblem without memoization.
- String concatenation in each iteration over large loops.
- Running graph algorithms assuming sparse edges when `E` may be dense.

## 8) Fast Wins (Try These Early)

- Sort first, then apply two pointers.
- Convert repeated membership checks to hash set/map.
- Use prefix sums for many range-sum queries.
- Use binary search on answer when predicate is monotonic.
- Replace brute force recursion with memoization/tabulation.
- Use DSU for repeated connectivity/merge queries.

## 9) Complexity Traps

- `O(V^2)` assumptions on graphs when adjacency list gives `O(V + E)`.
- Confusing average-case and worst-case behavior in hash-based structures.
- Ignoring recursion depth/stack space in deep DFS.
- Forgetting `t * complexity` across multiple test cases.
- Underestimating constants in `O(n log n)` when combined with heavy object work.

## 10) Problem Start Template (Use Before Coding)

Fill this quickly in your head or on paper:

- Constraints: `n = ?`, `t = ?`, memory = `?`
- Target complexity: `O(?)`
- Candidate techniques: `[...]`
- Chosen approach and why: `...`
- Edge cases: empty/single element, duplicates, max values, disconnected/invalid states

This 30-second checklist prevents most TLE and overcomplicated solutions.
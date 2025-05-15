

## Matrix Chain Multiplication (MCM)

### Logic:

- **Given**: A sequence of matrices `A1, A2, ..., An`.
- **Goal**: Find the most efficient way (minimum number of scalar multiplications) to multiply them together.

### Key Intuition:
- **Matrix multiplication is associative** but **not commutative** — order matters for cost.
- The cost of multiplying two matrices `A[i..k]` and `A[k+1..j]` depends on their dimensions.
- **Idea**: Try every possible partition (`k`) between `i` and `j`, recursively solve for left and right, and combine them with current multiplication cost.

### Approach:
1. Define a DP function `dp(i, j)` = minimum cost to multiply matrices from index `i` to `j`.
2. For every `k` between `i` and `j-1`, calculate:

dp(i, j) = min over all k (dp(i, k) + dp(k+1, j) + cost of multiplying result of (i..k) and (k+1..j))

where cost is `dimensions[i-1] * dimensions[k] * dimensions[j]`.
3. Base case: If `i == j`, only one matrix → no cost (`dp(i, i) = 0`).

**Thus, MCM = Partition at every index ➔ Solve left & right recursively ➔ Add combination cost ➔ Pick the minimum.**

---

# Child Problems of Matrix Chain Multiplication

## 1. **Palindrome Partitioning (List of Lists Version)**
**Problem**: Given a string, partition it into all possible lists of substrings where each substring is a palindrome.

**MCM Usage**:
- Partition the string at every index.
- For each prefix, if it is a palindrome:
- Recur for the remaining suffix.
- Add the prefix to current partition and proceed.
- **Final goal**: Collect all valid partition combinations.

---

## 2. **Evaluate Boolean Expression to True**
**Problem**: Given a boolean expression with 'T', 'F', '&', '|', '^', count the number of ways to parenthesize it so that it evaluates to `True`.

**MCM Usage**:
- Partition the expression at each operator.
- For each partition:
- Recursively solve left and right for True and False counts.
- Combine counts based on operator's truth table (`AND`, `OR`, `XOR` rules).
- Aggregate the number of ways to get `True` overall.

---

## 3. **Minimum Cost to Cut a Stick**

- **Problem**: Given stick length and cut positions, find the minimum total cost to perform all cuts.

- **MCM Usage**:
- Treat each cut as a partition.
- For each possible first cut, solve left and right parts recursively.
- Add the cost of the current cut (length of current stick segment).
- Pick the cut position that minimizes total cost.

---

## 4. **Burst Balloons**
**Problem**: Given balloons with numbers, burst balloons to maximize coins collected.

**MCM Usage**:
- Think of the balloon you burst **last** in a range.
- For each balloon:
- Coins collected = `nums[left] * nums[current] * nums[right]`.
- Recur on left and right sides separately.
- Pick the last balloon choice that maximizes the coins.

---

## 5. **Scramble String**

**Problem**: Given two strings `s1` and `s2`, determine if `s2` is a scrambled form of `s1`.

**MCM Usage**:
- Partition both strings at every possible split.
- Two cases for each split:
- Without swapping: Left-left and Right-right.
- With swapping: Left-right and Right-left.
- Recursively check both cases.

⸻

## 6 Egg Dropping Problem
**Problem Statement**

You are given e eggs and f floors. Find the minimum number of trials needed in the worst case to find the critical floor where the egg starts breaking.

**Core Idea:**

Partition by trying every floor k (from 1 to f).
Two possibilities:
- Egg breaks → search below (e-1 eggs, k-1 floors).
- Egg doesn’t break → search above (e eggs, f-k floors).
We aim to minimize the maximum of these two outcomes (because we care about the worst-case scenario).
- DP Relation:
`dp(e, f) = 1 + min over all k (max(dp(e-1, k-1), dp(e, f-k)))`


**MCM Usage**:
- Same as Matrix Chain Multiplication (MCM) (trying all partitions).
- But instead of minimizing sum of costs, we minimize the maximum trials (due to worst-case requirement).
- Important Points:
- Base Cases:
  - If floors = 0 → 0 trials.
  - If floors = 1 → 1 trial.
  - If eggs = 1 → f trials (brute-force each floor).
- Can be optimized further with binary search on k because the two parts (breaks vs survives) are monotonic.
---

# Core Concept Behind All Child Problems

| Concept | Description |
|:--------|:------------|
| Partitioning | Try all possible splits/partitions of the input |
| Recursion + Memoization | Solve left and right sides recursively, cache results |
| Combining Results | Merge answers depending on the problem logic (minimize, maximize, count, validate) |
| Optimal Selection | Choose split giving minimum, maximum, or valid result as per problem |

---

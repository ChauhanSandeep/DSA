

## Matrix Chain Multiplication (MCM)

### How to know that a problem is related to MCM?
Not every problem is exactly MCM, but many are variations or analogs (e.g., the "Minimum Cost to Cut a Stick" problem you referenced earlier is a direct analog). You can identify MCM-like problems by these characteristics:

- Sequence of Operations on a Chain/Interval: The problem involves a linear sequence (e.g., matrices, segments, or items) where you perform associative operations (like multiplication, cutting, or merging) in an order that affects the total cost.
- Cost Depends on Boundaries and Subproblems: The cost of an operation is based on the "dimensions" or "lengths" of the current segment (e.g., matrix sizes or stick lengths). Subproblems are defined over intervals/subchains, and the total cost is the sum of subproblem costs plus a boundary-dependent cost.
- Optimization Goal: You need to minimize (or sometimes maximize) the total cost by choosing the optimal order or split points. The operations are exhaustive (all items must be processed).
- Overlapping Subproblems and Optimal Substructure: Solving smaller intervals optimally contributes to the larger solution, with many reusable subcomputations—perfect for dynamic programming.

##### Common Indicators:
- Problems phrased as "find min/max cost to process a sequence" (e.g., multiply matrices, cut a stick/rod, burst balloons).
- Involves trying all possible split points (k) in a range [i, j] and adding a cost like arr[i] * arr[k] * arr[j] (or similar, like length(j - i)).

---
### Generic Steps to Follow for Matrix Chain Multiplication
This is bottom-up (tabulation) approach,but a top-down (memoization) variant follows similar logic.

1. **Characterize the Optimal Substructure**:
  - Define the problem recursively: The min cost for multiplying matrices from i to j is the min over all split points k (i ≤ k < j) of [cost(i to k) + cost(k+1 to j) + cost of combining them (e.g., dimensions[i-1] * dimensions[k] * dimensions[j])].
  - Base case: Cost for a single matrix (i == j) is 0.

2. **Set Up the DP Table**:
  - Create a 2D array dp[n+1][n+1], where dp[i][j] = min cost to multiply matrices from i to j (1-based indexing often used).
  - Initialize diagonals (dp[i][i] = 0) and unused parts (e.g., lower triangle) to infinity or ignore them.

3. **Fill the DP Table Bottom-Up**:
  - Iterate over chain lengths (l) from 2 to n (number of matrices).
  - For each length l:
    - Iterate over starting index i from 1 to (n - l + 1).
    - Set j = i + l - 1 (end of current chain).
    - Initialize dp[i][j] to a large number (e.g., infinity).
    - For each possible split k from i to j-1:
      - Compute temp_cost = dp[i][k] + dp[k+1][j] + dimensions[i-1] * dimensions[k] * dimensions[j].
      - Update dp[i][j] = min(dp[i][j], temp_cost).
  - This builds from small chains to the full chain.

4. **Extract the Result**:
  - The answer is dp[n], the min cost for the entire chain.[10]

5. **(Optional) Reconstruct the Solution**:
  - If needed, track split points (e.g., in another table) to print the optimal parenthesization/order.
---
### Generic Code Template for MCM

```java
public int matrixChainMultiplication(int[] inputArr) {  // inputArr = array of inputArr, size inputLength+1 for inputLength matrices
  // Step 1: Initialize variables and determine the number of matrices
  int inputLength = inputArr.length - 1;  // Number of matrices is one less than inputArr array length

  // Step 2: Create and initialize DP table
  int[][] dp = new int[inputLength + 1][inputLength + 1];
  for (int[] row : dp) {
    Arrays.fill(row, Integer.MAX_VALUE / 2);  // Avoid overflow
  }

  // Step 3: Fill diagonal elements (base case - single matrix has 0 cost)
  for (int i = 1; i <= inputLength; i++) {
    dp[i][i] = 0;
  }

  // Step 4: Fill DP table for chains of increasing length
  for (int length = 2; length <= inputLength; length++) {  // Chain length from 2 to inputLength

    // Step 5: For each starting position of current chain length
    for (int i = 1; i <= inputLength - length + 1; i++) {

      // Step 6: Calculate ending position for current chain
      int j = i + length - 1;

      // Step 7: Try all possible intermediate split points
      for (int intermediateIndex = i; intermediateIndex < j; intermediateIndex++) {

        // Step 8: Calculate cost for current split
        int cost = dp[i][intermediateIndex] +
            dp[intermediateIndex + 1][j] +
            inputArr[i - 1] * inputArr[intermediateIndex] * inputArr[j];

        // Step 9: Update minimum cost for current subproblem
        dp[i][j] = Math.min(dp[i][j], cost);
      }
    }
  }

  // Step 10: Return the minimum cost for the entire chain
  return dp[1][inputLength];  // Fixed: was dp[10][inputLength] which was incorrect
}
```

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

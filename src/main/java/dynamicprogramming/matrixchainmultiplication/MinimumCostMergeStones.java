package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;


/**
 * Minimum Cost to Merge Stones
 *
 * There are n piles of stones arranged in a row. The ith pile has stones[i] stones.
 * A move consists of merging exactly k consecutive piles into one pile, and the cost
 * of this move is equal to the total number of stones in these k piles.
 *
 * Return the minimum cost to merge all piles into one pile. If it is impossible, return -1.
 *
 * Key constraint: You can only merge exactly k consecutive piles at a time.
 * Each merge operation reduces the number of piles by (k-1).
 *
 * Example:
 * Input: stones = [3,2,4,1], k = 2
 * Output: 20
 * Explanation:
 * - Merge [3,2] for cost 5, left with [5,4,1]
 * - Merge [4,1] for cost 5, left with [5,5]
 * - Merge [5,5] for cost 10, left with [10]
 * Total cost: 5 + 5 + 10 = 20
 *
 * LeetCode: https://leetcode.com/problems/minimum-cost-to-merge-stones/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if different stone types have different merge costs beyond just weight?
 *    Answer: Extend DP state to include stone type combinations and modify cost calculation.
 * 2. How would you handle circular stone arrangements where ends can connect?
 *    Answer: Use circular DP by considering all possible starting points and circular splits.
 * 3. What if we can merge any consecutive subsequence, not just exactly k piles?
 *    Answer: Modify DP transitions to consider all possible merge sizes from 2 to remaining piles.
 * 4. How to optimize for very large arrays with repeated patterns?
 *    Answer: Use matrix exponentiation or identify repeating subproblems for memoization.
 *
 * Related Problems:
 * - LeetCode 312: Burst Balloons (Interval DP)
 * - LeetCode 1130: Minimum Cost Tree From Leaf Values (Interval DP)
 * - LeetCode 375: Guess Number Higher or Lower II (Interval DP)
 */
public class MinimumCostMergeStones {

  public static void main(String[] args) {
    MinimumCostMergeStones solver = new MinimumCostMergeStones();

    // Test case for k=2 (binary merge)
    int[] stones = {3, 2, 4, 1};
    System.out.println("Min cost (k=2): " + solver.mergeStones(stones, 2)); // Expected: 20
  }

    /**
     * Main method: 2D Dynamic Programming with interval optimization.
     * Step-by-step:
     *  1. Check feasibility: To merge n piles into 1, we need (n-1) to be divisible by (k-1)
     *     - Each merge reduces piles by (k-1), so after m merges: n - m*(k-1) = 1
     *     - This gives: (n-1) % (k-1) == 0
     *  2. Define DP state: dp[i][j] = minimum cost to merge stones[i..j] into minimum possible piles
     *     - Minimum piles = ((j-i) % (k-1)) + 1
     *  3. For each interval [i,j]:
     *     a. Try all split points: split at i, i+k-1, i+2*(k-1), ... (increment by k-1)
     *     b. Each split divides into two parts that are optimally merged
     *     c. If (j-i) is divisible by (k-1), we can merge into 1 pile (add sum cost)
     *  4. Use prefix sum for O(1) range sum queries
     *
     * Key Insight:
     * Each merge reduces piles by exactly (k-1). To merge interval into 1 pile, we first
     * merge into k piles, then do final merge. Split points must be at k-1 intervals to
     * ensure both sides can be optimally merged. The (n-1) % (k-1) == 0 condition ensures
     * we can eventually reach 1 pile.
     *
     * Algorithm: Interval Dynamic Programming.
     * Time Complexity: O(n³/k), outer loops O(n²), inner loop O(n/k) split points.
     * Space Complexity: O(n²) for DP table.
     */
    public int mergeStones(int[] stones, int k) {
        int length = stones.length;
        
        // Check if merging to 1 pile is possible
        if ((length - 1) % (k - 1) != 0) {
            return -1;
        }
        
        // Compute prefix sums for O(1) range sum
        int[] prefixSum = new int[length + 1];
        for (int i = 0; i < length; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // dp[i][j] = min cost to merge stones[i..j] into minimum piles
        int[][] dp = new int[length][length];
        
        // Fill DP table for increasing interval lengths
        for (int gap = k; gap <= length; gap++) {
            for (int start = 0; start + gap <= length; start++) {
                int end = start + gap - 1;
                dp[start][end] = Integer.MAX_VALUE;
                
                // Try all valid split points (increment by k-1)
                for (int mid = start; mid < end; mid += k - 1) {
                    dp[start][end] = Math.min(dp[start][end], 
                                       dp[start][mid] + dp[mid + 1][end]);
                }
                
                // If this interval can be merged into 1 pile, add merge cost
                if ((end - start) % (k - 1) == 0) {
                    dp[start][end] += prefixSum[end + 1] - prefixSum[start];
                }
            }
        }
        
        return dp[0][length - 1];
    }

    /**
     * Alternative method: Top-Down DP with Memoization (easier to understand).
     * Step-by-step:
     *  1. Use recursion with memoization to compute minimum cost
     *  2. Base case: if i >= j, cost is 0 (single pile or invalid)
     *  3. Try all split points at k-1 intervals
     *  4. Recursively compute cost for left and right parts
     *  5. If interval length allows merging to 1 pile, add merge cost
     *
     * Key Insight:
     * Same logic as bottom-up but uses recursion with memo for clarity.
     * Easier to understand the subproblem decomposition.
     *
     * Algorithm: Top-Down DP with Memoization.
     * Time Complexity: O(n³/k).
     * Space Complexity: O(n²) for memoization + O(n) recursion stack.
     */
    public int mergeStonesTopDown(int[] stones, int k) {
        int length = stones.length;
        
        // Check feasibility
        if ((length - 1) % (k - 1) != 0) {
            return -1;
        }
        
        // Prefix sum
        int[] prefixSum = new int[length + 1];
        for (int i = 0; i < length; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // Memoization table (-1 means not computed)
        int[][] memo = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                memo[i][j] = -1;
            }
        }
        
        return helper(stones, k, 0, length - 1, prefixSum, memo);
    }

    /**
     * Helper: Recursively computes minimum cost to merge stones[i..j].
     */
    private int helper(int[] stones, int k, int i, int j, 
                      int[] prefixSum, int[][] memo) {
        // Base case: single pile or invalid
        if (i >= j) {
            return 0;
        }
        
        // Check memo
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        int minCost = Integer.MAX_VALUE;
        
        // Try all valid split points (increment by k-1)
        for (int mid = i; mid < j; mid += k - 1) {
            int cost = helper(stones, k, i, mid, prefixSum, memo) + 
                       helper(stones, k, mid + 1, j, prefixSum, memo);
            minCost = Math.min(minCost, cost);
        }
        
        // If this interval can be merged into 1 pile, add merge cost
        if ((j - i) % (k - 1) == 0) {
            minCost += prefixSum[j + 1] - prefixSum[i];
        }
        
        memo[i][j] = minCost;
        return minCost;
    }
}

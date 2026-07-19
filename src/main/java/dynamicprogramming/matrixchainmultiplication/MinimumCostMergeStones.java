package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;


/**
 * Problem: Minimum Cost to Merge Stones
 *
 * Given stone piles in a row, one move merges exactly k consecutive piles and
 * costs the total stones in those piles. Return the minimum cost to merge all
 * piles into one pile, or -1 if the exact-k merge rule makes that impossible.
 *
 * Leetcode: https://leetcode.com/problems/minimum-cost-to-merge-stones/
 * Rating:   2423 (zerotrac Elo)
 * Pattern:  Dynamic Programming | Interval DP | k-way merge feasibility
 *
 * Example:
 *   Input:  stones = [3,2,4,1], k = 2
 *   Output: 20
 *   Why:    merge [3,2] for 5, [4,1] for 5, then [5,5] for 10, giving total 20.
 *
 * Follow-ups:
 *   1. Why can some inputs never become one pile?
 *      Each merge reduces pile count by k-1, so n-1 must be divisible by k-1.
 *   2. Can this handle circular piles?
 *      Duplicate the array and run interval DP over all length-n windows, taking the minimum valid result.
 *   3. Can you reconstruct the merge sequence?
 *      Store the split that minimized each interval and recursively output merges.
 *
 * Related: Burst Balloons (312), Minimum Cost Tree From Leaf Values (1130).
 */
public class MinimumCostMergeStones {

        /**
     * Intuition: merging k piles reduces the pile count by k - 1, which determines
     * which intervals can become one pile. For a valid interval, split points spaced
     * by k - 1 preserve feasible pile counts on both sides.
     *
     * Algorithm:
     *   1. Reject arrays where (length - 1) is not divisible by k - 1.
     *   2. Build prefix sums for O(1) interval merge costs.
     *   3. Fill dp[start][end] by increasing interval length and valid split points.
     *   4. When an interval can merge into one pile, add its stone sum.
     *
     * Time:  O(n^3 / k) - each interval tries split points spaced by k - 1.
     * Space: O(n^2) - interval DP table.
     *
     * @param stones pile sizes
     * @param k number of adjacent piles merged at once
     * @return minimum merge cost, or -1 if impossible
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
     * Intuition: the top-down version asks for the minimum cost of an interval and
     * uses the same feasible split spacing. Memoization keeps each interval from
     * being recomputed.
     *
     * Algorithm:
     *   1. Reject impossible global pile counts.
     *   2. Build prefix sums and an interval memo table.
     *   3. Recursively try valid split points for each interval.
     *   4. Add the interval sum when the interval can collapse to one pile.
     *
     * Time:  O(n^3 / k) - each interval scans valid split points.
     * Space: O(n^2) - memo table plus recursion depth.
     *
     * @param stones pile sizes
     * @param k number of adjacent piles merged at once
     * @return minimum merge cost, or -1 if impossible
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

        /** Recursively returns the minimum merge cost for stones[i..j]. */
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


    public static void main(String[] args) {
        MinimumCostMergeStones solver = new MinimumCostMergeStones();
        int[][] stones = { {3, 2, 4, 1}, {3, 2, 4, 1}, {3} };
        int[] kValues = {2, 3, 2};
        int[] expected = {20, -1, 0};

        for (int i = 0; i < stones.length; i++) {
            int output = solver.mergeStones(stones[i], kValues[i]);
            System.out.printf("stones=%s k=%d  ->  %d  expected=%d%n",
                Arrays.toString(stones[i]), kValues[i], output, expected[i]);
        }
    }

}

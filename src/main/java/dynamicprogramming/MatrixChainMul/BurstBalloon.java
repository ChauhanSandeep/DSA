package dynamicprogramming.MatrixChainMul;

import java.util.Arrays;


/**
 * LeetCode Problem: Burst Balloons
 * Link: https://leetcode.com/problems/burst-balloons/
 *
 * Problem Statement:
 * - You are given `n` balloons, indexed from `0` to `n-1`, each having a value in an array `nums`.
 * - When you burst the `i-th` balloon, you gain `nums[i-1] * nums[i] * nums[i+1]` coins.
 * - If `i-1` or `i+1` is out of bounds, assume a balloon with value `1` exists there.
 * - Find the maximum coins that can be collected by bursting all balloons optimally.
 *
 * Example:
 * Input: nums = [3, 1, 5, 8]
 * Output: 167
 * Explanation:
 * - Burst balloons in the order: 1, 2, 0, 3
 * - nums = [3,1,5,8] --> [3,5,8] --> [3,8] --> [8] --> []
 * - coins =  3*1*5    +   3*5*8   +  1*3*8  + 1*8*1 = 167
 *
 * Follow-up Questions:
 * 1. How would the solution change if we wanted to minimize the coins instead?
 *      - We could modify the DP to take min instead of max, but the problem is about max;
 *      minimization might not make sense in this context as coins are positive.
 * 2. What if balloons could be burst in groups?
 *      - That would require a different DP state, perhaps segmenting into groups, but here it's individual bursts.
 * 3. How to handle if some balloons give negative coins?
 *      - DP would still work, but we'd need to consider if skipping is allowed; here all must be burst.
 * Relevant follow-up problem: https://leetcode.com/problems/matrix-chain-multiplication/ (similar interval DP optimization).
 */
public class BurstBalloon {

    public static void main(String[] args) {
        int[] nums = {3, 1, 5, 8};
        BurstBalloon solver = new BurstBalloon();
        System.out.println("Maximum coins: " + solver.maxCoinsIterative(nums));
    }

    /**
     * Solves the Burst Balloons problem using recursion with memoization.
     *
     * Intuition:
     * - Think of the last balloon to burst between a given range (left, right).
     * - Bursting balloon `i` at last will maximize coins for subproblem (left, right).
     * - So, try every balloon as the last one to burst in the current range.
     * - Use memoization to store results of (left, right) to avoid recomputation.
     *
     * Steps:
     * 1. Add virtual balloons with value 1 at the start and end of nums.
     * 2. Recursively calculate the maximum coins for each subarray.
     * 3. Memoize results to optimize overlapping subproblems.
     *
     * Time Complexity: O(n^3)
     * - For each (left, right) pair (~n^2 pairs), we try all balloons in between (~n choices).
     *
     * Space Complexity: O(n^2)
     * - For memoization table + O(n) stack space due to recursion depth.
     *
     * @param nums the array of balloon values
     * @return maximum coins collected
     */
    public int maxCoinsRecursiveApproach(int[] nums) {
        int length = nums.length;
        int[][] memo = new int[length][length]; // memo[i][j] stores max coins for range i to j

        // Fill memo with -1 to indicate uncomputed states
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return burstRecHelper(nums, 0, length - 1, memo);
    }

    /**
     * Recursive helper to calculate max coins between left and right indices (inclusive).
     *
     * @param nums the original balloon values
     * @param left start index
     * @param right end index
     * @param memo memoization table
     * @return max coins collected
     */
    private int burstRecHelper(int[] nums, int left, int right, int[][] memo) {
        // Base case: no balloons to burst
        if (left > right) {
            return 0;
        }

        // Return memoized result
        if (memo[left][right] != -1) {
            return memo[left][right];
        }

        int maxCoinsInRange = 0;

        // Try bursting each balloon between left and right as the last one
        for (int i = left; i <= right; i++) {
            // Coins from bursting balloon i is calculated as:
            // nums[left - 1] * nums[i] * nums[right + 1]
            int coinsFromCurrent = getValue(nums, left - 1) * nums[i] * getValue(nums, right + 1);

            // Coins from remaining left and right partitions
            int coinsFromLeft = burstRecHelper(nums, left, i - 1, memo);
            int coinsFromRight = burstRecHelper(nums, i + 1, right, memo);

            int currentTotalCoins = coinsFromCurrent + coinsFromLeft + coinsFromRight;

            maxCoinsInRange = Math.max(maxCoinsInRange, currentTotalCoins);
        }

        // Memoize and return
        memo[left][right] = maxCoinsInRange;
        return maxCoinsInRange;
    }

    /**
     * Computes the maximum coins collectible by bursting balloons optimally.
     * Steps:
     * 1. Create a DP table to store results for subproblems.
     * 2. Iterate over all possible subarrays of balloons.
     * 3. For each subarray, try bursting each balloon and calculate the maximum coins.
     * 4. Store results in the DP table.
     * 5. Return the result for the entire array.
     *
     * Time Complexity: O(n^3)
     * - For each (left, right) pair (~n^2 pairs), we try all balloons in between (~n choices).
     * Space Complexity: O(n^2)
     * - For DP table storing results for all [left, right] ranges.
     *
     * @param nums Original array representing balloon values.
     * @return Maximum coins that can be collected.
     */
    public int maxCoinsIterative(int[] nums) {
        int length = nums.length;
        int[][] dp = new int[length][length];

        // Fill DP table for subarrays of different lengths
        for (int gap = 0; gap < length; gap++) {
            for (int startIndex = 0; startIndex + gap < length; startIndex++) {
                int endIndex = startIndex + gap;
                int maxCoinsInRange = Integer.MIN_VALUE;

                // Try bursting each balloon in the range [startIndex, endIndex]
                for (int burstIndex = startIndex; burstIndex <= endIndex; burstIndex++) {
                    int coinsFromLeft = (burstIndex == startIndex) ? 0 : dp[startIndex][burstIndex - 1]; // Left subproblem
                    int coinsFromRight = (burstIndex == endIndex) ? 0 : dp[burstIndex + 1][endIndex]; // Right subproblem
                    int coinsFromCurrent = getValue(nums, startIndex - 1) * nums[burstIndex] * getValue(nums, endIndex + 1);

                    int currentTotalCoins = coinsFromLeft + coinsFromRight + coinsFromCurrent;
                    maxCoinsInRange = Math.max(maxCoinsInRange, currentTotalCoins);
                }

                dp[startIndex][endIndex] = maxCoinsInRange;
            }
        }

        return dp[0][length - 1];
    }

    /**
     * Helper function to handle out-of-bounds cases (treats missing elements as `1`).
     */
    private int getValue(int[] nums, int index) {
        if (index < 0 || index >= nums.length) return 1;
        return nums[index];
    }
}

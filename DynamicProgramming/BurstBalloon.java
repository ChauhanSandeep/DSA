package DynamicProgramming;

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
 * Approach:
 * - Uses **Dynamic Programming** with the concept of partitions.
 * - Defines `dp[left][right]` as the maximum coins that can be obtained by bursting balloons in the range `[left, right]`.
 * - Iterates over all possible partition points and calculates the best gain for each subproblem.
 * 
 * Time Complexity: **O(n^3)** (Nested loops iterating over `n`, solving `n^2` subproblems)
 * Space Complexity: **O(n^2)** (DP table storing results for all `[left, right]` ranges)
 */
public class BurstBalloon {

    public static void main(String[] args) {
        int[] nums = {3, 1, 5, 8};
        BurstBalloon solver = new BurstBalloon();
        System.out.println("Maximum coins: " + solver.maxCoins(nums));
    }

    /**
     * Computes the maximum coins collectible by bursting balloons optimally.
     * 
     * @param nums Original array representing balloon values.
     * @return Maximum coins that can be collected.
     */
    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];

        // Fill DP table for subarrays of different lengths
        for (int length = 0; length < n; length++) {
            for (int left = 0, right = length; right < n; left++, right++) {
                int maxGain = Integer.MIN_VALUE;

                // Try bursting each balloon in the range [left, right]
                for (int lastBurst = left; lastBurst <= right; lastBurst++) {
                    int leftGain = (lastBurst == left) ? 0 : dp[left][lastBurst - 1]; // Left subproblem
                    int rightGain = (lastBurst == right) ? 0 : dp[lastBurst + 1][right]; // Right subproblem
                    int balloonValue = getValue(nums, left - 1) * nums[lastBurst] * getValue(nums, right + 1);

                    int totalGain = leftGain + rightGain + balloonValue;
                    maxGain = Math.max(maxGain, totalGain);
                }

                dp[left][right] = maxGain;
            }
        }

        return dp[0][n - 1];
    }

    /**
     * Helper function to handle out-of-bounds cases (treats missing elements as `1`).
     */
    private int getValue(int[] nums, int index) {
        if (index < 0 || index >= nums.length) return 1;
        return nums[index];
    }
}

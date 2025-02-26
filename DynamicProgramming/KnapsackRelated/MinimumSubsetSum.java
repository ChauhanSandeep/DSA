package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Partition the given array into two subsets such that the absolute difference 
 * between their sums is minimized.
 * 
 * LeetCode Link: https://leetcode.com/problems/partition-equal-subset-sum/ (related problem)
 *
 * Approach:
 * - Compute the total sum of the array.
 * - The goal is to find a subset with sum closest to totalSum / 2.
 * - Use dynamic programming to check possible subset sums up to totalSum / 2.
 * - The minimum difference is given by: minDifference = totalSum - 2 * closestSubsetSum.
 *
 * Time Complexity: O(N * sum/2) = O(N * sum), where N is the array length.
 * Space Complexity: O(N * sum/2) = O(N * sum), for the DP table.
 */
public class MinimumSubsetSum {
    public static void main(String[] args) {
        int[] arr = {1, 6, 11, 5};
        System.out.println("Minimum Subset Sum Difference: " + findMinimumSubsetSumDifference(arr));
    }

    /**
     * Finds the minimum absolute difference between two subset sums.
     *
     * @param arr Input array of positive integers
     * @return Minimum subset sum difference
     */
    public static int findMinimumSubsetSumDifference(int[] arr) {
        if (arr.length == 0) {
            return 0; // Edge case: Empty array
        }

        int totalSum = Arrays.stream(arr).sum();
        int targetSum = totalSum / 2;

        boolean[][] dp = subsetSumDP(arr, targetSum);

        int closestSubsetSum = findClosestSubsetSum(dp, arr.length, targetSum);
        return totalSum - 2 * closestSubsetSum;
    }

    /**
     * Computes a DP table to check if subset sums up to targetSum are achievable.
     *
     * @param arr       Input array
     * @param targetSum Maximum subset sum to check
     * @return DP table where dp[i][j] is true if sum j is possible with first i elements
     */
    private static boolean[][] subsetSumDP(int[] arr, int targetSum) {
        int size = arr.length;
        boolean[][] dp = new boolean[size + 1][targetSum + 1];

        // Base Case: A subset sum of 0 is always possible (empty subset)
        for (int i = 0; i <= size; i++) {
            dp[i][0] = true;
        }

        // Build the DP table
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= targetSum; j++) {
                if (arr[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - arr[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp;
    }

    /**
     * Finds the closest possible subset sum to targetSum.
     *
     * @param dp        DP table containing subset sum possibilities
     * @param size      Number of elements in the array
     * @param targetSum Maximum subset sum to consider
     * @return The largest subset sum ≤ targetSum that can be achieved
     */
    private static int findClosestSubsetSum(boolean[][] dp, int size, int targetSum) {
        for (int sum = targetSum; sum >= 0; sum--) {
            if (dp[size][sum]) {
                return sum;
            }
        }
        return 0; // This should never be reached due to dp[i][0] = true
    }
}

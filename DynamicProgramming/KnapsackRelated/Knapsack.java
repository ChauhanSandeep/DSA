package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: 0/1 Knapsack
 *
 * Problem Statement:
 * You are given `n` items, each with a weight `wt[i]` and a value `val[i]`.
 * Your task is to find the maximum total value you can achieve without exceeding a given capacity `W`.
 * Each item can either be included (1) or excluded (0) — fractional inclusion is not allowed.
 *
 * Leetcode Equivalent: No exact match, but conceptually linked to:
 * https://leetcode.com/problems/partition-equal-subset-sum/
 */
public class Knapsack {

    /**
     * Recursive Approach (with memoization):
     *
     * Intuition:
     * At every index, you have two choices — include the item or exclude it.
     * You explore both and take the one that gives you the maximum value.
     *
     * Approach:
     * - Base Case: if you reach index 0, take the item if it fits.
     * - Recursively calculate max value for taking or skipping the item.
     * - Use memoization to avoid recomputation.
     *
     * Time Complexity: O(length * capacity)
     * Space Complexity: O(length * capacity) for memo + O(length) stack space
     */
    public static int knapsackRecursive(int[] weights, int[] values, int length, int capacity) {
        int[][] dp = new int[length][capacity + 1];
        for (int[] row : dp) {
            java.util.Arrays.fill(row, -1);
        }
        return helper(weights, values, length - 1, capacity, dp);
    }

    private static int helper(int[] weights, int[] values, int currentIndex, int capacity, int[][] dp) {
        if (currentIndex == 0) {
            if (weights[0] <= capacity) return values[0];
            return 0;
        }

        if (dp[currentIndex][capacity] != -1) return dp[currentIndex][capacity];

        int excludeItem = helper(weights, values, currentIndex - 1, capacity, dp);
        int includeItem = 0;
        if (weights[currentIndex] <= capacity) {
            includeItem = values[currentIndex] + helper(weights, values, currentIndex - 1, capacity - weights[currentIndex], dp);
        }

        return dp[currentIndex][capacity] = Math.max(includeItem, excludeItem);
    }

    /**
     * Iterative Tabulation Approach:
     *
     * Intuition:
     * Use a DP table where dp[i][w] stores the max value for items [0..i] and capacity w.
     * Build the table bottom-up.
     *
     * Approach:
     * - Initialize first row based on whether item 0 can be taken.
     * - Loop through items and capacities, applying the recurrence relation.
     *
     * Time Complexity: O(length * capacity)
     * Space Complexity: O(length * capacity)
     */
    public static int knapsackIterative(int[] weights, int[] values, int length, int capacity) {
        int[][] dp = new int[length][capacity + 1]; // dp[i][j] denotes max value for first i items and capacity j

        // Initialize for the first item.
        for (int w = weights[0]; w <= capacity; w++) {
            dp[0][w] = values[0];
        }

        for (int itemIndex = 1; itemIndex < length; itemIndex++) {
            for (int remainingCapacity = 0; remainingCapacity <= capacity; remainingCapacity++) {
                int excludeItem = dp[itemIndex - 1][remainingCapacity];
                int includeItem = 0;
                if (weights[itemIndex] <= remainingCapacity) {
                    // If we can include the item, calculate the value.
                    // Include the item and add its value to the remaining capacity.
                    includeItem = values[itemIndex] + dp[itemIndex - 1][remainingCapacity - weights[itemIndex]];
                }
                dp[itemIndex][remainingCapacity] = Math.max(includeItem, excludeItem);
            }
        }

        return dp[length - 1][capacity];
    }

    public static void main(String[] args) {
        int[] wt = {1, 3, 4, 5};
        int[] val = {1, 4, 5, 7};
        int capacity = 7;

        System.out.println("Recursive (Memoized): " + knapsackRecursive(wt, val, wt.length, capacity));
        System.out.println("Iterative (Tabulated): " + knapsackIterative(wt, val, wt.length, capacity));
    }
}

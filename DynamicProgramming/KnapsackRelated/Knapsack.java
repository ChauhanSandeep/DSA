package DynamicProgramming.KnapsackRelated;

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
    public static int knapsackRecursive(int[] weights, int[] values, int length, int maxCapacity) {
        // Create memoization table with default value -1
        int[][] memo = new int[length][maxCapacity + 1]; // memo[i][j] denotes max value for first i items and capacity j
        for (int[] row : memo) {
            java.util.Arrays.fill(row, -1);
        }
        return knapsackRecursiveHelper(weights, values, 0, maxCapacity, memo);
    }

    /**
     * Recursive helper to compute maximum value using memoization.
     * @param weights Array of item weights.
     * @param values Array of item values.
     * @param currentIndex Current item index being considered.
     * @param remainingCapacity Remaining capacity in the knapsack.
     * @param memo DP table storing intermediate results.
     * @return Maximum value possible from current index onward.
     */
    private static int knapsackRecursiveHelper(int[] weights, int[] values, int currentIndex, int remainingCapacity, int[][] memo) {
        // Base condition: no more items to check
        if (currentIndex == weights.length) {
            return 0;
        }

        // Return cached value if already computed
        if (memo[currentIndex][remainingCapacity] != -1) {
            return memo[currentIndex][remainingCapacity];
        }

        // Option 1: Do not include the current item
        int exclude = knapsackRecursiveHelper(weights, values, currentIndex + 1, remainingCapacity, memo);

        // Option 2: Include the current item (if it fits)
        int include = 0;
        if (weights[currentIndex] <= remainingCapacity) {
            include = values[currentIndex] +
                knapsackRecursiveHelper(weights, values, currentIndex + 1, remainingCapacity - weights[currentIndex], memo);
        }

        // Store and return the maximum of both options
        memo[currentIndex][remainingCapacity] = Math.max(include, exclude);
        return memo[currentIndex][remainingCapacity];
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

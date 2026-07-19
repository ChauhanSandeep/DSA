package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: 0/1 Knapsack
 *
 * Given item weights, item values, and a maximum capacity, choose a subset of
 * items with total weight at most capacity and maximum total value. Each item can
 * be taken once or skipped.
 *
 * Source: GeeksForGeeks 0/1 Knapsack Problem
 * Pattern:  Dynamic Programming | 0/1 knapsack | Take or skip each item
 *
 * Example:
 *   Input:  weights = [1,3,4,5], values = [1,4,5,7], capacity = 7
 *   Output: 9
 *   Why:    taking weights 3 and 4 gives value 4 + 5 = 9 within capacity 7.
 *
 * Follow-ups:
 *   1. Can you print the chosen items?
 *      Store take/skip decisions or backtrack through the DP table from the final cell.
 *   2. What changes for unbounded knapsack?
 *      The take transition stays in the same item row so the item can be reused.
 *   3. Can space be reduced to O(capacity)?
 *      Yes; use a 1D array and iterate capacity backward for each item.
 *
 * Related: Partition Equal Subset Sum (416), Target Sum (494).
 */
public class Knapsack {

        /**
     * Intuition: for each item, the optimal value comes from one of two futures:
     * skip it and keep capacity unchanged, or take it and spend its weight. The
     * best answer for currentIndex and remainingCapacity is reused many times.
     *
     * Algorithm:
     *   1. Allocate memo by item index and remaining capacity.
     *   2. At each item, compute the value if skipped.
     *   3. If the item fits, compute value if taken.
     *   4. Memoize and return the better of the two choices.
     *
     * Time:  O(length * maxCapacity) - each item/capacity state is solved once.
     * Space: O(length * maxCapacity) - memo table plus recursion depth.
     *
     * @param weights item weights
     * @param values item values
     * @param length number of items
     * @param maxCapacity knapsack capacity
     * @return maximum value that fits in the knapsack
     */
    public static int knapsackRecursive(int[] weights, int[] values, int length, int maxCapacity) {
        // Create memoization table with default value -1
        int[][] memo = new int[length][maxCapacity + 1]; // memo[i][j] denotes max value for first i items and capacity j
        for (int[] row : memo) {
            java.util.Arrays.fill(row, -1);
        }
        return knapsackRecursiveHelper(weights, values, 0, maxCapacity, memo);
    }

        /** Solves the take/skip state for currentIndex and remainingCapacity. */
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
     * Intuition: the recursive take/skip state can be filled as a table where rows
     * are item prefixes and columns are capacities. Each cell asks whether the last
     * item of the prefix should be skipped or included.
     *
     * Algorithm:
     *   1. Create dp[itemCount][capacity] for best value from a prefix.
     *   2. Copy the value from skipping the current item.
     *   3. If the current item fits, compare with taking it.
     *   4. Return the full-prefix value at maxCapacity.
     *
     * Time:  O(length * maxCapacity) - every table cell is filled once.
     * Space: O(length * maxCapacity) - the DP table stores all prefix capacities.
     *
     * @param weights item weights
     * @param values item values
     * @param length number of items
     * @param maxCapacity knapsack capacity
     * @return maximum value that fits in the knapsack
     */
    public static int knapsackIterative(int[] weights, int[] values, int length, int maxCapacity) {
        int[][] dp = new int[length + 1][maxCapacity + 1];

        // Base case: 0 items or 0 capacity gives 0 value
        for (int itemCount = 0; itemCount <= length; itemCount++) {
            dp[itemCount][0] = 0;
        }
        for (int capacity = 0; capacity <= maxCapacity; capacity++) {
            dp[0][capacity] = 0;
        }

        // Fill the dp table
        for (int itemCount = 1; itemCount <= length; itemCount++) {
            for (int capacity = 0; capacity <= maxCapacity; capacity++) {
                int currentItemIndex = itemCount - 1;

                // Option 1: Don't include current item
                dp[itemCount][capacity] = dp[itemCount - 1][capacity];

                // Option 2: Include current item if weight allows
                if (weights[currentItemIndex] <= capacity) {
                    int valueWithItem = values[currentItemIndex] +
                        dp[itemCount - 1][capacity - weights[currentItemIndex]];
                    dp[itemCount][capacity] = Math.max(dp[itemCount][capacity], valueWithItem);
                }
            }
        }

        return dp[length][maxCapacity];
    }


    public static void main(String[] args) {
        int[][] weights = { {}, {4, 5, 1}, {1, 3, 4, 5} };
        int[][] values = { {}, {1, 2, 3}, {1, 4, 5, 7} };
        int[] capacities = {7, 4, 7};
        int[] expected = {0, 3, 9};

        for (int i = 0; i < weights.length; i++) {
            int output = knapsackIterative(weights[i], values[i], weights[i].length, capacities[i]);
            System.out.printf("weights=%s values=%s capacity=%d  ->  %d  expected=%d%n",
                Arrays.toString(weights[i]), Arrays.toString(values[i]), capacities[i], output, expected[i]);
        }
    }

}

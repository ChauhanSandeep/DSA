package dynamicprogramming.knapsackbounded;

/**
 * Problem Statement:
 * Given N items where each item has a weight (wt[i]) and value (val[i]), and a knapsack with capacity W,
 * determine the maximum value that can be obtained by selecting items such that the total weight does not exceed W.
 * Each item can either be included completely (1) or excluded (0), hence the name 0-1 Knapsack.
 *
 * Example 1:
 * Input: W = 4, val[] = [1, 2, 3], wt[] = [4, 5, 1]
 * Output: 3
 * Explanation:
 * There are two items with weight less than or equal to 4.
 * - Selecting item with weight 4 gives profit = 1
 * - Selecting item with weight 1 gives profit = 3
 * Maximum possible profit is 3 (by choosing the item with weight 1 and value 3).
 *
 * Example 2:
 * Input: W = 5, val[] = [3, 4, 5, 6], wt[] = [2, 3, 4, 5]
 * Output: 7
 * Explanation:
 * Best combination is selecting items with weights 2 and 3 (total weight = 5, total value = 3 + 4 = 7).
 *
 * GeeksForGeeks link: https://www.geeksforgeeks.org/problems/0-1-knapsack-problem0945/1
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - Can you print the items included in the optimal solution, not just the maximum value?
 *    → Yes, maintain backtracking pointers in the DP table to reconstruct which items were selected.
 *  - How would you modify this to solve the unbounded knapsack problem where items can be chosen multiple times?
 *    → Change the DP recurrence to allow reusing the same item: dp[i][w] = max(dp[i-1][w], val[i] + dp[i][w - wt[i]]).
 *  - What if you need to handle fractional knapsack where items can be broken?
 *    → Use a greedy approach: sort by value/weight ratio and pick greedily. This gives O(n log n) solution.
 *  - Can you optimize space complexity further?
 *    → Yes, use 1D DP array by iterating in reverse order for weights. Space becomes O(W).
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 416 (Partition Equal Subset Sum): https://leetcode.com/problems/partition-equal-subset-sum/
 *  - LeetCode 494 (Target Sum): https://leetcode.com/problems/target-sum/
 *  - LeetCode 1049 (Last Stone Weight II): https://leetcode.com/problems/last-stone-weight-ii/
 * LeetCode Contest Rating: Not available (not a contest problem)
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
     * Alternative method: Bottom-Up DP (Tabulation). Avoids recursion overhead.
     * Step-by-step:
     *  1. Create dp table where dp[itemCount][capacity] represents max value with first itemCount items
     *     and capacity.
     *  2. Initialize base case: dp[0][capacity] = 0 for all capacity (no items means 0 value).
     *  3. For each item itemCount from 1 to length:
     *     - For each capacity from 0 to maxCapacity:
     *       - If current item weight > capacity: dp[itemCount][capacity] = dp[itemCount-1][capacity] (cannot include)
     *       - Else: dp[itemCount][capacity] = max(dp[itemCount-1][capacity], values[itemCount-1] + dp[itemCount-1][capacity - weights[itemCount-1]])
     *  4. Return dp[length][maxCapacity].
     *
     * Algorithm: Dynamic Programming with Tabulation (0-1 Knapsack).
     * Time Complexity: O(length * maxCapacity), where length is number of items and maxCapacity is knapsack capacity.
     * Space Complexity: O(length * maxCapacity) for the dp table.
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
        int[] wt = {1, 3, 4, 5};
        int[] val = {1, 4, 5, 7};
        int capacity = 7;

        System.out.println("Recursive (Memoized): " + knapsackRecursive(wt, val, wt.length, capacity));
        System.out.println("Iterative (Tabulated): " + knapsackIterative(wt, val, wt.length, capacity));
    }
}

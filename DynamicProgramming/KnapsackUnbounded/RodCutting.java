package DynamicProgramming.KnapsackUnbounded;

import java.util.Arrays;

/**
 * Rod Cutting Problem (Unbounded Knapsack)
 * 
 * Given a rod of length `N` and an array `prices` where `prices[i]` represents the price of a rod piece of length `i+1`,
 * determine the maximum profit obtainable by cutting the rod and selling the pieces.
 * 
 * Approach:
 * - This is an **Unbounded Knapsack** problem where:
 *   - We can **reuse** the same piece multiple times.
 *   - We decide to **include** or **exclude** a piece of a given length.
 * - The key recurrence relation:
 *   - If we use a piece of length `i`: `dp[i][j] = max(prices[i-1] + dp[i][j - (i)], dp[i-1][j])`
 *   - If we don't use it: `dp[i][j] = dp[i-1][j]`
 * 
 * Time Complexity: O(N²), where N is the rod length.
 * Space Complexity: O(N²), due to the DP table.
 */
public class RodCutting {
    public static void main(String[] args) {
        int[] prices = {1, 5, 8, 9}; // Prices for rod pieces of length 1, 2, 3, 4
        int rodLength = prices.length;
        int maxProfit = cutRod(prices, rodLength);
        System.out.println("Maximum Profit: " + maxProfit);
    }

    /**
     * Solves the rod cutting problem using dynamic programming (bottom-up approach).
     *
     * @param prices   Price array where prices[i] is the price of a rod piece of length i+1.
     * @param rodLength The total length of the rod.
     * @return The maximum profit obtainable.
     */
    public static int cutRod(int[] prices, int rodLength) {
        int[][] dp = new int[rodLength + 1][rodLength + 1];

        for (int i = 1; i <= rodLength; i++) { // Iterate over possible rod pieces
            for (int j = 1; j <= rodLength; j++) { // Iterate over total rod length
                if (i <= j) { // Can we take this piece?
                    dp[i][j] = Math.max(prices[i - 1] + dp[i][j - i], dp[i - 1][j]); 
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        System.out.println(Arrays.deepToString(dp)); // Debugging: Print DP table
        return dp[rodLength][rodLength];
    }
}

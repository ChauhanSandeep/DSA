package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Coins in a Line (Two Players Game)
 * 
 * Description:
 * - Given `n` coins in a line (where `n` is even), two players take turns picking a coin from either end.
 * - The goal is to maximize the amount collected by the first player.
 * - Both players play optimally.
 * 
 * Approach:
 * - This is a variation of the "Optimal Game Strategy" problem.
 * - We use **Dynamic Programming (DP)** to store results of overlapping subproblems.
 * - Two approaches are implemented:
 *   1. **Recursive DP with Memoization** (`maxCoinsRecursive`)
 *   2. **Optimized DP Approach** (`maxCoinsOptimized`) with a reduced state space.
 * 
 * Time Complexity:
 * - Recursive DP: **O(n²)**
 * - Optimized DP: **O(n²)**
 * 
 * Space Complexity:
 * - Recursive DP: **O(n²)** (for memoization table)
 * - Optimized DP: **O(n²)** (for DP table)
 * 
 * Problem Link:
 * https://www.interviewbit.com/problems/coins-in-a-line/
 */
public class CoinsInLine {
    public static void main(String[] args) {
        int[] coins = {5, 4, 8, 10};
        CoinsInLine game = new CoinsInLine();
        System.out.println("Max coins (Recursive DP): " + game.maxCoinsRecursive(coins));
        System.out.println("Max coins (Optimized DP): " + game.maxCoinsOptimized(coins));
    }

    /**
     * Recursive DP approach with memoization.
     * 
     * @param coins Array representing coin values.
     * @return Maximum amount the first player can collect.
     */
    public int maxCoinsRecursive(int[] coins) {
        int n = coins.length;
        int[][][] memo = new int[n][n][2];

        // Initialize memoization table with -1
        for (int[][] matrix : memo) {
            for (int[] row : matrix) {
                Arrays.fill(row, -1);
            }
        }

        return maxCoinsHelper(coins, 0, n - 1, true, memo);
    }

    /**
     * Helper function for Recursive DP approach.
     * 
     * @param coins   Array representing coin values.
     * @param start   Starting index of available coins.
     * @param end     Ending index of available coins.
     * @param isPlayerOne Turn indicator (true for Player 1, false for Player 2).
     * @param memo    DP table for memoization.
     * @return Maximum coins that can be collected.
     */
    private int maxCoinsHelper(int[] coins, int start, int end, boolean isPlayerOne, int[][][] memo) {
        if (start > end) return 0;

        int playerIdx = isPlayerOne ? 1 : 0;

        // Return memoized result
        if (memo[start][end][playerIdx] != -1) {
            return memo[start][end][playerIdx];
        }

        if (isPlayerOne) {
            // Maximize Player 1's score
            int pickStart = coins[start] + maxCoinsHelper(coins, start + 1, end, false, memo);
            int pickEnd = coins[end] + maxCoinsHelper(coins, start, end - 1, false, memo);
            memo[start][end][playerIdx] = Math.max(pickStart, pickEnd);
        } else {
            // Minimize Player 2's score (opponent plays optimally)
            int pickStart = maxCoinsHelper(coins, start + 1, end, true, memo);
            int pickEnd = maxCoinsHelper(coins, start, end - 1, true, memo);
            memo[start][end][playerIdx] = Math.min(pickStart, pickEnd);
        }

        return memo[start][end][playerIdx];
    }

    /**
     * Optimized DP approach (without tracking opponent moves).
     * 
     * @param coins Array representing coin values.
     * @return Maximum amount the first player can collect.
     */
    public int maxCoinsOptimized(int[] coins) {
        int n = coins.length;
        int[][] dp = new int[n][n];

        return maxCoinsOptimizedHelper(coins, 0, n - 1, dp);
    }

    /**
     * Helper function for Optimized DP approach.
     * 
     * @param coins Array representing coin values.
     * @param start Starting index of available coins.
     * @param end   Ending index of available coins.
     * @param dp    DP table to store results.
     * @return Maximum coins that can be collected.
     */
    private int maxCoinsOptimizedHelper(int[] coins, int start, int end, int[][] dp) {
        if (start > end) {
            return 0;
        }

        // Return precomputed result
        if (dp[start][end] != 0) {
            return dp[start][end];
        }

        // Player picks start or end, minimizing opponent's best move
        int pickStart = coins[start] + Math.min(
            maxCoinsOptimizedHelper(coins, start + 2, end, dp),
            maxCoinsOptimizedHelper(coins, start + 1, end - 1, dp)
        );

        int pickEnd = coins[end] + Math.min(
            maxCoinsOptimizedHelper(coins, start + 1, end - 1, dp),
            maxCoinsOptimizedHelper(coins, start, end - 2, dp)
        );

        dp[start][end] = Math.max(pickStart, pickEnd);
        return dp[start][end];
    }
}

package dynamicprogramming;

import java.util.Arrays;

/**
 * Problem: Coins in a Line (Two Players Game)
 *
 * Description:
 * - Given `n` coins in a line (where `n` is even), two players take turns picking a coin from either end.
 * - The goal is to maximize the amount collected by the first player.
 * - Both players play optimally.
 *
 * Example:
 *  Input: [5, 4, 8, 10]
 *  Output: 15
 *  Explanation:
 *  - Player 1 picks 10 (right end), Player 2 picks 5 (left end).
 *  Player 1 then picks 8 (right end), Player 2 picks 4 (left end).
 *  Player 1's total = 10 + 8 = 18, Player 2's total = 5 + 4 = 9.
 *  Player 1 wins with a total of 18 coins.
 *
 * Intuition:
 * - The first player can choose either the leftmost or rightmost coin.
 * - The second player will then choose optimally from the remaining coins.
 * - The first player must anticipate the second player's optimal moves to maximize their own score.
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
     * Time complexity: O(n²)
     * Space complexity: O(n²)
     */
    public int maxCoinsRecursive(int[] coins) {
        int n = coins.length;
        // memo[start][end][playerIdx] signifies the maximum coins that can be collected
        // from coins[start] to coins[end] when it's playerIdx's turn (0 for Player 2, 1 for Player 1).
        int[][][] memo = new int[n][n][2];


        // Initialize memoization table with -1
        for (int[][] matrix : memo) {
            for (int[] row : matrix) {
                Arrays.fill(row, -1);
            }
        }

        return maxCoinsHelper(coins, 0, n - 1, true, memo);
    }

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
     * Optimized DP approach : This approach uses a 2D DP table to track the maximum coins
     * We do not need to track the opponent's moves explicitly,
     * as we can compute the maximum coins directly
     *
     * Time complexity: O(n²)
     * Space complexity: O(n²)
     * While time and space complexity remain the same as the recursive approach,
     * this method is more efficient in practice due to reduced overhead from recursion and memoization.
     */
    public int maxCoinsOptimized(int[] coins) {
        int n = coins.length;
        // dp[start][end] signifies the maximum coins that can be collected
        // from coins[start] to coins[end] when it's the first player's turn.
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
        int pickStart = coins[start] + Math.min( // take Math.min because the opponent will choose optimally for themselves
            maxCoinsOptimizedHelper(coins, start + 2, end, dp), // opponent picks next start
            maxCoinsOptimizedHelper(coins, start + 1, end - 1, dp) // opponent picks end
        );

        int pickEnd = coins[end] + Math.min(
            maxCoinsOptimizedHelper(coins, start + 1, end - 1, dp), // opponent picks start
            maxCoinsOptimizedHelper(coins, start, end - 2, dp) // opponent picks next end
        );

        dp[start][end] = Math.max(pickStart, pickEnd);
        return dp[start][end];
    }
}

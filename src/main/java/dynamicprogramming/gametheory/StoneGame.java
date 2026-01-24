package dynamicprogramming.gametheory;

/**
 * Problem: Stone Game
 *
 * Given an array `piles` where `piles[i]` represents the number of stones in each pile,
 * two players (Alex & Lee) take turns picking stones from either end. The goal is to determine
 * whether Alex can win given both play optimally.
 *
 * Example:
 * Input: piles = [5, 3, 4, 5]
 * Output: true (Alex can win)
 * Explanation: Alex can pick 5, then Lee picks 3, Alex picks 5, and Lee picks 4.
 *
 * LeetCode Problem: https://leetcode.com/problems/stone-game/
 */
public class StoneGame {

    public static void main(String[] args) {
        int[] piles = {5, 3, 4, 5};
        System.out.println("Alex wins? " + stoneGameDP(piles));   // Using DP
    }

    /**
     * **Recursive DFS with Memoization**
     * - Uses a depth-first search (DFS) approach to explore all possible moves.
     * - Memoization is used to store results of subproblems to avoid redundant calculations.
     *
     * Time Complexity: O(N²), because for each pair (start, end), we compute the result once.
     * Space Complexity: O(N²) for memoization table
     *
     * @param piles Array of stone piles
     * @return true if Alex can win, false otherwise
     */
    public boolean stoneGameRecursive(int[] piles) {
        int length = piles.length;
        Integer[][] memo = new Integer[length][length];

        // Alex wins if he can get strictly more than opponent
        return maxDiffRec(0, length - 1, piles, memo) > 0;
    }

    /**
     * @param start Start index
     * @param end End index
     * @param piles Array of stone piles
     * @param memo Memoization table
     * @return Maximum score difference the current player can guarantee
     */
    private int maxDiffRec(int start, int end, int[] piles, Integer[][] memo) {
        if (start == end) return piles[start]; // Only one pile to take

        if (memo[start][end] != null) return memo[start][end];

        // Pick start: take piles[start], opponent plays optimally on [start+1, end]
        int pickLeft = piles[start] - maxDiffRec(start + 1, end, piles, memo);

        // Pick end: take piles[end], opponent plays optimally on [start, end-1]
        int pickRight = piles[end] - maxDiffRec(start, end - 1, piles, memo);

        memo[start][end] = Math.max(pickLeft, pickRight);
        return memo[start][end];
    }


    /**
     * **Dynamic Programming (Top-Down Memoization)**
     * - Uses a `dp` table where `dp[i][j]` stores the maximum stones Alex can collect
     *   from `piles[i]` to `piles[j]`, assuming both play optimally.
     *
     * Time Complexity: O(N²),
     * Space Complexity: O(N²)
     */
    public static boolean stoneGameDP(int[] piles) {
        int length = piles.length;
        int[][] dp = new int[length][length]; // dp[i][j] = max stones Alex can collect from piles[i] to piles[j]

        // Base case: When i == j, the only choice is taking the single pile
        for (int i = 0; i < length; i++) {
            dp[i][i] = piles[i];
        }

        // Fill DP table for subarrays of increasing length
        for (int len = 2; len <= length; len++) {
            for (int i = 0; i <= length - len; i++) {
                int j = i + len - 1;
                // Alex picks either left (i) or right (j)
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }

        // Alex wins if his total stones are greater than half of the total
        return dp[0][length - 1] > 0;
    }
}

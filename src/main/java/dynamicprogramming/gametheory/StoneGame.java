package dynamicprogramming.gametheory;

import java.util.Arrays;

/**
 * Problem: Stone Game
 *
 * Two players alternately take a pile from either end of the array. Both play
 * optimally, and the method decides whether Alex can finish with more stones
 * than Lee.
 *
 * Leetcode: https://leetcode.com/problems/stone-game/ (Medium)
 * Rating:   zerotrac 1590 (Q2, weekly-contest-95)
 * Pattern:  Dynamic Programming | Game theory | Score difference intervals
 *
 * Example:
 *   Input:  piles = [5,3,4,5]
 *   Output: true
 *   Why:    optimal play lets Alex finish ahead by choosing an end pile that
 *           leaves Lee a worse interval.
 *
 * Follow-ups:
 *   1. Return Alex's maximum winning margin?
 *      Return dp[0][n - 1] directly instead of checking whether it is positive.
 *   2. What if a player may take up to k piles from an end?
 *      Expand each state transition over all allowed take counts.
 *   3. What if the piles are circular?
 *      Try breaking the circle at each removed pile or use interval DP over doubled input.
 *
 * Related: Predict the Winner (486), Stone Game II (1140), Stone Game VII (1690).
 */
public class StoneGame {

    public static void main(String[] args) {
        int[][] cases = { {5, 3, 4, 5}, {3, 7, 2, 3} };
        boolean[] expected = { true, true };

        for (int i = 0; i < cases.length; i++) {
            boolean got = stoneGameDP(cases[i]);
            System.out.printf("piles=%s -> %s  expected=%s%n",
                Arrays.toString(cases[i]), got, expected[i]);
        }
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
     * Intuition: dp[i][j] stores the best score difference the current player
     * can force on the interval piles[i..j]. Taking the left pile gains piles[i]
     * now, but then the opponent becomes the current player on [i + 1..j], so
     * that future advantage is subtracted. Taking the right pile is symmetric,
     * and optimal play keeps the larger difference.
     *
     * Algorithm:
     *   1. Initialize dp[i][i] with piles[i] because one pile can be taken immediately.
     *   2. Fill intervals by increasing length so smaller intervals are already known.
     *   3. For each interval, choose the better of taking the left or right pile.
     *
     * Time:  O(n^2) - every interval [i, j] is filled once.
     * Space: O(n^2) - the table stores one score difference per interval.
     *
     * @param piles stone counts in order
     * @return true if Alex can force a positive score difference
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

package Recursion;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Unique Paths
 *
 * Count how many ways a robot can travel from the top-left cell of an m by n
 * grid to the bottom-right cell. The robot may only move down or right, so every
 * path has the same length but a different ordering of those moves.
 *
 * Leetcode: https://leetcode.com/problems/unique-paths/ (Medium)
 * Rating:   acceptance 67.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Recursion | Memoization | Grid choices down or right
 *
 * Example:
 *   Input:  m = 3, n = 7
 *   Output: 28
 *   Why:    every path is a sequence of 2 down moves and 6 right moves, and
 *           there are 28 positions for the down moves among those 8 steps.
 *
 * Follow-ups:
 *   1. How do you solve this in O(1) extra space?
 *      Use the combinatorics formula C(m+n-2, m-1) with careful multiplication.
 *   2. What if some cells are blocked?
 *      Memoize only valid cells, or use DP where blocked cells contribute 0 paths.
 *   3. What if diagonal moves are also allowed?
 *      Add a third recursive branch and memoize the larger state transition.
 *   4. What if the grid is huge and the answer must be modulo a prime?
 *      Use factorials and modular inverses for the binomial coefficient.
 *
 * Related: Unique Paths II (63), Minimum Path Sum (64), Unique Paths III (980).
 */
public class NumberOfPaths {

    public static void main(String[] args) {
        int[][] grids = { {0, 5}, {1, 5}, {3, 7} };
        long[] expected = { 0, 1, 28 };

        for (int i = 0; i < grids.length; i++) {
            long got = numberOfPaths(grids[i][0], grids[i][1]);
            System.out.printf("grid=%dx%d -> %d  expected=%d%n",
                grids[i][0], grids[i][1], got, expected[i]);
        }
    }

    /**
     * Intuition: from any cell, every complete path begins with exactly one of
     * two choices: go down or go right. Those choices are disjoint, so the number
     * of paths from a cell is the sum of the answers from those two neighboring
     * cells. The base cases give meaning to the edges of the recursion tree:
     * outside the grid contributes 0, and the destination contributes 1 completed
     * path. Memoization makes each coordinate's answer get computed once.
     *
     * Algorithm:
     *   1. Create a memo map for solved grid coordinates.
     *   2. Start counting from cell (1,1).
     *   3. Return 0 for out-of-bounds cells and 1 for the destination.
     *   4. Cache and return down-paths plus right-paths for each remaining cell.
     *
     * Time:  O(m*n) - memoization computes each grid cell once.
     * Space: O(m*n) - the memo table stores one value per cell, plus recursion stack.
     *
     * @param m number of rows
     * @param n number of columns
     * @return number of unique down/right paths
     */
    public static long numberOfPaths(int m, int n) {
        Map<String, Long> memo = new HashMap<>();
        return countPaths(1, 1, m, n, memo);
    }

    /** Counts paths from the current cell to the destination, caching repeated cells. */
    private static long countPaths(int i, int j, int rows, int cols, Map<String, Long> memo) {
        if (i > rows || j > cols) return 0;  // Out of bounds
        if (i == rows && j == cols) return 1; // Reached destination

        String key = i + "," + j;
        if (memo.containsKey(key)) return memo.get(key); // Return cached result

        long paths = countPaths(i + 1, j, rows, cols, memo) // Move down
                   + countPaths(i, j + 1, rows, cols, memo); // Move right

        memo.put(key, paths); // Store result in cache
        return paths;
    }

    /**
     * Computes number of unique paths using bottom-up DP (0-based indexing).
     *
     * @param m Number of rows.
     * @param n Number of columns.
     * @return Total number of unique paths from (0,0) to (m-1,n-1)
     */
    public static long numberOfPathsIterative(int m, int n) {
        long[][] dp = new long[m][n]; // dp[i][j] = number of paths to reach cell (i,j) from (0,0)

        // Base cases: First row and first column = 1 path
        for (int i = 0; i < m; i++) dp[i][0] = 1; // can travel only in one direction
        for (int j = 0; j < n; j++) dp[0][j] = 1; // can travel only in one direction

        // Fill rest of the table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // Total paths is sum of paths from top and left cells
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }
}

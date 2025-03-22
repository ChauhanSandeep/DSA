package recursion;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Find the number of unique paths from the top-left corner (1,1)
 * to the bottom-right corner (m,n) in an m x n grid.
 * - Only **right** and **down** movements are allowed.
 *
 * Approach:
 * - **Recursive Backtracking:** Try both right and down moves.
 * - **Memoization (Top-Down Dynamic Programming):** Avoid redundant calculations.
 * - **Mathematical Formula Approach (Optimal)**: Using combinatorics (Binomial Coefficient).
 *
 * Time Complexity (Recursive): O(2^(m+n)) (Exponential, without memoization)
 * Time Complexity (Optimized DP): O(m * n) (With Memoization)
 * Space Complexity: O(m * n) (For memoization storage)
 *
 * LeetCode Link: https://leetcode.com/problems/unique-paths/
 */
public class NumberOfPaths {

    public static void main(String[] args) {
        System.out.println("Number of unique paths (3x3 grid): " + numberOfPaths(3, 3));
        System.out.println("Number of unique paths (5x5 grid): " + numberOfPaths(5, 5));
    }

    /**
     * Finds the number of unique paths in an `m x n` grid.
     *
     * @param m Number of rows.
     * @param n Number of columns.
     * @return Total number of unique paths.
     */
    public static long numberOfPaths(int m, int n) {
        Map<String, Long> memo = new HashMap<>();
        return countPaths(1, 1, m, n, memo);
    }

    /**
     * Recursive function to count unique paths using memoization.
     *
     * @param i    Current row index.
     * @param j    Current column index.
     * @param rows Total rows in the grid.
     * @param cols Total columns in the grid.
     * @param memo Cache to store previously computed results.
     * @return Number of unique paths from (i, j) to (rows, cols).
     */
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
}

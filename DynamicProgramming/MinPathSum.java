package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Minimum Path Sum in a Grid
 *
 * Given an `m x n` grid filled with non-negative numbers, find a path from
 * the **top-left** to the **bottom-right** corner, minimizing the sum of all numbers along the path.
 *
 * You can only move **down** or **right** at any point in time.
 *
 * Example:
 * Input:
 * grid = [
 *   [1, 3, 1],
 *   [1, 5, 1],
 *   [4, 2, 1]
 * ]
 * Output: 7  (Path: 1 → 3 → 1 → 1 → 1)
 *
 * Approach:
 * - **Bottom-Up Dynamic Programming**
 * - `dp[i][j]` represents **minimum sum path to reach (i, j)**.
 * - Transition Formula:
 *   - If coming from **above**: `dp[i][j] = dp[i-1][j] + grid[i][j]`
 *   - If coming from **left**: `dp[i][j] = dp[i][j-1] + grid[i][j]`
 *   - Take **minimum of both**.
 *
 * Time Complexity: **O(m * n)**
 * Space Complexity: **O(m * n) → Can be optimized to O(n)**
 */
public class MinPathSum {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };
        System.out.println("Minimum Path Sum: " + minPathSum(grid));
    }

    public static int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] dp = new int[rows][cols]; // DP table to store min path sum

        // Initialize DP table with grid values
        dp[0][0] = grid[0][0];

        // Fill first row (only right moves possible)
        for (int j = 1; j < cols; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }

        // Fill first column (only down moves possible)
        for (int i = 1; i < rows; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }

        // Fill the rest of the DP table
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }

        return dp[rows - 1][cols - 1]; // Min path sum to reach bottom-right
    }
}

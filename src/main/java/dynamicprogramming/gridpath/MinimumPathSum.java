package dynamicprogramming.gridpath;

import java.util.Arrays;

/**
 * Problem: Minimum Path Sum
 *
 * Given a grid of non-negative numbers, move from the top-left cell to the
 * bottom-right cell using only right or down moves. Return the minimum possible
 * sum of values along such a path.
 *
 * Leetcode: https://leetcode.com/problems/minimum-path-sum/ (Medium)
 * Rating:   acceptance 68.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Grid DP | Prefix minimum path cost
 *
 * Example:
 *   Input:  pathGrid = [[1,3,1],[1,5,1],[4,2,1]]
 *   Output: 7
 *   Why:    the path 1, 3, 1, 1, 1 has the smallest sum among all right/down paths.
 *
 * Follow-ups:
 *   1. Return the path, not just the sum?
 *      Store parent directions while filling the DP table.
 *   2. Can space be reduced to O(n)?
 *      Keep one row because each cell needs only the value above and to the left.
 *   3. What if obstacles are present?
 *      Treat blocked cells as unreachable and skip their transitions.
 *
 * Related: Unique Paths II (63), Dungeon Game (174), Triangle (120).
 */
public class MinimumPathSum {

    public static void main(String[] args) {
        MinimumPathSum solver = new MinimumPathSum();
        int[][][] cases = {
            {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}},
            {{5}}
        };
        int[] expected = { 7, 5 };

        for (int i = 0; i < cases.length; i++) {
            int got = solver.minPathSum(cases[i]);
            System.out.printf("pathGrid=%s -> %d  expected=%d%n",
                Arrays.deepToString(cases[i]), got, expected[i]);
        }
    }


        /**
     * Intuition: minSumToCell[row][col] means the cheapest sum needed to reach
     * that exact cell from the start. The first row can only come from the left,
     * and the first column can only come from above. Every interior cell has two
     * possible predecessors, so its best sum is its own value plus the smaller
     * of those two predecessor sums.
     *
     * Algorithm:
     *   1. Return 0 for a null or empty grid.
     *   2. Initialize the start cell, then cumulative sums for the first row and column.
     *   3. Fill every remaining cell from top-left to bottom-right using min(above, left) plus current value.
     *
     * Time:  O(m * n) - every grid cell is filled once.
     * Space: O(m * n) - the DP table stores one minimum sum per cell.
     *
     * @param pathGrid grid of non-negative path costs
     * @return minimum sum along a top-left to bottom-right path
     */
    public int minPathSum(int[][] pathGrid) {
        if (pathGrid == null || pathGrid.length == 0 || pathGrid[0].length == 0) {
            return 0;
        }

        int numRows = pathGrid.length;
        int numCols = pathGrid[0].length;

        // DP table to store minimum path sum to each cell
        int[][] minSumToCell = new int[numRows][numCols];

        // Base case: starting cell
        minSumToCell[0][0] = pathGrid[0][0];

        // Fill first row: can only move right
        for (int colIndex = 1; colIndex < numCols; colIndex++) {
            minSumToCell[0][colIndex] = minSumToCell[0][colIndex - 1] + pathGrid[0][colIndex];
        }

        // Fill first column: can only move down
        for (int rowIndex = 1; rowIndex < numRows; rowIndex++) {
            minSumToCell[rowIndex][0] = minSumToCell[rowIndex - 1][0] + pathGrid[rowIndex][0];
        }

        // Fill remaining cells: choose minimum of coming from above or left
        for (int rowIndex = 1; rowIndex < numRows; rowIndex++) {
            for (int colIndex = 1; colIndex < numCols; colIndex++) {
                int pathFromAbove = minSumToCell[rowIndex - 1][colIndex];
                int pathFromLeft = minSumToCell[rowIndex][colIndex - 1];

                minSumToCell[rowIndex][colIndex] = Math.min(pathFromAbove, pathFromLeft) + pathGrid[rowIndex][colIndex];
            }
        }

        return minSumToCell[numRows - 1][numCols - 1];
    }

    /**
     * Space-optimized solution using 1D array instead of 2D DP table.
     *
     * Algorithm Steps:
     * 1. Use single array representing current row being processed
     * 2. Initialize first row with cumulative sums
     * 3. For each subsequent row, update array in-place
     * 4. For each cell, dp[j] = min(dp[j] from above, dp[j-1] from left) + current value
     * 5. Final answer is in dp[n-1]
     *
     * Time Complexity: O(m * n) where m is rows and n is columns
     * Space Complexity: O(n) where n is number of columns
     *
     * @param pathGrid 2D grid with non-negative integers
     * @return Minimum sum of path from top-left to bottom-right
     */
    public int minPathSumOptimized(int[][] pathGrid) {
        if (pathGrid == null || pathGrid.length == 0 || pathGrid[0].length == 0) {
            return 0;
        }

        int numRows = pathGrid.length;
        int numCols = pathGrid[0].length;

        // Use 1D array to store minimum path sum for current row
        int[] minSumCurrentRow = new int[numCols];

        // Initialize first row
        minSumCurrentRow[0] = pathGrid[0][0];
        for (int colIndex = 1; colIndex < numCols; colIndex++) {
            minSumCurrentRow[colIndex] = minSumCurrentRow[colIndex - 1] + pathGrid[0][colIndex];
        }

        // Process remaining rows
        for (int rowIndex = 1; rowIndex < numRows; rowIndex++) {
            // First column can only come from above
            minSumCurrentRow[0] += pathGrid[rowIndex][0];

            // Remaining columns can come from left or above
            for (int colIndex = 1; colIndex < numCols; colIndex++) {
                int pathFromAbove = minSumCurrentRow[colIndex]; // Current value represents path from above
                int pathFromLeft = minSumCurrentRow[colIndex - 1]; // Already updated value from left

                minSumCurrentRow[colIndex] = Math.min(pathFromAbove, pathFromLeft) + pathGrid[rowIndex][colIndex];
            }
        }

        return minSumCurrentRow[numCols - 1];
    }
}

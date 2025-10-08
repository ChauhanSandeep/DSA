package dynamicprogramming;

/**
 * Problem: Minimum Path Sum
 *
 * Given an m x n grid filled with non-negative numbers, find a path from the top-left to the
 * bottom-right, which minimizes the sum of all numbers along its path.
 * Note: You can only move either down or right at any point in time.
 *
 * Example:
 * Input: grid = [[1,3,1],
 *                [1,5,1],
 *                [4,2,1]]
 * Output: 7
 * Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/minimum-path-sum/
 *
 * Follow-up Questions:
 * 1. Q: What if you can move in all 4 directions but cannot revisit cells?
 *    A: Use DFS with backtracking or Dijkstra's algorithm for shortest path
 * 2. Q: Find the actual path, not just the minimum sum?
 *    A: Store parent pointers in DP table and reconstruct path from bottom-right to top-left
 * 3. Q: What if some cells are blocked (obstacles)?
 *    A: Similar to Unique Paths II - mark blocked cells and skip them in DP transitions
 * 4. Q: What if you can move diagonally as well?
 *    A: Extend DP transition to include diagonal move: min(up, left, diagonal) + current
 * 5. Q: Maximum Path Sum instead of minimum?
 *    A: Change Math.min to Math.max in the recurrence relation
 */
public class MinimumPathSum {

    public static void main(String[] args) {
        int[][] pathGrid = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        MinimumPathSum solver = new MinimumPathSum();
        System.out.println("Minimum Path Sum: " + solver.minPathSum(pathGrid));
        System.out.println("Minimum Path Sum (Space Optimized): " + solver.minPathSumOptimized(pathGrid));
    }

    /**
     * Finds minimum path sum from top-left to bottom-right using 2D dynamic programming.
     *
     * Algorithm Steps:
     * 1. Initialize DP table where dp[i][j] represents minimum sum to reach cell (i,j)
     * 2. Fill first row: can only come from left, so cumulative sum
     * 3. Fill first column: can only come from above, so cumulative sum
     * 4. Fill remaining cells: min(from above, from left) + current cell value
     * 5. Return dp[m-1][n-1] as the minimum path sum to bottom-right
     *
     * Time Complexity: O(m * n) where m is rows and n is columns
     * Space Complexity: O(m * n) for the DP table
     *
     * @param pathGrid 2D grid with non-negative integers
     * @return Minimum sum of path from top-left to bottom-right
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

package dynamicprogramming.gridpath;

import java.util.*;

/**
 * Problem: Longest Increasing Path in a Matrix (LeetCode #329)
 *
 * Problem Statement:
 * Given an m x n integers matrix, return the length of the longest increasing path in the matrix.
 * From each cell, you can move in four directions: left, right, up, or down. You may NOT
 * move diagonally or move outside the boundary (i.e., wrap-around is not allowed).
 *
 * Example 1:
 * Input: matrix = [[9,9,4],
 *                 [6,6,8],
 *                 [2,1,1]]
 * Output: 4
 * Explanation: The longest increasing path is [1, 2, 6, 9].
 *
 * Example 2:
 * Input: matrix = [[3,4,5],
 *                 [3,2,6],
 *                 [2,2,1]]
 * Output: 4
 * Explanation: The longest increasing path is [3, 4, 5, 6].
 *
 * Approach:
 * We'll use dynamic programming with memoization to solve this problem. The key insight is that the longest
 * increasing path starting at a cell (i,j) is 1 plus the maximum of the longest paths starting from its
 * neighbors with greater values. We'll use memoization to avoid recalculating the longest path for the same cell.
 *
 * Steps to solve:
 * 1. Create a memoization table to store the longest increasing path starting from each cell.
 * 2. For each cell in the matrix, perform a depth-first search (DFS) to find the longest increasing path.
 * 3. During DFS, if we've already computed the result for a cell, return the memoized value.
 * 4. Otherwise, explore all four possible directions (up, down, left, right) and recursively find the longest path.
 * 5. The result for the current cell is 1 + the maximum of the results from the four directions.
 * 6. Keep track of the maximum path length found during the process.
 *
 * Time Complexity: O(m*n) where m is the number of rows and n is the number of columns
 * Space Complexity: O(m*n) for the memoization table
 *
 * Follow-up Questions:
 * 1. What if we need to return the actual path instead of just its length?
 *    Answer: We can modify the solution to store the path along with the length. Instead of just storing the length
 *    in the memo table, we can store both the length and the path. When exploring directions, we'll keep track of
 *    the path that gives us the maximum length.
 *
 * 2. What if the matrix is very large and doesn't fit in memory?
 *    Answer: We can process the matrix in chunks or use external memory algorithms. We might also consider using
 *    a BFS-based approach with level-order traversal to limit memory usage.
 *
 * 3. Can we solve this using BFS instead of DFS?
 *    Answer: While possible, BFS would be less efficient as it would require processing each cell multiple times.
 *    The DFS with memoization approach is more suitable for this problem as it naturally explores all possible
 *    paths from each cell and caches the results.
 *
 * LeetCode: https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
 */
public class LongestIncreasingPathInAMatrix {
    // Directions: up, right, down, left
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    /**
     * Calculates the length of the longest increasing path in the matrix.
     *
     * Steps to solve:
     * 1. Check for edge cases (null or empty matrix).
     * 2. Initialize a memoization table with the same dimensions as the matrix.
     * 3. For each cell in the matrix, perform DFS to find the longest increasing path.
     * 4. During DFS, check if the result is already cached in the memo table.
     * 5. For each direction, check if the next cell is within bounds and has a greater value.
     * 6. Update the maximum path length found so far.
     *
     * Time Complexity: O(m*n) where m is the number of rows and n is the number of columns
     * Space Complexity: O(m*n) for the memoization table
     *
     * @param matrix The input matrix of integers
     * @return The length of the longest increasing path
     */
    public int longestIncreasingPath(int[][] matrix) {
        // Check for edge cases
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxLength = 0;

        // Memoization table to store the longest increasing path starting from each cell
        int[][] memo = new int[rows][cols];

        // Perform DFS from each cell to find the longest increasing path
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int currentLength = dfs(matrix, i, j, memo);
                maxLength = Math.max(maxLength, currentLength);
            }
        }

        return maxLength;
    }

    /**
     * Helper method to perform DFS and find the longest increasing path starting from (i,j).
     * This also updates memo table with the max path length found from this cell.
     */
    private int dfs(int[][] matrix, int i, int j, int[][] memo) {
        // If we've already computed the result for this cell, return it
        if (memo[i][j] != 0) {
            return memo[i][j];
        }

        // The minimum path length is 1 (the cell itself)
        int maxPathLength = 1;

        // Explore all four directions
        for (int[] dir : DIRECTIONS) {
            int newRow = i + dir[0];
            int newCol = j + dir[1];

            // Check if the next cell is within bounds and has a greater value
            if (isValid(matrix, newRow, newCol) && matrix[newRow][newCol] > matrix[i][j]) {
                int currentLength = 1 + dfs(matrix, newRow, newCol, memo);
                maxPathLength = Math.max(maxPathLength, currentLength);
            }
        }

        // Memoize the result
        memo[i][j] = maxPathLength;
        return maxPathLength;
    }

    /**
     * Checks if the given cell (i,j) is within the matrix bounds
     */
    private boolean isValid(int[][] matrix, int i, int j) {
        return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length;
    }

    /**
     * Alternative solution using BFS with topological sort (Kahn's algorithm)
     *
     * This approach treats the matrix as a directed acyclic graph (DAG) where an edge exists from cell A to cell B
     * if A's value is less than B's value and they are adjacent. We can then perform a topological sort and
     * find the longest path in the DAG.
     *
     * Time Complexity: O(m*n)
     * Space Complexity: O(m*n)
     */
    public int longestIncreasingPathBFS(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Directions: up, right, down, left
        int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        // outDegree[i][j] represents the number of adjacent cells with greater values
        int[][] outDegree = new int[rows][cols];

        // Calculate outDegree for each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int[] dir : dirs) {
                    int newRow = i + dir[0];
                    int newCol = j + dir[1];

                    if (isValid(matrix, newRow, newCol) && matrix[newRow][newCol] > matrix[i][j]) {
                        outDegree[i][j]++;
                    }
                }
            }
        }

        // Initialize the queue with all cells that have outDegree 0 (sinks)
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (outDegree[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                }
            }
        }

        int maxLength = 0;

        // Perform BFS level by level
        while (!queue.isEmpty()) {
            maxLength++;
            int size = queue.size();

            for (int k = 0; k < size; k++) {
                int[] cell = queue.poll();
                int i = cell[0];
                int j = cell[1];

                // Check all four directions
                for (int[] dir : dirs) {
                    int newRow = i + dir[0];
                    int newCol = j + dir[1];

                    // If the neighbor is valid and has a smaller value
                    if (isValid(matrix, newRow, newCol) && matrix[newRow][newCol] < matrix[i][j]) {
                        outDegree[newRow][newCol]--;

                        // If outDegree becomes 0, add to the queue
                        if (outDegree[newRow][newCol] == 0) {
                            queue.offer(new int[]{newRow, newCol});
                        }
                    }
                }
            }
        }

        return maxLength;
    }
}

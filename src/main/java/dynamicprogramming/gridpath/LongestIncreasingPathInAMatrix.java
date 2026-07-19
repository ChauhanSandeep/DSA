package dynamicprogramming.gridpath;

import java.util.*;

/**
 * Problem: Longest Increasing Path in a Matrix
 *
 * Given a matrix, return the maximum number of cells in a path where each next
 * cell is adjacent up, down, left, or right and has a strictly larger value.
 * Paths cannot leave the matrix or move diagonally.
 *
 * Leetcode: https://leetcode.com/problems/longest-increasing-path-in-a-matrix/ (Hard)
 * Rating:   acceptance 56.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | DFS memoization | DAG longest path
 *
 * Example:
 *   Input:  matrix = [[9,9,4],[6,6,8],[2,1,1]]
 *   Output: 4
 *   Why:    one longest increasing path is 1, 2, 6, 9.
 *
 * Follow-ups:
 *   1. Return the actual path?
 *      Store the best next cell for each memoized state and reconstruct from the best start.
 *   2. Can this be solved without recursion?
 *      Use topological BFS from local maxima or minima by cell outdegree or indegree.
 *   3. What if equal values may be used?
 *      The graph can contain cycles, so extra visited-state handling is required.
 *
 * Related: Number of Increasing Paths in a Grid (2328).
 */
public class LongestIncreasingPathInAMatrix {

    public static void main(String[] args) {
        LongestIncreasingPathInAMatrix solver = new LongestIncreasingPathInAMatrix();
        int[][][] cases = {
            {{9, 9, 4}, {6, 6, 8}, {2, 1, 1}},
            {{1}}
        };
        int[] expected = { 4, 1 };

        for (int i = 0; i < cases.length; i++) {
            int got = solver.longestIncreasingPath(cases[i]);
            System.out.printf("matrix=%s -> %d  expected=%d%n",
                Arrays.deepToString(cases[i]), got, expected[i]);
        }
    }
    // Directions: up, right, down, left
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        /**
     * Intuition: memo[i][j] means the longest strictly increasing path that
     * starts at matrix[i][j]. From that cell, a path may move only to neighbors
     * with larger values, so each candidate length is 1 plus that neighbor's
     * memoized answer. Taking the maximum over all valid neighbors gives the
     * best path from the current cell.
     *
     * Algorithm:
     *   1. Return 0 for a null or empty matrix.
     *   2. Run DFS from every cell, caching each cell's best starting path length in memo.
     *   3. Track and return the largest cached length seen over all starts.
     *
     * Time:  O(m * n) - each cell's DFS result is computed once and checks four neighbors.
     * Space: O(m * n) - memo stores one value per cell, plus recursion stack in the worst case.
     *
     * @param matrix grid of integer values
     * @return length of the longest strictly increasing path
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

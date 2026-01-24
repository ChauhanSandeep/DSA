package graphs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode: https://leetcode.com/problems/01-matrix/
 *
 * --- Problem Description ---
 * Given a binary matrix where:
 * - `0` represents an empty cell.
 * - `1` represents a filled cell.
 * Return a matrix where each `1` is replaced by the shortest distance to the nearest `0`.
 *
 * --- Approach ---
 * 1. **Use Multi-Source BFS**, starting from all `0`s and expanding outward.
 * 2. **Mark all `1`s as `Integer.MAX_VALUE` initially** to indicate unprocessed distances.
 * 3. **BFS propagates distances optimally** in O(N²) time.
 *
 * --- Complexity Analysis ---
 * - **Time Complexity:** O(N * M) → Each cell is processed at most once.
 * - **Space Complexity:** O(N * M) → Queue stores at most N * M elements.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ShortestDistance {

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Up, Down, Left, Right
    };

    public static void main(String[] args) {
        int[][] mat = {
                {0, 0, 0},
                {0, 1, 0},
                {1, 1, 1}
        };

        int[][] result = new ShortestDistance().updateMatrix(mat);
        for (int[] row : result) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     * Computes the shortest distance from each cell to the nearest '0' using Multi-Source BFS.
     *
     * @param matrix The input binary matrix.
     * @return The transformed matrix with distances.
     */
    public int[][] updateMatrix(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        Queue<int[]> queue = new LinkedList<>();

        // Step 1: Initialize queue with all '0's and mark '1's as unprocessed (max value)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    queue.offer(new int[]{i, j}); // Enqueue all '0' cells
                } else {
                    matrix[i][j] = Integer.MAX_VALUE; // Unprocessed '1's
                }
            }
        }

        // Step 2: Multi-Source BFS to calculate the shortest distance
        while (!queue.isEmpty()) {
            // SELECT
            int[] cell = queue.poll();
            int row = cell[0], col = cell[1];

            for (int[] dir : DIRECTIONS) {
                // READ
                int newRow = row + dir[0], newCol = col + dir[1];

                // If within bounds and found a shorter path to a '1' cell
                // condition `matrix[newRow][newCol] > matrix[row][col] + 1` avoids reprocessing so visited matrix is not required
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                        matrix[newRow][newCol] > matrix[row][col] + 1) {

                    matrix[newRow][newCol] = matrix[row][col] + 1;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }

        return matrix;
    }
}

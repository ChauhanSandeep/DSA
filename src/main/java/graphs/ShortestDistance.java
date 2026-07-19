package graphs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: 01 Matrix
 *
 * Given a binary matrix, replace every 1 with its distance to the nearest 0.
 * Distance is measured by 4-directional moves between neighboring cells.
 *
 * Leetcode: https://leetcode.com/problems/01-matrix/
 * Rating:   acceptance 54.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Multi-source BFS | Nearest zero distance
 *
 * Example:
 *   Input:  mat = [[0,0,0],[0,1,0],[1,1,1]]
 *   Output: [[0,0,0],[0,1,0],[1,2,1]]
 *   Why:    all zero cells start at distance 0, and each 1 receives the first BFS
 *           layer that reaches it from any zero.
 *
 * Follow-ups:
 *   1. Return distance to the nearest 1 instead?
 *      Seed BFS with all 1 cells and expand into 0 cells.
 *   2. Movement has different costs by direction?
 *      Replace BFS with Dijkstra because layers no longer mean equal distance.
 *   3. Avoid modifying the input matrix?
 *      Write distances into a separate matrix initialized with a large value.
 *
 * Related: Walls and Gates (286), Rotting Oranges (994), Shortest Bridge (934).
 */
public class ShortestDistance {

    public static void main(String[] args) {
        ShortestDistance solver = new ShortestDistance();
        int[][][] inputs = {{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}}, {{0}}};
        int[][][] expected = {{{0, 0, 0}, {0, 1, 0}, {1, 2, 1}}, {{0}}};
        for (int i = 0; i < inputs.length; i++) {
            String input = Arrays.deepToString(inputs[i]);
            int[][] output = solver.updateMatrix(inputs[i]);
            System.out.printf("matrix=%s -> %s  expected=%s%n", input, Arrays.deepToString(output), Arrays.deepToString(expected[i]));
        }
    }

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Up, Down, Left, Right
    };


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

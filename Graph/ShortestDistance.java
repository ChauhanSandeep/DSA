package Graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Leetcode 542: 01 Matrix
 * Given a binary matrix, find the shortest distance of each cell from the nearest 0.
 * Uses Multi-Source BFS for optimal performance.
 */
public class ShortestDistance {

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    public static void main(String[] args) {
        int[][] mat = {
                {0, 0, 0},
                {0, 1, 0},
                {1, 1, 1}
        };
        int[][] result = new ShortestDistance().updateMatrix(mat);
        System.out.println(Arrays.deepToString(result));
    }

    public int[][] updateMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        // Step 1: Add all '0' cells to the queue and mark them visited
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                    visited[i][j] = true;
                } else {
                    matrix[i][j] = Integer.MAX_VALUE - 1;  // Prevent integer overflow
                }
            }
        }

        // Step 2: Perform Multi-Source BFS
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0], col = cell[1];

            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !visited[newRow][newCol]) {
                    matrix[newRow][newCol] = matrix[row][col] + 1;
                    queue.offer(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                }
            }
        }
        return matrix;
    }
}

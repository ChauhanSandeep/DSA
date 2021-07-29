package Graph;

import java.util.*;

/**
 * From each cell of the binary matrix find the distance to the nearest 0.
 */
public class ShortestDistance {

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

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                } else {
                    matrix[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] direction : dirs) {
                int i = cell[0] + direction[0];
                int j = cell[1] + direction[1];
                if (i < 0 || i >= rows || j < 0 || j >= cols || matrix[i][j] <= matrix[cell[0]][cell[1]] + 1) {
                    continue;
                }

                queue.add(new int[]{i, j});
                matrix[i][j] = matrix[cell[0]][cell[1]] + 1;
            }
        }

        return matrix;
    }
}

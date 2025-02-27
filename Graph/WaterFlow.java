package Graph;

import java.util.*;

public class WaterFlow {

    public static void main(String[] args) {
        int[][] grid = {
                {1, 2, 2, 3, 5},
                {3, 2, 3, 4, 4},
                {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5},
                {5, 1, 1, 2, 4},
        };
        System.out.println(new WaterFlow().solve(grid)); // Output: 7
    }

    int rows, cols;
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public int solve(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        rows = grid.length;
        cols = grid[0].length;

        boolean[][] reachTopLeft = new boolean[rows][cols];
        boolean[][] reachBottomRight = new boolean[rows][cols];

        Queue<int[]> queueTopLeft = new LinkedList<>();
        Queue<int[]> queueBottomRight = new LinkedList<>();

        // Add boundary nodes to respective queues
        for (int i = 0; i < rows; i++) {
            queueTopLeft.add(new int[]{i, 0});
            queueBottomRight.add(new int[]{i, cols - 1});
            reachTopLeft[i][0] = true;
            reachBottomRight[i][cols - 1] = true;
        }
        for (int j = 0; j < cols; j++) {
            queueTopLeft.add(new int[]{0, j});
            queueBottomRight.add(new int[]{rows - 1, j});
            reachTopLeft[0][j] = true;
            reachBottomRight[rows - 1][j] = true;
        }

        // Run BFS for both top-left and bottom-right
        bfs(grid, queueTopLeft, reachTopLeft);
        bfs(grid, queueBottomRight, reachBottomRight);

        // Count common nodes where water can flow from both sources
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (reachTopLeft[i][j] && reachBottomRight[i][j]) count++;
            }
        }

        return count;
    }

    private void bfs(int[][] grid, Queue<int[]> queue, boolean[][] visited) {
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int i = cell[0], j = cell[1];

            for (int[] dir : directions) {
                int ni = i + dir[0], nj = j + dir[1];

                if (ni >= 0 && nj >= 0 && ni < rows && nj < cols && !visited[ni][nj] && grid[ni][nj] >= grid[i][j]) {
                    visited[ni][nj] = true;
                    queue.add(new int[]{ni, nj});
                }
            }
        }
    }
}

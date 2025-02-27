package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Shortest Bridge - Leetcode 934
 * Uses DFS + BFS (Multi-Source) to find the shortest bridge between two islands.
 * https://leetcode.com/problems/shortest-bridge/
 */
public class ShortestBridge {

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    public static void main(String[] args) {
        int[][] grid = {
                {0, 1},
                {1, 0}
        };
        int result = new ShortestBridge().shortestBridge(grid);
        System.out.println(result);
    }

    public int shortestBridge(int[][] grid) {
        int n = grid.length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[n][n];

        // Step 1: Find the first island and mark it in the queue
        boolean found = false;
        for (int i = 0; i < n; i++) {
            if (found) break;
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    dfs(grid, i, j, visited, queue);
                    found = true;
                    break;
                }
            }
        }

        // Step 2: Perform BFS to expand from the first island to find the shortest path to the second island
        int level = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                int row = curr[0], col = curr[1];

                for (int[] dir : DIRECTIONS) {
                    int newRow = row + dir[0], newCol = col + dir[1];

                    if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n && !visited[newRow][newCol]) {
                        if (grid[newRow][newCol] == 1) {
                            return level; // Found the shortest bridge!
                        }
                        queue.offer(new int[]{newRow, newCol});
                        visited[newRow][newCol] = true; // Mark as visited
                    }
                }
            }
            level++;
        }
        return -1; // Should never reach here
    }

    private void dfs(int[][] grid, int i, int j, boolean[][] visited, Queue<int[]> queue) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid.length || visited[i][j] || grid[i][j] == 0) {
            return;
        }
        visited[i][j] = true;
        queue.offer(new int[]{i, j}); // Add island border to BFS queue

        for (int[] dir : DIRECTIONS) {
            dfs(grid, i + dir[0], j + dir[1], visited, queue);
        }
    }
}

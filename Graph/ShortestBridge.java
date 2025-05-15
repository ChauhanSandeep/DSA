package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * **Shortest Bridge - Leetcode 934**
 * LeetCode: https://leetcode.com/problems/shortest-bridge/
 *
 * --- Problem Description ---
 * Given a binary grid where 1 represents land and 0 represents water, 
 * find the shortest bridge (minimum flips of 0 → 1) to connect the two islands.
 *
 * --- Approach ---
 * 1. **Find the first island using DFS**, marking all its positions and adding its boundary to a queue.
 * 2. **Use BFS to expand from the first island** and find the shortest path to the second island.
 *
 * --- Complexity Analysis ---
 * - **Time Complexity:** O(N²), where N is the grid size (DFS + BFS traversal).
 * - **Space Complexity:** O(N²), for the queue and visited matrix.
 */
public class ShortestBridge {

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Up, Down, Left, Right
    };

    public static void main(String[] args) {
        int[][] grid = {
                {0, 1},
                {1, 0}
        };
        int result = new ShortestBridge().shortestBridge(grid);
        System.out.println("Shortest Bridge: " + result);
    }

    /**
     * Finds the shortest bridge (minimum 0 → 1 flips) to connect two islands.
     *
     * @param grid The binary grid representing islands (1) and water (0).
     * @return The shortest number of flips required.
     */
    public int shortestBridge(int[][] grid) {
        int n = grid.length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[n][n];

        // Step 1: Find the first island and mark it in the queue
        boolean islandFound = false;
        for (int i = 0; i < n; i++) {
            if (islandFound) break;
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    markFirstIsland(grid, i, j, visited, queue);
                    islandFound = true;
                    break;
                }
            }
        }

        // Step 2: Perform BFS to find the shortest bridge
        return bfs(grid, queue, visited);
    }

    /**
     * Marks the first island using DFS and adds its boundary to the BFS queue.
     *
     * @param grid    The binary grid.
     * @param i       Row index.
     * @param j       Column index.
     * @param visited Visited matrix.
     * @param queue   BFS queue to store island boundary.
     */
    private void markFirstIsland(int[][] grid, int i, int j, boolean[][] visited, Queue<int[]> queue) {
        int n = grid.length;
        if (i < 0 || j < 0 || i >= n || j >= n || visited[i][j] || grid[i][j] == 0) {
            return;
        }

        visited[i][j] = true;
        queue.offer(new int[]{i, j}); // Add island border for BFS expansion

        for (int[] dir : DIRECTIONS) {
            markFirstIsland(grid, i + dir[0], j + dir[1], visited, queue);
        }
    }

    /**
     * Expands from the first island using BFS to reach the second island.
     *
     * @param grid    The binary grid.
     * @param queue   The BFS queue containing the first island's boundary.
     * @param visited Visited matrix.
     * @return The shortest number of flips required to connect the islands.
     */
    private int bfs(int[][] grid, Queue<int[]> queue, boolean[][] visited) {
        int level = 0;
        int n = grid.length;

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
            level++; // Increment BFS level (distance)
        }

        return -1; // Should never reach here
    }
}

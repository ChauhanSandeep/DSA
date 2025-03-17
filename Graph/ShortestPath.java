package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * **Leetcode 1091: Shortest Path in Binary Matrix**
 * https://leetcode.com/problems/shortest-path-in-binary-matrix/
 *
 * --- Problem Description ---
 * Given an `n x n` binary grid, where:
 * - `0` represents an empty cell.
 * - `1` represents an obstacle.
 * Find the shortest path from **top-left (0,0) to bottom-right (n-1, n-1)**.
 * You can move in **8 directions** (diagonal allowed).
 *
 * --- Approach ---
 * 1. **Use BFS** (Breadth-First Search) to find the shortest path.
 * 2. **Multi-directional movement** (8 directions).
 * 3. **Mark visited nodes by modifying `grid[][]` itself** (stores the distance).
 *
 * --- Complexity Analysis ---
 * - **Time Complexity:** O(N²) (Worst case: visiting all `N×N` cells).
 * - **Space Complexity:** O(N²) (Queue stores at most `N²` elements).
 */
public class ShortestPath {

    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0},
                {1, 0, 0},
                {1, 1, 0}
        };
        int shortestPath = new ShortestPath().shortestPathBinaryMatrix(grid);
        System.out.println("Shortest Path: " + shortestPath);
    }

    /**
     * Returns the shortest path from (0,0) to (n-1,n-1) in a binary grid.
     *
     * @param grid The input binary matrix.
     * @return The shortest path length, or -1 if no path exists.
     */
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;

        // Edge Case: If the start or end cell is blocked, return -1
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) return -1;

        // Edge Case: If the grid is a single cell [0], return 1
        if (n == 1) return 1;

        // BFS Initialization
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        grid[0][0] = 1; // Mark as visited with step count

        // BFS Traversal
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0], y = curr[1];
            int step = grid[x][y];

            // Explore all 8 possible directions
            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0], newY = y + dir[1];

                // If within bounds & unvisited cell (grid[newX][newY] == 0)
                if (newX >= 0 && newY >= 0 && newX < n && newY < n && grid[newX][newY] == 0) {
                    grid[newX][newY] = step + 1; // Mark visited with step count
                    queue.offer(new int[]{newX, newY});

                    // If we reached the bottom-right cell, return the step count
                    if (newX == n - 1 && newY == n - 1) return grid[newX][newY];
                }
            }
        }

        return -1; // No path found
    }
}

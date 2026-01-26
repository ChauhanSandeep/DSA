package graphs;

import java.util.*;

/**
 * 1162. As Far from Land as Possible
 *
 * Problem: Find the maximum distance from any water cell to the nearest land cell
 * in an n x n grid where 1 represents land and 0 represents water.
 *
 * Example:
 * Input: grid = [[1,0,1],[0,0,0],[1,0,1]]
 * Output: 2
 * Explanation: The cell (1,1) is as far as possible from all the land with distance 2.
 *
 * LeetCode: https://leetcode.com/problems/as-far-from-land-as-possible
 *
 * Follow-up questions:
 * Q: What if we need to find all cells with maximum distance?
 * A: During BFS, keep track of maximum distance and collect all cells with that distance.
 *
 * Q: How to optimize for memory when grid is very large?
 * A: Use in-place modification of grid to mark visited cells instead of separate visited array.
 *
 * Q: What if we have obstacles that block paths?
 * A: Modify BFS to skip obstacle cells, treating them like boundaries.
 * LeetCode Contest Rating: 1666
 */
public class AsFarFromLandAsPossible {

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Finds the maximum distance from any water cell to the nearest land cell.
     *
     * Algorithm: Multi-source BFS
     * - Start BFS from all land cells simultaneously
     * - Use queue to process cells level by level
     * - Each level represents increasing distance from land
     * - Return the maximum level reached, or -1 if no water cells
     *
     * Time Complexity: O(n^2) where n is the grid size
     * Space Complexity: O(n^2) for the queue in worst case
     */
    public int maxDistance(int[][] grid) {
        int n = grid.length;
        Queue<int[]> queue = new LinkedList<>();

        // Add all land cells to queue as starting points
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    queue.offer(new int[]{i, j});
                }
            }
        }

        // Edge cases: all land or all water
        if (queue.isEmpty() || queue.size() == n * n) {
            return -1;
        }

        int maxDist = -1;

        // Multi-source BFS
        while (!queue.isEmpty()) {
            int size = queue.size();
            maxDist++;

            // Process all cells at current distance level
            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int row = cell[0], col = cell[1];

                // Explore all 4 directions
                for (int[] dir : DIRECTIONS) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];

                    // Check bounds and if it's water that hasn't been visited
                    if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n
                        && grid[newRow][newCol] == 0) {
                        grid[newRow][newCol] = 1; // mark as visited
                        queue.offer(new int[]{newRow, newCol});
                    }
                }
            }
        }

        return maxDist;
    }

    /**
     * Alternative approach without modifying input grid.
     * Uses separate visited array to track processed cells.
     */
    public int maxDistanceWithoutModification(int[][] grid) {
        int n = grid.length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[n][n];

        // Add all land cells and mark them as visited
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    queue.offer(new int[]{i, j});
                    visited[i][j] = true;
                }
            }
        }

        if (queue.isEmpty() || queue.size() == n * n) {
            return -1;
        }

        int distance = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean foundWater = false;

            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int row = cell[0], col = cell[1];

                for (int[] dir : DIRECTIONS) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];

                    if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n
                        && !visited[newRow][newCol] && grid[newRow][newCol] == 0) {
                        visited[newRow][newCol] = true;
                        queue.offer(new int[]{newRow, newCol});
                        foundWater = true;
                    }
                }
            }

            if (foundWater) {
                distance++;
            }
        }

        return distance;
    }

    /**
     * Space-optimized approach using the grid itself to store distances.
     * Useful when we need to know the actual distance to land for each cell.
     */
    public int maxDistanceWithDistanceTracking(int[][] grid) {
        int n = grid.length;
        Queue<int[]> queue = new LinkedList<>();

        // Initialize: land = 0, water = -1
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    grid[i][j] = 0; // land has distance 0
                    queue.offer(new int[]{i, j});
                } else {
                    grid[i][j] = -1; // water marked as unvisited
                }
            }
        }

        if (queue.isEmpty() || queue.size() == n * n) {
            return -1;
        }

        int maxDist = 0;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0], col = cell[1];

            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n
                    && grid[newRow][newCol] == -1) {
                    grid[newRow][newCol] = grid[row][col] + 1;
                    maxDist = Math.max(maxDist, grid[newRow][newCol]);
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }

        return maxDist;
    }
}
package graphs;

import java.util.*;

/**
 * Problem: As Far from Land as Possible
 *
 * Given an n by n grid of land cells (1) and water cells (0), choose a water
 * cell whose distance to the nearest land cell is as large as possible. Return
 * that distance, or -1 when the grid has no useful land/water mix.
 *
 * Leetcode: https://leetcode.com/problems/as-far-from-land-as-possible/ (Medium)
 * Rating:   1666 (zerotrac Elo)
 * Pattern:  Graph | Multi-source BFS | Grid shortest distance
 *
 * Example:
 *   Input:  grid = [[1,0,1],[0,0,0],[1,0,1]]
 *   Output: 2
 *   Why:    the center water cell is two steps from every corner land cell, and
 *           every other water cell is closer to at least one land cell.
 *
 * Follow-ups:
 *   1. Return all farthest water cells, not just the distance?
 *      Keep the cells from the last BFS level and return them with the final distance.
 *   2. What if obstacles block movement?
 *      Treat obstacles as non-walkable cells and do not enqueue them during BFS.
 *   3. What if the grid is very large and sparse?
 *      Store only land and discovered water in hash sets instead of a dense visited grid.
 *
 * Related: 01 Matrix (542), Rotting Oranges (994), Walls and Gates (286).
 */
public class AsFarFromLandAsPossible {


    public static void main(String[] args) {
        AsFarFromLandAsPossible solver = new AsFarFromLandAsPossible();
        int[][][] inputs = {
            {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}},
            {{1, 1}, {1, 1}}
        };
        int[] expected = {2, -1};

        for (int i = 0; i < inputs.length; i++) {
            int[][] grid = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            int output = solver.maxDistance(grid);
            System.out.printf("grid=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    /**
     * Intuition: every land cell is a source with distance 0. Multi-source BFS grows
     * all islands outward one layer at a time, so the first time a water cell is
     * reached is by its nearest land. The last water layer reached is therefore as
     * far from land as possible.
     *
     * Algorithm:
     *   1. Enqueue every land cell and count how many land cells exist.
     *   2. Return -1 if the grid is all water or all land.
     *   3. Run level-order BFS using the original queue and four directions.
     *   4. Mark reached water as land and return the final BFS level distance.
     *
     * Time:  O(n^2) - each grid cell is enqueued at most once.
     * Space: O(n^2) - the queue can hold a full BFS layer in the worst case.
     *
     * @param grid square grid with 1 for land and 0 for water
     * @return maximum distance from any water cell to nearest land, or -1 if none exists
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

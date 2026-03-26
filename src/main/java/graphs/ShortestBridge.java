package graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Shortest Bridge - Leetcode 934 (Extended to N islands)
 * Problem Link: https://leetcode.com/problems/shortest-bridge/
 *
 * --- Problem Statement ---
 * You are given an n x n binary matrix grid where 1 represents land and 0 represents water.
 * There are one or more islands (groups of connected 1s). You may flip 0s to 1s to connect islands.
 * Return the smallest number of 0s that must be flipped to connect any two islands.
 *
 * --- Example ---
 * Input: grid = [[0,1],[1,0]]
 * Output: 1
 * Explanation: We can flip one 0 to connect the two islands.
 *
 * --- Follow-Up Questions ---
 * Q: How does the algorithm change for more than 2 islands?
 * A: Label each island with a unique ID (2, 3, 4, ...) in-place using DFS.
 *    Then run BFS separately from each island's boundary. BFS stops when it
 *    reaches any cell belonging to a *different* island. Return the global minimum.
 *
 * Q: Can the islands be diagonally connected?
 * A: No. Only 4-directionally connected land forms an island.
 */
public class ShortestBridge {

    private static final int[][] DIRECTION_OFFSETS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Up, Down, Left, Right
    };

    public static void main(String[] args) {
        // Two islands
        int[][] grid1 = {
            {0, 1},
            {1, 0}
        };
        System.out.println("Shortest Bridge (2 islands): " + new ShortestBridge().findShortestBridge(grid1)); // 1

        // Three islands
        int[][] grid2 = {
            {1, 0, 0, 0, 1},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0}
        };
        System.out.println("Shortest Bridge (3 islands): " + new ShortestBridge().findShortestBridge(grid2)); // 2
    }

    /**
     * Finds the minimum number of 0 → 1 flips required to connect any two islands.
     *
     * Steps:
     * 1. Use DFS to locate and label ALL islands with unique IDs (2, 3, 4, ...) in-place.
     *    The grid itself acts as the visited tracker — no separate visited array needed.
     * 2. For each island, run BFS outward from its boundary.
     *    BFS stops as soon as it reaches a cell belonging to a different island.
     * 3. Return the minimum distance across all islands.
     *
     * Algorithm: DFS + BFS
     * Time Complexity: O(K * N^2) where K = number of islands
     * - Because in the worst case, BFS from each island could explore the entire grid until it finds another island.
     * Space Complexity: O(N^2) in the worst case for the visited array in BFS, but typically much less due to early stopping.
     *
     * @param grid 2D binary grid of islands and water.
     * @return Minimum number of water cells to flip.
     */
    public int findShortestBridge(int[][] grid) {
        int length = grid.length;

        // Each entry holds the boundary cells of one island
        List<Queue<int[]>> islandBoundaries = new ArrayList<>();

        // Step 1: Label all islands with unique IDs (starting from 2) using DFS
        int islandId = 2;
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                if (grid[row][col] == 1) {
                    Queue<int[]> boundary = new LinkedList<>();
                    dfsMarkIsland(grid, row, col, islandId, boundary);
                    islandBoundaries.add(boundary);
                    islandId++;
                }
            }
        }

        // Step 2: BFS from each island; find the minimum bridge to any other island
        int minBridge = Integer.MAX_VALUE;
        for (int id = 2; id < islandId; id++) {
            Queue<int[]> boundary = islandBoundaries.get(id - 2);
            int dist = bfsExpandToNearestIsland(grid, boundary, id);
            minBridge = Math.min(minBridge, dist);
        }

        return minBridge;
    }

    /**
     * Performs DFS to label all cells of an island with the given ID.
     * Only adds cells that are true boundary cells (adjacent to at least one water cell or grid edge)
     * to the queue, since interior cells surrounded by island cells contribute nothing to BFS.
     *
     * The grid value itself tracks visited state: a cell with value == islandId is already processed.
     *
     * @param grid      The binary matrix (modified in-place with island IDs).
     * @param row       Current row index.
     * @param col       Current column index.
     * @param islandId  Unique ID to stamp on this island's cells (>= 2).
     * @param boundary  Queue to collect only the island's boundary cells for BFS.
     */
    private void dfsMarkIsland(int[][] grid, int row, int col, int islandId, Queue<int[]> boundary) {
        int length = grid.length;

        // Base case: out of bounds, already labeled, or water
        if (row < 0 || col < 0 || row >= length || col >= length || grid[row][col] != 1) {
            return;
        }

        grid[row][col] = islandId; // Label in-place — doubles as visited marker

        // Only enqueue cells adjacent to water (true boundary cells); interior cells are useless BFS sources
        boolean isBoundaryCell = false;
        for (int[] dir : DIRECTION_OFFSETS) {
            int nr = row + dir[0], nc = col + dir[1];
            if (nr < 0 || nc < 0 || nr >= length || nc >= length || grid[nr][nc] == 0) {
                isBoundaryCell = true;
                break;
            }
        }
        if (isBoundaryCell) boundary.offer(new int[]{row, col});

        for (int[] direction : DIRECTION_OFFSETS) {
            dfsMarkIsland(grid, row + direction[0], col + direction[1], islandId, boundary);
        }
    }

    /**
     * Performs BFS from the given island's boundary to find the shortest distance to any other island.
     *
     * Uses a local visited array so BFS runs for each island are independent.
     * Stops as soon as a cell with a different island ID (> 0 and != currentIslandId) is reached.
     *
     * @param grid          The matrix with island IDs stamped.
     * @param queue         Boundary cells of the current island (BFS source).
     * @param currentIsland ID of the island being expanded from.
     * @return              Minimum water cells to cross to reach the nearest other island.
     */
    private int bfsExpandToNearestIsland(int[][] grid, Queue<int[]> queue, int currentIsland) {
        int length = grid.length;
        boolean[][] visited = new boolean[length][length];
        int flips = 0;

        // Copy the queue so the original boundary list stays reusable
        Queue<int[]> bfsQueue = new LinkedList<>(queue);
        for (int[] cell : bfsQueue) {
            visited[cell[0]][cell[1]] = true;
        }

        while (!bfsQueue.isEmpty()) {
            int layerSize = bfsQueue.size();

            for (int i = 0; i < layerSize; i++) {
                int[] cell = bfsQueue.poll();
                int row = cell[0];
                int col = cell[1];

                for (int[] direction : DIRECTION_OFFSETS) {
                    int newRow = row + direction[0];
                    int newCol = col + direction[1];

                    if (newRow < 0 || newCol < 0 || newRow >= length || newCol >= length || visited[newRow][newCol]) {
                        continue;
                    }

                    // Skip cells belonging to the same island (interior cells not in queue)
                    if (grid[newRow][newCol] == currentIsland) {
                        visited[newRow][newCol] = true;
                        continue;
                    }

                    // Reached a different island
                    if (grid[newRow][newCol] != 0) {
                        return flips;
                    }

                    // Expand into water
                    bfsQueue.offer(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                }
            }

            flips++;
        }

        return Integer.MAX_VALUE; // No other island reachable (shouldn't happen on a valid grid)
    }
}

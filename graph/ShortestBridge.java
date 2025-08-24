package graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Shortest Bridge - Leetcode 934
 * Problem Link: https://leetcode.com/problems/shortest-bridge/
 *
 * --- Problem Statement ---
 * You are given an n x n binary matrix grid where 1 represents land and 0 represents water.
 * There are exactly two islands (groups of connected 1s). You may flip 0s to 1s to connect the two islands.
 * Return the smallest number of 0s that must be flipped to connect the two islands.
 *
 * --- Example ---
 * Input: grid = [[0,1],[1,0]]
 * Output: 1
 * Explanation: We can flip one 0 to connect the two islands.
 *
 * --- Follow-Up Questions ---
 * Q: Can we reduce space usage by modifying the grid in-place?
 * A: Yes. Instead of using a visited matrix, we can use a special marker in grid to track visited cells.
 *
 * Q: Can the islands be diagonally connected?
 * A: No. Only 4-directionally connected land forms an island.
 *
 * --- Tags ---
 * Graph, DFS, BFS, Matrix, Flood Fill
 */
public class ShortestBridge {

    private static final int[][] DIRECTION_OFFSETS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Up, Down, Left, Right
    };

    public static void main(String[] args) {
        int[][] grid = {
            {0, 1},
            {1, 0}
        };
        int minFlips = new ShortestBridge().findShortestBridge(grid);
        System.out.println("Shortest Bridge: " + minFlips);
    }

    /**
     * Finds the minimum number of 0 → 1 flips required to connect two separate islands.
     *
     * Steps:
     * 1. Use DFS to locate and mark the first island, storing its boundary coordinates.
     * 2. Use BFS to expand outward from the first island until the second island is found.
     *
     * Algorithm: DFS + BFS
     * Time Complexity: O(N^2)
     * Space Complexity: O(N^2)
     *
     * @param grid 2D binary grid of islands and water.
     * @return Minimum number of water cells to flip.
     */
    public int findShortestBridge(int[][] grid) {
        int length = grid.length;
        boolean[][] visited = new boolean[length][length];
        Queue<int[]> islandBoundary = new LinkedList<>();

        // Step 1: Find and mark first island using DFS
        boolean isFirstIslandLocated = false;
        for (int row = 0; row < length; row++) {
            if (isFirstIslandLocated) break;
            for (int col = 0; col < length; col++) {
                if (grid[row][col] == 1) {
                    dfsMarkIsland(grid, visited, row, col, islandBoundary);
                    isFirstIslandLocated = true;
                    break;
                }
            }
        }

        // Step 2: Expand using BFS to reach second island
        return bfsExpandToSecondIsland(grid, visited, islandBoundary);
    }

    /**
     * Performs DFS to mark all land cells of the first island and adds their coordinates to the queue.
     *
     * @param grid      The binary matrix.
     * @param visited   Tracks visited cells.
     * @param row       Current row index.
     * @param col       Current column index.
     * @param boundary  Queue to collect the first island's boundary for BFS.
     */
    private void dfsMarkIsland(int[][] grid, boolean[][] visited, int row, int col, Queue<int[]> boundary) {
        int length = grid.length;

        // Base case: Check boundaries and if the cell is already visited or not land
        if (row < 0 || col < 0 || row >= length || col >= length || visited[row][col] || grid[row][col] != 1) {
            return;
        }

        visited[row][col] = true;
        boundary.offer(new int[]{row, col}); // Add for BFS expansion

        for (int[] direction : DIRECTION_OFFSETS) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];
            dfsMarkIsland(grid, visited, nextRow, nextCol, boundary);
        }
    }

    /**
     * Performs BFS from the first island's boundary to find the shortest path to the second island.
     *
     * @param grid      The binary matrix.
     * @param visited   Tracks visited cells.
     * @param queue     Queue initialized with the first island's boundary.
     * @return          Minimum flips (BFS level) to connect the two islands.
     */
    private int bfsExpandToSecondIsland(int[][] grid, boolean[][] visited, Queue<int[]> queue) {
        int length = grid.length;
        int flips = 0;

        while (!queue.isEmpty()) {
            int layerSize = queue.size();

            for (int i = 0; i < layerSize; i++) {
                int[] cell = queue.poll();
                int row = cell[0];
                int col = cell[1];

                for (int[] direction : DIRECTION_OFFSETS) {
                    int newRow = row + direction[0];
                    int newCol = col + direction[1];

                    // Skip invalid or already visited cells
                    if (newRow < 0 || newCol < 0 || newRow >= length || newCol >= length || visited[newRow][newCol]) {
                        continue;
                    }

                    // Found the second island
                    if (grid[newRow][newCol] == 1) {
                        return flips;
                    }

                    // Expand to water cell
                    queue.offer(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                }
            }

            flips++;
        }

        return -1; // This should never happen given the problem guarantees
    }
}
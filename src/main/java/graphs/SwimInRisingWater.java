package graphs;

import java.util.PriorityQueue;
import java.util.Arrays;

/**
 * Problem: Swim in Rising Water
 *
 * Each grid value is an elevation. At time t, water level t lets you enter any
 * cell with elevation at most t. Return the earliest time you can move from the
 * top-left cell to the bottom-right cell.
 *
 * Leetcode: https://leetcode.com/problems/swim-in-rising-water/
 * Rating:   2097 (zerotrac Elo)
 * Pattern:  Graph | Dijkstra/minimax path | Priority queue
 *
 * Example:
 *   Input:  grid = [[0,2],[1,3]]
 *   Output: 3
 *   Why:    the destination elevation is 3, so no path can arrive before time 3,
 *           and at time 3 all four cells are reachable.
 *
 * Follow-ups:
 *   1. Solve with binary search instead?
 *      Binary search time t and flood-fill cells with elevation at most t.
 *   2. Return the path that achieves the time?
 *      Store parent pointers when pushing cells into the priority queue.
 *   3. Many start/end queries on the same grid?
 *      Build a minimum spanning tree and answer minimax path queries on it.
 *
 * Related: Path With Minimum Effort (1631), Reachable Nodes in Subdivided Graph (882).
 */
public class SwimInRisingWater {

    public static void main(String[] args) {
        SwimInRisingWater solver = new SwimInRisingWater();
        int[][][] grids = {{{0, 2}, {1, 3}}, {{0}}};
        int[] expected = {3, 0};
        for (int i = 0; i < grids.length; i++) {
            int output = solver.swimInWater(grids[i]);
            System.out.printf("grid=%s -> %d  expected=%d%n", Arrays.deepToString(grids[i]), output, expected[i]);
        }
    }
    // Directions for 4-directional movement
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public int swimInWater(int[][] grid) {
        int n = grid.length;
        // Min-heap to always explore the cell with the smallest elevation next
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        boolean[][] visited = new boolean[n][n];

        // Start from the top-left corner (0, 0)
        minHeap.offer(new int[]{grid[0][0], 0, 0});
        visited[0][0] = true;

        int maxElevation = 0;

        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int elevation = current[0];
            int i = current[1];
            int j = current[2];

            // Update the maximum elevation encountered so far
            maxElevation = Math.max(maxElevation, elevation);

            // If we've reached the bottom-right corner, return the result
            if (i == n - 1 && j == n - 1) {
                return maxElevation;
            }

            // Explore all 4-directional neighbors
            for (int[] dir : DIRECTIONS) {
                int ni = i + dir[0];
                int nj = j + dir[1];

                // Check if the neighbor is within bounds and not visited
                if (ni >= 0 && ni < n && nj >= 0 && nj < n && !visited[ni][nj]) {
                    visited[ni][nj] = true;
                    // The elevation of the path is the maximum elevation encountered so far
                    minHeap.offer(new int[]{grid[ni][nj], ni, nj});
                }
            }
        }

        return maxElevation;
    }
}

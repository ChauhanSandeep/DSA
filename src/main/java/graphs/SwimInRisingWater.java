package graphs;

import java.util.PriorityQueue;
import java.util.Arrays;

/**
 * Problem: Swim in Rising Water
 *
 * You are given an n x n grid where grid[i][j] is the elevation of that cell.
 * It starts raining: at time t, every cell whose elevation is at most t is
 * underwater. Starting at the top-left cell, you want to reach the bottom-right
 * cell by moving 4-directionally. You may swim any distance in zero time, but
 * you can only pass through cells that are already underwater (elevation <= t).
 * Return the least time t at which the bottom-right cell becomes reachable.
 *
 * Leetcode: https://leetcode.com/problems/swim-in-rising-water/   (Hard)
 * Rating:   2097 (zerotrac Elo)
 * Pattern:  Graph | Dijkstra / minimax path | Priority queue
 *
 * Example:
 *   Input:
 *     grid = [[ 0,  1,  2,  3,  4],
 *             [24, 23, 22, 21,  5],
 *             [12, 13, 14, 15, 16],
 *             [11, 17, 18, 19, 20],
 *             [10,  9,  8,  7,  6]]
 *   Output: 16
 *   Why:    the exit cell is only elevation 6, but the start is fenced in by
 *           high cells (the 21-24 wall and the 12-16 band). Every route out
 *           must cross that fence, and the lowest crossing peaks at 16 -- so no
 *           path opens until the water rises to 16, even though start and exit
 *           are both low.
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
        int[][][] grids = {
            {
                {0,  1,  2,  3,  4},
                {24, 23, 22, 21, 5},
                {12, 13, 14, 15, 16},
                {11, 17, 18, 19, 20},
                {10, 9,  8,  7,  6}
            },
            {
                {0, 2},
                {1, 3}
            },
            {
                {0}
            }
        };
        int[] expected = {16, 3, 0};
        for (int i = 0; i < grids.length; i++) {
            int output = solver.swimInWater(grids[i]);
            System.out.printf("grid=%s -> %d  expected=%d%n", Arrays.deepToString(grids[i]), output, expected[i]);
        }
    }
    // Directions for 4-directional movement
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public int swimInWater(int[][] grid) {
        int size = grid.length;
        // Min-heap to always explore the cell with the smallest elevation next
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        boolean[][] visited = new boolean[size][size];

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
            if (i == size - 1 && j == size - 1) {
                return maxElevation;
            }

            // Explore all 4-directional neighbors
            for (int[] dir : DIRECTIONS) {
                int ni = i + dir[0];
                int nj = j + dir[1];

                // Check if the neighbor is within bounds and not visited
                if (ni >= 0 && ni < size && nj >= 0 && nj < size && !visited[ni][nj]) {
                    visited[ni][nj] = true;
                    // The elevation of the path is the maximum elevation encountered so far
                    minHeap.offer(new int[]{grid[ni][nj], ni, nj});
                }
            }
        }

        return maxElevation;
    }
}

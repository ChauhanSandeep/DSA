package arrays.twopointers;

import java.util.*;

/**
 * Problem: Trapping Rain Water II
 *
 * Given an m x n integer matrix heightMap representing the height of each unit cell in a 2D elevation map,
 * return the volume of water it can trap after raining.
 *
 * Example:
 * Input: heightMap = [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]
 * Output: 4
 * Explanation: After the rain, water is trapped between the blocks.
 * We have two small ponds (blue sections) and one large pond (red section).
 * The total volume of water trapped is 4.
 *
 * LeetCode: https://leetcode.com/problems/trapping-rain-water-ii
 *
 * Time Complexity: O(m * n * log(m + n)) where m is the number of rows and n is the number of columns
 * Space Complexity: O(m * n) for the priority queue and visited matrix
 */
public class TrappingRainWaterII {
    // Directions for 4-directional movement
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public int trapRainWater(int[][] heightMap) {
        if (heightMap == null || heightMap.length <= 2 || heightMap[0].length <= 2) {
            return 0;
        }

        int m = heightMap.length;
        int n = heightMap[0].length;
        boolean[][] visited = new boolean[m][n];

        // Min-heap to always process the cell with the smallest height
        PriorityQueue<Cell> minHeap = new PriorityQueue<>((a, b) -> a.height - b.height);

        // Add all boundary cells to the heap and mark them as visited
        for (int i = 0; i < m; i++) {
            minHeap.offer(new Cell(i, 0, heightMap[i][0]));
            minHeap.offer(new Cell(i, n - 1, heightMap[i][n - 1]));
            visited[i][0] = true;
            visited[i][n - 1] = true;
        }

        for (int j = 1; j < n - 1; j++) {
            minHeap.offer(new Cell(0, j, heightMap[0][j]));
            minHeap.offer(new Cell(m - 1, j, heightMap[m - 1][j]));
            visited[0][j] = true;
            visited[m - 1][j] = true;
        }

        int water = 0;
        int maxHeight = Integer.MIN_VALUE;

        while (!minHeap.isEmpty()) {
            Cell cell = minHeap.poll();
            maxHeight = Math.max(maxHeight, cell.height);

            // Explore all 4-directional neighbors
            for (int[] dir : DIRECTIONS) {
                int ni = cell.i + dir[0];
                int nj = cell.j + dir[1];

                // Check if the neighbor is within bounds and not visited
                if (ni >= 0 && ni < m && nj >= 0 && nj < n && !visited[ni][nj]) {
                    visited[ni][nj] = true;

                    // If the neighbor's height is less than the current max height, it can trap water
                    if (heightMap[ni][nj] < maxHeight) {
                        water += maxHeight - heightMap[ni][nj];
                    }

                    // Add the neighbor to the heap with its height
                    minHeap.offer(new Cell(ni, nj, heightMap[ni][nj]));
                }
            }
        }

        return water;
    }

    // Helper class to store cell information
    private static class Cell {
        int i, j, height;

        Cell(int i, int j, int height) {
            this.i = i;
            this.j = j;
            this.height = height;
        }
    }
}

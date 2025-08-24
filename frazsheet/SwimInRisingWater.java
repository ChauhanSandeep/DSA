package frazsheet;

import java.util.PriorityQueue;

/**
 * Problem: Swim in Rising Water
 * 
 * You are given an n x n integer matrix grid where each value grid[i][j] represents the elevation at that point (i, j).
 * The rain starts to fall. At time t, the depth of the water everywhere is t. You can swim from a square to another 4-directionally adjacent square if and only if the elevation of both squares individually are at most t.
 * 
 * Return the least time until you can reach the bottom right square (n-1, n-1) if you start at the top left square (0, 0).
 * 
 * Example:
 * Input: grid = [[0,2],[1,3]]
 * Output: 3
 * Explanation: 
 * At time 0, you are in grid location (0, 0).
 * You cannot go anywhere else because 4-directionally adjacent neighbors have a higher elevation than t = 0.
 * You cannot reach point (1, 1) until time 3.
 * When the depth of water is 3, we can swim anywhere inside the grid.
 * 
 * LeetCode: https://leetcode.com/problems/swim-in-rising-water
 * 
 * Time Complexity: O(n^2 * log n) where n is the size of the grid
 * Space Complexity: O(n^2) for the priority queue and visited set
 */
public class SwimInRisingWater {
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

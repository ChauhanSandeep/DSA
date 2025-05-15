package Graph;

import java.util.*;

/**
 * LeetCode 827: Making A Large Island
 * Problem Link: https://leetcode.com/problems/making-a-large-island/
 *
 * Given an n x n binary grid, where 1 represents land and 0 represents water,
 * we can flip exactly one 0 to a 1. The goal is to return the size of the largest
 * possible island after making this flip.
 *
 * Approach:
 * 1. First, perform a DFS to label each island with a unique ID and compute their sizes.
 * 2. Then, iterate over all water cells (0s) and check the potential island size if flipped.
 * 3. Keep track of the maximum island size found.
 *
 * Time Complexity: O(N^2) (DFS for labeling + Checking potential flips)
 * Space Complexity: O(N^2) (For storing island sizes and visited nodes)
 */
public class MakeLargeIsland {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 1, 1},
                {0, 1, 0},
                {0, 0, 1}
        };
        int largestIsland = new MakeLargeIsland().largestIsland(grid);
        System.out.println("Largest island after flipping one 0 is " + largestIsland);
    }

    public int largestIsland(int[][] grid) {
        int n = grid.length;
        int islandId = 2;  // Start from 2 to differentiate from 1
        int maxIslandSize = 0;
        Map<Integer, Integer> islandSizeMap = new HashMap<>();
        
        // Step 1: Identify and label islands while storing their sizes
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] == 1) { 
                    int islandSize = performDFS(grid, row, col, islandId);
                    islandSizeMap.put(islandId, islandSize);
                    maxIslandSize = Math.max(maxIslandSize, islandSize);
                    islandId++;
                }
            }
        }

        // Step 2: Try flipping each '0' and compute the largest island possible
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] == 0) {
                    maxIslandSize = Math.max(maxIslandSize, computePotentialIslandSize(grid, row, col, islandSizeMap));
                }
            }
        }

        return maxIslandSize;
    }

    /**
     * Performs DFS to label an island with a unique ID and compute its size.
     * @param grid - The input grid.
     * @param row - Current row position.
     * @param col - Current column position.
     * @param islandId - Unique ID assigned to the island.
     * @return The size of the island.
     */
    private int performDFS(int[][] grid, int row, int col, int islandId) {
        int n = grid.length;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{row, col});
        grid[row][col] = islandId; // Label island
        int islandSize = 0;

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int x = cell[0], y = cell[1];
            islandSize++;

            for (int[] dir : directions) {
                int newX = x + dir[0], newY = y + dir[1];
                if (newX >= 0 && newX < n && newY >= 0 && newY < n && grid[newX][newY] == 1) {
                    grid[newX][newY] = islandId;
                    stack.push(new int[]{newX, newY});
                }
            }
        }
        return islandSize;
    }

    /**
     * Computes the potential island size if a 0 is flipped to a 1.
     * @param grid - The input grid.
     * @param row - Row index of the water cell.
     * @param col - Column index of the water cell.
     * @param islandSizeMap - Map containing sizes of identified islands.
     * @return The size of the newly formed island.
     */
    private int computePotentialIslandSize(int[][] grid, int row, int col, Map<Integer, Integer> islandSizeMap) {
        int n = grid.length;
        Set<Integer> neighboringIslands = new HashSet<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // Identify unique neighboring island IDs
        for (int[] dir : directions) {
            int newX = row + dir[0], newY = col + dir[1];
            if (newX >= 0 && newX < n && newY >= 0 && newY < n && grid[newX][newY] > 1) {
                neighboringIslands.add(grid[newX][newY]);
            }
        }

        // Compute new island size by merging connected islands
        int newIslandSize = 1; // 1 for the flipped cell itself
        for (int islandId : neighboringIslands) {
            newIslandSize += islandSizeMap.getOrDefault(islandId, 0);
        }

        return newIslandSize;
    }
}

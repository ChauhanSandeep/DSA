package graph;

import java.util.Stack;

/**
 * LeetCode 695: Max Area of Island
 * Problem Link: https://leetcode.com/problems/max-area-of-island/
 *
 * Given a binary grid where 1 represents land and 0 represents water,
 * the task is to find the maximum area of an island. An island consists
 * of connected 1s in the horizontal or vertical direction.
 *
 * Approach:
 * 1. Use Depth-First Search (DFS) to explore each island.
 * 2. Mark visited cells to avoid reprocessing.
 * 3. Compute the area of each island and track the maximum found.
 *
 * Time Complexity: O(M × N) → Visiting each cell once
 * Space Complexity: O(M × N) → For visited array in worst case (all land)
 */
public class MaxAreaIsland {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // Down, Up, Right, Left

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 1, 1, 0},
            {1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1}
        };
        System.out.println("Max area of island is " + maxAreaOfIsland(grid));
    }

    public static int maxAreaOfIsland(int[][] grid) {
        if (grid == null || grid.length == 0) return 0; // Edge case: Empty grid

        int maxIslandSize = 0;
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        // Iterate over each cell in the grid
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 1 && !visited[row][col]) {
                    maxIslandSize = Math.max(maxIslandSize, performDFS(grid, visited, row, col));
                }
            }
        }
        return maxIslandSize;
    }

    /**
     * Performs DFS to compute the size of an island using an iterative approach.
     * @param grid - The input grid
     * @param visited - Boolean array to track visited cells
     * @param startRow - Row index of the current land cell
     * @param startCol - Column index of the current land cell
     * @return The area of the island
     */
    private static int performDFS(int[][] grid, boolean[][] visited, int startRow, int startCol) {
        int n = grid.length, m = grid[0].length;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        int islandSize = 0;

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int row = cell[0], col = cell[1];
            islandSize++;

            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0], newCol = col + dir[1];

                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < m &&
                    grid[newRow][newCol] == 1 && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    stack.push(new int[]{newRow, newCol});
                }
            }
        }
        return islandSize;
    }
}

package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Leetcode 1091: Shortest Path in Binary Matrix
 * Uses BFS for optimal shortest path search with 8-direction movement.
 */
public class ShortestPath {

    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0},
                {1, 0, 0},
                {1, 1, 0}
        };
        int shortestPath = new ShortestPath().shortestPathBinaryMatrix(grid);
        System.out.println(shortestPath);
    }

    public int shortestPathBinaryMatrix(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;

        // If start or end cell is blocked, return -1 immediately
        if (grid[0][0] == 1 || grid[rows - 1][cols - 1] == 1) return -1;

        // BFS initialization
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        grid[0][0] = 1; // Mark as visited

        // BFS traversal
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0], y = curr[1];
            int step = grid[x][y];

            // If we reach the bottom-right cell, return the step count
            if (x == rows - 1 && y == cols - 1) return step;

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0], newY = y + dir[1];

                // Valid cell check
                if (newX >= 0 && newY >= 0 && newX < rows && newY < cols && grid[newX][newY] == 0) {
                    queue.offer(new int[]{newX, newY});
                    grid[newX][newY] = step + 1; // Mark visited & store distance
                }
            }
        }

        return -1; // No path found
    }
}

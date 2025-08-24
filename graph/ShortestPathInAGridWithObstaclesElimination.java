package graph;

import java.util.*;

/**
 * Problem: Shortest Path in a Grid with Obstacles Elimination
 *
 * You are given an m x n integer matrix grid where each cell is either 0 (empty) or 1 (obstacle).
 * You can move up, down, left, or right from and to an empty cell. Return the minimum number of steps
 * to walk from the upper left corner (0, 0) to the lower right corner (m - 1, n - 1) given that
 * you can eliminate at most k obstacles. If it is not possible to find such walk, return -1.
 *
 * Example:
 * Input: grid = [[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]], k = 1
 * Output: 6
 * Explanation: The shortest path without eliminating any obstacle is 10.
 * The shortest path with one obstacle elimination at position (3,2) is 6.
 *
 * LeetCode: https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination
 *
 * Follow-up Questions:
 * 1. What if we need to find all possible shortest paths?
 *    Answer: Modify BFS to track all parent nodes and reconstruct all paths.
 *
 * 2. How would you handle negative weights (rewards for eliminating obstacles)?
 *    Answer: Use Dijkstra's algorithm instead of BFS for weighted shortest path.
 *
 * 3. What if obstacles have different elimination costs?
 *    Answer: Extend state to track remaining budget and use priority queue.
 *    Related: https://leetcode.com/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
 *
 * @author Sandeep
 */
public class ShortestPathInAGridWithObstaclesElimination {

    /**
     * Finds shortest path using BFS with state tracking for obstacle eliminations.
     *
     * Algorithm:
     * 1. Use BFS with state (row, col, obstacles_eliminated)
     * 2. Track visited states to avoid revisiting same configuration
     * 3. For each cell, try moving in all 4 directions
     * 4. If next cell is obstacle, check if we can eliminate it (k > 0)
     * 5. Return steps when we reach destination, -1 if impossible
     *
     * Time Complexity: O(m * n * k) where m, n are grid dimensions and k is max eliminations
     * Space Complexity: O(m * n * k) for visited state tracking
     *
     * @param grid 2D grid with 0s (empty) and 1s (obstacles)
     * @param k Maximum number of obstacles that can be eliminated
     * @return Minimum steps to reach destination, -1 if impossible
     */
    public int shortestPath(int[][] grid, int k) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Special case: if we can eliminate enough obstacles to go straight
        if (k >= rows + cols - 2) {
            return rows + cols - 2;
        }

        // BFS with state tracking
        Queue<State> queue = new LinkedList<>();
        boolean[][][] visited = new boolean[rows][cols][k + 1];

        // Directions: up, right, down, left
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        // Start from top-left corner
        queue.offer(new State(0, 0, 0, k));
        visited[0][0][k] = true;

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Check if we reached destination
            if (current.row == rows - 1 && current.col == cols - 1) {
                return current.steps;
            }

            // Try all four directions
            for (int[] direction : directions) {
                int newRow = current.row + direction[0];
                int newCol = current.col + direction[1];

                // Check bounds
                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                    continue;
                }

                int newObstaclesRemaining = current.obstaclesRemaining;

                // If next cell is obstacle, check if we can eliminate it
                if (grid[newRow][newCol] == 1) {
                    if (newObstaclesRemaining == 0) {
                        continue; // Cannot eliminate more obstacles
                    }
                    newObstaclesRemaining--;
                }

                // Check if we've visited this state before
                if (visited[newRow][newCol][newObstaclesRemaining]) {
                    continue;
                }

                // Mark as visited and add to queue
                visited[newRow][newCol][newObstaclesRemaining] = true;
                queue.offer(new State(newRow, newCol, current.steps + 1, newObstaclesRemaining));
            }
        }

        return -1; // No path found
    }

    // State class for BFS
    private static class State {
        int row, col, steps, obstaclesRemaining;

        State(int row, int col, int steps, int obstaclesRemaining) {
            this.row = row;
            this.col = col;
            this.steps = steps;
            this.obstaclesRemaining = obstaclesRemaining;
        }
    }
}
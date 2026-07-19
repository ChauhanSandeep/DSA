package graphs;

import java.util.*;

/**
 * Problem: Shortest Path in a Grid with Obstacles Elimination
 *
 * Move from the top-left cell to the bottom-right cell of a 0/1 grid. You may
 * eliminate at most k obstacle cells, and each move goes one step in the four
 * cardinal directions. Return the fewest steps, or -1 if no valid walk exists.
 *
 * Leetcode: https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/
 * Rating:   1967 (zerotrac Elo)
 * Pattern:  Graph | BFS with state | Dominance pruning
 *
 * Example:
 *   Input:  grid = [[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]], k = 1
 *   Output: 6
 *   Why:    one obstacle can be removed near the bottom, making a shorter route
 *           than walking around all obstacles.
 *
 * Follow-ups:
 *   1. Return the actual path?
 *      Store parent pointers keyed by row, column, and remaining eliminations.
 *   2. Obstacles have different removal costs?
 *      Track remaining budget and use Dijkstra if moves or removals have weights.
 *   3. Count how many shortest paths exist?
 *      Run BFS by layers and count ways for states that reach the destination at the first depth.
 *
 * Related: Shortest Path to Get All Keys (864), Minimum Cost to Make at Least One Valid Path (1368).
 */
public class ShortestPathInAGridWithObstaclesElimination {

    public static void main(String[] args) {
        ShortestPathInAGridWithObstaclesElimination solver = new ShortestPathInAGridWithObstaclesElimination();
        int[][][] grids = {{{0, 0, 0}, {1, 1, 0}, {0, 0, 0}, {0, 1, 1}, {0, 0, 0}}, {{0, 1, 1}, {1, 1, 1}, {1, 0, 0}}};
        int[] eliminations = {1, 1};
        int[] expected = {6, -1};
        for (int i = 0; i < grids.length; i++) {
            int output = solver.shortestPath(grids[i], eliminations[i]);
            System.out.printf("grid=%s k=%d -> %d  expected=%d%n", Arrays.deepToString(grids[i]), eliminations[i], output, expected[i]);
        }
    }

    /**
     * Finds shortest path using BFS with optimized state tracking.
     *
     * Algorithm:
     * 1. Use BFS with state (row, col, obstacles_remaining)
     * 2. Track the maximum remaining eliminations seen at each cell
     * 3. Only process a cell if we reach it with MORE remaining eliminations than
     * before
     * 4. This allows revisiting cells with better states (more flexibility)
     * 5. For each cell, try moving in all 4 directions
     * 6. If next cell is obstacle, check if we can eliminate it (k > 0)
     * 7. Return steps when we reach destination, -1 if impossible
     *
     * Insights: Instead of tracking all (row, col, k) states, we only track
     * the best k value seen at each (row, col). If we reach a cell with more
     * remaining
     * eliminations, that's a strictly better state worth exploring.
     *
     * Time Complexity: O(m * n * k) where m, n are grid dimensions and k is max
     * eliminations
     * Space Complexity: O(m * n) for tracking max remaining eliminations at each
     * cell
     *
     * @param grid 2D grid with 0s (empty) and 1s (obstacles)
     * @param k    Maximum number of obstacles that can be eliminated
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

        // BFS with optimized state tracking
        Queue<State> queue = new LinkedList<>();
        // Track the maximum remaining eliminations we've seen at each cell
        int[][] maxRemainingAt = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(maxRemainingAt[i], -1); // -1 means not visited yet
        }

        // Directions: up, right, down, left
        int[][] directions = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

        // Start from top-left corner
        queue.offer(new State(0, 0, 0, k));
        maxRemainingAt[0][0] = k;

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

                // Only process this cell if we have MORE remaining eliminations than before
                // This ensures we always keep the best state (most flexible path)
                if (newObstaclesRemaining > maxRemainingAt[newRow][newCol]) {
                    maxRemainingAt[newRow][newCol] = newObstaclesRemaining;
                    queue.offer(new State(newRow, newCol, current.steps + 1, newObstaclesRemaining));
                }
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
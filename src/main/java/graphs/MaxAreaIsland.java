package graphs;

import java.util.Stack;

import java.util.Arrays;
/**
 * Problem: Max Area of Island
 *
 * Given a binary grid, return the largest area of any island. An island is a
 * four-directionally connected group of 1 cells, and its area is the number of
 * cells in that group.
 *
 * Leetcode: https://leetcode.com/problems/max-area-of-island/ (Medium)
 * Rating:   acceptance 74.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | DFS connected components | Grid traversal
 *
 * Example:
 *   Input:  grid = [[0,1,1,0],[0,1,0,0],[1,0,0,1]]
 *   Output: 3
 *   Why:    the upper island has three connected land cells, while the other
 *           islands have area one.
 *
 * Follow-ups:
 *   1. Return all island areas sorted?
 *      Record each DFS area in a list and sort it after the scan.
 *   2. Support diagonal connectivity?
 *      Add diagonal directions to the neighbor list.
 *   3. Avoid a separate visited array?
 *      Mark visited land in the grid by changing 1 to 0 during traversal.
 *
 * Related: Number of Islands (200), Making A Large Island (827).
 */
public class MaxAreaIsland {

    public static void main(String[] args) {
        int[][][] inputs = {{{0, 1, 1, 0}, {0, 1, 0, 0}, {1, 0, 0, 1}}, {{0, 0}, {0, 0}}};
        int[] expected = {3, 0};
        for (int i = 0; i < inputs.length; i++) {
            int output = maxAreaOfIsland(inputs[i]);
            System.out.printf("grid=%s  ->  %d  expected=%d%n", Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // Down, Up, Right, Left
    /**
     * Intuition: an island is a connected component of 1-cells. DFS consumes one
     * component at a time, counting its cells and marking them visited by changing
     * them to water. The maximum component size seen is the answer.
     *
     * Algorithm:
     *   1. Scan every cell in the grid.
     *   2. When a land cell is found, DFS its four-direction component.
     *   3. Mark visited land as water and return the component area.
     *   4. Keep the largest area over all components.
     *
     * Time:  O(m*n) - each cell is visited at most once.
     * Space: O(m*n) - recursion can span the whole grid in the worst case.
     *
     * @param grid binary grid with 1 as land and 0 as water
     * @return maximum island area
     */


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

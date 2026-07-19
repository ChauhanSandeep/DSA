package graphs;

import java.util.*;
/**
 * Problem: Number of Closed Islands
 *
 * Given a grid where 0 is land and 1 is water, count islands of land that are
 * completely surrounded by water. Any land connected to the border is not closed.
 *
 * Leetcode: https://leetcode.com/problems/number-of-closed-islands/ (Medium)
 * Rating:   1659 (zerotrac Elo)
 * Pattern:  Graph | Boundary flood fill | Grid components
 *
 * Example:
 *   Input:  grid = [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]
 *   Output: 2
 *   Why:    two interior land components are fully enclosed, while the land
 *           touching the right border is open and does not count.
 *
 * Follow-ups:
 *   1. Return the largest closed island area?
 *      Count cells during the second flood fill and keep the maximum.
 *   2. Avoid recursion on large grids?
 *      Use the BFS implementation style with a queue.
 *   3. Support multiple land types?
 *      Flood-fill only cells matching the chosen land type and treat others as boundaries.
 *
 * Related: Surrounded Regions (130), Number of Islands (200).
 */
public class NumberOfClosedIslands {

    public static void main(String[] args) {
        NumberOfClosedIslands solver = new NumberOfClosedIslands();
        int[][][] inputs = {
            {{1, 1, 1, 1, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 1, 0}, {1, 0, 1, 0, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 0, 1}, {1, 1, 1, 1, 1, 1, 1, 0}},
            {{0, 0, 1}, {0, 1, 1}, {1, 1, 1}}
        };
        int[] expected = {2, 0};
        for (int i = 0; i < inputs.length; i++) {
            int[][] grid = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            int output = solver.closedIsland(grid);
            System.out.printf("grid=%s  ->  %d  expected=%d%n", Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    // Directions for moving in 4 directions
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    /**
     * Intuition: a closed island is a land component that never touches the grid
     * boundary. DFS explores one land component and returns whether every recursive
     * branch stays inside the board; touching the boundary makes that component open.
     *
     * Algorithm:
     *   1. Scan only interior cells as possible starts of closed islands.
     *   2. When land is found, DFS its full four-direction component.
     *   3. Mark visited land as water to avoid recounting it.
     *   4. Count the component only if DFS never reaches the boundary.
     *
     * Time:  O(m*n) - each cell is visited at most once.
     * Space: O(m*n) - recursion stack can cover the whole grid in the worst case.
     *
     * @param grid binary grid where 0 is land and 1 is water
     * @return number of closed islands
     */
    public int closedIsland(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        // Mark all land cells connected to the boundary as visited (not closed)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Check boundary cells
                if ((i == 0 || j == 0 || i == rows - 1 || j == cols - 1) && grid[i][j] == 0) {
                    markBoundary(grid, i, j);
                }
            }
        }

        // Count closed islands
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    dfs(grid, i, j);
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Marks all land cells connected to the boundary as visited (value 1).
     */
    private void markBoundary(int[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != 0) {
            return;
        }

        grid[i][j] = 1; // Mark as water to indicate it's connected to boundary

        // Visit all four directions
        for (int[] dir : DIRECTIONS) {
            markBoundary(grid, i + dir[0], j + dir[1]);
        }
    }

    /**
     * Performs DFS to mark all connected land cells as visited.
     */
    private void dfs(int[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != 0) {
            return;
        }

        grid[i][j] = 1; // Mark as visited

        // Visit all four directions
        for (int[] dir : DIRECTIONS) {
            dfs(grid, i + dir[0], j + dir[1]);
        }
    }

    /**
     * Alternative BFS solution for better handling of large grids (avoids stack overflow).
     */
    public int closedIslandBFS(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int count = 0;

        // Mark boundary-connected land cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((i == 0 || j == 0 || i == rows - 1 || j == cols - 1) && grid[i][j] == 0) {
                    bfsBoundary(grid, i, j, visited);
                }
            }
        }

        // Count closed islands
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0 && !visited[i][j]) {
                    bfs(grid, i, j, visited);
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * BFS to mark all land cells connected to the boundary.
     */
    private void bfsBoundary(int[][] grid, int i, int j, boolean[][] visited) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length ||
            grid[i][j] != 0 || visited[i][j]) {
            return;
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{i, j});
        visited[i][j] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length &&
                    grid[newX][newY] == 0 && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                }
            }
        }
    }

    /**
     * BFS to mark all connected land cells as part of the current island.
     */
    private void bfs(int[][] grid, int i, int j, boolean[][] visited) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{i, j});
        visited[i][j] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX > 0 && newX < grid.length - 1 && newY > 0 && newY < grid[0].length - 1 &&
                    grid[newX][newY] == 0 && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                }
            }
        }
    }
}

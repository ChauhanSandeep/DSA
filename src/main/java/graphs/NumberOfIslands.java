package graphs;

import java.util.Arrays;

/**
 * Problem: Number of Islands
 *
 * Given a grid of water cells ('0') and land cells ('1'), count how many separate
 * islands are present. Land belongs to the same island only when it touches another
 * land cell horizontally or vertically.
 *
 * Leetcode: https://leetcode.com/problems/number-of-islands/
 * Rating:   acceptance 64.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Grid traversal | DFS flood fill
 *
 * Example:
 *   Input:  grid = [[1,1,0],[0,1,0],[1,0,1]]
 *   Output: 3
 *   Why:    the top-left land mass is one island, and the two diagonal land cells
 *           do not connect to it or to each other because diagonals do not count.
 *
 * Follow-ups:
 *   1. Count islands without modifying the grid?
 *      Keep a separate visited matrix instead of sinking land in place.
 *   2. Avoid recursion on a huge grid?
 *      Use the BFS variant with an explicit queue to avoid call-stack overflow.
 *   3. Count islands as land is added online?
 *      Use Union-Find and merge newly added land with active neighbors.
 *
 * Related: Number of Provinces (547), Max Area of Island (695), Number of Closed Islands (1254).
 *
 */
public class NumberOfIslands {

    public static void main(String[] args) {
        char[][][] grids = {{{'1', '1', '0'}, {'0', '1', '0'}, {'1', '0', '1'}}, {{'0'}}, {{'1', '1'}, {'1', '1'}}};
        int[] expected = {3, 0, 1};
        for (int i = 0; i < grids.length; i++) {
            String input = Arrays.deepToString(grids[i]);
            int output = numIslands(grids[i]);
            System.out.printf("grid=%s -> %d  expected=%d%n", input, output, expected[i]);
        }
    }


    /**
     * Counts the number of islands using DFS.
     *
     * @param grid 2D grid representing land ('1') and water ('0')
     * @return Number of islands in the grid
     */
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int islandCount = 0;
        int rows = grid.length, cols = grid[0].length;

        // Iterate through the grid to find land ('1') and perform DFS
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == '1') {
                    islandCount++;
                    exploreIslandDFS(grid, row, col);
                }
            }
        }
        return islandCount;
    }

    /**
     * Depth-First Search (DFS) to explore an island and mark it as visited.
     *
     * @param grid The grid representing land ('1') and water ('0')
     * @param row  Current row index
     * @param col  Current column index
     */
    private static void exploreIslandDFS(char[][] grid, int row, int col) {
        int rows = grid.length, cols = grid[0].length;

        // Base case: If out of bounds or water ('0'), return
        if (row < 0 || col < 0 || row >= rows || col >= cols || grid[row][col] != '1') {
            return;
        }

        // Mark the current cell as visited ('0')
        grid[row][col] = '0';

        // Explore the four possible directions (up, down, left, right)
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            exploreIslandDFS(grid, row + dir[0], col + dir[1]);
        }
    }

    /**
     * Alternative approach: Breadth-First Search (BFS) to count islands.
     * Useful when recursion depth is a concern (avoids stack overflow).
     *
     * @param grid The grid representing land ('1') and water ('0')
     * @return Number of islands in the grid
     */
    public static int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int islandCount = 0;
        int rows = grid.length, cols = grid[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == '1') {
                    islandCount++;
                    exploreIslandBFS(grid, row, col);
                }
            }
        }
        return islandCount;
    }

    /**
     * Uses BFS to explore an island and mark it as visited.
     *
     * @param grid The grid representing land ('1') and water ('0')
     * @param row  Current row index
     * @param col  Current column index
     */
    private static void exploreIslandBFS(char[][] grid, int row, int col) {
        int rows = grid.length, cols = grid[0].length;
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.offer(new int[]{row, col});
        grid[row][col] = '0'; // Mark as visited

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            for (int[] dir : directions) {
                int newRow = current[0] + dir[0];
                int newCol = current[1] + dir[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol] == '1') {
                    queue.offer(new int[]{newRow, newCol});
                    grid[newRow][newCol] = '0'; // Mark as visited
                }
            }
        }
    }
}

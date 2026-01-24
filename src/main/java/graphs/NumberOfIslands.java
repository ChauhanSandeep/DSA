package graphs;

/**
 * Number of Islands problem
 * LeetCode: https://leetcode.com/problems/number-of-islands/
 *
 * --- Problem Description ---
 * Given a 2D grid of '1' (land) and '0' (water), count the number of islands.
 * An island is surrounded by water and is formed by horizontally or vertically connected lands.
 *
 * --- Approach ---
 * 1. Iterate through each cell in the grid.
 * 2. When encountering a '1', it signifies the start of an island:
 *    - Use **DFS** to explore and mark all connected land as visited.
 *    - Increment island count.
 * 3. Continue scanning the grid until all islands are counted.
 *
 * --- Complexity Analysis ---
 * - **Time Complexity:** O(m * n) in the worst case (every cell visited once).
 * - **Space Complexity:** O(m * n) in worst case (DFS recursion stack).
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class NumberOfIslands {

    public static void main(String[] args) {
        char[][] grid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };

        System.out.println("Number of islands: " + numIslands(grid));
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

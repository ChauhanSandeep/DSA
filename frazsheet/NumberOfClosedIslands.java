package frazsheet;

/**
 * Given a 2D grid consists of 0s (land) and 1s (water). An island is a maximal 4-directionally 
 * connected group of 0s and a closed island is an island totally (all left, top, right, bottom) 
 * surrounded by 1s.
 * 
 * Return the number of closed islands.
 * 
 * Example 1:
 * Input: grid = [
 *   [1,1,1,1,1,1,1,0],
 *   [1,0,0,0,0,1,1,0],
 *   [1,0,1,0,1,1,1,0],
 *   [1,0,0,0,0,1,0,1],
 *   [1,1,1,1,1,1,1,0]]
 * Output: 2
 * Explanation: Islands in gray are closed because they are completely surrounded by water (group of 1s).
 * 
 * Example 2:
 * Input: grid = [
 *   [0,0,1,0,0],
 *   [0,1,0,1,0],
 *   [0,1,1,1,0]]
 * Output: 1
 * 
 * LeetCode: https://leetcode.com/problems/number-of-closed-islands/
 * 
 * Follow-up Questions:
 * 1. How would you handle very large grids (e.g., 1000x1000)?
 *    - The DFS/BFS approach is O(mn) which is optimal, but for very large grids, we might need to 
 *      consider iterative solutions to avoid stack overflow.
 * 2. What if we need to find the largest closed island?
 *    - We could modify the solution to track the size of each island and return the maximum size.
 * 3. How would you handle grids with more than two values (e.g., different types of land)?
 *    - The solution would need to be adjusted to handle different land types and their specific rules.
 * 
 * Related Problems:
 * - Number of Islands (https://leetcode.com/problems/number-of-islands/)
 * - Surrounded Regions (https://leetcode.com/problems/surrounded-regions/)
 */
public class NumberOfClosedIslands {
    // Directions for moving in 4 directions
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    
    /**
     * Counts the number of closed islands in the given grid.
     * 
     * @param grid 2D grid where 0 represents land and 1 represents water
     * @return Number of closed islands
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

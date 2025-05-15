package Graph;

import java.util.*;

/**
 * Problem: Given an `m x n` matrix where each cell represents height, determine
 * how many cells can allow water to flow to both the top-left and bottom-right corners.
 *
 * Intuition:
 * - Water can flow from a higher or equal elevation to a lower one.
 * - Use BFS from both the top-left and bottom-right edges to mark reachable cells.
 * - Count the number of common cells reachable from both sources.
 *
 * Algorithm:
 * 1. **Initialization:**
 *    - Use two boolean matrices to track if water can reach from top-left and bottom-right.
 *    - Add boundary nodes to their respective BFS queues.
 * 2. **BFS Traversal:**
 *    - Expand from each queue, marking reachable cells.
 * 3. **Count Intersection:**
 *    - Count cells that are marked as reachable in both boolean matrices.
 *
 * Time Complexity: **O(m * n)**  
 * - Each cell is processed at most twice (once per BFS), making it linear.
 *
 * Space Complexity: **O(m * n)**  
 * - Boolean matrices store reachability status.
 *
 * Similar Problem: [LeetCode 417 - Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/)
 */
public class WaterFlow {
    
    private int rows, cols;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) {
        int[][] grid = {
            {1, 2, 2, 3, 5},
            {3, 2, 3, 4, 4},
            {2, 4, 5, 3, 1},
            {6, 7, 1, 4, 5},
            {5, 1, 1, 2, 4}
        };
        System.out.println(new WaterFlow().countCommonWaterFlowCells(grid)); // Output: 7
    }

    /**
     * Counts the number of grid cells where water can flow from both the top-left and bottom-right edges.
     *
     * @param grid The height matrix.
     * @return The count of common cells.
     */
    public int countCommonWaterFlowCells(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        rows = grid.length;
        cols = grid[0].length;

        boolean[][] canReachTopLeft = new boolean[rows][cols];
        boolean[][] canReachBottomRight = new boolean[rows][cols];

        Queue<int[]> queueTopLeft = new LinkedList<>();
        Queue<int[]> queueBottomRight = new LinkedList<>();

        // Add top-left and bottom-right boundary nodes to BFS queues
        for (int i = 0; i < rows; i++) {
            queueTopLeft.add(new int[]{i, 0});
            queueBottomRight.add(new int[]{i, cols - 1});
            canReachTopLeft[i][0] = true;
            canReachBottomRight[i][cols - 1] = true;
        }
        for (int j = 0; j < cols; j++) {
            queueTopLeft.add(new int[]{0, j});
            queueBottomRight.add(new int[]{rows - 1, j});
            canReachTopLeft[0][j] = true;
            canReachBottomRight[rows - 1][j] = true;
        }

        // Perform BFS for both top-left and bottom-right
        bfs(grid, queueTopLeft, canReachTopLeft);
        bfs(grid, queueBottomRight, canReachBottomRight);

        // Count cells reachable from both top-left and bottom-right
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (canReachTopLeft[i][j] && canReachBottomRight[i][j]) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Performs BFS to mark all reachable cells in a given boolean matrix.
     *
     * @param grid    The height matrix.
     * @param queue   The BFS queue containing the starting boundary cells.
     * @param visited The boolean matrix marking reachable cells.
     */
    private void bfs(int[][] grid, Queue<int[]> queue, boolean[][] visited) {
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0], col = cell[1];

            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0], newCol = col + dir[1];

                // Check if the new cell is within bounds, unvisited, and has a valid flow condition
                if (newRow >= 0 && newCol >= 0 && newRow < rows && newCol < cols &&
                    !visited[newRow][newCol] && grid[newRow][newCol] >= grid[row][col]) {
                    
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }
    }
}

package DynamicProgramming;

/**
 * Problem: Dungeon Game
 * LeetCode Link: https://leetcode.com/problems/dungeon-game/
 *
 * Description:
 * A knight is on an `m x n` dungeon grid, starting at the top-left corner (0,0).
 * The knight needs to reach the bottom-right corner (m-1, n-1).
 * Each cell contains a value:
 *   - Positive value: Increases knight's health.
 *   - Negative value: Decreases knight's health.
 *   - Zero: No effect.
 * The knight must always maintain at least 1 health point.
 *
 * Goal:
 * Determine the minimum initial health required for the knight to safely reach the destination.
 *
 * Approach:
 * - **Recursive (Top-down with Memoization)**: Uses DFS with memoization to explore all paths.
 * - **Iterative (Bottom-up Dynamic Programming)**: Uses a DP table to compute health backwards.
 *
 * Complexity Analysis:
 * - **Recursive DFS + Memoization**: O(m * n) time, O(m * n) space (due to recursion & memoization).
 * - **Iterative DP Approach**: O(m * n) time, O(m * n) space (DP table).
 */
public class DungeonGame {

    public static void main(String[] args) {
        int[][] dungeon = {
                {-2, -3, 3},
                {-5, -10, 1},
                {10, 30, -5}
        };

        DungeonGame solver = new DungeonGame();
        System.out.println("Minimum HP (Recursive): " + solver.calculateMinimumHPRecursive(dungeon));
        System.out.println("Minimum HP (Iterative DP): " + solver.calculateMinimumHPIterative(dungeon));
    }

    /**
     * Recursive approach using DFS with memoization.
     * 
     * @param dungeon The given dungeon grid.
     * @return The minimum initial health required.
     */
    public int calculateMinimumHPRecursive(int[][] dungeon) {
        if (dungeon == null || dungeon.length == 0 || dungeon[0].length == 0) {
            return 1;
        }
        
        int rows = dungeon.length;
        int cols = dungeon[0].length;
        Integer[][] memo = new Integer[rows][cols];

        // Compute the minimum health required from (0,0) to (m-1, n-1)
        int requiredHealth = dfs(dungeon, 0, 0, memo);

        // Ensure knight starts with at least 1 HP
        return Math.max(1, -requiredHealth + 1);
    }

    /**
     * Depth-first search (DFS) with memoization.
     */
    private int dfs(int[][] dungeon, int row, int col, Integer[][] memo) {
        int rows = dungeon.length;
        int cols = dungeon[0].length;

        // Out of bounds check
        if (row >= rows || col >= cols) {
            return Integer.MIN_VALUE;
        }

        // Base case: If at the bottom-right cell, minimum health required is min(dungeon value, 0)
        if (row == rows - 1 && col == cols - 1) {
            return Math.min(dungeon[row][col], 0);
        }

        // Check memoization table
        if (memo[row][col] != null) {
            return memo[row][col];
        }

        // Recursively find the minimum health needed for both possible moves (right & down)
        int moveDown = dfs(dungeon, row + 1, col, memo);
        int moveRight = dfs(dungeon, row, col + 1, memo);

        // If both moves are invalid, return invalid value
        if (moveDown == Integer.MIN_VALUE && moveRight == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }

        // Compute max health needed at the current cell
        int maxHealthNeeded = dungeon[row][col] + Math.max(moveDown, moveRight);

        // Memoize the result
        memo[row][col] = Math.min(maxHealthNeeded, 0);

        return memo[row][col];
    }

    /**
     * Iterative DP approach (Bottom-up).
     * 
     * @param dungeon The given dungeon grid.
     * @return The minimum initial health required.
     */
    public int calculateMinimumHPIterative(int[][] dungeon) {
        if (dungeon == null || dungeon.length == 0 || dungeon[0].length == 0) {
            return 1;
        }

        int rows = dungeon.length;
        int cols = dungeon[0].length;
        int[][] dp = new int[rows][cols];

        // Process cells from bottom-right to top-left
        for (int row = rows - 1; row >= 0; row--) {
            for (int col = cols - 1; col >= 0; col--) {
                
                // Base case: Bottom-right cell (destination)
                if (row == rows - 1 && col == cols - 1) {
                    dp[row][col] = Math.max(1, 1 - dungeon[row][col]);
                }
                // Last row (can only move right)
                else if (row == rows - 1) {
                    dp[row][col] = Math.max(dp[row][col + 1] - dungeon[row][col], 1);
                }
                // Last column (can only move down)
                else if (col == cols - 1) {
                    dp[row][col] = Math.max(dp[row + 1][col] - dungeon[row][col], 1);
                }
                // General case: Take the min health required from right or down cell
                else {
                    dp[row][col] = Math.min(
                        Math.max(dp[row + 1][col] - dungeon[row][col], 1),
                        Math.max(dp[row][col + 1] - dungeon[row][col], 1)
                    );
                }
            }
        }

        return dp[0][0];
    }
}

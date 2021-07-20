package DynamicProgramming;

import java.util.Arrays;

public class MinPathSum {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}};
        System.out.println(new MinPathSum().minPathSum(grid));
    }

    /**
     * Given a m x n grid filled with non-negative numbers,
     * find a path from top left to bottom right, which minimizes the sum of all numbers along its path.
     * You can only move either down or right at any point in time.
     */
    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int[][] dp = new int[grid.length][grid[0].length];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        return minPathSumRec(grid, 0, 0, dp);

    }

    public int minPathSumRec(int[][] grid, int i, int j, int[][] dp) {
        if (i == grid.length - 1 && j == grid[i].length - 1) return grid[i][j];
        if (i >= grid.length || j >= grid[i].length) return Integer.MAX_VALUE;
        if (dp[i][j] != Integer.MAX_VALUE) return dp[i][j];

        int result = Math.min(
                minPathSumRec(grid, i + 1, j, dp),
                minPathSumRec(grid, i, j + 1, dp)
        ) + grid[i][j];
        dp[i][j] = result;
        return result;
    }
}

package array;

/**
 * Given a 2D binary matrix filled with 0s and 1s, find the largest square containing only 1s and return its area.
 *
 * LeetCode: https://leetcode.com/problems/maximal-square/
 */
public class MaximalSquare {
    public static void main(String[] args) {
        int[][] matrix = {
                {0, 1, 1, 0, 1},
                {1, 1, 0, 1, 0},
                {0, 1, 1, 1, 0},
                {1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0}
        };
        System.out.println("Maximum Square Area: " + maximalSquare(matrix));
    }

    public static int maximalSquare(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;

        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxSide = 0;

        // DP matrix to store max square size ending at (i, j)
        int[][] dp = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // If cell contains '1', check the minimum square formed by the top, left, and top-left diagonal cells
                if (matrix[i][j] == 1) {
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1; // First row/column can only have size 1
                    } else {
                        dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                    }
                    maxSide = Math.max(maxSide, dp[i][j]);
                }
            }
        }

        // Return area of largest square found
        return maxSide * maxSide;
    }
}

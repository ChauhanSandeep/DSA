package arrays;

import java.util.Arrays;
/**
 * Problem: Maximal Square
 *
 * Given a binary matrix, find the area of the largest square containing only 1s.
 * The square must be axis-aligned inside the matrix, and the return value is its
 * area, not its side length.
 *
 * Leetcode: https://leetcode.com/problems/maximal-square/ (Medium)
 * Rating:   acceptance 50.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Dynamic programming | Square ending at each cell
 *
 * Example:
 *   Input:  matrix = [[1,0,1,0,0],[1,0,1,1,1],[1,1,1,1,1],[1,0,0,1,0]]
 *   Output: 4
 *   Why:    the largest all-1 square has side length 2, so its area is 2 * 2 = 4.
 *
 * Follow-ups:
 *   1. Return the coordinates of one largest square?
 *      Track the row and column whenever maxSide improves, then derive the top-left corner.
 *   2. Reduce space from O(rows * cols) to O(cols)?
 *      Keep only the previous row, current row, and the previous diagonal value.
 *   3. Count all all-1 squares instead of just the largest?
 *      Sum the DP side length at every cell, since each side length contributes that many squares.
 *
 * Related: Count Square Submatrices with All Ones (1277), Maximal Rectangle (85).
 */
public class MaximalSquare {

    public static void main(String[] args) {
        int[][][] inputs = {
            { {1, 0, 1, 0, 0}, {1, 0, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 0, 0, 1, 0} },
            { {0, 1}, {1, 0} },
            { {0} }
        };
        int[] expected = { 4, 1, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = maximalSquare(inputs[i]);
            System.out.printf("matrix=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }
    /**
     * Intuition: a cell containing 1 can be the bottom-right corner of a square only as
     * large as its top, left, and top-left neighbours can all support. The smallest of
     * those three neighbouring square sizes is the limiting side length, so adding one
     * extends that common square through the current cell.
     *
     * Algorithm:
     *   1. Return 0 for a null or empty matrix.
     *   2. Create dp where dp[i][j] is the largest all-1 square ending at cell (i, j).
     *   3. For each 1 cell, set border cells to 1 or use min(top, left, diagonal) + 1.
     *   4. Track maxSide and return maxSide * maxSide.
     *
     * Time:  O(rows * cols) - every matrix cell is processed once.
     * Space: O(rows * cols) - dp stores one square side length per cell.
     *
     * @param matrix binary matrix using 1 for filled cells
     * @return area of the largest all-1 square
     */
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

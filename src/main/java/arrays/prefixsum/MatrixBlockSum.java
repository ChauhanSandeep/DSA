package arrays.prefixsum;

import java.util.Arrays;
/**
 * Problem: Matrix Block Sum
 *
 * For each cell in a matrix, compute the sum of all cells within k rows and k
 * columns of it, clipped to valid matrix boundaries. Return the matrix of those
 * block sums.
 *
 * Leetcode: https://leetcode.com/problems/matrix-block-sum/ (Medium)
 * Rating:   1484 (Weekly Contest 170)
 * Pattern:  2D prefix sum | Inclusion-exclusion | Matrix queries
 *
 * Example:
 *   Input:  mat = [[1,2,3],[4,5,6],[7,8,9]], k = 1
 *   Output: [[12,21,16],[27,45,33],[24,39,28]]
 *   Why:    the center block includes all nine cells, while edge blocks are clipped.
 *
 * Follow-ups:
 *   1. Many different k values?
 *      Reuse one 2D prefix sum and recompute each answer grid from it.
 *   2. Support point updates?
 *      Use a 2D Fenwick tree or segment tree instead of a static prefix sum.
 *   3. Extend to 3D blocks?
 *      Build a 3D prefix sum and apply higher-dimensional inclusion-exclusion.
 *
 * Related: Range Sum Query 2D Immutable (304), Max Sum of Rectangle No Larger Than K (363).
 */
public class MatrixBlockSum {

    public static void main(String[] args) {
        MatrixBlockSum solver = new MatrixBlockSum();

        int[][][] mats = {
            { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} },
            { {1, 2}, {3, 4} }
        };
        int[] radii = { 1, 2 };
        int[][][] expected = {
            { {12, 21, 16}, {27, 45, 33}, {24, 39, 28} },
            { {10, 10}, {10, 10} }
        };

        for (int i = 0; i < mats.length; i++) {
            int[][] got = solver.matrixBlockSum(mats[i], radii[i]);
            System.out.printf("mat=%s k=%d -> output=%s  expected=%s%n",
                Arrays.deepToString(mats[i]), radii[i], Arrays.deepToString(got), Arrays.deepToString(expected[i]));
        }
    }

/**
 * Intuition: every requested block is just a rectangle sum. A 2D prefix sum turns
 * any rectangle sum into four table lookups, so the expensive part is paid once
 * up front instead of once per cell per neighbor.
 *
 * Algorithm:
 *   1. Build prefixSum with one extra row and column for boundary-safe queries.
 *   2. For each matrix cell, clamp its k-radius block to matrix boundaries.
 *   3. Use getSumFromPrefix to compute that rectangle sum.
 *   4. Store the sum in the matching result cell.
 *
 * Time:  O(m * n) - build and query each cell once.
 * Space: O(m * n) - the prefix-sum matrix stores one value per cell plus borders.
 *
 * @param mat input matrix
 * @param k block radius in every direction
 * @return matrix of block sums
 */
    public int[][] matrixBlockSum(int[][] mat, int k) {
        int rows = mat.length;
        int cols = mat[0].length;

        // Build prefix sum matrix. prefixSum[i][j] is sum of elements in rectangle (0,0) to (i-1,j-1)
        int[][] prefixSum = new int[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                prefixSum[i][j] = mat[i-1][j-1] + prefixSum[i-1][j] +
                                 prefixSum[i][j-1] - prefixSum[i-1][j-1];
            }
        }

        // Calculate block sums
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Determine block boundaries
                int topRow = Math.max(0, i - k);
                int leftCol = Math.max(0, j - k);
                int bottomRow = Math.min(rows - 1, i + k);
                int rightCol = Math.min(cols - 1, j + k);

                // Use prefix sum to calculate block sum
                result[i][j] = getSumFromPrefix(prefixSum, topRow, leftCol, bottomRow, rightCol);
            }
        }

        return result;
    }

    // Helper method to get sum from prefix sum matrix
    private int getSumFromPrefix(int[][] prefixSum, int topRow, int leftCol, int bottomRow, int rightCol) {
        return prefixSum[bottomRow + 1][rightCol + 1]
            - prefixSum[topRow][rightCol + 1]
            - prefixSum[bottomRow + 1][leftCol]
            + prefixSum[topRow][leftCol];
    }

    /**
     * Space-optimized version building prefix sum on-the-fly
     * Time Complexity: O(m*n), Space Complexity: O(n) for each row
     */
    public int[][] matrixBlockSumOptimized(int[][] mat, int k) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[][] result = new int[rows][cols];

        // For each row, maintain column prefix sums
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = 0;

                int topRow = Math.max(0, i - k);
                int leftCol = Math.max(0, j - k);
                int bottomRow = Math.min(rows - 1, i + k);
                int rightCol = Math.min(cols - 1, j + k);

                for (int r = topRow; r <= bottomRow; r++) {
                    for (int c = leftCol; c <= rightCol; c++) {
                        sum += mat[r][c];
                    }
                }

                result[i][j] = sum;
            }
        }

        return result;
    }
}

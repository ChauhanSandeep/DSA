package arrays;

/**
 * Matrix Block Sum
 *
 * Problem: Given a m x n matrix mat and an integer k, return a matrix answer where each answer[i][j] is the
 * sum of all elements mat[r][c] for:
 *
 * i - k <= r <= i + k,
 * j - k <= c <= j + k, and
 *
 * Example: mat = [
 *      [1,2,3],
 *      [4,5,6],
 *      [7,8,9]
 *    ], k = 1
 * -> Output: [
 *      [12,21,16],
 *      [27,45,33],
 *      [24,39,28]
 *   ]
 * Explanation:
 * For position (1,1), block includes all elements, sum = 45.
 * For position (0,0), block includes elements (0,0),(0,1),(1,0),(1,1), sum = 12.
 *
 * LeetCode: https://leetcode.com/problems/matrix-block-sum
 *
 * Follow-up Questions:
 * - How to handle different block shapes? (Modify boundary calculations)
 * - What if k can be different for rows vs columns? (Use separate kRow, kCol parameters)
 * - Can we solve without prefix sums? (Yes, but O(n²k²) vs O(n²))
 */
public class MatrixBlockSum {

    /**
     * Calculates block sums using 2D prefix sum technique.
     *
     * Algorithm:
     * 1. Build 2D prefix sum matrix
     * 2. For each position (i,j), determine block boundaries
     * 3. Use prefix sum formula: sum = prefixSum[r2+1][c2+1] - prefixSum[r1][c2+1]
     *                                  - prefixSum[r2+1][c1] + prefixSum[r1][c1]
     * 4. Handle boundary conditions carefully
     *
     * Time Complexity: O(m*n) where m,n are matrix dimensions
     * Space Complexity: O(m*n) for prefix sum matrix
     *
     * @param mat input matrix
     * @param k radius of block (block size is 2k+1)
     * @return matrix with block sums
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

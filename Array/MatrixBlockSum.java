package Array;

/**
 * Matrix Block Sum
 * 
 * Problem: For each position (i,j), calculate sum of elements in block of size (2k+1) x (2k+1)
 * centered at (i,j). Return matrix with these block sums.
 * 
 * Example: mat = [[1,2,3],[4,5,6],[7,8,9]], k = 1 -> Output: [[12,21,16],[27,45,33],[24,39,28]]
 * Each position contains sum of its k-radius neighborhood.
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
        int m = mat.length;
        int n = mat[0].length;

        // Build prefix sum matrix
        int[][] prefixSum = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                prefixSum[i][j] = mat[i-1][j-1] + prefixSum[i-1][j] + 
                                 prefixSum[i][j-1] - prefixSum[i-1][j-1];
            }
        }

        // Calculate block sums
        int[][] result = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Determine block boundaries
                int r1 = Math.max(0, i - k);
                int c1 = Math.max(0, j - k);
                int r2 = Math.min(m - 1, i + k);
                int c2 = Math.min(n - 1, j + k);

                // Use prefix sum to calculate block sum
                result[i][j] = getSumFromPrefix(prefixSum, r1, c1, r2, c2);
            }
        }

        return result;
    }

    // Helper method to get sum from prefix sum matrix
    private int getSumFromPrefix(int[][] prefixSum, int r1, int c1, int r2, int c2) {
        return prefixSum[r2 + 1][c2 + 1] - prefixSum[r1][c2 + 1] - 
               prefixSum[r2 + 1][c1] + prefixSum[r1][c1];
    }

    /**
     * Brute force approach for verification (less efficient)
     * Time Complexity: O(m*n*k²), Space Complexity: O(1)
     */
    public int[][] matrixBlockSumBruteForce(int[][] mat, int k) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] result = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;

                // Sum all elements in k-radius block
                for (int r = Math.max(0, i - k); r <= Math.min(m - 1, i + k); r++) {
                    for (int c = Math.max(0, j - k); c <= Math.min(n - 1, j + k); c++) {
                        sum += mat[r][c];
                    }
                }

                result[i][j] = sum;
            }
        }

        return result;
    }

    /**
     * Space-optimized version building prefix sum on-the-fly
     * Time Complexity: O(m*n), Space Complexity: O(n) for each row
     */
    public int[][] matrixBlockSumOptimized(int[][] mat, int k) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] result = new int[m][n];

        // For each row, maintain column prefix sums
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;

                int r1 = Math.max(0, i - k);
                int c1 = Math.max(0, j - k);
                int r2 = Math.min(m - 1, i + k);
                int c2 = Math.min(n - 1, j + k);

                for (int r = r1; r <= r2; r++) {
                    for (int c = c1; c <= c2; c++) {
                        sum += mat[r][c];
                    }
                }

                result[i][j] = sum;
            }
        }

        return result;
    }
}

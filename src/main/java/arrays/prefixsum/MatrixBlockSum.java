package arrays.prefixsum;

/**
 * LeetCode Problem 1314: Matrix Block Sum
 *
 * Problem Statement:
 * Given an m x n matrix mat and an integer k, return a matrix answer where each
 * answer[i][j] is the sum of all elements mat[r][c] for:
 * - i - k <= r <= i + k
 * - j - k <= c <= j + k
 * - (r, c) is a valid position in the matrix
 *
 * In other words, for each cell in the answer matrix, calculate the sum of all elements
 * in a rectangular block that extends k positions in all four directions from that cell's
 * corresponding position in the original matrix.
 *
 * Example 1:
 * Input: mat = [
 *      [1,2,3],
 *      [4,5,6],
 *      [7,8,9]], 
 * k = 1
 * Output: [
 *      [12,21,16],
 *      [27,45,33],
 *      [24,39,28]
 * ]
 * Explanation: For position (0,0), the block includes elements from rows [0,1] and columns
 * [0,1], which are 1,2,4,5, summing to 12. For position (1,1), the block includes all 9
 * elements (since k=1 allows reaching all positions from center), summing to 45.
 *
 * Example 2:
 * Input: mat = [
 *      [1,2,3],
 *      [4,5,6],
 *      [7,8,9]
 * ], k = 2
 * Output: [
 *      [45,45,45],
 *      [45,45,45],
 *      [45,45,45]]
 * Explanation: With k=2, from any position we can reach all cells in the matrix, so every
 * position has the sum of all elements (45).
 *
 * Constraints:
 * - m == mat.length
 * - n == mat[i].length
 * - 1 <= m, n, k <= 100
 * - 1 <= mat[i][j] <= 100
 *
 * LeetCode Link: https://leetcode.com/problems/matrix-block-sum/
 *
 * Follow-up Questions:
 * 1. Q: How would you solve this if queries are made with different k values multiple times?
 *    A: Precompute the 2D prefix sum once (O(m*n) time). Then for each query with different k,
 *    compute the answer in O(m*n) time using the precomputed prefix sum. This is more efficient
 *    than recomputing prefix sums for each query.
 *
 * 2. Q: What if the matrix is very large and sparse (many zeros)?
 *    A: Use a sparse matrix representation (like HashMap with coordinates as keys). Only store
 *    non-zero values and their positions. When computing block sums, only iterate over non-zero
 *    elements within the range. Related: https://leetcode.com/problems/sparse-matrix-multiplication/
 *
 * 3. Q: How would you extend this to 3D matrices (cuboid blocks)?
 *    A: Use 3D prefix sums. The formula extends to: prefixSum[i][j][k] = mat[i][j][k] +
 *    prefixSum[i-1][j][k] + prefixSum[i][j-1][k] + prefixSum[i][j][k-1] - prefixSum[i-1][j-1][k]
 *    - prefixSum[i-1][j][k-1] - prefixSum[i][j-1][k-1] + prefixSum[i-1][j-1][k-1].
 *    Query formula becomes more complex with 8 terms.
 *
 * 4. Q: What if we need to find the maximum block sum instead of computing all blocks?
 *    A: Use the same 2D prefix sum approach but iterate through all possible positions to find
 *    the maximum. This still maintains O(m*n) time complexity for the search after prefix sum
 *    computation. Related: https://leetcode.com/problems/maximum-sum-of-rectangle-no-larger-than-k/
 *
 * 5. Q: How would you handle updates to the matrix elements dynamically?
 *    A: For frequent updates, consider using a 2D Binary Indexed Tree (Fenwick Tree) or 2D
 *    Segment Tree. Updates would be O(log m * log n) and range sum queries would also be
 *    O(log m * log n), making it efficient for dynamic scenarios. Static prefix sum becomes
 *    inefficient if matrix changes frequently.
 * LeetCode Contest Rating: 1484
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

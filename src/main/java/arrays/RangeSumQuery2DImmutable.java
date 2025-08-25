package arrays;

import java.util.Arrays;


/**
 * Range Sum Query 2D Immutable
 *
 * Problem: Design data structure to efficiently calculate sum of elements in 2D region.
 * Matrix is immutable after construction.
 *
 * Example: matrix = [[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]
 * sumRegion(2,1,4,3) should return 8 (sum of region from (2,1) to (4,3)).
 *
 * LeetCode: https://leetcode.com/problems/range-sum-query-2d-immutable
 *
 * Follow-up Questions:
 * - How to handle updates to matrix? (Use 2D Fenwick tree or segment tree)
 * - What if queries are much more frequent than construction? (Current solution is optimal)
 * - Can we handle 3D range queries? (Extend to 3D prefix sum)
 */
public class RangeSumQuery2DImmutable {

    private int[][] prefixSum;

    /**
     * Constructs the data structure with 2D prefix sum for fast range queries.
     *
     * Algorithm:
     * 1. Build 2D prefix sum array where prefixSum[i][j] = sum of rectangle from (0,0) to (i-1,j-1)
     * 2. Use inclusion-exclusion principle: prefixSum[i][j] = matrix[i-1][j-1] + prefixSum[i-1][j] + prefixSum[i][j-1] - prefixSum[i-1][j-1]
     * 3. Pad with extra row/column of zeros for easier boundary handling
     *
     * Time Complexity: O(m*n) for construction
     * Space Complexity: O(m*n) for prefix sum array
     *
     * @param matrix input 2D matrix
     */
    public RangeSumQuery2DImmutable(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        // Create prefix sum array with extra padding
        prefixSum = new int[m + 1][n + 1];

        // Build prefix sum array
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                prefixSum[i][j] = matrix[i-1][j-1] + prefixSum[i-1][j] +
                                 prefixSum[i][j-1] - prefixSum[i-1][j-1];
            }
        }
    }

    /**
     * Returns sum of elements in rectangle from (row1,col1) to (row2,col2) inclusive.
     *
     * Algorithm:
     * 1. Use inclusion-exclusion principle on prefix sum array
     * 2. Sum = prefixSum[row2+1][col2+1] - prefixSum[row1][col2+1] - prefixSum[row2+1][col1] + prefixSum[row1][col1]
     * 3. The +1 offsets account for the extra padding in prefix sum array
     *
     * Time Complexity: O(1) per query
     * Space Complexity: O(1)
     *
     * @param row1 top row of region (inclusive)
     * @param col1 left column of region (inclusive)
     * @param row2 bottom row of region (inclusive)
     * @param col2 right column of region (inclusive)
     * @return sum of elements in specified region
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (prefixSum == null) return 0;

        // Apply inclusion-exclusion principle
        return prefixSum[row2 + 1][col2 + 1] - prefixSum[row1][col2 + 1] -
               prefixSum[row2 + 1][col1] + prefixSum[row1][col1];
    }

    /**
     * Alternative constructor without padding (requires more careful boundary handling)
     */
    public static class RangeSumQuery2DNoPadding {
        private int[][] prefixSum;
        private int m, n;

        public RangeSumQuery2DNoPadding(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return;
            }

            m = matrix.length;
            n = matrix[0].length;
            prefixSum = new int[m][n];

            // First row
            prefixSum[0][0] = matrix[0][0];
            for (int j = 1; j < n; j++) {
                prefixSum[0][j] = prefixSum[0][j-1] + matrix[0][j];
            }

            // First column
            for (int i = 1; i < m; i++) {
                prefixSum[i][0] = prefixSum[i-1][0] + matrix[i][0];
            }

            // Rest of the matrix
            for (int i = 1; i < m; i++) {
                for (int j = 1; j < n; j++) {
                    prefixSum[i][j] = matrix[i][j] + prefixSum[i-1][j] +
                                     prefixSum[i][j-1] - prefixSum[i-1][j-1];
                }
            }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            if (prefixSum == null) return 0;

            int total = prefixSum[row2][col2];

            if (row1 > 0) total -= prefixSum[row1-1][col2];
            if (col1 > 0) total -= prefixSum[row2][col1-1];
            if (row1 > 0 && col1 > 0) total += prefixSum[row1-1][col1-1];

            return total;
        }
    }

    /**
     * Brute force approach for comparison (less efficient)
     * Time Complexity: O(1) construction, O(k) per query where k is region size
     */
    public static class RangeSumQuery2DBruteForce {
        private int[][] matrix;

        public RangeSumQuery2DBruteForce(int[][] matrix) {
            this.matrix = matrix;
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            if (matrix == null || matrix.length == 0) return 0;

            int sum = 0;
            for (int i = row1; i <= row2; i++) {
                for (int j = col1; j <= col2; j++) {
                    sum += matrix[i][j];
                }
            }
            return sum;
        }
    }

    /**
     * Helper method to visualize prefix sum array (for debugging)
     */
    public void printPrefixSum() {
        if (prefixSum == null) {
            System.out.println("Prefix sum array is null");
            return;
        }

        System.out.println("Prefix Sum Array:");
        for (int[] row : prefixSum) {
            System.out.println(Arrays.toString(row));
        }
    }
}

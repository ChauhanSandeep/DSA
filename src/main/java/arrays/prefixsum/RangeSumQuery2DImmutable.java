package arrays.prefixsum;

/**
 * Range Sum Query 2D - Immutable
 *
 * Problem Statement:
 * Given a 2D matrix, handle multiple queries to calculate the sum of elements
 * within any rectangular region defined by upper left corner (row1, col1) and
 * lower right corner (row2, col2). Both corners are inclusive.
 *
 * Example:
 * Input: matrix = [
 *      [3,0,1,4,2],
 *      [5,6,3,2,1],
 *      [1,2,0,1,5],
 *      [4,1,0,1,7],
 *      [1,0,3,0,5]
 * ]
 * sumRegion(2, 1, 4, 3) returns 8
 * Explanation: Sum of elements in rectangle from (2,1) to (4,3) = 2+0+1+1+0+1+0+3+0 = 8
 *
 * LeetCode Link: https://leetcode.com/problems/range-sum-query-2d-immutable
 *
 * Follow-up Questions:
 * 1. What if the matrix is very large but sparse?
 *    Answer: Use HashMap to store only non-zero prefix sums to save space.
 * 2. What if we need to support updates to matrix elements?
 *    Answer: Use 2D Binary Indexed Tree (Fenwick Tree) for O(log m * log n) updates and queries.
 *    Related: https://leetcode.com/problems/range-sum-query-2d-mutable/
 * 3. How would you handle 3D range sum queries?
 *    Answer: Extend prefix sum concept to 3D using inclusion-exclusion principle.
 * 4. Can we optimize space if queries are sparse?
 *    Answer: Lazy evaluation - compute prefix sums on-demand and cache results.
 */
public class RangeSumQuery2DImmutable {

    private final int[][] prefixSum;

    /**
     * Initializes the NumMatrix object with the given 2D matrix.
     *
     * Algorithm: 2D Prefix Sum Array Construction
     * Step 1: Create prefixSum array where prefixSum[i][j] represents sum from (0,0) to (i-1,j-1)
     * Step 2: Use dynamic programming formula:
     *         prefixSum[i+1][j+1] = prefixSum[i][j+1] + prefixSum[i+1][j] - prefixSum[i][j] + matrix[i][j]
     * Step 3: Handle boundary conditions by making prefixSum array one size larger
     *
     * Time Complexity: O(m * n) where m is rows, n is columns
     * Space Complexity: O(m * n) for the prefix sum array
     *
     * @param matrix the input 2D matrix
     */
    public RangeSumQuery2DImmutable(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            prefixSum = new int[1][1];
            return;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Create prefix sum array with one extra row and column for easier boundary handling
        prefixSum = new int[rows + 1][cols + 1];

        // Build prefix sum array using dynamic programming
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                prefixSum[i + 1][j + 1] = prefixSum[i][j + 1]
                    + prefixSum[i + 1][j]
                    - prefixSum[i][j]
                    + matrix[i][j];
            }
        }
    }

    /**
     * Returns the sum of elements in the rectangle from (row1, col1) to (row2, col2) inclusive.
     *
     * Algorithm: Inclusion-Exclusion Principle
     * Step 1: Get total sum from origin to bottom-right corner
     * Step 2: Subtract the region above the target rectangle
     * Step 3: Subtract the region to the left of the target rectangle
     * Step 4: Add back the top-left region that was subtracted twice
     *
     * Formula: sum = prefixSum[row2+1][col2+1] - prefixSum[row1][col2+1]
     *                - prefixSum[row2+1][col1] + prefixSum[row1][col1]
     *
     * Time Complexity: O(1) - constant time lookup
     * Space Complexity: O(1) - no extra space needed
     *
     * @param row1 top boundary row index
     * @param col1 left boundary column index
     * @param row2 bottom boundary row index
     * @param col2 right boundary column index
     * @return sum of elements in the specified rectangle
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        // Handle edge cases
        if (prefixSum.length <= 1 || prefixSum[0].length <= 1) {
            return 0;
        }

        // Apply inclusion-exclusion principle for 2D range sum
        return prefixSum[row2 + 1][col2 + 1]
            - prefixSum[row1][col2 + 1]
            - prefixSum[row2 + 1][col1]
            + prefixSum[row1][col1];
    }

}

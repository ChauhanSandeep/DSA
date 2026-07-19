package arrays.prefixsum;

import java.util.Arrays;

/**
 * Problem: Range Sum Query 2D - Immutable
 *
 * Preprocess a fixed matrix so sumRegion can answer the sum of any inclusive
 * rectangle quickly. Since the matrix never changes, a 2D prefix sum can pay the
 * work once during construction.
 *
 * Leetcode: https://leetcode.com/problems/range-sum-query-2d-immutable/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  2D prefix sum | Inclusion-exclusion | Immutable queries
 *
 * Example:
 *   Input:  sumRegion(2,1,4,3) on the sample matrix
 *   Output: 8
 *   Why:    inclusion-exclusion keeps only the rectangle from rows 2..4 and cols 1..3.
 *
 * Follow-ups:
 *   1. Support updates?
 *      Use a 2D Fenwick tree or 2D segment tree.
 *   2. Many sparse matrices?
 *      Store non-zero values or sparse prefix structures instead of dense arrays.
 *   3. 3D range sum queries?
 *      Extend the prefix-sum table and inclusion-exclusion formula to eight corners.
 *
 * Related: Range Sum Query 2D Mutable (308), Matrix Block Sum (1314).
 */
public class RangeSumQuery2DImmutable {

    public static void main(String[] args) {
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        int[][] queries = { {2, 1, 4, 3}, {1, 1, 2, 2}, {0, 0, 0, 0} };
        int[] expected = { 8, 11, 3 };
        RangeSumQuery2DImmutable solver = new RangeSumQuery2DImmutable(matrix);

        for (int i = 0; i < queries.length; i++) {
            int[] query = queries[i];
            int got = solver.sumRegion(query[0], query[1], query[2], query[3]);
            System.out.printf("query=%s -> output=%d  expected=%d%n",
                Arrays.toString(query), got, expected[i]);
        }
    }


    private final int[][] prefixSum;

    /**
     * Intuition: prefixSum[i + 1][j + 1] stores the sum from the origin through
     * matrix[i][j]. The extra top row and left column turn edge rectangles into the
     * same formula as every other rectangle.
     *
     * Algorithm:
     *   1. Use a 1 x 1 prefix table for null or empty input.
     *   2. Allocate prefixSum with one extra row and column.
     *   3. Fill each prefix cell from top, left, overlap, and matrix value.
     *
     * Time:  O(m * n) - each matrix cell contributes to one prefix cell.
     * Space: O(m * n) - prefixSum stores the precomputed rectangle totals.
     *
     * @param matrix immutable matrix to preprocess
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
     * Intuition: the prefix sum to the bottom-right corner includes the target
     * rectangle plus extra area above and left of it. Subtract those extras, then add
     * the top-left overlap back once.
     *
     * Algorithm:
     *   1. Return 0 for the empty-prefix sentinel.
     *   2. Read the full prefix sum ending at row2, col2.
     *   3. Subtract the area above and the area to the left.
     *   4. Add the top-left overlap that was subtracted twice.
     *
     * Time:  O(1) - every query uses four prefix table reads.
     * Space: O(1) - queries allocate no extra data.
     *
     * @param row1 top row of the query rectangle
     * @param col1 left column of the query rectangle
     * @param row2 bottom row of the query rectangle
     * @param col2 right column of the query rectangle
     * @return sum of the inclusive rectangle
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

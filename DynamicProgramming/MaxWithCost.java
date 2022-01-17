package DynamicProgramming;

/**
 * https://leetcode.com/problems/maximum-number-of-points-with-cost/
 */
public class MaxWithCost {

    public long maxPoints(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        long[] prevRow = new long[cols];

        for (int i = 0; i < cols; ++i) {
            prevRow[i] = matrix[0][i];
        }
        for (int i = 0; i < rows - 1; ++i) {
            long[] left = createLeft(prevRow, cols);
            long[] right = createRight(prevRow, cols);
            long[] currRow = new long[cols];

            for (int j = 0; j < cols; ++j) {
                currRow[j] = matrix[i + 1][j] + Math.max(left[j], right[j]);
            }
            prevRow = currRow;
        }

        long result = 0;
        for (int i = 0; i < cols; ++i) {
            result = Math.max(result, prevRow[i]);
        }
        return result;
    }

    private long[] createRight(long[] prevRow, int cols) {
        long[] right = new long[cols];
        right[cols - 1] = prevRow[cols - 1];

        for (int j = cols - 2; j >= 0; --j) {
            right[j] = Math.max(right[j + 1] - 1, prevRow[j]);
        }
        return right;
    }

    private long[] createLeft(long[] prevRow, int cols) {
        long[] left = new long[cols];
        left[0] = prevRow[0];
        for (int j = 1; j < cols; ++j) {
            left[j] = Math.max(left[j - 1] - 1, prevRow[j]);
        }
        return left;
    }
}

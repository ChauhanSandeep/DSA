package DynamicProgramming;

/**
 * Problem: Maximum Number of Points with Cost
 * LeetCode: https://leetcode.com/problems/maximum-number-of-points-with-cost/
 *
 * Approach:
 * - This is a dynamic programming problem where we aim to find the maximum points we can collect
 *   while considering the cost of moving between columns.
 * - We use a bottom-up DP approach, updating row-by-row while maintaining an optimized space usage.
 * - Two auxiliary arrays (`leftMax` and `rightMax`) help efficiently compute the maximum values
 *   while traversing left-to-right and right-to-left.
 *
 * Time Complexity: O(rows * cols) - We process each element twice per row.
 * Space Complexity: O(cols) - We store only the previous row's results.
 */
public class MaxWithCost {

    public long maxPoints(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Stores the maximum points that can be achieved at each column in the previous row
        long[] previousRowPoints = new long[cols];

        // Initialize with first row values
        for (int col = 0; col < cols; col++) {
            previousRowPoints[col] = matrix[0][col];
        }

        // Iterate through each row, updating the max points for the next row
        for (int row = 1; row < rows; row++) {
            long[] leftMax = computeLeftMax(previousRowPoints, cols);
            long[] rightMax = computeRightMax(previousRowPoints, cols);
            long[] currentRowPoints = new long[cols];

            // Compute max points for the current row using left and right max arrays
            for (int col = 0; col < cols; col++) {
                currentRowPoints[col] = matrix[row][col] + Math.max(leftMax[col], rightMax[col]);
            }

            // Update the previous row reference for the next iteration
            previousRowPoints = currentRowPoints;
        }

        // The maximum value from the last computed row gives the answer
        long maxPoints = 0;
        for (long points : previousRowPoints) {
            maxPoints = Math.max(maxPoints, points);
        }
        return maxPoints;
    }

    /**
     * Computes the maximum achievable points when moving from left to right.
     */
    private long[] computeLeftMax(long[] previousRowPoints, int cols) {
        long[] leftMax = new long[cols];
        leftMax[0] = previousRowPoints[0];

        for (int col = 1; col < cols; col++) {
            leftMax[col] = Math.max(leftMax[col - 1] - 1, previousRowPoints[col]);
        }
        return leftMax;
    }

    /**
     * Computes the maximum achievable points when moving from right to left.
     */
    private long[] computeRightMax(long[] previousRowPoints, int cols) {
        long[] rightMax = new long[cols];
        rightMax[cols - 1] = previousRowPoints[cols - 1];

        for (int col = cols - 2; col >= 0; col--) {
            rightMax[col] = Math.max(rightMax[col + 1] - 1, previousRowPoints[col]);
        }
        return rightMax;
    }
}

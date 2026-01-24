package dynamicprogramming.MiscellaneousDP;

/**
 * LeetCode: https://leetcode.com/problems/maximum-number-of-points-with-cost/
 *
 * Problem Statement:
 * You are given a 2D integer matrix `points` of size `m x n`.
 * Each cell contains some points. You start from any cell in the first row
 * and move down row by row. From a cell `(i, j)`, you can move to any cell `(i+1, k)`
 * in the next row but you incur a cost of `abs(j - k)`.
 *
 * Your goal is to return the maximum total points you can collect.
 *
 * Example:
 * Input: points = [
*       [1,2,3],
*       [1,5,1],
*       [3,1,1]
 * ]
 * Output: 9
 * Explanation: Start from points[0][2] = 3 → points[1][1] = 5 → points[2][0] = 3
 * Total = 3 + 5 + 3 - (2) (minus cost) = 9
 *
 * Follow-up Questions:
 * Q: Can this be done in-place with O(1) extra space?
 * A: Not without altering input or compromising performance. Right now O(n) is optimal.
 *
 * Q: Can this be done using segment tree?
 * A: Yes, but would be overkill as current approach is already linear per row.
 */
public class MaxWithCost {

  /**
   * Calculates the maximum total points one can collect row by row,
   * factoring in the cost of jumping between columns.
   *
   * Approach:
   * - Use **Dynamic Programming** with a 2D DP table where `dp[i][j]` holds the max points at row `i`, col `j`.
   * - For each cell, calculate the maximum points from the previous row considering the jump cost.
   *
   * Time Complexity: O(m * n^2) — m rows and n columns with nested loops.
   * Space Complexity: O(m * n) — for the DP table.
   */
  public long maxPointsNaive(int[][] points) {
    int rows = points.length;
    int cols = points[0].length;

    long[][] dp = new long[rows][cols];

    // Initialize with the first row values
    for (int col = 0; col < cols; col++) {
      dp[0][col] = points[0][col];
    }

    // Fill the DP table row by row
    for (int row = 1; row < rows; row++) {
      for (int currCol = 0; currCol < cols; currCol++) {
        long maxVal = 0;
        for (int prevCol = 0; prevCol < cols; prevCol++) {
          long candidate = dp[row - 1][prevCol] - Math.abs(currCol - prevCol);
          maxVal = Math.max(maxVal, candidate);
        }
        dp[row][currCol] = points[row][currCol] + maxVal;
      }
    }

    // Find the maximum in the last row
    long maxPoints = 0;
    for (int col = 0; col < cols; col++) {
      maxPoints = Math.max(maxPoints, dp[rows - 1][col]);
    }

    return maxPoints;
  }

  /**  Optimized approach using linear sweeps to avoid O(n²) complexity.
   *
   * Naively comparing all `k` leads to O(n²) time per row.
   * Instead, we precompute:
   * - `leftMax[col]`: The best possible value when coming from the left side of col,
   *   accounting for the cost of moving right (−1 per step).
   * - `rightMax[col]`: The best possible value when coming from the right side of col,
   *   accounting for the cost of moving left (−1 per step).
   *
   * Transformation Logic:
   * - Moving right from col `k` to col `col` reduces the value by (col - k),
   *   which can be simulated in left-to-right sweep:
   *     leftMax[col] = max(prevRowMaxPoints[col], leftMax[col - 1] - 1)
   *     [ This means that we can either take the max from previous row at the same column
   *                                      OR
   *     we can take the value from left column of same row and reduce it by 1 ]
   *
   * - Moving left from col `k` to col `col` reduces the value by (k - col),
   *   simulated in right-to-left sweep:
   *     rightMax[col] = max(rightMax[col + 1] - 1, prevRowMaxPoints[col])
   *
   * At each column, the best transition from the previous row is:
   *     max(leftMax[col], rightMax[col])
   * Then add the current cell’s point value: `points[row][col]`
   *
   * Time Complexity: O(rows × cols)
   * Space Complexity: O(cols)
   */
  public long maxPointsOptimized(int[][] points) {
    int rows = points.length;
    int cols = points[0].length;

    long[] prevRowMaxPoints = new long[cols];

    // Initialize with the first row's points
    for (int col = 0; col < cols; col++) {
      prevRowMaxPoints[col] = points[0][col];
    }

    // Process each row from second to last
    for (int row = 1; row < rows; row++) {
      long[] leftMax = computeLeftMax(prevRowMaxPoints);
      long[] rightMax = computeRightMax(prevRowMaxPoints);
      long[] currRowMaxPoints = new long[cols];

      for (int col = 0; col < cols; col++) {
        // At each cell, choose the best from left or right while adding current cell value
        currRowMaxPoints[col] = points[row][col] + Math.max(leftMax[col], rightMax[col]);
      }

      // Update the previous row to current for next iteration
      prevRowMaxPoints = currRowMaxPoints;
    }

    // Final result is the max value in the last row
    long maxPoints = 0;
    for (long pointsAtCol : prevRowMaxPoints) {
      maxPoints = Math.max(maxPoints, pointsAtCol);
    }
    return maxPoints;
  }

  /**
   * Computes maximum possible values when moving left to right,
   * deducting 1 cost per rightward movement.
   *
   * @param prevRowPoints previous row's max point values
   * @return left-to-right max values
   */
  private long[] computeLeftMax(long[] prevRowPoints) {
    int length = prevRowPoints.length;
    long[] leftMax = new long[length];
    leftMax[0] = prevRowPoints[0];

    for (int col = 1; col < length; col++) {
      leftMax[col] = Math.max(leftMax[col - 1] - 1, prevRowPoints[col]);
    }
    return leftMax;
  }

  /**
   * Computes maximum possible values when moving right to left,
   * deducting 1 cost per leftward movement.
   *
   * @param prevRowPoints previous row's max point values
   * @return right-to-left max values
   */
  private long[] computeRightMax(long[] prevRowPoints) {
    int n = prevRowPoints.length;
    long[] rightMax = new long[n];
    rightMax[n - 1] = prevRowPoints[n - 1];

    for (int col = n - 2; col >= 0; col--) {
      rightMax[col] = Math.max(rightMax[col + 1] - 1, prevRowPoints[col]);
    }
    return rightMax;
  }
}
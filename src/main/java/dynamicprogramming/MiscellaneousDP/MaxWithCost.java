package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;
/**
 * Problem: Maximum Number of Points With Cost
 *
 * Choose one cell in each row of a points matrix. Moving from column c in one row
 * to column d in the next loses abs(c - d) points. Return the maximum total score.
 *
 * Leetcode: https://leetcode.com/problems/maximum-number-of-points-with-cost/
 * Rating:   2106 (zerotrac Elo)
 * Pattern:  Dynamic programming | Row DP | Left/right sweep optimization
 *
 * Example:
 *   Input:  points = [[1,2,3],[1,5,1],[3,1,1]]
 *   Output: 9
 *   Why:    choosing columns 2 -> 1 -> 0 scores 3 + 5 + 3 minus movement cost 2, for 9.
 *
 * Follow-ups:
 *   1. What if the movement cost is squared distance?
 *      The left/right linear sweep no longer works; use convex hull trick or divide-and-conquer optimization if applicable.
 *   2. Return the chosen path of columns?
 *      Store parent columns while computing each row's best transition.
 *   3. Can the input be updated online?
 *      Maintain row transitions with segment trees, though full recomputation is often simpler.
 *
 * Related: Minimum Falling Path Sum (931), Paint House II (265).
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

  /**
     * Intuition (interview default): for a target column, the best previous column
     * is either on its left or on its right. A left-to-right sweep carries the best
     * value that can arrive from the left, losing one point per step as it moves
     * right. A right-to-left sweep does the symmetric work. Taking the max of those
     * two precomputed values gives the same recurrence as the naive scan, but each
     * row is only linear.
     *
     * Algorithm:
     *   1. Initialize prevRowMaxPoints from the first row.
     *   2. For each next row, compute leftMax and rightMax sweeps from the previous row.
     *   3. Set currRowMaxPoints[col] to points[row][col] plus the better sweep value.
     *   4. Replace the previous row and return its maximum after the last row.
     *
     * Time:  O(m*n) - each row performs three linear passes over the columns.
     * Space: O(n) - only previous row and two sweep arrays are stored.
     *
     * @param points matrix of point values
     * @return maximum score after choosing one cell per row
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

    public static void main(String[] args) {
        MaxWithCost solver = new MaxWithCost();
        int[][][] inputs = {
            {{5}},
            {{1, 2, 3}, {1, 5, 1}, {3, 1, 1}}
        };
        long[] expected = {5, 9};

        for (int i = 0; i < inputs.length; i++) {
            long got = solver.maxPointsOptimized(inputs[i]);
            System.out.printf("points=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }
}

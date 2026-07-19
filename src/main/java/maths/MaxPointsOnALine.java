package maths;

import java.util.*;


/**
 * Problem: Max Points on a Line
 *
 * Given points on a 2D plane, return the largest number of points that lie on
 * one straight line. A line can be identified from an anchor point by its
 * reduced slope to every other point.
 *
 * Leetcode: https://leetcode.com/problems/max-points-on-a-line/ (Hard)
 * Rating:   acceptance 31.1% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Geometry | GCD-normalized slopes
 *
 * Example:
 *   Input:  points = [[1,1],[2,2],[3,3]]
 *   Output: 3
 *   Why:    all three points have the same slope from any endpoint.
 *
 * Follow-ups:
 *   1. How do you avoid floating-point precision bugs?
 *      Store each slope as a reduced dy/dx pair using gcd normalization.
 *   2. How would you return the actual points on the best line?
 *      Keep the point indices for the slope bucket that produces the maximum.
 *   3. How would this extend to 3D collinearity?
 *      Normalize direction vectors (dx, dy, dz) from each anchor instead of 2D slopes.
 *
 * Related: Erect the Fence (587), Check If It Is a Straight Line (1232).
 */

public class MaxPointsOnALine {

  public static void main(String[] args) {
    MaxPointsOnALine solver = new MaxPointsOnALine();
    int[][][] inputs = {
        { {1, 1}, {2, 2}, {3, 3} },
        { {1, 1}, {3, 2}, {5, 3}, {4, 1}, {2, 3}, {1, 4} },
        { {0, 0} }
    };
    int[] expected = { 3, 4, 1 };

    for (int i = 0; i < inputs.length; i++) {
      int got = solver.maxPoints(inputs[i]);
      System.out.printf("points=%s -> %d  expected=%d%n",
          Arrays.deepToString(inputs[i]), got, expected[i]);
    }
  }


    /**
   * Intuition: fix one anchor point and group every later point by the line it
   * forms with that anchor. Two points are on the same anchor line exactly when
   * their reduced dy/dx slope is the same, so a map from slope string to count
   * gives the best line through that anchor.
   *
   * Algorithm:
   *   1. Return the point count directly for arrays of size 2 or less.
   *   2. For each anchorIndex, build a fresh slopeMap for later points.
   *   3. Reduce dy and dx by their greatest common divisor.
   *   4. Normalize the sign and vertical-line representation.
   *   5. Update the global maximum with the largest bucket plus the anchor.
   *
   * Time:  O(n^2) - every pair of points is considered once.
   * Space: O(n) - one slope map is stored for a single anchor at a time.
   *
   * @param points array of [x, y] coordinates
   * @return maximum number of collinear points
   */

  public int maxPoints(int[][] points) {
    if (points.length <= 2) {
      return points.length;
    }

    int maxCount = 0;

    for (int anchorIndex = 0; anchorIndex < points.length; anchorIndex++) {
      Map<String, Integer> slopeMap = new HashMap<>(); // <slope, count>
      int currentMax = 0;

      for (int otherIndex = anchorIndex + 1; otherIndex < points.length; otherIndex++) {
        int dx = points[otherIndex][0] - points[anchorIndex][0];
        int dy = points[otherIndex][1] - points[anchorIndex][1];

        int greatestCommonDivisor = getGreatestCommonDivisor(dx, dy);
        dy /= greatestCommonDivisor;
        dx /= greatestCommonDivisor;

        if (dx < 0) {
          // Ensure that the negative sign is always in the numerator
          dx = -dx;
          dy = -dy;
        } else if (dx == 0) {
          // Ensure vertical lines have consistent representation
          dy = Math.abs(dy);
        }

        String slope = dy + "/" + dx;
        slopeMap.put(slope, slopeMap.getOrDefault(slope, 0) + 1);
        currentMax = Math.max(currentMax, slopeMap.get(slope));
      }

      maxCount = Math.max(maxCount, currentMax + 1);
    }

    return maxCount;
  }

    /** Computes the positive greatest common divisor with the Euclidean algorithm. */

  private int getGreatestCommonDivisor(int firstNumber, int secondNumber) {
    // Handle negative inputs — GCD is always positive
    firstNumber = Math.abs(firstNumber);
    secondNumber = Math.abs(secondNumber);

    // Euclidean algorithm
    while (secondNumber != 0) {
      int remainder = firstNumber % secondNumber;
      firstNumber = secondNumber;
      secondNumber = remainder;
    }

    return firstNumber;
  }

  /**
   * Alternative approach: Finds maximum number of points on the same line using double precision slopes.
   *
   * This method uses floating-point arithmetic to calculate slopes, which is simpler but potentially
   * less precise than the fraction-based approach in the main maxPoints() method.
   *
   * Algorithm:
   * 1. For each point as anchor, calculate slopes to all other points using doubles
   * 2. Use HashMap to count occurrences of each slope from the anchor
   * 3. Handle duplicate points separately by counting them
   * 4. Handle vertical lines using Double.MAX_VALUE as slope representation
   * 5. Track maximum count across all anchor points
   *
   * Key differences from main approach:
   * - Uses double precision for slopes (potential floating point precision issues)
   * - Handles duplicate points explicitly by counting them separately
   * - Simpler slope calculation but less robust for edge cases
   *
   * Time Complexity: O(N^2) where N is the number of points. For each of N points,
   * we calculate slopes to all other points.
   *
   * Space Complexity: O(N) for the HashMap storing slopes from each anchor point.
   *
   * Note: This approach may have precision issues with very close slopes due to floating point arithmetic.
   * For production code, prefer the fraction-based approach in maxPoints().
   *
   * @param points array of 2D points
   * @return maximum number of points on the same line
   */
  public int maxPointsUsingSlopeCalculation(int[][] points) {
    int totalPoints = points.length;
    if (totalPoints < 2) {
      return totalPoints;
    }

    int globalMaxPoints = 1; // At least one point can always form a "line"

    // Try each point as an anchor point
    for (int anchorIndex = 0; anchorIndex < totalPoints; anchorIndex++) {
      int[] anchorPoint = points[anchorIndex];

      Map<Double, Integer> slopeToCountMap = new HashMap<>();
      int duplicatePointsCount = 0;

      // Calculate slopes from anchor to all subsequent points
      for (int currentIndex = anchorIndex + 1; currentIndex < totalPoints; currentIndex++) {
        int[] currentPoint = points[currentIndex];

        if (arePointsIdentical(anchorPoint, currentPoint)) {
          duplicatePointsCount++;
          continue; // Skip duplicate points
        }
        double slopeValue = calculateSlope(anchorPoint, currentPoint);
        slopeToCountMap.put(slopeValue, slopeToCountMap.getOrDefault(slopeValue, 0) + 1);
      }

      // Find the maximum number of points on any single line through this anchor
      int maxPointsOnLineFromAnchor = 0;
      for (int pointsOnLine : slopeToCountMap.values()) {
        maxPointsOnLineFromAnchor = Math.max(maxPointsOnLineFromAnchor, pointsOnLine);
      }

      // Total points on best line = anchor point + duplicate points + max points on any line
      int totalPointsOnBestLine = 1 + duplicatePointsCount + maxPointsOnLineFromAnchor;
      globalMaxPoints = Math.max(globalMaxPoints, totalPointsOnBestLine);
    }

    return globalMaxPoints;
  }

    /** Returns true when two points have the same coordinates. */

  private boolean arePointsIdentical(int[] firstPoint, int[] secondPoint) {
    return firstPoint[0] == secondPoint[0] && firstPoint[1] == secondPoint[1];
  }

    /** Calculates the floating-point slope used by the alternative solution. */

  private double calculateSlope(int[] firstPoint, int[] secondPoint) {
    int deltaX = secondPoint[0] - firstPoint[0];
    int deltaY = secondPoint[1] - firstPoint[1];

    if (deltaX == 0) {
      return Double.MAX_VALUE; // Vertical line (infinite slope)
    }
    if (deltaY == 0) {
      return 0.0; // Horizontal line (zero slope)
    }

    return (double) deltaY / (double) deltaX;
  }
}
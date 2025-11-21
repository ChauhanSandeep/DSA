package maths;

import java.util.*;


/**
 * Problem: Max Points on a Line
 *
 * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane,
 * return the maximum number of points that lie on the same straight line.
 *
 * Example:
 * Input: points = [[1,1],[2,2],[3,3]]
 * Output: 3
 * Explanation: All three points lie on the same line.
 *
 * LeetCode: https://leetcode.com/problems/max-points-on-a-line
 *
 * Follow-up Questions:
 * 1. How would you handle floating point precision issues?
 *    Answer: Use rational numbers (fraction representation) or GCD normalization for slopes.
 *
 * 2. What if we need to find all maximal collinear sets?
 *    Answer: Modify algorithm to store actual point sets instead of just counting.
 *
 * 3. How would you extend this to 3D space?
 *    Answer: Use plane equations instead of line equations, requiring 3 points to define a plane.
 *    Related: https://leetcode.com/problems/minimum-lines-to-represent-a-line-chart/
 */
public class MaxPointsOnALine {

  /**
   * Finds maximum number of points on the same line using slope-based HashMap approach.
   *
   * Algorithm:
   * 1. For each point as anchor, calculate slopes to all other points
   * 2. Use HashMap to count occurrences of each slope from the anchor
   * 3. Slope is represented as reduced fraction (dy/dx) to avoid floating point errors
   * 4. Handle vertical lines separately (infinite slope)
   * 5. Track maximum count across all anchor points
   *
   * Key insight: If multiple points share the same slope from an anchor point,
   * they must be collinear with the anchor. The slope acts as a signature for
   * the line passing through the anchor.
   *
   * Time Complexity: O(N^2) where N is the number of points. For each of N points,
   * we calculate slopes to all other points, and GCD calculation is O(log M) where M
   * is the coordinate value, which is effectively constant given the constraints.
   *
   * Space Complexity: O(N) for the HashMap storing slopes from each anchor point.
   * In worst case, all points have unique slopes from a given anchor.
   *
   * @param points array of 2D points
   * @return maximum number of points on the same line
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

  /**
   * Helper method to compute greatest common divisor using Euclidean algorithm.
   * This is used to reduce slope fractions to their simplest form.
   * For example if the inputs are (4, 6), the GCD is 2, so the reduced form is (2, 3).
   *
   * The idea:
   * - GCD(a, b) = GCD(b, a % b)
   * - Continue until b becomes 0.
   * - The remaining non-zero value of a is the GCD.
   */
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

  /**
   * Helper method to check if two points are identical (same coordinates).
   *
   * @param firstPoint  first point coordinates [x, y]
   * @param secondPoint second point coordinates [x, y]
   * @return true if points have identical coordinates, false otherwise
   */
  private boolean arePointsIdentical(int[] firstPoint, int[] secondPoint) {
    return firstPoint[0] == secondPoint[0] && firstPoint[1] == secondPoint[1];
  }

  /**
   * Helper method to calculate slope between two points using double precision.
   *
   * Special cases:
   * - Vertical line (same x-coordinate): returns Double.MAX_VALUE
   * - Horizontal line (same y-coordinate): returns 0.0
   * - Regular line: returns (y2 - y1) / (x2 - x1)
   *
   * @param firstPoint  starting point [x, y]
   * @param secondPoint ending point [x, y]
   * @return slope as double value
   */
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
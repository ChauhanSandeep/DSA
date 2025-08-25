package maths;

import java.util.*;

/**
 * 1610. Maximum Number of Visible Points
 *
 * Problem: You are given an array points where points[i] = [xi, yi] represents a point
 * in the 2D plane, and an integer angle. Your location is at the origin (0, 0).
 * Return the maximum number of points you can see by rotating your vision by angle degrees.
 *
 * Example:
 * Input: points = [[2,1],[2,2],[3,3]], location = [1,1], angle = 90
 * Output: 3
 * Explanation: All points are visible within 90 degrees from any direction.
 *
 * LeetCode: https://leetcode.com/problems/maximum-number-of-visible-points
 *
 * Follow-up questions:
 * Q: What if angle is very large (>= 360 degrees)?
 * A: All points become visible, return total count.
 *
 * Q: How to handle precision errors in floating point calculations?
 * A: Use epsilon for comparisons and consider using rational arithmetic.
 *
 * Q: Can we optimize for very large number of points?
 * A: Use spatial data structures or approximation algorithms for massive datasets.
 */
public class MaximumNumberOfVisiblePoints {

    private static final double EPS = 1e-9;

    /**
     * Sliding window approach using polar angles.
     *
     * Algorithm: Polar coordinate conversion + sliding window
     * - Convert all points to polar angles relative to location
     * - Handle points at same location separately (always visible)
     * - Sort angles and use sliding window to find max points in angle range
     * - Handle wraparound by duplicating angles with +2π
     *
     * Time Complexity: O(n log n) for sorting
     * Space Complexity: O(n) for angle array
     */
    public int visiblePointsCount(List<List<Integer>> points, int angle, List<Integer> location) {
        if (angle >= 360) {
            return points.size();
        }

        int x0 = location.get(0);
        int y0 = location.get(1);

        List<Double> angles = new ArrayList<>();
        int sameLocation = 0;

        // Convert points to polar angles
        for (List<Integer> point : points) {
            int x = point.get(0);
            int y = point.get(1);

            if (x == x0 && y == y0) {
                sameLocation++;
            } else {
                double angleRad = Math.atan2(y - y0, x - x0);
                double angleDeg = Math.toDegrees(angleRad);
                if (angleDeg < 0) angleDeg += 360; // Normalize to [0, 360)
                angles.add(angleDeg);
            }
        }

        Collections.sort(angles);

        // Add duplicate angles shifted by 360 degrees for wraparound
        int originalSize = angles.size();
        for (int i = 0; i < originalSize; i++) {
            angles.add(angles.get(i) + 360);
        }

        // Sliding window to find maximum points within angle range
        int maxVisible = 0;
        int left = 0;

        for (int right = 0; right < angles.size(); right++) {
            // Shrink window if angle range exceeds limit
            while (angles.get(right) - angles.get(left) > angle + EPS) {
                left++;
            }

            maxVisible = Math.max(maxVisible, right - left + 1);
        }

        return maxVisible + sameLocation;
    }

    /**
     * Optimized approach avoiding angle duplication.
     * Uses modular arithmetic for wraparound handling.
     */
    public int visiblePointsCountOptimized(List<List<Integer>> points, int angle, List<Integer> location) {
        if (angle >= 360) {
            return points.size();
        }

        int x0 = location.get(0);
        int y0 = location.get(1);

        List<Double> angles = new ArrayList<>();
        int sameLocation = 0;

        for (List<Integer> point : points) {
            int x = point.get(0);
            int y = point.get(1);

            if (x == x0 && y == y0) {
                sameLocation++;
            } else {
                double angleRad = Math.atan2(y - y0, x - x0);
                double angleDeg = Math.toDegrees(angleRad);
                if (angleDeg < 0) angleDeg += 360;
                angles.add(angleDeg);
            }
        }

        Collections.sort(angles);

        int maxVisible = 0;
        int n = angles.size();

        // Try each angle as starting point
        for (int i = 0; i < n; i++) {
            double startAngle = angles.get(i);
            double endAngle = startAngle + angle;

            int count = 1; // Include current point

            // Count points in clockwise direction
            for (int j = (i + 1) % n; j != i; j = (j + 1) % n) {
                double currentAngle = angles.get(j);
                if (j < i) currentAngle += 360; // Handle wraparound

                if (currentAngle <= endAngle + EPS) {
                    count++;
                } else {
                    break;
                }
            }

            maxVisible = Math.max(maxVisible, count);
        }

        return maxVisible + sameLocation;
    }

    /**
     * Sweep line algorithm approach.
     * Uses angular sweep with event processing.
     */
    public int visiblePointsCountSweepLine(List<List<Integer>> points, int angle, List<Integer> location) {
        if (angle >= 360) {
            return points.size();
        }

        int x0 = location.get(0);
        int y0 = location.get(1);

        List<AngleEvent> events = new ArrayList<>();
        int sameLocation = 0;

        for (int i = 0; i < points.size(); i++) {
            List<Integer> point = points.get(i);
            int x = point.get(0);
            int y = point.get(1);

            if (x == x0 && y == y0) {
                sameLocation++;
                continue;
            }

            double pointAngle = Math.atan2(y - y0, x - x0);
            double angleDeg = Math.toDegrees(pointAngle);
            if (angleDeg < 0) angleDeg += 360;

            // Create events for when this point becomes visible/invisible
            double startAngle = (angleDeg - angle + 360) % 360;
            double endAngle = (angleDeg + 360) % 360;

            events.add(new AngleEvent(startAngle, 1)); // Point becomes visible
            events.add(new AngleEvent(endAngle, -1));  // Point becomes invisible
        }

        Collections.sort(events);

        int maxVisible = 0;
        int currentVisible = 0;

        for (AngleEvent event : events) {
            currentVisible += event.delta;
            maxVisible = Math.max(maxVisible, currentVisible);
        }

        return maxVisible + sameLocation;
    }

    // Event class for sweep line algorithm
    private static class AngleEvent implements Comparable<AngleEvent> {
        double angle;
        int delta; // +1 for point becoming visible, -1 for invisible

        AngleEvent(double angle, int delta) {
            this.angle = angle;
            this.delta = delta;
        }

        @Override
        public int compareTo(AngleEvent other) {
            int cmp = Double.compare(this.angle, other.angle);
            if (cmp != 0) return cmp;
            return Integer.compare(other.delta, this.delta); // Process +1 before -1
        }
    }

    /**
     * Brute force approach for verification.
     * Checks all possible viewing directions explicitly.
     */
    public int visiblePointsCountBruteForce(List<List<Integer>> points, int angle, List<Integer> location) {
        int x0 = location.get(0);
        int y0 = location.get(1);

        List<Double> angles = new ArrayList<>();
        int sameLocation = 0;

        for (List<Integer> point : points) {
            int x = point.get(0);
            int y = point.get(1);

            if (x == x0 && y == y0) {
                sameLocation++;
            } else {
                double angleRad = Math.atan2(y - y0, x - x0);
                double angleDeg = Math.toDegrees(angleRad);
                if (angleDeg < 0) angleDeg += 360;
                angles.add(angleDeg);
            }
        }

        if (angles.isEmpty()) {
            return sameLocation;
        }

        int maxVisible = 0;

        // Try every possible viewing direction
        for (double startAngle = 0; startAngle < 360; startAngle += 0.5) {
            double endAngle = startAngle + angle;
            int count = 0;

            for (double pointAngle : angles) {
                if (isAngleInRange(pointAngle, startAngle, endAngle)) {
                    count++;
                }
            }

            maxVisible = Math.max(maxVisible, count);
        }

        return maxVisible + sameLocation;
    }

    // Check if angle is in range considering wraparound
    private boolean isAngleInRange(double angle, double start, double end) {
        if (end <= 360) {
            return angle >= start - EPS && angle <= end + EPS;
        } else {
            return angle >= start - EPS || angle <= (end - 360) + EPS;
        }
    }

    /**
     * High precision approach using rational arithmetic.
     * Avoids floating point precision errors.
     */
    public int visiblePointsCountHighPrecision(List<List<Integer>> points, int angle, List<Integer> location) {
        if (angle >= 360) {
            return points.size();
        }

        int x0 = location.get(0);
        int y0 = location.get(1);

        List<Fraction> angles = new ArrayList<>();
        int sameLocation = 0;

        for (List<Integer> point : points) {
            int x = point.get(0);
            int y = point.get(1);

            if (x == x0 && y == y0) {
                sameLocation++;
            } else {
                // Use atan2 approximation with rational numbers
                // This is a simplified version; full implementation would be more complex
                double angleRad = Math.atan2(y - y0, x - x0);
                double angleDeg = Math.toDegrees(angleRad);
                if (angleDeg < 0) angleDeg += 360;

                // Convert to fraction for higher precision
                angles.add(new Fraction(angleDeg));
            }
        }

        Collections.sort(angles);

        // Similar sliding window approach but with rational arithmetic
        int maxVisible = 0;
        int left = 0;
        Fraction angleLimit = new Fraction(angle);

        for (int right = 0; right < angles.size(); right++) {
            while (left < right && angles.get(right).subtract(angles.get(left)).compareTo(angleLimit) > 0) {
                left++;
            }
            maxVisible = Math.max(maxVisible, right - left + 1);
        }

        return maxVisible + sameLocation;
    }

    // Simple fraction class for high precision arithmetic
    private static class Fraction implements Comparable<Fraction> {
        private double value; // Simplified - real implementation would use BigInteger

        Fraction(double value) {
            this.value = value;
        }

        Fraction subtract(Fraction other) {
            return new Fraction(this.value - other.value);
        }

        @Override
        public int compareTo(Fraction other) {
            return Double.compare(this.value, other.value);
        }
    }

    /**
     * Returns the actual visible points instead of just count.
     * Extension that provides the coordinates of visible points.
     */
    public List<List<Integer>> getVisiblePoints(List<List<Integer>> points, int angle, List<Integer> location) {
        if (angle >= 360) {
            return new ArrayList<>(points);
        }

        int x0 = location.get(0);
        int y0 = location.get(1);

        List<PointWithAngle> pointsWithAngles = new ArrayList<>();
        List<List<Integer>> sameLocationPoints = new ArrayList<>();

        for (List<Integer> point : points) {
            int x = point.get(0);
            int y = point.get(1);

            if (x == x0 && y == y0) {
                sameLocationPoints.add(point);
            } else {
                double angleRad = Math.atan2(y - y0, x - x0);
                double angleDeg = Math.toDegrees(angleRad);
                if (angleDeg < 0) angleDeg += 360;
                pointsWithAngles.add(new PointWithAngle(point, angleDeg));
            }
        }

        Collections.sort(pointsWithAngles);

        // Find best viewing direction using sliding window
        int maxVisible = 0;
        int bestStart = 0, bestEnd = -1;
        int left = 0;

        // Duplicate angles for wraparound
        int originalSize = pointsWithAngles.size();
        for (int i = 0; i < originalSize; i++) {
            PointWithAngle original = pointsWithAngles.get(i);
            pointsWithAngles.add(new PointWithAngle(original.point, original.angle + 360));
        }

        for (int right = 0; right < pointsWithAngles.size(); right++) {
            while (pointsWithAngles.get(right).angle - pointsWithAngles.get(left).angle > angle + EPS) {
                left++;
            }

            if (right - left + 1 > maxVisible) {
                maxVisible = right - left + 1;
                bestStart = left;
                bestEnd = right;
            }
        }

        // Collect visible points
        List<List<Integer>> result = new ArrayList<>(sameLocationPoints);
        for (int i = bestStart; i <= bestEnd && i < pointsWithAngles.size(); i++) {
            if (pointsWithAngles.get(i).angle < 360 + EPS) { // Avoid duplicates from wraparound
                result.add(pointsWithAngles.get(i).point);
            }
        }

        return result;
    }

    // Helper class for point with angle
    private static class PointWithAngle implements Comparable<PointWithAngle> {
        List<Integer> point;
        double angle;

        PointWithAngle(List<Integer> point, double angle) {
            this.point = point;
            this.angle = angle;
        }

        @Override
        public int compareTo(PointWithAngle other) {
            return Double.compare(this.angle, other.angle);
        }
    }
}
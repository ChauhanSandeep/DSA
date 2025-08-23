package com.sandeep.frazsheet.math;

import java.util.HashMap;
import java.util.Map;

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
 * 
 * @author Sandeep
 */
public class MaxPointsOnALine {
    
    /**
     * Finds maximum points on a line using slope-based grouping with GCD normalization.
     * 
     * Algorithm:
     * 1. For each point as base, calculate slopes to all other points
     * 2. Use rational representation of slopes to avoid floating point issues
     * 3. Group points by normalized slope and count maximum group size
     * 4. Handle special cases: vertical lines, duplicate points
     * 5. Return maximum count across all base points
     * 
     * Time Complexity: O(n²) where n is number of points
     * Space Complexity: O(n) for slope map in worst case
     * 
     * @param points Array of points [x, y]
     * @return Maximum number of points on same line
     */
    public int maxPointsOnLine(int[][] points) {
        if (points == null || points.length == 0) return 0;
        if (points.length <= 2) return points.length;
        
        int maxPoints = 2; // At least 2 points can always form a line
        
        // Try each point as base point
        for (int i = 0; i < points.length; i++) {
            Map<String, Integer> slopeMap = new HashMap<>();
            int duplicates = 0;
            int verticalPoints = 0;
            int currentMax = 0;
            
            // Compare with all other points
            for (int j = i + 1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                
                if (dx == 0 && dy == 0) {
                    // Duplicate point
                    duplicates++;
                } else if (dx == 0) {
                    // Vertical line
                    verticalPoints++;
                    currentMax = Math.max(currentMax, verticalPoints);
                } else {
                    // Calculate normalized slope
                    String slope = getNormalizedSlope(dy, dx);
                    slopeMap.put(slope, slopeMap.getOrDefault(slope, 0) + 1);
                    currentMax = Math.max(currentMax, slopeMap.get(slope));
                }
            }
            
            // Add base point and duplicates to the maximum count
            maxPoints = Math.max(maxPoints, currentMax + duplicates + 1);
        }
        
        return maxPoints;
    }
    
    /**
     * Alternative implementation using double slopes with epsilon comparison.
     * Less precise but more intuitive.
     * 
     * Time Complexity: O(n²)
     * Space Complexity: O(n)
     */
    public int maxPointsOnLineDouble(int[][] points) {
        if (points.length <= 2) return points.length;
        
        int maxPoints = 2;
        final double EPS = 1e-9;
        
        for (int i = 0; i < points.length; i++) {
            Map<Double, Integer> slopeCount = new HashMap<>();
            int duplicates = 0;
            int verticalCount = 0;
            int localMax = 0;
            
            for (int j = i + 1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                
                if (dx == 0 && dy == 0) {
                    duplicates++;
                } else if (dx == 0) {
                    verticalCount++;
                    localMax = Math.max(localMax, verticalCount);
                } else {
                    double slope = (double) dy / dx;
                    
                    // Find if similar slope exists
                    boolean found = false;
                    for (Double existingSlope : slopeCount.keySet()) {
                        if (Math.abs(slope - existingSlope) < EPS) {
                            slopeCount.put(existingSlope, slopeCount.get(existingSlope) + 1);
                            localMax = Math.max(localMax, slopeCount.get(existingSlope));
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        slopeCount.put(slope, 1);
                        localMax = Math.max(localMax, 1);
                    }
                }
            }
            
            maxPoints = Math.max(maxPoints, localMax + duplicates + 1);
        }
        
        return maxPoints;
    }
    
    /**
     * Comprehensive solution that also returns the actual lines.
     * Useful for visualization and debugging.
     * 
     * @param points Array of points
     * @return Result containing max count and the lines
     */
    public LineAnalysisResult analyzeLines(int[][] points) {
        if (points.length <= 2) {
            return new LineAnalysisResult(points.length, java.util.Arrays.asList(points));
        }
        
        int maxPoints = 2;
        java.util.List<java.util.List<int[]>> maximalLines = new java.util.ArrayList<>();
        
        for (int i = 0; i < points.length; i++) {
            Map<String, java.util.List<int[]>> slopeGroups = new HashMap<>();
            java.util.List<int[]> verticalGroup = new java.util.ArrayList<>();
            verticalGroup.add(points[i]);
            
            for (int j = i + 1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                
                if (dx == 0) {
                    verticalGroup.add(points[j]);
                } else {
                    String slope = getNormalizedSlope(dy, dx);
                    
                    if (!slopeGroups.containsKey(slope)) {
                        slopeGroups.put(slope, new java.util.ArrayList<>());
                        slopeGroups.get(slope).add(points[i]);
                    }
                    slopeGroups.get(slope).add(points[j]);
                }
            }
            
            // Check vertical group
            if (verticalGroup.size() > maxPoints) {
                maxPoints = verticalGroup.size();
                maximalLines.clear();
                maximalLines.add(new java.util.ArrayList<>(verticalGroup));
            } else if (verticalGroup.size() == maxPoints) {
                maximalLines.add(new java.util.ArrayList<>(verticalGroup));
            }
            
            // Check slope groups
            for (java.util.List<int[]> group : slopeGroups.values()) {
                if (group.size() > maxPoints) {
                    maxPoints = group.size();
                    maximalLines.clear();
                    maximalLines.add(new java.util.ArrayList<>(group));
                } else if (group.size() == maxPoints) {
                    maximalLines.add(new java.util.ArrayList<>(group));
                }
            }
        }
        
        return new LineAnalysisResult(maxPoints, maximalLines);
    }
    
    /**
     * Optimized version with early termination.
     * Stops early if remaining points can't improve the result.
     */
    public int maxPointsOnLineOptimized(int[][] points) {
        if (points.length <= 2) return points.length;
        
        int maxPoints = 2;
        
        for (int i = 0; i < points.length - 1; i++) {
            // Early termination: if remaining points can't beat current max
            if (points.length - i <= maxPoints) break;
            
            Map<String, Integer> slopeMap = new HashMap<>();
            int duplicates = 0;
            int verticalCount = 0;
            int localMax = 0;
            
            for (int j = i + 1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                
                if (dx == 0 && dy == 0) {
                    duplicates++;
                } else if (dx == 0) {
                    verticalCount++;
                    localMax = Math.max(localMax, verticalCount);
                } else {
                    String slope = getNormalizedSlope(dy, dx);
                    slopeMap.put(slope, slopeMap.getOrDefault(slope, 0) + 1);
                    localMax = Math.max(localMax, slopeMap.get(slope));
                }
            }
            
            maxPoints = Math.max(maxPoints, localMax + duplicates + 1);
        }
        
        return maxPoints;
    }
    
    // Get normalized slope representation using GCD
    private String getNormalizedSlope(int dy, int dx) {
        if (dy == 0) return "0/1"; // Horizontal line
        
        int gcd = gcd(Math.abs(dy), Math.abs(dx));
        dy /= gcd;
        dx /= gcd;
        
        // Ensure consistent sign representation
        if (dx < 0) {
            dy = -dy;
            dx = -dx;
        }
        
        return dy + "/" + dx;
    }
    
    // Calculate Greatest Common Divisor
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    /**
     * Validates that points are actually collinear.
     * 
     * @param points Array of points to check
     * @return true if all points are collinear
     */
    public boolean arePointsCollinear(int[][] points) {
        if (points.length <= 2) return true;
        
        // Use first two points to define the line
        int x1 = points[0][0], y1 = points[0][1];
        int x2 = points[1][0], y2 = points[1][1];
        
        for (int i = 2; i < points.length; i++) {
            int x3 = points[i][0], y3 = points[i][1];
            
            // Check if (x3, y3) lies on line through (x1, y1) and (x2, y2)
            // Using cross product: (y2-y1)*(x3-x1) == (y3-y1)*(x2-x1)
            if ((y2 - y1) * (x3 - x1) != (y3 - y1) * (x2 - x1)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Finds the equation of line passing through maximum points.
     * Returns line in form ax + by + c = 0.
     */
    public LineEquation findMaximalLineEquation(int[][] points) {
        LineAnalysisResult analysis = analyzeLines(points);
        
        if (analysis.maximalLines.isEmpty()) {
            return null;
        }
        
        // Get first maximal line
        java.util.List<int[]> line = analysis.maximalLines.get(0);
        
        if (line.size() < 2) {
            return null;
        }
        
        int x1 = line.get(0)[0], y1 = line.get(0)[1];
        int x2 = line.get(1)[0], y2 = line.get(1)[1];
        
        // Line equation: (y2-y1)x - (x2-x1)y + (x2-x1)y1 - (y2-y1)x1 = 0
        int a = y2 - y1;
        int b = x1 - x2;
        int c = (x2 - x1) * y1 - (y2 - y1) * x1;
        
        return new LineEquation(a, b, c);
    }
    
    // Result classes for comprehensive analysis
    static class LineAnalysisResult {
        int maxCount;
        java.util.List<java.util.List<int[]>> maximalLines;
        
        LineAnalysisResult(int maxCount, java.util.List<java.util.List<int[]>> maximalLines) {
            this.maxCount = maxCount;
            this.maximalLines = maximalLines;
        }
        
        LineAnalysisResult(int maxCount, int[][] singleLine) {
            this.maxCount = maxCount;
            this.maximalLines = new java.util.ArrayList<>();
            java.util.List<int[]> line = new java.util.ArrayList<>();
            for (int[] point : singleLine) {
                line.add(point);
            }
            this.maximalLines.add(line);
        }
    }
    
    static class LineEquation {
        int a, b, c; // ax + by + c = 0
        
        LineEquation(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        
        @Override
        public String toString() {
            return String.format("%dx + %dy + %d = 0", a, b, c);
        }
        
        // Check if point lies on this line
        public boolean containsPoint(int x, int y) {
            return a * x + b * y + c == 0;
        }
    }
}
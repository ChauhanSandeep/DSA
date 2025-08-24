package Maths;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Valid Square
 * 
 * Given the coordinates of four points in 2D space p1, p2, p3 and p4, 
 * return true if the four points construct a square.
 * 
 * A square has four equal sides with positive length and four equal angles (90-degree angles).
 * 
 * Example:
 * Input: p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,1]
 * Output: true
 * 
 * LeetCode: https://leetcode.com/problems/valid-square
 * 
 * Time Complexity: O(1) as we're only dealing with 4 points
 * Space Complexity: O(1) as we're using a fixed-size set
 */
public class ValidSquare {
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        // Collect all points in an array for easier access
        int[][] points = {p1, p2, p3, p4};
        
        // Calculate all possible distances between any two points
        // There should be exactly 2 unique distances (sides and diagonals)
        // and 4 sides and 2 diagonals for a valid square
        Set<Integer> distances = new HashSet<>();
        
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                int dx = points[i][0] - points[j][0];
                int dy = points[i][1] - points[j][1];
                int distance = dx * dx + dy * dy;
                
                // If any distance is 0, points are not distinct
                if (distance == 0) {
                    return false;
                }
                
                distances.add(distance);
            }
        }
        
        // A square must have exactly 2 unique distances (sides and diagonals)
        // and there should be 4 sides and 2 diagonals
        return distances.size() == 2;
    }
}

package maths;

import java.util.Arrays;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Valid Square
 *
 * Given four 2D points, return true if they form a square. A valid square has
 * four distinct points, four equal positive side lengths, and two equal longer
 * diagonals.
 *
 * Leetcode: https://leetcode.com/problems/valid-square/ (Medium)
 * Rating:   acceptance 45.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Geometry | Pairwise squared distances
 *
 * Example:
 *   Input:  p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,1]
 *   Output: true
 *   Why:    the six pairwise distances contain one side length and one diagonal length.
 *
 * Follow-ups:
 *   1. How would you handle floating-point coordinates?
 *      Compare squared distances with an epsilon or normalize rational coordinates.
 *   2. How would you validate a rectangle instead?
 *      Require equal diagonals and opposite sides, but not all four sides equal.
 *   3. How would you return the square's side length?
 *      Return the smaller non-zero squared distance, or its square root if needed.
 *
 * Related: Max Points on a Line (149), Erect the Fence (587).
 */

public class ValidSquare {

    public static void main(String[] args) {
        ValidSquare solver = new ValidSquare();
        int[][][] inputs = {
            { {0, 0}, {1, 1}, {1, 0}, {0, 1} },
            { {0, 0}, {1, 1}, {1, 0}, {0, 12} }
        };
        boolean[] expected = { true, false };

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.validSquare(inputs[i][0], inputs[i][1], inputs[i][2], inputs[i][3]);
            System.out.printf("points=%s -> %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

        /**
         * Intuition: among four square vertices there are exactly six pairwise
         * distances: four equal sides and two equal diagonals. Using squared
         * distances avoids square roots, and rejecting distance 0 ensures the points
         * are distinct.
         *
         * Algorithm:
         *   1. Put the four input points into one array.
         *   2. Compute every pairwise squared distance.
         *   3. Return false immediately if any distance is 0.
         *   4. Store distances in a set and require exactly two distinct values.
         *
         * Time:  O(1) - exactly six point pairs are checked.
         * Space: O(1) - the set holds at most six squared distances.
         *
         * @param p1 first point [x, y]
         * @param p2 second point [x, y]
         * @param p3 third point [x, y]
         * @param p4 fourth point [x, y]
         * @return true if the four points form a square
         */
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

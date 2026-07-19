package arrays.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
/**
 * Problem: Spiral Matrix
 *
 * Given a rectangular matrix, return its elements in clockwise spiral order.
 * Start from the top-left corner, peel the outer border, then continue with the
 * next inner rectangle until every cell is visited.
 *
 * Leetcode: https://leetcode.com/problems/spiral-matrix/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Matrix traversal | Boundary shrinking | Simulation
 *
 * Example:
 *   Input:  matrix = [[1,2,3],[4,5,6],[7,8,9]]
 *   Output: [1,2,3,6,9,8,7,4,5]
 *   Why:    the traversal takes the outer ring clockwise, then the center cell.
 *
 * Follow-ups:
 *   1. Generate an n x n spiral matrix?
 *      Write values while moving with the same shrinking boundaries.
 *   2. Traverse counter-clockwise?
 *      Change the direction order and boundary updates.
 *   3. Handle a jagged matrix?
 *      Track visited cells explicitly because rectangular boundaries no longer work.
 *
 * Related: Spiral Matrix II (59), Spiral Matrix III (885).
 */
public class SpiralMatrixTraversal {

  public static void main(String[] args) {
    int[][][] inputs = {
        { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} },
        { {1, 2, 3, 4} },
        { {1}, {2}, {3} }
    };
    String[] expected = {
        "[1, 2, 3, 6, 9, 8, 7, 4, 5]",
        "[1, 2, 3, 4]",
        "[1, 2, 3]"
    };

    for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = getSpiralOrder(inputs[i]);
      System.out.printf("matrix=%s -> output=%s  expected=%s%n",
          Arrays.deepToString(inputs[i]), got, expected[i]);
    }
  }

/**
 * Intuition: the current unvisited cells always form a rectangle. Visit its top
 * edge, right edge, bottom edge, and left edge, then shrink those four boundaries
 * inward and repeat.
 *
 * Algorithm:
 *   1. Return an empty result for null or empty input.
 *   2. Maintain top, bottom, left, and right boundaries.
 *   3. Traverse top row and right column, then shrink them.
 *   4. If still valid, traverse bottom row and left column, then shrink them.
 *   5. Stop when the boundaries cross.
 *
 * Time:  O(m * n) - each cell is appended exactly once.
 * Space: O(1) - excluding the returned list.
 *
 * @param matrix input matrix
 * @return elements in clockwise spiral order
 */
  public static List<Integer> getSpiralOrder(int[][] matrix) {
    List<Integer> result = new ArrayList<>();

    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return result; // Return empty if input is invalid
    }

    int top = 0;
    int bottom = matrix.length - 1;
    int left = 0;
    int right = matrix[0].length - 1;

    // Loop until boundaries cross
    while (top <= bottom && left <= right) {

      // → Traverse from left to right on the top row
      for (int col = left; col <= right; col++) {
        result.add(matrix[top][col]);
      }
      top++; // Move top boundary down

      // ↓ Traverse from top to bottom on the right column
      for (int row = top; row <= bottom; row++) {
        result.add(matrix[row][right]);
      }
      right--; // Move right boundary left

      // ← Traverse from right to left on the bottom row (if still valid)
      if (top <= bottom) {
        for (int col = right; col >= left; col--) {
          result.add(matrix[bottom][col]);
        }
        bottom--; // Move bottom boundary up
      }

      // ↑ Traverse from bottom to top on the left column (if still valid)
      if (left <= right) {
        for (int row = bottom; row >= top; row--) {
          result.add(matrix[row][left]);
        }
        left++; // Move left boundary right
      }
    }

    return result;
  }
}

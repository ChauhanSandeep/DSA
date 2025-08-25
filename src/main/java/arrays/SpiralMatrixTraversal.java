package arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Print the elements of a 2D matrix in spiral (clockwise) order.
 *
 * ✅ Starting from the top-left corner, move in the order:
 *    → → → ↓ ↓ ← ← ↑ ... until all elements are visited.
 *
 * 🔗 LeetCode Link: https://leetcode.com/problems/spiral-matrix/
 *
 * Example:
 * Input:
 *  [
 *    [1,  2,  3],
 *    [4,  5,  6],
 *    [7,  8,  9]
 *  ]
 * Output: [1, 2, 3, 6, 9, 8, 7, 4, 5]
 *
 * Follow-up Questions:
 * - Q: How do we do reverse spiral order?
 *   A: Collect in same spiral order, then reverse the result.
 * - Q: How to do spiral traversal recursively?
 *   A: Yes, define boundaries and recurse on each layer.
 * - Q: What if it's a jagged matrix?
 *   A: Handle each row’s length individually; logic gets trickier.
 */
public class SpiralMatrixTraversal {

  public static void main(String[] args) {
    int[][] matrix = {
        {1, 2, 3, 4},
        {4, 5, 6, 5},
        {7, 8, 9, 1},
        {1, 9, 0, 2},
        {1, 9, 0, 2}
    };

    System.out.println(getSpiralOrder(matrix));
  }

  /**
   * Returns a list of elements traversed in spiral order from a 2D matrix.
   *
   * ✅ Algorithm: Layer-by-layer traversal using boundary tracking.
   * ✅ Time Complexity: O(m * n) for m rows and n columns
   * ✅ Space Complexity: O(1) excluding result list
   *
   * @param matrix 2D integer matrix
   * @return list of integers in spiral order
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
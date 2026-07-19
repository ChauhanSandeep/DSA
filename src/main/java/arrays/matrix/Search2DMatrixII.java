package arrays.matrix;

import java.util.Arrays;
/**
 * Problem: Search a 2D Matrix II
 *
 * Given a matrix sorted left-to-right in each row and top-to-bottom in each
 * column, determine whether target exists. Rows do not form one global sorted
 * array, so the search must eliminate one row or column at a time.
 *
 * Leetcode: https://leetcode.com/problems/search-a-2d-matrix-ii/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Matrix | Staircase search | Monotonic elimination
 *
 * Example:
 *   Input:  matrix = [[1,4,7,11],[2,5,8,12],[3,6,9,16]], target = 5
 *   Output: true
 *   Why:    starting at the top-right, comparisons discard columns or rows until 5 is reached.
 *
 * Follow-ups:
 *   1. Count all targets?
 *      Continue the staircase after each match or combine per-row binary searches.
 *   2. Find the kth smallest value?
 *      Binary search on value and count values <= mid with the sorted columns.
 *   3. Search many targets?
 *      Reuse row ranges or index rows, depending on target distribution.
 *
 * Related: Search a 2D Matrix (74), Kth Smallest Element in a Sorted Matrix (378).
 */
public class Search2DMatrixII {

    public static void main(String[] args) {
        Search2DMatrixII solver = new Search2DMatrixII();
        int[][] matrix = {
            {1, 4, 7, 11, 15},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };
        int[] targets = { 5, 20, 30 };
        boolean[] expected = { true, false, true };

        for (int i = 0; i < targets.length; i++) {
            boolean got = solver.searchMatrix(matrix, targets[i]);
            System.out.printf("matrix=%s target=%d -> output=%s  expected=%s%n",
                Arrays.deepToString(matrix), targets[i], got, expected[i]);
        }
    }

/**
 * Intuition: the top-right value is the smallest candidate in its column but the
 * largest candidate in its row. If it is too large, the whole column below it is
 * too large; if it is too small, the whole row to its left is too small.
 *
 * Algorithm:
 *   1. Reject null or empty matrices.
 *   2. Start at row 0 and the last column.
 *   3. Move left when currentValue is greater than target.
 *   4. Move down when currentValue is less than target.
 *   5. Return true on a match, otherwise false when bounds are crossed.
 *
 * Time:  O(m + n) - each move removes one row or one column.
 * Space: O(1) - only the current row and column are stored.
 *
 * @param matrix row-wise and column-wise sorted matrix
 * @param target value to find
 * @return true if target exists in the matrix
 */
  public boolean searchMatrix(int[][] matrix, int target) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return false;
    }

    int rows = matrix.length;
    int cols = matrix[0].length;

    // Start from top-right corner
    int row = 0;
    int col = cols - 1;

    while (row < rows && col >= 0) {
      int currentValue = matrix[row][col];

      if (currentValue == target) {
        return true;  // Found target
      } else if (currentValue > target) {
        col--;  // Move left (eliminate current column)
      } else {
        row++;  // Move down (eliminate current row)
      }
    }

    return false;  // Target not found
  }

  /**
   * Binary search approach using row and column binary searches.
   * Less optimal but demonstrates alternative technique.
   *
   * Algorithm:
   * 1. Binary search each row for target
   * 2. If found in any row, return true
   * 3. If not found in all rows, return false
   *
   * Time Complexity: O(m log n) where m is rows and n is columns.
   * Slower than staircase approach but still reasonable.
   *
   * Space Complexity: O(1) using only constant extra variables.
   *
   * @param matrix 2D sorted matrix
   * @param target value to search for
   * @return true if target found, false otherwise
   */
  public boolean searchMatrixBinarySearch(int[][] matrix, int target) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
      return false;
    }

    // Binary search each row
    for (int[] row : matrix) {
      if (binarySearch(row, target)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Helper method for binary search on a 1D array.
   *
   * @param arr sorted 1D array
   * @param target value to search for
   * @return true if target found, false otherwise
   */
  private boolean binarySearch(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;

      if (arr[mid] == target) {
        return true;
      } else if (arr[mid] < target) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }

    return false;
  }
}

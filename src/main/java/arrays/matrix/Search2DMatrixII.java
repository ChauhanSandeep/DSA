package arrays.matrix;

/**
 * Write an efficient algorithm that searches for a target value in an m x n integer matrix.
 * This matrix has the following properties:
 * - Integers in each row are sorted in ascending from left to right.
 * - Integers in each column are sorted in ascending from top to bottom.
 *
 * Example 1:
 * Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
 * Output: true
 *
 * Example 2:
 * Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 20
 * Output: false
 *
 * LeetCode: https://leetcode.com/problems/search-a-2d-matrix-ii/
 *
 * Follow-up Questions:
 * 1. How would you find all occurrences of the target value?
 *    - We could modify the search to continue after finding a match.
 * 2. What if the matrix is very large and doesn't fit in memory?
 *    - We could implement an external search that loads only necessary rows/columns.
 * 3. How would you find the kth smallest element in this matrix?
 *    - We could use a min-heap or binary search approach.
 *
 * Related Problems:
 * - Search a 2D Matrix (https://leetcode.com/problems/search-a-2d-matrix/)
 * - Kth Smallest Element in a Sorted Matrix (https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/)
 */
public class Search2DMatrixII {
  /**
   * Searches for target in 2D matrix using staircase approach.
   *
   * Algorithm:
   * 1. Start from top-right corner (or bottom-left, both work)
   * 2. Compare target with current element:
   *    - If target equals current: return true (found)
   *    - If target < current: move left
   *    - If target > current: move down
   * 3. Continue until found or reach boundary
   * 4. Return false if boundary reached without finding target
   *
   * Key insight: Starting from top-right (or bottom-left) corner ensures that at each
   * step we can eliminate exactly one row or column. This creates a "staircase" pattern
   * of elimination.
   *
   * Why top-right works:
   * - Moving left: current element is largest in the row to the left
   * - Moving down: current element is smallest in column below
   * - This monotonic property allows us to eliminate regions
   *
   * Time Complexity: O(m + n) where m is rows and n is columns.
   * Worst case: travel from top-right to bottom-left (m + n steps).
   *
   * Space Complexity: O(1) using only constant extra variables.
   *
   * @param matrix 2D sorted matrix
   * @param target value to search for
   * @return true if target found, false otherwise
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

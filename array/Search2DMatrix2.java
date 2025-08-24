package array;

/**
 * 🔍 Problem: Search a 2D Matrix II
 * https://leetcode.com/problems/search-a-2d-matrix-ii/
 *
 * The matrix has:
 * - Rows sorted in ascending order (left → right)
 * - Columns sorted in ascending order (top ↓ bottom)
 *
 * Goal: Search if a target exists efficiently.
 */
public class Search2DMatrix2 {

  public static void main(String[] args) {
    int[][] matrix =
        {
            {1,  4,  7,  11, 15},
            {2,  5,  8,  12, 19},
            {3,  6,  9,  16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };

    System.out.println("Searching 19: " + new Search2DMatrix2().searchMatrix(matrix, 19)); // true
    System.out.println("Searching 20: " + new Search2DMatrix2().searchMatrix(matrix, 20)); // false
  }

  /**
   * Efficient search from top-right corner.
   *
   * 🔹 Approach:
   * - Start from the top-right corner of the matrix.
   * - Compare the current element with the target:
   *  - If equal, return true.
   *  - If less than target, move down to the next row.
   *  - If greater than target, move left to the previous column.
   *  - Continue until you either find the target or exhaust the search space.
   *
   *  🔹 Time Complexity: O(N + M) where N is the number of rows and M is the number of columns.
   *  🔹 Space Complexity: O(1) since we are using constant space.
   */
  public boolean searchMatrix(int[][] matrix, int target) {
      if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
          return false;
      }

    int rowCount = matrix.length;
    int colCount = matrix[0].length;

    int row = 0, col = colCount - 1; // Start from top-right

    while (row < rowCount && col >= 0) {
      int value = matrix[row][col];

        if (value == target) {
            return true;
        } else if (value < target) {
            row++;     // Move down
        } else {
            col--;     // Move left
        }
    }

    return false;
  }
}
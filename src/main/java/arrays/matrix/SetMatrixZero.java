package arrays.matrix;

import java.util.Arrays;


/**
 * Set Matrix Zeroes
 *
 * Problem:
 * Given an m x n integer matrix, if an element is 0, set its entire row and column to 0's.
 * You must do it in place (modify the input matrix directly).
 *
 * Example:
 * Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
 * Output: [[1,0,1],[0,0,0],[1,0,1]]
 *
 * Example:
 * Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
 * Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
 *
 * Constraints:
 * - m == matrix.length
 * - n == matrix[0].length
 * - 1 <= m, n <= 200
 * - -2^31 <= matrix[i][j] <= 2^31 - 1
 *
 * LeetCode: https://leetcode.com/problems/set-matrix-zeroes/
 *
 * Follow-up Questions:
 * Q1: What if we're allowed O(m + n) space? How would that simplify the solution?
 * A1: Use two boolean arrays to track which rows and columns need zeroing, making logic simpler.
 *
 * Q2: Can you solve this in O(1) space without using the first row/column as markers?
 * A2: No, we need some storage to remember which rows/columns to zero. First row/col is the optimal choice.
 *
 * Q3: What if the matrix contains only positive numbers? Can we use a marker value?
 * A3: Yes, we could use -1 or any value outside the valid range as a temporary marker.
 *
 * Q4: How would you parallelize this algorithm for very large matrices?
 * A4: Split matrix into chunks, mark zeros in parallel, then synchronize and apply zeros in parallel.
 *
 * Q5: What if we want to set rows/columns to a different value instead of 0?
 * A5: Same algorithm - just change the final value we set from 0 to the desired value.
 */
public class SetMatrixZero {

  public static void main(String[] args) {
    int[][] matrix = {{1, 1, 1}, {0, 1, 2}, {1, 3, 1}};

    setZeroes(matrix);

    System.out.println("Matrix after setting zeroes:");
    for (int[] row : matrix) {
      System.out.println(Arrays.toString(row));
    }
  }

  /**
   * Sets entire rows and columns to zero if any cell in them contains a zero.
   *
   * Algorithm:
   * 1. Record whether first row and first column originally contain any zeros
   * 2. Use first row and column as marker storage for other rows/columns
   * 3. Scan matrix (excluding first row/col) and mark first row/col when zero found
   * 4. Zero out cells in the main matrix based on markers in first row/col
   * 5. Finally, zero out first row and column if they originally had zeros
   *
   * Key Insight: Use the matrix itself for storage by repurposing first row and column
   * as marker arrays. Handle them separately since they serve dual purpose.
   *
   * Time Complexity: O(m * n) - scan matrix twice
   * Space Complexity: O(1) - only two boolean flags for first row/col
   *
   * @param matrix Input m x n matrix to modify in-place
   */
  public static void setZeroes(int[][] matrix) {
    int numRows = matrix.length;
    int numCols = matrix[0].length;

    boolean shouldZeroFirstRow = false;
    boolean shouldZeroFirstCol = false;

    // Check if first row originally contains any zero
    for (int col = 0; col < numCols; col++) {
      if (matrix[0][col] == 0) {
        shouldZeroFirstRow = true;
        break;
      }
    }

    // Check if first column originally contains any zero
    for (int row = 0; row < numRows; row++) {
      if (matrix[row][0] == 0) {
        shouldZeroFirstCol = true;
        break;
      }
    }

    // Use first row and column as markers for other rows/columns
    for (int row = 1; row < numRows; row++) {
      for (int col = 1; col < numCols; col++) {
        if (matrix[row][col] == 0) {
          matrix[row][0] = 0; // Mark row
          matrix[0][col] = 0; // Mark column
        }
      }
    }

    // Zero out cells based on markers in first row/column
    for (int row = 1; row < numRows; row++) {
      for (int col = 1; col < numCols; col++) {
        if (matrix[row][0] == 0 || matrix[0][col] == 0) {
          matrix[row][col] = 0;
        }
      }
    }

    // Zero out first row if it originally had a zero
    if (shouldZeroFirstRow) {
      for (int col = 0; col < numCols; col++) {
        matrix[0][col] = 0;
      }
    }

    // Zero out first column if it originally had a zero
    if (shouldZeroFirstCol) {
      for (int row = 0; row < numRows; row++) {
        matrix[row][0] = 0;
      }
    }
  }
}
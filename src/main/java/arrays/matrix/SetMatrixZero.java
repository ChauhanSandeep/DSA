package arrays.matrix;

import java.util.Arrays;
/**
 * Problem: Set Matrix Zeroes
 *
 * Given an m x n matrix, if a cell is zero, set its entire row and column to
 * zero in place. The challenge is remembering which rows and columns must change
 * without using O(m + n) extra marker arrays.
 *
 * Leetcode: https://leetcode.com/problems/set-matrix-zeroes/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Matrix | In-place markers | Prefix row and column flags
 *
 * Example:
 *   Input:  matrix = [[1,1,1],[1,0,1],[1,1,1]]
 *   Output: [[1,0,1],[0,0,0],[1,0,1]]
 *   Why:    the zero at the center clears row 1 and column 1.
 *
 * Follow-ups:
 *   1. Allow O(m + n) space?
 *      Store row and column marker arrays directly, then do one rewrite pass.
 *   2. Matrix values are all positive?
 *      A sentinel can mark cells, but first-row and first-column markers are safer.
 *   3. Stream the matrix row by row?
 *      Exact in-place behavior is difficult because future zeros can affect past rows.
 *
 * Related: Game of Life (289), Walls and Gates (286).
 */
public class SetMatrixZero {

  public static void main(String[] args) {
    int[][][] inputs = {
        { {1, 1, 1}, {1, 0, 1}, {1, 1, 1} },
        { {0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5} }
    };
    int[][][] expected = {
        { {1, 0, 1}, {0, 0, 0}, {1, 0, 1} },
        { {0, 0, 0, 0}, {0, 4, 5, 0}, {0, 3, 1, 0} }
    };

    for (int i = 0; i < inputs.length; i++) {
      int[][] input = new int[inputs[i].length][];
      for (int row = 0; row < inputs[i].length; row++) {
        input[row] = inputs[i][row].clone();
      }
            setZeroes(input);
      System.out.printf("matrix=%s -> output=%s  expected=%s%n",
          Arrays.deepToString(inputs[i]), Arrays.deepToString(input), Arrays.deepToString(expected[i]));
    }
  }

/**
 * Intuition: the first row and first column can act as marker arrays for the rest
 * of the matrix. Because those marker areas are real data too, two booleans first
 * remember whether they originally needed to be zeroed.
 *
 * Algorithm:
 *   1. Record whether the first row or first column originally contains a zero.
 *   2. Mark rows and columns by writing zeros into the first column and first row.
 *   3. Rewrite the inner matrix according to those markers.
 *   4. Zero the first row and first column if their original flags require it.
 *
 * Time:  O(m * n) - the matrix is scanned a constant number of times.
 * Space: O(1) - only two boolean flags are used.
 *
 * @param matrix matrix modified in place
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

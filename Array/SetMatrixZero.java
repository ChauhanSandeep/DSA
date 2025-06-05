package Array;

import java.util.Arrays;


/**
 * LeetCode: https://leetcode.com/problems/set-matrix-zeroes/
 *
 * Problem:
 * Given an m x n matrix. If an element is 0, set its entire row and column to 0.
 * Do it in-place using constant space (excluding input matrix itself).
 *
 * Approach:
 * - Use first row and first column to mark which rows and columns should be zeroed.
 * - Use `isFirstRowZero` and `isFirstColZero` to independently track whether the first row or column had any 0.
 * - Iterate through the matrix, marking the first row and column for zeroing.
 * - Finally, zero out the marked rows and columns.
 *
 * Time Complexity: O(m * n)
 * Space Complexity: O(1)  (In-place)
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

  public static void setZeroes(int[][] matrix) {
    int rows = matrix.length;
    int cols = matrix[0].length;

    boolean isFirstRowZero = false; // Flag for first row is zero
    boolean isFirstColZero = false; // Flag for first column is zero

    // Step 1: Check if first row has any 0
    for (int j = 0; j < cols; j++) {
      if (matrix[0][j] == 0) {
        isFirstRowZero = true;
        break;
      }
    }

    // Step 2: Check if first column has any 0
    for (int i = 0; i < rows; i++) {
      if (matrix[i][0] == 0) {
        isFirstColZero = true;
        break;
      }
    }

    // Step 3: Use first row & column as markers for zeroing rows/columns
    for (int i = 1; i < rows; i++) {
      for (int j = 1; j < cols; j++) {
        if (matrix[i][j] == 0) {
          matrix[i][0] = 0; // Mark row
          matrix[0][j] = 0; // Mark col
        }
      }
    }

    // Step 4: Zero out cells based on markers
    for (int i = 1; i < rows; i++) {
      for (int j = 1; j < cols; j++) {
        if (matrix[i][0] == 0 || matrix[0][j] == 0) {
          matrix[i][j] = 0;
        }
      }
    }

    // Step 5: Zero out first row if needed
    if (isFirstRowZero) {
      for (int j = 0; j < cols; j++) {
        matrix[0][j] = 0;
      }
    }

    // Step 6: Zero out first column if needed
    if (isFirstColZero) {
      for (int i = 0; i < rows; i++) {
        matrix[i][0] = 0;
      }
    }
  }
}
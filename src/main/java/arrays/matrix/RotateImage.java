package arrays.matrix;

import java.util.Arrays;
/**
 * Problem: Rotate Image
 *
 * Given an n x n matrix representing an image, rotate it 90 degrees clockwise.
 * The rotation must happen in place without allocating another matrix for the
 * answer.
 *
 * Leetcode: https://leetcode.com/problems/rotate-image/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Matrix | Transpose | Reverse rows
 *
 * Example:
 *   Input:  matrix = [[1,2,3],[4,5,6],[7,8,9]]
 *   Output: [[7,4,1],[8,5,2],[9,6,3]]
 *   Why:    each original cell (row, col) moves to (col, n - 1 - row).
 *
 * Follow-ups:
 *   1. Rotate counter-clockwise?
 *      Transpose, then reverse each column instead of each row.
 *   2. Rotate by 180 degrees?
 *      Reverse every row and then reverse row order, or perform two 90-degree turns.
 *   3. Rotate a non-square matrix?
 *      Dimensions change, so a separate n x m output matrix is required.
 *
 * Related: Rotate Array (189), Spiral Matrix (54), Transpose Matrix (867).
 */
public class RotateImage {

    public static void main(String[] args) {
        RotateImage solver = new RotateImage();

        int[][][] inputs = {
            { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} },
            { {1} }
        };
        int[][][] expected = {
            { {7, 4, 1}, {8, 5, 2}, {9, 6, 3} },
            { {1} }
        };

        for (int i = 0; i < inputs.length; i++) {
            int[][] input = new int[inputs[i].length][];
            for (int row = 0; row < inputs[i].length; row++) {
                input[row] = inputs[i][row].clone();
            }
            solver.rotate(input);
            System.out.printf("matrix=%s -> output=%s  expected=%s%n",
                Arrays.deepToString(inputs[i]), Arrays.deepToString(input), Arrays.deepToString(expected[i]));
        }
    }

/**
 * Intuition: a clockwise rotation can be decomposed into two simpler symmetric
 * moves. Transpose reflects the matrix across the main diagonal, then reversing
 * each row pushes every transposed value to its final clockwise column.
 *
 * Algorithm:
 *   1. Return for null, empty, or non-square input.
 *   2. Transpose the matrix by swapping only cells above the main diagonal.
 *   3. Reverse every row in place.
 *
 * Time:  O(n^2) - transpose and row reversals together visit matrix cells linearly.
 * Space: O(1) - only temporary swap variables are used.
 *
 * @param matrix square matrix rotated in place
 */
  public void rotate(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
      return;
    }

    int size = matrix.length;

    // Step 1: Transpose the matrix (swap across main diagonal)
    for (int i = 0; i < size; i++) {
      for (int j = i + 1; j < size; j++) {
        // Swap matrix[i][j] with matrix[j][i]
        int temp = matrix[i][j];
        matrix[i][j] = matrix[j][i];
        matrix[j][i] = temp;
      }
    }

    // Step 2: Reverse each row
    for (int i = 0; i < size; i++) {
      reverseRow(matrix[i]);
    }
  }

  // Helper method to reverse a single row
  private void reverseRow(int[] row) {
    int left = 0;
    int right = row.length - 1;

    while (left < right) {
      int temp = row[left];
      row[left] = row[right];
      row[right] = temp;
      left++;
      right--;
    }
  }

  /**
   * Educational version with detailed four-point swap for interview explanation.
   * Demonstrates clear understanding of the rotation pattern.
   *
   * Algorithm:
   * 1. Divide matrix into concentric squares (layers)
   * 2. For each layer, process elements in groups of 4
   *    - Explicitly swap 4 positions in layer: top, right, bottom, left
   *    - Move top → right, right → bottom, bottom → left, left → top
   *    - Keep doing this for each element in the layer
   * 3. Continue until all layers are processed
   *
   * Time Complexity: O(n²) where n is dimension of matrix
   * Space Complexity: O(1) using constant extra space
   *
   * @param matrix n×n 2D matrix to rotate in-place
   */
  public void rotateFourPointSwap(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
      return;
    }

    int length = matrix.length;

    // Process outer to inner layers
    for (int layer = 0; layer < length / 2; layer++) {
      int first = layer;
      int last = length - 1 - layer;

      // Process each element in the current layer
      for (int i = first; i < last; i++) {
        int offset = i - first; // Offset within the layer

        // Define the four positions that need to be rotated
        int top = matrix[first][i];                      // Top position
        int right = matrix[i][last];                     // Right position
        int bottom = matrix[last][last - offset];        // Bottom position
        int left = matrix[last - offset][first];         // Left position

        // Perform cyclic rotation: top → right → bottom → left → top
        matrix[i][last] = top;                           // Move top to right
        matrix[last][last - offset] = right;             // Move right to bottom
        matrix[last - offset][first] = bottom;           // Move bottom to left
        matrix[first][i] = left;                         // Move left to top
      }
    }
  }
}

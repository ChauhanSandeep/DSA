package arrays.matrix;

/**
 * Problem: Rotate Image
 *
 * You are given an n x n 2D matrix representing an image. Rotate the image by 90 degrees (clockwise).
 * You have to rotate the image in-place, which means you have to modify the input 2D matrix directly.
 * DO NOT allocate another 2D matrix and do the rotation.
 *
 * Example:
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [[7,4,1],[8,5,2],[9,6,3]]
 * Explanation:
 * Original:       Rotated 90° clockwise:
 * 1 2 3           7 4 1
 * 4 5 6     →     8 5 2
 * 7 8 9           9 6 3
 *
 * Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
 * Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 * Explanation:
 * Original:          Rotated 90° clockwise:
 * 5  1  9  11        15 13  2  5
 * 2  4  8  10   →    14  3  4  1
 * 13 3  6  7         12  6  8  9
 * 15 14 12 16        16  7 10 11
 *
 * LeetCode: https://leetcode.com/problems/rotate-image/
 *
 * Follow-up Questions:
 * 1. Q: How would you rotate the matrix counter-clockwise instead?
 *    A: Transpose first, then reverse each column instead of each row.
 *
 * 2. Q: What if you needed to rotate by 180 degrees?
 *    A: Could call rotate90 twice, or simply reverse all rows then all columns.
 *
 * 3. Q: How would you handle non-square matrices?
 *    A: Would need to allocate new matrix as dimensions change (m×n becomes n×m).
 *
 * 4. Q: What if rotation angle is arbitrary (not just 90 degrees)?
 *    A: Would require complex geometric transformations and likely extra space.
 *
 * Related Problems:
 * - Rotate Array: https://leetcode.com/problems/rotate-array/
 * - Spiral Matrix: https://leetcode.com/problems/spiral-matrix/
 * - Transpose Matrix: https://leetcode.com/problems/transpose-matrix/
 */
public class RotateImage {

  /**
   * Rotates matrix 90 degrees clockwise using transpose and reverse approach.
   *
   * Algorithm:
   * 1. Transpose the matrix: swap matrix[i][j] with matrix[j][i]
   *    - This converts rows to columns
   * 2. Reverse each row: reverse elements in each row
   *    - This completes the 90-degree clockwise rotation
   *
   * Mathematical proof:
   * - After transpose: element at (i,j) goes to (j,i)
   * - After row reversal: element at (j,i) goes to (j,n-1-i)
   * - Combined: element at (i,j) ends at (j,n-1-i), which is correct for 90° clockwise rotation
   *
   * Time Complexity: O(n²) where n is dimension of matrix
   * Space Complexity: O(1) using constant extra space
   *
   * @param matrix n×n 2D matrix to rotate in-place
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

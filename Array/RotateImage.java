package Array;

/**
 * Rotate Image
 * 
 * Problem: Rotate n×n 2D matrix 90 degrees clockwise in-place.
 * 
 * Example: matrix = [[1,2,3],[4,5,6],[7,8,9]] -> [[7,4,1],[8,5,2],[9,6,3]]
 * Each element moves: (i,j) -> (j, n-1-i)
 * 
 * LeetCode: https://leetcode.com/problems/rotate-image
 * 
 * Follow-up Questions:
 * - How to rotate counter-clockwise? (Transpose then reverse columns)
 * - What if matrix is not square? (Need different approach, can't be done in-place)
 * - How to rotate by arbitrary angles? (Use mathematical rotation matrix)
 */
public class RotateImage {

    /**
     * Rotates matrix 90 degrees clockwise using transpose + reverse approach.
     * 
     * Algorithm:
     * 1. Transpose matrix: swap element (i,j) with (j,i)
     * 2. Reverse each row to complete 90-degree rotation
     * 3. This transforms (i,j) -> (j,i) -> (j,n-1-i)
     * 
     * Time Complexity: O(n²) where n is matrix dimension
     * Space Complexity: O(1) - in-place rotation
     * 
     * @param matrix n×n matrix to rotate
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // Step 1: Transpose matrix (swap across main diagonal)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Swap matrix[i][j] with matrix[j][i]
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            reverseRow(matrix[i]);
        }
    }

    // Helper method to reverse a row
    private void reverseRow(int[] row) {
        int left = 0, right = row.length - 1;
        while (left < right) {
            int temp = row[left];
            row[left] = row[right];
            row[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Direct layer-by-layer rotation approach
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public void rotateLayerByLayer(int[][] matrix) {
        int n = matrix.length;

        // Process each concentric layer
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - 1 - layer;

            // Rotate elements in current layer
            for (int i = first; i < last; i++) {
                int offset = i - first;

                // Save top element
                int top = matrix[first][i];

                // top = left
                matrix[first][i] = matrix[last - offset][first];

                // left = bottom
                matrix[last - offset][first] = matrix[last][last - offset];

                // bottom = right
                matrix[last][last - offset] = matrix[i][last];

                // right = top
                matrix[i][last] = top;
            }
        }
    }

    /**
     * Four-way swap approach (clearer visualization)
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public void rotateFourWaySwap(int[][] matrix) {
        int n = matrix.length;

        for (int layer = 0; layer < n / 2; layer++) {
            for (int i = layer; i < n - layer - 1; i++) {
                // Calculate positions for four-way swap
                int top = matrix[layer][i];
                int right = matrix[i][n - layer - 1];
                int bottom = matrix[n - layer - 1][n - i - 1];
                int left = matrix[n - i - 1][layer];

                // Perform four-way rotation
                matrix[layer][i] = left;
                matrix[i][n - layer - 1] = top;
                matrix[n - layer - 1][n - i - 1] = right;
                matrix[n - i - 1][layer] = bottom;
            }
        }
    }

    /**
     * Using extra space for clarity (not in-place)
     * Time Complexity: O(n²), Space Complexity: O(n²)
     */
    public void rotateWithExtraSpace(int[][] matrix) {
        int n = matrix.length;
        int[][] rotated = new int[n][n];

        // Copy elements to rotated positions
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        // Copy back to original matrix
        for (int i = 0; i < n; i++) {
            System.arraycopy(rotated[i], 0, matrix[i], 0, n);
        }
    }

    /**
     * Counter-clockwise rotation (for comparison)
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public void rotateCounterClockwise(int[][] matrix) {
        int n = matrix.length;

        // Transpose matrix
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // Reverse each column (instead of row)
        for (int j = 0; j < n; j++) {
            int top = 0, bottom = n - 1;
            while (top < bottom) {
                int temp = matrix[top][j];
                matrix[top][j] = matrix[bottom][j];
                matrix[bottom][j] = temp;
                top++;
                bottom--;
            }
        }
    }

    /**
     * 180-degree rotation
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public void rotate180(int[][] matrix) {
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Only swap once for each pair
                if (i < n / 2 || (i == n / 2 && j < n / 2)) {
                    int temp = matrix[i][j];
                    matrix[i][j] = matrix[n - 1 - i][n - 1 - j];
                    matrix[n - 1 - i][n - 1 - j] = temp;
                }
            }
        }
    }

    /**
     * Helper method to print matrix (for debugging)
     */
    private void printMatrix(int[][] matrix, String title) {
        System.out.println(title + ":");
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }

    /**
     * Helper method to create a copy of matrix (for testing)
     */
    private int[][] copyMatrix(int[][] matrix) {
        int n = matrix.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, n);
        }
        return copy;
    }
}

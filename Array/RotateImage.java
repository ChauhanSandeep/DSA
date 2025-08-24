package Array;

/**
 * You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).
 * You have to rotate the image in-place, which means you have to modify the input 2D matrix directly.
 * DO NOT allocate another 2D matrix and do the rotation.
 * 
 * Example 1:
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [[7,4,1],[8,5,2],[9,6,3]]
 * 
 * Example 2:
 * Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
 * Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 * 
 * LeetCode: https://leetcode.com/problems/rotate-image/
 * 
 * Follow-up Questions:
 * 1. How would you rotate the image counter-clockwise instead?
 *    - We could reverse the order of the transpose or reverse each column instead of each row.
 * 2. What if the matrix is not square?
 *    - For non-square matrices, rotation would change the dimensions (m×n → n×m).
 * 3. How would you rotate the image by 180 degrees?
 *    - We could either rotate 90 degrees twice or reverse both rows and columns.
 * 
 * Related Problems:
 * - Rotate Array (https://leetcode.com/problems/rotate-array/)
 * - Determine Whether Matrix Can Be Obtained By Rotation (https://leetcode.com/problems/determine-whether-matrix-can-be-obtained-by-rotation/)
 */
public class RotateImage {
    /**
     * Rotates the matrix 90 degrees clockwise in-place.
     * 
     * @param matrix The n x n matrix to rotate
     */
    public void rotate(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
            return; // Not a valid square matrix
        }
        
        int n = matrix.length;
        
        // Step 1: Transpose the matrix (swap elements across the main diagonal)
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
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
    
    /**
     * Reverses the elements in a row array.
     */
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
     * Alternative approach: Rotate layer by layer.
     * This approach is more direct but slightly more complex to implement.
     */
    public void rotateLayerByLayer(int[][] matrix) {
        int n = matrix.length;
        
        // Rotate layer by layer, starting from the outer layer
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - 1 - layer;
            
            for (int i = first; i < last; i++) {
                int offset = i - first;
                int top = matrix[first][i]; // Save top
                
                // Left -> Top
                matrix[first][i] = matrix[last - offset][first];
                
                // Bottom -> Left
                matrix[last - offset][first] = matrix[last][last - offset];
                
                // Right -> Bottom
                matrix[last][last - offset] = matrix[i][last];
                
                // Top -> Right
                matrix[i][last] = top; // Right <- saved top
            }
        }
    }
    
    /**
     * Rotates the matrix 90 degrees counter-clockwise.
     * This is similar to rotating clockwise but with a different order of operations.
     */
    public void rotateCounterClockwise(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
            return; // Not a valid square matrix
        }
        
        int n = matrix.length;
        
        // Step 1: Transpose the matrix
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // Step 2: Reverse each column (or reverse the order of rows)
        for (int i = 0; i < n / 2; i++) {
            int[] temp = matrix[i];
            matrix[i] = matrix[n - 1 - i];
            matrix[n - 1 - i] = temp;
        }
    }
    
    /**
     * Rotates the matrix 180 degrees in-place.
     * This can be done by reversing both rows and columns.
     */
    public void rotate180(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
            return; // Not a valid square matrix
        }
        
        int n = matrix.length;
        
        // Reverse each row
        for (int i = 0; i < n; i++) {
            reverseRow(matrix[i]);
        }
        
        // Reverse the order of the rows
        for (int i = 0; i < n / 2; i++) {
            int[] temp = matrix[i];
            matrix[i] = matrix[n - 1 - i];
            matrix[n - 1 - i] = temp;
        }
    }
    
    /**
     * Rotates the matrix 90 degrees clockwise using extra space.
     * This is not in-place but demonstrates the concept clearly.
     */
    public void rotateWithExtraSpace(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
            return; // Not a valid square matrix
        }
        
        int n = matrix.length;
        int[][] rotated = new int[n][n];
        
        // Copy elements to the new matrix with rotation
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }
        
        // Copy back to the original matrix
        for (int i = 0; i < n; i++) {
            System.arraycopy(rotated[i], 0, matrix[i], 0, n);
        }
    }
}

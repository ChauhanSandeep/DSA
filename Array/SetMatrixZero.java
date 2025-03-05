package Array;

import java.util.Arrays;

public class SetMatrixZero {
    public static void main(String[] args) {
        int[][] matrix = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
        };

        new SetMatrixZero().setZeroes(matrix);
        
        // Print the modified matrix
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void setZeroes(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return;

        boolean firstColHasZero = false;
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Step 1: Use first row & column as markers
        for (int i = 0; i < rows; i++) {
            if (matrix[i][0] == 0) firstColHasZero = true;  // Track if first col should be zero
            
            for (int j = 1; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;  // Mark the row
                    matrix[0][j] = 0;  // Mark the column
                }
            }
        }

        // Step 2: Set cells to zero based on markers (skip first row & col)
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        // Step 3: Handle first row separately if needed
        if (matrix[0][0] == 0) {
            for (int j = 0; j < cols; j++) {
                matrix[0][j] = 0;
            }
        }

        // Step 4: Handle first column separately if needed
        if (firstColHasZero) {
            for (int i = 0; i < rows; i++) {
                matrix[i][0] = 0;
            }
        }
    }
}

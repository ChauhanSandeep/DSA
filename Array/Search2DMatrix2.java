package Array;

/**
 * Write an efficient algorithm that searches for a target value in an m x n integer matrix. The matrix has the following properties:
 *
 * Integers in each row are sorted in ascending from left to right.
 * Integers in each column are sorted in ascending from top to bottom.
 */
public class Search2DMatrix2 {

    public static void main(String[] args) {
        int[][] matrix = {
            {1, 4, 7, 11, 15},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}};
        boolean result = new Search2DMatrix2().searchMatrix(matrix, 19);
        System.out.println(result);
        result = new Search2DMatrix2().searchMatrix(matrix, 20);
        System.out.println(result);
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        // Edge case: if the matrix is empty or null, return false
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int row = 0, col = matrix[0].length - 1; // Start from the top-right corner

        // Traverse the matrix diagonally: move left if the current element is greater, move down if smaller
        while (row < matrix.length && col >= 0) {
            if (matrix[row][col] == target) { // If target is found, return true
                return true;
            } else if (matrix[row][col] < target) { // Move down if target is greater
                row++;
            } else { // Move left if target is smaller
                col--;
            }
        }

        // Target not found
        return false;
    }
}
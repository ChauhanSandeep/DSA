public boolean searchMatrix(int[][] matrix, int target) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;

    int rows = matrix.length;
    int cols = matrix[0].length;
    int row = rows - 1, col = 0; // Start from the bottom-left corner

    while (row >= 0 && col < cols) {
        if (matrix[row][col] == target) return true;
        else if (matrix[row][col] < target) col++; // Move right
        else row--; // Move up
    }

    return false;
}

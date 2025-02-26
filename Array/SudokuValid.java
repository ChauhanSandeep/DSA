package Array;

public class SudokuValid {
    public static void main(String[] args) {
        char[][] board =
                {
                        {'9', '3', '.', '.', '7', '.', '.', '.', '.'},
                        {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                        {'.', '7', '8', '.', '.', '.', '.', '6', '.'},
                        {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                        {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                        {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                        {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                        {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                        {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
                };
        System.out.println(isValidSudoku(board));
    }

    /**
     * Check if a given Sudoku board is valid.
     * A valid Sudoku must follow these rules:
     * - Each row must contain unique digits (1-9)
     * - Each column must contain unique digits (1-9)
     * - Each 3x3 block must contain unique digits (1-9)
     * - Empty cells ('.') are ignored
     * @param board 9x9 character matrix representing Sudoku
     * @return true if valid, false otherwise
     */
    public static boolean isValidSudoku(char[][] board) {
        boolean[][] rowDigits = new boolean[9][9];
        boolean[][] colDigits = new boolean[9][9];
        boolean[][] blockDigits = new boolean[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') continue;

                int digit = board[i][j] - '1';  // Convert '1'-'9' to 0-8
                int blockNum = (i / 3) * 3 + j / 3;  // Determine which 3x3 block

                if (rowDigits[i][digit] || colDigits[j][digit] || blockDigits[blockNum][digit]) {
                    return false;  // Duplicate found in row, column, or block
                }

                // Mark the digit as seen in the current row, column, and block
                rowDigits[i][digit] = colDigits[j][digit] = blockDigits[blockNum][digit] = true;
            }
        }
        return true;
    }
}

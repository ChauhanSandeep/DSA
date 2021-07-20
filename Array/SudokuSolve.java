package Array;

import java.util.Arrays;

public class SudokuSolve {

    private boolean[][] rowDigit = new boolean[9][9];
    private boolean[][] colDigit = new boolean[9][9];
    private boolean[][] blockDigit = new boolean[9][9];

    public static void main(String[] args) {
        char[][] board =
                {
                        {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                        {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                        {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                        {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                        {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                        {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                        {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                        {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                        {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        new SudokuSolve().solveSudoku(board);
        System.out.println(Arrays.deepToString(board));
    }

    /**
     * Write a program to solve a Sudoku puzzle by filling the empty cells.
     * @param board
     */
    public void solveSudoku(char[][] board) {
        int num, k;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    num = board[i][j] - '1';
                    k = i / 3 * 3 + j / 3;
                    rowDigit[i][num] = colDigit[j][num] = blockDigit[k][num] = true;
                }
            }
        }
        solveSudokuRec(0, board);
    }

    public boolean solveSudokuRec(int index, char[][] board) {
        if (index == 81) return true;

        int row = index / 9;
        int col = index % 9;
        int block = row / 3 * 3 + col / 3;

        if (board[row][col] != '.') {
            return solveSudokuRec(index + 1, board);
        } else {
            for (char c = '1'; c <= '9'; c++) {
                int digit = c - '1';

                if (!rowDigit[row][digit] && !colDigit[col][digit] && !blockDigit[block][digit]) {
                    board[row][col] = c;
                    rowDigit[row][digit] = colDigit[col][digit] = blockDigit[block][digit] = true;

                    if (solveSudokuRec(index + 1, board)) return true;
                    board[row][col] = '.';
                    rowDigit[row][digit] = colDigit[col][digit] = blockDigit[block][digit] = false;
                }
            }
            return false;
        }
    }
}

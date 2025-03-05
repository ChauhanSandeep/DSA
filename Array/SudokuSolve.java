package Array;

public class SudokuSolve {
    private static final int SIZE = 9;
    private static boolean[][] rowDigit = new boolean[SIZE][SIZE];
    private static boolean[][] colDigit = new boolean[SIZE][SIZE];
    private static boolean[][] blockDigit = new boolean[SIZE][SIZE];

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
                        {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
                };

        if (solveSudoku(board)) {
            printBoard(board);
        } else {
            System.out.println("No solution exists!");
        }
    }

    public static boolean solveSudoku(char[][] board) {
        if (board == null || board.length != SIZE || board[0].length != SIZE) {
            return false; // Invalid board
        }

        initializeConstraints(board);
        return solve(0, board);
    }

    private static void initializeConstraints(char[][] board) {
        rowDigit = new boolean[SIZE][SIZE];
        colDigit = new boolean[SIZE][SIZE];
        blockDigit = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '1';
                    int block = (i / 3) * 3 + (j / 3);
                    rowDigit[i][num] = colDigit[j][num] = blockDigit[block][num] = true;
                }
            }
        }
    }

    private static boolean solve(int index, char[][] board) {
        if (index == SIZE * SIZE) return true; // Solved

        int row = index / SIZE;
        int col = index % SIZE;
        int block = (row / 3) * 3 + (col / 3);

        if (board[row][col] != '.') {
            return solve(index + 1, board);
        }

        for (char c = '1'; c <= '9'; c++) {
            int num = c - '1';

            if (!rowDigit[row][num] && !colDigit[col][num] && !blockDigit[block][num]) {
                board[row][col] = c;
                rowDigit[row][num] = colDigit[col][num] = blockDigit[block][num] = true;

                if (solve(index + 1, board)) return true;

                // Backtrack
                board[row][col] = '.';
                rowDigit[row][num] = colDigit[col][num] = blockDigit[block][num] = false;
            }
        }
        return false;
    }

    private static void printBoard(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}

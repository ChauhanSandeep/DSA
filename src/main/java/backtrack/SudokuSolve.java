package backtrack;

import java.util.Arrays;

/**
 * Problem: Sudoku Solver
 *
 * Fill a partially completed 9 x 9 Sudoku board in place. Empty cells are '.',
 * and every row, column, and 3 x 3 box must contain digits 1..9 without
 * repetition in the solved board.
 *
 * Leetcode: https://leetcode.com/problems/sudoku-solver/
 * Rating:   acceptance 65.6% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Constraint tracking | Fill empty cells
 *
 * Example:
 *   Input:  top row [5,3,.,.,7,.,.,.,.]
 *   Output: top row [5,3,4,6,7,8,9,1,2]
 *   Why:    the completed board fills every empty cell while keeping each row,
 *           column, and 3 x 3 box free of repeated digits.
 *
 * Follow-ups:
 *   1. Choose the next cell with minimum candidates instead of row-major order?
 *      Scan empty cells each step and branch on the one with the fewest legal digits.
 *   2. Count all possible solutions?
 *      Continue DFS after one solution and increment a counter at each completed board.
 *   3. Detect invalid given boards before solving?
 *      During constraint initialization, reject any duplicate digit in a row/column/box.
 *   4. Solve larger Sudoku variants?
 *      Parameterize board size and box dimensions, then replace fixed arrays with size-based masks.
 *
 * Related: Valid Sudoku (36), N-Queens (51).
 */
public class SudokuSolve {
    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;
    private static final char EMPTY = '.';

    private static boolean[][] rowDigit = new boolean[SIZE][SIZE];
    private static boolean[][] colDigit = new boolean[SIZE][SIZE];
    private static boolean[][] boxDigit = new boolean[SIZE][SIZE];

    /**
     * Intuition: Sudoku backtracking is about trying digits, but the trick is to
     * make legality checks instant. A digit is legal in a cell only if it has not
     * appeared in that row, that column, or that 3 x 3 box. Three boolean tables
     * track those facts, so placing a digit also updates the future constraints.
     * If a later cell gets stuck, we undo the placement and try another digit.
     *
     * Algorithm:
     *   1. Reject boards that are not 9 x 9 or that already contain duplicate fixed digits.
     *   2. Initialize row, column, and box constraint tables from the prefilled cells.
     *   3. Visit cells in row-major order and skip cells that were already filled.
     *   4. For an empty cell, try digits 1..9 that are absent from its row, column, and box tables.
     *   5. Place a legal digit and mark its constraints; if solving the rest fails,
     *      restore the dot and unmark the digit before trying the next one.
     *
     * Time:  O(9^e) - each empty cell may need to try up to 9 digits before constraints prune it.
     * Space: O(1) for fixed-size constraint tables plus O(e) recursion depth.
     *
     * @param board 9 x 9 Sudoku board, mutated in place when solvable
     * @return true if a solution exists
     */
    public static boolean solveSudoku(char[][] board) {
        if (!hasValidShape(board)) return false;
        if (!initializeConstraints(board)) return false;

        return solve(0, board);
    }

    /** Checks that the board is a non-null 9 x 9 grid. */
    private static boolean hasValidShape(char[][] board) {
        if (board == null || board.length != SIZE) return false;
        for (char[] row : board) {
            if (row == null || row.length != SIZE) return false;
        }
        return true;
    }

    /** Initializes row, column, and box digit tables from the fixed cells. */
    private static boolean initializeConstraints(char[][] board) {
        rowDigit = new boolean[SIZE][SIZE];
        colDigit = new boolean[SIZE][SIZE];
        boxDigit = new boolean[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                char cell = board[row][col];
                if (cell == EMPTY) continue;
                if (cell < '1' || cell > '9') return false;

                int digit = cell - '1';
                int box = boxIndex(row, col);
                if (rowDigit[row][digit] || colDigit[col][digit] || boxDigit[box][digit]) return false;

                rowDigit[row][digit] = true;
                colDigit[col][digit] = true;
                boxDigit[box][digit] = true;
            }
        }
        return true;
    }

    /** Fills empty cells in row-major order using the current constraint tables. */
    private static boolean solve(int cellIndex, char[][] board) {
        if (cellIndex == SIZE * SIZE) return true;

        int row = cellIndex / SIZE;
        int col = cellIndex % SIZE;
        if (board[row][col] != EMPTY) return solve(cellIndex + 1, board);

        int box = boxIndex(row, col);
        for (char digitChar = '1'; digitChar <= '9'; digitChar++) {
            int digit = digitChar - '1';
            if (rowDigit[row][digit] || colDigit[col][digit] || boxDigit[box][digit]) continue;

            board[row][col] = digitChar;
            rowDigit[row][digit] = true;
            colDigit[col][digit] = true;
            boxDigit[box][digit] = true;

            if (solve(cellIndex + 1, board)) return true;

            board[row][col] = EMPTY;
            rowDigit[row][digit] = false;
            colDigit[col][digit] = false;
            boxDigit[box][digit] = false;
        }
        return false;
    }

    /** Returns the 3 x 3 box index for a cell. */
    private static int boxIndex(int row, int col) {
        return (row / BOX_SIZE) * BOX_SIZE + (col / BOX_SIZE);
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        char[][] puzzle = {
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
        char[][] solved = {
            {'5', '3', '4', '6', '7', '8', '9', '1', '2'},
            {'6', '7', '2', '1', '9', '5', '3', '4', '8'},
            {'1', '9', '8', '3', '4', '2', '5', '6', '7'},
            {'8', '5', '9', '7', '6', '1', '4', '2', '3'},
            {'4', '2', '6', '8', '5', '3', '7', '9', '1'},
            {'7', '1', '3', '9', '2', '4', '8', '5', '6'},
            {'9', '6', '1', '5', '3', '7', '2', '8', '4'},
            {'2', '8', '7', '4', '1', '9', '6', '3', '5'},
            {'3', '4', '5', '2', '8', '6', '1', '7', '9'}
        };
        char[][] invalid = {
            {'5', '5', '.', '.', '7', '.', '.', '.', '.'},
            {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
            {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
            {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
            {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
            {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
            {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
            {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
            {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };

        char[][] puzzleCopy = copyBoard(puzzle);
        boolean solvedResult = solveSudoku(puzzleCopy);
        System.out.printf("board=puzzle  ->  solved=%s output=%s  expected=%s%n",
            solvedResult, Arrays.deepToString(puzzleCopy), Arrays.deepToString(solved));

        char[][] invalidCopy = copyBoard(invalid);
        boolean invalidResult = solveSudoku(invalidCopy);
        System.out.printf("board=invalid  ->  solved=%s  expected=false%n", invalidResult);
    }

    /** Copies a Sudoku board for demo cases so inputs are not shared. */
    private static char[][] copyBoard(char[][] board) {
        char[][] copy = new char[board.length][];
        for (int i = 0; i < board.length; i++) copy[i] = board[i].clone();
        return copy;
    }
}

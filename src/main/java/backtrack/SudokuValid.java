package backtrack;

import java.util.Arrays;

/**
 * Problem: Valid Sudoku
 *
 * Given a partially filled 9 x 9 Sudoku board, determine whether the filled
 * cells obey Sudoku rules. Empty cells are '.', and the board does not need to
 * be solvable; it only needs to have no duplicate digit in any row, column, or
 * 3 x 3 box.
 *
 * Leetcode: https://leetcode.com/problems/valid-sudoku/
 * Rating:   acceptance 64.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Matrix | Constraint tracking | Row/column/box validation
 *
 * Example:
 *   Input:  a board with two '8' values in the top-left 3 x 3 box
 *   Output: false
 *   Why:    the repeated digit breaks one Sudoku group, so the board is invalid
 *           even though empty cells are ignored.
 *
 * Follow-ups:
 *   1. Validate and solve the board?
 *      Reuse these row/column/box constraints inside Sudoku Solver backtracking.
 *   2. Support arbitrary n^2 x n^2 Sudoku boards?
 *      Parameterize board size and box size, then allocate constraints dynamically.
 *   3. Explain the first violation instead of boolean false?
 *      Return row/column/box metadata when a duplicate is detected.
 *   4. Validate a streaming list of filled cells?
 *      Update three hash sets keyed by row, column, and box as cells arrive.
 *
 * Related: Sudoku Solver (37).
 */
public class SudokuValid {
    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;
    private static final char EMPTY = '.';

    /**
     * Intuition: a filled Sudoku cell participates in exactly three groups: its
     * row, its column, and its 3 x 3 box. The board is invalid the moment the same
     * digit appears twice in any one of those groups. Since the board is fixed at
     * 9 x 9, simple boolean tables are enough to remember what has been seen, and
     * empty cells can be ignored because they do not constrain validity yet.
     *
     * Algorithm:
     *   1. Reject boards that are not a non-null 9 x 9 grid.
     *   2. Create seen tables for rows, columns, and boxes, each indexed by digit 1..9.
     *   3. Scan every cell; ignore dots because they represent empty cells.
     *   4. For each filled digit, compute its digit index and 3 x 3 box index.
     *   5. If that digit was already seen in the same row, column, or box, return
     *      false; otherwise mark it in all three tables.
     *
     * Time:  O(1) - the board always has exactly 81 cells to scan.
     * Space: O(1) because the seen tables are fixed size.
     *
     * @param board partially filled Sudoku board
     * @return true if no filled digit violates Sudoku rules
     */
    public static boolean isValidSudoku(char[][] board) {
        if (!hasValidShape(board)) return false;

        boolean[][] rowSeen = new boolean[SIZE][SIZE];
        boolean[][] colSeen = new boolean[SIZE][SIZE];
        boolean[][] boxSeen = new boolean[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                char cell = board[row][col];
                if (cell == EMPTY) continue;
                if (cell < '1' || cell > '9') return false;

                int digit = cell - '1';
                int box = (row / BOX_SIZE) * BOX_SIZE + (col / BOX_SIZE);
                if (rowSeen[row][digit] || colSeen[col][digit] || boxSeen[box][digit]) return false;

                rowSeen[row][digit] = true;
                colSeen[col][digit] = true;
                boxSeen[box][digit] = true;
            }
        }
        return true;
    }

    /** Checks that the board is a non-null 9 x 9 grid. */
    private static boolean hasValidShape(char[][] board) {
        if (board == null || board.length != SIZE) return false;
        for (char[] row : board) {
            if (row == null || row.length != SIZE) return false;
        }
        return true;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        char[][] validBoard = {
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
        char[][] invalidBoard = copyBoard(validBoard);
        invalidBoard[0][0] = '8';

        char[][][] inputs = {validBoard, invalidBoard};
        boolean[] expected = {true, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = isValidSudoku(inputs[i]);
            System.out.printf("board=%s  ->  %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

    /** Copies a Sudoku board for demo cases so inputs are not shared. */
    private static char[][] copyBoard(char[][] board) {
        char[][] copy = new char[board.length][];
        for (int i = 0; i < board.length; i++) copy[i] = board[i].clone();
        return copy;
    }
}

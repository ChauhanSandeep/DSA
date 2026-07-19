package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Problem: N-Queens
 *
 * Place n queens on an n x n chessboard so no two queens attack each other.
 * Return every valid board configuration, using 'Q' for queens and '.' for
 * empty cells.
 *
 * Leetcode: https://leetcode.com/problems/n-queens/
 * Rating:   acceptance 76.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Row-by-row placement | Column/diagonal pruning
 *
 * Example:
 *   Input:  n = 4
 *   Output: [[.Q.., ...Q, Q..., ..Q.], [..Q., Q..., ...Q, .Q..]]
 *   Why:    these are the two ways to put one queen in each row and column
 *           without sharing either diagonal on a 4 x 4 board.
 *
 * Follow-ups:
 *   1. Count solutions without materializing boards?
 *      Track only occupied columns/diagonals and increment a counter at row n.
 *   2. Use bitmasks for faster conflict checks?
 *      Store columns and both diagonals as masks; available positions are computed with bit operations.
 *   3. Remove mirror-symmetric duplicates?
 *      Restrict the first queen to half the columns and mirror counts carefully for odd n.
 *   4. Return the lexicographically first board only?
 *      Stop DFS after the first completed placement.
 *
 * Related: N-Queens II (52), Sudoku Solver (37).
 */
public class NQueen {

    /**
     * Intuition: place queens one row at a time, because every valid board needs
     * exactly one queen in each row. Once earlier rows are fixed, a new queen is
     * unsafe only if its column or one of its two diagonals is already occupied.
     * Tracking those three attack sets lets us test a square immediately instead
     * of scanning the whole board. When all rows are filled, every queen is safe
     * by construction, so the board is a valid answer.
     *
     * Algorithm:
     *   1. Return an empty list for non-positive n and initialize an n x n board with dots.
     *   2. DFS by row, trying each column as the queen position for that row.
     *   3. Skip a column if its column, main diagonal, or anti-diagonal is already occupied.
     *   4. Place the queen, mark its attacked column and diagonals, recurse to the
     *      next row, then undo those marks before trying another column.
     *   5. When row == n, convert the current board into strings and add it to the answer.
     *
     * Time:  O(n!) - each row chooses from the remaining safe columns, so choices shrink quickly.
     * Space: O(n^2) for the board plus O(n) recursion/sets, excluding output.
     *
     * @param n board size
     * @return all valid n-queens boards
     */
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> solutions = new ArrayList<>();
        if (n <= 0) return solutions;

        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');

        backtrack(0, n, board, new HashSet<>(), new HashSet<>(), new HashSet<>(), solutions);
        return solutions;
    }

    /** Places queens row by row while maintaining occupied columns and diagonals. */
    private static void backtrack(int row, int boardSize, char[][] board,
                                  Set<Integer> occupiedColumns,
                                  Set<Integer> occupiedMainDiagonals,
                                  Set<Integer> occupiedAntiDiagonals,
                                  List<List<String>> solutions) {
        if (row == boardSize) {
            solutions.add(toBoard(board));
            return;
        }

        for (int col = 0; col < boardSize; col++) {
            int mainDiagonal = row - col;
            int antiDiagonal = row + col;
            if (occupiedColumns.contains(col)
                || occupiedMainDiagonals.contains(mainDiagonal)
                || occupiedAntiDiagonals.contains(antiDiagonal)) {
                continue;
            }

            // select -> mark attacks -> work -> unmark attacks
            board[row][col] = 'Q';
            occupiedColumns.add(col);
            occupiedMainDiagonals.add(mainDiagonal);
            occupiedAntiDiagonals.add(antiDiagonal);

            backtrack(row + 1, boardSize, board, occupiedColumns,
                occupiedMainDiagonals, occupiedAntiDiagonals, solutions);

            board[row][col] = '.';
            occupiedColumns.remove(col);
            occupiedMainDiagonals.remove(mainDiagonal);
            occupiedAntiDiagonals.remove(antiDiagonal);
        }
    }

    /** Converts the current character board into the string format returned by LeetCode. */
    private static List<String> toBoard(char[][] board) {
        List<String> boardRows = new ArrayList<>();
        for (char[] row : board) boardRows.add(new String(row));
        return boardRows;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        int[] inputs = {1, 4};
        String[] expected = {
            "[[Q]]",
            "[[.Q.., ...Q, Q..., ..Q.], [..Q., Q..., ...Q, .Q..]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<String>> got = solveNQueens(inputs[i]);
            System.out.printf("n=%d  ->  %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }
}

package backtrack;

import java.util.*;

/**
 * LeetCode: https://leetcode.com/problems/n-queens/
 *
 * Problem:
 * The N-Queens problem asks to place N queens on an N×N chessboard such that no two queens attack each other.
 * Return all valid board configurations.
 *
 * Approach:
 * - We use **backtracking** to explore different placements.
 * - Maintain three sets to track attacked columns, diagonals, and anti-diagonals.
 * - Recursively place queens row by row and backtrack when needed.
 *
 * Time Complexity:  O(N!) → Each queen placement reduces available options significantly.
 * Space Complexity: O(N^2) → Storing board configurations in the result list.
 */
public class NQueen {
    public static void main(String[] args) {
        int n = 4;
        List<List<String>> solutions = solveNQueens(n);
        for (List<String> board : solutions) {
            for (String row : board) {
                System.out.println(row);
            }
            System.out.println();
        }
    }

    /**
     * Solves the N-Queens problem and returns all possible board configurations.
     *
     * @param n The size of the chessboard (n × n).
     * @return A list of valid board configurations.
     */
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> solutions = new ArrayList<>();
        char[][] board = new char[n][n];

        // Initialize board with empty cells ('.')
        for (char[] row : board) Arrays.fill(row, '.');

        backtrack(0, n, board, new HashSet<>(), new HashSet<>(), new HashSet<>(), solutions);
        return solutions;
    }

    /**
     * Backtracking function to place queens on the board.
     *
     * @param row          The current row being processed.
     * @param n            The size of the board.
     * @param board        The chessboard representation.
     * @param columns      Set to track occupied columns.
     * @param diagonals    Set to track occupied major diagonals.
     * @param antiDiagonals Set to track occupied minor diagonals.
     * @param solutions    The list storing valid board configurations.
     */
    private static void backtrack(int row, int n, char[][] board, Set<Integer> columns,
                                  Set<Integer> diagonals, Set<Integer> antiDiagonals, List<List<String>> solutions) {
        // Base case: All queens are placed
        if (row == n) {
            solutions.add(convertBoardToList(board));
            return;
        }

        for (int col = 0; col < n; col++) {
            int diagonal = row - col;
            int antiDiagonal = row + col;

            // Skip if placing a queen here is unsafe
            if (columns.contains(col) || diagonals.contains(diagonal) || antiDiagonals.contains(antiDiagonal)) continue;

            // Place queen
            board[row][col] = 'Q';
            columns.add(col);
            diagonals.add(diagonal);
            antiDiagonals.add(antiDiagonal);

            // Recur to the next row
            backtrack(row + 1, n, board, columns, diagonals, antiDiagonals, solutions);

            // Remove queen (backtrack)
            board[row][col] = '.';
            columns.remove(col);
            diagonals.remove(diagonal);
            antiDiagonals.remove(antiDiagonal);
        }
    }

    /**
     * Converts a 2D board representation into a list of strings.
     *
     * @param board The board state.
     * @return List representation of the board.
     */
    private static List<String> convertBoardToList(char[][] board) {
        List<String> boardList = new ArrayList<>();
        for (char[] row : board) {
            boardList.add(new String(row));
        }
        return boardList;
    }
}

package Graph;

import java.util.Arrays;

/**
 * Minesweeper problem
 * LeetCode: https://leetcode.com/problems/minesweeper/
 *
 * --- Problem Description ---
 * Given a Minesweeper board and a click position, update the board:
 * - If a mine ('M') is clicked, change it to 'X' (game over).
 * - If an empty ('E') cell is clicked:
 *   - If adjacent to mines, update with mine count ('1'-'8').
 *   - Otherwise, reveal it as 'B' and continue revealing adjacent 'E' cells recursively.
 *
 * --- Approach ---
 * 1. If the clicked cell is a mine, change it to 'X' and return.
 * 2. Use **DFS (Depth-First Search)** to explore adjacent cells.
 * 3. If a cell has adjacent mines, update it with the count.
 * 4. Otherwise, mark it as 'B' and recursively process neighbors.
 *
 * --- Complexity Analysis ---
 * - **Time Complexity:** O(m * n) in the worst case (all cells revealed).
 * - **Space Complexity:** O(m * n) for recursion depth in DFS.
 */
public class Minesweeper {

    // Constants for board characters
    private static final char UNREVEALED_MINE = 'M';
    private static final char UNREVEALED_EMPTY = 'E';
    private static final char REVEALED_BLANK = 'B';
    private static final char REVEALED_MINE = 'X';

    // Direction vectors for 8 possible adjacent cells (up, down, left, right, diagonals)
    private static final int[][] DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, 
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    public static void main(String[] args) {
        char[][] board = {
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'E', 'M', 'E', 'E'},
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'E', 'E', 'E', 'E'}
        };
        int[] click = {3, 0};
        
        char[][] updatedBoard = new Minesweeper().updateBoard(board, click);
        System.out.println(Arrays.deepToString(updatedBoard));
    }

    /**
     * Updates the Minesweeper board based on the user's click.
     * 
     * @param board The given Minesweeper board.
     * @param click The position [row, col] where the user clicked.
     * @return The updated board after applying the move.
     */
    public char[][] updateBoard(char[][] board, int[] click) {
        int rows = board.length, cols = board[0].length;
        int x = click[0], y = click[1];

        // Base case: If the click is on a mine, game over
        if (board[x][y] == UNREVEALED_MINE) {
            board[x][y] = REVEALED_MINE;
        } else {
            revealCells(board, x, y, rows, cols);
        }
        return board;
    }

    /**
     * Reveals cells recursively using DFS.
     * - Stops if the cell is already revealed.
     * - If adjacent mines exist, updates the cell with mine count.
     * - Otherwise, marks it as 'B' and expands in all 8 directions.
     */
    private void revealCells(char[][] board, int row, int col, int rows, int cols) {
        // Boundary & already revealed checks
        if (row < 0 || row >= rows || col < 0 || col >= cols || board[row][col] != UNREVEALED_EMPTY) {
            return;
        }

        // Count adjacent mines
        int mineCount = countAdjacentMines(board, row, col, rows, cols);

        if (mineCount > 0) {
            board[row][col] = (char) ('0' + mineCount); // Set mine count
        } else {
            board[row][col] = REVEALED_BLANK; // Mark as blank ('B')
            for (int[] dir : DIRECTIONS) {
                revealCells(board, row + dir[0], col + dir[1], rows, cols);
            }
        }
    }

    /**
     * Counts the number of mines in adjacent 8 directions.
     */
    private int countAdjacentMines(char[][] board, int row, int col, int rows, int cols) {
        int count = 0;
        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0], newCol = col + dir[1];
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && board[newRow][newCol] == UNREVEALED_MINE) {
                count++;
            }
        }
        return count;
    }
}

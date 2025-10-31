package arrays.simulation;

import java.util.Arrays;

/**
 * Problem: Game of Life (Conway's Game of Life)
 *
 * Given a m x n board representing current state of each cell (1 = live, 0 = dead),
 * update the board to its next state using the given rules. The transformation must happen in-place.
 *
 * LeetCode Link:
 * https://leetcode.com/problems/game-of-life/
 */
public class GameOfLife {

    // Cell state constants
    private static final int DEAD = 0;
    private static final int LIVE = 1;
    private static final int LIVE_TO_DEAD = 2;   // was LIVE, now DEAD
    private static final int DEAD_TO_LIVE = -1;  // was DEAD, now LIVE

    public static void main(String[] args) {
        int[][] board = {
            {0, 1, 0},
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
        };

        new GameOfLife().gameOfLife(board);
        System.out.println(Arrays.deepToString(board));
    }

    /**
     * Updates the board to the next generation in-place using transitional states.
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(1)
     *
     * @param board m x n input grid
     */
    public void gameOfLife(int[][] board) {
        if (board == null || board.length == 0) return;

        int rows = board.length, cols = board[0].length;

        // First pass: determine the new transitional state
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int liveNeighbors = countLiveNeighbors(board, row, col);

                if (board[row][col] == LIVE) {
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        board[row][col] = LIVE_TO_DEAD; // Live → Dead
                    }
                } else if (board[row][col] == DEAD) {
                    if (liveNeighbors == 3) {
                        board[row][col] = DEAD_TO_LIVE; // Dead → Live
                    }
                }
            }
        }

        // Second pass: finalize the transformation
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col] == LIVE_TO_DEAD) board[row][col] = DEAD;

                else if (board[row][col] == DEAD_TO_LIVE) board[row][col] = LIVE;
            }
        }
    }

    /**
     * Counts the number of live neighbors for a given cell.
     * Treats LIVE and LIVE_TO_DEAD as "was originally live".
     */
    private int countLiveNeighbors(int[][] board, int row, int col) {
        int liveCount = 0;

        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},          {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            if (r >= 0 && r < board.length && c >= 0 && c < board[0].length) {
                if (board[r][c] == LIVE || board[r][c] == LIVE_TO_DEAD) {
                    liveCount++;
                }
            }
        }

        return liveCount;
    }
}
package arrays.simulation;

import java.util.Arrays;

/**
 * Problem: Game of Life
 *
 * Given a board of live and dead cells, update it in place to the next generation
 * using Conway's Game of Life rules. Every cell's next state depends on the
 * original states of its eight neighbors, so updates cannot destroy old state
 * before neighboring cells have been evaluated.
 *
 * Leetcode: https://leetcode.com/problems/game-of-life/
 * Rating:   acceptance 72.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Matrix | Simulation | In-place transitional states
 *
 * Example:
 *   Input:  [[0,1,0],[0,0,1],[1,1,1],[0,0,0]]
 *   Output: [[0,0,0],[1,0,1],[0,1,1],[0,1,0]]
 *   Why:    each cell is updated from the original live-neighbor counts, including
 *           births for dead cells with exactly three live neighbors.
 *
 * Follow-ups:
 *   1. What if the board is infinite but sparse?
 *      Store only live cells in a set and count neighbors around those cells.
 *   2. What if many generations are requested?
 *      Detect repeated board states or use sparse updates when the live set is small.
 *   3. What if the board is too large for memory?
 *      Process tiles with halo borders so neighbor counts remain correct.
 */
public class GameOfLife {

    // Cell state constants
    private static final int DEAD = 0;
    private static final int LIVE = 1;
    private static final int LIVE_TO_DEAD = 2;   // was LIVE, now DEAD
    private static final int DEAD_TO_LIVE = -1;  // was DEAD, now LIVE
    public static void main(String[] args) {
        GameOfLife solver = new GameOfLife();
        int[][][] inputs = {
            {{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}},
            {{1, 1}, {1, 0}}
        };
        String[] expected = {
            "[[0, 0, 0], [1, 0, 1], [0, 1, 1], [0, 1, 0]]",
            "[[1, 1], [1, 1]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            int[][] board = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            solver.gameOfLife(board);
            System.out.printf("board=%s -> %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), Arrays.deepToString(board), expected[i]);
        }
    }

    /**
     * Intuition: every cell must be judged using the old board, but the problem
     * asks us to write the next board in place. The trick is to use transitional
     * values that remember both states: a live cell that will die is marked 2, and
     * a dead cell that will become live is marked -1. During neighbor counting,
     * values 1 and 2 both mean the cell was originally live. A second pass then
     * collapses the transitional values into final 0 or 1 states.
     *
     * Time:  O(rows * cols) - each cell checks a fixed set of eight neighbors.
     * Space: O(1) - the board itself stores transitional states.
     *
     * @param board grid of 0 dead cells and 1 live cells, modified in place
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
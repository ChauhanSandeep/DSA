package graphs;


import java.util.Arrays;
/**
 * Problem: Battleships in a Board
 *
 * Given a board containing 'X' battleship cells and '.' empty water, count how
 * many battleships are present. Ships are straight horizontal or vertical lines,
 * and valid boards keep separate ships from touching each other.
 *
 * Leetcode: https://leetcode.com/problems/battleships-in-a-board/ (Medium)
 * Rating:   acceptance 77.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Matrix | One-pass counting | Top-left representative
 *
 * Example:
 *   Input:  board = [[X,.,.,X],[.,.,.,X],[.,.,.,X]]
 *   Output: 2
 *   Why:    the single X in the first column starts one ship, and the vertical
 *           run in the last column starts one more ship.
 *
 * Follow-ups:
 *   1. Validate whether an arbitrary board is legal?
 *      Check that no ship bends and no two ships touch in forbidden positions.
 *   2. What if diagonal ships are allowed?
 *      Count connected components using eight directions instead of this corner rule.
 *   3. What if ships can be rectangles?
 *      Flood-fill each component and validate that it fills its bounding rectangle.
 *
 * Related: Number of Islands (200), Max Area of Island (695).
 */
public class BattleshipsInABoard {


    public static void main(String[] args) {
        BattleshipsInABoard solver = new BattleshipsInABoard();
        char[][][] inputs = {
            {{'X', '.', '.', 'X'}, {'.', '.', '.', 'X'}, {'.', '.', '.', 'X'}},
            {{'.'}}
        };
        int[] expected = {2, 0};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.countBattleships(inputs[i]);
            System.out.printf("board=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: each battleship is a straight horizontal or vertical run of X cells.
     * Counting every X would overcount, so count only the first cell of each ship:
     * the cell that has no X directly above it and no X directly to its left.
     *
     * Algorithm:
     *   1. Scan every board cell in row-major order.
     *   2. Skip water cells.
     *   3. Skip X cells that continue a ship from above or from the left.
     *   4. Count the remaining X cells as ship starts.
     *
     * Time:  O(m*n) - every cell is inspected once.
     * Space: O(1) - only the running count is stored.
     *
     * @param board grid containing 'X' ship cells and '.' water cells
     * @return number of battleships on the board
     */
    public int countBattleships(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return 0;
        }

        int rows = board.length;
        int cols = board[0].length;
        int battleshipCount = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Skip if current cell is not a battleship part
                if (board[i][j] != 'X') {
                    continue;
                }

                // Check if this is the top-left corner of a battleship
                boolean isTopLeft = true;

                // Check if there's an 'X' above
                if (i > 0 && board[i - 1][j] == 'X') {
                    isTopLeft = false;
                }

                // Check if there's an 'X' to the left
                if (j > 0 && board[i][j - 1] == 'X') {
                    isTopLeft = false;
                }

                // If this is a top-left corner, count it as a battleship
                if (isTopLeft) {
                    battleshipCount++;
                }
            }
        }

        return battleshipCount;
    }

    /**
     * Alternative DFS approach that finds connected components.
     * This approach modifies the board by marking visited cells.
     *
     * Steps:
     * 1. For each 'X' cell, initiate a DFS to mark the entire battleship as visited.
     * 2. Increment the battleship count for each DFS initiation.
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(m * n) for recursion stack in worst case
     */
    public int countBattleshipsDFS(char[][] board) {
        if (board == null || board.length == 0) return 0;

        int rows = board.length;
        int cols = board[0].length;
        int battleshipCount = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'X') {
                    battleshipCount++;
                    // Mark entire battleship as visited
                    dfs(board, i, j, rows, cols);
                }
            }
        }

        return battleshipCount;
    }

    // DFS to mark entire battleship as visited
    private void dfs(char[][] board, int row, int col, int rows, int cols) {
        // Check bounds and if current cell is part of battleship
        if (row < 0 || row >= rows || col < 0 || col >= cols || board[row][col] != 'X') {
            return;
        }

        // Mark current cell as visited
        board[row][col] = '.';

        // Visit all four directions
        dfs(board, row + 1, col, rows, cols);  // Down
        dfs(board, row - 1, col, rows, cols);  // Up
        dfs(board, row, col + 1, rows, cols);  // Right
        dfs(board, row, col - 1, rows, cols);  // Left
    }
}

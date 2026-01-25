package graphs;

/**
 * Problem: Battleships in a Board
 *
 * Given an m x n matrix board where each cell is a battleship 'X' or empty '.', return the number
 * of the battleships on board. Battleships can only be placed horizontally or vertically on board.
 * In other words, they can only be made of the shape 1 x k (1 row, k columns) or k x 1 (k rows, 1 column),
 * where k can be of any size. At least one horizontal or vertical cell separates between two battleships.
 *
 * Example:
 * Input: board = [
 * ["X",".",".","X"],
 * [".",".",".","X"],
 * [".",".",".","X"]
 * ]
 * Output: 2
 * Explanation: There are 2 battleships on the board.
 *
 * LeetCode: https://leetcode.com/problems/battleships-in-a-board
 *
 * Follow-up Questions:
 * 1. Could you solve it in one-pass, using only O(1) extra memory and without modifying the board?
 *    Answer: Yes, count only the top-left corner of each battleship (cells with no 'X' above or left).
 *
 * 2. What if battleships could be placed diagonally?
 *    Answer: Use DFS/BFS to find connected components considering diagonal directions.
 *
 * 3. How would you validate if the board configuration is valid?
 *    Answer: Check that no adjacent battleships exist and all battleships are rectangular.
 *    Related: https://leetcode.com/problems/number-of-islands/
 *
 * @author Sandeep
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class BattleshipsInABoard {

    /**
     * Counts battleships by identifying top-left corners (one-pass, O(1) space).
     *
     * Algorithm:
     * 1. For each 'X' cell, check if it's the top-left corner of a battleship
     * 2. A cell is top-left if there's no 'X' above it and no 'X' to its left
     * 3. Count all such top-left corner cells
     *
     * Time Complexity: O(m * n) where m and n are board dimensions
     * Space Complexity: O(1) as required by follow-up
     *
     * @param board 2D character array representing the board
     * @return Number of battleships on the board
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
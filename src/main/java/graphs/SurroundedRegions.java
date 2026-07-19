package graphs;

import java.util.Arrays;

/**
 * Problem: Surrounded Regions
 *
 * Given a board of X and O, flip every O region that is completely surrounded by
 * X. Any O connected to the border is safe and must remain O.
 *
 * Leetcode: https://leetcode.com/problems/surrounded-regions/
 * Rating:   acceptance 45.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Boundary DFS | Reverse capture
 *
 * Example:
 *   Input:  board = [[X,X,X,X],[X,O,O,X],[X,X,O,X],[X,O,X,X]]
 *   Output: [[X,X,X,X],[X,X,X,X],[X,X,X,X],[X,O,X,X]]
 *   Why:    the bottom O touches the border and survives; the middle O component
 *           has no border connection, so it is captured.
 *
 * Follow-ups:
 *   1. Avoid recursion for very large boards?
 *      Use a queue from border O cells instead of recursive DFS.
 *   2. Return the number of cells flipped?
 *      Increment a counter when converting remaining O cells to X.
 *   3. Capture regions under diagonal connectivity?
 *      Add diagonal directions to the border traversal and component definition.
 *
 * Related: Number of Islands (200), Pacific Atlantic Water Flow (417), Walls and Gates (286).
 */
public class SurroundedRegions {

    public static void main(String[] args) {
        SurroundedRegions solver = new SurroundedRegions();
        char[][][] boards = {{{'X', 'X', 'X', 'X'}, {'X', 'O', 'O', 'X'}, {'X', 'X', 'O', 'X'}, {'X', 'O', 'X', 'X'}}, {{'O'}}};
        char[][][] expected = {{{'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}, {'X', 'O', 'X', 'X'}}, {{'O'}}};
        for (int i = 0; i < boards.length; i++) {
            String input = Arrays.deepToString(boards[i]);
            solver.solve(boards[i]);
            System.out.printf("board=%s -> %s  expected=%s%n", input, Arrays.deepToString(boards[i]), Arrays.deepToString(expected[i]));
        }
    }
    public void solve(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }

        int m = board.length;
        int n = board[0].length;

        // Mark all 'O's connected to the borders as 'T' (temporary)
        // Check first and last column
        for (int i = 0; i < m; i++) {
            if (board[i][0] == 'O') {
                dfs(board, i, 0);
            }
            if (board[i][n - 1] == 'O') {
                dfs(board, i, n - 1);
            }
        }

        // Check first and last row
        for (int j = 0; j < n; j++) {
            if (board[0][j] == 'O') {
                dfs(board, 0, j);
            }
            if (board[m - 1][j] == 'O') {
                dfs(board, m - 1, j);
            }
        }

        // Convert remaining 'O's to 'X' and restore 'T's to 'O's
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                } else if (board[i][j] == 'T') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    // Helper method to perform DFS and mark connected 'O's as 'T'
    private void dfs(char[][] board, int i, int j) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'O') {
            return;
        }

        // Mark as visited
        board[i][j] = 'T';

        // Explore 4-directionally
        dfs(board, i + 1, j);
        dfs(board, i - 1, j);
        dfs(board, i, j + 1);
        dfs(board, i, j - 1);
    }
}

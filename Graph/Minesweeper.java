package Graph;

import java.util.Arrays;

/**
 * Minesweeper problem
 * https://leetcode.com/problems/minesweeper/
 */
public class Minesweeper {

    private static final char UNREVEALED_MINE = 'M';
    private static final char UNREVEALED_EMPTY = 'E';
    private static final char REVEALED_BLANK = 'B';
    private static final char REVEALED_MINE = 'X';

    // Direction vectors (8 directions)
    private static final int[][] dirs = {
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
        char[][] result = new Minesweeper().updateBoard(board, click);
        System.out.println(Arrays.deepToString(result));
    }

    public char[][] updateBoard(char[][] board, int[] click) {
        if (board.length == 0 || board[0].length == 0 || click.length != 2) return board;
        int x = click[0], y = click[1];

        if (board[x][y] == UNREVEALED_MINE) {
            board[x][y] = REVEALED_MINE; // Game over scenario
        } else {
            dfs(board, x, y, board.length, board[0].length);
        }
        return board;
    }

    private void dfs(char[][] board, int x, int y, int rows, int cols) {
        // Base case: Stop at already revealed cells or out-of-bounds
        if (x < 0 || x >= rows || y < 0 || y >= cols || board[x][y] == REVEALED_BLANK || (board[x][y] >= '1' && board[x][y] <= '8')) {
            return;
        }

        int mineCount = countAdjacentMines(board, x, y, rows, cols);
        if (mineCount > 0) {
            board[x][y] = (char) ('0' + mineCount);
        } else {
            board[x][y] = REVEALED_BLANK;
            for (int[] dir : dirs) {
                dfs(board, x + dir[0], y + dir[1], rows, cols);
            }
        }
    }

    private int countAdjacentMines(char[][] board, int x, int y, int rows, int cols) {
        int count = 0;
        for (int[] dir : dirs) {
            int newX = x + dir[0], newY = y + dir[1];
            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && board[newX][newY] == UNREVEALED_MINE) {
                count++;
            }
        }
        return count;
    }
}

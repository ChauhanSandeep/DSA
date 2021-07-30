package Graph;

import java.util.Arrays;

/**
 * Minesweeper problem
 * https://leetcode.com/problems/minesweeper/
 */
public class Minesweeper {

    private static final char UNREVEALED_MINE= 'M';
    private static final char UNREVEALED_EMPTY= 'E';
    private static final char REVEALED_BLANK= 'B';
    private static final char REVEALED_MINE= 'X';

    public static void main(String[] args) {
        char[][] board = {
                {'E','E','E','E','E'},
                {'E','E','M','E','E'},
                {'E','E','E','E','E'},
                {'E','E','E','E','E'}};
        int[] click = {3, 0};
        char[][] result = new Minesweeper().updateBoard(board, click);
        System.out.println(Arrays.deepToString(result));
    }

    public char[][] updateBoard(char[][] board, int[] click) {
        if (board.length == 0 || board[0].length == 0 || click.length != 2) return board;
        int x = click[0], y = click[1], rows = board.length, cols = board[0].length;
        if (board[x][y] == UNREVEALED_MINE) {
            board[x][y] = REVEALED_MINE;
        } else {
            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            dfs(board, x, y, rows, cols, dirs);
        }
        return board;
    }

    private void dfs(char[][] board, int x, int y, int rows, int cols, int[][] dirs) {
        if (x < 0 || x >= rows || y < 0 || y >= cols || board[x][y] != UNREVEALED_EMPTY) return;
        int mine = adjMine(board, x, y, rows, cols);
        if (mine > 0) {
            board[x][y] = (char) ('0' + mine);
        } else {
            board[x][y] = REVEALED_BLANK;
            for (int[] d : dirs) {
                dfs(board, x + d[0], y + d[1], rows, cols, dirs);
            }
        }
    }

    private int adjMine(char[][] board, int x, int y, int rows, int cols) {
        int cnt = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && board[i][j] == UNREVEALED_MINE)
                    cnt++;
            }
        }
        return cnt;
    }
}

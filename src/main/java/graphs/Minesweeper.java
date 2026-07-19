package graphs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Problem: Minesweeper
 *
 * Given a Minesweeper board and a click, reveal the board according to the game
 * rules. Clicking a mine marks it as X. Clicking an empty cell either writes the
 * adjacent mine count or expands blank cells when that count is zero.
 *
 * Leetcode: https://leetcode.com/problems/minesweeper/ (Medium)
 * Rating:   acceptance 68.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | DFS/BFS reveal | Eight-direction grid search
 *
 * Example:
 *   Input:  board = [[E,E,E,E,E],[E,E,M,E,E],[E,E,E,E,E],[E,E,E,E,E]], click = [3,0]
 *   Output: [[B,1,E,1,B],[B,1,M,1,B],[B,1,1,1,B],[B,B,B,B,B]]
 *   Why:    the clicked empty region has no adjacent mines in blank areas, so it
 *           expands until cells bordering the mine receive numbers.
 *
 * Follow-ups:
 *   1. Avoid recursion on a large board?
 *      Use the BFS method with a queue.
 *   2. Support custom neighbor shapes?
 *      Replace the eight-direction list with the game's adjacency rules.
 *   3. Reveal many clicks efficiently?
 *      Mutate the board in place and ignore clicks on already revealed cells.
 *
 * Related: Flood Fill (733), Surrounded Regions (130).
 */
public class Minesweeper {


    public static void main(String[] args) {
        Minesweeper solver = new Minesweeper();
        char[][][] boards = {
            {{'E', 'E', 'E', 'E', 'E'}, {'E', 'E', 'M', 'E', 'E'}, {'E', 'E', 'E', 'E', 'E'}, {'E', 'E', 'E', 'E', 'E'}},
            {{'M'}}
        };
        int[][] clicks = {{3, 0}, {0, 0}};
        String[] expected = {
            "[[B, 1, E, 1, B], [B, 1, M, 1, B], [B, 1, 1, 1, B], [B, B, B, B, B]]",
            "[[X]]"
        };
        for (int i = 0; i < boards.length; i++) {
            char[][] board = Arrays.stream(boards[i]).map(char[]::clone).toArray(char[][]::new);
            char[][] output = solver.updateBoardDFS(board, clicks[i]);
            System.out.printf("board=%s click=%s  ->  %s  expected=%s%n",
                Arrays.deepToString(boards[i]), Arrays.toString(clicks[i]), Arrays.deepToString(output), expected[i]);
        }
    }
  private static final char UNREVEALED_MINE = 'M';
  private static final char UNREVEALED_EMPTY = 'E';
  private static final char REVEALED_BLANK = 'B';
  private static final char REVEALED_MINE = 'X';

  // Direction vectors for 8 neighbors
  private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    /**
     * Intuition: revealing an empty Minesweeper cell is flood fill with a boundary.
     * A cell next to a mine becomes a digit and stops expanding; a cell with zero
     * adjacent mines becomes blank and recursively reveals its eight neighbors.
     *
     * Algorithm:
     *   1. If the clicked cell is a mine, mark it X and stop.
     *   2. Otherwise DFS from the clicked empty cell.
     *   3. Count adjacent mines for each revealed cell.
     *   4. Write a digit when the count is positive; otherwise write B and reveal neighbors.
     *
     * Time:  O(m*n) - each cell can be revealed at most once.
     * Space: O(m*n) - recursion can include many revealed cells.
     *
     * @param board Minesweeper board
     * @param click clicked coordinate [row, col]
     * @return updated board
     */
  public char[][] updateBoardDFS(char[][] board, int[] click) {
    int row = click[0], col = click[1];

    if (board[row][col] == UNREVEALED_MINE) {
      board[row][col] = REVEALED_MINE;
    } else {
      revealCellsDFS(board, row, col);
    }
    return board;
  }

  private void revealCellsDFS(char[][] board, int row, int col) {
    int rows = board.length, cols = board[0].length;

    if (row < 0 || row >= rows || col < 0 || col >= cols || board[row][col] != UNREVEALED_EMPTY) {
      return;
    }

    int adjacentMines = countAdjacentMines(board, row, col);
    if (adjacentMines > 0) {
      board[row][col] = (char) ('0' + adjacentMines);
    } else {
      board[row][col] = REVEALED_BLANK;
      // Recursively reveal adjacent cells
      for (int[] dir : DIRECTIONS) {
        revealCellsDFS(board, row + dir[0], col + dir[1]);
      }
    }
  }
    /**
     * Intuition: BFS reveals the same region as DFS without using the call stack.
     * Cells are pulled from a queue, numbered when adjacent mines exist, or marked
     * blank and used to enqueue neighboring unrevealed empty cells.
     *
     * Algorithm:
     *   1. If the clicked cell is a mine, mark it X and stop.
     *   2. Enqueue the clicked cell and process cells in FIFO order.
     *   3. Count adjacent mines; write a digit and do not expand when positive.
     *   4. Otherwise mark B and enqueue unrevealed empty neighbors.
     *
     * Time:  O(m*n) - each cell can be processed at most once.
     * Space: O(m*n) - the queue can hold many cells in the worst case.
     *
     * @param board Minesweeper board
     * @param click clicked coordinate [row, col]
     * @return updated board
     */
  public char[][] updateBoardBFS(char[][] board, int[] click) {
    int row = click[0], col = click[1];
    if (board[row][col] == UNREVEALED_MINE) {
      board[row][col] = REVEALED_MINE;
      return board;
    }

    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{row, col});

    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      int r = cell[0], c = cell[1];

        if (board[r][c] != UNREVEALED_EMPTY) {
            continue;
        }

      int adjacentMines = countAdjacentMines(board, r, c);
      if (adjacentMines > 0) {
        board[r][c] = (char) ('0' + adjacentMines);
      } else {
        board[r][c] = REVEALED_BLANK;
        for (int[] dir : DIRECTIONS) {
          int newRow = r + dir[0], newCol = c + dir[1];
          if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length
              && board[newRow][newCol] == UNREVEALED_EMPTY) {
            queue.offer(new int[]{newRow, newCol});
          }
        }
      }
    }
    return board;
  }

  /**
   * Counts the adjacent mines around a cell
   */
  private int countAdjacentMines(char[][] board, int row, int col) {
    int count = 0;
    for (int[] dir : DIRECTIONS) {
      int newRow = row + dir[0], newCol = col + dir[1];
      if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length
          && board[newRow][newCol] == UNREVEALED_MINE) {
        count++;
      }
    }
    return count;
  }
}

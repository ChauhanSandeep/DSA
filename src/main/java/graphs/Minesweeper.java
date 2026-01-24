package graphs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Problem: Minesweeper Board Update
 * LeetCode: https://leetcode.com/problems/minesweeper/
 *
 * --- Problem Statement ---
 * You are given a Minesweeper board and a click position:
 *  - If a mine ('M') is clicked, it turns into 'X' (game over).
 *  - If an empty ('E') cell is clicked:
 *      - If adjacent to mines, update with mine count ('1'-'8').
 *      - Otherwise, reveal as 'B' and recursively reveal neighbors.
 *
 * Example:
 * Input:
 * board =
 * [ ['E','E','E','E','E'],
 *   ['E','E','M','E','E'],
 *   ['E','E','E','E','E'],
 *   ['E','E','E','E','E'] ]
 *
 * click = [3,0]
 *
 * Output:
 * [ ['B','1','E','1','B'],
 *   ['B','1','M','1','B'],
 *   ['B','1','1','1','B'],
 *   ['B','B','B','B','B'] ]
 *
 * --- Follow-up Questions ---
 * 1. Can we solve this using BFS instead of DFS?
 *    - Yes, BFS can be applied using a queue to reveal cells iteratively instead of recursion.
 * 2. How to handle large boards efficiently?
 *    - Use BFS to avoid stack overflow from recursion.
 *    - Use a visited set or modify the board in-place to avoid re-processing cells.
 * 3. Can this be extended to multiplayer or streaming boards?
 *    - Yes, but would require incremental updates and careful synchronization.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class Minesweeper {

  private static final char UNREVEALED_MINE = 'M';
  private static final char UNREVEALED_EMPTY = 'E';
  private static final char REVEALED_BLANK = 'B';
  private static final char REVEALED_MINE = 'X';

  // Direction vectors for 8 neighbors
  private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

  public static void main(String[] args) {
    char[][] board =
        {{'E', 'E', 'E', 'E', 'E'}, {'E', 'E', 'M', 'E', 'E'}, {'E', 'E', 'E', 'E', 'E'}, {'E', 'E', 'E', 'E', 'E'}};
    int[] click = {3, 0};

    Minesweeper solver = new Minesweeper();

    System.out.println("DFS Solution:");
    char[][] updatedBoardDFS = solver.updateBoardDFS(board, click);
    System.out.println(Arrays.deepToString(updatedBoardDFS));

    // Reset board for BFS demo
    char[][] board2 =
        {{'E', 'E', 'E', 'E', 'E'}, {'E', 'E', 'M', 'E', 'E'}, {'E', 'E', 'E', 'E', 'E'}, {'E', 'E', 'E', 'E', 'E'}};

    System.out.println("BFS Solution:");
    char[][] updatedBoardBFS = solver.updateBoardBFS(board2, click);
    System.out.println(Arrays.deepToString(updatedBoardBFS));
  }

  /**
   * DFS Approach to update the board
   *
   * Steps:
   * 1. If the cell is a mine, mark it as 'X'.
   * 2. If the cell is empty, recursively reveal adjacent cells.
   * 3. If the cell has adjacent mines, mark it with the number of mines.
   * 4. If the cell has no adjacent mines, mark it as 'B' and recursively reveal adjacent cells.
   *
   * Time Complexity: O(m * n), where m is the number of rows and n is the number of columns.
   * Space Complexity: O(m * n), due to recursion stack.
   *
   * @param board Minesweeper board
   * @param click position [row, col]
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
   * BFS Approach to update the board
   *
   * @param board Minesweeper board
   * @param click position [row, col]
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

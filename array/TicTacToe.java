package array;

/**
 * 🔗 LeetCode: https://leetcode.com/problems/design-tic-tac-toe/
 *
 * Problem:
 * Design a TicTacToe class for an n x n board where two players play by calling `move(row, col, player)`.
 * Determine the winner after each move in O(1) time.
 *
 * Example:
 * Input:
 * TicTacToe toe = new TicTacToe(3);
 * toe.move(0, 0, 1); -> 0
 * toe.move(0, 2, 2); -> 0
 * toe.move(2, 2, 1); -> 0
 * toe.move(1, 1, 2); -> 0
 * toe.move(2, 0, 1); -> 0
 * toe.move(1, 0, 2); -> 0
 * toe.move(2, 1, 1); -> 1 (Player 1 wins)
 *
 * Time Complexity per move: O(1)
 * Space Complexity: O(N)
 */
public class TicTacToe {

  private final int n;              // Size of the board (n x n).
  private final int[] rowCounts;    // Stores counts for each row. When count reaches n or -n, player wins.
  private final int[] colCounts;    // Stores counts for each column. When count reaches n or -n, player wins.
  private int diagCount;            // Count for the main diagonal (top-left to bottom-right).
  private int antiDiagCount;        // Count for the anti-diagonal (top-right to bottom-left).

  /** Initialize the board. */
  public TicTacToe(int n) {
    this.n = n;
    this.rowCounts = new int[n];
    this.colCounts = new int[n];
    this.diagCount = 0;
    this.antiDiagCount = 0;
  }

  /**
   * Player makes a move at (row, col).
   *
   * ✅ Strategy:
   * - Use +1 for Player 1, -1 for Player 2
   * - Keep count of how many of each player's moves are in a row/column/diagonal
   * - If any count reaches `n` or `-n`, that player wins
   *
   * @param row row index
   * @param col column index
   * @param player 1 or 2
   * @return 0 if no one wins, 1 if Player 1 wins, 2 if Player 2 wins
   */
  public int move(int row, int col, int player) {
    int delta = player == 1 ? 1 : -1;

    rowCounts[row] += delta;
    colCounts[col] += delta;

    if (row == col) {
      diagCount += delta;
    }

    if (row + col == n - 1) {
      antiDiagCount += delta;
    }

    if (Math.abs(rowCounts[row]) == n || Math.abs(colCounts[col]) == n || Math.abs(diagCount) == n
        || Math.abs(antiDiagCount) == n) {
      return player;
    }

    return 0;
  }

  public static void main(String[] args) {
    TicTacToe toe = new TicTacToe(2);
    System.out.println(toe.move(0, 1, 1)); // 0
    System.out.println(toe.move(1, 1, 2)); // 0
    System.out.println(toe.move(1, 0, 1)); // 1
  }
}
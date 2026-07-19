package arrays;

import java.util.Arrays;
/**
 * Problem: Design Tic-Tac-Toe
 *
 * Design an n x n Tic-Tac-Toe board that receives moves one at a time. After each
 * move, return 0 if nobody has won yet, or the winning player number if that move
 * completes a row, column, or diagonal.
 *
 * Leetcode: https://leetcode.com/problems/design-tic-tac-toe/ (Medium)
 * Rating:   acceptance 58.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Design | Row/column counters
 *
 * Example:
 *   Input:  n = 3, moves = [[0,0,1],[0,2,2],[2,2,1],[1,1,2],[2,0,1],[1,0,2],[2,1,1]]
 *   Output: [0,0,0,0,0,0,1]
 *   Why:    player 1 fills the bottom row on the final move, so that move returns 1.
 *
 * Follow-ups:
 *   1. Reject invalid moves on occupied cells?
 *      Store the board or a set of occupied cells in addition to the counters.
 *   2. Support undo?
 *      Record move history and subtract the same counter deltas when undoing.
 *   3. Generalize to k-in-a-row on a larger board?
 *      Track directional streaks from the latest move instead of whole-row counts.
 *
 * Related: Valid Tic-Tac-Toe State (794), Find Winner on a Tic Tac Toe Game (1275).
 */
public class TicTacToe {

    public static void main(String[] args) {
        int[][] winningMoves = {
            {0, 0, 1}, {0, 2, 2}, {2, 2, 1}, {1, 1, 2}, {2, 0, 1}, {1, 0, 2}, {2, 1, 1}
        };
        int[] winningResults = new int[winningMoves.length];
        TicTacToe winningGame = new TicTacToe(3);
        for (int i = 0; i < winningMoves.length; i++) {
            winningResults[i] = winningGame.move(winningMoves[i][0], winningMoves[i][1], winningMoves[i][2]);
        }
        int[] winningExpected = { 0, 0, 0, 0, 0, 0, 1 };
        System.out.printf("moves=%s  ->  %s  expected=%s%n",
            Arrays.deepToString(winningMoves), Arrays.toString(winningResults), Arrays.toString(winningExpected));

        int[][] shortMoves = { {0, 0, 1}, {1, 1, 2} };
        int[] shortResults = new int[shortMoves.length];
        TicTacToe shortGame = new TicTacToe(2);
        for (int i = 0; i < shortMoves.length; i++) {
            shortResults[i] = shortGame.move(shortMoves[i][0], shortMoves[i][1], shortMoves[i][2]);
        }
        int[] shortExpected = { 0, 0 };
        System.out.printf("moves=%s  ->  %s  expected=%s%n",
            Arrays.deepToString(shortMoves), Arrays.toString(shortResults), Arrays.toString(shortExpected));
    }

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
   * Intuition: only the latest move's row, column, and possible diagonals can create
   * a new win. Encode player 1 as +1 and player 2 as -1; a line reaches n or -n only
   * when all moves in that line belong to the same player. Updating those counters is
   * enough to detect a winner in constant time.
   *
   * Algorithm:
   *   1. Convert player to delta +1 or -1.
   *   2. Add delta to the moved row and column counters.
   *   3. Add delta to diagonal counters when the move lies on them.
   *   4. Return player if any affected counter reaches +/-n; otherwise return 0.
   *
   * Time:  O(1) - each move updates a fixed number of counters.
   * Space: O(n) - row and column counter arrays store n values each.
   *
   * @param row zero-based row of the move
   * @param col zero-based column of the move
   * @param player player number, either 1 or 2
   * @return 0 if nobody wins yet, otherwise the winning player
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


}

package backtrack;

import java.util.HashSet;
import java.util.Set;


/**
 * Problem: N-Queens II
 * LeetCode: https://leetcode.com/problems/n-queens-ii/
 *
 * Statement:
 * Count the total number of distinct ways to place N queens on an N × N chessboard
 * such that no two queens attack each other (no same row, column, or diagonal).
 *
 * Example:
 * Input: n = 4
 * Output: 2
 * Explanation: There are two valid ways to place queens on a 4x4 board.
 *
 * Follow-up Questions:
 * 1. Can you return all configurations?
 *    - Yes, store column placements and convert them to board strings at base case.
 *    - Related Problem: https://leetcode.com/problems/n-queens/
 * 2. Can this be optimized further?
 *    - Yes, by using bit masking instead of HashSet for better performance and less memory. Solved in method 2.
 * 3. Can you solve iteratively?
 *    - Possible, but recursion is cleaner for this problem.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class NQueen2 {

  private int boardSize;

  public int totalNQueens(int boardSize) {
    this.boardSize = boardSize;
    return backtrack(
        0,
        new HashSet<>(), // occupiedMainDiagonals
        new HashSet<>(), // occupiedAntiDiagonals
        new HashSet<>()  // occupiedColumns
    );
  }

  /**
   * Backtracking helper to count solutions.
   *
   * Steps:
   * 1. If all rows are processed, count this as one valid solution.
   * 2. For each column in the current row:
   *    - Skip if the column or diagonal is already under attack.
   *    - Place the queen and mark attacked positions.
   *    - Recurse for the next row.
   *    - Remove the queen (backtrack) to explore other possibilities.
   *
   * Algorithm: Backtracking with pruning.
   * Time Complexity: O(N!)
   * Space Complexity: O(N)
   *
   * @param currentRow          Current row index.
   * @param occupiedMainDiagonals  Set of main diagonals under attack.
   * @param occupiedAntiDiagonals  Set of anti-diagonals under attack.
   * @param occupiedColumns        Set of columns under attack.
   * @return Number of valid arrangements from this point.
   */
  private int backtrack(int currentRow, Set<Integer> occupiedMainDiagonals, Set<Integer> occupiedAntiDiagonals,
      Set<Integer> occupiedColumns) {

    // Base case: all queens placed
    if (currentRow == boardSize) {
      return 1;
    }

    int totalSolutions = 0;

    for (int col = 0; col < boardSize; col++) {
      int mainDiagonal = currentRow - col;
      int antiDiagonal = currentRow + col;

      // Skip if under attack
      if (occupiedColumns.contains(col)
          || occupiedMainDiagonals.contains(mainDiagonal)
          || occupiedAntiDiagonals.contains(antiDiagonal)) {
        continue;
      }

      // Place queen
      occupiedColumns.add(col);
      occupiedMainDiagonals.add(mainDiagonal);
      occupiedAntiDiagonals.add(antiDiagonal);

      // Explore next row
      totalSolutions += backtrack(currentRow + 1,
          occupiedMainDiagonals,
          occupiedAntiDiagonals,
          occupiedColumns
      );

      // Backtrack
      occupiedColumns.remove(col);
      occupiedMainDiagonals.remove(mainDiagonal);
      occupiedAntiDiagonals.remove(antiDiagonal);
    }

    return totalSolutions;
  }

  /**
   * Entry method to count total N-Queens solutions.
   *
   * Optimized Approach:
   * - Use bitwise operations to track column and diagonal attacks.
   * - Bit masks for:
   *   1. occupiedColumns → Tracks columns already under attack.
   *   2. occupiedMainDiagonals → Tracks main diagonals (row - col constant).
   *   3. occupiedAntiDiagonals → Tracks anti-diagonals (row + col constant).
   * - Each mask is updated recursively, giving O(1) checking and updating.
   *
   * Time Complexity: O(N!) — At most N possibilities for first queen, decreasing thereafter.
   * Space Complexity: O(N) — Recursion depth and bit mask storage.
   *
   * @param boardSize Size of the chessboard (N).
   * @return Total count of valid arrangements.
   */
  public int countNQueensUsingBitWiseOperation(int boardSize) {
    return countNQueensUsingBitWiseOperationRec(0, boardSize, 0, 0, 0);
  }

  /**
   * Recursive helper to count solutions.
   *
   * @param currentRow             Current row index being processed.
   * @param boardSize              Size of the chessboard (N).
   * @param occupiedColumns        Bitmask for occupied columns.
   * @param occupiedMainDiagonals  Bitmask for occupied main diagonals.
   * @param occupiedAntiDiagonals  Bitmask for occupied anti-diagonals.
   * @return Number of valid solutions from this state.
   */
  private int countNQueensUsingBitWiseOperationRec(int currentRow, int boardSize,
      int occupiedColumns, int occupiedMainDiagonals, int occupiedAntiDiagonals) {

    // Base case: All queens placed successfully
    if (currentRow == boardSize) {
      return 1;
    }

    // Determine free positions for the current row
    int availablePositions = ((1 << boardSize) - 1) & ~(occupiedColumns | occupiedMainDiagonals | occupiedAntiDiagonals);

    int totalSolutions = 0;

    // Try placing queen in each free position
    while (availablePositions != 0) {
      // Pick the rightmost available position
      int position = availablePositions & -availablePositions;

      // Mark this position as used
      availablePositions ^= position;

      // Recurse to the next row with updated attack masks
      totalSolutions += countNQueensUsingBitWiseOperationRec(
          currentRow + 1,
          boardSize,
          occupiedColumns | position,
          (occupiedMainDiagonals | position) << 1,
          (occupiedAntiDiagonals | position) >> 1
      );
    }

    return totalSolutions;
  }
}

package dynamicprogramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a matrix denoting a chessboard with a few queens, compute
 * the number of queens that can attack each cell (i, j).
 * Assume there is no queen at that cell while calculating the count.
 *
 * Input
 *  "010",
 *  "100",
 *  "001"
 *  Output:
 *  [
 *    [3, 1, 2],
 *    [1, 3, 3],
 *    [2, 3, 0]
 *  ]
 *  Explanation:
 *  - Cell (0, 0) can be attacked by 3 queens (1 at (1, 0), 1 at (0, 1), and 1 at (2, 2)).
 *  Similarly, for other cells.
 */
public class QueenAttack {

  // 8 possible movement directions for a Queen (Row, Column)
  private static final int[] DX = {0, -1, -1, -1, 0, 1, 1, 1};
  private static final int[] DY = {1, 1, 0, -1, -1, -1, 0, 1};

  public static void main(String[] args) {
    List<String> board = Arrays.asList(
        "010",
        "100",
        "001"
    );
    List<List<Integer>> result = new QueenAttack().queenAttack(board);
    System.out.println(result);
  }

  /**
   * Approach:
   * - Use **direction-based traversal** (8 directions) to count attacking queens.
   * - **Avoid recursion** (uses iterative traversal).
   * - **Optimized data structures** (`int[][]` instead of nested lists).
   *
   * Time Complexity: O(N × M × 8) ≈ O(N × M)
   * Space Complexity: O(N × M)
   */
  public List<List<Integer>> queenAttack(List<String> board) {
    int rows = board.size();
    int cols = board.get(0).length();
    int[][] attackCount = new int[rows][cols];

    // Iterate through every cell to find queens
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (board.get(i).charAt(j) == '1') {
          // Mark attack positions in all 8 directions
          markAttacks(board, attackCount, i, j, rows, cols);
        }
      }
    }

    // Convert result into List<List<Integer>>
    return convertToList(attackCount);
  }

  private void markAttacks(List<String> board, int[][] attackCount, int currRow, int currCol, int numRows, int numCols) {
    for (int dir = 0; dir < 8; dir++) {
      int i = currRow + DX[dir];
      int j = currCol + DY[dir];

      while (i >= 0 && i < numRows && j >= 0 && j < numCols) {
        attackCount[i][j]++; // Increase attack count
        if (board.get(i).charAt(j) == '1') {
          break; // Stop if another queen is encountered
        }
        /*
         * This does not always increment diagonally. The code increments the indices `i` and `j` according to the direction specified by `DX[dir]` and `DY[dir]`,
         * Depending on the value of `dir`, the movement could be horizontal, vertical, or diagonal.
         */
        i += DX[dir];
        j += DY[dir];
      }
    }
  }

  private List<List<Integer>> convertToList(int[][] matrix) {
    List<List<Integer>> result = new ArrayList<>();
    for (int[] row : matrix) {
      List<Integer> listRow = new ArrayList<>();
      for (int cell : row) {
        listRow.add(cell);
      }
      result.add(listRow);
    }
    return result;
  }
}

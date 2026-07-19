package dynamicprogramming.MiscellaneousDP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Queen Attack
 *
 * Given a board where '1' marks a queen and '0' marks an empty cell, compute how
 * many queens can attack every cell. A queen attacks horizontally, vertically, and
 * diagonally until another queen blocks the ray.
 *
 * Source: InterviewBit - Queen Attack
 * Pattern:  Grid | Direction scan | Ray blocking
 *
 * Example:
 *   Input:  board = ["010","100","001"]
 *   Output: [[3,1,2],[1,3,3],[2,3,0]]
 *   Why:    cell (0,0) is seen by queens from the right, below, and down-right;
 *           the same eight-direction scan gives the counts for every cell.
 *
 * Follow-ups:
 *   1. Can this be faster than scanning from every queen?
 *      Sweep each of the eight directions across the board and accumulate visibility counts.
 *   2. What if queens do not block each other?
 *      Count queens sharing each row, column, and diagonal with precomputed frequency maps.
 *   3. Return attacking queen coordinates for each cell?
 *      Store lists per cell while scanning rays, though output size can be large.
 *
 * Related: Queens That Can Attack the King (1222), N-Queens (51).
 */
public class QueenAttack {

  // 8 possible movement directions for a Queen (Row, Column)
  private static final int[] dx = {0, -1, -1, -1, 0, 1, 1, 1};
  private static final int[] dy = {1, 1, 0, -1, -1, -1, 0, 1};

    public static void main(String[] args) {
        QueenAttack solver = new QueenAttack();
        List<List<String>> inputs = Arrays.asList(
            Arrays.asList("0"),
            Arrays.asList("010", "100", "001")
        );
        String[] expected = {"[[0]]", "[[3, 1, 2], [1, 3, 3], [2, 3, 0]]"};

        for (int i = 0; i < inputs.size(); i++) {
            List<List<Integer>> got = solver.queenAttack(inputs.get(i));
            System.out.printf("board=%s -> %s  expected=%s%n", inputs.get(i), got, expected[i]);
        }
    }


  /**
     * Intuition: each queen sends one attack ray in each of the eight directions.
     * Every empty cell on that ray gains one attacking queen until another queen is
     * reached, because that next queen blocks anything beyond it in the same
     * direction. By adding contributions from each queen into a matrix, each cell's
     * final count is simply the number of rays that reached it.
     *
     * Algorithm:
     *   1. Allocate attackCount with the same dimensions as board.
     *   2. For every cell containing a queen, call markAttacks from that position.
     *   3. markAttacks walks all eight directions, incrementing cells until the ray leaves the board or hits a queen.
     *   4. Convert the count matrix into List<List<Integer>>.
     *
     * Time:  O(q*(rows+cols)) - each queen scans up to a board-length ray in eight directions.
     * Space: O(rows*cols) - the count matrix stores one answer per cell.
     *
     * @param board rows of '0' and '1' characters
     * @return attack count for every cell
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
      int i = currRow + dx[dir];
      int j = currCol + dy[dir];

      while (i >= 0 && i < numRows && j >= 0 && j < numCols) {
        attackCount[i][j]++; // Increase attack count
        if (board.get(i).charAt(j) == '1') {
          break; // Stop if another queen is encountered
        }
        /*
         * This does not always increment diagonally. The code increments the indices `i` and `j` according to the direction specified by `DX[dir]` and `DY[dir]`,
         * Depending on the value of `dir`, the movement could be horizontal, vertical, or diagonal.
         */
        i += dx[dir];
        j += dy[dir];
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

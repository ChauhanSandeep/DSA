package dynamicprogramming.MiscellaneousDP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Queens That Can Attack the King
 *
 * Given queen positions and one king position on an 8 by 8 chessboard, return the
 * queens that can attack the king. A farther queen in the same direction is blocked
 * by the first queen found in that direction.
 *
 * Leetcode: https://leetcode.com/problems/queens-that-can-attack-the-king/
 * Rating:   1392 (zerotrac Elo)
 * Pattern:  Grid | Direction scan | Constant-size board
 *
 * Example:
 *   Input:  queens = [[0,1],[1,0],[4,0],[0,4],[3,3],[2,4]], king = [0,0]
 *   Output: [[0,1],[1,0],[3,3]]
 *   Why:    those are the first queens seen upward/right, downward, and diagonal
 *           from the king; queens behind them are blocked.
 *
 * Follow-ups:
 *   1. What if the board is n by n?
 *      Keep the same eight direction scan and replace the fixed board bound with n.
 *   2. What if pieces move like rooks or bishops only?
 *      Restrict the direction list to orthogonal or diagonal directions.
 *   3. What if many king queries use the same queen set?
 *      Store occupied squares once and scan from each queried king in O(8n).
 *
 * Related: Queen Attack, Available Captures for Rook (999).
 */
public class QueensAttackKing {

  // 8 possible directions (rowOffset, colOffset)
  private static final int[][] DIRECTIONS = {
      {-1, 0},  // up
      {-1, 1},  // up-right
      {0, 1},   // right
      {1, 1},   // down-right
      {1, 0},   // down
      {1, -1},  // down-left
      {0, -1},  // left
      {-1, -1}  // up-left
  };

    public static void main(String[] args) {
        QueensAttackKing solver = new QueensAttackKing();
        int[][][] queensCases = {
            {{0, 1}, {1, 0}, {4, 0}, {0, 4}, {3, 3}, {2, 4}},
            {{5, 6}}
        };
        int[][] kings = {{0, 0}, {5, 5}};
        String[] expected = {"[[0, 1], [3, 3], [1, 0]]", "[[5, 6]]"};

        for (int i = 0; i < queensCases.length; i++) {
            List<List<Integer>> got = solver.queensAttacktheKing(queensCases[i], kings[i]);
            System.out.printf("queens=%s king=%s -> %s  expected=%s%n",
                Arrays.deepToString(queensCases[i]), Arrays.toString(kings[i]), got, expected[i]);
        }
    }


  /**
     * Intuition: from the king's point of view, only the nearest queen in each of
     * the eight straight-line directions matters. A boolean board turns queen
     * lookup into O(1), then each ray walks square by square until it leaves the
     * board or hits a queen. Because the chessboard size is fixed, this is small,
     * but the same idea scales to a larger grid.
     *
     * Algorithm:
     *   1. Mark all queen coordinates on an 8 by 8 boolean board.
     *   2. From the king, walk one ray for each of the eight directions.
     *   3. Add the first queen found on a ray and stop that ray.
     *   4. Return all such nearest attacking queens.
     *
     * Time:  O(1) - the board has only 64 squares, so every direction scan is bounded.
     * Space: O(1) - the boolean board is always 8 by 8.
     *
     * @param queens queen coordinates as [row, col]
     * @param king king coordinate as [row, col]
     * @return attacking queens, at most one per direction
     */
  public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {
    boolean[][] board = new boolean[8][8];

    // Mark all queen positions
    for (int[] queen : queens) {
      board[queen[0]][queen[1]] = true;
    }

    List<List<Integer>> result = new ArrayList<>();

    // Explore each of the 8 directions
    for (int[] dir : DIRECTIONS) {
      int row = king[0] + dir[0];
      int col = king[1] + dir[1];

      // Move step-by-step in the current direction
      while (row >= 0 && row < 8 && col >= 0 && col < 8) {
        if (board[row][col]) {
          result.add(Arrays.asList(row, col)); // Break if we find a queen
          break;
        }
        row += dir[0];
        col += dir[1];
      }
    }

    return result;
  }
}
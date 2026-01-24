package dynamicprogramming.MiscellaneousDP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Leetcode 1222 - Queens That Can Attack the King
 * https://leetcode.com/problems/queens-that-can-attack-the-king/
 *
 * Given positions of queens and a king on an 8x8 chessboard, return all queen positions
 * that can directly attack the king.
 *
 * A queen can attack in all 8 directions until blocked by another piece or the edge.
 *
 * Example:
 * Input:
 *   queens = [[0,1],[1,0],[4,0],[0,4],[3,3],[2,4]],
 *   king = [0,0]
 * Output:
 *   [[0,1],[1,0],[3,3]]
 *
 * Follow-up:
 * - What if you want only the closest attacking queen in each direction? → This solution does that.
 * LeetCode Contest Rating: 1392
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
    int[][] queens = {
        {0, 1}, {1, 0}, {4, 0},
        {0, 4}, {3, 3}, {2, 4}
    };
    int[] king = {0, 0};

    QueensAttackKing solver = new QueensAttackKing();
    List<List<Integer>> result = solver.queensAttacktheKing(queens, king);
    System.out.println(result);
  }

  /**
   * Finds all queens that can attack the king.
   *
   * Steps:
   * 1. Store all queen positions in a hash set for O(1) lookup.
   * 2. For each of the 8 directions, move outward from the king.
   * 3. Stop at the first queen encountered in each direction and add to result.
   *
   * Time Complexity: O(8 * 8) = O(64) – since board size is constant
   * Space Complexity: O(64) for storing queen positions in hash table
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
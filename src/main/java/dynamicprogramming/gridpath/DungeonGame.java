package dynamicprogramming.gridpath;

import java.util.Arrays;

/**
 * Problem: Dungeon Game
 *
 * A knight starts in the top-left cell and must reach the bottom-right princess
 * cell, moving only right or down. Each dungeon cell changes health, and health
 * must never drop below 1, so return the minimum initial health required.
 *
 * Leetcode: https://leetcode.com/problems/dungeon-game/ (Hard)
 * Rating:   acceptance 41.7% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Grid DP | Reverse minimum-health requirement
 *
 * Example:
 *   Input:  dungeon = [[-2,-3,3],[-5,-10,1],[10,30,-5]]
 *   Output: 7
 *   Why:    starting with 7 keeps health at least 1 along the best path to the princess.
 *
 * Follow-ups:
 *   1. Can space be reduced to O(n)?
 *      Fill one rolling row from right to left.
 *   2. What if movement in four directions is allowed?
 *      Cycles make this a graph shortest-path style problem with state constraints.
 *   3. What if you need the actual route?
 *      Store the chosen next cell while filling the DP table and reconstruct from start.
 *
 * Related: Minimum Path Sum (64), Cherry Pickup (741).
 */
public class DungeonGame {

  public static void main(String[] args) {
    DungeonGame solver = new DungeonGame();
    int[][][] cases = {
        {{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}},
        {{0}}
    };
    int[] expected = { 7, 1 };

    for (int i = 0; i < cases.length; i++) {
      int got = solver.calculateMinimumHealthIterativeApproach(cases[i]);
      System.out.printf("dungeon=%s -> %d  expected=%d%n",
          Arrays.deepToString(cases[i]), got, expected[i]);
    }
  }


  /**
   * Top-down recursive DFS with memoization to avoid recomputation.
   *
   * Steps:
   * - Start from (0,0) and move to bottom-right.
   * - At each step, calculate minimum HP needed to reach the destination.
   * - Cache intermediate results using memo.
   *
   * Time Complexity: O(m * n)
   * Space Complexity: O(m * n) due to recursion stack and memoization table
   */
  public int calculateMinimumHealthRecursiveApproach(int[][] dungeon) {
      if (dungeon == null || dungeon.length == 0 || dungeon[0].length == 0) {
          return 1;
      }

    int rows = dungeon.length;
    int cols = dungeon[0].length;
    Integer[][] memo = new Integer[rows][cols];

    // Required health at the starting cell to survive the path
    int requiredHealth = minHealthRecHelper(dungeon, 0, 0, memo);

    // Knight must start with at least 1 HP
    return Math.max(1, -requiredHealth + 1);
  }

  /**
   * Recursive helper: Returns minimum health needed to enter cell (i, j) and reach princess.
   */
  private int minHealthRecHelper(int[][] dungeon, int row, int col, Integer[][] memo) {
    int rows = dungeon.length;
    int cols = dungeon[0].length;

    // Boundary condition: invalid path
    if (row >= rows || col >= cols) {
      return Integer.MIN_VALUE;
    }

    // Base case: bottom-right cell (princess cell)
    if (row == rows - 1 && col == cols - 1) {
      return Math.min(dungeon[row][col], 0); // health debt (<= 0)
    }

    // Check cached value
    if (memo[row][col] != null) {
      return memo[row][col];
    }

    // Recursive move options: right and down
    int moveDownHealth = minHealthRecHelper(dungeon, row + 1, col, memo);
    int moveRightHealth = minHealthRecHelper(dungeon, row, col + 1, memo);

    // Choose the path which leads to least health deficit
    int bestNextMove = Math.max(moveDownHealth, moveRightHealth);

    // Add current cell effect and clamp to max 0 (i.e., worst deficit)
    int currentDeficit = dungeon[row][col] + bestNextMove;

    // Store min health deficit needed from this point
    memo[row][col] = Math.min(currentDeficit, 0);
    return memo[row][col];
  }

    /**
   * Intuition: minHealth[row][col] means the minimum health needed before
   * entering that cell and still being able to reach the princess alive. At the
   * princess cell, we need enough health to survive that cell and leave at least
   * 1. For any other cell, the next move can be right or down, so we compute the
   * entry health required by each valid next cell and keep the smaller one.
   *
   * Algorithm:
   *   1. Allocate a rows by cols table for minimum entry health.
   *   2. Fill cells from bottom-right toward top-left so right and down states are already known.
   *   3. For each cell, subtract the dungeon value from the chosen next requirement and clamp to at least 1.
   *
   * Time:  O(m * n) - every dungeon cell is processed once.
   * Space: O(m * n) - the DP table stores one health requirement per cell.
   *
   * @param dungeon grid of health gains and losses
   * @return minimum initial health needed at the start
   */
  public int calculateMinimumHealthIterativeApproach(int[][] dungeon) {
      if (dungeon == null || dungeon.length == 0 || dungeon[0].length == 0) {
          return 1;
      }

    int rows = dungeon.length;
    int cols = dungeon[0].length;
    int[][] minHealth = new int[rows][cols];

    for (int row = rows - 1; row >= 0; row--) {
      for (int col = cols - 1; col >= 0; col--) {

        // Bottom-right cell (destination)
        if (row == rows - 1 && col == cols - 1) {
          minHealth[row][col] = Math.max(1, 1 - dungeon[row][col]);
        }
        // Last row (can only move right)
        else if (row == rows - 1) {
          minHealth[row][col] = Math.max(minHealth[row][col + 1] - dungeon[row][col], 1);
        }
        // Last column (can only move down)
        else if (col == cols - 1) {
          minHealth[row][col] = Math.max(minHealth[row + 1][col] - dungeon[row][col], 1);
        }
        // Internal cell: choose min between right and down move
        else {
          int healthIfMoveRight = Math.max(minHealth[row][col + 1] - dungeon[row][col], 1);
          int healthIfMoveDown = Math.max(minHealth[row + 1][col] - dungeon[row][col], 1);
          minHealth[row][col] = Math.min(healthIfMoveRight, healthIfMoveDown);
        }
      }
    }

    return minHealth[0][0];
  }
}
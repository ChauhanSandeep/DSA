package dynamicprogramming;

/**
 * Problem: Dungeon Game
 * LeetCode: https://leetcode.com/problems/dungeon-game/
 *
 * Problem Statement:
 * Given a 2D dungeon grid with each cell representing health points (positive for gain, negative for damage),
 * determine the minimum initial health required for a knight to rescue the princess starting from the top-left
 * corner and reaching the bottom-right. The knight can only move right or down, and health must never drop below 1.
 *
 * Example:
 * Input:
 * [
 *   [-2, -3,  3],
 *   [-5,-10,  1],
 *   [10, 30, -5]
 * ]
 * Output: 7
 * Explanation: The knight needs at least 7 health to reach the princess using path
 * (0,0) [-2] -> (0,1) [-3] -> (0,2) [3] -> (1,2) [1] -> (2,2) [-5].
 *
 * Follow-up Interview Questions:
 * - Can you optimize the space complexity to O(n) using a rolling array? (Yes)
 *   https://leetcode.com/problems/dungeon-game/discuss/52827/O(n)-space-solution
 * - What if knight can move in all four directions? (Cycle detection + memoized DFS)
 */
public class DungeonGame {

  public static void main(String[] args) {
    int[][] dungeon = {{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}};

    DungeonGame solver = new DungeonGame();
    System.out.println("Minimum HP (Recursive): " + solver.calculateMinimumHealthRecursiveApproach(dungeon));
    System.out.println("Minimum HP (Iterative DP): " + solver.calculateMinimumHealthIterativeApproach(dungeon));
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
   * Bottom-up DP approach that fills a table from destination to start.
   *
   * Steps:
   * - Start from bottom-right and move to top-left.
   * - At each cell, calculate minimum HP needed based on right and down cell.
   * - Ensure knight always has at least 1 HP after visiting a cell.
   *
   * Time Complexity: O(m * n)
   * Space Complexity: O(m * n)
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
package array;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ✅ Problem Statement:
 * Given a 2D grid of oranges where:
 * - 0 = Empty cell
 * - 1 = Fresh orange
 * - 2 = Rotten orange
 *
 * Every minute, any fresh orange that is 4-directionally adjacent to any rotten one becomes rotten.
 * Return the **minimum number of minutes** that must elapse until no cell has a fresh orange.
 * If it is impossible, return -1.
 *
 * 🔗 LeetCode: https://leetcode.com/problems/rotting-oranges/
 *
 * 🔄 Example:
 * Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
 * Output: 4
 *
 * 🧠 Follow-up:
 * - What if the input grid is extremely large (e.g., 10^6 x 10^6)?
 *   → You must optimize memory. Use streaming techniques or mark changes in-place.
 */
public class RottenOranges {

  private static final int EMPTY = 0;
  private static final int FRESH = 1;
  private static final int ROTTEN = 2;

  public static void main(String[] args) {
    int[][] grid = {
        {2, 1, 1},
        {1, 1, 0},
        {0, 1, 1}
    };
    int minutes = new RottenOranges().orangesRotting(grid);
    System.out.println("Time elapsed to rot all oranges: " + minutes); // Output: 4
  }

  /**
   * Performs a level-order BFS to simulate minute-wise rotting of adjacent fresh oranges.
   *
   * 🧩 Algorithm Steps:
   * 1. Traverse the grid to enqueue all rotten oranges and count fresh ones.
   * 2. Use BFS to simulate the rotting process minute-by-minute.
   * 3. After each minute, check if any fresh oranges remain.
   *
   * ⏱ Time Complexity: O(R × C), where R = rows and C = cols
   * 🧠 Space Complexity: O(R × C), for the queue in the worst case
   *
   * @param grid The 2D matrix of oranges
   * @return Minimum number of minutes to rot all fresh oranges, or -1 if impossible
   */
  public int orangesRotting(int[][] grid) {
    if (grid == null || grid.length == 0) return -1;

    int rows = grid.length;
    int cols = grid[0].length;
    int freshOranges = 0;

    Queue<int[]> queue = new LinkedList<>();

    // Step 1: Initialize queue with all rotten oranges & count fresh oranges
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (grid[row][col] == ROTTEN) {
          queue.offer(new int[]{row, col});
        } else if (grid[row][col] == FRESH) {
          freshOranges++;
        }
      }
    }

    // Edge case: No fresh oranges to begin with
    if (freshOranges == 0) return 0;

    int minutesElapsed = 0;
    int[][] directions = {
        {-1, 0}, // up
        {0, 1},  // right
        {1, 0},  // down
        {0, -1}  // left
    };

    // Step 2: BFS - level-order traversal
    while (!queue.isEmpty()) {
      int currentLevelSize = queue.size();
      boolean anyFreshRotted = false;

      for (int i = 0; i < currentLevelSize; i++) {
        int[] pos = queue.poll();
        int row = pos[0];
        int col = pos[1];

        // Try all 4 directions
        for (int[] dir : directions) {
          int newRow = row + dir[0];
          int newCol = col + dir[1];

          // If adjacent cell is a fresh orange
          if (isValidCell(newRow, newCol, rows, cols) && grid[newRow][newCol] == FRESH) {
            grid[newRow][newCol] = ROTTEN; // Mark it as rotten
            queue.offer(new int[]{newRow, newCol});
            freshOranges--;
            anyFreshRotted = true;
          }
        }
      }

      // Only increment time if something actually rotted in this round
      if (anyFreshRotted) minutesElapsed++;
    }

    // Step 3: Check if all fresh oranges are rotted
    return freshOranges == 0 ? minutesElapsed : -1;
  }

  /**
   * Helper to check if the coordinates are within grid bounds
   */
  private boolean isValidCell(int row, int col, int maxRow, int maxCol) {
    return row >= 0 && row < maxRow && col >= 0 && col < maxCol;
  }
}
package arrays.bfs;

import java.util.LinkedList;
import java.util.Queue;

import java.util.Arrays;
/**
 * Problem: Rotting Oranges
 *
 * A grid contains empty cells, fresh oranges, and rotten oranges. Every minute,
 * rot spreads from each rotten orange to its four-directional fresh neighbors.
 * Return the minimum minutes needed to rot every fresh orange, or -1 when some
 * fresh orange is cut off from all rot sources.
 *
 * Leetcode: https://leetcode.com/problems/rotting-oranges/
 * Rating:   1433 (zerotrac Elo, Q2, weekly-contest-124)
 * Pattern:  Arrays | BFS | Multi-source level order traversal
 *
 * Example:
 *   Input:  [[2,1,1],[1,1,0],[0,1,1]]
 *   Output: 4
 *   Why:    rot spreads one layer per minute, and the farthest fresh orange is
 *           four steps away from the initial rotten orange.
 *
 * Follow-ups:
 *   1. What if the grid is too large to fit in memory?
 *      Store only orange coordinates and stream frontier layers from external storage.
 *   2. What if each edge has a different rotting time?
 *      Replace BFS with Dijkstra's algorithm over the grid cells.
 *   3. What if new fresh oranges can appear over time?
 *      Process events chronologically and restart or update the frontier when needed.
 *
 * Related: Walls and Gates (286), Shortest Path in Binary Matrix (1091).
 */
public class RottenOranges {

  private static final int EMPTY = 0;
  private static final int FRESH = 1;
  private static final int ROTTEN = 2;
    public static void main(String[] args) {
        RottenOranges solver = new RottenOranges();
        int[][][] inputs = {
            {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}},
            {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}},
            {{0, 2}}
        };
        int[] expected = {4, -1, 0};

        for (int i = 0; i < inputs.length; i++) {
            int[][] grid = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            int got = solver.orangesRotting(grid);
            System.out.printf("grid=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

  /**
     * Intuition: every initially rotten orange is a starting point, so this is not
     * a single-source search. Put all rotten oranges into the queue first; then
     * each BFS layer represents exactly one minute of spreading. When a fresh
     * orange is first reached, that is the earliest possible minute it can rot,
     * because BFS visits cells by distance from the nearest rotten source. If any
     * fresh orange never gets reached, walls or empty cells have isolated it.
     *
     * Time:  O(rows * cols) - each cell is scanned once and enqueued at most once.
     * Space: O(rows * cols) - in the worst case the BFS queue can hold a whole grid layer.
     *
     * @param grid matrix where 0 is empty, 1 is fresh, and 2 is rotten
     * @return minimum minutes to rot all fresh oranges, or -1 if impossible
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
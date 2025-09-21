package graphs;

import java.util.*;


/**
 * LeetCode 827: Making A Large Island
 * Problem Link: https://leetcode.com/problems/making-a-large-island/
 *
 * Problem Statement:
 * Given an n x n binary grid where 1 represents land and 0 represents water,
 * you may flip exactly one water cell (0) to land (1). Find the size of the
 * largest island (group of 1's connected vertically or horizontally) that can be formed.
 * Return the largest possible island size after making exactly one flip.
 *
 * Example:
 * Input: grid = [[1,1,1],[0,1,0],[0,0,1]]
 * Output: 7
 * Explanation:
 * Flip grid[2][0] or grid[2][1] to 1 to connect all 1's into a single large island of size 7.
 *
 * Follow-up Questions:
 * - What if you can flip at most K zeroes? (Use Union-Find, multi-source BFS, intermediate merging)
 * - What if the grid is too large for in-place marking? (Hash-mapping outside grid or Union-Find)
 *
 * Time Complexity: O(N^2) (for DFS labeling + checking all zeros)
 * Space Complexity: O(N^2) (for map and marking)
 */
public class MakeLargeIsland {

  /**
   * Main method for simple test drive.
   */
  public static void main(String[] args) {
    int[][] grid = {{1, 1, 1}, {0, 1, 0}, {0, 0, 1}};
    int largestIsland = new MakeLargeIsland().largestIsland(grid);
    System.out.println("Largest island after flipping one 0 is " + largestIsland);
  }

  /**
   * Returns the size of the largest possible island after flipping one 0 to 1.
   *
   * Steps:
   * 1. Use DFS to mark connected components (islands) with unique ids and record their areas.
   * 2. For every water cell (0), simulate flipping and sum unique neighboring island areas.
   * 3. Return the max possible island size.
   *
   * Algorithm: DFS island labeling + local neighbor set merging
   * Time: O(N^2), Space: O(N^2)
   *
   * @param grid Input binary grid of land and water.
   * @return Largest possible island size.
   */
  public int largestIsland(int[][] grid) {
    int length = grid.length;
    int islandId = 2; // Start labeling with 2 to distinguish from 0 (water) and 1 (unmarked land)
    int maxIslandArea = 0;
    Map<Integer, Integer> islandIdToAreaMap = new HashMap<>();

    // Step 1: Label each island with a unique id and record its area
    for (int row = 0; row < length; row++) {
      for (int col = 0; col < length; col++) {
        if (grid[row][col] == 1) {
          int area = markIslandDFS(grid, row, col, islandId);
          islandIdToAreaMap.put(islandId, area);
          maxIslandArea = Math.max(maxIslandArea, area); // In case grid is all 1s
          islandId++;
        }
      }
    }

    // Step 2: For each zero, compute potential island area if this 0 is flipped to 1
    for (int row = 0; row < length; row++) {
      for (int col = 0; col < length; col++) {
        if (grid[row][col] == 0) {
          int mergedArea = computeFlippedZeroIslandArea(grid, row, col, islandIdToAreaMap);
          maxIslandArea = Math.max(maxIslandArea, mergedArea);
        }
      }
    }
    return maxIslandArea;
  }

  /**
   * Helper to mark all cells of an island via DFS.
   * Labels the island with the specified id and calculates its area.
   *
   * Steps:
   * - Use explicit stack (avoids recursion stack overflow).
   * - For each land cell, mark it, increase area, add its unvisited land neighbors.
   *
   * Time: O(Area of one island)
   * Space: O(Area of one island)
   *
   * @param grid      Input grid.
   * @param row       Row of starting cell.
   * @param col       Col of starting cell.
   * @param islandId  Label to mark all cells of this island.
   * @return Area of this newly marked island.
   */
  private int markIslandDFS(int[][] grid, int row, int col, int islandId) {
    int n = grid.length;
    int area = 0;
    Deque<int[]> stack = new ArrayDeque<>();
    stack.push(new int[]{row, col});
    grid[row][col] = islandId;
    final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    while (!stack.isEmpty()) {
      int[] cell = stack.pop();
      int r = cell[0], c = cell[1];
      area++;
      for (int[] d : DIRS) {
        int nr = r + d[0], nc = c + d[1];
        if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 1) {
          grid[nr][nc] = islandId;
          stack.push(new int[]{nr, nc});
        }
      }
    }
    return area;
  }

  /**
   * When flipping water cell at (row, col), determine the total area
   * by summing up the unique neighboring islands' sizes and itself.
   *
   * Steps:
   * - For all 4 directions, collect unique island ids.
   * - Sum their areas and add 1 (for this flipped cell).
   * - Avoid double-counting islands.
   *
   * @param grid             Input grid labeled with island ids.
   * @param row              Row of zero being flipped.
   * @param col              Col of zero being flipped.
   * @param islandAreaById   Map from island id to its computed area.
   * @return Potential new area if this zero is flipped.
   */
  private int computeFlippedZeroIslandArea(int[][] grid, int row, int col, Map<Integer, Integer> islandAreaById) {
    int length = grid.length;
    Set<Integer> neighborIslandIds = new HashSet<>();
    final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    // Check if this cell has any neighboring islands
    for (int[] dir : DIRS) {
      int nextRow = row + dir[0];
      int nextCol = col + dir[1];
      if (nextRow >= 0 && nextRow < length && nextCol >= 0 && nextCol < length && grid[nextRow][nextCol] > 1) {
        neighborIslandIds.add(grid[nextRow][nextCol]);
      }
    }
    int totalArea = 1; // Include the flipped cell itself
    for (int id : neighborIslandIds) {
        // Sum the areas of unique neighboring islands joined by this flip
      totalArea += islandAreaById.getOrDefault(id, 0);
    }
    return totalArea;
  }

  /**
   * Optimized approach using Union-Find (Disjoint Set Union).
   * Use when multiple flips are allowed or union/query times are to be minimized.
   * Not implemented here for brevity, but see:
   * https://leetcode.com/problems/making-a-large-island/solutions/1270172/
   *
   * Follow-up Reference: https://leetcode.com/problems/making-a-large-island/solutions/127159/
   */
}

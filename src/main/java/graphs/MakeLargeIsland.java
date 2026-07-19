package graphs;

import java.util.*;


/**
 * Problem: Making A Large Island
 *
 * Given an n by n binary grid, you may flip one water cell to land. Return the
 * largest island size possible after that flip, where islands connect only in
 * four directions.
 *
 * Leetcode: https://leetcode.com/problems/making-a-large-island/ (Hard)
 * Rating:   1934 (zerotrac Elo)
 * Pattern:  Graph | Island labeling | Local component merge
 *
 * Example:
 *   Input:  grid = [[1,0],[0,1]]
 *   Output: 3
 *   Why:    flipping either zero connects the two diagonal islands through the
 *           flipped cell, making one island of size three.
 *
 * Follow-ups:
 *   1. Flip up to k zeros?
 *      Use a more global search or DSU plus windowing; one-cell local merging is not enough.
 *   2. Avoid modifying the grid?
 *      Keep a separate component-id matrix instead of writing ids into grid cells.
 *   3. Process many flip queries?
 *      Pre-label islands once, then answer each query by summing unique neighboring ids.
 *
 * Related: Max Area of Island (695), Number of Islands (200).
 */
public class MakeLargeIsland {



    public static void main(String[] args) {
        MakeLargeIsland solver = new MakeLargeIsland();
        int[][][] inputs = {{{1, 0}, {0, 1}}, {{1, 1}, {1, 1}}};
        int[] expected = {3, 4};

        for (int i = 0; i < inputs.length; i++) {
            int[][] grid = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            int output = solver.largestIsland(grid);
            System.out.printf("grid=%s  ->  %d  expected=%d%n", Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: flipping one zero can connect the distinct islands touching that
     * cell. First label every existing island with a unique id and remember its
     * area. Then each zero only needs to add the areas of neighboring ids once.
     *
     * Algorithm:
     *   1. DFS every unlabelled land island and store its area by island id.
     *   2. For each zero cell, inspect four neighbors and collect distinct island ids.
     *   3. Add one for the flipped cell plus each neighboring island area.
     *   4. Return the largest possible area, including the all-land case.
     *
     * Time:  O(n^2) - each cell is labeled once and inspected again as a candidate.
     * Space: O(n^2) - island labels/area storage and recursion stack in the worst case.
     *
     * @param grid square binary grid
     * @return largest island area after flipping at most one zero
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

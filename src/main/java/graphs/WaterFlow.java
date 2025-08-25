package graph;

import java.util.*;


/**
 * Leetcode: https://leetcode.com/problems/pacific-atlantic-water-flow/
 *
 * --- Problem Statement ---
 * Given an m x n matrix of non-negative integers representing the height of each cell in a island,
 * the "Pacific ocean" touches the left and top edges, while the "Atlantic ocean" touches the right and bottom edges.
 * Water can only flow from a cell to another one directly adjacent (up/down/left/right) with an equal or lower height.
 * Return the list of grid coordinates where water can flow to **both** the Pacific and Atlantic ocean.
 *
 * --- Example ---
 * Input:
 * heights = [
 *   [1,2,2,3,5],
 *   [3,2,3,4,4],
 *   [2,4,5,3,1],
 *   [6,7,1,4,5],
 *   [5,1,1,2,4]
 * ]
 * Output: Coordinates count = 7 (full list of coordinates not returned here)
 *
 * --- Explanation ---
 * Water can flow from any higher or same-height cell to a lower or same-height neighbor.
 * We reverse the problem: instead of checking whether each cell can reach both oceans, we perform BFS/DFS
 * from the Pacific and Atlantic boundaries and mark the reachable cells. The intersection gives the result.
 *
 * --- Follow-Up Questions ---
 * Q: Can you return the actual list of coordinates instead of just count?
 * A: Yes, return `List<List<Integer>>` instead of count. See: https://leetcode.com/problems/pacific-atlantic-water-flow/
 *
 * Q: Can we use DFS instead of BFS?
 * A: Yes, DFS is also valid and easier to write recursively.
 */
public class WaterFlow {

  private int numRows, numCols;
  private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

  public static void main(String[] args) {
    int[][] heights = {
        {1, 2, 2, 3, 5},
        {3, 2, 3, 4, 4},
        {2, 4, 5, 3, 1},
        {6, 7, 1, 4, 5},
        {5, 1, 1, 2, 4}};
    System.out.println(new WaterFlow().countPacificAtlanticReachableCells(heights)); // Output: 7
  }

  /**
   * Returns the number of grid cells where water can flow to both Pacific and Atlantic oceans.
   *
   * --- Steps ---
   * 1. Initialize two boolean matrices: one for Pacific-reachable and one for Atlantic-reachable cells.
   * 2. For each matrix, start BFS from respective borders (top-left and bottom-right edges).
   * 3. After marking reachable cells, count the intersection (cells reachable from both oceans).
   *
   * --- Algorithm ---
   * BFS from both oceans.
   *
   * Time Complexity: O(m * n) — each cell is visited at most twice.
   * Space Complexity: O(m * n) — boolean matrices and BFS queues.
   */
  public int countPacificAtlanticReachableCells(int[][] heights) {
    if (heights == null || heights.length == 0) {
      return 0;
    }

    numRows = heights.length;
    numCols = heights[0].length;

    boolean[][] pacificReachable = new boolean[numRows][numCols];
    boolean[][] atlanticReachable = new boolean[numRows][numCols];

    Queue<int[]> pacificQueue = new LinkedList<>();
    Queue<int[]> atlanticQueue = new LinkedList<>();

    // Initialize Pacific (top row + left column) and Atlantic (bottom row + right column) BFS queues
    // Top and left blocks can always reach Pacific, bottom and right blocks can always reach Atlantic
    for (int row = 0; row < numRows; row++) {
      pacificQueue.add(new int[]{row, 0});
      atlanticQueue.add(new int[]{row, numCols - 1});
      pacificReachable[row][0] = true;
      atlanticReachable[row][numCols - 1] = true;
    }
    for (int col = 0; col < numCols; col++) {
      pacificQueue.add(new int[]{0, col});
      atlanticQueue.add(new int[]{numRows - 1, col});
      pacificReachable[0][col] = true;
      atlanticReachable[numRows - 1][col] = true;
    }

    // Run BFS for both Pacific and Atlantic borders
    performBFS(heights, pacificQueue, pacificReachable);
    performBFS(heights, atlanticQueue, atlanticReachable);

    // Count intersection: cells that can flow to both oceans
    int reachableCellCount = 0;
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        if (pacificReachable[row][col] && atlanticReachable[row][col]) {
          reachableCellCount++;
        }
      }
    }

    return reachableCellCount;
  }

  /**
   * Performs BFS from the provided border cells and marks all reachable cells in the given matrix.
   *
   * @param heights The original height matrix.
   * @param bfsQueue Queue initialized with border cells.
   * @param visited Boolean matrix to mark reachable cells.
   */
  private void performBFS(int[][] heights, Queue<int[]> bfsQueue, boolean[][] visited) {
    while (!bfsQueue.isEmpty()) {
      // SELECT : Poll the next cell to process
      int[] current = bfsQueue.poll();
      int row = current[0], col = current[1];

      // MARK(*) : Mark the current cell as visited
      if(visited[row][col]) {
        continue; // Skip if already visited
      }
      visited[row][col] = true;
      // WORK(*) : Process the current cell. Processing here is just marking it as visited.

      for (int[] direction : DIRECTIONS) {
        int nextRow = row + direction[0];
        int nextCol = col + direction[1];

        // Skip if out of bounds or already visited
        if (nextRow < 0 || nextCol < 0 || nextRow >= numRows || nextCol >= numCols) {
          continue;
        }
        // ADD(*) : Add the non visited neighbor to the queue
        if (!visited[nextRow][nextCol]) {
          // Only allow water to flow to neighbors with height >= current cell (reverse of natural flow)
          if (heights[nextRow][nextCol] >= heights[row][col]) {
            visited[nextRow][nextCol] = true;
            bfsQueue.add(new int[]{nextRow, nextCol});
          }
        }
      }
    }
  }
}
package graphs.bfs;

import java.util.LinkedList;
import java.util.Queue;


/**
 * LeetCode 1091: Shortest Path in Binary Matrix
 * https://leetcode.com/problems/shortest-path-in-binary-matrix/
 *
 * Problem Statement:
 * - Given an n x n binary grid:
 *   - 0 → empty cell
 *   - 1 → obstacle
 * - Find the shortest path from top-left (0,0) to bottom-right (n-1,n-1).
 * - Movement is allowed in 8 directions (horizontal, vertical, diagonal).
 * - Return the length of the shortest path, or -1 if no path exists.
 *
 * Example:
 * Input: grid = [[0,0,0],
 *                [1,0,0],
 *                [1,1,0]]
 * Output: 4
 *
 * Approach:
 * - Perform BFS (Breadth-First Search) starting from (0,0).
 * - Track steps by marking grid[x][y] with the distance from the start.
 * - Stop early once bottom-right cell is reached.
 *
 * Time Complexity: O(N^2) – BFS visits each cell once at most.
 * Space Complexity: O(N^2) – Queue may hold up to N^2 elements in worst case.
 *
 * Follow-up Questions:
 * 1. Can we optimize space by not modifying the grid?
 *    - Yes, maintain a visited[][] array or use a HashSet.
 * 2. How to handle weighted grids?
 *    - Use Dijkstra’s algorithm instead of BFS.
 * 3. Can this be solved bidirectionally?
 *    - Yes, bidirectional BFS reduces search space significantly.
 * LeetCode Contest Rating: 1658
 **/
public class ShortestPath {

  // Directions: up, down, left, right, and 4 diagonals
  private static final int[][] DIRECTIONS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

  public static void main(String[] args) {
    int[][] grid = {{0, 0, 0}, {1, 0, 0}, {1, 1, 0}};
    ShortestPath solver = new ShortestPath();
    int shortestPath = solver.shortestPathBinaryMatrix(grid);
    System.out.println("Shortest Path: " + shortestPath);
  }

  /**
   * Finds the shortest path in a binary matrix using BFS.
   *
   * Steps:
   * 1. Check base cases (blocked start/end).
   * 2. Initialize BFS queue with starting position.
   * 3. Expand neighbors in 8 directions.
   * 4. Track distance using grid itself.
   * 5. Return distance when bottom-right is reached.
   *
   * Time: O(N^2), Space: O(N^2)
   *
   * @param grid Binary matrix (0 = empty, 1 = obstacle).
   * @return Length of shortest path or -1 if no path exists.
   */
  public int shortestPathBinaryMatrix(int[][] grid) {
    int n = grid.length;

    // Edge case: Start or end blocked
      if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
          return -1;
      }

    // Edge case: Single cell grid
      if (n == 1) {
          return 1;
      }

    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{0, 0});
    grid[0][0] = 1; // Mark start cell with step count

    while (!queue.isEmpty()) {
      int[] current = queue.poll();
      int row = current[0], col = current[1];
      int steps = grid[row][col];

      // Explore all 8 directions
      for (int[] dir : DIRECTIONS) {
        int newRow = row + dir[0];
        int newCol = col + dir[1];

        // Check boundaries and unvisited empty cell
        if (newRow >= 0 && newCol >= 0 && newRow < n && newCol < n && grid[newRow][newCol] == 0) {
          grid[newRow][newCol] = steps + 1; // Mark with step count
          queue.offer(new int[]{newRow, newCol});

          // If reached destination
          if (newRow == n - 1 && newCol == n - 1) {
            return grid[newRow][newCol];
          }
        }
      }
    }
    return -1; // Path not found
  }

  /**
   * Optimized alternative: Bidirectional BFS
   *
   * Idea:
   * - Start BFS from both source and destination simultaneously.
   * - Expand from the side with fewer nodes each round.
   * - Stop when searches meet.
   *
   * Complexity:
   * - Time: O(N^2), but faster in practice (reduced search space).
   * - Space: O(N^2).
   */
  public int shortestPathBinaryMatrixBidirectional(int[][] grid) {
    int length = grid.length;
      if (grid[0][0] == 1 || grid[length - 1][length - 1] == 1) {
          return -1;
      }
      if (length == 1) {
          return 1;
      }

    boolean[][] visitedFromStart = new boolean[length][length];
    boolean[][] visitedFromEnd = new boolean[length][length];
    Queue<int[]> queueFromStart = new LinkedList<>();
    Queue<int[]> queueFromEnd = new LinkedList<>();

    queueFromStart.offer(new int[]{0, 0});
    queueFromEnd.offer(new int[]{length - 1, length - 1});
    visitedFromStart[0][0] = true;
    visitedFromEnd[length - 1][length - 1] = true;

    int steps = 1;

    while (!queueFromStart.isEmpty() && !queueFromEnd.isEmpty()) {
      steps++;

      if (queueFromStart.size() > queueFromEnd.size()) { // Always expand from the side with fewer nodes
        Queue<int[]> temp = queueFromStart;
        queueFromStart = queueFromEnd;
        queueFromEnd = temp;

        boolean[][] tempVisited = visitedFromStart;
        visitedFromStart = visitedFromEnd;
        visitedFromEnd = tempVisited;
      }

      int size = queueFromStart.size();
      for (int i = 0; i < size; i++) {
        int[] current = queueFromStart.poll();
        int row = current[0], col = current[1];

        for (int[] dir : DIRECTIONS) {
          int newRow = row + dir[0], newCol = col + dir[1];

          if (newRow >= 0 && newCol >= 0 && newRow < length && newCol < length && !visitedFromStart[newRow][newCol]
              && grid[newRow][newCol] == 0) {

            if (visitedFromEnd[newRow][newCol]) {
              return steps;
            }

            visitedFromStart[newRow][newCol] = true;
            queueFromStart.offer(new int[]{newRow, newCol});
          }
        }
      }
    }
    return -1;
  }
}

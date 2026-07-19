package graphs.bfs;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;


  /**
   * Intuition: with every move costing one step, BFS is the natural shortest-path
   * tool. The original code writes the distance directly into grid cells, so the
   * first time an open cell is reached is also its shortest distance from the start.
   *
   * Algorithm:
   *   1. Reject blocked endpoints and handle the single-cell grid.
   *   2. Queue the start cell and mark it with distance 1 in the grid.
   *   3. Pop cells breadth-first and inspect all 8 directions.
   *   4. Mark each open neighbor with steps + 1, queue it, and return when the target is reached.
   *
   * Time:  O(n^2) - each cell is queued at most once.
   * Space: O(n^2) - the queue can hold a full BFS frontier in the worst case.
   *
   * @param grid binary matrix where 0 is open and 1 is blocked; mutated to store distances
   * @return length of the shortest clear path, or -1 if none exists
   */
public class ShortestPath {

  // Directions: up, down, left, right, and 4 diagonals
  private static final int[][] DIRECTIONS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

  public static void main(String[] args) {
    ShortestPath solver = new ShortestPath();
    int[][] grid1 = {{0, 0, 0}, {1, 0, 0}, {1, 1, 0}};
    int[][] grid2 = {{1, 0}, {0, 0}};
    int[][] run1 = Arrays.stream(grid1).map(int[]::clone).toArray(int[][]::new);
    int[][] run2 = Arrays.stream(grid2).map(int[]::clone).toArray(int[][]::new);

    System.out.printf("grid=%s -> %d  expected=3%n",
        Arrays.deepToString(grid1), solver.shortestPathBinaryMatrix(run1));
    System.out.printf("grid=%s -> %d  expected=-1%n",
        Arrays.deepToString(grid2), solver.shortestPathBinaryMatrix(run2));
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

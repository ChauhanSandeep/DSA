package graphs.mst;

import java.util.*;

/**
 * Problem: Min Cost to Connect All Points
 *
 * Given points on a 2D plane, connect every point with edges whose costs are
 * Manhattan distances. Return the minimum total cost required so all points are
 * connected in one component.
 *
 * Leetcode: https://leetcode.com/problems/min-cost-to-connect-all-points/ (Medium)
 * Rating:   1858 (zerotrac Elo)
 * Pattern:  Graph | Minimum spanning tree | Prim on complete graph
 *
 * Example:
 *   Input:  points = [[0,0],[2,2],[3,10],[5,2],[7,0]]
 *   Output: 20
 *   Why:    the MST chooses the cheapest set of Manhattan edges that connects every point once.
 *
 * Follow-ups:
 *   1. Improve the heap-based Prim solution on a complete graph?
 *      Use the O(n^2) optimized Prim scan to avoid pushing O(n^2) heap entries.
 *   2. Need the actual connections?
 *      Store the parent point whenever minDist improves.
 *   3. What if points arrive online?
 *      Dynamic MST maintenance is needed; recomputing may be simplest for moderate n.
 *
 * Related: Connecting Cities With Minimum Cost (1135), Minimum Spanning Tree.
 */
public class MinCostToConnectPoints {

  public static void main(String[] args) {
    MinCostToConnectPoints solver = new MinCostToConnectPoints();
    int[][] points1 = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
    int[][] points2 = {{3, 12}};

    System.out.printf("points=%s -> prim=%d optimized=%d  expected=20%n",
        Arrays.deepToString(points1), solver.minCostConnectPoints(points1), solver.minCostConnectPointsOptimized(points1));
    System.out.printf("points=%s -> prim=%d optimized=%d  expected=0%n",
        Arrays.deepToString(points2), solver.minCostConnectPoints(points2), solver.minCostConnectPointsOptimized(points2));
  }

    /**
   * Intuition: all points form a complete graph where edge cost is Manhattan
   * distance. Prim's algorithm grows the MST from point 0, always accepting the
   * cheapest edge that connects a not-yet-visited point.
   *
   * Algorithm:
   *   1. Seed the heap with all edges from point 0 and record their best known costs.
   *   2. Pop the cheapest edge; skip it if the target is already visited or stale.
   *   3. Mark the target visited and add the edge cost to totalCost.
   *   4. Relax edges from the new point to every unvisited point.
   *
   * Time:  O(N^2 log N) - dense graph relaxations create heap work.
   * Space: O(N^2) - heap can hold many candidate edges in the worst case.
   *
   * @param points coordinates [x, y]
   * @return minimum total Manhattan cost to connect all points
   */
  public int minCostConnectPoints(int[][] points) {
    int length = points.length;

    // Min-heap to pick the next edge with the smallest weight (cost)
    PriorityQueue<Edge> minHeap = new PriorityQueue<>((x, y) -> Integer.compare(x.cost, y.cost));
    // Track whether a point is already included in the MST
    boolean[] visited = new boolean[length];

    // Best known cost to connect each point to the current MST.
    // This is optimization to avoid unnecessarily adding nodes into the heap multiple times.
    int[] minDist = new int[length];
    Arrays.fill(minDist, Integer.MAX_VALUE);

    // Start MST with point 0
    int edgesUsed = 0;
    int totalCost = 0;
    visited[0] = true;
    minDist[0] = 0;

    // Add all edges from point 0 to the heap
    for (int i = 1; i < length; i++) {
      int cost = manhattanDistance(points[0], points[i]);
      minDist[i] = cost;
      minHeap.offer(new Edge(0, i, cost));
    }

    // Continue adding edges until all points are in the MST
    while (!minHeap.isEmpty()) {
      Edge edge = minHeap.poll();

      if (visited[edge.to] || edge.cost > minDist[edge.to]) {
        continue;
      }

      // Add the edge to the MST
      visited[edge.to] = true;
      totalCost += edge.cost;
      edgesUsed++;

      // Add edges from this point to the queue
      for (int next = 0; next < length; next++) {
        if (!visited[next]) {
          int cost = manhattanDistance(points[edge.to], points[next]);
          if (cost < minDist[next]) {
            minDist[next] = cost;
            minHeap.offer(new Edge(edge.to, next, cost));
          }
        }
      }
    }

    return totalCost;
  }

  /**
   * Solves the same MST problem with an optimized Prim approach for dense graphs.
   *
   * Optimization: Replace the min-heap with a linear scan over {@code minDist}. In a complete
   * graph (like this problem), each node can reach all others, so avoiding heap push/pop removes
   * the extra {@code logN} factor and improves from {@code O(N^2 logN)} to {@code O(N^2)}.
   *
   * Steps:
   * 1. Initialize minDist[i] as the best known cost to connect node i
   * to the current MST.
   * 2. Repeatedly pick the unvisited node with smallest minDist using a linear scan.
   * 3. Add it to MST and add that cost to answer.
   * 4. Relax all unvisited nodes by updating {@code minDist} with Manhattan distance from the newly added node.
   *
   * Time Complexity: O(N^2)
   * Space Complexity: O(N)
   */
  public int minCostConnectPointsOptimized(int[][] points) {
    int length = points.length;
    boolean[] visited = new boolean[length];
    int[] minDist = new int[length];
    Arrays.fill(minDist, Integer.MAX_VALUE);
    minDist[0] = 0;

    int totalCost = 0;

    for (int used = 0; used < length; used++) {
      int current = -1;

      // Pick the unvisited node with the smallest connection cost.
      for (int node = 0; node < length; node++) {
        if (!visited[node] && (current == -1 || minDist[node] < minDist[current])) {
          current = node;
        }
      }

      visited[current] = true;
      totalCost += minDist[current];

      // Relax the best known cost to connect each remaining node.
      for (int next = 0; next < length; next++) {
        if (!visited[next]) {
          int cost = manhattanDistance(points[current], points[next]);
          if (cost < minDist[next]) {
            minDist[next] = cost;
          }
        }
      }
    }

    return totalCost;
  }

  /** Helper to calculate Manhattan distance between two points */
  private int manhattanDistance(int[] p1, int[] p2) {
    return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);
  }

  /** Edge class to store connection and its cost */
  private static class Edge {
    int from;
    int to;
    int cost;

    Edge(int from, int to, int cost) {
      this.from = from;
      this.to = to;
      this.cost = cost;
    }
  }
}

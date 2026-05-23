package graphs.mst;

import java.util.*;

/**
 * You are given an array `points` representing integer coordinates of some points on a 2D plane,
 * where points[i] = [xi, yi]. The cost of connecting two points (xi, yi) and (xj, yj) is the
 * Manhattan distance: |xi - xj| + |yi - yj|.
 *
 * <p>Return the minimum cost to make all points connected. All points must be connected using
 * edges.
 *
 * <p>Example: Input: points = [[0,0],[2,2],[3,10],[5,2],[7,0]] Output: 20
 *
 * <p>LeetCode Link: https://leetcode.com/problems/min-cost-to-connect-all-points/ LeetCode Contest
 * Rating: 1858
 */
public class MinCostToConnectPoints {

  /**
   * Solves the problem using Prim's Algorithm for Minimum Spanning Tree (MST).
   *
   * <p>Intuition: - Treat the points as nodes in a graph. - Edges between them have a cost =
   * Manhattan distance. - Use Prim’s algorithm to build the MST with minimum total cost.
   *
   * <p>Steps: 1. Start with point 0 in MST. 2. Use a min-heap to always add the edge with the least
   * cost to the MST. 3. Track visited points and avoid cycles. 4. Repeat until all points are
   * included.
   *
   * <p>Time Complexity: O(N^2 * logN) where N is number of points Space Complexity: O(N) for
   * visited and minDist, plus heap overhead
   *
   * @param points An array of 2D coordinates
   * @return The minimum total cost to connect all points
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

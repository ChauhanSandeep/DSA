package graphs.mst;

import java.util.*;


/**
 * LeetCode 1135: Connecting Cities With Minimum Cost
 *
 * Given `n` cities labeled from 1 to n and a list of connections [city1, city2, cost],
 * connect all cities with minimum total cost. Each connection connects two cities.
 * Return the minimum cost to connect all cities, or -1 if it's not possible.
 *
 * Example:
 * Input: n = 3, connections = [[1,2,5],[1,3,6],[2,3,1]]
 * Output: 6
 * Explanation : Connect 1-2 with cost 5 and 2-3 with cost 1.
 *
 * Link: https://leetcode.com/problems/connecting-cities-with-minimum-cost/
 */
public class ConnectingCitiesWithMinimumCost_Prim {

  private static class Edge {
    int target;
    int cost;

    Edge(int target, int cost) {
      this.target = target;
      this.cost = cost;
    }
  }

  /**
   * Intuition:
   * - We start with a random node and expand the MST by always picking
   *   the cheapest edge that connects a new city (node).
   * - Use an adjacency list to store the graph.
   * - Use a min-heap to always select the smallest cost edge.
   *
   * Steps:
   * 1. Build an undirected weighted graph from the input.
   * 2. Use a priority queue to pick the next minimum edge.
   * 3. Track visited cities to avoid cycles.
   * 4. Stop when all cities are visited.
   *
   * Time Complexity: O(E log V)
   * Space Complexity: O(V + E)
   */
  public int minimumCost(int n, int[][] connections) {
    // Step 1: Build graph (1-based indexing)
    Map<Integer, List<Edge>> graph = new HashMap<>();
    for (int[] conn : connections) {
      int node1 = conn[0], node2 = conn[1], cost = conn[2];
      graph.computeIfAbsent(node1, k -> new ArrayList<>()).add(new Edge(node2, cost));
      graph.computeIfAbsent(node2, k -> new ArrayList<>()).add(new Edge(node1, cost)); // undirected
    }

    // Step 2: Min-heap to pick the smallest edge
    PriorityQueue<Edge> minHeap = new PriorityQueue<>((edge1, edge2) -> edge1.cost - edge2.cost);
    Set<Integer> visited = new HashSet<>();

    // Start from any city, say 1
    visited.add(1);
    if (graph.containsKey(1)) {
      for (Edge edge : graph.get(1)) {
        minHeap.offer(edge);
      }
    }

    int totalCost = 0;

    // Step 3: Process edges until all cities are connected
    while (!minHeap.isEmpty() && visited.size() < n) {
      Edge edge = minHeap.poll();
      if (visited.contains(edge.target)) {
        continue;
      }

      visited.add(edge.target);
      totalCost += edge.cost;

      // Push all edges from the newly visited node
      if (graph.containsKey(edge.target)) {
        for (Edge next : graph.get(edge.target)) {
          if (!visited.contains(next.target)) {
            minHeap.offer(next);
          }
        }
      }
    }

    return visited.size() == n ? totalCost : -1;
  }
}
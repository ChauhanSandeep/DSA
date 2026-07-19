package graphs.mst;

import java.util.*;


/**
 * Problem: Connecting Cities With Minimum Cost (Prim)
 *
 * Given cities labeled 1 through n and weighted undirected connections, return
 * the minimum total cost to connect all cities. This version grows the minimum
 * spanning tree from city 1 using a priority queue of outgoing edges.
 *
 * Leetcode: https://leetcode.com/problems/connecting-cities-with-minimum-cost/ (Medium)
 * Rating:   1753 (zerotrac Elo)
 * Pattern:  Graph | Minimum spanning tree | Prim's algorithm
 *
 * Example:
 *   Input:  n = 3, connections = [[1,2,5],[1,3,6],[2,3,1]]
 *   Output: 6
 *   Why:    the cheapest network uses edges 2-3 and 1-2 for total cost 6.
 *
 * Follow-ups:
 *   1. Need to start from a city other than 1?
 *      Any city works for a connected undirected graph; the MST cost is unchanged.
 *   2. Need to detect disconnected input explicitly?
 *      Return -1 when the visited set has fewer than n cities after the heap drains.
 *   3. Compare with Kruskal?
 *      Kruskal sorts edges globally; Prim expands the cheapest boundary edge from the current tree.
 *
 * Related: Min Cost to Connect All Points (1584), Commutable Islands.
 */
public class ConnectingCitiesWithMinimumCost_Prim {

  public static void main(String[] args) {
    ConnectingCitiesWithMinimumCost_Prim solver = new ConnectingCitiesWithMinimumCost_Prim();
    int[][] connections1 = {{1, 2, 5}, {1, 3, 6}, {2, 3, 1}};
    int[][] connections2 = {{1, 2, 3}};

    System.out.printf("n=3 connections=%s -> %d  expected=6%n",
        Arrays.deepToString(connections1), solver.minimumCost(3, connections1));
    System.out.printf("n=3 connections=%s -> %d  expected=-1%n",
        Arrays.deepToString(connections2), solver.minimumCost(3, connections2));
  }

  private static class Edge {
    int target;
    int cost;

    Edge(int target, int cost) {
      this.target = target;
      this.cost = cost;
    }
  }

    /**
   * Intuition: Prim's algorithm keeps a growing connected set of cities and always
   * buys the cheapest edge that reaches a new city. If the heap runs out before
   * all cities are visited, the graph was disconnected.
   *
   * Algorithm:
   *   1. Build the original undirected adjacency map from connections.
   *   2. Start from city 1, mark it visited, and push its outgoing edges.
   *   3. Pop cheapest edges, skipping targets already visited.
   *   4. Add each new city cost and push that city's unvisited outgoing edges.
   *
   * Time:  O(E log E) - each connection can be pushed into the priority queue.
   * Space: O(V + E) - adjacency map, visited set, and heap.
   *
   * @param n number of cities labeled 1 through n
   * @param connections undirected weighted edges [city1, city2, cost]
   * @return minimum cost to connect all cities, or -1 when impossible
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
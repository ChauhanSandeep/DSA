package graphs.mst;

import java.util.*;


/**
 * Problem: Minimum Maximum Edge Cost After K Components
 *
 * Given a connected weighted undirected graph, remove edges so the graph has at
 * most k connected components. Minimize the maximum edge weight that remains in
 * any component.
 *
 * Leetcode: https://leetcode.com/problems/minimize-the-maximum-edge-weight-of-graph/ (Hard)
 * Rating:   2243 (zerotrac Elo)
 * Pattern:  Graph | Minimum spanning tree | Remove largest MST edges
 *
 * Example:
 *   Input:  n = 5, edges = [[0,1,4],[1,2,3],[1,3,2],[3,4,6]], k = 2
 *   Output: 4
 *   Why:    remove the heaviest MST edge 6, leaving the largest remaining component edge as 4.
 *
 * Follow-ups:
 *   1. Need exactly k components rather than at most k?
 *      For a connected graph, remove exactly k - 1 MST edges unless k >= n.
 *   2. Need the removed edges?
 *      Track the selected MST edges, sort them, and return the largest k - 1 selected edges.
 *   3. Graph may be disconnected?
 *      Build a spanning forest and validate whether the starting component count is already within k.
 *
 * Related: Min Cost to Connect All Points (1584), Kruskal MST partitioning.
 */
public class MinimumMaxCostAfterKComponents {

  /** Pair to represent (node, weight) for adjacency list and priority queue. */
  static class Pair {
    int node, weight;

    Pair(int node, int weight) {
      this.node = node;
      this.weight = weight;
    }
  }

    /**
   * Intuition: any useful solution can be viewed through an MST: first keep the
   * lightest edges needed for connectivity, then split components by removing the
   * largest MST edges. The largest remaining kept edge is the minimized maximum cost.
   *
   * Algorithm:
   *   1. Build the original adjacency list and run Prim's algorithm from node 0.
   *   2. Record weights of accepted MST edges, skipping the dummy starting edge.
   *   3. If the MST did not reach nodes - 1 edges, return -1.
   *   4. Sort MST weights, remove allowedComponents - 1 largest, and return the largest kept weight.
   *
   * Time:  O(E log E) - Prim's heap processing plus sorting selected MST weights.
   * Space: O(V + E) - adjacency list, heap, visited array, and MST weights.
   *
   * @param nodes number of nodes labeled 0 to nodes - 1
   * @param edges undirected weighted edges [u, v, weight]
   * @param allowedComponents maximum number of components allowed
   * @return minimum possible maximum edge cost among components, or -1 if the MST cannot be built
   */
  public int minimumCost(int nodes, int[][] edges, int allowedComponents) {
    // Build graph using adjacency list
    List<List<Pair>> graph = new ArrayList<>();
    for (int i = 0; i < nodes; i++) {
      graph.add(new ArrayList<>());
    }

    for (int[] edge : edges) {
      int node1 = edge[0], node2 = edge[1], weight = edge[2];
      graph.get(node1).add(new Pair(node2, weight));
      graph.get(node2).add(new Pair(node1, weight)); // Undirected graph
    }

    // Prim’s algorithm setup
    boolean[] visited = new boolean[nodes];
    PriorityQueue<Pair> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.weight, b.weight));
    List<Integer> mstWeights = new ArrayList<>(); // To track weights of edges in MST

    // Start Prim’s from node 0
    minHeap.offer(new Pair(0, 0));

    while (!minHeap.isEmpty() && mstWeights.size() < nodes - 1) {
      Pair current = minHeap.poll();
      int node = current.node;
      int weight = current.weight;

      if (visited[node]) {
        continue;
      }
      visited[node] = true;

      // Skip adding dummy 0 weight of starting node
      if (weight != 0) {
        mstWeights.add(weight);
      }

      // Add all unvisited neighbors of current node to the min-heap
      for (Pair neighbor : graph.get(node)) {
        if (!visited[neighbor.node]) {
          minHeap.offer(new Pair(neighbor.node, neighbor.weight));
        }
      }
    }

    // Sanity check: MST must have exactly (nodes - 1) edges
    if (mstWeights.size() != nodes - 1) {
      return -1;
    }

    // If allowedComponents >= nodes, each node can be its own component => no edges needed
    if (allowedComponents >= nodes) {
      return 0;
    }

    // Sort MST weights and remove allowedComponents-1 largest weights
    Collections.sort(mstWeights);
    int edgesToKeep = mstWeights.size() - (allowedComponents - 1);

    // Max of remaining weights = answer
    return edgesToKeep > 0 ? mstWeights.get(edgesToKeep - 1) : 0;
  }

  /** Driver method to test the logic */
  public static void main(String[] args) {
    MinimumMaxCostAfterKComponents solver = new MinimumMaxCostAfterKComponents();
    int[][] edges1 = {{0, 1, 4}, {1, 2, 3}, {1, 3, 2}, {3, 4, 6}};
    int[][] edges2 = {{0, 1, 4}};

    System.out.printf("nodes=5 edges=%s k=2 -> %d  expected=4%n",
        Arrays.deepToString(edges1), solver.minimumCost(5, edges1, 2));
    System.out.printf("nodes=2 edges=%s k=2 -> %d  expected=0%n",
        Arrays.deepToString(edges2), solver.minimumCost(2, edges2, 2));
  }


}
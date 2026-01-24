package graphs.mst;

import java.util.*;


/**
 Leetcode: Minimum Maximum Edge Weight in a Graph After Removals

 You are given an undirected connected graph with n nodes labeled from 0 to n - 1
 and a 2D integer array edges where edges[i] = [ui, vi, wi] denotes an edge between ui and vi with weight wi.
 You are allowed to remove any number of edges such that there are at most k connected components.

 Cost of a component = max edge weight in that component (0 if no edges).
 Return the minimum possible value of the maximum cost among all components.

 Example:
 Input: n = 5, edges = [[0,1,4],[1,2,3],[1,3,2],[3,4,6]], k = 2
 Output: 4
 Explanation: Remove edge (3-4 with weight 6), components have costs [4, 0], max = 4.

 Link: https://leetcode.com/problems/minimize-the-maximum-edge-weight-of-graph/
 *
 * LeetCode Contest Rating: 2243
 **/
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
   * Builds MST using Prim’s algorithm and removes (allowedComponents - 1) largest edges to form allowedComponents components.
   *
   * @param nodes     Number of nodes
   * @param edges Edges in format [u, v, weight]
   * @param allowedComponents     Max allowed components
   * @return Minimum possible maximum cost among all components
   *
   * Approach:
   * 1. Build an adjacency list from edges.
   * 2. Use Prim's algorithm to construct the MST.
   * 3. Track weights of selected MST edges.
   * 4. Remove top (allowedComponents - 1) highest weights to get allowedComponents components.
   * 5. Return max of remaining MST edge weights.
   *
   * Time Complexity: O(E log V) — Prim’s algorithm via PriorityQueue
   * Space Complexity: O(E + V) — For graph and visited tracking
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
    int n = 5;
    int[][] edges = {{0, 1, 4}, {1, 2, 3}, {1, 3, 2}, {3, 4, 6}};
    int k = 2;
    int result = solver.minimumCost(n, edges, k);
    System.out.println("Minimum max component cost = " + result); // Expected: 4
  }
}
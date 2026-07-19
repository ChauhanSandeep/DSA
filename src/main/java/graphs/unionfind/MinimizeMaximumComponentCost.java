package graphs.unionfind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Problem: Minimize Maximum Component Cost
 *
 * Given a connected weighted graph and a target number of components k, remove
 * edges so the maximum edge weight left inside any component is as small as
 * possible. The original solution builds an MST and cuts its largest edges.
 *
 * Leetcode: https://leetcode.com/problems/minimize-maximum-component-cost/ (Medium)
 * Rating:   1642 (zerotrac Elo)
 * Pattern:  Graph | Union-Find | Kruskal MST partitioning
 *
 * Example:
 *   Input:  n = 5, edges = [[0,1,4],[1,2,3],[1,3,2],[3,4,6]], k = 2
 *   Output: 4
 *   Why:    after removing the MST edge of weight 6, the largest remaining selected edge is 4.
 *
 * Follow-ups:
 *   1. Return which edges to remove?
 *      Track MST edges and output the k - 1 largest selected edges.
 *   2. Edge weights can be negative?
 *      Kruskal still sorts by weight; define component cost carefully for all-negative components.
 *   3. Minimize the sum of component costs instead?
 *      That changes the objective and generally needs a different optimization strategy.
 *
 * Related: Minimum Spanning Tree, Min Cost to Connect All Points (1584).
 */
public class MinimizeMaximumComponentCost {

  public static void main(String[] args) {
    MinimizeMaximumComponentCost solver = new MinimizeMaximumComponentCost();
    int[][] edges1 = {{0, 1, 4}, {1, 2, 3}, {1, 3, 2}, {3, 4, 6}};
    int[][] edges2 = {{0, 1, 4}};

    System.out.printf("nodes=5 edges=%s k=2 -> %d  expected=4%n",
        Arrays.deepToString(edges1), solver.minimizeMaximumComponentCost(5, edges1, 2));
    System.out.printf("nodes=2 edges=%s k=2 -> %d  expected=0%n",
        Arrays.deepToString(edges2), solver.minimizeMaximumComponentCost(2, edges2, 2));
  }

  /**
   * Uses Kruskal's MST algorithm with strategic edge removal to partition the graph.
   *
   * Intuition:
   * To minimize the maximum edge weight in any component, we should:
   * 1. Keep only the lightest edges that connect nodes (MST gives us this)
   * 2. Break the graph by removing the heaviest edges from the MST
   * 
   * Key Insight: 
   * An MST of n nodes has exactly (n-1) edges. To create k components, we need to 
   * remove (k-1) edges. By removing the (k-1) heaviest edges from the MST, we create 
   * k components where each component's maximum edge weight is minimized.
   *
   * Approach:
   * Step 1: Build MST using Kruskal's algorithm (Union-Find with sorted edges)
   *         - Sort all edges by weight in ascending order
   *         - Add edges to MST if they connect different components
   *         - Track all MST edge weights in a list
   * 
   * Step 2: Partition the MST into k components
   *         - Sort MST edges by weight (already sorted from Kruskal's)
   *         - Remove the (k-1) largest edges from MST
   *         - This splits the tree into exactly k components
   * 
   * Step 3: Find the answer
   *         - After removing (k-1) edges, we have (n-k) edges remaining
   *         - The maximum weight among remaining edges is our answer
   *
   * Example Walkthrough (n=5, edges=[[0,1,4],[1,2,3],[1,3,2],[3,4,6]], k=2):
   * - MST edges after Kruskal's: [2, 3, 4, 6] (weights only)
   * - To create k=2 components, remove (k-1)=1 heaviest edge: remove 6
   * - Remaining MST edges: [2, 3, 4]
   * - Maximum weight among remaining edges: 4 ✓
   *
   * Time Complexity: O(E log E) - dominated by edge sorting in Kruskal's algorithm
   * Space Complexity: O(N + E) - Union-Find structure (N) + MST edge list (E)
   *
   * @param numNodes Number of nodes in the graph
   * @param edges List of edges where each edge is [u, v, weight]
   * @param numComponents Number of connected components desired (k)
   * @return Minimum possible maximum cost among all components
   */
  public int minimizeMaximumComponentCost(int numNodes, int[][] edges, int numComponents) {
    // Edge Case: If k >= n, isolate all nodes (remove all edges)
    if (numComponents >= numNodes) {
      return 0;
    }

    // Step 1: Build MST using Kruskal's Algorithm
    // Sort edges by weight (ascending order)
    Arrays.sort(edges, Comparator.comparingInt(edge -> edge[2]));

    DSU disjointSet = new DSU(numNodes);
    List<Integer> sortedMstEdgeWeights = new ArrayList<>();

    // Add edges to MST if they connect different components
    for (int[] edge : edges) {
      int nodeA = edge[0];
      int nodeB = edge[1];
      int weight = edge[2];
      
      if (disjointSet.union(nodeA, nodeB)) {
        sortedMstEdgeWeights.add(weight);
      }
    }

    // Step 2: Remove (k-1) heaviest edges to create k components
    // MST has (n-1) edges, removing (k-1) leaves us with (n-k) edges
    int edgesAfterRemoval = sortedMstEdgeWeights.size() - (numComponents - 1);

    // Step 3: The maximum weight among remaining edges is the answer
    // Since mstEdgeWeights is sorted, the last remaining edge has max weight
    return sortedMstEdgeWeights.get(edgesAfterRemoval - 1);
  }

  /**
   * Union-Find (Disjoint Set Union) with Path Compression.
   */
  static class DSU {
    int[] parent;

    DSU(int size) {
      parent = new int[size];
      for (int i = 0; i < size; i++) {
        parent[i] = i;
      }
    }

    // Finds the representative (root) of the component for node x
    int find(int x) {
      if (x != parent[x]) {
        parent[x] = find(parent[x]); // Path compression
      }
      return parent[x];
    }

    // Unions the components of x and y if they are in different sets
    // Returns true if they were in different sets (union was successful)
    boolean union(int x, int y) {
      int rootX = find(x);
      int rootY = find(y);
      if (rootX == rootY) {
        return false;
      }
      // union by rank is not necessary here since we are not optimizing for size. Therefore, we can just attach one root to another.
      parent[rootX] = rootY;
      return true;
    }
  }
}
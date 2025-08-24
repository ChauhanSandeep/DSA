package graph.unionfind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Problem: Minimize Maximum Component Cost
 * Leetcode: https://leetcode.com/problems/minimize-maximum-component-cost
 *
 * Problem Statement:
 * Given a weighted undirected graph with `n` nodes and a list of edges [u, v, weight],
 * partition the graph into `k` connected components such that the maximum cost
 * of any component is minimized. Cost is defined as the largest edge weight in that component.
 *f
 * Example:
 * Input: n = 5, edges = [[0,1,1],[1,2,3],[2,3,4],[3,4,2]], k = 2
 * Output: 3
 *
 * Explanation:
 * - The MST of the graph has edges [0-1 (1), 3-4 (2), 1-2 (3), 2-3 (4)]
 * - To split into 2 components, we remove 1 edge from MST.
 * - Remove the largest weight edge (4), then the max weight in remaining MST is 3.
 *
 * Follow-up Questions:
 * - Can you do this for directed graphs? → Not directly with MST, need SCC algorithms.
 * - Can you maximize the *minimum* cost instead? (Binary search + DSU variant)
 * - Leetcode Follow-up: None explicitly, but similar pattern in:
 *     - https://leetcode.com/problems/optimize-water-distribution-in-a-village/
 */
public class MinimizeMaximumComponentCost {

  /**
   * Uses Kruskal's algorithm to build MST and removes (k - 1) heaviest edges
   * to partition the graph into k components with minimized max edge cost.
   *
   * Approach:
   * 1. Build a Minimum Spanning Tree (MST) using Kruskal’s Algorithm. This ensures that we
   * have the minimum possible edge weights connecting all nodes. These will be definitely removed.
   * 2. From the MST, remove (k - 1) largest edges to split it into k components.
   * 3. The result is the max weight among the remaining edges.
   *
   * Time Complexity: O(E * logE + E) ~ O(E logE), where E is number of edges.
   * Space Complexity: O(N + E)
   *
   * @param numNodes Number of nodes in the graph
   * @param edges List of edges where each edge is [u, v, weight]
   * @param numComponents Number of connected components desired
   * @return Minimum possible maximum cost among all components
   */
  public int minimizeMaximumComponentCost(int numNodes, int[][] edges, int numComponents) {
    // Step 1: Sort all edges by weight in ascending order (Kruskal’s)
    Arrays.sort(edges, Comparator.comparingInt(edge -> edge[2]));

    DSU disjointSet = new DSU(numNodes);
    List<Integer> mstWeightList = new ArrayList<>();

    // Step 2: Build MST and record selected edge weights
    for (int[] edge : edges) {
      int nodeA = edge[0];
      int nodeB = edge[1];
      int weight = edge[2];
      if (disjointSet.union(nodeA, nodeB)) {
        // if these nodes were in different components, add this edge to MST
        mstWeightList.add(weight);
      }
    }

    // Edge Case: If components required are >= number of nodes, all nodes can be isolated
    if (numComponents >= numNodes) {
      return 0;
    }

    // Step 3: Remove (k - 1) largest weights to form k components
    Collections.sort(mstWeightList); // Already sorted during Kruskal; but re-confirm here
    int remainingEdges = mstWeightList.size() - (numComponents - 1);

    // Step 4: The answer is the maximum edge in the remaining MST
    return mstWeightList.get(remainingEdges - 1);
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
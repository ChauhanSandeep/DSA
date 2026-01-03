package graphs.unionfind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * You are given an undirected connected graph with n nodes labeled from 0 to n - 1 and a 2D
 * integer array edges where edges[i] = [ui, vi, wi] denotes an undirected edge between node ui
 * and node vi with weight wi, and an integer k.
 *
 * You are allowed to remove any number of edges from the graph such that the resulting graph
 * has at most k connected components.
 *
 * The cost of a component is defined as the maximum edge weight in that component. If a component
 * has no edges, its cost is 0.
 *
 * Return the minimum possible value of the maximum cost among all components after such removals.
 *
 * Example 1:
 * Input: n = 5, edges = [[0,1,4],[1,2,3],[1,3,2],[3,4,6]], k = 2
 * Output: 4
 * Explanation:
 * We need to split the graph into at most 2 components. The key insight is to remove the
 * heaviest edge (weight 6) between nodes 3 and 4.
 * After removal:
 * - Component 1: nodes {0,1,2,3} with edges [0,1,4], [1,2,3], [1,3,2]. Maximum edge weight = 4
 * - Component 2: node {4} with no edges. Maximum edge weight = 0
 * The maximum cost among all components is max(4, 0) = 4.
 * This is optimal because if we keep the edge with weight 6, the maximum cost would be 6.
 *
 * LeetCode Problem: https://leetcode.com/problems/minimize-maximum-component-cost/
 *
 * Follow-up Questions:
 *
 * 1. What if we want to know which exact edges to remove?
 *    Answer: Modify the solution to track which edges are included when forming components.
 *    During binary search, when we find the optimal threshold weight, record all edges with
 *    weight greater than this threshold - those are the edges to remove.
 *
 * 2. How would you handle the case where edge weights can be negative?
 *    Answer: The problem becomes more complex. We'd need to adjust the binary search range
 *    to include negative values. The concept remains the same, but the lower bound would be
 *    the minimum edge weight instead of 0.
 *
 * 3. What if instead of removing edges, we can add edges with cost?
 *    Answer: This becomes a different problem - Minimum Spanning Tree variants. We'd use
 *    Kruskal's or Prim's algorithm but stop when we have exactly k components, adding edges
 *    in increasing order of weight.
 *    Related: https://leetcode.com/problems/min-cost-to-connect-all-points/
 *
 * 4. Can we solve this if k can be larger than n?
 *    Answer: If k >= n, we can split the graph into n separate components (all isolated nodes)
 *    by removing all edges. The maximum cost would be 0 since no component has edges.
 *
 * 5. What if we want to minimize the sum of all component costs instead of maximum?
 *    Answer: This requires a different approach. We'd use dynamic programming or greedy
 *    algorithm to partition the graph. We'd still use Union-Find but optimize for sum
 *    rather than maximum, which changes the decision-making process significantly.
 */
public class MinimizeMaximumComponentCost {

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
    List<Integer> mstEdgeWeights = new ArrayList<>();

    // Add edges to MST if they connect different components
    for (int[] edge : edges) {
      int nodeA = edge[0];
      int nodeB = edge[1];
      int weight = edge[2];
      
      if (disjointSet.union(nodeA, nodeB)) {
        mstEdgeWeights.add(weight);
      }
    }

    // Step 2: Remove (k-1) heaviest edges to create k components
    // MST has (n-1) edges, removing (k-1) leaves us with (n-k) edges
    int edgesAfterRemoval = mstEdgeWeights.size() - (numComponents - 1);

    // Step 3: The maximum weight among remaining edges is the answer
    // Since mstEdgeWeights is sorted, the last remaining edge has max weight
    return mstEdgeWeights.get(edgesAfterRemoval - 1);
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
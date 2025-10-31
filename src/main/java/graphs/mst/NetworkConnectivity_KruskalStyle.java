package graphs.mst;

/**
 * There are n computers labeled from 0 to n - 1.
 * You are given a list of connections where each connection[i] = [ai, bi] represents a connection between computers ai and bi.
 * Any computer can reach any other directly or indirectly through a series of connections.
 * You can remove and reuse cables (edges) between any two computers.
 *
 * Return the minimum number of operations needed to connect all computers.
 * If it's not possible, return -1.
 *
 * Example:
 * Input: n = 6, connections = [[0,1],[0,2],[0,3],[1,4]]
 * Output: 1
 *
 * Link: https://leetcode.com/problems/number-of-operations-to-make-network-connected/
 */
public class NetworkConnectivity_KruskalStyle {

  /**
   * Intuition:
   * - This is a graph connectivity problem. We want to connect all nodes (computers) using given edges.
   * - Think of it as forming a single connected component (like a Minimum Spanning Tree).
   * - Use Union-Find to count how many connected components we have.
   *
   * Key Observations:
   * - To connect n nodes, we need at least (n - 1) edges.
   * - Extra edges (that form cycles) can be reused to connect disconnected parts.
   *
   * Steps:
   * 1. If number of connections < n - 1 => Not enough cables, return -1.
   * 2. Use Union-Find to group nodes into connected components.
   * 3. The number of operations needed = (number of components - 1)
   *
   * Time Complexity: O(N + E * α(N)) — α(N) is inverse Ackermann, nearly constant
   * Space Complexity: O(N) — for parent and rank arrays
   */
  public int makeConnected(int n, int[][] connections) {
    // Not enough cables to connect all nodes
    if (connections.length < n - 1) {
      return -1;
    }

    UnionFind uf = new UnionFind(n);
    int components = n;

    for (int[] conn : connections) {
      int node1 = conn[0];
      int node2 = conn[1];
      if (uf.union(node1, node2)) {
        components--; // Successfully merged two components
      }
    }

    // Minimum operations to connect all components is (components - 1)
    return components - 1;
  }

  /**
   * Classic Union-Find (Disjoint Set Union) implementation.
   * This helps in efficiently managing and merging connected components.
   * It uses path compression and union by rank to optimize the find and union operations.
   */
  static class UnionFind {
    int[] parent; // Denotes the parent of each node, initially itself
    int[] rank; // Denotes the rank (depth) of each node, used for union by rank

    public UnionFind(int size) {
      parent = new int[size];
      rank = new int[size]; // initially all ranks are 0
      for (int i = 0; i < size; i++) {
        parent[i] = i; // each node is its own parent initially
      }
    }

    public int find(int x) {
      if (parent[x] != x) {
        parent[x] = find(parent[x]); // Path compression while finding the root
      }
      return parent[x];
    }

    /**
     * Join two components if they are not already connected.
     * This method returns true if they were successfully merged.
     * If they are already connected, it returns false.
     * Join is done in by using rank to keep the tree flat.
     *
     * @param nodeX
     * @param nodeY
     * @return
     */
    public boolean union(int nodeX, int nodeY) {
      int rootX = find(nodeX);
      int rootY = find(nodeY);

      if (rootX == rootY) {
        return false; // already connected
      }

      /**
       * Merge the two components while maintaining the rank
       * The idea is that we always attach the smaller tree under the larger tree
       * because this keeps the tree flat and optimizes future find operations
       * otherwise we could end up with a deep tree which would slow down the find operation
       */
      if (rank[rootX] < rank[rootY]) {
        // rootX is deeper, attach it to rootY
        parent[rootX] = rootY;
      } else if (rank[rootX] > rank[rootY]) {
        // rootY is deeper, attach it to rootX
        parent[rootY] = rootX;
      } else {
        // both have same rank, attach one to the other and increase rank
        parent[rootY] = rootX;
        rank[rootX]++;
      }
      return true;
    }
  }
}
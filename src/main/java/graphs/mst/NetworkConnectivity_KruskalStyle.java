package graphs.mst;

/**
 * Problem: Number of Operations to Make Network Connected
 *
 * Given n computers and undirected cable connections, cables may be removed and
 * reused elsewhere. Return the minimum number of operations needed to connect all
 * computers, or -1 if there are not enough cables.
 *
 * Leetcode: https://leetcode.com/problems/number-of-operations-to-make-network-connected/ (Medium)
 * Rating:   1633 (zerotrac Elo)
 * Pattern:  Graph | Union-Find | Connected components
 *
 * Example:
 *   Input:  n = 6, connections = [[0,1],[0,2],[0,3],[1,4]]
 *   Output: -1
 *   Why:    four cables cannot connect six computers because at least n - 1 cables are required.
 *
 * Follow-ups:
 *   1. Need the specific cables to move?
 *      Track redundant edges while union returns false, then pair them with disconnected components.
 *   2. Connections arrive online?
 *      DSU can merge components incrementally and report the current component count.
 *   3. Need to support deletions?
 *      Use offline dynamic connectivity or more advanced fully dynamic graph structures.
 *
 * Related: Number of Provinces (547), Redundant Connection (684), Accounts Merge (721).
 */
public class NetworkConnectivity_KruskalStyle {

  public static void main(String[] args) {
    NetworkConnectivity_KruskalStyle solver = new NetworkConnectivity_KruskalStyle();
    int[][] connections1 = {{0, 1}, {0, 2}, {0, 3}, {1, 4}};
    int[][] connections2 = {{0, 1}, {0, 2}, {1, 2}};

    System.out.printf("n=6 connections=%s -> %d  expected=-1%n",
        java.util.Arrays.deepToString(connections1), solver.makeConnected(6, connections1));
    System.out.printf("n=4 connections=%s -> %d  expected=1%n",
        java.util.Arrays.deepToString(connections2), solver.makeConnected(4, connections2));
  }

    /**
   * Intuition: to connect n computers, at least n - 1 cables must exist. Once that
   * is true, every extra cable inside a component can be reused to connect two
   * components, so the answer is the number of connected components minus one.
   *
   * Algorithm:
   *   1. Return -1 immediately if there are fewer than n - 1 connections.
   *   2. Initialize Union-Find with each computer as its own component.
   *   3. Union every cable endpoint and decrement components on successful merges.
   *   4. Return components - 1.
   *
   * Time:  O(N + E * a(N)) - Union-Find operations are effectively constant time.
   * Space: O(N) - parent and rank arrays.
   *
   * @param n number of computers labeled 0 to n - 1
   * @param connections undirected cable endpoints [a, b]
   * @return minimum rewiring operations, or -1 if not enough cables exist
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
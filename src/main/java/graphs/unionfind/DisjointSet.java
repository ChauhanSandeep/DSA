package graphs.unionfind;

/**
 * Disjoint Set (Union-Find) Data Structure
 *
 * A disjoint-set data structure maintains a collection of non-overlapping sets.
 * It provides near-constant-time operations to add new sets, merge sets,
 * and determine whether elements are in the same set.
 *
 * This implementation uses two key optimizations:
 * 1. **Path Compression**: During find operations, flatten the tree structure by making
 *    nodes point directly to the root, reducing future query times.
 * 2. **Union by Rank**: When merging sets, attach the shorter tree under the root of
 *    the taller tree to keep the tree balanced and shallow.
 *
 * Applications:
 * - Kruskal's Minimum Spanning Tree algorithm
 * - Network connectivity problems
 * - Image processing (finding connected components)
 * - Detecting cycles in undirected graphs
 * - Least Common Ancestor problems
 *
 * Time Complexity (with both optimizations):
 * - Find: O(α(n)) - inverse Ackermann function, practically constant
 * - Union: O(α(n)) - inverse Ackermann function, practically constant
 * - Connected: O(α(n)) - two find operations
 *
 * Space Complexity: O(n) for parent and rank arrays
 *
 * Related LeetCode Problems:
 * - #547 Number of Provinces
 * - #684 Redundant Connection
 * - #721 Accounts Merge
 * - #990 Satisfiability of Equality Equations
 * - #1202 Smallest String With Swaps
 *
 * @author Sandeep Chauhan
 */
public class DisjointSet {
  private int[] parent; // parent[i] is the parent of node i
  private int[] rank; // rank[i] is the rank (approximate depth) of the tree rooted at i

  /**
   * Constructor to create 'n' disjoint sets (0 to n - 1),
   * where each element starts in its own group.
   *
   * @param n Number of elements (creates sets for elements 0 to n-1)
   */
  public DisjointSet(int n) {
    parent = new int[n];
    rank = new int[n];

    // Initially, every node is its own parent (self-rooted)
    for (int i = 0; i < n; i++) {
      parent[i] = i;
    }
  }

  /**
   * Find the root (representative) of the set that 'x' belongs to.
   * Uses path compression to flatten the tree structure for better performance.
   *
   * Path Compression Optimization:
   * - As we traverse up to find the root, we update each node to point directly to the root.
   * - This flattens the tree structure and speeds up future find operations.
   * - Example: If chain is 1->2->3->4 (root), after find(1), it becomes 1->4, 2->4, 3->4.
   *
   * Time Complexity: O(α(n)) - inverse Ackermann function, practically constant
   *
   * @param x Element to find the root of
   * @return The root (representative) of the set containing x
   */
  public int find(int x) {
    if (parent[x] != x) {
      parent[x] = find(parent[x]); // Path compression - point directly to root
    }
    return parent[x];
  }

  /**
   * Union the sets containing 'x' and 'y'.
   * Uses union-by-rank to attach smaller tree under the larger one, keeping trees balanced.
   *
   * Union by Rank Optimization:
   * - Always attach the tree with lower rank under the root of the tree with higher rank.
   * - This keeps the overall tree structure shallow, improving find operation performance.
   * - Rank is only incremented when merging two trees of equal rank.
   * - This ensures the tree height grows logarithmically with the number of elements.
   *
   * Algorithm:
   * 1. Find roots of both elements
   * 2. If already in same set, do nothing
   * 3. Otherwise, attach the tree with smaller rank under the tree with larger rank
   * 4. If ranks are equal, arbitrarily choose one as parent and increment its rank
   *
   * Time Complexity: O(α(n)) - dominated by two find operations
   *
   * @param x First element
   * @param y Second element
   */
  public void union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) {
      return; // Already in same set, no operation needed
    }

    // Union by rank: attach smaller tree under larger tree
    if (rank[rootX] < rank[rootY]) {
      parent[rootX] = rootY;
    } else if (rank[rootX] > rank[rootY]) {
      parent[rootY] = rootX;
    } else {
      // Equal rank: choose arbitrarily and increment rank
      parent[rootY] = rootX;
      rank[rootX]++;
    }
  }

  /**
   * Check if two nodes are in the same set.
   * Two elements are connected if they have the same root representative.
   *
   * Time Complexity: O(α(n)) - performs two find operations
   *
   * @param x First element
   * @param y Second element
   * @return true if x and y belong to the same set, false otherwise
   */
  public boolean connected(int x, int y) {
    return find(x) == find(y);
  }
}
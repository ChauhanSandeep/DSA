package graphs.unionfind;

/**
 * Problem: Disjoint Set (Union-Find) Data Structure
 *
 * Maintain non-overlapping sets with efficient find, union, and connected
 * operations. Path compression and union by rank keep repeated operations fast.
 *
 * Pattern:  Data structure | Union-Find | Path compression and union by rank
 *
 * Example:
 *   Input:  union(0,1), union(1,2), connected(0,2)
 *   Output: true
 *   Why:    0 and 2 share the same root after the two unions.
 *
 * Follow-ups:
 *   1. Need component sizes?
 *      Store a size array at roots and update it during union.
 *   2. Need rollback support?
 *      Avoid path compression and keep a stack of parent/rank changes to undo unions.
 *   3. Need weighted ratios between nodes?
 *      Store each node's weight relative to its parent and update weights during union.
 *
 * Related: Number of Provinces (547), Accounts Merge (721), Redundant Connection (684).
 */
public class DisjointSet {

  public static void main(String[] args) {
    DisjointSet disjointSet = new DisjointSet(4);
    disjointSet.union(0, 1);
    disjointSet.union(1, 2);
    System.out.printf("operations=[union(0,1), union(1,2)] query=connected(0,2) -> %s  expected=true%n",
        disjointSet.connected(0, 2));
    System.out.printf("operations=[union(0,1), union(1,2)] query=connected(0,3) -> %s  expected=false%n",
        disjointSet.connected(0, 3));
  }
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
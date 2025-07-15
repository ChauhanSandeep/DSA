package Graph.unionfind;

public class DisjointSet {
  private int[] parent; // parent[i] is the parent of i
  private int[] rank; // rank[i] is the rank (depth) of the tree rooted at i

  /**
   * Constructor to create 'n' disjoint sets (0 to n - 1),
   * where each element starts in its own group.
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
   * Uses path compression to flatten the tree structure.
   */
  public int find(int x) {
    if (parent[x] != x) {
      parent[x] = find(parent[x]); // Path compression
    }
    return parent[x];
  }

  /**
   * Union the sets containing 'x' and 'y'.
   * Uses union-by-rank to attach smaller tree under the larger one.
   */
  public void union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) {
      return; // Already in same set
    }

    // Union by rank
    if (rank[rootX] < rank[rootY]) {
      parent[rootX] = rootY;
    } else if (rank[rootX] > rank[rootY]) {
      parent[rootY] = rootX;
    } else {
      parent[rootY] = rootX;
      rank[rootX]++;
    }
  }

  /**
   * Check if two nodes are in the same set.
   */
  public boolean connected(int x, int y) {
    return find(x) == find(y);
  }
}
package graphs;

/**
 * In this problem, a rooted tree is a directed graph such that, there is exactly one node (the root)
 * for which all other nodes are descendants of this node, plus every node has exactly one parent,
 * except for the root node which has no parents.
 *
 * The given input is a directed graph that started as a rooted tree with n nodes (with distinct values
 * from 1 to n), with one additional directed edge added. The added edge has two different vertices
 * chosen from 1 to n, and was not an edge that already existed.
 *
 * The resulting graph is given as a 2D-array of edges. Each element of edges is a pair [ui, vi]
 * which represents a directed edge connecting nodes ui and vi, where ui is the parent of child vi.
 *
 * Return an edge that can be removed so that the resulting graph is a rooted tree of n nodes.
 * If there are multiple answers, return the answer that occurs last in the given 2D-array.
 *
 * Example 1:
 * Input: edges = [[1,2],[1,3],[2,3]]
 * Output: [2,3]
 *
 * Example 2:
 * Input: edges = [[1,2],[2,3],[3,4],[4,1],[1,5]]
 * Output: [4,1]
 *
 * LeetCode: https://leetcode.com/problems/redundant-connection-ii/
 *
 * Follow-up Questions:
 * 1. How would you handle the case where multiple edges can be removed to form a valid tree?
 *    - The problem guarantees exactly one redundant edge, but if there were multiple, we'd need to return all of them.
 * 2. What if the graph is very large (e.g., 10^5 nodes and edges)?
 *    - The Union-Find solution with path compression and union by rank is efficient (near O(1) per operation).
 * 3. How would you modify the solution to return all possible redundant edges?
 *    - We could collect all edges that cause a cycle or a node to have two parents.
 *
 * Related Problems:
 * - Redundant Connection (https://leetcode.com/problems/redundant-connection/)
 * - Graph Valid Tree (https://leetcode.com/problems/graph-valid-tree/)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RedundantConnectionII {
    /**
     * Finds the redundant directed connection in the given edges using Union-Find.
     *
     * @param edges Array of directed edges where edges[i] = [parent, child]
     * @return The redundant edge that can be removed
     */
    public int[] findRedundantDirectedConnection(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1]; // To detect nodes with two parents
        int[] candidate1 = null, candidate2 = null;

        // First pass: Find if any node has two parents
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];

            if (parent[v] == 0) {
                parent[v] = u;
            } else {
                // Found a node with two parents
                candidate1 = new int[]{parent[v], v};
                candidate2 = new int[]{u, v};
                // Mark the second edge as invalid by setting its second element to 0
                edge[1] = 0;
                break;
            }
        }

        // Second pass: Union-Find to find the edge causing the cycle
        UnionFind uf = new UnionFind(n + 1);
        for (int[] edge : edges) {
            // Skip the invalid edge (if any)
            if (edge[1] == 0) {
                continue;
            }

            int u = edge[0], v = edge[1];

            // If we find a cycle
            if (!uf.union(u, v)) {
                // If we had a node with two parents, return the first candidate
                // Otherwise, return the current edge causing the cycle
                return candidate1 == null ? edge : candidate1;
            }
        }

        // If we get here, the redundant edge must be the second candidate
        return candidate2;
    }

    /**
     * Union-Find (Disjoint Set) data structure with path compression and union by rank.
     */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];

            // Initialize each element as its own parent
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        /**
         * Finds the root of the set containing x with path compression.
         */
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        /**
         * Unions the sets containing x and y.
         *
         * @return true if the union was successful, false if x and y are already in the same set
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false; // Already in the same set, this edge forms a cycle
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

            return true;
        }
    }

    /**
     * Alternative solution that handles all cases explicitly.
     */
    public int[] findRedundantDirectedConnectionAlternative(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1]; // To track direct parent of each node
        int[] root = new int[n + 1];   // For Union-Find
        int[] size = new int[n + 1];   // For Union-Find

        // Initialize Union-Find
        for (int i = 1; i <= n; i++) {
            root[i] = i;
            size[i] = 1;
        }

        int[] candidate1 = null, candidate2 = null;
        boolean hasCycle = false;

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];

            // Case 1: Node v already has a parent (two parents case)
            if (parent[v] != 0) {
                candidate1 = new int[]{parent[v], v};
                candidate2 = new int[]{u, v};

                // Remove the second candidate edge
                edge[1] = 0;
            } else {
                parent[v] = u;

                // Find roots for Union-Find
                int rootU = find(root, u);
                int rootV = find(root, v);

                // If adding this edge creates a cycle
                if (rootU == rootV) {
                    hasCycle = true;
                    if (candidate1 == null) {
                        // No node with two parents, so this edge is the answer
                        return edge;
                    } else {
                        // We have a node with two parents, return the first candidate
                        return candidate1;
                    }
                }

                // Union the sets
                union(root, size, u, v);
            }
        }

        // If we get here, the graph has a node with two parents but no cycle
        // We need to determine which edge to remove
        if (candidate1 != null && candidate2 != null) {
            // If there's a cycle, remove the first candidate
            // Otherwise, remove the second candidate
            return hasCycle ? candidate1 : candidate2;
        }

        // Shouldn't reach here as per problem statement
        return new int[0];
    }

    // Helper method for Union-Find find operation
    private int find(int[] root, int x) {
        while (root[x] != x) {
            root[x] = root[root[x]]; // Path compression
            x = root[x];
        }
        return x;
    }

    // Helper method for Union-Find union operation
    private void union(int[] root, int[] size, int x, int y) {
        int rootX = find(root, x);
        int rootY = find(root, y);

        if (rootX == rootY) return;

        // Union by size
        if (size[rootX] < size[rootY]) {
            root[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            root[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }
}

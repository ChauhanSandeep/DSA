package graphs;

/**
 * Alice and Bob have an undirected graph of n nodes and 3 types of edges:
 * - Type 1: Can be traversed by Alice only.
 * - Type 2: Can be traversed by Bob only.
 * - Type 3: Can by traversed by both Alice and Bob.
 *
 * Given the edges, return the maximum number of edges you can remove so that after removing the edges,
 * the graph can still be fully traversed by both Alice and Bob. If it's impossible for the graph to be
 * fully traversed by both Alice and Bob, return -1.
 *
 * Example 1:
 * Input: n = 4, edges = [[3,1,2],[3,2,3],[1,1,3],[1,2,4],[1,1,2],[2,3,4]]
 * Output: 2
 * Explanation: If we remove the 2 edges [1,1,2] and [1,1,3], then both Alice and Bob can still traverse the graph.
 *
 * LeetCode: https://leetcode.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/
 *
 * Follow-up Questions:
 * 1. How would you handle the case where there are more types of users with different edge access?
 *    - We could extend the solution to handle k different users by maintaining k separate Union-Find structures.
 * 2. What if the graph is very large (e.g., 100,000 nodes)?
 *    - The Union-Find with path compression and union by rank is efficient enough (near O(1) per operation).
 * 3. How would you modify the solution to find the specific edges to remove?
 *    - We could track which edges are included in the MSTs for both Alice and Bob.
 *
 * Related Problems:
 * - Redundant Connection (https://leetcode.com/problems/redundant-connection/)
 * - Graph Valid Tree (https://leetcode.com/problems/graph-valid-tree/)
 */
public class RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable {
    /**
     * Calculates the maximum number of edges that can be removed while keeping the graph fully traversable.
     *
     * @param n Number of nodes
     * @param edges Array of edges where edges[i] = [type, u, v]
     * @return Maximum number of edges that can be removed, or -1 if not possible
     */
    public int maxNumEdgesToRemove(int n, int[][] edges) {
        // Sort edges in descending order of type (type 3 first, then type 1 and 2)
        // This ensures we process shared edges first
        java.util.Arrays.sort(edges, (a, b) -> b[0] - a[0]);

        // Create separate Union-Find structures for Alice and Bob
        UnionFind aliceUF = new UnionFind(n);
        UnionFind bobUF = new UnionFind(n);

        int edgesAdded = 0; // Count of edges added to both Alice's and Bob's graphs

        for (int[] edge : edges) {
            int type = edge[0];
            int u = edge[1] - 1; // Convert to 0-based index
            int v = edge[2] - 1;

            if (type == 3) { // Type 3: Can be used by both Alice and Bob
                boolean aliceUnion = aliceUF.union(u, v);
                boolean bobUnion = bobUF.union(u, v);

                if (aliceUnion || bobUnion) {
                    // If at least one of them can use this edge, count it
                    edgesAdded++;
                }
            } else if (type == 1) { // Type 1: Alice only
                if (aliceUF.union(u, v)) {
                    edgesAdded++;
                }
            } else if (type == 2) { // Type 2: Bob only
                if (bobUF.union(u, v)) {
                    edgesAdded++;
                }
            }
        }

        // Check if both Alice and Bob can traverse the entire graph
        if (aliceUF.getComponents() == 1 && bobUF.getComponents() == 1) {
            return edges.length - edgesAdded;
        } else {
            return -1; // Not possible for both to traverse
        }
    }

    /**
     * Union-Find (Disjoint Set) data structure with path compression and union by rank.
     */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;
        private int components;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            components = size; // Initially, each node is its own component

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
                return false; // Already in the same set
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

            components--; // Decrease the number of components
            return true;
        }

        /**
         * Returns the number of connected components in the Union-Find structure.
         */
        public int getComponents() {
            return components;
        }
    }

    /**
     * Alternative approach using a single Union-Find structure for both Alice and Bob.
     * This approach processes type 3 edges first, then type 1 and 2 separately.
     */
    public int maxNumEdgesToRemoveAlternative(int n, int[][] edges) {
        // Sort edges in descending order of type (type 3 first, then type 1 and 2)
        java.util.Arrays.sort(edges, (a, b) -> b[0] - a[0]);

        UnionFind aliceUF = new UnionFind(n);
        UnionFind bobUF = new UnionFind(n);

        int edgesAdded = 0;

        // First, add all type 3 edges (shared by both Alice and Bob)
        for (int[] edge : edges) {
            if (edge[0] == 3) {
                int u = edge[1] - 1;
                int v = edge[2] - 1;

                boolean aliceUnion = aliceUF.union(u, v);
                boolean bobUnion = bobUF.union(u, v);

                if (aliceUnion || bobUnion) {
                    edgesAdded++;
                }
            }
        }

        // Then, add type 1 edges for Alice and type 2 for Bob
        for (int[] edge : edges) {
            int type = edge[0];
            int u = edge[1] - 1;
            int v = edge[2] - 1;

            if (type == 1) {
                if (aliceUF.union(u, v)) {
                    edgesAdded++;
                }
            } else if (type == 2) {
                if (bobUF.union(u, v)) {
                    edgesAdded++;
                }
            }
        }

        // Check if both Alice and Bob can traverse the entire graph
        if (aliceUF.getComponents() == 1 && bobUF.getComponents() == 1) {
            return edges.length - edgesAdded;
        } else {
            return -1;
        }
    }
}

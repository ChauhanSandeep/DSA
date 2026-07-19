package graphs;

import java.util.Arrays;

/**
 * Problem: Remove Max Number of Edges to Keep Graph Fully Traversable
 *
 * Alice and Bob share an undirected graph with three edge types: Alice-only,
 * Bob-only, and shared. Remove as many edges as possible while still allowing
 * both Alice and Bob to traverse every node in their own usable graph.
 *
 * Leetcode: https://leetcode.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/
 * Rating:   2132 (zerotrac Elo)
 * Pattern:  Graph | Union-Find | Greedy shared-edge reuse
 *
 * Example:
 *   Input:  n = 4, edges = [[3,1,2],[3,2,3],[1,1,3],[1,2,4],[1,1,2],[2,3,4]]
 *   Output: 2
 *   Why:    the shared edges connect most of both graphs, then one Alice-only and
 *           one Bob-only edge finish connectivity; the two leftover Alice edges are removable.
 *
 * Follow-ups:
 *   1. Return the exact edges removed?
 *      Add every edge whose union fails to a removable list, then validate connectivity.
 *   2. More than two users have different edge access?
 *      Keep one Union-Find per user and greedily process edges usable by the most users first.
 *   3. Edge removals have different profits?
 *      This becomes a weighted optimization problem, not just counting failed unions.
 *
 * Related: Redundant Connection (684), Graph Valid Tree (261), Most Stones Removed (947).
 */
public class RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable {

    public static void main(String[] args) {
        RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable solver = new RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable();
        int[] nodes = {4, 4};
        int[][][] edges = {{{3, 1, 2}, {3, 2, 3}, {1, 1, 3}, {1, 2, 4}, {1, 1, 2}, {2, 3, 4}}, {{3, 1, 2}, {3, 2, 3}, {1, 1, 4}, {2, 1, 4}}};
        int[] expected = {2, 0};
        for (int i = 0; i < nodes.length; i++) {
            String input = Arrays.deepToString(edges[i]);
            int output = solver.maxNumEdgesToRemove(nodes[i], edges[i]);
            System.out.printf("n=%d edges=%s -> %d  expected=%d%n", nodes[i], input, output, expected[i]);
        }
    }
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

package graphs;

import java.util.*;

/**
 * Problem: Redundant Connection
 *
 * An undirected graph started as a tree, then one extra edge was added. Return
 * the edge that can be removed to make the graph a tree again; if several edges
 * could work, return the one that appears last in the input.
 *
 * Leetcode: https://leetcode.com/problems/redundant-connection/
 * Rating:   acceptance 67.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Union-Find | Cycle detection
 *
 * Example:
 *   Input:  edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
 *   Output: [1,4]
 *   Why:    adding [1,4] closes the cycle 1-2-3-4-1, while [1,5] remains needed
 *           to keep node 5 connected.
 *
 * Follow-ups:
 *   1. Return all redundant edges if more than one extra edge exists?
 *      Keep collecting every edge whose endpoints are already connected.
 *   2. Handle edge deletions as well as additions?
 *      Use a dynamic connectivity data structure or rebuild Union-Find in batches.
 *   3. Need the full cycle instead of just the edge?
 *      Run DFS from one endpoint before adding the edge and record the path.
 *
 * Related: Redundant Connection II (685), Graph Valid Tree (261), Number of Provinces (547).
 *
 */
public class RedundantConnection {

    public static void main(String[] args) {
        RedundantConnection solver = new RedundantConnection();
        int[][][] inputs = {{{1, 2}, {1, 3}, {2, 3}}, {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}};
        int[][] expected = {{2, 3}, {1, 4}};
        for (int i = 0; i < inputs.length; i++) {
            int[] output = solver.findRedundantConnection(inputs[i]);
            System.out.printf("edges=%s -> %s  expected=%s%n", Arrays.deepToString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
        }
    }
    /**
     * Finds the redundant connection in the given edges using Union-Find.
     *
     * @param edges Array of undirected edges
     * @return The redundant edge that can be removed
     */
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        UnionFind uf = new UnionFind(n + 1); // Nodes are 1-based

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            if (!uf.union(u, v)) {
                return edge; // Found the edge that forms a cycle
            }
        }

        return new int[0]; // Shouldn't reach here as per problem statement
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
     * DFS solution to find the redundant connection.
     * This approach is less efficient than Union-Find for this problem.
     */
    public int[] findRedundantConnectionDFS(int[][] edges) {
        // Build adjacency list
        Map<Integer, List<Integer>> graph = new java.util.HashMap<>();

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];

            // Check if there's already a path between u and v
            boolean[] visited = new boolean[edges.length + 1];
            if (hasPath(graph, u, v, visited)) {
                return edge;
            }

            // Add the edge to the graph
            graph.computeIfAbsent(u, k -> new java.util.ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new java.util.ArrayList<>()).add(u);
        }

        return new int[0]; // Shouldn't reach here as per problem statement
    }

    /**
     * Helper method to check if there's a path between u and v using DFS.
     */
    private boolean hasPath(Map<Integer, List<Integer>> graph, int u, int v, boolean[] visited) {
        if (u == v) {
            return true;
        }

        visited[u] = true;

        if (graph.containsKey(u)) {
            for (int neighbor : graph.get(u)) {
                if (!visited[neighbor] && hasPath(graph, neighbor, v, visited)) {
                    return true;
                }
            }
        }

        return false;
    }
}

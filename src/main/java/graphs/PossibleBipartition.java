package graphs;

import java.util.*;

/**
 * We want to split a group of n people (labeled from 1 to n) into two groups of any size.
 * Each person may dislike some other people, and they should not go into the same group.
 *
 * Given the integer n and the array dislikes where dislikes[i] = [ai, bi] indicates that
 * the person labeled ai does not like the person labeled bi, return true if it is possible
 * to split everyone into two groups in this way.
 *
 * Example 1:
 * Input: n = 4, dislikes = [[1,2],[1,3],[2,4]]
 * Output: true
 * Explanation: group1 [1,4] and group2 [2,3]
 *
 * Example 2:
 * Input: n = 3, dislikes = [[1,2],[1,3],[2,3]]
 * Output: false
 *
 * LeetCode: https://leetcode.com/problems/possible-bipartition/
 *
 * Follow-up Questions:
 * 1. How would you modify the solution if we needed to split into k groups instead of 2?
 *    - We would need to use graph coloring with k colors and ensure no two adjacent nodes have the same color.
 * 2. What if the graph is very large (e.g., 10^5 nodes)?
 *    - We would need to use BFS/DFS with an adjacency list for efficiency.
 * 3. How would you find the actual groups if the partition is possible?
 *    - We could track the color assignments during BFS/DFS to reconstruct the groups.
 *
 * Related Problems:
 * - Is Graph Bipartite? (https://leetcode.com/problems/is-graph-bipartite/)
 * - Flower Planting With No Adjacent (https://leetcode.com/problems/flower-planting-with-no-adjacent/)
 * LeetCode Contest Rating: 1795
 **/
public class PossibleBipartition {
    /**
     * Checks if the given graph is bipartite using BFS.
     *
     * @param n Number of nodes
     * @param dislikes Array of dislike pairs
     * @return true if the graph is bipartite, false otherwise
     */
    public boolean possibleBipartition(int n, int[][] dislikes) {
        // Build adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] dislike : dislikes) {
            int u = dislike[0], v = dislike[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        // 0: uncolored, 1: group 1, -1: group 2
        int[] color = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            if (color[i] == 0 && !bfs(adj, color, i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Performs BFS to check if the component starting at node is bipartite.
     */
    private boolean bfs(List<List<Integer>> adj, int[] color, int start) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        color[start] = 1; // Assign to group 1

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : adj.get(u)) {
                if (color[v] == 0) {
                    // Color with opposite color
                    color[v] = -color[u];
                    queue.offer(v);
                } else if (color[v] == color[u]) {
                    // If adjacent nodes have same color, not bipartite
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Union-Find (Disjoint Set) solution for checking bipartition.
     */
    public boolean possibleBipartitionUnionFind(int n, int[][] dislikes) {
        // Each person can be in group A or group B
        // We'll use Union-Find to group people who must be in the same group
        // and check for conflicts

        // For each person i, we'll have two nodes: i (in group A) and i+n (in group B)
        UnionFind uf = new UnionFind(2 * n + 1);

        for (int[] dislike : dislikes) {
            int u = dislike[0], v = dislike[1];

            // If u is in A, v must be in B
            // If u is in B, v must be in A
            uf.union(u, v + n);
            uf.union(u + n, v);

            // Check for conflict
            if (uf.find(u) == uf.find(u + n) || uf.find(v) == uf.find(v + n)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Optimized Union-Find data structure with path compression and union by rank.
     */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];

            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return; // Already in the same set
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
    }

    /**
     * DFS solution for checking bipartition.
     */
    public boolean possibleBipartitionDFS(int n, int[][] dislikes) {
        // Build adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] dislike : dislikes) {
            int u = dislike[0], v = dislike[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        // 0: uncolored, 1: group 1, -1: group 2
        int[] color = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            if (color[i] == 0 && !dfs(adj, color, i, 1)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Performs DFS to check if the component starting at node is bipartite.
     */
    private boolean dfs(List<List<Integer>> adj, int[] color, int u, int currentColor) {
        color[u] = currentColor;

        for (int v : adj.get(u)) {
            if (color[v] == currentColor) {
                return false; // Conflict found
            }
            if (color[v] == 0 && !dfs(adj, color, v, -currentColor)) {
                return false;
            }
        }

        return true;
    }
}

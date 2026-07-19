package graphs;

import java.util.*;

/**
 * Problem: Possible Bipartition
 *
 * Split people labeled 1 through n into two groups so no dislike pair lands in
 * the same group. Return whether such a split is possible for all connected
 * dislike components.
 *
 * Leetcode: https://leetcode.com/problems/possible-bipartition/
 * Rating:   1795 (zerotrac Elo)
 * Pattern:  Graph | Bipartite coloring | BFS and Union-Find
 *
 * Example:
 *   Input:  n = 3, dislikes = [[1,2],[1,3],[2,3]]
 *   Output: false
 *   Why:    person 1 forces 2 and 3 into the opposite group, but 2 and 3 also
 *           dislike each other, so they cannot share that group.
 *
 * Follow-ups:
 *   1. Return the two actual groups?
 *      Keep the color array and collect people by color after all components pass.
 *   2. Split into k groups instead of two?
 *      That becomes general graph coloring, which is much harder than bipartite checking.
 *   3. Process dislikes online?
 *      Maintain parity-aware Union-Find and reject the first contradiction.
 *
 * Related: Is Graph Bipartite? (785), Flower Planting With No Adjacent (1042).
 *
 */
public class PossibleBipartition {

    public static void main(String[] args) {
        PossibleBipartition solver = new PossibleBipartition();
        int[] people = {4, 3};
        int[][][] dislikes = {{{1, 2}, {1, 3}, {2, 4}}, {{1, 2}, {1, 3}, {2, 3}}};
        boolean[] expected = {true, false};
        for (int i = 0; i < people.length; i++) {
            boolean output = solver.possibleBipartition(people[i], dislikes[i]);
            System.out.printf("n=%d dislikes=%s -> %b  expected=%b%n", people[i], Arrays.deepToString(dislikes[i]), output, expected[i]);
        }
    }
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

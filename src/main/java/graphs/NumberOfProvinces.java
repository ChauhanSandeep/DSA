package graphs;

import java.util.*;

/**
 * Problem: Number of Provinces
 *
 * Given an n by n connection matrix for cities, count how many disconnected
 * groups of cities exist. If city A reaches city B through any chain of direct
 * connections, both cities belong to the same province.
 *
 * Leetcode: https://leetcode.com/problems/number-of-provinces/
 * Rating:   acceptance 70.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Connected components | DFS and Union-Find
 *
 * Example:
 *   Input:  isConnected = [[1,1,0],[1,1,0],[0,0,1]]
 *   Output: 2
 *   Why:    cities 0 and 1 reach each other, while city 2 is alone, so there are
 *           exactly two connected groups.
 *
 * Follow-ups:
 *   1. The matrix is too large but the graph is sparse?
 *      Store only existing edges in an adjacency list and traverse that list.
 *   2. Connections arrive as a stream?
 *      Use Union-Find and report the current component count after each union.
 *   3. Need the largest province size as well?
 *      Track component sizes in Union-Find or count nodes during each traversal.
 *
 * Related: Number of Islands (200), Friend Circles (547), Graph Valid Tree (261).
 *
 */
public class NumberOfProvinces {

    public static void main(String[] args) {
        NumberOfProvinces solver = new NumberOfProvinces();
        int[][][] inputs = {{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}, {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}};
        int[] expected = {2, 3};
        for (int i = 0; i < inputs.length; i++) {
            int output = solver.findCircleNum(inputs[i]);
            System.out.printf("isConnected=%s -> %d  expected=%d%n", Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Finds the number of provinces using DFS.
     *
     * @param isConnected Adjacency matrix representing connections between cities
     * @return Number of provinces
     */
    public int findCircleNum(int[][] isConnected) {
        if (isConnected == null || isConnected.length == 0) {
            return 0;
        }

        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(isConnected, visited, i);
                count++;
            }
        }

        return count;
    }

    /**
     * Performs DFS to mark all connected cities as visited.
     */
    private void dfs(int[][] isConnected, boolean[] visited, int i) {
        for (int j = 0; j < isConnected.length; j++) {
            if (isConnected[i][j] == 1 && !visited[j]) {
                visited[j] = true;
                dfs(isConnected, visited, j);
            }
        }
    }

    /**
     * Union-Find (Disjoint Set) solution for finding the number of provinces.
     * More efficient for large graphs.
     */
    public int findCircleNumUnionFind(int[][] isConnected) {
        if (isConnected == null || isConnected.length == 0) {
            return 0;
        }

        int n = isConnected.length;
        UnionFind uf = new UnionFind(n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    uf.union(i, j);
                }
            }
        }

        return uf.getCount();
    }

    /**
     * Union-Find data structure with path compression and union by rank.
     */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;
        private int count;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;

            // Initially, each element is its own parent
            for (int i = 0; i < n; i++) {
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

            count--; // Decrease the number of components
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * BFS solution for finding the number of provinces.
     */
    public int findCircleNumBFS(int[][] isConnected) {
        if (isConnected == null || isConnected.length == 0) {
            return 0;
        }

        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int count = 0;
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                queue.add(i);
                visited[i] = true;

                while (!queue.isEmpty()) {
                    int current = queue.poll();

                    for (int j = 0; j < n; j++) {
                        if (isConnected[current][j] == 1 && !visited[j]) {
                            visited[j] = true;
                            queue.add(j);
                        }
                    }
                }

                count++;
            }
        }

        return count;
    }
}

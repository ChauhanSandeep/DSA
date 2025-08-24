package graph;

import java.util.*;

/**
 * There are n cities. Some of them are connected, while some are not. If city a is connected directly with city b,
 * and city b is connected directly with city c, then city a is connected indirectly with city c.
 *
 * A province is a group of directly or indirectly connected cities and no other cities outside the group.
 *
 * You are given an n x n matrix isConnected where isConnected[i][j] = 1 if the ith city and the jth city are directly
 * connected, and isConnected[i][j] = 0 otherwise.
 *
 * Return the total number of provinces.
 *
 * Example 1:
 * Input: isConnected = [[1,1,0],[1,1,0],[0,0,1]]
 * Output: 2
 *
 * Example 2:
 * Input: isConnected = [[1,0,0],[0,1,0],[0,0,1]]
 * Output: 3
 *
 * LeetCode: https://leetcode.com/problems/number-of-provinces/
 *
 * Follow-up Questions:
 * 1. How would you handle very large graphs (e.g., n = 1000)?
 *    - The union-find solution is efficient with O(n²) time complexity, but for very large n,
 *      we might need to optimize further or use a more space-efficient representation.
 * 2. What if the graph is sparse (few connections)?
 *    - For sparse graphs, an adjacency list representation would be more space-efficient.
 * 3. How would you find the largest province?
 *    - We could modify the solution to track the size of each connected component.
 *
 * Related Problems:
 * - Number of Islands (https://leetcode.com/problems/number-of-islands/)
 * - Friend Circles (same as this problem)
 */
public class NumberOfProvinces {
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

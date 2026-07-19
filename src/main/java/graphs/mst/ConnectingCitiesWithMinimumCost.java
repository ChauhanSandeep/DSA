package graphs.mst;

import java.util.*;

/**
 * Problem: Connecting Cities With Minimum Cost
 *
 * Given cities labeled 1 through n and weighted undirected connections, return
 * the minimum total cost to connect all cities. Return -1 if the available
 * connections cannot form one connected component.
 *
 * Leetcode: https://leetcode.com/problems/connecting-cities-with-minimum-cost/ (Medium)
 * Rating:   1753 (zerotrac Elo)
 * Pattern:  Graph | Minimum spanning tree | Kruskal with Union-Find
 *
 * Example:
 *   Input:  n = 3, connections = [[1,2,5],[1,3,6],[2,3,1]]
 *   Output: 6
 *   Why:    choosing edges 2-3 and 1-2 connects all cities for 1 + 5.
 *
 * Follow-ups:
 *   1. Need the exact edges in the minimum network?
 *      Store each connection whose union succeeds.
 *   2. Graph is very dense?
 *      Prim's algorithm with adjacency lists or a matrix may be preferable.
 *   3. Connections stream in over time?
 *      Maintain DSU incrementally, but recomputing the MST is needed if cheaper cycle edges arrive late.
 *
 * Related: Min Cost to Connect All Points (1584), Kruskal's algorithm.
 */
public class ConnectingCitiesWithMinimumCost {

    public static void main(String[] args) {
        ConnectingCitiesWithMinimumCost solver = new ConnectingCitiesWithMinimumCost();
        int[][] connections1 = {{1, 2, 5}, {1, 3, 6}, {2, 3, 1}};
        int[][] connections2 = {{1, 2, 3}};

        System.out.printf("n=3 connections=%s -> %d  expected=6%n",
            Arrays.deepToString(connections1), solver.minimumCost(3, connections1));
        System.out.printf("n=3 connections=%s -> %d  expected=-1%n",
            Arrays.deepToString(connections2), solver.minimumCost(3, connections2));
    }

        /**
     * Intuition: the cheapest network that connects all cities is a minimum
     * spanning tree. Kruskal's algorithm considers connections from cheapest to
     * most expensive and accepts only edges that join different components.
     *
     * Algorithm:
     *   1. Sort connections by cost in ascending order.
     *   2. Initialize Union-Find over the 1-based city labels.
     *   3. For each connection, union its endpoints; accepted edges add to totalCost.
     *   4. Return totalCost once citiesCount - 1 edges are accepted, otherwise -1.
     *
     * Time:  O(E log E) - sorting connections dominates Union-Find operations.
     * Space: O(N) - Union-Find parent array.
     *
     * @param citiesCount number of cities labeled 1 through citiesCount
     * @param connections undirected weighted edges [city1, city2, cost]
     * @return minimum cost to connect all cities, or -1 when impossible
     */
    public int minimumCost(int citiesCount, int[][] connections) {
        // Sort edges by cost (ascending)
        Arrays.sort(connections, (e1, e2) -> e1[2] - e2[2]);

        UnionFind uf = new UnionFind(citiesCount + 1); // 1-based index
        int totalCost = 0;
        int edgesUsed = 0;

        for (int[] conn : connections) {
            int city1 = conn[0];
            int city2 = conn[1];
            int cost = conn[2];

            // If cities are not already connected, connect them
            if (uf.union(city1, city2)) {
                totalCost += cost;
                edgesUsed++;
            }

            // If we’ve connected all cities with citiesCount - 1 edges, we’re done
            if (edgesUsed == citiesCount - 1) {
                return totalCost;
            }
        }

        // If not all cities are connected, return -1
        return -1;
    }

    /**
     * Simple Union-Find (Disjoint Set Union) with Path Compression
     */
    private static class UnionFind {
        // This denotes the parent of each node, where parent[i] = i means node i is its own root
        int[] parent;

        UnionFind(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        /**
         * Find the root of the node with path compression
         * @param node
         * @return
         */
        int find(int node) {
            if (parent[node] != node) { // The node is not its own root
                // Recursively find the root and compress the path
                parent[node] = find(parent[node]); // Path compression stores the root directly in parent[node]
            }
            return parent[node];
        }

        /**
         * Join two nodes if they are not already connected, return true if they were connected
         * and false if they were already connected
         * @param nodeA
         * @param nodeB
         * @return
         */
        boolean union(int nodeA, int nodeB) {
            int rootA = find(nodeA);
            int rootB = find(nodeB);
            if (rootA == rootB) { // Both the nodes are already connected
                return false; // Already connected
            }
            // This naively merges the two sets,
            // but we can optimize it with union by rank like in class NetworkConnectivity_KruskalStyle
            parent[rootA] = rootB; // Merge sets
            return true;
        }
    }
}
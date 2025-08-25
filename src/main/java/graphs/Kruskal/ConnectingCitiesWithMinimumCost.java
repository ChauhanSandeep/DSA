package graph.Kruskal;

import java.util.*;

/**
 * There are `n` cities labeled from 1 to n. You are given an array `connections` where
 * connections[i] = [city1, city2, cost] represents the cost to connect city1 and city2.
 *
 * Return the minimum total cost to connect all cities such that every pair of cities is connected.
 * If it's not possible to connect all cities, return -1.
 *
 * Example:
 * Input: n = 3, connections = [[1,2,5],[1,3,6],[2,3,1]]
 * Output: 6
 * Explaination : Connect 1-2 with cost 5 and 2-3 with cost 1.
 *
 * LeetCode Link: https://leetcode.com/problems/connecting-cities-with-minimum-cost/
 */
public class ConnectingCitiesWithMinimumCost {

    /**
     * Uses Kruskal's algorithm to compute the minimum cost to connect all cities.
     *
     * Intuition:
     *  - This is a Minimum Spanning Tree (MST) problem.
     *  - Kruskal's algorithm sorts all edges by cost and greedily picks the smallest edges
     *    that connect two different components (using Union-Find).
     *
     * Steps:
     * 1. Sort all connections based on cost.
     * 2. Use Union-Find to add edges without forming cycles.
     * 3. Stop when all cities are connected (citiesCount - 1 edges used).
     *
     * Time Complexity: O(E log E) where E = number of connections
     * Space Complexity: O(N) for Union-Find data structures
     *
     * @param citiesCount Number of cities
     * @param connections List of connections [city1, city2, cost]
     * @return Minimum cost to connect all cities, or -1 if impossible
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
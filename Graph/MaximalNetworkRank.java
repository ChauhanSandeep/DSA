package Graph;

import java.util.*;

/**
 * LeetCode 1615: Maximal Network Rank
 * Problem Link: https://leetcode.com/problems/maximal-network-rank/
 *
 * The network rank of two different cities is defined as the total number of directly connected roads to either city.
 * If a road is directly connected to both cities, it is counted only once.
 *
 * Approach:
 * 1. Use an adjacency list (HashSet) to store the connections between cities.
 * 2. Maintain an array to track the degree (number of direct roads) for each city.
 * 3. Iterate through all pairs of cities to compute their network rank.
 * 4. If a road exists between two cities, subtract 1 from their combined degree.
 *
 * Time Complexity: O(N^2) → Since we check all pairs of cities.
 * Space Complexity: O(N + E) → For adjacency list and degree array.
 */
public class MaximalNetworkRank {

    public static void main(String[] args) {
        int[][] roads = {
                {0, 1},
                {0, 3},
                {1, 2},
                {1, 3}
        };
        int result = maximalNetworkRank(4, roads);
        System.out.println("Maximal network rank: " + result);
    }

    /**
     * Finds the maximal network rank of the given cities.
     * @param n      - Number of cities
     * @param roads  - List of roads connecting the cities
     * @return       - Maximum network rank possible
     */
    public static int maximalNetworkRank(int n, int[][] roads) {
        if (n == 0 || roads == null) return 0; // Edge case: No cities or no roads

        // Graph representation: Adjacency list using HashSet
        List<Set<Integer>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjacencyList.add(new HashSet<>());
        }

        int[] degree = new int[n]; // Stores count of direct roads for each city

        // Build the graph representation
        for (int[] road : roads) {
            int cityA = road[0], cityB = road[1];
            adjacencyList.get(cityA).add(cityB);
            adjacencyList.get(cityB).add(cityA);
            degree[cityA]++;
            degree[cityB]++;
        }

        int maxRank = 0;

        // Compute maximal network rank by iterating through all pairs
        for (int city1 = 0; city1 < n; city1++) {
            for (int city2 = city1 + 1; city2 < n; city2++) {
                int networkRank = degree[city1] + degree[city2];

                // If cities are directly connected, avoid double counting
                if (adjacencyList.get(city1).contains(city2)) {
                    networkRank--;
                }

                maxRank = Math.max(maxRank, networkRank);
            }
        }

        return maxRank;
    }
}

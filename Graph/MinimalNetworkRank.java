package Graph;

import java.util.HashSet;

/**
 * The network rank of two different cities is defined as the total number of directly connected roads to either city.
 * If a road is directly connected to both cities, it is only counted once.
 *
 * https://leetcode.com/problems/maximal-network-rank/
 */
public class MaximalNetworkRank {

    public static void main(String[] args) {
        int[][] roads = {
                {0, 1},
                {0, 3},
                {1, 2},
                {1, 3}
        };
        int result = new MaximalNetworkRank().maximalNetworkRank(4, roads);
        System.out.println("Maximal network rank: " + result);
    }

    public int maximalNetworkRank(int n, int[][] roads) {
        // Create adjacency list representation using HashSet
        HashSet<Integer>[] graph = new HashSet[n];
        int[] degree = new int[n]; // Stores count of direct roads for each city

        for (int i = 0; i < n; i++) {
            graph[i] = new HashSet<>();
        }

        // Build graph representation
        for (int[] road : roads) {
            int city1 = road[0], city2 = road[1];
            graph[city1].add(city2);
            graph[city2].add(city1);
            degree[city1]++;
            degree[city2]++;
        }

        int maxRank = 0;

        // Compute maximal network rank
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int rank = degree[i] + degree[j];

                // If the two cities are directly connected, subtract 1 (avoid double-counting)
                if (graph[i].contains(j)) rank--;

                maxRank = Math.max(maxRank, rank);
            }
        }

        return maxRank;
    }
}

package graphs;

import java.util.*;


/**
 * Problem: Maximal Network Rank
 * LeetCode: https://leetcode.com/problems/maximal-network-rank/
 *
 * Statement:
 * - The network rank of two different cities is defined as the total number of directly connected roads to either city.
 * - If a road is directly connected to both cities, it is counted only once.
 * - Given n cities and a list of roads, find the maximal network rank among all pairs of cities.
 * - Maximal network rank of two different cities is defined as the total number of directly connected roads to either city.
 *
 * Example:
 * Input: n = 4, roads = [[0,1],[0,3],[1,2],[1,3]]
 * Output: 4
 * Explanation: City 1 and City 3 form the pair with maximal network rank of 4.
 * City 1 is directly connected to cities 0, 2, and 3. City 3 is directly connected to cities 0 and 1.
 *
 * Follow-up Questions (FAANG-style):
 * 1. Can we solve this without checking all O(N^2) pairs?
 *    - Yes, we can optimize by sorting cities by degree and only checking top candidates.
 * 2. What if the number of roads is extremely large (dense graph)?
 *    - Use adjacency matrix instead of adjacency list for O(1) edge existence checks.
 * 3. Can this problem be extended to weighted edges?
 *    - Yes, instead of counting degrees, sum up weights of connected roads.
 */
public class MaximalNetworkRank {

  public static void main(String[] args) {
    int[][] roads = {{0, 1}, {0, 3}, {1, 2}, {1, 3}};
    int result = maximalNetworkRank(4, roads);
    System.out.println("Maximal network rank: " + result);
  }

  /**
   * Computes the maximal network rank by checking all pairs of cities.
   *
   * Steps:
   * 1. Build an adjacency list to represent the graph.
   * 2. Track the degree (number of roads) for each city.
   * 3. Iterate through all pairs of cities:
   *      - Compute combined degree.
   *      - Subtract 1 if the cities are directly connected.
   * 4. Return the maximum rank found.
   *
   * Algorithm: Brute-force check with degree optimization.
   * Time Complexity: O(N^2) for checking all city pairs.
   * Space Complexity: O(N + E) for adjacency list and degree array.
   *
   * @param cityCount      number of cities
   * @param roads  list of roads connecting the cities
   * @return maximal network rank
   */
  public static int maximalNetworkRank(int cityCount, int[][] roads) {
      if (cityCount == 0 || roads == null) {
          return 0;
      }

    // Graph representation: adjacency list using HashSet for O(1) edge existence check
    List<Set<Integer>> adjacencyList = new ArrayList<>();
    for (int i = 0; i < cityCount; i++) {
      adjacencyList.add(new HashSet<>());
    }

    int[] degree = new int[cityCount]; // stores count of direct roads for each city

    // Build adjacency list and degree array
    for (int[] road : roads) {
      int cityA = road[0], cityB = road[1];
      adjacencyList.get(cityA).add(cityB);
      adjacencyList.get(cityB).add(cityA);
      degree[cityA]++;
      degree[cityB]++;
    }

    int maxRank = 0;

    // Check all pairs of cities
    for (int city1 = 0; city1 < cityCount; city1++) {
      for (int city2 = city1 + 1; city2 < cityCount; city2++) {
        int currentRank = degree[city1] + degree[city2];
        if (adjacencyList.get(city1).contains(city2)) {
          currentRank--; // avoid double-counting shared road
        }
        maxRank = Math.max(maxRank, currentRank);
      }
    }

    return maxRank;
  }

  /**
   * Alternative Approach: Using Adjacency Matrix (DP style representation)
   *
   * Idea:
   * - Instead of adjacency list, we use a boolean adjacency matrix (dp[][])
   *   where dp[i][j] = true if a road exists between city i and city j.
   * - Also maintain an array `degree[]` for storing the number of direct roads connected to each city.
   * - For each pair of cities (i, j), compute the combined degree and subtract 1 if they share a direct road.
   *
   * Steps:
   * 1. Initialize adjacency matrix dp[n][n] and degree array.
   * 2. Populate dp and degree array while iterating through roads.
   * 3. Iterate through all city pairs (i, j), compute rank = degree[i] + degree[j].
   *    - If dp[i][j] is true, subtract 1 to avoid double-counting.
   * 4. Track the maximum rank found.
   *
   * Time Complexity: O(N^2) → Checking all city pairs.
   * Space Complexity: O(N^2) → Adjacency matrix storage.
   *
   * Suitable when the graph is dense, since adjacency matrix allows O(1) edge existence check.
   */
  public int maximalNetworkRankUsingDP(int n, int[][] roads) {
      if (n == 0 || roads == null) {
          return 0;
      }

    boolean[][] adjacencyMatrix = new boolean[n][n]; // adjacency representation
    int[] degree = new int[n]; // stores count of direct roads per city

    // Build adjacency matrix and degree array
    for (int[] road : roads) {
      int cityA = road[0], cityB = road[1];
      degree[cityA]++;
      degree[cityB]++;
      adjacencyMatrix[cityA][cityB] = true;
      adjacencyMatrix[cityB][cityA] = true;
    }

    int maxRank = 0;

    // Check all city pairs
    for (int city1 = 0; city1 < n; city1++) {
      for (int city2 = city1 + 1; city2 < n; city2++) {
        int currentRank = degree[city1] + degree[city2];

        // Subtract 1 if direct road exists between them
        if (adjacencyMatrix[city1][city2]) {
          currentRank--;
        }

        maxRank = Math.max(maxRank, currentRank);
      }
    }

    return maxRank;
  }
}

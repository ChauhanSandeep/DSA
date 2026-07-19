package graphs;

import java.util.*;


/**
 * Problem: Maximal Network Rank
 *
 * Given n cities and undirected roads, the network rank of two different cities
 * is the total number of roads touching either city, counting their shared road
 * only once. Return the maximum rank over all city pairs.
 *
 * Leetcode: https://leetcode.com/problems/maximal-network-rank/ (Medium)
 * Rating:   1522 (zerotrac Elo)
 * Pattern:  Graph | Degree counting | Pair enumeration
 *
 * Example:
 *   Input:  n = 4, roads = [[0,1],[0,3],[1,2],[1,3]]
 *   Output: 4
 *   Why:    cities 1 and 3 have degree 3 and 2, but their direct road is shared,
 *           so their rank is 3 + 2 - 1 = 4.
 *
 * Follow-ups:
 *   1. Avoid checking all city pairs for very large n?
 *      Focus on cities with the highest degrees and stop once no lower degree can improve the answer.
 *   2. Handle weighted roads?
 *      Replace degree counts with incident weight sums and subtract the shared edge weight.
 *   3. Return all pairs with maximal rank?
 *      Store each pair whose rank equals the best value while scanning.
 *
 * Related: Find Center of Star Graph (1791), Find the Town Judge (997).
 */
public class MaximalNetworkRank {



    public static void main(String[] args) {
        int[][][] roads = {{{0, 1}, {0, 3}, {1, 2}, {1, 3}}, {}};
        int[] cityCounts = {4, 2};
        int[] expected = {4, 0};
        for (int i = 0; i < roads.length; i++) {
            int output = maximalNetworkRank(cityCounts[i], roads[i]);
            System.out.printf("n=%d roads=%s  ->  %d  expected=%d%n",
                cityCounts[i], Arrays.deepToString(roads[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: the network rank of two cities is their combined degree, except a
     * road directly between them is counted twice and must be subtracted once. After
     * degrees and direct-road lookup are built, every city pair can be scored.
     *
     * Algorithm:
     *   1. Count the degree of every city from the road list.
     *   2. Store whether each pair of cities has a direct road.
     *   3. Try every unordered city pair.
     *   4. Maximize degree[i] + degree[j] minus one when the pair is directly connected.
     *
     * Time:  O(n^2 + r) - roads are read once and all city pairs are checked.
     * Space: O(n^2) - direct connection matrix stores city pairs.
     *
     * @param cityCount number of cities labeled 0 to cityCount - 1
     * @param roads undirected road list
     * @return maximal network rank among all city pairs
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

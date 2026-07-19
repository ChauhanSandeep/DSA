package graphs.floydwarshall;

import java.util.Arrays;


/**
 * Problem: Find the City With the Smallest Number of Neighbors
 *
 * Given an undirected weighted graph and a distance threshold, count for every
 * city how many other cities are reachable within that threshold. Return the city
 * with the smallest count, breaking ties by choosing the greatest city index.
 *
 * Leetcode: https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/ (Medium)
 * Rating:   1855 (zerotrac Elo)
 * Pattern:  Graph | Floyd-Warshall | All-pairs shortest paths
 *
 * Example:
 *   Input:  n = 4, edges = [[0,1,3],[1,2,1],[1,3,4],[2,3,1]], threshold = 4
 *   Output: 3
 *   Why:    city 3 can reach cities 1 and 2 within distance 4, tying the minimum count and winning by larger index.
 *
 * Follow-ups:
 *   1. Graph is sparse and n is large?
 *      Run Dijkstra from each city and stop expanding once distance exceeds the threshold.
 *   2. Need all cities with the minimum count?
 *      Track the minimum count and collect every city matching it instead of only the largest index.
 *   3. Edge weights update frequently?
 *      Recompute from affected sources or use dynamic shortest-path techniques for heavy update workloads.
 *
 * Related: Network Delay Time (743), Design Graph With Shortest Path Calculator (2642).
 */
public class SmallestReachableCity {

    public static void main(String[] args) {
        SmallestReachableCity solver = new SmallestReachableCity();
        int[][] edges1 = {{0, 1, 3}, {1, 2, 1}, {1, 3, 4}, {2, 3, 1}};
        int[][] edges2 = {{0, 1, 2}};

        System.out.printf("n=4 edges=%s threshold=4 -> %d  expected=3%n",
            Arrays.deepToString(edges1), solver.findTheCity(4, edges1, 4));
        System.out.printf("n=2 edges=%s threshold=1 -> %d  expected=1%n",
            Arrays.deepToString(edges2), solver.findTheCity(2, edges2, 1));
    }

        /**
     * Intuition: once the shortest distance between every pair of cities is known,
     * the problem becomes counting how many distances from each city are within
     * the threshold. Floyd-Warshall is a compact all-pairs fit for this matrix.
     *
     * Algorithm:
     *   1. Initialize dist with infinity, zero diagonals, and direct edge weights.
     *   2. Run Floyd-Warshall to improve every from->to path through each via city.
     *   3. Count threshold-reachable neighbors for every city.
     *   4. Keep the city with the smallest count, using <= so ties choose the larger index.
     *
     * Time:  O(n^3) - Floyd-Warshall dominates the counting pass.
     * Space: O(n^2) - all-pairs distance matrix.
     *
     * @param n number of cities labeled 0 to n - 1
     * @param edges bidirectional weighted edges [from, to, weight]
     * @param distanceThreshold maximum distance considered reachable
     * @return city with the fewest reachable neighbors, breaking ties by larger index
     */
    public int findTheCity(int n, int[][] edges, int distanceThreshold) {
        int INF = 10001; // A large value greater than any possible path sum

        // Initialize distance matrix
        int[][] dist = new int[n][n]; // dist[i][j] = distance from city i to city j
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }

        // Populate direct edge weights
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];
            dist[from][to] = weight;
            dist[to][from] = weight;
        }

        // This is Floyd-Warshall algorithm to update shortest paths
        for (int via = 0; via < n; via++) {
            for (int from = 0; from < n; from++) {
                for (int to = 0; to < n; to++) {
                    // If the path from 'from' to 'to' through 'via' is shorter, update it
                    if (dist[from][via] + dist[via][to] < dist[from][to]) {
                        dist[from][to] = dist[from][via] + dist[via][to];
                    }
                }
            }
        }

        // Find the city with the smallest number of reachable cities within the threshold
        int minReachable = n;
        int targetCity = -1;

        for (int city = 0; city < n; city++) {
            int reachableCount = 0;
            for (int other = 0; other < n; other++) {
                if (city != other && dist[city][other] <= distanceThreshold) {
                    reachableCount++;
                }
            }

            // In case of tie, pick the city with the larger index
            if (reachableCount <= minReachable) {
                minReachable = reachableCount;
                targetCity = city;
            }
        }

        return targetCity;
    }
}
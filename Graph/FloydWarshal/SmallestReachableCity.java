package Graph.FloydWarshal;

import java.util.Arrays;


/**
 * Leetcode Problem: 1334. Find the City With the Smallest Number of Neighbors at a Threshold Distance
 *
 * Problem Statement:
 * There are n cities numbered from 0 to n-1. You are given edges where each edge[i] = [from, to, weight]
 * represents a bidirectional and weighted edge between cities. Also given is a distanceThreshold.
 *
 * Your task is to find the city with the smallest number of neighbors at or below the given distance threshold.
 * If there are multiple such cities, return the city with the greatest number.
 *
 * Link: https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
 *
 * Example:
 * Input: n = 4, edges = [[0,1,3],[1,2,1],[1,3,4],[2,3,1]], distanceThreshold = 4
 * Output: 3
 */
public class SmallestReachableCity {

    /**
     * Uses Floyd-Warshall algorithm to compute all-pairs shortest paths.
     *
     * Intuition:
     * - First build a distance matrix with direct edge weights.
     * - Use Floyd-Warshall to compute shortest paths between all city pairs.
     * - For each city, count how many other cities are reachable within the distance threshold.
     * - Return the city with the smallest such count. If tie, prefer the city with higher index.
     *
     * Time Complexity: O(n^3) - due to Floyd-Warshall triple loop.
     * Space Complexity: O(n^2) - for storing the distance matrix.
     *
     * @param n Number of cities
     * @param edges 2D array of bidirectional weighted edges
     * @param distanceThreshold Max allowed distance for "neighbor"
     * @return City index with smallest number of reachable neighbors within threshold
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
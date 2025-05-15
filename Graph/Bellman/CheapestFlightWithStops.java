package Graph.Bellman;

import java.util.*;

/**
 * LeetCode Problem: Cheapest Flights Within K Stops
 * https://leetcode.com/problems/cheapest-flights-within-k-stops/
 *
 * Problem Statement:
 * Given `n` cities, an array `flights` where flights[i] = [from, to, price],
 * find the cheapest price from `src` to `dst` with at most `k` stops.
 * If no route exists, return -1.
 *
 * Approaches:
 * 1. **Dijkstra’s with modifications (Priority Queue - Optimized BFS)**:
 *    - Uses a Min-Heap to always expand the cheapest route first.
 *    - Tracks stops to prevent unnecessary exploration.
 *    - Time Complexity: **O(E log V)**
 *    - Space Complexity: **O(V + E)**
 *
 * 2. **Bellman-Ford Variation (Simple BFS-based DP)**:
 *    - Uses a distance array and iterates up to `k+1` times.
 *    - Time Complexity: **O(K * E)**
 *    - Space Complexity: **O(V)**
 */
public class CheapestFlightWithStops {

    public static void main(String[] args) {
        int[][] flights = {
            {0, 1, 100},
            {1, 2, 100},
            {0, 2, 500}
        };

        CheapestFlightWithStops solver = new CheapestFlightWithStops();
        int cheapestCost = solver.findCheapestPriceDijkstra(3, flights, 0, 2, 1);
        System.out.println("Cheapest cost using Dijkstra's: " + cheapestCost);

        int cheapestCostBFS = solver.findCheapestPriceBellmanFord(3, flights, 0, 2, 1);
        System.out.println("Cheapest cost using BFS: " + cheapestCostBFS);
    }

    /**
     * Approach 1: Modified Dijkstra’s Algorithm using Priority Queue (Min-Heap)
     */
    public int findCheapestPriceDijkstra(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // Step 1: Build the adjacency list {source -> [destination, price]}
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] flight : flights) {
            graph.computeIfAbsent(flight[0], k -> new ArrayList<>()).add(new int[]{flight[1], flight[2]});
        }

        // Step 2: Min-Heap (PriorityQueue) -> {costSoFar, currentCity, remainingStops}
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minHeap.offer(new int[]{0, source, maxStops + 1}); // Cost, city, stops left

        // Step 3: Track minimum stops needed for each city
        Map<Integer, Integer> minStops = new HashMap<>();
        minStops.put(source, maxStops + 1);

        // Step 4: Process the queue
        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int currentCost = current[0], city = current[1], stopsLeft = current[2];

            if (city == destination) return currentCost; // If we reach the destination, return the cost

            if (stopsLeft > 0) { // Process only if we have stops left
                for (int[] neighbor : graph.getOrDefault(city, Collections.emptyList())) {
                    int nextCity = neighbor[0], flightCost = neighbor[1];
                    int newCost = currentCost + flightCost;

                    // Only proceed if we have a better path or more stops left
                    if (!minStops.containsKey(nextCity) || stopsLeft - 1 > minStops.get(nextCity)) {
                        minStops.put(nextCity, stopsLeft - 1);
                        minHeap.offer(new int[]{newCost, nextCity, stopsLeft - 1});
                    }
                }
            }
        }
        return -1; // No valid route found
    }

    /**
     * Finds the cheapest price to travel from a source city to a destination city
     * with at most K stops using a Bellman-Ford style approach.
     *
     * 💡 Intuition:
     * - Each flight is an edge. We perform at most (K+1) "levels" of edge relaxation,
     *   where each level represents one extra stop.
     * - In each level, we try to improve the cost of reaching each city using the
     *   flights available, *without prematurely using updated values in the same level.*
     *   (hence we use a temp array for the update).
     *
     * 🔍 Why it works:
     * - We’re relaxing all edges up to K+1 times, just like Bellman-Ford.
     * - But we do it in a controlled way using copies of the distance array,
     *   which simulates layer-wise propagation (like BFS).
     *
     * ✅ Time Complexity: O(K * E), where:
     *      - K is the max number of stops
     *      - E is the number of flights (edges)
     * ✅ Space Complexity: O(V), where V is the number of cities (vertices)
     *
     * @param totalCities   Total number of cities (vertices)
     * @param flights       List of flights; each flight is [from, to, cost]
     * @param source        Starting city
     * @param destination   Target city
     * @param maxStops      Maximum number of stops allowed (K)
     * @return Minimum cost to reach destination within K stops, or -1 if unreachable
     */
    public int findCheapestPriceBellmanFord(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // minCost[i] will hold the minimum cost to reach city i from source
        // Initialize all costs to infinity, except the source city
        int[] minCost = new int[totalCities];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[source] = 0;

        // Perform up to (maxStops + 1) rounds of edge relaxation. Edge relaxation is the
        // process of updating the cost to reach a city if a cheaper route is found.
        for (int stops = 0; stops <= maxStops; stops++) {
            // Create a copy of the current cost array to prevent premature updates
            int[] updatedCost = Arrays.copyOf(minCost, totalCities);

            for (int[] flight : flights) {
                int from = flight[0];
                int to = flight[1];
                int price = flight[2];

                // Only relax if source city is reachable
                if (minCost[from] != Integer.MAX_VALUE) {
                    updatedCost[to] = Math.min(updatedCost[to], minCost[from] + price);
                }
            }

            // Move to the next level of relaxation
            minCost = updatedCost;
        }

        return minCost[destination] == Integer.MAX_VALUE ? -1 : minCost[destination];
    }
}

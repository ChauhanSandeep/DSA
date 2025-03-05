package Graph;

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

        int cheapestCostBFS = solver.findCheapestPriceBFS(3, flights, 0, 2, 1);
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
     * Approach 2: Bellman-Ford Style BFS (Relax edges up to k+1 times)
     */
    public int findCheapestPriceBFS(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // Distance array initialized to max value
        int[] minCost = new int[totalCities];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[source] = 0;

        // Iterate maxStops + 1 times to relax the edges
        for (int i = 0; i <= maxStops; i++) {
            int[] tempCost = Arrays.copyOf(minCost, totalCities);

            for (int[] flight : flights) {
                int from = flight[0], to = flight[1], price = flight[2];
                if (minCost[from] != Integer.MAX_VALUE) { // Only relax if reachable
                    tempCost[to] = Math.min(tempCost[to], minCost[from] + price);
                }
            }

            minCost = tempCost;
        }

        return minCost[destination] == Integer.MAX_VALUE ? -1 : minCost[destination];
    }
}

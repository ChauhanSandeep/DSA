package graphs.Bellman;

import java.util.*;

/**
 * LeetCode Problem: Cheapest Flights Within K Stops
 * https://leetcode.com/problems/cheapest-flights-within-k-stops/
 *
 * Problem Statement:
 * Given `n` cities and a list of flights in the form [from, to, price],
 * determine the cheapest price from `src` to `dst` with at most `k` stops.
 * Return -1 if such a route does not exist.
 *
 * Example:
 * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
 * Output: 200
 * Explanation: The cheapest route is 0 -> 1 -> 2 with a total cost of 200.
 *
 * Follow-up Questions:
 * - What if we need to return the actual path instead of just the cost?
 * - Can we adapt the solution to handle dynamic changes in the flight network?
 * - What changes are needed if the constraints change to *at most K edges* (vs stops)?
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
     * Approach 1 : Modified Dijkstra's algorithm that tracks the remaining stops.
     *
     * Steps:
     * 1. Build adjacency list of the graph.
     * 2. Use a MinHeap (PriorityQueue) to always expand the lowest-cost route.
     * 3. Keep track of how many stops remain for each path.
     * 4. Skip visiting a city if we’ve already reached it with more stops left.
     *
     * Time Complexity: O(E log V)
     * Space Complexity: O(V + E)
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
     */
    public int findCheapestPriceBellmanFord(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // minCostFromSource[i] will hold the minimum cost to reach city i from source
        // Initialize all costs to infinity, except the source city
        int[] minCostFromSource = new int[totalCities];
        Arrays.fill(minCostFromSource, Integer.MAX_VALUE);
        minCostFromSource[source] = 0;

        // Perform up to (maxStops + 1) rounds of edge relaxation. Edge relaxation is the
        // process of updating the cost to reach a city if a cheaper route is found.
        for (int stops = 0; stops <= maxStops; stops++) {
            // Create a copy of the current cost array to prevent premature updates
            int[] copiedMinCostFromSource = Arrays.copyOf(minCostFromSource, totalCities);

            for (int[] flight : flights) {
                int from = flight[0];
                int to = flight[1];
                int price = flight[2];

                // Only relax if source city is reachable
                if (minCostFromSource[from] != Integer.MAX_VALUE) {
                    copiedMinCostFromSource[to] = Math.min(copiedMinCostFromSource[to], minCostFromSource[from] + price);
                }
            }

            // Move to the next level of relaxation
            minCostFromSource = copiedMinCostFromSource;
        }

        return minCostFromSource[destination] == Integer.MAX_VALUE ? -1 : minCostFromSource[destination];
    }
}

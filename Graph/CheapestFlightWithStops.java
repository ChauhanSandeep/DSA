package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * https://leetcode.com/problems/cheapest-flights-within-k-stops/
 */
import java.util.*;

/**
 * This class provides solutions for the "Cheapest Flights Within K Stops" problem.
 *
 * Problem Description:
 * Given a list of flights represented as [source, destination, cost], find the cheapest price from a source
 * to a destination with up to K stops. If no such route exists, return -1.
 *
 * Implemented Algorithms:
 * 1. BFS-based approach: Explores routes level by level up to K+1 levels.
 *    - Time Complexity: O(V + E) per level, where V is the number of vertices and E is the number of edges.
 *    - Space Complexity: O(V) for storing the queue.
 *
 * 2. Bellman-Ford approach: Iteratively relaxes edges for K+1 iterations.
 *    - Time Complexity: O(K * E), where E is the number of flights.
 *    - Space Complexity: O(V), where V is the number of vertices.
 *
 * 3. Dijkstra's Algorithm variant: Uses a priority queue to always expand the current lowest cost route while accounting for stops.
 *    - Time Complexity: O(E * log(E)), in the worst case.
 *    - Space Complexity: O(V), for the auxiliary data structures.
 *
 * Note: This class assumes that flight indices start at 0 and vertex labels are integers.
 * https://leetcode.com/problems/cheapest-flights-within-k-stops/
 */
public class CheapestFlightWithStops {

    public static void main(String[] args) {
        int[][] flights = {
            {0, 1, 100},
            {1, 2, 100},
            {0, 2, 500}
        };

        CheapestFlightWithStops solver = new CheapestFlightWithStops();
        // Example: Using Dijkstra-based solution with 1 allowed stop
        int cheapestCost = solver.findCheapestPriceDijkstra(3, flights, 0, 2, 1);
        System.out.println("Cheapest cost (Dijkstra): " + cheapestCost);
    }

    /**
     * BFS-based solution to find the cheapest flight price within K stops.
     *
     * @param totalCities Total number of cities.
     * @param flights     2D array of flights where each flight is represented as [source, destination, price].
     * @param source      Starting city.
     * @param destination Destination city.
     * @param maxStops    Maximum number of allowed stops.
     * @return The cheapest price if a route exists, otherwise -1.
     */
    public int findCheapestPriceBFS(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // Build the flight graph: <source, List of [destination, price]>
        Map<Integer, List<int[]>> flightGraph = new HashMap<>();
        for (int[] flight : flights) {
            int from = flight[0], to = flight[1], price = flight[2];
            flightGraph.computeIfAbsent(from, key -> new ArrayList<>()).add(new int[]{to, price});
        }

        // Queue holds arrays of [currentCity, totalCostSoFar]
        Queue<int[]> routeQueue = new LinkedList<>();
        routeQueue.offer(new int[]{source, 0});
        int cheapestPrice = Integer.MAX_VALUE;
        int stops = 0;

        // Process routes level by level, where each level represents one stop.
        while (stops <= maxStops + 1) {
            int levelSize = routeQueue.size();
            for (int i = 0; i < levelSize; i++) {
                int[] currentRoute = routeQueue.poll();
                int currentCity = currentRoute[0];
                int costSoFar = currentRoute[1];

                // If destination is reached, update the cheapest price.
                if (currentCity == destination) {
                    cheapestPrice = Math.min(cheapestPrice, costSoFar);
                    // Continue exploring current level to possibly find a cheaper route.
                    continue;
                }

                // Explore the neighboring flights if available.
                if (flightGraph.containsKey(currentCity)) {
                    for (int[] neighbor : flightGraph.get(currentCity)) {
                        int nextCity = neighbor[0];
                        int flightCost = neighbor[1];
                        if (costSoFar + flightCost < cheapestPrice) {
                            routeQueue.offer(new int[]{nextCity, costSoFar + flightCost});
                        }
                    }
                }
            }
            stops++;
        }
        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    /**
     * Bellman-Ford based solution to find the cheapest flight price within K stops.
     *
     * @param totalCities Total number of cities.
     * @param flights     2D array of flights where each flight is represented as [source, destination, price].
     * @param source      Starting city.
     * @param destination Destination city.
     * @param maxStops    Maximum number of allowed stops.
     * @return The cheapest price if a route exists, otherwise -1.
     */
    public int findCheapestPriceBellmanFord(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        int[] prices = new int[totalCities];
        Arrays.fill(prices, Integer.MAX_VALUE);
        prices[source] = 0;

        // Relax edges up to maxStops+1 times (each iteration represents one level of stops).
        for (int i = 0; i <= maxStops; i++) {
            int[] tempPrices = Arrays.copyOf(prices, totalCities);
            for (int[] flight : flights) {
                int from = flight[0], to = flight[1], flightCost = flight[2];
                if (prices[from] == Integer.MAX_VALUE) continue;
                tempPrices[to] = Math.min(tempPrices[to], prices[from] + flightCost);
            }
            prices = tempPrices;
        }
        return prices[destination] == Integer.MAX_VALUE ? -1 : prices[destination];
    }

    /**
     * Dijkstra's Algorithm variant to find the cheapest flight price within K stops.
     *
     * @param totalCities Total number of cities.
     * @param flights     2D array of flights where each flight is represented as [source, destination, price].
     * @param source      Starting city.
     * @param destination Destination city.
     * @param maxStops    Maximum number of allowed stops.
     * @return The cheapest price if a route exists, otherwise -1.
     */
    public int findCheapestPriceDijkstra(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // Build the flight graph: <source, List of [destination, price]>
        Map<Integer, List<int[]>> flightGraph = new HashMap<>();
        for (int[] flight : flights) {
            int from = flight[0], to = flight[1], cost = flight[2];
            flightGraph.computeIfAbsent(from, key -> new ArrayList<>()).add(new int[]{to, cost});
        }

        // Priority queue orders by total cost so far (min-heap)
        PriorityQueue<RouteNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.costSoFar));
        // Initial node: starting city, cost = 0, allowed stops = maxStops + 1
        minHeap.offer(new RouteNode(source, 0, maxStops + 1));

        while (!minHeap.isEmpty()) {
            RouteNode currentNode = minHeap.poll();
            int currentCity = currentNode.city;
            int currentCost = currentNode.costSoFar;
            int stopsRemaining = currentNode.allowedStops;

            // Destination reached; return current cost.
            if (currentCity == destination) return currentCost;

            // If stops remain, explore the neighboring flights.
            if (stopsRemaining > 0 && flightGraph.containsKey(currentCity)) {
                for (int[] neighbor : flightGraph.get(currentCity)) {
                    int nextCity = neighbor[0];
                    int flightCost = neighbor[1];
                    minHeap.offer(new RouteNode(nextCity, currentCost + flightCost, stopsRemaining - 1));
                }
            }
        }
        return -1;
    }
}

/**
 * Helper class representing a node in the flight route search.
 */
class RouteNode {
    int city;
    int costSoFar;
    int allowedStops;

    public RouteNode(int city, int costSoFar, int allowedStops) {
        this.city = city;
        this.costSoFar = costSoFar;
        this.allowedStops = allowedStops;
    }
}

package Graph;

import java.util.*;

/**
 * LeetCode 815: Bus Routes
 * Problem Link: https://leetcode.com/problems/bus-routes/
 *
 * Problem:
 * - Given a list of bus routes where routes[i] represents the bus stops a bus route i covers.
 * - We need to find the minimum number of buses required to travel from 'source' to 'destination'.
 * - If it's not possible to reach the destination, return -1.
 *
 * Approach:
 * 1. **Map Stops to Routes** → Create a mapping from each stop to the bus routes that pass through it.
 * 2. **Breadth-First Search (BFS)** → Treat the problem as a graph traversal:
 *    - Each bus stop is a node.
 *    - Each bus route is an edge connecting multiple stops.
 *    - Perform BFS starting from the source stop.
 * 3. **Avoid Re-visiting Routes** → Use a `visitedRoutes` set to track already considered routes.
 * 4. **Stop Early If Destination is Found** → Optimize performance.
 *
 * Time Complexity: O(N * M), where N is the number of routes and M is the number of stops per route.
 * Space Complexity: O(N + M), for the mapping, queue, and visited sets.
 */
public class MinBuses {
    public static void main(String[] args) {
        int[][] routes = {
                {7, 12},
                {4, 5, 15},
                {6},
                {15, 19},
                {19, 12, 13}
        };
        int source = 4;
        int destination = 13;
        int minBuses = new MinBuses().numBusesToDestination(routes, source, destination);
        System.out.println("Min buses required to go from source to destination: " + minBuses);
    }

    /**
     * Finds the minimum number of buses required to travel from source to destination.
     *
     * @param routes      Bus routes, where each route[i] contains the bus stops it covers.
     * @param source      Starting bus stop.
     * @param destination Target bus stop.
     * @return Minimum number of buses required to reach destination, or -1 if not possible.
     */
    public int numBusesToDestination(int[][] routes, int source, int destination) {
        if (source == destination) return 0;

        // Step 1: Build Stop-to-Routes Mapping
        Map<Integer, List<Integer>> stopToRoutesMap = new HashMap<>();
        for (int routeIndex = 0; routeIndex < routes.length; routeIndex++) {
            for (int stop : routes[routeIndex]) {
                stopToRoutesMap.computeIfAbsent(stop, k -> new ArrayList<>()).add(routeIndex);
            }
        }

        // Step 2: BFS Initialization
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visitedRoutes = new HashSet<>();
        queue.offer(source);
        int busesTaken = 0;

        // Step 3: BFS Traversal
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            busesTaken++;

            for (int i = 0; i < levelSize; i++) {
                int currentStop = queue.poll();
                List<Integer> availableRoutes = stopToRoutesMap.getOrDefault(currentStop, Collections.emptyList());

                for (int routeIndex : availableRoutes) {
                    if (visitedRoutes.contains(routeIndex)) continue;  // Skip if already processed
                    visitedRoutes.add(routeIndex);

                    for (int nextStop : routes[routeIndex]) {
                        if (nextStop == destination) return busesTaken;
                        queue.offer(nextStop);
                    }
                }
            }
        }
        return -1;  // If no path is found
    }
}

package graphs;

import java.util.*;

/**
 * Problem: Bus Routes
 *
 * Given bus routes, a source stop, and a target stop, return the minimum number
 * of buses needed to travel from source to target. Riding one route lets you get
 * off at any stop on that route.
 *
 * Leetcode: https://leetcode.com/problems/bus-routes/ (Hard)
 * Rating:   1964 (zerotrac Elo)
 * Pattern:  Graph | BFS over stops and routes | Minimum transfers
 *
 * Example:
 *   Input:  routes = [[1,2,7],[3,6,7]], source = 1, target = 6
 *   Output: 2
 *   Why:    take the first bus from stop 1 to stop 7, then transfer to the second
 *           bus and ride to stop 6.
 *
 * Follow-ups:
 *   1. Return the actual bus sequence?
 *      Store parent route/stop information when enqueuing stops.
 *   2. Routes have different fares or travel times?
 *      Use Dijkstra over route/stop states instead of plain BFS.
 *   3. Handle route updates in real time?
 *      Maintain the stop-to-routes index incrementally and invalidate affected searches.
 *
 * Related: Shortest Path Visiting All Nodes (847), Open the Lock (752).
 */
public class MinBuses {


    public static void main(String[] args) {
        MinBuses solver = new MinBuses();
        int[][][] routes = {{{1, 2, 7}, {3, 6, 7}}, {{7}, {8}}};
        int[][] endpoints = {{1, 6}, {7, 8}};
        int[] expected = {2, -1};
        for (int i = 0; i < routes.length; i++) {
            int output = solver.numBusesToDestination(routes[i], endpoints[i][0], endpoints[i][1]);
            System.out.printf("routes=%s source=%d target=%d  ->  %d  expected=%d%n",
                Arrays.deepToString(routes[i]), endpoints[i][0], endpoints[i][1], output, expected[i]);
        }
    }
    /**
     * Intuition: taking a bus route has unit cost, while stops connect all routes
     * that serve them. BFS over routes counts how many buses have been boarded; once
     * a route containing the target stop is reached, that BFS level is the minimum.
     *
     * Algorithm:
     *   1. Map each stop to all route indexes that visit it.
     *   2. Enqueue every route containing the source stop with bus count 1.
     *   3. BFS route by route, visiting connected routes through shared stops.
     *   4. Return the current bus count when a route reaches the target stop.
     *
     * Time:  O(totalStops) - route-stop entries are processed through the BFS mapping.
     * Space: O(totalStops) - stop-to-routes map plus visited route/stop tracking.
     *
     * @param routes routes[i] lists the stops served by bus route i
     * @param source starting stop
     * @param target destination stop
     * @return minimum buses needed, or -1 if unreachable
     */
    public int numBusesToDestination(int[][] routes, int source, int target) {
        // Base case: already at target, no buses needed
        if (source == target) {
            return 0;
        }

        // Step 1: Build a mapping from each stop to all buses that serve it
        // This allows us to quickly find which buses we can board at any stop
        Map<Integer, List<Integer>> stopToBuses = new HashMap<>();

        for (int busIndex = 0; busIndex < routes.length; busIndex++) {
            for (int stop : routes[busIndex]) {
                stopToBuses.computeIfAbsent(stop, k -> new ArrayList<>()).add(busIndex);
            }
        }

        // Step 2: Initialize BFS data structures
        // Queue stores pairs of (currentStop, numberOfBusesTaken)
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{source, 0}); // Start at source with 0 buses taken

        // Track visited stops to avoid processing the same stop multiple times
        Set<Integer> visitedStops = new HashSet<>();
        visitedStops.add(source);

        // Track visited buses to avoid exploring the same bus route multiple times
        // This is crucial for performance - without it, we might explore the same bus
        // from different stops on its route, leading to redundant work
        Set<Integer> visitedBuses = new HashSet<>();

        // Step 3: BFS exploration
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentStop = current[0];
            int busCount = current[1];

            // Get all buses that serve the current stop
            List<Integer> availableBuses = stopToBuses.getOrDefault(currentStop, new ArrayList<>());

            // Try boarding each available bus at this stop
            for (int busIndex : availableBuses) {
                // Skip if we've already explored this bus route
                // This prevents infinite loops and redundant exploration
                if (visitedBuses.contains(busIndex)) {
                    continue;
                }

                // Mark this bus as visited before exploring its route
                visitedBuses.add(busIndex);

                // Explore all stops on this bus route
                for (int nextStop : routes[busIndex]) {
                    // Check if we reached the target
                    if (nextStop == target) {
                        // We took busCount buses to reach currentStop,
                        // and now we're boarding one more bus (busIndex) to reach target
                        return busCount + 1;
                    }

                    // If this stop hasn't been visited, add it to queue for exploration
                    if (!visitedStops.contains(nextStop)) {
                        visitedStops.add(nextStop);
                        // When we board this bus, we increment bus count by 1
                        queue.offer(new int[]{nextStop, busCount + 1});
                    }
                }
            }
        }

        // If we exhausted all possibilities without reaching target, it's impossible
        return -1;
    }

  /**
   * Optimized Approach using Bidirectional BFS.
   * Applicable when stop space is large but routes are fewer and sparsely connected.
   *
   * Steps:
   * 1. Build the stop to routes map.
   * 2. Start BFS from both ends (source & destination), expanding from the side with fewer nodes.
   * 3. If search frontiers meet, sum the number of buses taken from both sides.
   *
   * Time Complexity: O(N * K)
   * Space Complexity: O(N + S)
   *
   * @param routes      Array of bus routes.
   * @param source      Source stop.
   * @param destination Destination stop.
   * @return Minimum number of buses taken, or -1 if impossible.
   */
  public int numBusesToDestinationBidirectionalBFS(int[][] routes, int source, int destination) {
    if (source == destination) return 0;

    Map<Integer, Set<Integer>> stopToRoutes = new HashMap<>();
    for (int i = 0; i < routes.length; i++) {
      for (int stop : routes[i]) {
        stopToRoutes.computeIfAbsent(stop, k -> new HashSet<>()).add(i);
      }
    }

    // Routes available at source and destination
    Set<Integer> sourceRoutes = stopToRoutes.getOrDefault(source, new HashSet<>());
    Set<Integer> destRoutes = stopToRoutes.getOrDefault(destination, new HashSet<>());

    // If there's no connection at either endpoint
    if (sourceRoutes.isEmpty() || destRoutes.isEmpty()) return -1;

    // Queues for BFS from both ends. Each queue holds route indices.
    Queue<Integer> sourceQueue = new LinkedList<>(sourceRoutes);
    Queue<Integer> destQueue = new LinkedList<>(destRoutes);


    Set<Integer> visitedRoutesFromSource = new HashSet<>(sourceRoutes);
    Set<Integer> visitedRoutesFromDest = new HashSet<>(destRoutes);

    int busesTaken = 1;

    while (!sourceQueue.isEmpty() && !destQueue.isEmpty()) {
      // Always expand the smaller queue
      if (sourceQueue.size() > destQueue.size()) {
        Queue<Integer> tmp = sourceQueue;
        sourceQueue = destQueue;
        destQueue = tmp;

        Set<Integer> tmpSet = visitedRoutesFromSource;
        visitedRoutesFromSource = visitedRoutesFromDest;
        visitedRoutesFromDest = tmpSet;
      }

      int size = sourceQueue.size();
      for (int i = 0; i < size; i++) {
        int currentRoute = sourceQueue.poll();
        if (visitedRoutesFromDest.contains(currentRoute)) return busesTaken;

        // Each stop in this route
        for (int stop : routes[currentRoute]) {
          for (int nextRoute : stopToRoutes.get(stop)) {
            if (visitedRoutesFromSource.contains(nextRoute)) continue;
            visitedRoutesFromSource.add(nextRoute);
            sourceQueue.offer(nextRoute);
          }
        }
      }
      busesTaken++;
    }
    return -1;
  }
}

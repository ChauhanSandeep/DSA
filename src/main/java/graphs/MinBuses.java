package graphs;

import java.util.*;

/**
 * LeetCode 815: Bus Routes
 * Problem Link: https://leetcode.com/problems/bus-routes/
 *
 * Problem Statement:
 * Given a list of bus routes where routes[i] represents a bus route covering a sequence of stops,
 * determine the minimum number of buses required to travel from a source stop to a destination stop.
 * If it's impossible to reach the destination, return -1.
 *
 * Example:
 *   Input: routes = [
     *   [7,12], // route is 7 -> 12 -> 7 -> 12
     *   [4,5,15], // route is 4 -> 5 -> 15 -> 4 -> 5 -> 15
     *   [6], // route is 6 -> 6
     *   [15,19],
     *   [19,12,13]
 *   ], source = 4, destination = 13
 *   Output: 3
 *   Explanation:
 *   4 ->[Bus 1]-> 15 ->[Bus 3]-> 19 ->[Bus 4]-> 13.
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we need to find all possible paths with minimum transfers?
 *    Answer: Modify BFS to store all paths at the minimum level before returning.
 * 2. How would you handle dynamic bus routes that change over time?
 *    Answer: Implement event-driven updates to the stop-to-routes mapping and cache invalidation.
 * 3. What if bus routes have different costs or travel times?
 *    Answer: Use Dijkstra's algorithm with priority queue instead of BFS.
 * 4. How to optimize for memory when dealing with very large route networks?
 *    Answer: Use compressed representations, lazy loading, or external storage for route mappings.
 *
 * Related Problems:
 * - LeetCode 1311: Get Watched Videos by Your Friends: https://leetcode.com/problems/get-watched-videos-by-your-friends/
 * - LeetCode 1129: Shortest Path with Alternating Colors: https://leetcode.com/problems/shortest-path-with-alternating-colors/
 * - LeetCode 847: Shortest Path Visiting All Nodes: https://leetcode.com/problems/shortest-path-visiting-all-nodes/
 * LeetCode Contest Rating: 1964
 **/
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
    MinBuses minBusesSolver = new MinBuses();
    int minBuses = minBusesSolver.numBusesToDestination(routes, source, destination);
    System.out.println("Minimum buses required from " + source + " to " + destination + ": " + minBuses);
  }

  /**
     * Finds minimum number of buses needed to travel from source to target using BFS.
     *
     * Algorithm:
     * 1. Base Case Check:
     *    - If source equals target, return 0 (already at destination)
     *
     * 2. Build Stop-to-Buses Mapping:
     *    - Create a map where key is a bus stop and value is list of all buses serving that stop
     *    - This allows us to quickly find which buses we can board at any given stop
     *    - Example: If routes = [[1,2,7],[3,6,7]], then stop 7 -> [bus 0, bus 1]
     *
     * 3. BFS Initialization:
     *    - Start from source stop with 0 buses taken
     *    - Use a queue to store (currentStop, busCount) pairs
     *    - Track visited stops to avoid revisiting same stop
     *    - Track visited buses to avoid re-exploring the same bus route
     *
     * 4. BFS Exploration:
     *    - For each stop, find all buses serving that stop
     *    - For each unvisited bus, board it and explore all stops on that route
     *    - When boarding a new bus, increment bus count by 1
     *    - Mark each newly visited stop and add to queue for further exploration
     *    - If we reach target stop, return the current bus count
     *
     * 5. Return Result:
     *    - If BFS completes without reaching target, return -1 (impossible)
     *
     * Key Insight: We treat this as an unweighted shortest path problem. Each "edge" represents
     * boarding a new bus. BFS guarantees we find the minimum number of buses because it explores
     * level by level, where each level represents taking one additional bus.
     *
     * Time Complexity: O(N + S) where N is total number of stops across all routes (sum of all
     * route lengths) and S is number of unique stops. We visit each bus route at most once and
     * each stop at most once. Building the stop-to-buses map takes O(N) time.
     *
     * Space Complexity: O(N + S) for the stop-to-buses map, visited sets, and BFS queue.
     * In worst case, all stops are unique and need to be stored.
     *
     * @param routes array of bus routes where routes[i] contains all stops for bus i
     * @param source starting bus stop
     * @param target destination bus stop
     * @return minimum number of buses needed, or -1 if impossible
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

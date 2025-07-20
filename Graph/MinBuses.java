package Graph;

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
     *   [6],
     *   [15,19],
     *   [19,12,13]
 *   ], source = 4, destination = 13
 *   Output: 3
 *   Explanation:
 *   4 ->[Bus 1]-> 15 ->[Bus 3]-> 19 ->[Bus 4]-> 13.
 *
 * Follow-Up Questions:
 * - What if you need to return the path/stops traversed?
 *   (Use BFS predecessor map to reconstruct path; see LeetCode Discuss for implementation tips.)
 * - What if buses can be taken multiple times or there are express routes?
 *   (Allow revisiting routes if a more optimal path can be found.)
 *
 * Related Link: https://leetcode.com/problems/bus-routes/
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
    MinBuses minBusesSolver = new MinBuses();
    int minBuses = minBusesSolver.numBusesToDestination(routes, source, destination);
    System.out.println("Minimum buses required from " + source + " to " + destination + ": " + minBuses);
  }

  /**
   * Computes the minimum number of buses needed from the source stop to the destination stop.
   *
   * Steps:
   * 1. Build a map from each bus stop to all the routes that include it.
   * 2. Perform a BFS from the source stop. For each stop, traverse all the bus routes passing through it,
   *    and all their stops. Ensure we never reprocess the same route.
   * 3. Track visited routes and visited stops to reduce redundant processing.
   * 4. Return the level at which we find the destination, representing the minimum buses taken.
   *
   * Algorithm: Breadth-First Search (BFS)
   * - Each bus route is processed once, and each stop is queued as needed.
   * - Time Complexity: O(N * K) where N is the number of routes, K is the average number of stops per route.
   * - Space Complexity: O(N + S), where S is number of unique stops.
   *
   * @param routes      Array of bus routes, each route is an array of bus stops.
   * @param source      Starting stop.
   * @param destination Target stop.
   * @return Minimum number of buses required, or -1 if no possible route.
   */
  public int numBusesToDestination(int[][] routes, int source, int destination) {
    if (source == destination) return 0;

    // Map each stop to a set of bus route indices it appears in.
    Map<Integer, Set<Integer>> stopToRoutesMap = new HashMap<>(); // <stop, set of route indices>
    for (int routeIdx = 0; routeIdx < routes.length; routeIdx++) {
      for (int stop : routes[routeIdx]) {
        stopToRoutesMap.computeIfAbsent(stop, k -> new HashSet<>()).add(routeIdx);
      }
    }

    Queue<Integer> stopsQueue = new LinkedList<>();
    Set<Integer> visitedStops = new HashSet<>();
    Set<Integer> visitedRoutes = new HashSet<>();
    stopsQueue.offer(source);
    visitedStops.add(source);
    int busesTaken = 0;

    // Standard BFS
    while (!stopsQueue.isEmpty()) {
      int currentLevelSize = stopsQueue.size();
      busesTaken++;
      for (int i = 0; i < currentLevelSize; i++) {
        int currentStop = stopsQueue.poll();
        Set<Integer> possibleRoutes = stopToRoutesMap.getOrDefault(currentStop, Collections.emptySet());
        for (int routeIdx : possibleRoutes) {
          if (visitedRoutes.contains(routeIdx)) continue;
          visitedRoutes.add(routeIdx);
          for (int stop : routes[routeIdx]) {
            if (stop == destination) return busesTaken; // Found destination
            if (visitedStops.add(stop)) { // true if stop was not already present
              stopsQueue.offer(stop);
            }
          }
        }
      }
    }
    return -1; // Destination unreachable
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
    Set<Integer> visitedRoutesFromSource = new HashSet<>();
    Set<Integer> visitedRoutesFromDest = new HashSet<>();
    Set<Integer> sourceRoutes = stopToRoutes.getOrDefault(source, new HashSet<>());
    Set<Integer> destRoutes = stopToRoutes.getOrDefault(destination, new HashSet<>());

    // If there's no connection at either endpoint
    if (sourceRoutes.isEmpty() || destRoutes.isEmpty()) return -1;

    Queue<Integer> sourceQueue = new LinkedList<>(sourceRoutes);
    Queue<Integer> destQueue = new LinkedList<>(destRoutes);
    for (int route : sourceRoutes) visitedRoutesFromSource.add(route);
    for (int route : destRoutes) visitedRoutesFromDest.add(route);

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

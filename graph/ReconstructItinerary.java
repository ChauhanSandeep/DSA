package graph;

import java.util.*;

/**
 * **Reconstruct Itinerary - Leetcode 332**
 * Leetcode Link: https://leetcode.com/problems/reconstruct-itinerary/
 *
 * --- Problem Statement ---
 * You are given a list of airline tickets represented as pairs of departure and arrival airports [from, to].
 * Reconstruct the itinerary in such a way that:
 * - The itinerary must begin with "JFK".
 * - If multiple valid itineraries exist, return the one with the smallest lexicographical order.
 * - All tickets must be used exactly once.
 *
 * --- Example ---
 * Input: [["JFK","SFO"]3,["JFK","ATL"]1,["SFO","ATL"]4,["ATL","JFK"]2,["ATL","SFO"]5]
 * Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
 *
 * --- Explanation ---
 * Among all valid itineraries, the one with the lexicographically smallest order is selected.
 * This requires using a priority queue at each node to always pick the next destination in sorted order.
 *
 * --- Approach ---
 * - Model the tickets as a directed graph with adjacency list using a PriorityQueue at each node.
 * - Use **Hierholzer's algorithm** (post-order DFS) to build the Eulerian path.
 * - Since we insert airports into the result after all their edges are visited, the itinerary is built in reverse.
 *
 * --- Time Complexity ---
 * - O(E log E) for sorting edges in the priority queue, where E is number of tickets.
 * --- Space Complexity ---
 * - O(V + E), where V is number of airports and E is number of tickets.
 *
 * --- Follow-up Questions ---
 * 1. What if the itinerary must be the longest instead of lexicographically smallest?
 *    - Use backtracking instead of PriorityQueue to explore all paths.
 * 2. What if tickets are reused?
 *    - Mark visited tickets to avoid revisiting.
 */
public class ReconstructItinerary {

  public static void main(String[] args) {
    List<List<String>> tickets = Arrays.asList(
        Arrays.asList("JFK", "SFO"),
        Arrays.asList("JFK", "ATL"),
        Arrays.asList("SFO", "ATL"),
        Arrays.asList("ATL", "JFK"),
        Arrays.asList("ATL", "SFO")
    );

    ReconstructItinerary itineraryBuilder = new ReconstructItinerary();
    List<String> itinerary = itineraryBuilder.findLexicographicalItinerary(tickets);
    System.out.println(itinerary);
  }

  /**
   * Reconstructs the itinerary using Hierholzer's algorithm.
   *
   * Steps:
   * 1. Build a graph from the tickets with lexicographical ordering using PriorityQueue.
   * 2. Run DFS starting from "JFK", always choosing the smallest destination.
   * 3. Post-order insert airports into the result list to ensure correct itinerary order.
   *
   * Algorithm: Hierholzer’s algorithm for Eulerian path (DFS with post-order insertion).
   *
   * Time Complexity: O(E log E), due to PriorityQueue operations per edge.
   * Space Complexity: O(V + E), for storing the graph and the result.
   *
   * @param tickets List of [from, to] ticket pairs.
   * @return List of airport codes representing the itinerary.
   */
  public List<String> findLexicographicalItinerary(List<List<String>> tickets) {
    Map<String, PriorityQueue<String>> flightGraph = constructGraph(tickets);

    LinkedList<String> itinerary = new LinkedList<>();
    performDFS("JFK", flightGraph, itinerary);

    return itinerary;
  }

  /**
   * Builds a graph from the ticket list using a PriorityQueue to maintain lexicographical order.
   *
   * @param tickets List of [from, to] airport pairs.
   * @return Map representing adjacency list with min-heaps for destinations.
   */
  private Map<String, PriorityQueue<String>> constructGraph(List<List<String>> tickets) {
    Map<String, PriorityQueue<String>> graph = new HashMap<>();

    for (List<String> ticket : tickets) {
      String fromAirport = ticket.get(0);
      String toAirport = ticket.get(1);

      graph.computeIfAbsent(fromAirport, k -> new PriorityQueue<>())
          .add(toAirport);
    }

    return graph;
  }

  /**
   * Performs DFS traversal while removing used tickets (edges).
   * Airports are inserted into the result list in post-order to build the path in reverse.
   *
   * @param currentAirport Current airport node.
   * @param flightGraph Directed graph with destination airports.
   * @param itinerary Output list collecting the reconstructed path.
   */
  private void performDFS(String currentAirport, Map<String, PriorityQueue<String>> flightGraph, LinkedList<String> itinerary) {
    PriorityQueue<String> nextDestinations = flightGraph.get(currentAirport);

    while (nextDestinations != null && !nextDestinations.isEmpty()) {
      String nextAirport = nextDestinations.poll(); // ensure to poll so that we don't revisit this edge
      performDFS(nextAirport, flightGraph, itinerary);
    }

    itinerary.addFirst(currentAirport);
  }
}
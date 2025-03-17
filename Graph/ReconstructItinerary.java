package Graph;

import java.util.*;

/**
 * **Reconstruct Itinerary - Leetcode 332**
 * LeetCode: https://leetcode.com/problems/reconstruct-itinerary/
 *
 * --- Problem Description ---
 * Given a list of airline tickets in the format [from, to], reconstruct the itinerary
 * that starts from "JFK" and follows lexicographically smallest order when multiple routes exist.
 *
 * --- Approach ---
 * - **Graph Representation:** Use an adjacency list with a **min-heap (PriorityQueue)**
 *   to maintain destinations in lexicographical order.
 * - **Hierholzer’s Algorithm:** Used for finding an Eulerian path.
 *   - Perform **DFS traversal** while consuming edges.
 *   - Add nodes to result in **reverse order** to reconstruct the path correctly.
 *
 * --- Complexity Analysis ---
 * - **Time Complexity:** O(E log E), where E is the number of tickets.
 * - **Space Complexity:** O(V + E), where V is the number of airports (graph nodes).
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

        List<String> itinerary = new ReconstructItinerary().findItinerary(tickets);
        System.out.println(itinerary);
    }

    /**
     * Reconstructs the itinerary using Hierholzer's algorithm.
     *
     * @param tickets List of airline tickets represented as [from, to] pairs.
     * @return The itinerary in lexicographical order.
     */
    public List<String> findItinerary(List<List<String>> tickets) {
        // Step 1: Build the graph (Adjacency List with PriorityQueue for lexicographical order)
        Map<String, PriorityQueue<String>> flightMap = buildGraph(tickets);

        // Step 2: Perform Hierholzer's Algorithm (DFS for Eulerian Path)
        LinkedList<String> itinerary = new LinkedList<>();
        dfs("JFK", flightMap, itinerary);
        return itinerary;
    }

    /**
     * Builds a directed graph from the given list of tickets.
     *
     * @param tickets List of airline tickets represented as [from, to] pairs.
     * @return A map where each key (airport) has a PriorityQueue of destination airports.
     */
    private Map<String, PriorityQueue<String>> buildGraph(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        
        for (List<String> ticket : tickets) {
            String from = ticket.get(0);
            String to = ticket.get(1);
            graph.computeIfAbsent(from, k -> new PriorityQueue<>()).add(to);
        }

        return graph;
    }

    /**
     * Performs Depth-First Search (DFS) for Hierholzer's algorithm.
     *
     * @param airport   The current airport being visited.
     * @param flightMap The adjacency list representing available flights.
     * @param itinerary The reconstructed itinerary (result list).
     */
    private void dfs(String airport, Map<String, PriorityQueue<String>> flightMap, LinkedList<String> itinerary) {
        PriorityQueue<String> destinations = flightMap.get(airport);

        // Process all destinations in lexicographical order
        while (destinations != null && !destinations.isEmpty()) {
            dfs(destinations.poll(), flightMap, itinerary);
        }

        // Insert at the front to maintain correct order (post-order DFS)
        itinerary.addFirst(airport);
    }
}

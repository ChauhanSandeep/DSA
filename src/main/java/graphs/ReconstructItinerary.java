package graphs;

import java.util.*;


/**
 * Problem: Reconstruct Itinerary
 *
 * Given airline tickets [from, to], rebuild an itinerary that starts at JFK and
 * uses every ticket exactly once. If more than one valid itinerary exists, return
 * the lexicographically smallest airport sequence.
 *
 * Leetcode: https://leetcode.com/problems/reconstruct-itinerary/
 * Rating:   acceptance 44.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Eulerian path | Hierholzer DFS with lexical ordering
 *
 * Example:
 *   Input:  tickets = [[JFK,SFO],[JFK,ATL],[SFO,ATL],[ATL,JFK],[ATL,SFO]]
 *   Output: [JFK, ATL, JFK, SFO, ATL, SFO]
 *   Why:    both JFK choices can lead to valid trips, so the path taking ATL first
 *           wins because it is lexicographically smaller.
 *
 * Follow-ups:
 *   1. Validate that an itinerary exists before building it?
 *      Check directed Euler path degree conditions and that all ticket endpoints are connected.
 *   2. Return all valid itineraries in lexical order?
 *      Use backtracking with ticket counts, but it can be exponential.
 *   3. Start from an arbitrary airport?
 *      Pass the start airport into the same Hierholzer traversal.
 *
 * Related: Course Schedule II (210), Alien Dictionary (269), Network Delay Time (743).
 *
 */
public class ReconstructItinerary {

  public static void main(String[] args) {
    ReconstructItinerary solver = new ReconstructItinerary();
    List<List<List<String>>> cases = Arrays.asList(
        Arrays.asList(Arrays.asList("MUC", "LHR"), Arrays.asList("JFK", "MUC"), Arrays.asList("SFO", "SJC"), Arrays.asList("LHR", "SFO")),
        Arrays.asList(Arrays.asList("JFK", "SFO"), Arrays.asList("JFK", "ATL"), Arrays.asList("SFO", "ATL"), Arrays.asList("ATL", "JFK"), Arrays.asList("ATL", "SFO")));
    List<List<String>> expected = Arrays.asList(Arrays.asList("JFK", "MUC", "LHR", "SFO", "SJC"), Arrays.asList("JFK", "ATL", "JFK", "SFO", "ATL", "SFO"));
    for (int i = 0; i < cases.size(); i++) {
      List<String> output = solver.findItinerary(cases.get(i));
      System.out.printf("tickets=%s -> %s  expected=%s%n", cases.get(i), output, expected.get(i));
    }
  }


  /**
     * Main method: Reconstructs itinerary using Hierholzer's Algorithm (Optimal).
     * Step-by-step:
     *  1. Build adjacency list graph where each airport maps to sorted list of destinations
     *  2. Use PriorityQueue (min-heap) for each airport to ensure lexicographically smallest choice
     *  3. Perform DFS starting from "JFK":
     *     a. Visit airports greedily (always pick smallest lexical destination)
     *     b. Remove used tickets as we traverse (mark edges as visited)
     *     c. Use post-order traversal: add airport to result after visiting all its destinations
     *  4. Reverse the result since we build it backwards (post-order)
     *  5. Return the itinerary
     *
     * Key Insight:
     * This is an Eulerian path problem - find a path that visits every edge exactly once.
     * Hierholzer's algorithm efficiently finds such paths. By using post-order DFS:
     * - Dead-end airports (no outgoing flights) are added first
     * - Airports leading to dead-ends are added next
     * - Starting airport is added last
     * Reversing gives us the correct forward path.
     *
     * Algorithm: Hierholzer's Algorithm with DFS and Priority Queue.
     * Time Complexity: O(E log E), where E is number of tickets/edges.
     *                  - Building graph with sorting: O(E log E)
     *                  - DFS traversal: O(E), each edge visited once
     * Space Complexity: O(E) for adjacency list and result array.
     */
    public List<String> findItinerary(List<List<String>> tickets) {
        // Build adjacency list with priority queues for lexical ordering
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        
        for (List<String> ticket : tickets) {
            String from = ticket.get(0);
            String to = ticket.get(1);
            
            graph.putIfAbsent(from, new PriorityQueue<>());
            graph.get(from).offer(to);
        }
        
        List<String> itinerary = new ArrayList<>();
        
        // Start DFS from JFK
        dfs("JFK", graph, itinerary);

        // Validation, if it's not guaranteed that all tickets form a valid itinerary
        if (itinerary.size() != tickets.size() + 1) {
            return new ArrayList<>(); // Invalid: couldn't use all tickets
        }
        
        // Reverse since we built path in post-order
        Collections.reverse(itinerary);
        
        return itinerary;
    }

    /**
     * Helper: Performs DFS using Hierholzer's algorithm to find Eulerian path.
     */
    private void dfs(String currentAirport, Map<String, PriorityQueue<String>> graph, List<String> itinerary) {
        PriorityQueue<String> destinations = graph.get(currentAirport);
        
        // Visit all destinations from current airport
        while (destinations != null && !destinations.isEmpty()) {
            // Poll (remove) the smallest lexical destination
            String nextAirport = destinations.poll();
            // Recursively visit next airport
            dfs(nextAirport, graph, itinerary);
        }
        
        // Add current airport after visiting all its destinations (post-order)
        itinerary.add(currentAirport);
    }

    /**
     * Alternative method: Using stack-based iterative approach (avoids recursion).
     * Step-by-step:
     *  1. Build graph with priority queues as above
     *  2. Use explicit stack instead of recursion call stack
     *  3. Process airports iteratively, simulating DFS behavior
     *  4. Build result in post-order and reverse at end
     *
     * Algorithm: Iterative Hierholzer's Algorithm with explicit stack.
     * Time Complexity: O(E log E).
     * Space Complexity: O(E) for graph, stack, and result.
     */
    public List<String> findItineraryIterative(List<List<String>> tickets) {
        // Build graph
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        
        for (List<String> ticket : tickets) {
            String from = ticket.get(0);
            String to = ticket.get(1);
            graph.putIfAbsent(from, new PriorityQueue<>());
            graph.get(from).offer(to);
        }
        
        List<String> itinerary = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push("JFK");
        
        while (!stack.isEmpty()) {
            String currentAirport = stack.peek();
            
            if (graph.containsKey(currentAirport) && !graph.get(currentAirport).isEmpty()) {
                // Has unvisited destinations, go deeper
                stack.push(graph.get(currentAirport).poll());
            } else {
                // No more destinations, add to result (post-order)
                itinerary.add(stack.pop());
            }
        }
        
        // Reverse to get correct order
        Collections.reverse(itinerary);
        
        return itinerary;
    }
}
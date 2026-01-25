package graphs;

import java.util.*;


/**
 * Problem Statement:
 * You are given a list of airline tickets where tickets[i] = [from_i, to_i] represent the departure 
 * and arrival airports of one flight. Reconstruct the itinerary in order and return it.
 *
 * All of the tickets belong to a man who departs from "JFK", thus, the itinerary must begin with "JFK".
 * If there are multiple valid itineraries, you should return the itinerary that has the smallest 
 * lexical order when read as a single string.
 *
 * For example, the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].
 * You may assume all tickets form at least one valid itinerary. You must use all the tickets once and only once.
 *
 * Example 1:
 * Input: tickets = [
 *    ["MUC","LHR"],
 *    ["JFK","MUC"],
 *    ["SFO","SJC"],
 *    ["LHR","SFO"]]
 * Output: ["JFK","MUC","LHR","SFO","SJC"]
 * Explanation:
 * Start at JFK → MUC → LHR → SFO → SJC (uses all 4 tickets)
 *
 * LeetCode link: https://leetcode.com/problems/reconstruct-itinerary/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - What if there's no valid itinerary (tickets don't form a connected path)?
 *    → Problem guarantees valid itinerary exists, but could add validation to check connectivity.
 *  - How would you handle returning all possible valid itineraries?
 *    → Use backtracking to generate all paths, then sort and return all valid ones.
 *  - What if we don't start from "JFK" but from any arbitrary airport?
 *    → Modify algorithm to accept starting airport as parameter; Hierholzer's algorithm still works.
 *  - Can you determine if an Eulerian path exists before attempting to find it?
 *    → Yes, check if at most 2 vertices have odd degree (for undirected) or specific in/out-degree conditions (for directed).
 * LeetCode Contest Rating: Not available (not a contest problem)
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
    List<String> itinerary = itineraryBuilder.findItinerary(tickets);
    System.out.println(itinerary);
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
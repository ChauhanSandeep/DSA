package Graph;
import java.util.*;

/**
 * Reconstruct Itinerary - Leetcode 332
 * Uses Hierholzer's algorithm for Eulerian path.
 * https://leetcode.com/problems/reconstruct-itinerary/
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

        List<String> result = new ReconstructItinerary().findItinerary(tickets);
        System.out.println(result);
    }

    public List<String> findItinerary(List<List<String>> tickets) {
        // Step 1: Build the adjacency list (graph)
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for (List<String> ticket : tickets) {
            graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).add(ticket.get(1));
        }

        // Step 2: Perform Hierholzer's Algorithm (DFS-based Eulerian Path)
        LinkedList<String> result = new LinkedList<>();
        dfs("JFK", graph, result);
        return result;
    }

    private void dfs(String source, Map<String, PriorityQueue<String>> graph, LinkedList<String> result) {
        PriorityQueue<String> destinations = graph.get(source);
        
        // Process all destinations in lexicographical order
        while (destinations != null && !destinations.isEmpty()) {
            dfs(destinations.poll(), graph, result);
        }

        // Add to result in reverse order (topological sort)
        result.addFirst(source);
    }
}

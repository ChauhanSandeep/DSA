package Graph;

import java.util.*;

/**
 * Problem: Shortest Path with Useful Extra Edges
 *
 * Statement:
 * Given a weighted directed graph with `n` vertices, find the shortest path from `source` to `destination`.
 * Additionally, you are given a list of bidirectional "extra edges" that can be optionally added.
 * Using these edges may reduce the shortest path.
 *
 * Example:
 * Input:
 * n = 3, edges = [[1, 2, 1], [2, 3, 2]], extraEdges = [[1, 3, 2]]
 * source = 1, destination = 3
 * Output: 2
 * Explanation: Direct extra edge 1 → 3 with weight 2 gives shortest path.
 *
 * InterviewBit Link:  https://www.interviewbit.com/problems/useful-extra-edges
 *
 * Follow-up Questions (FAANG-style):
 * 1. How would you handle negative weights?
 *    - Use Bellman-Ford or Johnson’s algorithm instead of Dijkstra.
 * 2. What if there are multiple extra edges and you can use more than one?
 *    - This problem would reduce to standard shortest path with all edges included.
 * 3. Can you solve it with A* search if we need paths repeatedly?
 *    - Yes, A* is more efficient when heuristic is available (like Euclidean distance in grid graphs).
 */
public class UsefulExtraEdges {

    /**
     * Helper class to represent edges in adjacency list.
     */
    static class Edge {
        int target, weight;

        Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    /**
     * Finds the shortest path between source and destination considering extra bidirectional edges.
     *
     * Optimized Approach: Avoids re-running Dijkstra for each extra edge.
     *
     * Approach:
     * 1. Build adjacency list from given directed edges.
     * 2. Run Dijkstra’s algorithm from `source` to all nodes → get distFromSource[].
     * 3. Run Dijkstra’s algorithm from `destination` on reversed graph → get distToDestination[].
     * 4. For each extra bidirectional edge (u, v, w):
     *      - Candidate path = distFromSource[u] + w + distToDestination[v]
     *      - Candidate path = distFromSource[v] + w + distToDestination[u]
     * 5. Take the minimum among base shortest path and candidate paths.
     *
     * Reason for doing bidirectional Dijkstra:
     * - If we simply had to find the shortest path from `source` to `destination` then we can use Dijkstra.
     * - But if we need to find the shortest path from `source` to `destination` using only one extra edge from list of edges,
     * then we have to run two Dijkstra runs: one from `source` to all nodes, and another from `destination` to all nodes.
     * because we need to consider both forward and backward paths.
     *
     * Time Complexity:
     * - Two Dijkstra runs: O((V + E) log V)
     * - Checking extra edges: O(E)
     * - Total: O((V + E) log V)
     *
     * Space Complexity:
     * - O(V + E) for adjacency lists and distances.
     *
     * @param vertices    Number of vertices in the graph.
     * @param edges       List of directed edges [from, to, weight].
     * @param source      Source vertex.
     * @param destination Destination vertex.
     * @param extraEdges  List of bidirectional extra edges [u, v, weight].
     * @return            Minimum shortest path distance, or -1 if unreachable.
     */
    public int findShortestPath(int vertices, List<List<Integer>> edges, int source, int destination,
        List<List<Integer>> extraEdges) {
        // Step 1: Build adjacency list
        List<List<Edge>> graph = new ArrayList<>(); // index is node and value is list of neighbors with weight
        List<List<Edge>> reverseGraph = new ArrayList<>();
        for (int i = 0; i <= vertices; i++) {
            graph.add(new ArrayList<>());
            reverseGraph.add(new ArrayList<>());
        }

        for (List<Integer> edge : edges) {
            int u = edge.get(0), v = edge.get(1), w = edge.get(2);
            graph.get(u).add(new Edge(v, w));
            reverseGraph.get(v).add(new Edge(u, w)); // for reverse graph
        }

        // Step 2: Run Dijkstra from source to find shortest path to all nodes
        int[] distFromSource = dijkstra(graph, source, vertices);

        // Step 3: Run Dijkstra from destination on reversed graph to find shortest path to all nodes
        int[] distToDestination = dijkstra(reverseGraph, destination, vertices);

        int shortestPath = distFromSource[destination];

        // Step 4: Try using each extra edge
        for (List<Integer> edge : extraEdges) {
            int node1 = edge.get(0), node2 = edge.get(1), weight = edge.get(2);

            if (distFromSource[node1] != Integer.MAX_VALUE && distToDestination[node2] != Integer.MAX_VALUE) {
                shortestPath = Math.min(shortestPath, distFromSource[node1] + weight + distToDestination[node2]);
            }

            if (distFromSource[node2] != Integer.MAX_VALUE && distToDestination[node1] != Integer.MAX_VALUE) {
                shortestPath = Math.min(shortestPath, distFromSource[node2] + weight + distToDestination[node1]);
            }
        }

        return (shortestPath == Integer.MAX_VALUE) ? -1 : shortestPath;
    }

    /**
     * Dijkstra’s algorithm for shortest paths.
     *
     * @param graph    Graph adjacency list.
     * @param source   Start node.
     * @param vertices Number of vertices.
     * @return         Array of shortest distances from source.
     */
    private int[] dijkstra(List<List<Edge>> graph, int source, int vertices) {
        int[] distances = new int[vertices + 1];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        pq.add(new Edge(source, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int node = current.target;
            int currDist = current.weight;

            if (currDist > distances[node]) continue; // skip outdated entries

            for (Edge neighbor : graph.get(node)) {
                int newDist = distances[node] + neighbor.weight;
                if (newDist < distances[neighbor.target]) {
                    distances[neighbor.target] = newDist;
                    pq.add(new Edge(neighbor.target, newDist));
                }
            }
        }
        return distances;
    }

    /**
     * Driver method for testing.
     */
    public static void main(String[] args) {
        List<List<Integer>> inputEdges = Arrays.asList(
            Arrays.asList(1, 2, 1),
            Arrays.asList(2, 3, 2)
        );

        List<List<Integer>> extraEdges = Arrays.asList(
            Arrays.asList(1, 3, 2)
        );

        UsefulExtraEdges solver = new UsefulExtraEdges();
        int result = solver.findShortestPath(3, inputEdges, 1, 3, extraEdges);
        System.out.println("Shortest Path: " + result); // Expected: 2
    }
}

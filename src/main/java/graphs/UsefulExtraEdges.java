package graphs;

import java.util.*;

/**
 * Problem: Useful Extra Edges
 *
 * Given a weighted directed graph and optional bidirectional extra edges, find the
 * shortest path from source to destination when you may use at most one extra
 * edge. Return -1 if the destination is unreachable.
 *
 * Source: InterviewBit: Useful Extra Edges
 * Pattern:  Graph | Dijkstra | Try one optional shortcut edge
 *
 * Example:
 *   Input:  edges = [[1,2,1],[2,3,2]], extraEdges = [[1,3,2]], source = 1, destination = 3
 *   Output: 2
 *   Why:    the optional edge from 1 to 3 costs 2, which beats the normal path
 *           1 -> 2 -> 3 with total cost 3.
 *
 * Follow-ups:
 *   1. Extra edges can be used more than once?
 *      Add all extra edges to the graph and run one normal shortest-path search.
 *   2. Edge weights can be negative?
 *      Use Bellman-Ford or Johnson's algorithm instead of Dijkstra.
 *   3. Return which extra edge was used?
 *      Store the best candidate edge while comparing shortcut distances.
 */
public class UsefulExtraEdges {

    public static void main(String[] args) {
        UsefulExtraEdges solver = new UsefulExtraEdges();
        List<List<List<Integer>>> edgesCases = Arrays.asList(Arrays.asList(Arrays.asList(1, 2, 1), Arrays.asList(2, 3, 2)), Arrays.asList(Arrays.asList(1, 2, 5)));
        List<List<List<Integer>>> extraCases = Arrays.asList(Arrays.asList(Arrays.asList(1, 3, 2)), Collections.emptyList());
        int[] vertices = {3, 2};
        int[] expected = {2, 5};
        for (int i = 0; i < edgesCases.size(); i++) {
            int output = solver.findShortestPath(vertices[i], edgesCases.get(i), 1, vertices[i], extraCases.get(i));
            System.out.printf("edges=%s extraEdges=%s -> %d  expected=%d%n", edgesCases.get(i), extraCases.get(i), output, expected[i]);
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
            int edge1 = edge.get(0);
            int edge2 = edge.get(1);
            int weight = edge.get(2);
            graph.get(edge1).add(new Edge(edge2, weight));
            reverseGraph.get(edge2).add(new Edge(edge1, weight)); // for reverse graph
        }

        // Step 2: Run Dijkstra from source to find shortest path to all nodes
        int[] distFromSource = dijkstra(graph, source, vertices);

        // Step 3: Run Dijkstra from destination on reversed graph to find shortest path to all nodes
        int[] distToDestination = dijkstra(reverseGraph, destination, vertices);

        int shortestPath = distFromSource[destination];

        // Step 4: Try using each extra edge
        for (List<Integer> edge : extraEdges) {
            int node1 = edge.get(0);
            int node2 = edge.get(1);
            int weight = edge.get(2);

            // Distance from source to node1 + weight + from node2 to destination
            if (distFromSource[node1] != Integer.MAX_VALUE && distToDestination[node2] != Integer.MAX_VALUE) {
                shortestPath = Math.min(shortestPath, distFromSource[node1] + weight + distToDestination[node2]);
            }

            // Distance from source to node2 + weight + from node1 to destination
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

        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        queue.add(new Edge(source, 0));

        while (!queue.isEmpty()) {
            Edge currNode = queue.poll();
            int nodeVal = currNode.target;
            int nodeWeight = currNode.weight;

            if (nodeWeight > distances[nodeVal]) continue; // skip outdated entries

            for (Edge neighbor : graph.get(nodeVal)) {
                int newDist = distances[nodeVal] + neighbor.weight;
                if (newDist < distances[neighbor.target]) {
                    distances[neighbor.target] = newDist;
                    queue.add(new Edge(neighbor.target, newDist));
                }
            }
        }
        return distances;
    }
    /**
     * Helper class to represent edges in adjacency list.
     */
    static class Edge {
        int target;
        int weight;

        Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    /**
     * Driver method for testing.
     */

}

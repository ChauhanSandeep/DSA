package Graph;

import java.util.*;

/**
 * Problem: Find the shortest path from `source` to `destination` in a weighted directed graph,
 * considering additional bidirectional edges that may reduce the shortest path.
 *
 * Intuition:
 * - Compute the shortest path using Dijkstra's algorithm without extra edges.
 * - Try adding each extra edge one by one, recompute the shortest path, and track the minimum distance.
 *
 * Algorithm:
 * 1. Construct an adjacency list representation of the graph.
 * 2. Compute the shortest path using Dijkstra's algorithm without extra edges.
 * 3. Iterate through each extra edge:
 *    - Temporarily add the edge.
 *    - Run Dijkstra’s algorithm again.
 *    - Remove the edge to restore the original graph.
 * 4. Return the minimum found path, or -1 if no valid path exists.
 *
 * Time Complexity:
 * - Initial Dijkstra: O((V + E) log V)
 * - Each extra edge runs another Dijkstra: O(E * (V + E) log V)
 * - Worst-case: O(E * (V + E) log V)
 *
 * Space Complexity:
 * - O(V + E) for the adjacency list.
 * - O(V) for Dijkstra’s distance array.
 *
 * LeetCode Link: https://leetcode.com/problems/shortest-path-with-alternatives (Similar problem)
 */
public class UsefulExtraEdges {

    /**
     * Helper class to represent edges in the adjacency list.
     */
    static class Edge {
        int target, weight;

        Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    /**
     * Finds the shortest path between source and destination, considering extra bidirectional edges.
     *
     * @param vertices      Number of vertices in the graph.
     * @param edges         List of directed edges [from, to, weight].
     * @param source        Source vertex.
     * @param destination   Destination vertex.
     * @param extraEdges    List of additional bidirectional edges [u, v, weight].
     * @return              Minimum shortest path distance, or -1 if no path exists.
     */
    public int findShortestPath(int vertices, List<List<Integer>> edges, int source, int destination, List<List<Integer>> extraEdges) {
        // Step 1: Build adjacency list representation of the graph
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i <= vertices; i++) {
            graph.add(new ArrayList<>());
        }

        for (List<Integer> edge : edges) {
            graph.get(edge.get(0)).add(new Edge(edge.get(1), edge.get(2)));
        }

        // Step 2: Compute the shortest path without extra edges
        int[] baseDistances = dijkstra(graph, source, vertices);
        int shortestPath = baseDistances[destination];

        // Step 3: Try adding each extra edge and recompute the shortest path
        for (List<Integer> edge : extraEdges) {
            int u = edge.get(0), v = edge.get(1), weight = edge.get(2);

            // Temporarily add the extra bidirectional edge
            graph.get(u).add(new Edge(v, weight));
            graph.get(v).add(new Edge(u, weight));

            int[] updatedDistances = dijkstra(graph, source, vertices);
            shortestPath = Math.min(shortestPath, updatedDistances[destination]);

            // Remove the extra edge to restore the original graph
            graph.get(u).remove(graph.get(u).size() - 1);
            graph.get(v).remove(graph.get(v).size() - 1);
        }

        return (shortestPath == Integer.MAX_VALUE) ? -1 : shortestPath;
    }

    /**
     * Runs Dijkstra's algorithm to compute the shortest distances from the source node.
     *
     * @param graph    The adjacency list representation of the graph.
     * @param source   The starting node.
     * @param vertices The number of vertices.
     * @return         The shortest distance from the source to every other node.
     */
    private int[] dijkstra(List<List<Edge>> graph, int source, int vertices) {
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        int[] distances = new int[vertices + 1];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;
        minHeap.add(new Edge(source, 0));

        while (!minHeap.isEmpty()) {
            Edge current = minHeap.poll();
            int currentNode = current.target;

            for (Edge neighbor : graph.get(currentNode)) {
                int newDistance = distances[currentNode] + neighbor.weight;
                if (newDistance < distances[neighbor.target]) {
                    distances[neighbor.target] = newDistance;
                    minHeap.add(new Edge(neighbor.target, newDistance));
                }
            }
        }
        return distances;
    }

    /**
     * Driver method to test the implementation.
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
        System.out.println("Shortest Path: " + result);
    }
}

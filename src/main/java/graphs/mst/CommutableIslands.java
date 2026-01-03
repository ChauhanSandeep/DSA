package graphs.mst;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Problem: Minimum Cost to Connect All Islands
 *
 * Statement:
 * There are N islands and M possible bridges between them.
 * Each bridge has a cost. We need to find the minimum total cost to
 * connect all islands so that they form a single connected component.
 * It is guaranteed that at least one spanning structure exists.
 *
 * Example:
 * Input (edges):
 *  [ [1,2,1], [2,3,4], [1,4,3], [4,3,2], [1,3,10] ]
 * Output:
 *  6  (bridges chosen: [1-2 @1], [4-3 @2], [1-4 @3])
 *
 * LeetCode Similar Problem:
 * - Min Cost to Connect All Points: https://leetcode.com/problems/min-cost-to-connect-all-points/
 *
 * Follow-up Questions:
 * 1. How would you solve this if the graph was disconnected?
 *    - Detect connectivity using DFS/BFS; if disconnected, no spanning tree exists.
 * 2. Can you solve using Kruskal's algorithm instead of Prim's?
 *    - Yes, Kruskal sorts all edges and uses Union-Find to connect components.
 * 3. What is the time complexity difference between Prim’s and Kruskal’s?
 *    - Prim’s: O(E log V), Kruskal’s: O(E log E). Choice depends on graph density.
 */
public class CommutableIslands {

    public static void main(String[] args) {
        List<List<Integer>> edges = new ArrayList<>();
        edges.add(Stream.of(1, 2, 1).collect(toList()));
        edges.add(Stream.of(2, 3, 4).collect(toList()));
        edges.add(Stream.of(1, 4, 3).collect(toList()));
        edges.add(Stream.of(4, 3, 2).collect(toList()));
        edges.add(Stream.of(1, 3, 10).collect(toList()));

        int minCost = new CommutableIslands().findMinimumCost(4, edges);
        System.out.println(minCost); // Expected 6
    }

    /**
     * Uses Prim’s algorithm to compute MST cost
     *
     * Steps:
     * 1. Build adjacency list from input
     * 2. Initialize min-heap with (vertex=1, weight=0)
     * 3. While heap not empty:
     *      - Extract min edge
     *      - If vertex not visited:
     *          - Mark visited
     *          - Add edge weight to result
     *          - Push all neighbors into heap
     *
     * Time Complexity: O(E log V) where E = edges, V = vertices
     * Space Complexity: O(V + E)
     *
     * @param vertices number of vertices (1-based indexing)
     * @param input edges in format [v1, v2, weight]
     * @return minimum spanning tree cost
     */
    public int findMinimumCost(int vertices, List<List<Integer>> input) {
        int totalCost = 0;

        // Build adjacency list
        List<Edge>[] graph = new ArrayList[vertices + 1];
        for (int i = 0; i <= vertices; i++) {
            graph[i] = new ArrayList<>();
        }
        for (List<Integer> edge : input) {
            int v1 = edge.get(0);
            int v2 = edge.get(1);
            int weight = edge.get(2);

            graph[v1].add(new Edge(v1, v2, weight));
            graph[v2].add(new Edge(v2, v1, weight));
        }

        // Min-heap based on weight
        PriorityQueue<VertexInfo> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.weight, b.weight));
        minHeap.add(new VertexInfo(1, 0, 0)); // Dummy starting parent

        boolean[] visited = new boolean[vertices + 1];

        while (!minHeap.isEmpty()) {
            // 1. SELECT
            VertexInfo current = minHeap.poll();

            // 2. MARK(*)
            if (!visited[current.vertex]) {
                visited[current.vertex] = true;

                // 3. WORK
                if (current.parentVertex != 0) { // Skip for the dummy starting parent because it has no weight
                    totalCost += current.weight;
                }

                // 4. ADD(*)
                for (Edge edge : graph[current.vertex]) {
                    if (!visited[edge.neighbor]) {
                        minHeap.add(new VertexInfo(edge.neighbor, current.vertex, edge.weight));
                    }
                }
            }
        }
        return totalCost;
    }

    /**
     * Graph edge representation
     */
    static class Edge {
        int source;
        int neighbor;
        int weight;

        public Edge(int source, int neighbor, int weight) {
            this.source = source;
            this.neighbor = neighbor;
            this.weight = weight;
        }
    }

    /**
     * Helper Pair for Prim’s algorithm
     */
    static class VertexInfo {
        int vertex;
        int parentVertex;
        int weight;

        public VertexInfo(int vertex, int parentVertex, int weight) {
            this.vertex = vertex;
            this.parentVertex = parentVertex;
            this.weight = weight;
        }
    }
}

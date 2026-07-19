package graphs.mst;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

    /**
     * Intuition: Prim's algorithm grows one connected component by always taking
     * the cheapest edge crossing from visited islands to unvisited islands. Each
     * accepted edge is safe because it is the lightest way to expand the current cut.
     *
     * Algorithm:
     *   1. Build the original undirected adjacency list from bridge input.
     *   2. Seed the min-heap with island 1 and a dummy zero-weight parent.
     *   3. Pop the cheapest candidate; if unvisited, mark it and add its edge cost.
     *   4. Push all outgoing edges that lead to unvisited islands.
     *
     * Time:  O(E log E) - bridge candidates are pushed into the priority queue.
     * Space: O(V + E) - adjacency list, visited array, and heap.
     *
     * @param vertices number of islands, using 1-based labels
     * @param input bridges in [island1, island2, cost] format
     * @return minimum total cost of the spanning tree found by Prim's algorithm
     */
public class CommutableIslands {

    public static void main(String[] args) {
        CommutableIslands solver = new CommutableIslands();
        List<List<Integer>> edges1 = new ArrayList<>();
        edges1.add(Stream.of(1, 2, 1).collect(toList()));
        edges1.add(Stream.of(2, 3, 4).collect(toList()));
        edges1.add(Stream.of(1, 4, 3).collect(toList()));
        edges1.add(Stream.of(4, 3, 2).collect(toList()));
        edges1.add(Stream.of(1, 3, 10).collect(toList()));
        List<List<Integer>> edges2 = new ArrayList<>();
        edges2.add(Stream.of(1, 2, 7).collect(toList()));

        System.out.printf("vertices=4 edges=%s -> %d  expected=6%n", edges1, solver.findMinimumCost(4, edges1));
        System.out.printf("vertices=2 edges=%s -> %d  expected=7%n", edges2, solver.findMinimumCost(2, edges2));
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

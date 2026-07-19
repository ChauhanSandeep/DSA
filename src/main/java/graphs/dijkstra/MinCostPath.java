package graphs.dijkstra;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

    /**
     * Intuition: Dijkstra repeatedly finalizes the unvisited vertex with the
     * smallest cost seen so far. The priority queue may contain stale paths, so
     * the original visited check decides whether the popped vertex should print
     * and expand its neighbors.
     *
     * Algorithm:
     *   1. Build the original undirected adjacency list from [vertex1, vertex2, weight] input.
     *   2. Push vertex 1 with cost 0 into the priority queue.
     *   3. Pop the lowest-cost pair; if unvisited, mark it and print its path/cost.
     *   4. Push all unvisited neighbors with accumulated weights.
     *
     * Time:  O(E log E) - every edge can add heap entries and heap operations are logarithmic.
     * Space: O(V + E) - adjacency list, visited array, and priority queue.
     *
     * @param vertices number of vertices in input, labeled 1 through vertices
     * @param input edges in [vertex1, vertex2, weight] format
     */
public class MinCostPath {

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        graph.add((ArrayList<Integer>) Stream.of(1, 2, 5).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(2, 3, 7).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(1, 3, 20).collect(toList()));

        MinCostPath solver = new MinCostPath();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(buffer));
        solver.solve(3, graph);
        System.setOut(originalOut);
        String output = buffer.toString().trim().replace(System.lineSeparator(), " | ");
        String expected = "[to: 1 via:  with weight: 0] | [to: 2 via: 2 with weight: 5] | [to: 3 via: 23 with weight: 12]";
        System.out.printf("edges=%s -> %s  expected=%s%n", graph, output, expected);
    }


    /**
     * This works on Dijkstra's algorithm
     * Remove -> Mark* -> Work -> Add*
     * remove from queue -> mark not visited -> print -> add not visited
     *
     * @param vertices number of vertices in input 1 to `vertices`
     * @param input list of edges in graph format -> [vertex1, vertex2, weight]
     * @return
     */
    private void solve(int vertices, ArrayList<ArrayList<Integer>> input) {
        ArrayList<Edge>[] graph = new ArrayList[vertices + 1];
        for (int i = 0; i <= vertices; i++) {
            graph[i] = new ArrayList<>();
        }

        for (ArrayList<Integer> edge : input) {
            int v1 = edge.get(0);
            int v2 = edge.get(1);
            int weight = edge.get(2);

            graph[v1].add(new Edge(v1, v2, weight));
            graph[v2].add(new Edge(v2, v1, weight));
        }

        PriorityQueue<Pair> queue = new PriorityQueue<>((a, b) -> a.weightSoFar - b.weightSoFar);
        queue.offer(new Pair(1, "", 0));
        boolean[] visited = new boolean[vertices + 1];

        while (!queue.isEmpty()) {
            Pair curr = queue.poll();

            if (!visited[curr.vertex]) {
                visited[curr.vertex] = true;
                System.out.printf("[to: %s via: %s with weight: %s]%n", curr.vertex, curr.pathSoFar, curr.weightSoFar);

                for (Edge edge : graph[curr.vertex]) {
                    if (!visited[edge.neighbor]) {
                        queue.offer(new Pair(edge.neighbor, curr.pathSoFar + edge.neighbor, curr.weightSoFar + edge.weight));
                    }
                }
            }

        }
    }

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

    static class Pair {
        int vertex;
        String pathSoFar;
        int weightSoFar;

        public Pair(int vertex, String pathSoFar, int weightSoFar) {
            this.vertex = vertex;
            this.pathSoFar = pathSoFar;
            this.weightSoFar = weightSoFar;
        }
    }

}

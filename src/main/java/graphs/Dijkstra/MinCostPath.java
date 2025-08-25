package graph.Dijkstra;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Find min cost required to reach each vertex of graph from vertex 1
 */
public class MinCostPath {

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        graph.add((ArrayList<Integer>) Stream.of(1, 2, 1).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(2, 3, 4).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(1, 4, 3).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(4, 3, 2).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(1, 3, 10).collect(toList()));

        new MinCostPath().solve(4, graph);
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

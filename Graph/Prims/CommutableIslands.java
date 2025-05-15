package Graph.Prims;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * There are `vertices` islands and there are M bridges connecting them.
 * Each bridge has some cost attached to it.
 * We need to find bridges with minimal cost such that all islands are connected.
 * It is guaranteed that input data will contain at least one possible scenario
 * in which all islands are connected with each other.
 */
public class CommutableIslands {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        graph.add((ArrayList<Integer>) Stream.of(1, 2, 1).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(2, 3, 4).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(1, 4, 3).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(4, 3, 2).collect(toList()));
        graph.add((ArrayList<Integer>) Stream.of(1, 3, 10).collect(toList()));

        int minCost = new CommutableIslands().solve(4, graph);
        System.out.println(minCost);
    }

    /**
     * This works on Prim's algorithm
     * Remove -> Mark* -> Work -> Add*
     * remove from queue -> mark not visited -> print -> add not visited
     *
     * @param vertices number of vertices in input 1 to `vertices`
     * @param input list of edges in graph format -> [vertex1, vertex2, weight]
     * @return min cost of spanning all vertices
     */
    private int solve(int vertices, ArrayList<ArrayList<Integer>> input) {
        int result = 0;
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

        PriorityQueue<Pair> queue = new PriorityQueue<>((a, b) -> a.weight - b.weight);
        queue.add(new Pair(1, 0, 0));
        boolean[] visited = new boolean[vertices + 1];

        /**
         * Take Pair with min weight from queue.
         * Add its weight to result
         * Put non-visited neighbor pairs in queue
         */
        while (!queue.isEmpty()) {
            Pair curr = queue.poll();

            if (!visited[curr.vertex]) {
                visited[curr.vertex] = true;
                if (curr.parentVertex != 0) {
                    // System.out.println("[from:" + curr.vertex + " to:" + curr.parentVertex + " @weight:" + curr.weight + "]");
                    result += curr.weight;
                }

                for (Edge edge : graph[curr.vertex]) {
                    if (!visited[edge.neighbor]) {
                        queue.add(new Pair(edge.neighbor, curr.vertex, edge.weight));
                    }

                }
            }
        }
        return result;
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
        int parentVertex;
        int weight;

        public Pair(int vertex, int parentVertex, int weight) {
            this.vertex = vertex;
            this.parentVertex = parentVertex;
            this.weight = weight;
        }
    }

}



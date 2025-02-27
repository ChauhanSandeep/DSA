package Graph;

import java.util.*;

public class UsefulExtraEdges {

    static class Pair {
        int vertex, weight;
        Pair(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    public int solve(int vertices, ArrayList<ArrayList<Integer>> input, int source, int destination, ArrayList<ArrayList<Integer>> extraGraph) {
        List<List<Pair>> graph = new ArrayList<>();
        for (int i = 0; i <= vertices; i++) {
            graph.add(new ArrayList<>());
        }

        // Add one-way edges from `input`
        for (ArrayList<Integer> edge : input) {
            graph.get(edge.get(0)).add(new Pair(edge.get(1), edge.get(2)));
        }

        // Compute shortest path without extra edges
        int[] dist = dijkstra(graph, source, vertices);
        int shortestPath = dist[destination];

        // Try adding each extra edge and compute shortest path again
        for (ArrayList<Integer> edge : extraGraph) {
            int u = edge.get(0), v = edge.get(1), weight = edge.get(2);
            graph.get(u).add(new Pair(v, weight));
            graph.get(v).add(new Pair(u, weight)); // Bidirectional edge

            int[] newDist = dijkstra(graph, source, vertices);
            shortestPath = Math.min(shortestPath, newDist[destination]);

            // Remove the extra edge to reset the graph
            graph.get(u).remove(graph.get(u).size() - 1);
            graph.get(v).remove(graph.get(v).size() - 1);
        }

        return shortestPath == Integer.MAX_VALUE ? -1 : shortestPath;
    }

    private int[] dijkstra(List<List<Pair>> graph, int source, int vertices) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.weight));
        int[] dist = new int[vertices + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        pq.add(new Pair(source, 0));

        while (!pq.isEmpty()) {
            Pair current = pq.poll();
            int u = current.vertex;

            for (Pair neighbor : graph.get(u)) {
                int v = neighbor.vertex, weight = neighbor.weight;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new Pair(v, dist[v]));
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList(1, 2, 1)));
        input.add(new ArrayList<>(Arrays.asList(2, 3, 2)));

        ArrayList<ArrayList<Integer>> extraGraph = new ArrayList<>();
        extraGraph.add(new ArrayList<>(Arrays.asList(1, 3, 2)));

        UsefulExtraEdges obj = new UsefulExtraEdges();
        int result = obj.solve(3, input, 1, 3, extraGraph);
        System.out.println("Shortest Path: " + result);
    }
}

package Graph;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a graph of `vertices` nodes. Also given the weighted edges in the form of array `input`.
 * You are also given starting point `source` and `destination`
 * Also given are some extra edges in the form of vector `extraGraph`.
 *
 * You need to find the length of the shortest path from `source` to `destination` if you can use maximum
 * one road from the given roads in `extraGraph`.
 * `input` roads are one way ie they go from input[i][0] to input[i][1].
 * `extraGraph` roads are bidirectional
 * https://www.interviewbit.com/problems/useful-extra-edges/
 */
public class UsefulExtraEdges {

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> input = new ArrayList<>();
        input.add((ArrayList<Integer>) Stream.of(1, 2, 1).collect(Collectors.toList()));
        input.add((ArrayList<Integer>) Stream.of(2, 3, 2).collect(Collectors.toList()));

        ArrayList<ArrayList<Integer>> extraGraph = new ArrayList<>();
        extraGraph.add((ArrayList<Integer>) Stream.of(1, 3, 2).collect(Collectors.toList()));

        System.out.println(new UsefulExtraEdges().solve(3, input, 1, 3, extraGraph));

    }

    private static final char UNIDIRECTIONAL = 'B';
    private static final char BIDIRECTIONAL = 'E';
    ArrayList<ArrayList<Pair>> graph;
    int result;
    boolean isTargetFound;

    public int solve(int vertices, ArrayList<ArrayList<Integer>> input, int source, int destination, ArrayList<ArrayList<Integer>> extraGraph) {

        result = Integer.MAX_VALUE;
        isTargetFound = false;
        graph = new ArrayList<ArrayList<Pair>>();
        boolean[] visited = new boolean[vertices + 1];

        for (int i = 0; i <= vertices; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < input.size(); i++) {
            int v1 = input.get(i).get(0);
            int v2 = input.get(i).get(1);
            int weight = input.get(i).get(2);
            graph.get(v1).add(new Pair(v2, weight, UNIDIRECTIONAL));
        }

        for (int i = 0; i < extraGraph.size(); i++) {
            int v1 = extraGraph.get(i).get(0);
            int v2 = extraGraph.get(i).get(1);
            int weight = extraGraph.get(i).get(2);
            if (1 <= v1 && 1 <= v2 && v1 <= vertices && v2 <= vertices) {
                graph.get(v1).add(new Pair(v2, weight, BIDIRECTIONAL));
                graph.get(v2).add(new Pair(v1, weight, BIDIRECTIONAL));
            }

        }

        dfs(source, destination, visited, 0, 0);
        return isTargetFound ? result : -1;
    }

    public void dfs(int source, int target, boolean[] visited, int tillCost, int extraEdges) {
        //base case
        if (extraEdges == 2) {
            return;
        } else if (target == source) {
            isTargetFound = true;
            result = Math.min(result, tillCost);
            return;
        }
        visited[source] = true;

        //go for children
        for (Pair pair : graph.get(source)) {
            if (!visited[pair.vertex]) {
                if (pair.type == BIDIRECTIONAL) {
                    dfs(pair.vertex, target, visited, tillCost + pair.weight, extraEdges + 1);
                } else {
                    dfs(pair.vertex, target, visited, tillCost + pair.weight, extraEdges);
                }
            }
        }

        visited[source] = false;
    }

    static class Pair {
        public int vertex;
        public int weight;
        public char type;

        Pair(int vertex, int weight, char type) {
            this.vertex = vertex;
            this.weight = weight;
            this.type = type;
        }
    }
}

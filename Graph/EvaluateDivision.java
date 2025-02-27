package Graph;

import java.util.*;

public class EvaluateDivision {

    public static void main(String[] args) {
        List<List<String>> equations = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("b", "c")
        );

        double[] values = {2.0, 3.0};
        List<List<String>> queries = Arrays.asList(
                Arrays.asList("a", "c"),
                Arrays.asList("b", "a"),
                Arrays.asList("a", "e"),
                Arrays.asList("a", "a"),
                Arrays.asList("x", "x")
        );

        EvaluateDivision solver = new EvaluateDivision();
        double[] results = solver.calcEquation(equations, values, queries);
        System.out.println(Arrays.toString(results));
    }

    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();

        // Build the graph
        for (int i = 0; i < equations.size(); i++) {
            String dividend = equations.get(i).get(0);
            String divisor = equations.get(i).get(1);
            double quotient = values[i];

            graph.putIfAbsent(dividend, new HashMap<>());
            graph.putIfAbsent(divisor, new HashMap<>());

            graph.get(dividend).put(divisor, quotient);
            graph.get(divisor).put(dividend, 1.0 / quotient);
        }

        // Process queries
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String dividend = queries.get(i).get(0);
            String divisor = queries.get(i).get(1);

            if (!graph.containsKey(dividend) || !graph.containsKey(divisor)) {
                results[i] = -1.0;
            } else if (dividend.equals(divisor)) {
                results[i] = 1.0;
            } else {
                Set<String> visited = new HashSet<>();
                results[i] = dfs(dividend, divisor, graph, visited, 1.0);
            }
        }
        return results;
    }

    private double dfs(String curr, String target, Map<String, Map<String, Double>> graph, Set<String> visited, double value) {
        if (curr.equals(target)) return value;
        if (visited.contains(curr)) return -1.0;

        visited.add(curr);
        for (Map.Entry<String, Double> neighbor : graph.get(curr).entrySet()) {
            double result = dfs(neighbor.getKey(), target, graph, visited, value * neighbor.getValue());
            if (result != -1.0) return result;  // Stop DFS if a valid path is found
        }
        return -1.0;
    }
}

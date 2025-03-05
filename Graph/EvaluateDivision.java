package Graph;

import java.util.*;

/**
 * Evaluate Division (Graph-Based Approach)
 *
 * Problem:
 * Given a list of equations like "a / b = 2.0" and queries like "a / c",
 * return the calculated values using a graph representation.
 *
 * Approach:
 * 1. **Graph Representation**:
 *    - Use a HashMap where each node stores its neighbors and division values.
 * 2. **DFS Search**:
 *    - Perform DFS to find a path between two variables and calculate the quotient.
 * 3. **Cycle and Invalid Case Handling**:
 *    - If a variable doesn't exist, return -1.0.
 *    - If two variables are the same, return 1.0.
 *
 * Time Complexity: **O(Q * (V + E))** (DFS per query)
 * Space Complexity: **O(V + E)** (Graph storage)
 */
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
        System.out.println(Arrays.toString(results)); // Expected: [6.0, 0.5, -1.0, 1.0, -1.0]
    }

    /**
     * Computes the results for division queries based on the given equations.
     *
     * @param equations List of equations, where each equation is represented as ["numerator", "denominator"].
     * @param values    Corresponding division values for each equation.
     * @param queries   List of division queries to evaluate.
     * @return An array of computed results for each query.
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();

        // Step 1: Build the graph (Adjacency List Representation)
        for (int i = 0; i < equations.size(); i++) {
            String numerator = equations.get(i).get(0);
            String denominator = equations.get(i).get(1);
            double quotient = values[i];

            graph.putIfAbsent(numerator, new HashMap<>());
            graph.putIfAbsent(denominator, new HashMap<>());

            graph.get(numerator).put(denominator, quotient);
            graph.get(denominator).put(numerator, 1.0 / quotient);
        }

        // Step 2: Process each query using DFS
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String numerator = queries.get(i).get(0);
            String denominator = queries.get(i).get(1);

            if (!graph.containsKey(numerator) || !graph.containsKey(denominator)) {
                results[i] = -1.0; // One of the variables does not exist
            } else if (numerator.equals(denominator)) {
                results[i] = 1.0; // Self-division case (e.g., a/a = 1.0)
            } else {
                Set<String> visited = new HashSet<>();
                results[i] = dfs(graph, numerator, denominator, visited, 1.0);
            }
        }
        return results;
    }

    /**
     * Depth-First Search (DFS) to find the conversion rate between numerator and denominator.
     *
     * @param graph   The adjacency list representation of the equation graph.
     * @param current The current variable in the search.
     * @param target  The target variable we are searching for.
     * @param visited A set to keep track of visited nodes to avoid cycles.
     * @param value   The accumulated conversion rate along the path.
     * @return The computed conversion rate, or -1.0 if no valid path exists.
     */
    private double dfs(Map<String, Map<String, Double>> graph, String current, String target, Set<String> visited, double value) {
        if (current.equals(target)) return value;
        if (visited.contains(current)) return -1.0;

        visited.add(current);

        for (Map.Entry<String, Double> neighbor : graph.get(current).entrySet()) {
            double result = dfs(graph, neighbor.getKey(), target, visited, value * neighbor.getValue());
            if (result != -1.0) return result; // Stop DFS if a valid path is found
        }

        return -1.0;
    }
}

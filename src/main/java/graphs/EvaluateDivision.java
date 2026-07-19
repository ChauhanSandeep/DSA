package graphs;

import java.util.*;


/**
 * Problem: Evaluate Division
 *
 * Given equations such as a / b = 2.0, answer division queries such as a / c.
 * If the variables cannot be connected through known equations, the query result
 * is -1.0.
 *
 * Leetcode: https://leetcode.com/problems/evaluate-division/ (Medium)
 * Rating:   acceptance 64.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Weighted DFS | Multiplicative paths
 *
 * Example:
 *   Input:  equations = [[a,b],[b,c]], values = [2.0,3.0], queries = [[a,c],[b,a],[x,x]]
 *   Output: [6.0, 0.5, -1.0]
 *   Why:    a/c follows a to b to c and multiplies 2.0 * 3.0, while x is unknown.
 *
 * Follow-ups:
 *   1. Answer many queries after one build?
 *      Use weighted union-find or precompute all-pairs ratios per component.
 *   2. Support adding equations online?
 *      Weighted union-find can merge components and preserve ratios incrementally.
 *   3. Detect inconsistent equations?
 *      When two variables are already connected, compare the implied ratio with the new value.
 *
 * Related: Currency conversion graphs, Satisfiability of Equality Equations (990).
 */
public class EvaluateDivision {



    public static void main(String[] args) {
        EvaluateDivision solver = new EvaluateDivision();
        List<List<String>> equations = Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c"));
        double[] values = {2.0, 3.0};
        List<List<String>> queries = Arrays.asList(
            Arrays.asList("a", "c"), Arrays.asList("b", "a"), Arrays.asList("a", "e"), Arrays.asList("a", "a"), Arrays.asList("x", "x")
        );
        double[] output = solver.evaluateEquations(equations, values, queries);
        System.out.printf("queries=%s  ->  %s  expected=%s%n",
            queries, Arrays.toString(output), Arrays.toString(new double[]{6.0, 0.5, -1.0, 1.0, -1.0}));
    }
    /**
     * Intuition: each equation a / b = value is two directed weighted edges: a to b
     * with value, and b to a with the reciprocal. Answering a query is graph search
     * where the path product gives the requested ratio.
     *
     * Algorithm:
     *   1. Build the weighted adjacency map from equations and reciprocal edges.
     *   2. For each query, reject it if either variable is missing.
     *   3. DFS from numerator to denominator, multiplying edge weights along the path.
     *   4. Return the product found, or -1.0 when no path exists.
     *
     * Time:  O(E + Q*(V+E)) - building is linear, and each query can search the graph.
     * Space: O(V+E) - graph storage plus visited nodes during a query.
     *
     * @param equations variable pairs representing division equations
     * @param values equation values aligned with equations
     * @param queries division queries to answer
     * @return one answer per query, or -1.0 for impossible queries
     */
  public double[] evaluateEquations(List<List<String>> equations, double[] values, List<List<String>> queries) {
    Map<String, Map<String, Double>> graph = buildGraph(equations, values); // <source, <target, value>>

    double[] results = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++) {
      String numerator = queries.get(i).get(0);
      String denominator = queries.get(i).get(1);

      if (!graph.containsKey(numerator) || !graph.containsKey(denominator)) {
        results[i] = -1.0; // Variable doesn't exist
      } else if (numerator.equals(denominator)) {
        results[i] = 1.0;  // Self division
      } else {
        Set<String> visited = new HashSet<>();
        results[i] = dfsEvaluate(graph, numerator, denominator, visited, 1.0);
      }
    }
    return results;
  }

  /**
   * Constructs a bidirectional graph from equations.
   * Each node stores neighbors and the division ratio.
   *
   * @param equations List of variable pairs.
   * @param values    Division results for each pair.
   * @return Graph represented as adjacency list.
   */
  private Map<String, Map<String, Double>> buildGraph(List<List<String>> equations, double[] values) {
    Map<String, Map<String, Double>> graph = new HashMap<>();

    for (int i = 0; i < equations.size(); i++) {
      String numerator = equations.get(i).get(0);
      String denominator = equations.get(i).get(1);
      double value = values[i];

      graph.computeIfAbsent(numerator, k -> new HashMap<>()).put(denominator, value);
      graph.computeIfAbsent(denominator, k -> new HashMap<>()).put(numerator, 1.0 / value);
    }

    return graph;
  }

  /**
   * Depth-First Search to compute value from `current` to `target`.
   *
   * @param graph  Adjacency list representing the equation graph.
   * @param current Current variable in DFS.
   * @param target  Target variable.
   * @param visited Set to avoid cycles.
   * @param product Accumulated product so far.
   * @return Result of the division, or -1.0 if path not found.
   */
  private double dfsEvaluate(Map<String, Map<String, Double>> graph, String current, String target, Set<String> visited,
      double product) {
      if (current.equals(target)) {
          return product;
      }
    visited.add(current);

    for (Map.Entry<String, Double> neighbor : graph.get(current).entrySet()) {
        if (visited.contains(neighbor.getKey())) {
            continue;
        }

      double result = dfsEvaluate(graph, neighbor.getKey(), target, visited, product * neighbor.getValue());
        if (result != -1.0) {
            return result;
        }
    }

    return -1.0; // No valid path
  }
}

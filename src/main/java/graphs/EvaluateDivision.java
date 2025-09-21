package graphs;

import java.util.*;


/**
 * Problem: Evaluate Division
 *
 * LeetCode Link: https://leetcode.com/problems/evaluate-division/
 *
 * Given a list of equations of the form `A / B = value`, return the results of
 * the division queries such as `C / D`. If the division cannot be evaluated,
 * return -1.0 for that query.
 *
 * Example:
 * Input:
 *   equations = [["a","b"],["b","c"]],
 *   values = [2.0,3.0],
 *   queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * Output:
 *   [6.0,0.5,-1.0,1.0,-1.0]
 *
 * Explanation:
 * Here the input equations mean:
 * *   - a / b = 2.0
 * *   - b / c = 3.0
 * * The queries can be evaluated as follows:
 *   - a / c = a / b * b / c = 2.0 * 3.0 = 6.0
 *
 * Follow-up Questions:
 * - Can you handle dynamic updates (add/remove equations)?
 * - Can you do this with Union-Find (Disjoint Set) instead of DFS/BFS?
 */
public class EvaluateDivision {

  public static void main(String[] args) {
    List<List<String>> equations = Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c"));
    double[] values = {2.0, 3.0};

    List<List<String>> queries =
        Arrays.asList(Arrays.asList("a", "c"), Arrays.asList("b", "a"), Arrays.asList("a", "e"),
            Arrays.asList("a", "a"), Arrays.asList("x", "x"));

    EvaluateDivision solver = new EvaluateDivision();
    double[] results = solver.evaluateEquations(equations, values, queries);
    System.out.println("Query Results: " + Arrays.toString(results));
  }

  /**
   * Evaluates each query using DFS on the graph built from equations.
   *
   * Steps:
   * 1. Construct a graph where each variable points to its neighbors with edge weights.
   * Here a/b becomes an edge a -> b with weight 2.0 and b -> a with weight 0.5.
   * a/c is calculated as path a -> b -> c with weights 2.0 * 3.0 = 6.0.
   * 2. For each query, run DFS to find a path from numerator to denominator.
   *
   * Time Complexity: O(Q * (V + E)) — Q queries and DFS per query.
   * Space Complexity: O(V + E) — to store the graph.
   *
   * @param equations List of variable pairs ["a", "b"] representing equations.
   * @param values    Array of corresponding equation values (a / b = value).
   * @param queries   Queries to evaluate as pairs ["a", "c"].
   * @return Array of results for each query.
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
package graphs.floydwarshall;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ✅ Problem: Evaluate Division
 *
 * You are given equations of the form `A / B = value` and a list of queries
 * `C / D`. For each query return the computed ratio by chaining equations, or
 * -1.0 if a variable is unknown or no chain connects the two variables.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/evaluate-division/   (Medium)
 * 🏷️ Pattern:  Graph · BFS traversal · Floyd–Warshall (multiplicative all-pairs)
 *
 * 🧪 Example:
 *   equations = [["a","b"],["b","c"]], values = [2.0, 3.0]
 *   queries   = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 *   Output    = [6.0, 0.5, -1.0, 1.0, -1.0]   // a/c = 2*3, x is unknown
 *
 * 🚧 Edge cases to remember:
 *   - query variable never appears in equations → -1.0
 *   - x / x where x is unknown                  → -1.0 (not 1.0)
 *   - a / a where a is known                    → 1.0  (diagonal initialised)
 *   - disconnected variables                    → -1.0 (ratio never filled)
 *
 *  Approach                Method                  Time          Space
 *  ----------------------  ----------------------  ------------  --------
 *  BFS per query           evaluateEquationsBFS    O(Q*(V+E))    O(V+E)
 *  Floyd-Warshall (best)   evaluateEquations       O(V^3 + Q)    O(V^2)
 *
 * 🔍 Follow-ups:
 *   1. Few variables, many queries? Floyd–Warshall precompute wins.
 *   2. Many variables, few queries? BFS/DFS per query is cheaper.
 *   3. Dynamic add/remove equations? Prefer Union-Find with ratios to root.
 *
 * 🔁 Related: Currency Conversion, Satisfiability of Equality Equations (990).
 */
public class EvaluateDivision {

    /**
     * 🧠 Intuition: model `a / b = w` as an edge a→b weighted w (and b→a = 1/w).
     * The answer to `c / d` is the product of edge weights along ANY path from
     * c to d, since c/d = (c/x)·(x/y)·…·(z/d). BFS that path level by level,
     * carrying the running product; the graph is consistent so any path works.
     *
     * Algorithm:
     *   1. Build adjacency list: node → (neighbor → ratio).
     *   2. For each query, short-circuit unknown vars and self/self to 1.
     *   3. BFS from `from`, pushing (node, productSoFar); on reaching `to` return.
     *   4. If BFS drains without reaching `to`, the pair is disconnected → -1.
     *
     * Time:  O(Q * (V + E))   — one BFS per query
     * Space: O(V + E)         — adjacency list + visited set / queue
     *
     * @param equations variable pairs, equations.get(i) = [from, to] meaning from/to
     * @param values    values.get(i) = value of equations.get(i)
     * @param queries   pairs [from, to] asking for from/to
     * @return array of query results, -1.0 when not computable
     */
    public double[] evaluateEquationsBFS(List<List<String>> equations, double[] values,
                                         List<List<String>> queries) {
        // --- Step 1: build the weighted adjacency list ------------------
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            String from = equations.get(i).get(0);
            String to = equations.get(i).get(1);
            graph.computeIfAbsent(from, key -> new HashMap<>()).put(to, values[i]);
            graph.computeIfAbsent(to, key -> new HashMap<>()).put(from, 1.0 / values[i]);
        }

        // --- Step 2: answer each query with a fresh BFS -----------------
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String from = queries.get(i).get(0);
            String to = queries.get(i).get(1);
            if (!graph.containsKey(from) || !graph.containsKey(to)) {
                results[i] = -1.0; // unknown variable
            } else {
                results[i] = bfsRatio(graph, from, to);
            }
        }
        return results;
    }

    /**
     * 🧠 Intuition: BFS from `from`, multiplying edge ratios as we expand, until
     * we pop `to`. The first time we reach `to` the accumulated product is the
     * answer (all paths agree under consistent equations).
     *
     * Algorithm:
     *   1. Seed the queue with (from, 1.0) and mark `from` visited.
     *   2. Pop (node, product); if node == to, return product.
     *   3. Push each unvisited neighbor with product * edgeRatio.
     *   4. Queue empties → no path → -1.0.
     *
     * Time:  O(V + E)
     * Space: O(V)
     *
     * @param graph node → (neighbor → ratio) adjacency list
     * @param from  numerator variable to start from
     * @param to    denominator variable to reach
     * @return ratio from/to, or -1.0 if `to` is unreachable
     */
    private double bfsRatio(Map<String, Map<String, Double>> graph, String from, String to) {
        Deque<String> nodeQueue = new ArrayDeque<>();
        Deque<Double> productQueue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        nodeQueue.offer(from);
        productQueue.offer(1.0);
        visited.add(from);

        while (!nodeQueue.isEmpty()) {
            String node = nodeQueue.poll();
            double weightSoFar = productQueue.poll();

            if (node.equals(to)) {
                return weightSoFar; // first arrival is the answer (also covers from == to)
            }
            Map<String, Double> neighbors = graph.get(node);

            for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
                String neighborNode = entry.getKey();
                Double neighborWeight = entry.getValue();
                if (visited.add(neighborNode)) {
                    nodeQueue.offer(neighborNode);
                    productQueue.offer(weightSoFar * neighborWeight);
                }
            }
        }
        return -1.0; // disconnected
    }

    /**
     * 🧠 Intuition: treat every variable as a node and `a / b = w` as an edge
     * whose "distance" is the ratio a/b (and b/a = 1/w backwards). Instead of
     * summing weights along a path like classic Floyd–Warshall, we MULTIPLY
     * them, because a/c = (a/b) * (b/c). Precompute every reachable ratio once,
     * then each query is a single matrix lookup.
     *
     * Algorithm:
     *   1. Map each distinct node (variable) to an index; init ratio[i][i] = 1.
     *   2. Fill direct edges: ratio[from][to] = w, ratio[to][from] = 1/w.
     *   3. Floyd–Warshall: if from→via and via→to known, set from→to = product.
     *   4. Answer each query by lookup; unknown node or 0 ratio → -1.0.
     *
     * Time:  O(V^3 + Q)   — V = distinct nodes, Q = queries
     * Space: O(V^2)       — the ratio matrix
     *
     * @param equations variable pairs, equations.get(i) = [from, to] meaning from/to
     * @param values    values.get(i) = value of equations.get(i)
     * @param queries   pairs [from, to] asking for from/to
     * @return array of query results, -1.0 when not computable
     */
    public double[] evaluateEquations(List<List<String>> equations, double[] values,
                                      List<List<String>> queries) {
        // --- Step 1: index every distinct node (variable) ---------------
        Map<String, Integer> nodeToIndexMap = new HashMap<>();
        for (List<String> equation : equations) {
            nodeToIndexMap.putIfAbsent(equation.get(0), nodeToIndexMap.size());
            nodeToIndexMap.putIfAbsent(equation.get(1), nodeToIndexMap.size());
        }

        int nodes = nodeToIndexMap.size();
        double[][] adjMatrix = new double[nodes][nodes];

        for (int i = 0; i < nodes; i++) {
            adjMatrix[i][i] = 1.0; // a / a = 1 for every known node
        }

        // --- Step 2: seed direct edges ----------------------------------
        for (int i = 0; i < equations.size(); i++) {
            int from = nodeToIndexMap.get(equations.get(i).get(0));
            int to = nodeToIndexMap.get(equations.get(i).get(1));

            adjMatrix[from][to] = values[i];
            adjMatrix[to][from] = 1.0 / values[i];
        }

        // --- Step 3: Floyd-Warshall, multiplying ratios along the path ---
        for (int via = 0; via < nodes; via++) {
            for (int from = 0; from < nodes; from++) {
                for (int to = 0; to < nodes; to++) {
                    // only bridge when both halves of the chain are known
                    if (adjMatrix[from][via] != 0.0 && adjMatrix[via][to] != 0.0 && adjMatrix[from][to] == 0d) {
                        adjMatrix[from][to] = adjMatrix[from][via] * adjMatrix[via][to];
                    }
                }
            }
        }

        // --- Step 4: resolve each query via a single lookup -------------
        double[] results = new double[queries.size()];

        for (int i = 0; i < queries.size(); i++) {
            Integer from = nodeToIndexMap.get(queries.get(i).get(0));
            Integer to = nodeToIndexMap.get(queries.get(i).get(1));

            if (from == null || to == null || adjMatrix[from][to] == 0.0) {
                results[i] = -1.0; // unknown node or no path
            } else {
                results[i] = adjMatrix[from][to];
            }
        }
        return results;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        EvaluateDivision solver = new EvaluateDivision();

        List<List<String>> equations = Arrays.asList(
                Arrays.asList("a", "b"), Arrays.asList("b", "c"));
        double[] values = {2.0, 3.0};
        List<List<String>> queries = Arrays.asList(
                Arrays.asList("a", "c"), Arrays.asList("b", "a"),
                Arrays.asList("a", "e"), Arrays.asList("a", "a"),
                Arrays.asList("x", "x"));

        double[] expected = {6.0, 0.5, -1.0, 1.0, -1.0};
        double[] gotBFS = solver.evaluateEquationsBFS(equations, values, queries);
        double[] gotFloyd = solver.evaluateEquations(equations, values, queries);
        System.out.printf("queries=%s%n", queries);
        System.out.printf("BFS   →  %s  expected=%s%n",
                Arrays.toString(gotBFS), Arrays.toString(expected));
        System.out.printf("Floyd →  %s  expected=%s%n",
                Arrays.toString(gotFloyd), Arrays.toString(expected));
    }
}


package graphs;

import java.util.*;

/**
 * Problem: All Paths From Source to Target
 *
 * Given a directed acyclic graph with nodes 0 through n - 1, return every path
 * that starts at node 0 and ends at node n - 1. The graph is already given as
 * adjacency lists, and the answer can be returned in any order.
 *
 * Leetcode: https://leetcode.com/problems/all-paths-from-source-to-target/ (Medium)
 * Rating:   1383 (zerotrac Elo)
 * Pattern:  Graph | DFS | Backtracking on a DAG
 *
 * Example:
 *   Input:  graph = [[1,2],[3],[3],[]]
 *   Output: [[0,1,3], [0,2,3]]
 *   Why:    from node 0 we can go through node 1 or node 2, and both routes end
 *           at node 3; there are no other outgoing choices that reach the target.
 *
 * Follow-ups:
 *   1. What if the graph may contain cycles?
 *      Track nodes on the current path and skip them so DFS cannot loop forever.
 *   2. What if only the number of paths is needed?
 *      Use memoized DFS on the DAG and return counts instead of materialized paths.
 *   3. What if the path list is too large to store?
 *      Stream paths to a callback or iterator as soon as each target path is found.
 *
 * Related: Path Sum II (113), All Paths From Source Lead to Destination (1059).
 */
public class AllPathsFromSourceToTarget {


    public static void main(String[] args) {
        AllPathsFromSourceToTarget solver = new AllPathsFromSourceToTarget();
        int[][][] inputs = {
            {{1, 2}, {3}, {3}, {}},
            {{}}
        };
        String[] expected = {
            "[[0, 1, 3], [0, 2, 3]]",
            "[[0]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Integer>> output = solver.allPathsSourceTarget(inputs[i]);
            System.out.printf("graph=%s  ->  %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: a path is built one vertex at a time from source 0. DFS keeps the
     * current path in order; whenever it reaches the target, that path is a complete
     * answer and a copy is stored. Because the graph is a DAG, the recursion can
     * explore each outgoing edge without a visited set.
     *
     * Algorithm:
     *   1. Start a path list with source node 0.
     *   2. Recursively try every neighbor from the current node, appending it first.
     *   3. When the current node is the target, copy the path into the result list.
     *   4. After each recursive call, remove the last node to backtrack to the prefix.
     *
     * Time:  O(2^n * n) - a DAG can contain exponentially many source-to-target paths, and copying each path costs up to n.
     * Space: O(n) - the recursion stack and active path hold at most one path length.
     *
     * @param graph adjacency list where graph[i] contains outgoing neighbors of i
     * @return all paths from node 0 to node graph.length - 1
     */
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path = new ArrayList<>();

        path.add(0);
        dfs(graph, 0, graph.length - 1, path, result);

        return result;
    }

    // DFS helper with backtracking
    private void dfs(int[][] graph, int node, int target, List<Integer> path, List<List<Integer>> result) {
        if (node == target) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int neighbor : graph[node]) {
            path.add(neighbor);
            dfs(graph, neighbor, target, path, result);
            path.remove(path.size() - 1); // backtrack
        }
    }

    /**
     * Alternative iterative approach using explicit stack.
     * Better for handling deep recursion scenarios.
     */
    public List<List<Integer>> allPathsSourceTargetIterative(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        int target = graph.length - 1;

        // Stack stores pairs of (current_node, current_path)
        Stack<PathState> stack = new Stack<>();
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(0);
        stack.push(new PathState(0, initialPath));

        while (!stack.isEmpty()) {
            PathState current = stack.pop();

            if (current.node == target) {
                result.add(current.path);
                continue;
            }

            for (int neighbor : graph[current.node]) {
                List<Integer> newPath = new ArrayList<>(current.path);
                newPath.add(neighbor);
                stack.push(new PathState(neighbor, newPath));
            }
        }

        return result;
    }

    // Helper class for iterative approach
    private static class PathState {
        int node;
        List<Integer> path;

        PathState(int node, List<Integer> path) {
            this.node = node;
            this.path = path;
        }
    }

    /**
     * Memory-optimized approach using DFS with path reconstruction.
     * Only stores indices during traversal, reconstructs paths at the end.
     */
    public List<List<Integer>> allPathsSourceTargetOptimized(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        int[] path = new int[graph.length];

        dfsOptimized(graph, 0, 0, path, result);
        return result;
    }

    // DFS with array-based path tracking
    private void dfsOptimized(int[][] graph, int node, int pathIndex, int[] path, List<List<Integer>> result) {
        path[pathIndex] = node;

        if (node == graph.length - 1) {
            List<Integer> currentPath = new ArrayList<>();
            for (int i = 0; i <= pathIndex; i++) {
                currentPath.add(path[i]);
            }
            result.add(currentPath);
            return;
        }

        for (int neighbor : graph[node]) {
            dfsOptimized(graph, neighbor, pathIndex + 1, path, result);
        }
    }
}

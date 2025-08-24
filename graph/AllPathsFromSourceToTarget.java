package graph;

import java.util.*;

/**
 * 797. All Paths From Source to Target
 *
 * Problem: Given a directed acyclic graph (DAG) with n nodes labeled from 0 to n-1,
 * find all possible paths from node 0 to node n-1.
 *
 * Example:
 * Input: graph = [[1,2],[3],[3],[]]
 * Output: [[0,1,3],[0,2,3]]
 * Explanation: There are two paths from 0 to 3: 0->1->3 and 0->2->3
 *
 * LeetCode: https://leetcode.com/problems/all-paths-from-source-to-target
 *
 * Follow-up questions:
 * Q: What if the graph has cycles?
 * A: Need to use visited array to avoid infinite loops, but problem guarantees DAG.
 *
 * Q: Can we optimize for space if we only need path count?
 * A: Yes, use DP to count paths without storing actual paths.
 *
 * Q: How to handle very large graphs?
 * A: Use iterative DFS with explicit stack, or implement path streaming.
 */
public class AllPathsFromSourceToTarget {

    /**
     * Finds all paths from source node 0 to target node n-1 in a DAG.
     *
     * Algorithm: DFS with backtracking
     * - Start from node 0 and explore all neighbors
     * - When reaching target (n-1), add current path to result
     * - Backtrack by removing current node from path
     * - No visited array needed since graph is DAG (no cycles)
     *
     * Time Complexity: O(2^N * N) where N is number of nodes
     * Space Complexity: O(2^N * N) for storing all paths, O(N) for recursion stack
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
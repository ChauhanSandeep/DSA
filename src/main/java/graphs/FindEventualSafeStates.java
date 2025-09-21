package graphs;

import java.util.*;

/**
 * 802. Find Eventual Safe States
 *
 * Problem: There is a directed graph of n nodes numbered from 0 to n-1.
 * A node is a terminal node if there are no outgoing edges. A node is a safe node
 * if every possible path starting from that node leads to a terminal node.
 * Return an array of all safe nodes sorted in ascending order.
 *
 * Example:
 * Input: graph = [[1,2],[2,3],[5],[0],[5],[],[]]
 * Output: [2,4,5,6]
 *
 * LeetCode: https://leetcode.com/problems/find-eventual-safe-states
 *
 * Follow-up questions:
 * Q: How to detect if there are cycles efficiently?
 * A: Use DFS with three colors (white, gray, black) or topological sort.
 *
 * Q: Can we optimize for very large graphs?
 * A: Use iterative DFS or BFS with explicit stack to avoid recursion limits.
 *
 * Q: How to handle dynamic updates to the graph?
 * A: Maintain incremental data structures or recompute affected components.
 */
public class FindEventualSafeStates {

    /**
     * DFS-based solution using three-color approach.
     *
     * Algorithm: Cycle detection with state tracking
     * - White (0): Unvisited node
     * - Gray (1): Currently in DFS path (potential cycle)
     * - Black (2): Completely processed, safe node
     * - If we revisit a gray node, there's a cycle
     *
     * Time Complexity: O(V + E) where V is nodes, E is edges
     * Space Complexity: O(V) for recursion stack and color array
     */
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n]; // 0: white, 1: gray, 2: black
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (isSafe(graph, i, color)) {
                result.add(i);
            }
        }

        return result;
    }

    // Check if node is safe (no cycles in any path from this node)
    private boolean isSafe(int[][] graph, int node, int[] color) {
        if (color[node] != 0) {
            return color[node] == 2; // Return true if black (safe)
        }

        color[node] = 1; // Mark as gray (in current path)

        for (int neighbor : graph[node]) {
            if (!isSafe(graph, neighbor, color)) {
                return false; // Cycle detected
            }
        }

        color[node] = 2; // Mark as black (safe)
        return true;
    }

    /**
     * Reverse graph + topological sort approach.
     * More intuitive: safe nodes are those that can reach terminal nodes.
     */
    public List<Integer> eventualSafeNodesTopological(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> reverseGraph = new ArrayList<>();
        int[] indegree = new int[n];

        // Initialize reverse graph
        for (int i = 0; i < n; i++) {
            reverseGraph.add(new ArrayList<>());
        }

        // Build reverse graph and calculate indegrees
        for (int i = 0; i < n; i++) {
            for (int neighbor : graph[i]) {
                reverseGraph.get(neighbor).add(i);
                indegree[i]++;
            }
        }

        // Start with terminal nodes (indegree 0 in original graph)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        boolean[] safe = new boolean[n];

        // Process nodes that can reach terminal nodes
        while (!queue.isEmpty()) {
            int node = queue.poll();
            safe[node] = true;

            for (int parent : reverseGraph.get(node)) {
                indegree[parent]--;
                if (indegree[parent] == 0) {
                    queue.offer(parent);
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (safe[i]) {
                result.add(i);
            }
        }

        return result;
    }

    /**
     * Iterative DFS approach to avoid recursion stack overflow.
     * Uses explicit stack for large graphs.
     */
    public List<Integer> eventualSafeNodesIterative(int[][] graph) {
        int n = graph.length;
        int[] state = new int[n]; // 0: unvisited, 1: visiting, 2: safe, 3: unsafe
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (isNodeSafeIterative(graph, i, state)) {
                result.add(i);
            }
        }

        return result;
    }

    // Iterative DFS to check if node is safe
    private boolean isNodeSafeIterative(int[][] graph, int startNode, int[] state) {
        if (state[startNode] != 0) {
            return state[startNode] == 2;
        }

        Stack<Integer> stack = new Stack<>();
        Stack<Integer> path = new Stack<>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            int node = stack.peek();

            if (state[node] == 1) {
                // Already visiting - cycle detected
                markPathAsUnsafe(path, state, node);
                return false;
            }

            if (state[node] != 0) {
                stack.pop();
                if (state[node] == 3) {
                    return false;
                }
                continue;
            }

            state[node] = 1; // Mark as visiting
            path.push(node);
            boolean allNeighborsSafe = true;

            for (int neighbor : graph[node]) {
                if (state[neighbor] == 0) {
                    stack.push(neighbor);
                    allNeighborsSafe = false;
                } else if (state[neighbor] == 1 || state[neighbor] == 3) {
                    markPathAsUnsafe(path, state, node);
                    return false;
                }
            }

            if (allNeighborsSafe) {
                state[node] = 2; // Mark as safe
                path.pop();
                stack.pop();
            }
        }

        return state[startNode] == 2;
    }

    // Mark all nodes in current path as unsafe
    private void markPathAsUnsafe(Stack<Integer> path, int[] state, int fromNode) {
        while (!path.isEmpty()) {
            int node = path.pop();
            state[node] = 3; // Mark as unsafe
            if (node == fromNode) {
                break;
            }
        }
    }

    /**
     * Union-Find approach for connected components analysis.
     * Groups nodes and identifies safe components.
     */
    public List<Integer> eventualSafeNodesUnionFind(int[][] graph) {
        int n = graph.length;
        UnionFind uf = new UnionFind(n);
        boolean[] hasCycle = new boolean[n];

        // Detect cycles using DFS and mark components
        int[] color = new int[n];
        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                detectCycleAndMark(graph, i, color, hasCycle, uf);
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int root = uf.find(i);
            if (!hasCycle[root]) {
                result.add(i);
            }
        }

        return result;
    }

    // Detect cycle and mark component
    private boolean detectCycleAndMark(int[][] graph, int node, int[] color, boolean[] hasCycle, UnionFind uf) {
        if (color[node] == 2) return false; // Already processed
        if (color[node] == 1) return true;  // Cycle found

        color[node] = 1;
        boolean cyclicComponent = false;

        for (int neighbor : graph[node]) {
            uf.union(node, neighbor);
            if (detectCycleAndMark(graph, neighbor, color, hasCycle, uf)) {
                cyclicComponent = true;
            }
        }

        color[node] = 2;
        if (cyclicComponent) {
            hasCycle[uf.find(node)] = true;
        }

        return cyclicComponent;
    }

    // Union-Find data structure
    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;

            if (rank[px] < rank[py]) {
                parent[px] = py;
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
            } else {
                parent[py] = px;
                rank[px]++;
            }
        }
    }
}
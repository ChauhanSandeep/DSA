package graphs;

import java.util.*;

/**
 * Problem: Find Eventual Safe States
 *
 * In a directed graph, a node is safe if every possible path starting from it
 * eventually reaches a terminal node instead of getting trapped in a cycle.
 * Return all safe nodes in increasing order.
 *
 * Leetcode: https://leetcode.com/problems/find-eventual-safe-states/ (Medium)
 * Rating:   1962 (zerotrac Elo)
 * Pattern:  Graph | DFS coloring | Cycle detection
 *
 * Example:
 *   Input:  graph = [[1,2],[2,3],[5],[0],[5],[],[]]
 *   Output: [2, 4, 5, 6]
 *   Why:    nodes 5 and 6 are terminal, nodes 2 and 4 can only flow to terminal
 *           nodes, and nodes 0, 1, 3 participate in or can reach a cycle.
 *
 * Follow-ups:
 *   1. Solve without recursion?
 *      Reverse the graph and remove terminal nodes with topological BFS.
 *   2. Return unsafe nodes grouped by cycle?
 *      Run strongly connected components and mark cyclic components unsafe.
 *   3. Support edge deletions over time?
 *      Maintain reverse edges and outdegree counts to promote newly terminal nodes.
 *
 * Related: Course Schedule (207), Detect Cycles in 2D Grid (1559).
 */
public class FindEventualSafeStates {


    public static void main(String[] args) {
        FindEventualSafeStates solver = new FindEventualSafeStates();
        int[][][] inputs = {
            {{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}},
            {{}, {0, 2, 3, 4}, {3}, {4}, {}}
        };
        String[] expected = {"[2, 4, 5, 6]", "[0, 1, 2, 3, 4]"};

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> output = solver.eventualSafeNodes(inputs[i]);
            System.out.printf("graph=%s  ->  %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: a node is eventually safe if every path from it ends at a terminal
     * node instead of cycling. DFS colors nodes by state: unvisited, currently in
     * the recursion path, or confirmed safe. Seeing a node already in the current
     * path proves a cycle; finishing all neighbors proves safety.
     *
     * Algorithm:
     *   1. Keep a state array for every node.
     *   2. DFS each node that has not already been resolved.
     *   3. Return false when DFS reaches a node currently in the recursion stack.
     *   4. Mark nodes safe only after every outgoing neighbor is safe, then collect them in order.
     *
     * Time:  O(V+E) - each node and edge is resolved once by DFS.
     * Space: O(V) - state array and recursion stack.
     *
     * @param graph directed adjacency list
     * @return sorted list of eventually safe node indexes
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

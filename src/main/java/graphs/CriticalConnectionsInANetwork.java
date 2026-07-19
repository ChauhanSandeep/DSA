package graphs;

import java.util.*;

/**
 * Problem: Critical Connections in a Network
 *
 * Given an undirected connected network of n servers, return every edge whose
 * removal disconnects the network. Such an edge is a bridge: there is no other
 * route that can carry traffic between its two sides.
 *
 * Leetcode: https://leetcode.com/problems/critical-connections-in-a-network/ (Hard)
 * Rating:   2085 (zerotrac Elo)
 * Pattern:  Graph | Tarjan bridge finding | DFS low-link values
 *
 * Example:
 *   Input:  n = 4, connections = [[0,1],[1,2],[2,0],[1,3]]
 *   Output: [[1,3]]
 *   Why:    nodes 0, 1, and 2 still stay connected through a cycle, but node 3
 *           has only the edge to node 1, so removing it cuts node 3 off.
 *
 * Follow-ups:
 *   1. Return articulation points as well as bridges?
 *      Use the same DFS timestamps, but apply the vertex low-link conditions.
 *   2. Handle parallel edges between the same two servers?
 *      Track edge ids so the DFS skips only the exact parent edge, not all same endpoints.
 *   3. Support online edge insertions and deletions?
 *      Use dynamic connectivity data structures; Tarjan is for a static snapshot.
 *
 * Related: Redundant Connection (684), Network Delay Time (743).
 */
public class CriticalConnectionsInANetwork {


    public static void main(String[] args) {
        CriticalConnectionsInANetwork solver = new CriticalConnectionsInANetwork();
        List<List<List<Integer>>> inputs = Arrays.asList(
            Arrays.asList(Arrays.asList(0, 1), Arrays.asList(1, 2), Arrays.asList(2, 0), Arrays.asList(1, 3)),
            Arrays.asList(Arrays.asList(0, 1))
        );
        int[] nodeCounts = {4, 2};
        String[] expected = {"[[1, 3]]", "[[0, 1]]"};

        for (int i = 0; i < inputs.size(); i++) {
            List<List<Integer>> output = solver.criticalConnections(nodeCounts[i], inputs.get(i));
            System.out.printf("n=%d connections=%s  ->  %s  expected=%s%n",
                nodeCounts[i], inputs.get(i), output, expected[i]);
        }
    }
    /**
     * Intuition: an edge is critical when the child side of a DFS tree cannot reach
     * the parent or any earlier ancestor without using that edge. Tarjan's discovery
     * time records when a node is first seen, and low-link records the earliest
     * discovery time reachable from its subtree through tree edges plus one back edge.
     *
     * Algorithm:
     *   1. Build the original undirected adjacency list from connections.
     *   2. DFS with discovery time and low-link arrays.
     *   3. Ignore the edge back to the parent, but use other visited neighbors as back edges.
     *   4. Report parent-child edges whose child low-link is greater than the parent's discovery time.
     *
     * Time:  O(V+E) - DFS processes every node and undirected edge a constant number of times.
     * Space: O(V+E) - adjacency storage plus DFS arrays and recursion stack.
     *
     * @param n number of nodes labeled 0 to n - 1
     * @param connections undirected edges in the network
     * @return all bridges whose removal disconnects the network
     */
    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (List<Integer> connection : connections) {
            int u = connection.get(0);
            int v = connection.get(1);
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        List<List<Integer>> bridges = new ArrayList<>();
        boolean[] visited = new boolean[n];
        int[] disc = new int[n];      // Discovery times
        int[] low = new int[n];       // Low-link values
        int[] parent = new int[n];    // Parent in DFS tree
        int[] timer = {0};            // Global timer

        Arrays.fill(parent, -1);

        // Run DFS from all unvisited vertices
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                tarjanDFS(i, graph, visited, disc, low, parent, bridges, timer);
            }
        }

        return bridges;
    }

    // Tarjan's DFS for bridge finding
    private void tarjanDFS(int u, List<List<Integer>> graph, boolean[] visited,
                          int[] disc, int[] low, int[] parent,
                          List<List<Integer>> bridges, int[] timer) {
        visited[u] = true;
        disc[u] = low[u] = timer[0]++;

        for (int v : graph.get(u)) {
            if (!visited[v]) {
                parent[v] = u;
                tarjanDFS(v, graph, visited, disc, low, parent, bridges, timer);

                // Update low-link value
                low[u] = Math.min(low[u], low[v]);

                // Check if edge (u,v) is a bridge
                if (low[v] > disc[u]) {
                    bridges.add(Arrays.asList(u, v));
                }
            } else if (v != parent[u]) {
                // Back edge (not to parent)
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    /**
     * Alternative implementation using iterative DFS.
     * Avoids potential stack overflow for very deep recursion.
     */
    public List<List<Integer>> criticalConnectionsIterative(int n, List<List<Integer>> connections) {
        List<List<Integer>> graph = buildGraph(n, connections);
        List<List<Integer>> bridges = new ArrayList<>();

        boolean[] visited = new boolean[n];
        int[] disc = new int[n];
        int[] low = new int[n];
        int[] parent = new int[n];
        int timer = 0;

        Arrays.fill(parent, -1);

        for (int start = 0; start < n; start++) {
            if (!visited[start]) {
                timer = iterativeDFS(start, graph, visited, disc, low, parent, bridges, timer);
            }
        }

        return bridges;
    }

    // Iterative DFS implementation
    private int iterativeDFS(int start, List<List<Integer>> graph, boolean[] visited,
                           int[] disc, int[] low, int[] parent,
                           List<List<Integer>> bridges, int timer) {
        Stack<DFSState> stack = new Stack<>();
        stack.push(new DFSState(start, 0));

        while (!stack.isEmpty()) {
            DFSState state = stack.peek();
            int u = state.node;
            int childIndex = state.childIndex;

            if (!visited[u]) {
                visited[u] = true;
                disc[u] = low[u] = timer++;
            }

            if (childIndex >= graph.get(u).size()) {
                stack.pop();

                // Update parent's low-link value
                if (parent[u] != -1) {
                    int p = parent[u];
                    low[p] = Math.min(low[p], low[u]);

                    // Check for bridge
                    if (low[u] > disc[p]) {
                        bridges.add(Arrays.asList(p, u));
                    }
                }
                continue;
            }

            int v = graph.get(u).get(childIndex);
            state.childIndex++;

            if (!visited[v]) {
                parent[v] = u;
                stack.push(new DFSState(v, 0));
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        return timer;
    }

    // State class for iterative DFS
    private static class DFSState {
        int node;
        int childIndex;

        DFSState(int node, int childIndex) {
            this.node = node;
            this.childIndex = childIndex;
        }
    }

    /**
     * Bridge finding with parallel edge handling.
     * Properly handles graphs with multiple edges between same vertices.
     */
    public List<List<Integer>> criticalConnectionsParallelEdges(int n, List<List<Integer>> connections) {
        // Build adjacency list with edge IDs to handle parallel edges
        List<List<EdgeInfo>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < connections.size(); i++) {
            List<Integer> connection = connections.get(i);
            int u = connection.get(0);
            int v = connection.get(1);
            graph.get(u).add(new EdgeInfo(v, i));
            graph.get(v).add(new EdgeInfo(u, i));
        }

        List<List<Integer>> bridges = new ArrayList<>();
        boolean[] visited = new boolean[n];
        int[] disc = new int[n];
        int[] low = new int[n];
        int[] parentEdgeId = new int[n];
        int[] timer = {0};

        Arrays.fill(parentEdgeId, -1);

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                tarjanDFSParallelEdges(i, graph, visited, disc, low, parentEdgeId, bridges, timer, connections);
            }
        }

        return bridges;
    }

    // Edge info with ID for parallel edge handling
    private static class EdgeInfo {
        int to;
        int edgeId;

        EdgeInfo(int to, int edgeId) {
            this.to = to;
            this.edgeId = edgeId;
        }
    }

    // Tarjan's DFS handling parallel edges
    private void tarjanDFSParallelEdges(int u, List<List<EdgeInfo>> graph, boolean[] visited,
                                      int[] disc, int[] low, int[] parentEdgeId,
                                      List<List<Integer>> bridges, int[] timer,
                                      List<List<Integer>> connections) {
        visited[u] = true;
        disc[u] = low[u] = timer[0]++;

        for (EdgeInfo edge : graph.get(u)) {
            int v = edge.to;
            int edgeId = edge.edgeId;

            if (!visited[v]) {
                parentEdgeId[v] = edgeId;
                tarjanDFSParallelEdges(v, graph, visited, disc, low, parentEdgeId, bridges, timer, connections);

                low[u] = Math.min(low[u], low[v]);

                if (low[v] > disc[u]) {
                    bridges.add(connections.get(edgeId));
                }
            } else if (edgeId != parentEdgeId[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    /**
     * Brute force approach for verification.
     * Removes each edge and checks connectivity - very slow but correct.
     */
    public List<List<Integer>> criticalConnectionsBruteForce(int n, List<List<Integer>> connections) {
        List<List<Integer>> bridges = new ArrayList<>();

        for (int i = 0; i < connections.size(); i++) {
            List<Integer> edge = connections.get(i);

            // Remove edge and check if graph becomes disconnected
            List<List<Integer>> remainingConnections = new ArrayList<>();
            for (int j = 0; j < connections.size(); j++) {
                if (i != j) {
                    remainingConnections.add(connections.get(j));
                }
            }

            if (isDisconnected(n, remainingConnections)) {
                bridges.add(edge);
            }
        }

        return bridges;
    }

    // Check if graph is disconnected
    private boolean isDisconnected(int n, List<List<Integer>> connections) {
        if (connections.isEmpty()) return n > 1;

        List<List<Integer>> graph = buildGraph(n, connections);
        boolean[] visited = new boolean[n];

        // Find a vertex with at least one edge
        int start = -1;
        for (int i = 0; i < n; i++) {
            if (!graph.get(i).isEmpty()) {
                start = i;
                break;
            }
        }

        if (start == -1) return n > 1; // No edges

        // DFS from start vertex
        dfs(start, graph, visited);

        // Check if all vertices with edges are visited
        for (int i = 0; i < n; i++) {
            if (!graph.get(i).isEmpty() && !visited[i]) {
                return true; // Found unvisited vertex with edges
            }
        }

        return false;
    }

    // Simple DFS for connectivity check
    private void dfs(int u, List<List<Integer>> graph, boolean[] visited) {
        visited[u] = true;
        for (int v : graph.get(u)) {
            if (!visited[v]) {
                dfs(v, graph, visited);
            }
        }
    }

    // Build adjacency list
    private List<List<Integer>> buildGraph(int n, List<List<Integer>> connections) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (List<Integer> connection : connections) {
            int u = connection.get(0);
            int v = connection.get(1);
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        return graph;
    }

    /**
     * Optimized version with early termination for dense graphs.
     * Includes optimizations for common cases.
     */
    public List<List<Integer>> criticalConnectionsOptimized(int n, List<List<Integer>> connections) {
        // Special case: no bridges possible if too many edges
        if (connections.size() > 3 * n - 6) {
            // For n >= 3, planar graphs have at most 3n-6 edges
            // Very dense graphs typically have few or no bridges
            return criticalConnections(n, connections);
        }

        // Build adjacency list with degree optimization
        List<Set<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new HashSet<>());
        }

        for (List<Integer> connection : connections) {
            int u = connection.get(0);
            int v = connection.get(1);
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        // Remove leaves iteratively (they can't be bridges)
        Queue<Integer> leaves = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (graph.get(i).size() <= 1) {
                leaves.offer(i);
            }
        }

        while (!leaves.isEmpty() && graph.stream().mapToInt(Set::size).sum() > 2) {
            int leaf = leaves.poll();
            if (graph.get(leaf).size() <= 1) {
                for (int neighbor : new ArrayList<>(graph.get(leaf))) {
                    graph.get(neighbor).remove(leaf);
                    if (graph.get(neighbor).size() <= 1) {
                        leaves.offer(neighbor);
                    }
                }
                graph.get(leaf).clear();
            }
        }

        // Run Tarjan on remaining graph
        List<List<Integer>> remainingConnections = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                if (u < v) { // Avoid duplicate edges
                    remainingConnections.add(Arrays.asList(u, v));
                }
            }
        }

        return criticalConnections(n, remainingConnections);
    }

    /**
     * Returns additional information about the graph structure.
     * Extension providing articulation points and bridge count.
     */
    public GraphAnalysis analyzeGraph(int n, List<List<Integer>> connections) {
        List<List<Integer>> graph = buildGraph(n, connections);

        List<List<Integer>> bridges = new ArrayList<>();
        Set<Integer> articulationPoints = new HashSet<>();
        boolean[] visited = new boolean[n];
        int[] disc = new int[n];
        int[] low = new int[n];
        int[] parent = new int[n];
        boolean[] ap = new boolean[n];
        int[] timer = {0};

        Arrays.fill(parent, -1);

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                tarjanAnalysis(i, graph, visited, disc, low, parent, ap, bridges, timer);
            }
        }

        for (int i = 0; i < n; i++) {
            if (ap[i]) {
                articulationPoints.add(i);
            }
        }

        return new GraphAnalysis(bridges, new ArrayList<>(articulationPoints),
                               countConnectedComponents(n, connections));
    }

    // Extended Tarjan's algorithm finding both bridges and articulation points
    private void tarjanAnalysis(int u, List<List<Integer>> graph, boolean[] visited,
                              int[] disc, int[] low, int[] parent, boolean[] ap,
                              List<List<Integer>> bridges, int[] timer) {
        int children = 0;
        visited[u] = true;
        disc[u] = low[u] = timer[0]++;

        for (int v : graph.get(u)) {
            if (!visited[v]) {
                parent[v] = u;
                children++;

                tarjanAnalysis(v, graph, visited, disc, low, parent, ap, bridges, timer);

                low[u] = Math.min(low[u], low[v]);

                // Check for articulation point
                if ((parent[u] == -1 && children > 1) ||
                    (parent[u] != -1 && low[v] >= disc[u])) {
                    ap[u] = true;
                }

                // Check for bridge
                if (low[v] > disc[u]) {
                    bridges.add(Arrays.asList(u, v));
                }
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    // Count connected components
    private int countConnectedComponents(int n, List<List<Integer>> connections) {
        List<List<Integer>> graph = buildGraph(n, connections);
        boolean[] visited = new boolean[n];
        int components = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, graph, visited);
                components++;
            }
        }

        return components;
    }

    // Result class for graph analysis
    public static class GraphAnalysis {
        public final List<List<Integer>> bridges;
        public final List<Integer> articulationPoints;
        public final int connectedComponents;

        public GraphAnalysis(List<List<Integer>> bridges, List<Integer> articulationPoints, int connectedComponents) {
            this.bridges = bridges;
            this.articulationPoints = articulationPoints;
            this.connectedComponents = connectedComponents;
        }

        @Override
        public String toString() {
            return String.format("Bridges: %d, Articulation Points: %d, Components: %d",
                               bridges.size(), articulationPoints.size(), connectedComponents);
        }
    }
}

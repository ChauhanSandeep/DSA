package graph;

import java.util.*;

/**
 * Maximum Flow - Ford-Fulkerson Algorithm with Edmonds-Karp Implementation
 *
 * Problem: Given a flow network (directed graph with edge capacities),
 * find the maximum flow from source to sink.
 *
 * Applications:
 * - Network routing and bandwidth optimization
 * - Bipartite matching problems
 * - Min-cut max-flow theorem applications
 * - Image segmentation and computer vision
 *
 * Follow-up questions:
 * Q: How to handle minimum cost maximum flow?
 * A: Use successive shortest path algorithm or cycle-canceling methods.
 *
 * Q: Can we solve maximum flow in distributed systems?
 * A: Use push-relabel algorithms adapted for distributed computing.
 *
 * Q: How to extend to multi-commodity flows?
 * A: Formulate as linear programming problem or use specialized algorithms.
 */
public class MaximumFlow {

    /**
     * Edmonds-Karp Algorithm (Ford-Fulkerson with BFS).
     *
     * Algorithm: Augmenting path with BFS
     * - Use BFS to find shortest augmenting path from source to sink
     * - Update residual graph by subtracting flow from forward edges
     * - Add reverse edges with the flow value for potential backtracking
     * - Repeat until no augmenting path exists
     *
     * Time Complexity: O(V*E²) where V is vertices, E is edges
     * Space Complexity: O(V²) for adjacency matrix representation
     */
    public int maxFlow(int[][] capacity, int source, int sink) {
        int n = capacity.length;

        // Create residual graph (copy of capacity matrix)
        int[][] residual = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(capacity[i], 0, residual[i], 0, n);
        }

        int[] parent = new int[n];
        int maxFlowValue = 0;

        // Find augmenting paths using BFS
        while (bfs(residual, source, sink, parent)) {
            // Find minimum residual capacity along the path
            int pathFlow = Integer.MAX_VALUE;

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residual[u][v]);
            }

            // Update residual capacities
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow; // Forward edge
                residual[v][u] += pathFlow; // Backward edge
            }

            maxFlowValue += pathFlow;
        }

        return maxFlowValue;
    }

    // BFS to find augmenting path
    private boolean bfs(int[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    queue.offer(v);
                    visited[v] = true;
                    parent[v] = u;

                    if (v == sink) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Dinic's Algorithm - More efficient for dense graphs.
     *
     * Algorithm: Level graph and blocking flows
     * - Build level graph using BFS
     * - Find blocking flow using DFS
     * - Repeat until no augmenting path in level graph
     *
     * Time Complexity: O(V²*E) which is better for dense graphs
     */
    public int maxFlowDinic(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        DinicGraph graph = new DinicGraph(capacity);

        int maxFlowValue = 0;

        while (graph.buildLevelGraph(source, sink)) {
            int[] start = new int[n];
            int flow;

            while ((flow = graph.findBlockingFlow(source, sink, Integer.MAX_VALUE, start)) > 0) {
                maxFlowValue += flow;
            }
        }

        return maxFlowValue;
    }

    // Dinic's algorithm helper class
    private static class DinicGraph {
        int[][] capacity;
        int[][] residual;
        int[] level;
        int n;

        DinicGraph(int[][] capacity) {
            this.n = capacity.length;
            this.capacity = capacity;
            this.residual = new int[n][n];
            this.level = new int[n];

            for (int i = 0; i < n; i++) {
                System.arraycopy(capacity[i], 0, residual[i], 0, n);
            }
        }

        boolean buildLevelGraph(int source, int sink) {
            Arrays.fill(level, -1);
            level[source] = 0;

            Queue<Integer> queue = new LinkedList<>();
            queue.offer(source);

            while (!queue.isEmpty()) {
                int u = queue.poll();

                for (int v = 0; v < n; v++) {
                    if (level[v] == -1 && residual[u][v] > 0) {
                        level[v] = level[u] + 1;
                        queue.offer(v);
                    }
                }
            }

            return level[sink] != -1;
        }

        int findBlockingFlow(int u, int sink, int flow, int[] start) {
            if (u == sink) return flow;

            for (int v = start[u]; v < n; v++) {
                if (level[v] == level[u] + 1 && residual[u][v] > 0) {
                    int minFlow = Math.min(flow, residual[u][v]);
                    int pushed = findBlockingFlow(v, sink, minFlow, start);

                    if (pushed > 0) {
                        residual[u][v] -= pushed;
                        residual[v][u] += pushed;
                        return pushed;
                    }
                }
                start[u]++;
            }

            return 0;
        }
    }

    /**
     * Push-Relabel Algorithm - Better for certain graph types.
     *
     * Algorithm: Local optimizations with height functions
     * - Initialize preflow by saturating all edges from source
     * - Maintain height function and excess flow at nodes
     * - Push excess flow towards sink using height gradients
     * - Relabel nodes when no valid push operations exist
     */
    public int maxFlowPushRelabel(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        PushRelabelGraph graph = new PushRelabelGraph(capacity, source, sink);

        // Initialize preflow
        graph.initializePreflow();

        // Process active nodes until none remain
        while (graph.hasActiveNode()) {
            int u = graph.getActiveNode();

            if (!graph.push(u)) {
                graph.relabel(u);
            }
        }

        return graph.getMaxFlow();
    }

    // Push-relabel helper class
    private static class PushRelabelGraph {
        int[][] capacity, residual;
        int[] height, excess;
        boolean[] active;
        Queue<Integer> activeNodes;
        int n, source, sink;

        PushRelabelGraph(int[][] capacity, int source, int sink) {
            this.n = capacity.length;
            this.capacity = capacity;
            this.source = source;
            this.sink = sink;
            this.residual = new int[n][n];
            this.height = new int[n];
            this.excess = new int[n];
            this.active = new boolean[n];
            this.activeNodes = new LinkedList<>();

            for (int i = 0; i < n; i++) {
                System.arraycopy(capacity[i], 0, residual[i], 0, n);
            }
        }

        void initializePreflow() {
            height[source] = n;

            for (int v = 0; v < n; v++) {
                if (capacity[source][v] > 0) {
                    residual[source][v] = 0;
                    residual[v][source] = capacity[source][v];
                    excess[v] = capacity[source][v];

                    if (v != sink) {
                        activeNodes.offer(v);
                        active[v] = true;
                    }
                }
            }
        }

        boolean hasActiveNode() {
            return !activeNodes.isEmpty();
        }

        int getActiveNode() {
            int u = activeNodes.poll();
            active[u] = false;
            return u;
        }

        boolean push(int u) {
            for (int v = 0; v < n; v++) {
                if (residual[u][v] > 0 && height[u] == height[v] + 1) {
                    int flow = Math.min(excess[u], residual[u][v]);

                    residual[u][v] -= flow;
                    residual[v][u] += flow;
                    excess[u] -= flow;
                    excess[v] += flow;

                    if (v != source && v != sink && !active[v] && excess[v] == flow) {
                        activeNodes.offer(v);
                        active[v] = true;
                    }

                    if (excess[u] == 0) return true;
                }
            }
            return false;
        }

        void relabel(int u) {
            int minHeight = Integer.MAX_VALUE;

            for (int v = 0; v < n; v++) {
                if (residual[u][v] > 0) {
                    minHeight = Math.min(minHeight, height[v]);
                }
            }

            height[u] = minHeight + 1;

            if (excess[u] > 0) {
                activeNodes.offer(u);
                active[u] = true;
            }
        }

        int getMaxFlow() {
            int flow = 0;
            for (int v = 0; v < n; v++) {
                flow += residual[v][source];
            }
            return flow;
        }
    }

    /**
     * Min-Cut computation using max-flow.
     * Finds minimum cut that separates source from sink.
     */
    public MinCutResult findMinCut(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = new int[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(capacity[i], 0, residual[i], 0, n);
        }

        int maxFlowValue = maxFlow(capacity, source, sink);

        // Find reachable nodes from source in residual graph
        boolean[] reachable = new boolean[n];
        dfsReachable(residual, source, reachable);

        // Identify cut edges
        List<Edge> cutEdges = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            if (reachable[u]) {
                for (int v = 0; v < n; v++) {
                    if (!reachable[v] && capacity[u][v] > 0) {
                        cutEdges.add(new Edge(u, v, capacity[u][v]));
                    }
                }
            }
        }

        Set<Integer> sourceSet = new HashSet<>();
        Set<Integer> sinkSet = new HashSet<>();

        for (int i = 0; i < n; i++) {
            if (reachable[i]) {
                sourceSet.add(i);
            } else {
                sinkSet.add(i);
            }
        }

        return new MinCutResult(maxFlowValue, cutEdges, sourceSet, sinkSet);
    }

    private void dfsReachable(int[][] residual, int u, boolean[] reachable) {
        reachable[u] = true;

        for (int v = 0; v < residual.length; v++) {
            if (!reachable[v] && residual[u][v] > 0) {
                dfsReachable(residual, v, reachable);
            }
        }
    }

    // Edge representation
    public static class Edge {
        public final int from, to, capacity;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }

        @Override
        public String toString() {
            return String.format("(%d -> %d, cap: %d)", from, to, capacity);
        }
    }

    // Min-cut result
    public static class MinCutResult {
        public final int maxFlow;
        public final List<Edge> cutEdges;
        public final Set<Integer> sourceSet;
        public final Set<Integer> sinkSet;

        public MinCutResult(int maxFlow, List<Edge> cutEdges, Set<Integer> sourceSet, Set<Integer> sinkSet) {
            this.maxFlow = maxFlow;
            this.cutEdges = cutEdges;
            this.sourceSet = sourceSet;
            this.sinkSet = sinkSet;
        }

        @Override
        public String toString() {
            return String.format("Max Flow: %d, Cut Edges: %d, Source Set: %s, Sink Set: %s",
                               maxFlow, cutEdges.size(), sourceSet, sinkSet);
        }
    }

    /**
     * Bipartite matching using maximum flow.
     * Solves maximum bipartite matching as max-flow problem.
     */
    public int maxBipartiteMatching(boolean[][] bipartiteGraph) {
        int leftSize = bipartiteGraph.length;
        int rightSize = bipartiteGraph[0].length;
        int n = leftSize + rightSize + 2; // +2 for super source and sink

        int source = n - 2;
        int sink = n - 1;

        // Build flow network
        int[][] capacity = new int[n][n];

        // Connect source to left nodes
        for (int i = 0; i < leftSize; i++) {
            capacity[source][i] = 1;
        }

        // Connect bipartite edges
        for (int i = 0; i < leftSize; i++) {
            for (int j = 0; j < rightSize; j++) {
                if (bipartiteGraph[i][j]) {
                    capacity[i][leftSize + j] = 1;
                }
            }
        }

        // Connect right nodes to sink
        for (int j = 0; j < rightSize; j++) {
            capacity[leftSize + j][sink] = 1;
        }

        return maxFlow(capacity, source, sink);
    }

    /**
     * Multi-source multi-sink maximum flow.
     * Handles multiple sources and sinks by adding super nodes.
     */
    public int maxFlowMultiSourceSink(int[][] capacity, Set<Integer> sources, Set<Integer> sinks) {
        int n = capacity.length;
        int newN = n + 2;
        int superSource = n;
        int superSink = n + 1;

        // Extend capacity matrix
        int[][] newCapacity = new int[newN][newN];
        for (int i = 0; i < n; i++) {
            System.arraycopy(capacity[i], 0, newCapacity[i], 0, n);
        }

        // Connect super source to sources
        for (int source : sources) {
            newCapacity[superSource][source] = Integer.MAX_VALUE;
        }

        // Connect sinks to super sink
        for (int sink : sinks) {
            newCapacity[sink][superSink] = Integer.MAX_VALUE;
        }

        return maxFlow(newCapacity, superSource, superSink);
    }

    /**
     * Minimum cost maximum flow using successive shortest paths.
     * Finds maximum flow with minimum total cost.
     */
    public MinCostMaxFlowResult minCostMaxFlow(int[][] capacity, int[][] cost, int source, int sink) {
        int n = capacity.length;
        int[][] residual = new int[n][n];
        int[][] residualCost = new int[n][n];

        // Initialize residual graph
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                residual[i][j] = capacity[i][j];
                if (capacity[i][j] > 0) {
                    residualCost[i][j] = cost[i][j];
                    residualCost[j][i] = -cost[i][j]; // Reverse edge cost
                }
            }
        }

        int totalFlow = 0;
        int totalCost = 0;
        int[] parent = new int[n];
        int[] dist = new int[n];

        while (spfa(residual, residualCost, source, sink, parent, dist)) {
            // Find minimum capacity along path
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residual[u][v]);
            }

            // Update residual graph and calculate cost
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
                totalCost += pathFlow * residualCost[u][v];
            }

            totalFlow += pathFlow;
        }

        return new MinCostMaxFlowResult(totalFlow, totalCost);
    }

    // SPFA (Shortest Path Faster Algorithm) for min-cost paths
    private boolean spfa(int[][] residual, int[][] cost, int source, int sink, int[] parent, int[] dist) {
        int n = residual.length;
        Arrays.fill(dist, Integer.MAX_VALUE);
        boolean[] inQueue = new boolean[n];

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        dist[source] = 0;
        inQueue[source] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;

            for (int v = 0; v < n; v++) {
                if (residual[u][v] > 0 && dist[u] + cost[u][v] < dist[v]) {
                    dist[v] = dist[u] + cost[u][v];
                    parent[v] = u;

                    if (!inQueue[v]) {
                        queue.offer(v);
                        inQueue[v] = true;
                    }
                }
            }
        }

        return dist[sink] != Integer.MAX_VALUE;
    }

    // Min-cost max-flow result
    public static class MinCostMaxFlowResult {
        public final int maxFlow;
        public final int minCost;

        public MinCostMaxFlowResult(int maxFlow, int minCost) {
            this.maxFlow = maxFlow;
            this.minCost = minCost;
        }

        @Override
        public String toString() {
            return String.format("Max Flow: %d, Min Cost: %d", maxFlow, minCost);
        }
    }

    /**
     * Flow decomposition - finds flow paths and cycles.
     * Decomposes max flow into path flows and cycles.
     */
    public List<FlowPath> decomposeFlow(int[][] capacity, int[][] flow, int source, int sink) {
        List<FlowPath> paths = new ArrayList<>();
        int n = capacity.length;
        int[][] residualFlow = new int[n][n];

        // Copy flow matrix
        for (int i = 0; i < n; i++) {
            System.arraycopy(flow[i], 0, residualFlow[i], 0, n);
        }

        // Find source-to-sink paths
        while (true) {
            List<Integer> path = new ArrayList<>();
            int pathFlow = findFlowPath(residualFlow, source, sink, path);

            if (pathFlow == 0) break;

            // Update residual flow
            for (int i = 0; i < path.size() - 1; i++) {
                int u = path.get(i);
                int v = path.get(i + 1);
                residualFlow[u][v] -= pathFlow;
            }

            paths.add(new FlowPath(new ArrayList<>(path), pathFlow, false));
        }

        // Find cycles (if any)
        for (int start = 0; start < n; start++) {
            List<Integer> cycle = new ArrayList<>();
            int cycleFlow = findFlowCycle(residualFlow, start, cycle);

            if (cycleFlow > 0) {
                // Update residual flow
                for (int i = 0; i < cycle.size() - 1; i++) {
                    int u = cycle.get(i);
                    int v = cycle.get(i + 1);
                    residualFlow[u][v] -= cycleFlow;
                }

                paths.add(new FlowPath(cycle, cycleFlow, true));
            }
        }

        return paths;
    }

    private int findFlowPath(int[][] flow, int source, int sink, List<Integer> path) {
        boolean[] visited = new boolean[flow.length];
        path.clear();
        path.add(source);

        return dfsFlowPath(flow, source, sink, Integer.MAX_VALUE, visited, path);
    }

    private int dfsFlowPath(int[][] flow, int u, int sink, int minFlow, boolean[] visited, List<Integer> path) {
        if (u == sink) return minFlow;

        visited[u] = true;

        for (int v = 0; v < flow.length; v++) {
            if (!visited[v] && flow[u][v] > 0) {
                path.add(v);
                int result = dfsFlowPath(flow, v, sink, Math.min(minFlow, flow[u][v]), visited, path);

                if (result > 0) return result;

                path.remove(path.size() - 1);
            }
        }

        return 0;
    }

    private int findFlowCycle(int[][] flow, int start, List<Integer> cycle) {
        // Simplified cycle detection - full implementation would be more complex
        return 0; // Placeholder
    }

    // Flow path representation
    public static class FlowPath {
        public final List<Integer> path;
        public final int flow;
        public final boolean isCycle;

        public FlowPath(List<Integer> path, int flow, boolean isCycle) {
            this.path = path;
            this.flow = flow;
            this.isCycle = isCycle;
        }

        @Override
        public String toString() {
            return String.format("%s: %s, flow: %d",
                               isCycle ? "Cycle" : "Path", path, flow);
        }
    }
}
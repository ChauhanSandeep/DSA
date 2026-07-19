package graphs;

import java.util.*;

/**
 * Problem: Reachable Nodes In Subdivided Graph
 *
 * Each original edge [u, v, cnt] is replaced by cnt new nodes placed between u
 * and v. Starting at original node 0 with maxMoves moves, count both original and
 * inserted nodes that can be reached.
 *
 * Leetcode: https://leetcode.com/problems/reachable-nodes-in-subdivided-graph/
 * Rating:   2328 (zerotrac Elo)
 * Pattern:  Graph | Dijkstra | Count partial edge coverage
 *
 * Example:
 *   Input:  edges = [[0,1,10],[0,2,1],[1,2,2]], maxMoves = 6, n = 3
 *   Output: 13
 *   Why:    all three original nodes are reachable; along the subdivided edges,
 *           the two directions together cover 10 + 1 + 2 inserted nodes.
 *
 * Follow-ups:
 *   1. Return which inserted nodes are reachable too?
 *      Store per-edge coverage from each endpoint and expand only that many labels.
 *   2. Edge traversal costs are not one per segment?
 *      Run Dijkstra on weighted segments or keep the edge as a weighted interval.
 *   3. Multiple starting nodes share the same move budget?
 *      Seed Dijkstra with all starts at distance 0 and count coverage from best distances.
 *
 * Related: Network Delay Time (743), Path With Minimum Effort (1631), Swim in Rising Water (778).
 */
public class ReachableNodesInSubdividedGraph {

    public static void main(String[] args) {
        ReachableNodesInSubdividedGraph solver = new ReachableNodesInSubdividedGraph();
        int[][][] edges = {{{0, 1, 0}}, {}};
        int[] moves = {1, 0};
        int[] nodes = {2, 1};
        int[] expected = {2, 1};
        for (int i = 0; i < edges.length; i++) {
            int output = solver.reachableNodes(edges[i], moves[i], nodes[i]);
            System.out.printf("edges=%s M=%d N=%d -> %d  expected=%d%n", Arrays.deepToString(edges[i]), moves[i], nodes[i], output, expected[i]);
        }
    }
    /**
     * Calculates the number of nodes reachable in at most M moves.
     *
     * @param edges Array of edges with subdivision counts
     * @param M Maximum number of moves
     * @param N Number of original nodes
     * @return Total number of reachable nodes
     */
    public int reachableNodes(int[][] edges, int M, int N) {
        // Build adjacency list
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], count = edge[2];
            graph.computeIfAbsent(u, k -> new HashMap<>()).put(v, count);
            graph.computeIfAbsent(v, k -> new HashMap<>()).put(u, count);
        }

        // Priority queue for Dijkstra's algorithm
        // [node, moves_remaining]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        pq.offer(new int[]{0, M});

        // Track the maximum moves remaining for each node
        Map<Integer, Integer> maxMoves = new HashMap<>();
        maxMoves.put(0, M);

        // Track the number of new nodes covered on each edge
        Map<String, Integer> used = new HashMap<>();

        int result = 0;

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int node = current[0];
            int moves = current[1];

            // If we've already processed this node with more moves, skip
            if (maxMoves.getOrDefault(node, -1) > moves) {
                continue;
            }

            // Count this node as reachable
            result++;

            // Process all neighbors
            for (Map.Entry<Integer, Integer> entry : graph.getOrDefault(node, new HashMap<>()).entrySet()) {
                int neighbor = entry.getKey();
                int count = entry.getValue();

                // Calculate how many new nodes we can cover on this edge
                String edgeKey = node + "-" + neighbor;
                int usedNodes = used.getOrDefault(edgeKey, 0);
                int remainingNodes = count - usedNodes;

                // If we have moves left to cover some nodes
                if (moves > usedNodes) {
                    int newMoves = moves - usedNodes - 1;

                    // If we can reach the neighbor with moves remaining
                    if (newMoves > maxMoves.getOrDefault(neighbor, -1)) {
                        maxMoves.put(neighbor, newMoves);
                        pq.offer(new int[]{neighbor, newMoves});
                    }

                    // Update the used nodes on this edge
                    int covered = Math.min(remainingNodes, moves - usedNodes);
                    used.put(edgeKey, usedNodes + covered);

                    // Also update the reverse edge
                    String reverseKey = neighbor + "-" + node;
                    used.put(reverseKey, used.getOrDefault(reverseKey, 0) + covered);
                }
            }
        }

        // Add the new nodes on the edges
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], count = edge[2];
            int a = used.getOrDefault(u + "-" + v, 0);
            int b = used.getOrDefault(v + "-" + u, 0);
            result += Math.min(a + b, count);
        }

        return result;
    }

    /**
     * Alternative solution using BFS with priority queue (Dijkstra's algorithm).
     * This version uses a 2D array for the graph representation.
     */
    public int reachableNodesOptimized(int[][] edges, int M, int N) {
        // Build adjacency matrix
        int[][] graph = new int[N][N];
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], count = edge[2];
            graph[u][v] = count + 1; // +1 to distinguish from 0 (no edge)
            graph[v][u] = count + 1;
        }

        // Priority queue for Dijkstra's: [node, moves_remaining]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        pq.offer(new int[]{0, M});

        // Track max moves remaining for each node
        int[] maxMoves = new int[N];
        Arrays.fill(maxMoves, -1);
        maxMoves[0] = M;

        // Track used new nodes on edges
        int[][] used = new int[N][N];

        int result = 0;

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int node = current[0];
            int moves = current[1];

            // Skip if we've already processed this node with more moves
            if (maxMoves[node] > moves) {
                continue;
            }

            // Count this node as reachable
            result++;

            // Process all neighbors
            for (int neighbor = 0; neighbor < N; neighbor++) {
                if (graph[node][neighbor] == 0) {
                    continue; // No edge between node and neighbor
                }

                int count = graph[node][neighbor] - 1; // Subtract 1 to get original count
                int usedNodes = used[node][neighbor];
                int remainingNodes = count - usedNodes;

                // If we have moves left to cover some nodes
                if (moves > usedNodes) {
                    int newMoves = moves - usedNodes - 1;

                    // If we can reach the neighbor with moves remaining
                    if (newMoves > maxMoves[neighbor]) {
                        maxMoves[neighbor] = newMoves;
                        pq.offer(new int[]{neighbor, newMoves});
                    }

                    // Update the used nodes on this edge
                    int covered = Math.min(remainingNodes, moves - usedNodes);
                    used[node][neighbor] += covered;

                    // Also update the reverse edge
                    used[neighbor][node] += covered;
                }
            }
        }

        // Add the new nodes on the edges
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], count = edge[2];
            result += Math.min(used[u][v] + used[v][u], count);
        }

        return result;
    }
}

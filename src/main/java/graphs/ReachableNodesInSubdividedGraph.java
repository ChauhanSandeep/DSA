package graphs;

import java.util.*;

/**
 * Starting with an undirected graph (the "original graph") with nodes labeled 0 to N-1,
 * subdivisions are made to some of the edges.
 *
 * The graph is given as follows: edges[i] is a list of integer pairs (i, j, n) such that
 * (i, j) is an edge of the original graph, and n is the total number of new nodes on that edge.
 *
 * Then, the edge (i, j) is deleted from the original graph, n new nodes (x_1, x_2, ..., x_n)
 * are added to the original graph, and n+1 new edges (i, x_1), (x_1, x_2), (x_2, x_3), ..., (x_n, j) are added.
 *
 * You will start at node 0, and you can move along the edges of the graph. At each step,
 * you can move to any adjacent node. You can visit each node at most once.
 *
 * Return how many nodes you can reach in at most M moves.
 *
 * Example 1:
 * Input: edges = [[0,1,10],[0,2,1],[1,2,2]], M = 6, N = 3
 * Output: 13
 * Explanation: The nodes that are reachable in the final graph after M = 6 moves are:
 * 0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13
 *
 * Example 2:
 * Input: edges = [[0,1,4],[1,2,6],[0,2,8],[1,3,1]], M = 10, N = 4
 * Output: 23
 *
 * LeetCode: https://leetcode.com/problems/reachable-nodes-in-subdivided-graph/
 *
 * Follow-up Questions:
 * 1. How would you modify the solution if edges could have different weights?
 *    - The Dijkstra's algorithm already handles different edge weights.
 * 2. What if the graph is very large (e.g., 10^5 nodes and edges)?
 *    - The solution uses an adjacency list and priority queue for efficiency.
 * 3. How would you find the actual path that visits the maximum number of nodes?
 *    - We could modify the solution to track the path taken during the BFS/Dijkstra's.
 *
 * Related Problems:
 * - Network Delay Time (https://leetcode.com/problems/network-delay-time/)
 * - Cheapest Flights Within K Stops (https://leetcode.com/problems/cheapest-flights-within-k-stops/)
 * LeetCode Contest Rating: 2328
 */
public class ReachableNodesInSubdividedGraph {
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

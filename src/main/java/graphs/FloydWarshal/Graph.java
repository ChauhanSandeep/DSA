package graph.FloydWarshal;

import java.util.*;

/**
 * You are given an integer n representing the number of nodes in a directed weighted graph.
 * Initially, there are no edges in the graph. You will be adding edges one at a time.
 *
 * Problem Link: https://leetcode.com/problems/design-graph-with-shortest-path-calculator/
 *
 * Steps:
 * 1. Initialize a distance matrix with size n x n, where dist[i][j] represents the shortest distance from node i to node j.
 * 2. Set all distances to infinity (or a large value) except for the diagonal (dist[i][i] = 0).
 * 3. For each edge added, update the distance matrix using the Floyd-Warshall algorithm.
 * 4. For each query, return the shortest distance from node1 to node2.
 *
 * Important Notes:
 * Floyd-Warshall-based solution. This is useful when the frequency of getting shortest paths is high
 * Therefore this the addEdge operation is costly and getting shortest paths is cheap.
 * If the frequency of getting shortest paths is low, then we can use Dijkstra's algorithm for each query.
 *
 * Time Complexity:
 * - addEdge: O(n^2) in the worst case (updating all pairs)
 * - shortestPath: O(1) per query
 *
 * Space Complexity: O(n^2)
 */
public class Graph {

    private static final int INF = (int) 1e9;
    private final int n;
    private final int[][] dist;

    /**
     * Initializes the graph with n nodes and no edges.
     *
     * @param n number of nodes in the graph
     */
    public Graph(int n) {
        this.n = n;
        this.dist = new int[n][n];

        // Initialize distances
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }
    }

    /**
     * Adds a directed edge to the graph and updates shortest paths using Floyd-Warshall logic.
     *
     * @param edge an array of [from, to, weight]
     */
    public void addEdge(int[] edge) {
        int from = edge[0], to = edge[1], weight = edge[2];

        // Update direct edge if it's shorter than any existing path
        if (weight < dist[from][to]) {
            dist[from][to] = weight;

            // Update all-pairs shortest paths
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][from] < INF && dist[to][j] < INF) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][from] + weight + dist[to][j]);
                    }
                }
            }
        }
    }

    /**
     * Returns the shortest path from node1 to node2 if exists, else -1.
     *
     * @param node1 source node
     * @param node2 target node
     * @return shortest distance or -1 if unreachable
     */
    public int shortestPath(int node1, int node2) {
        return dist[node1][node2] == INF ? -1 : dist[node1][node2];
    }
}
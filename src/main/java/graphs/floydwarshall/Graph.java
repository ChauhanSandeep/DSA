package graphs.floydwarshall;

import java.util.*;

/**
 * Problem: Design Graph With Shortest Path Calculator
 *
 * Maintain a directed weighted graph that supports adding edges and querying the
 * shortest path between two nodes. This implementation keeps an all-pairs
 * distance matrix so shortestPath is cheap after each addEdge update.
 *
 * Leetcode: https://leetcode.com/problems/design-graph-with-shortest-path-calculator/ (Hard)
 * Rating:   1811 (zerotrac Elo)
 * Pattern:  Graph design | Floyd-Warshall | Dynamic all-pairs shortest paths
 *
 * Example:
 *   Input:  add 0->1 cost 2, add 1->2 cost 1, query shortestPath(0,2)
 *   Output: 3
 *   Why:    the best known route from 0 to 2 goes through 1 with total cost 2 + 1.
 *
 * Follow-ups:
 *   1. Many edge additions but few shortest-path queries?
 *      Store adjacency lists and run Dijkstra per query instead of updating all pairs on every edge.
 *   2. Need to delete edges?
 *      Recompute all-pairs distances or use a more advanced fully dynamic shortest-path structure.
 *   3. Support negative edges?
 *      Floyd-Warshall can handle them, but negative cycles must be detected and reported.
 *
 * Related: Floyd-Warshall, Dijkstra, Network Delay Time (743).
 */
public class Graph {

    public static void main(String[] args) {
        Graph graph = new Graph(4);
        graph.addEdge(new int[]{0, 1, 2});
        graph.addEdge(new int[]{1, 2, 1});
        System.out.printf("edges=%s query=[0,2] -> %d  expected=3%n",
            Arrays.deepToString(new int[][]{{0, 1, 2}, {1, 2, 1}}), graph.shortestPath(0, 2));
        graph.addEdge(new int[]{3, 0, 3});
        System.out.printf("edges=%s query=[3,2] -> %d  expected=6%n",
            Arrays.deepToString(new int[][]{{0, 1, 2}, {1, 2, 1}, {3, 0, 3}}), graph.shortestPath(3, 2));
    }

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
     * Intuition: adding one directed edge can only improve paths that travel from
     * some node into edge[0], cross the new edge, then travel from edge[1] onward.
     * The distance matrix already knows the best prefix and suffix costs.
     *
     * Algorithm:
     *   1. Ignore the edge if it is not cheaper than the current direct edge.
     *   2. Store the new direct edge weight.
     *   3. Try every source and target pair through this new edge and relax dist.
     *
     * Time:  O(n^2) - all source-target pairs are checked after a useful edge insertion.
     * Space: O(1) - the update uses the existing distance matrix.
     *
     * @param edge directed edge [from, to, weight]
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
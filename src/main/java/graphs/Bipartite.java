package graphs;

import java.util.LinkedList;
import java.util.Queue;


import java.util.Arrays;
/**
 * Problem: Is Graph Bipartite?
 *
 * Given an undirected graph as adjacency lists, decide whether its nodes can be
 * split into two groups so every edge crosses between groups. The graph may be
 * disconnected, so every component must satisfy the same rule.
 *
 * Leetcode: https://leetcode.com/problems/is-graph-bipartite/ (Medium)
 * Rating:   1625 (zerotrac Elo)
 * Pattern:  Graph | BFS coloring | Odd-cycle detection
 *
 * Example:
 *   Input:  graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
 *   Output: false
 *   Why:    nodes 0, 1, and 2 form a triangle, so one edge must connect two nodes
 *           placed in the same group.
 *
 * Follow-ups:
 *   1. Return the two partitions when the graph is bipartite?
 *      Collect nodes by their final color after BFS completes.
 *   2. Find an actual odd cycle when it is not bipartite?
 *      Store parents during BFS and reconstruct the conflict path.
 *   3. Support online edge additions?
 *      Use a union-find with parity bits to detect contradictions incrementally.
 *
 * Related: Possible Bipartition (886), Redundant Connection (684).
 */
public class Bipartite {


    public static void main(String[] args) {
        Bipartite solver = new Bipartite();
        int[][][] inputs = {
            {{1, 3}, {0, 2}, {1, 3}, {0, 2}},
            {{1, 2, 3}, {0, 2}, {0, 1, 3}, {0, 2}}
        };
        boolean[] expected = {true, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean output = solver.isBipartite(inputs[i]);
            System.out.printf("graph=%s  ->  %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: a graph is bipartite when every edge connects opposite colors. BFS
     * assigns a color to an uncolored start node, pushes that color through the
     * component, and rejects the graph if an edge ever points to the same color.
     * Repeating this for unvisited nodes covers disconnected graphs.
     *
     * Algorithm:
     *   1. Keep a color array initialized to uncolored for every node.
     *   2. Start BFS from each uncolored node and assign it the first color.
     *   3. For each neighbor, assign the opposite color when it is uncolored.
     *   4. Return false if any neighbor already has the same color.
     *
     * Time:  O(V+E) - each node and adjacency entry is processed by BFS.
     * Space: O(V) - the color array and queue store graph nodes.
     *
     * @param graph adjacency list for an undirected graph
     * @return true if the graph can be split into two independent sets
     */
  public boolean isBipartite(int[][] graph) {
    int len = graph.length;

    // visited[i] = 0 -> unvisited
    // visited[i] = 1 -> assigned to group 1
    // visited[i] = -1 -> assigned to group -1
    int[] visited = new int[len];

    // There can be multiple disconnected components in the graph, for loop ensures that all the subgraphs are checked
    for (int node = 0; node < len; node++) {
      if (visited[node] != 0) {
        // This node is already visited in previous subgraph
        continue;
      }

      // Start BFS from this node and assign to group 1
      Queue<Integer> queue = new LinkedList<>();
      queue.offer(node);
      visited[node] = 1;

      while (!queue.isEmpty()) {
        int current = queue.poll();
        int currentGroup = visited[current];

        // Traverse all neighbors
        for (int neighbor : graph[current]) {
          if (visited[neighbor] == 0) {
            // Assign to opposite group and enqueue
            visited[neighbor] = -currentGroup;
            queue.offer(neighbor);
          } else if (visited[neighbor] == currentGroup) {
            // Conflict detected: adjacent nodes in same group
            return false;
          }
        }
      }
    }

    return true;
  }
}

package graphs;

import java.util.LinkedList;
import java.util.Queue;


/**
 * LeetCode Link: https://leetcode.com/problems/is-graph-bipartite/
 *
 * Problem Statement:
 * Given an undirected graph, determine whether it is bipartite. A graph is bipartite
 * if its vertices can be divided into two disjoint sets such that every edge connects
 * a vertex from one set to another.
 *
 * Approach:
 * We perform a BFS traversal for each unvisited node, treating each connected component
 * independently. During traversal:
 *   - Assign a group number (+1 or -1) when visiting a node.
 *   - Assign the opposite group number to all its neighbors.
 *   - If a neighbor has already been visited and has the same group number, the graph
 *     is not bipartite.
 *
 * This solution avoids using a separate color array and leverages the visited array
 * directly to encode group membership.
 *
 * Time Complexity: O(V + E)
 * Space Complexity: O(V)
 * LeetCode Contest Rating: 1625
 */
public class Bipartite {

  /**
   * Checks if the given graph is bipartite using BFS and group assignment.
   *
   * @param graph adjacency list representation of an undirected graph
   * @return true if the graph is bipartite, false otherwise
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
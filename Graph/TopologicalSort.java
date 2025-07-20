package Graph;

import java.util.*;


/**
 * Problem: Topological Sort using DFS with Cycle Detection
 *
 * --- Problem Statement ---
 * Given a Directed Graph with `N` nodes, perform Topological Sort.
 * Return a valid topological ordering if possible. If the graph contains a cycle,
 * return an empty array since a topological sort is not possible in cyclic graphs.
 *
 * --- Example ---
 * Input:
 * N = 6, Edges = [
 *   5 -> 2, 5 -> 0,
 *   4 -> 0, 4 -> 1,
 *   2 -> 3, 3 -> 1
 * ]
 * Output: [5, 4, 2, 3, 1, 0] (One possible valid ordering)
 *
 * --- Approach ---
 * - Use **DFS traversal** with **three-color marking** (UNVISITED, VISITING, VISITED) to detect cycles.
 * - Track the visited states to avoid reprocessing.
 * - Store result in postorder (dependencies before dependent).
 * - Reverse the final list to get valid topological order.
 *
 * --- Time Complexity ---
 * O(V + E), where V = number of vertices, E = number of edges.
 *
 * --- Space Complexity ---
 * O(V) for visited array and topological result list.
 *
 * Follow-up Questions:
 * Q: Can we use Kahn's Algorithm (BFS) instead of DFS?
 * A: Yes. Kahn’s Algorithm also works and is more suitable when in-degree tracking is needed.
 *
 * Q: What if the graph has multiple valid topological orders?
 * A: This algorithm will return any one of them based on traversal order.
 *
 * Leetcode link: https://leetcode.com/problems/course-schedule-ii/
 */
public class TopologicalSort {

  private static final int UNVISITED = 0;
  private static final int VISITING = 1;
  private static final int VISITED = 2;

  public static void main(String[] args) {
    int numNodes = 6;
    ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();
    for (int i = 0; i < numNodes; i++) {
      adjacencyList.add(new ArrayList<>());
    }

    // Directed Acyclic Graph (DAG)
    adjacencyList.get(5).add(2);
    adjacencyList.get(5).add(0);
    adjacencyList.get(4).add(0);
    adjacencyList.get(4).add(1);
    adjacencyList.get(2).add(3);
    adjacencyList.get(3).add(1);

    int[] result = performTopologicalSort(numNodes, adjacencyList);
    if (result.length == 0) {
      System.out.println("Cycle detected! Topological sorting not possible.");
    } else {
      System.out.println("Topological Sort Order: " + Arrays.toString(result));
    }
  }

  /**
   * Performs Topological Sort on a directed graph using DFS and cycle detection.
   *
   * --- Steps ---
   * 1. Initialize visited[] with UNVISITED status.
   * 2. For each unvisited node, perform DFS recursively.
   * 3. If a cycle is detected during DFS (back edge found), return empty array.
   * 4. Otherwise, collect postorder traversal of nodes and reverse it.
   *
   * Time: O(V + E)
   * Space: O(V)
   *
   * @param numNodes Total number of nodes (0-based indexing).
   * @param adjList  Adjacency list of the graph.
   * @return Topological order as int[], or empty array if a cycle is found.
   */
  public static int[] performTopologicalSort(int numNodes, ArrayList<ArrayList<Integer>> adjList) {
    int[] visited = new int[numNodes];
    List<Integer> topoOrder = new ArrayList<>();

    for (int node = 0; node < numNodes; node++) {
      if (visited[node] == UNVISITED) {
        boolean isAcyclic = dfsWithCycleDetection(node, visited, adjList, topoOrder);
        if (!isAcyclic) {
          return new int[0]; // Cycle found
        }
      }
    }

    // Reverse the post-order to get topological order
    Collections.reverse(topoOrder);
    return topoOrder.stream().mapToInt(i -> i).toArray();
  }

  /**
   * Recursive DFS with 3-state visited marking to detect cycles and build topo order.
   *
   * @param currentNode Current node being processed
   * @param visited     Status array (UNVISITED, VISITING, VISITED)
   * @param adjList     Graph adjacency list
   * @param topoOrder   List to store the result in postorder
   * @return false if cycle detected, true otherwise
   */
  private static boolean dfsWithCycleDetection(int currentNode, int[] visited, ArrayList<ArrayList<Integer>> adjList,
      List<Integer> topoOrder) {
    visited[currentNode] = VISITING;

    for (int neighbor : adjList.get(currentNode)) {
      if (visited[neighbor] == VISITING) {
        return false; // because we found a back edge to a node that is still being visited
      }
      if (visited[neighbor] == UNVISITED) {
        if (!dfsWithCycleDetection(neighbor, visited, adjList, topoOrder)) {
          return false; // Cycle found in deeper DFS call
        }
      }
    }

    visited[currentNode] = VISITED;
    topoOrder.add(currentNode); // Postorder addition
    return true;
  }
}
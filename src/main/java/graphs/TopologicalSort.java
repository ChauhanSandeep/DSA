package graphs;

import java.util.*;


/**
 * Problem: Topological Sort with Cycle Detection
 *
 * Given a directed graph, return an ordering where every node appears after all
 * of its prerequisites. If the graph contains a directed cycle, no topological
 * ordering exists, so return an empty array.
 *
 * Leetcode: https://leetcode.com/problems/course-schedule-ii/
 * Rating:   acceptance 55.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | DFS topological sort | Three-color cycle detection
 *
 * Example:
 *   Input:  n = 6, edges = [5->2,5->0,4->0,4->1,2->3,3->1]
 *   Output: [5,4,2,3,1,0]
 *   Why:    every directed edge points from an earlier node to a later node in the
 *           returned order; other valid orders may also exist.
 *
 * Follow-ups:
 *   1. Return the lexicographically smallest topological order?
 *      Use Kahn's algorithm with a min-heap of zero-indegree nodes.
 *   2. Count all possible topological orders?
 *      Backtrack over the current zero-indegree set; this is exponential.
 *   3. Detect and return one cycle?
 *      Keep the recursion path and slice it when a VISITING neighbor is found.
 *
 * Related: Course Schedule (207), Alien Dictionary (269), Parallel Courses (1136).
 */
public class TopologicalSort {

  public static void main(String[] args) {
    ArrayList<ArrayList<Integer>> dag = new ArrayList<>();
    for (int i = 0; i < 6; i++) dag.add(new ArrayList<>());
    dag.get(5).add(2); dag.get(5).add(0); dag.get(4).add(0); dag.get(4).add(1); dag.get(2).add(3); dag.get(3).add(1);
    ArrayList<ArrayList<Integer>> cycle = new ArrayList<>();
    for (int i = 0; i < 2; i++) cycle.add(new ArrayList<>());
    cycle.get(0).add(1); cycle.get(1).add(0);
    int[][] outputs = {performTopologicalSort(6, dag), performTopologicalSort(2, cycle)};
    int[][] expected = {{5, 4, 2, 3, 1, 0}, {}};
    String[] inputs = {"dag", "cycle"};
    for (int i = 0; i < outputs.length; i++) {
      System.out.printf("graph=%s -> %s  expected=%s%n", inputs[i], Arrays.toString(outputs[i]), Arrays.toString(expected[i]));
    }
  }

  private static final int UNVISITED = 0;
  private static final int VISITING = 1;
  private static final int VISITED = 2;


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
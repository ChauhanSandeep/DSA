package graphs.topologicalsort;

import java.util.*;

  /**
   * Intuition: a course schedule is impossible exactly when the prerequisite graph
   * has a directed cycle. DFS coloring catches this when recursion reaches a node
   * currently in the VISITING state.
   *
   * Algorithm:
   *   1. Build the original prerequisite adjacency list from prerequisite to dependent courses.
   *   2. Keep visitStatus values UNVISITED, VISITING, and VISITED.
   *   3. DFS from every course; a VISITING neighbor means a cycle.
   *   4. Return true only if no DFS finds a cycle.
   *
   * Time:  O(V + E) - each course and prerequisite edge is processed once.
   * Space: O(V + E) - adjacency map plus recursion and visit status.
   *
   * @param totalCourses number of courses labeled 0 to totalCourses - 1
   * @param prerequisites pairs [course, prerequisite]
   * @return true if all courses can be completed
   */
public class CourseSchedule {
  private static final int UNVISITED = 0;
  private static final int VISITING = 1;
  private static final int VISITED = 2;

  public static void main(String[] args) {
    CourseSchedule scheduler = new CourseSchedule();
    int[][] prerequisites1 = {{1, 0}};
    int[][] prerequisites2 = {{1, 0}, {0, 1}};

    System.out.printf("numCourses=2 prerequisites=%s -> dfs=%s kahn=%s  expected=true%n",
        Arrays.deepToString(prerequisites1), scheduler.canFinishUsingDFS(2, prerequisites1), scheduler.canFinishUsingKahns(2, prerequisites1));
    System.out.printf("numCourses=2 prerequisites=%s -> dfs=%s kahn=%s  expected=false%n",
        Arrays.deepToString(prerequisites2), scheduler.canFinishUsingDFS(2, prerequisites2), scheduler.canFinishUsingKahns(2, prerequisites2));
  }


  /**
   * Determines if all courses can be completed using DFS cycle detection.
   *
   * Steps:
   * - Build an adjacency list representing prerequisites.
   * - Traverse each course using DFS and track visit states:
   *   0 = unvisited, 1 = visiting, 2 = visited.
   * - If during DFS a course is found in visiting state, a cycle exists.
   *
   * Algorithm: DFS with cycle detection using coloring.
   * Time Complexity: O(V + E)
   * Space Complexity: O(V + E)
   *
   * @param totalCourses     Total number of courses.
   * @param prerequisites  Array of prerequisite pairs.
   * @return true if all courses can be finished, false otherwise.
   */
  public boolean canFinishUsingDFS(int totalCourses, int[][] prerequisites) {
    Map<Integer, List<Integer>> prerequisiteGraph = buildAdjacencyList(prerequisites); // <course, list of dependent courses>

    int[] visitStatus = new int[totalCourses];  // 0 = unvisited, 1 = visiting, 2 = visited

    for (int course = 0; course < totalCourses; course++) {
      if (hasCycleDFS(course, prerequisiteGraph, visitStatus)) {
        return false; // Cycle detected
      }
    }

    return true; // No cycles detected
  }

  /**
   * DFS helper to detect cycle.
   */
  private boolean hasCycleDFS(int course, Map<Integer, List<Integer>> graph, int[] visited) {
    if (visited[course] == VISITING) return true; // Cycle found
    if (visited[course] == VISITED) return false; // Already processed safely

    visited[course] = VISITING; // Mark as visiting
    List<Integer> neighbors = graph.getOrDefault(course, Collections.emptyList());
    for (int dependentCourse : neighbors) {
      if (hasCycleDFS(dependentCourse, graph, visited)) {
        return true;
      }
    }

    visited[course] = VISITED; // Mark as visited
    return false;
  }

  /**
   * Determines if all courses can be completed using BFS (Kahn's Algorithm).
   *
   * Steps:
   * - Build an adjacency list and compute in-degree of all courses.
   * - Initialize a queue with courses having zero in-degree.
   * - Perform topological sort by processing queue and reducing in-degrees.
   * - If all courses are processed, return true.
   *
   * Algorithm: Kahn’s Topological Sort (BFS)
   * Time Complexity: O(V + E)
   * Space Complexity: O(V + E)
   *
   * @param numCourses     Total number of courses.
   * @param prerequisites  Array of prerequisite pairs.
   * @return true if all courses can be finished, false otherwise.
   */
  public boolean canFinishUsingKahns(int numCourses, int[][] prerequisites) {
    Map<Integer, List<Integer>> prerequisiteGraph = new HashMap<>(); // <prerequisite, list of courses that depend on it>
    int[] inDegree = new int[numCourses]; // inDegree[i] = number of prerequisites for course i

    // Build graph and compute in-degrees
    for (int[] pair : prerequisites) {
      int course = pair[0];
      int prerequisite = pair[1];

      prerequisiteGraph.computeIfAbsent(prerequisite, k -> new ArrayList<>()).add(course);
      inDegree[course]++;
    }

    // Queue initialization for all courses with zero in-degree
    Queue<Integer> queue = new LinkedList<>();
    for (int course = 0; course < numCourses; course++) {
      if (inDegree[course] == 0) {
        queue.offer(course);
      }
    }

    int completedCourses = 0;

    while (!queue.isEmpty()) {
      int current = queue.poll();
      completedCourses++;

      for (int neighbor : prerequisiteGraph.getOrDefault(current, Collections.emptyList())) {
        inDegree[neighbor]--;
        if (inDegree[neighbor] == 0) {
          queue.offer(neighbor);
        }
      }
    }

    // If there is a cycle, not all courses can be completed
    return completedCourses == numCourses;
  }

  /**
   * Utility method to build adjacency list from prerequisite pairs.
   */
  private Map<Integer, List<Integer>> buildAdjacencyList(int[][] prerequisites) {
    Map<Integer, List<Integer>> graph = new HashMap<>();

    for (int[] pair : prerequisites) {
      int course = pair[0];
      int prerequisite = pair[1];
      graph.computeIfAbsent(prerequisite, k -> new ArrayList<>()).add(course);
    }

    return graph;
  }
}
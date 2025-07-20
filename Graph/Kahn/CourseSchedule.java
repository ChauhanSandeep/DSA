package Graph.Kahn;

import java.util.*;

/**
 * LeetCode Problem: Course Schedule
 * https://leetcode.com/problems/course-schedule/
 *
 * Problem Statement:
 * You are given the total number of courses (numCourses) and a list of prerequisite pairs
 * where each pair [a, b] indicates that course `a` depends on course `b`.
 * Return true if it is possible to finish all courses. Otherwise, return false.
 *
 * Example:
 * Input: numCourses = 2, prerequisites = [[1, 0]]
 * Output: true
 * Explanation: You can take course 0 first, then course 1.
 *
 * Input: numCourses = 2, prerequisites = [[1, 0], [0, 1]]
 * Output: false
 * Explanation: Circular dependency exists.
 *
 * Follow-up Questions:
 * - What if you need to return the course order?
 *   Leetcode: https://leetcode.com/problems/course-schedule-ii/
 *
 * - How to handle very large input efficiently?
 *   Use iterative approaches and adjacency lists.
 */
public class CourseSchedule {
  private static final int UNVISITED = 0;
  private static final int VISITING = 1;
  private static final int VISITED = 2;

  public static void main(String[] args) {
    int[][] prerequisites = {
        {1, 0},
        {0, 1}
    };

    CourseSchedule scheduler = new CourseSchedule();
    boolean canFinishDFS = scheduler.canFinishUsingDFS(2, prerequisites);
    System.out.println("Can finish all courses (DFS): " + canFinishDFS);

    boolean canFinishBFS = scheduler.canFinishUsingKahns(2, prerequisites);
    System.out.println("Can finish all courses (BFS): " + canFinishBFS);
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
    Queue<Integer> readyCourses = new LinkedList<>();
    for (int course = 0; course < numCourses; course++) {
      if (inDegree[course] == 0) {
        readyCourses.offer(course);
      }
    }

    int completedCourses = 0;

    while (!readyCourses.isEmpty()) {
      int current = readyCourses.poll();
      completedCourses++;

      for (int neighbor : prerequisiteGraph.getOrDefault(current, Collections.emptyList())) {
        inDegree[neighbor]--;
        if (inDegree[neighbor] == 0) {
          readyCourses.offer(neighbor);
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
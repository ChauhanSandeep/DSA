package graph.Kahn;

import java.util.*;

/**
 * LeetCode Problem: Course Schedule II
 * https://leetcode.com/problems/course-schedule-ii/
 *
 * Problem Statement:
 * There are `numCourses` labeled from 0 to numCourses-1. You are given an array `prerequisites`
 * where prerequisites[i] = [ai, bi] indicates that to take course ai you must first take course bi.
 * Return the ordering of courses you should take to finish all courses. If it is not possible, return an empty array.
 *
 * Example:
 * Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * Output: [0,2,1,3] or [0,1,2,3]
 *
 * Follow-up Questions:
 * - What if multiple valid orders are possible?
 *   You can return any of them.
 *
 * - How do you detect a cycle in a directed graph?
 *   Using DFS with coloring or Kahn’s algorithm (BFS with in-degrees).
 *
 * - Can you return all valid topological orderings?
 *   Not with this approach. Requires backtracking.
 */
public class CourseSchedule2 {
  private static final int UNVISITED = 0;
  private static final int VISITING = 1;
  private static final int VISITED = 2;

  public static void main(String[] args) {
    int numCourses = 4;
    int[][] prerequisites = {
        {1, 0},
        {2, 0},
        {3, 1},
        {3, 2}
    };

    CourseSchedule2 scheduler = new CourseSchedule2();
    int[] courseOrderDFS = scheduler.findCourseOrderDFS(numCourses, prerequisites);
    System.out.println("Course order (DFS): " + Arrays.toString(courseOrderDFS));

    int[] courseOrderBFS = scheduler.findCourseOrderUsingKahn(numCourses, prerequisites);
    System.out.println("Course order (BFS): " + Arrays.toString(courseOrderBFS));
  }

  /**
   * Returns a valid course order using DFS + Topological Sort with cycle detection.
   *
   * Steps:
   * - Build adjacency list representing the course dependency graph. <prerequisiteCourse, list of dependentCourses>
   * - Perform DFS on each unvisited node.
   * - Go deep into the graph, marking nodes as visiting (1) and visited (2). Until we reach the course with no dependencies.
   * - Add the course to the topological order after visiting all its neighbors.
   * - Maintain a post-order list and reverse it to get valid topological order.
   * - If a node is revisited while still being visited (1) → cycle exists → return empty array.
   *
   * Algorithm: DFS Topological Sort + Cycle Detection
   * Time Complexity: O(V + E)
   * Space Complexity: O(V + E)
   *
   * @param numCourses     Number of total courses.
   * @param prerequisites  Array of course dependency pairs.
   * @return Topological sort order or empty array if not possible.
   */
  public int[] findCourseOrderDFS(int numCourses, int[][] prerequisites) {
    List<List<Integer>> adjacencyList = buildAdjacencyList(numCourses, prerequisites); // <prerequisiteCourse, list of dependentCourses>
    int[] visitState = new int[numCourses]; // 0 = unvisited, 1 = visiting, 2 = visited
    List<Integer> topologicalOrder = new ArrayList<>();

    for (int course = 0; course < numCourses; course++) {
      if (hasCycleDFS(course, adjacencyList, visitState, topologicalOrder)) {
        return new int[0]; // Cycle detected
      }
    }

    // Reverse the post-order DFS result to get valid topological ordering
    Collections.reverse(topologicalOrder);
    return topologicalOrder.stream().mapToInt(i -> i).toArray();
  }

  /**
   * DFS helper to detect cycles and collect topological order.
   */
  private boolean hasCycleDFS(int course, List<List<Integer>> graph, int[] visitState, List<Integer> topoOrder) {
    if (visitState[course] == VISITING) return true;  // Cycle detected
    if (visitState[course] == VISITED) return false; // Already processed safely

    visitState[course] = 1; // Mark as visiting

    for (int neighbor : graph.get(course)) {
      if (hasCycleDFS(neighbor, graph, visitState, topoOrder)) {
        return true;
      }
    }

    visitState[course] = 2; // Mark as fully visited
    topoOrder.add(course); // Add after visiting all neighbors
    return false;
  }

  /**
   * Returns a valid course order using BFS (Kahn’s Algorithm).
   *
   * Steps:
   * - Build the graph and compute in-degree for each course.
   * - Add all nodes with in-degree 0 to a queue.
   * - Perform BFS and reduce in-degree for neighbors.
   * - Append courses to result list in the order they are processed.
   * - If result length != total courses → cycle exists → return empty array.
   *
   * Algorithm: Kahn’s Algorithm for Topological Sort
   * Time Complexity: O(V + E)
   * Space Complexity: O(V + E)
   *
   * @param numCourses     Number of total courses.
   * @param prerequisites  Array of course dependency pairs.
   * @return Topological sort order or empty array if not possible.
   */
  public int[] findCourseOrderUsingKahn(int numCourses, int[][] prerequisites) {
    List<List<Integer>> graph = buildAdjacencyList(numCourses, prerequisites); // <prerequisiteCourse, list of dependentCourses>
    int[] inDegree = new int[numCourses];

    for (int[] pair : prerequisites) {
      int course = pair[0];
      inDegree[course]++;
    }

    Queue<Integer> readyCourses = new LinkedList<>();
    for (int course = 0; course < numCourses; course++) {
      if (inDegree[course] == 0) {
        readyCourses.offer(course);
      }
    }

    List<Integer> courseOrder = new ArrayList<>();

    while (!readyCourses.isEmpty()) {
      int currentCourse = readyCourses.poll();
      courseOrder.add(currentCourse);

      for (int neighbor : graph.get(currentCourse)) {
        inDegree[neighbor]--;
        if (inDegree[neighbor] == 0) {
          // if all the prerequisites for this course are completed, add it to the ready queue
          readyCourses.offer(neighbor);
        }
      }
    }

    // if we are not able to process all courses, it means there is a cycle
    return courseOrder.size() != numCourses
        ? new int[0]
        : courseOrder.stream().mapToInt(i -> i).toArray();
  }

  /**
   * Utility method to build an adjacency list from prerequisites.
   */
  private List<List<Integer>> buildAdjacencyList(int numCourses, int[][] prerequisites) {
    List<List<Integer>> adjacencyList = new ArrayList<>();

    for (int i = 0; i < numCourses; i++) {
      adjacencyList.add(new ArrayList<>());
    }

    for (int[] pair : prerequisites) {
      int course = pair[0];
      int prerequisite = pair[1];
      adjacencyList.get(prerequisite).add(course);
    }

    return adjacencyList;
  }
}
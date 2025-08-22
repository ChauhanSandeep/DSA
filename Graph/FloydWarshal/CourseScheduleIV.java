package Graph.FloydWarshal;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Course Schedule IV (Leetcode 1462)
 * Link: https://leetcode.com/problems/course-schedule-iv/
 *
 * Problem Statement:
 * There are n courses labeled from 0 to n-1. You are given an array of prerequisite pairs
 * and a list of query pairs. Each prerequisite pair [a, b] means course a is a prerequisite of course b.
 *
 * Task:
 * For each query [u, v], determine whether u is a prerequisite of v (directly or indirectly).
 *
 * Example:
 * Input:
 *   n = 3
 *   prerequisites = [[0,1],[1,2]]
 *   queries = [[0,2],[1,2],[2,0]]
 * Output:
 *   [true, true, false]
 * Explanation:
 * [0,2] true because 0 -> 1 -> 2
 * [1,2] true because 1 -> 2
 * [2,0] false because 2 can be taken up without 0
 *
 * Follow-up Questions:
 * 1. Can we optimize beyond Floyd-Warshall?
 *    - Yes, by using Topological Sort + BFS/DFS for each query or
 *      precomputing reachability using graph traversal. Optimized approaches
 *      often achieve O(n^2 + m) where m is edges, instead of O(n^3).
 *
 * 2. What if queries are online (streaming)?
 *    - Maintain adjacency and use DFS with memoization for each query.
 *
 * 3. Can we handle dynamic prerequisites (edges added/removed)?
 *    - Yes, but requires advanced techniques like Dynamic Transitive Closure (using DAG reachability data structures).
 */
public class CourseScheduleIV {

  /**
   * Floyd-Warshall based approach.
   *
   * Steps:
   * 1. Initialize a reachability matrix with direct prerequisites.
   * 2. Use Floyd-Warshall to compute transitive closure:
   *    - If A → B and B → C, then A → C.
   * 3. For each query, directly check reachability[from][to].
   *
   * Algorithm: Floyd-Warshall
   * Time Complexity: O(courseCount^3 + queryCount), where courseCount = number of courses, queryCount = number of queries
   * Space Complexity: O(courseCount^2) for reachability matrix
   */
  public List<Boolean> checkIfPrerequisite(int courseCount, int[][] prerequisites, int[][] queries) {
    boolean[][] reachable = new boolean[courseCount][courseCount];

    // Step 1: Populate direct prerequisites
    for (int[] pair : prerequisites) {
      int prerequisiteCourse = pair[0];
      int targetCourse = pair[1];
      reachable[prerequisiteCourse][targetCourse] = true;
    }

    // Step 2: Floyd-Warshall for transitive closure
    for (int intermediateCourse = 0; intermediateCourse < courseCount; intermediateCourse++) {
      for (int fromCourse = 0; fromCourse < courseCount; fromCourse++) {
        for (int toCourse = 0; toCourse < courseCount; toCourse++) {
          if (reachable[fromCourse][intermediateCourse] && reachable[intermediateCourse][toCourse]) {
            reachable[fromCourse][toCourse] = true;
          }
        }
      }
    }

    // Step 3: Answer queries
    List<Boolean> result = new ArrayList<>();
    for (int[] query : queries) {
      int prerequisiteCourse = query[0];
      int targetCourse = query[1];
      result.add(reachable[prerequisiteCourse][targetCourse]);
    }

    return result;
  }

  /**
   * Optimized approach using Topological Sort + DFS.
   *
   * Steps:
   * 1. Build adjacency list of prerequisites.
   * 2. Perform DFS from each course to collect all reachable courses.
   * 3. For each query, check if target is in prerequisite's reachable set.
   *
   * Algorithm: DFS with memoization
   * Time Complexity: O(courseCount^2 + edgeCount + queryCount), where edgeCount = number of edges
   * The time complexity `O(courseCount^2 + edgeCount + queryCount)` is calculated as follows:
   * - O(courseCount^2): Each course runs DFS, and in the worst case, each course can reach every other course, filling the `reachable` matrix.
   * - O(edgeCount): Each edge is traversed once during DFS across all courses.
   * - O(queryCount): Each query is answered in constant time by looking up the `reachable` matrix.
   *
   * All steps are summed because they happen sequentially, not nested.
   * Space Complexity: O(courseCount^2) for reachability set
   */
  public List<Boolean> checkIfPrerequisiteOptimized(int courseCount, int[][] prerequisites, int[][] queries) {
    List<List<Integer>> adjacencyList = new ArrayList<>();
    for (int i = 0; i < courseCount; i++) {
      adjacencyList.add(new ArrayList<>());
    }
    for (int[] pair : prerequisites) {
      adjacencyList.get(pair[0]).add(pair[1]);
    }

    boolean[][] reachable = new boolean[courseCount][courseCount];
    for (int course = 0; course < courseCount; course++) {
      dfs(course, course, adjacencyList, reachable);
    }

    List<Boolean> result = new ArrayList<>();
    for (int[] query : queries) {
      result.add(reachable[query[0]][query[1]]);
    }

    return result;
  }

  /**
   * Helper DFS to compute reachability from a source course.
   */
  private void dfs(int source, int current, List<List<Integer>> adjacencyList, boolean[][] reachable) {
    for (int nextCourse : adjacencyList.get(current)) {
      if (!reachable[source][nextCourse]) {
        reachable[source][nextCourse] = true;
        dfs(source, nextCourse, adjacencyList, reachable);
      }
    }
  }
}

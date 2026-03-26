package graphs.floydwarshall;

import java.util.*;


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
 * LeetCode Contest Rating: 1693
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
      int sourceCourse = pair[0];
      int targetCourse = pair[1];
      reachable[sourceCourse][targetCourse] = true;
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
      int fromCourse = query[0];
      int targetCourse = query[1];
      result.add(reachable[fromCourse][targetCourse]);
    }

    return result;
  }

  /**
     * Approach 2: BFS/DFS with Preprocessing (Alternative).
     * Step-by-step:
     *  1. Build adjacency list from prerequisites
     *  2. For each course i from 0 to n-1:
     *     - Run BFS/DFS from course i
     *     - Mark all reachable courses as prerequisites of i
     *  3. Answer queries by looking up preprocessed results
     *
     * Key Insight:
     * Instead of Floyd-Warshall's all-pairs approach, explicitly find all descendants
     * of each course using BFS/DFS. Same result, different implementation.
     *
     * Algorithm: BFS from each node.
     * Time Complexity: O(n * (V + E)) = O(n²) best case, O(n³) worst case
     * Space Complexity: O(n²) for storing all prerequisites.
     * 
     * Best when: Sparse graphs (fewer edges), similar query count.
     */
    public List<Boolean> checkIfPrerequisite_BFS(int numCourses, int[][] prerequisites, int[][] queries) {
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] prereq : prerequisites) {
            graph.get(prereq[0]).add(prereq[1]);
        }
        
        // For each course, find all courses it's a prerequisite for
        boolean[][] isReachable = new boolean[numCourses][numCourses];
        
        for (int start = 0; start < numCourses; start++) {
            // BFS from start to find all reachable courses
            Queue<Integer> queue = new LinkedList<>();
            boolean[] visited = new boolean[numCourses];
            queue.offer(start);
            visited[start] = true;
            
            while (!queue.isEmpty()) {
                int course = queue.poll();
                
                for (int nextCourse : graph.get(course)) {
                    if (!visited[nextCourse]) {
                        visited[nextCourse] = true;
                        isReachable[start][nextCourse] = true;  // start is prerequisite of nextCourse
                        queue.offer(nextCourse);
                    }
                }
            }
        }
        
        // Answer queries
        List<Boolean> result = new ArrayList<>();
        for (int[] query : queries) {
            result.add(isReachable[query[0]][query[1]]);
        }
        
        return result;
    }

    /**
     * Approach 3: Topological Sort (Kahn's Algorithm) - Most Efficient.
     * Step-by-step:
     *  1. Build graph and calculate in-degrees
     *  2. Process nodes in topological order using Kahn's algorithm
     *  3. For each node, maintain a set of all its prerequisites
     *  4. When processing edge u → v, propagate all prerequisites of u to v
     *  5. Answer queries by checking prerequisite sets
     *
     * Key Insight:
     * Process courses in dependency order. When we process a course, we already know
     * all prerequisites of its dependencies, so we can aggregate them efficiently.
     *
     * Algorithm: Topological Sort with prerequisite propagation.
     * Time Complexity: O(V + E + V * max_prereqs) ≈ O(V²) in practice
     *  - 1. Time complexity for topological sort: O(V + E)
     *  - 2. Time complexity for propagating prerequisites: O(V * max_prereqs), where max_prereqs is the maximum number of prerequisites for any course (worst case)
     * Space Complexity: O(V²) for prerequisite sets.
     * 
     * Best when: DAG structure, clear dependency ordering.
     */
    public List<Boolean> checkIfPrerequisite_TopologicalSort(int numCourses, int[][] prerequisites, 
                                                               int[][] queries) {
        // Build graph and in-degree array
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[numCourses];
        
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] prereq : prerequisites) {
            graph.get(prereq[0]).add(prereq[1]);
            inDegree[prereq[1]]++;
        }
        
        // Map to store all prerequisites for each course
        Map<Integer, Set<Integer>> allPrereqs = new HashMap<>();
        
        // Kahn's algorithm for topological sort
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
            allPrereqs.put(i, new HashSet<>());
        }
        
        while (!queue.isEmpty()) {
            int course = queue.poll();
            
            for (int nextCourse : graph.get(course)) {
                // Add direct prerequisite
                allPrereqs.get(nextCourse).add(course);
                
                // Add all indirect prerequisites (from course's prerequisites)
                allPrereqs.get(nextCourse).addAll(allPrereqs.get(course));
                
                // Decrease in-degree and add to queue if ready
                inDegree[nextCourse]--;
                if (inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        
        // Answer queries
        List<Boolean> result = new ArrayList<>();
        for (int[] query : queries) {
            result.add(allPrereqs.get(query[1]).contains(query[0]));
        }
        
        return result;
    }
}

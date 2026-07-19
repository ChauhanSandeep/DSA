package graphs.topologicalsort;

import java.util.*;

  /**
   * Intuition: DFS postorder naturally puts a course after all courses that depend
   * on it in the traversal graph. Reversing that postorder gives a valid course
   * order, unless a VISITING node is reached again, which proves a cycle.
   *
   * Algorithm:
   *   1. Build the original adjacency list from prerequisite to dependent courses.
   *   2. DFS every course with UNVISITED, VISITING, and VISITED states.
   *   3. Add a course to topologicalOrder after all outgoing neighbors are processed.
   *   4. Reverse the postorder result, or return an empty array if a cycle is found.
   *
   * Time:  O(V + E) - each course and edge is visited once.
   * Space: O(V + E) - adjacency list, recursion stack, state array, and order list.
   *
   * @param numCourses number of courses labeled 0 to numCourses - 1
   * @param prerequisites pairs [course, prerequisite]
   * @return a valid course order, or an empty array when impossible
   */
public class CourseSchedule2 {
  private static final int UNVISITED = 0;
  private static final int VISITING = 1;
  private static final int VISITED = 2;

  public static void main(String[] args) {
    CourseSchedule2 scheduler = new CourseSchedule2();
    int[][] prerequisites1 = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
    int[][] prerequisites2 = {{1, 0}, {0, 1}};

    System.out.printf("numCourses=4 prerequisites=%s -> dfs=%s kahn=%s  expected dfs=[0, 2, 1, 3] kahn=[0, 1, 2, 3]%n",
        Arrays.deepToString(prerequisites1),
        Arrays.toString(scheduler.findCourseOrderDFS(4, prerequisites1)),
        Arrays.toString(scheduler.findCourseOrderUsingKahn(4, prerequisites1)));
    System.out.printf("numCourses=2 prerequisites=%s -> dfs=%s kahn=%s  expected=[]%n",
        Arrays.deepToString(prerequisites2),
        Arrays.toString(scheduler.findCourseOrderDFS(2, prerequisites2)),
        Arrays.toString(scheduler.findCourseOrderUsingKahn(2, prerequisites2)));
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
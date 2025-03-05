package Graph;

import java.util.*;

/**
 * LeetCode Problem: Course Schedule II
 * https://leetcode.com/problems/course-schedule-ii/
 *
 * Problem Statement:
 * Given `numCourses` and a list of `prerequisites` [course, prerequisite],
 * determine a valid order to take all courses. If there is no valid order, return an empty array.
 *
 * Approaches:
 * 1. **DFS with Cycle Detection**:
 *    - Build a directed graph using adjacency lists.
 *    - Perform DFS to detect cycles and determine a valid course order.
 *    - If a cycle is detected, return an empty array.
 *    - Time Complexity: **O(V + E)** (where V = courses, E = prerequisites)
 *    - Space Complexity: **O(V + E)** (Adjacency list + recursion stack)
 *
 * 2. **BFS (Kahn’s Algorithm - Topological Sort)**:
 *    - Compute in-degree for each course.
 *    - Use a queue to process courses with zero in-degree.
 *    - Process courses in topological order; if all courses are processed, return the order.
 *    - If a cycle exists, return an empty array.
 *    - Time Complexity: **O(V + E)**
 *    - Space Complexity: **O(V + E)**
 */
public class CourseSchedule2 {

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

        int[] courseOrderBFS = scheduler.findCourseOrderBFS(numCourses, prerequisites);
        System.out.println("Course order (BFS): " + Arrays.toString(courseOrderBFS));
    }

    /**
     * Approach 1: DFS with Cycle Detection
     * @param numCourses    Total number of courses.
     * @param prerequisites Array of prerequisite pairs {course, prerequisite}.
     * @return An array representing a valid course order; if no order exists, returns an empty array.
     */
    public int[] findCourseOrderDFS(int numCourses, int[][] prerequisites) {
        // Step 1: Build adjacency list (course dependency graph)
        List<List<Integer>> courseGraph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            courseGraph.add(new ArrayList<>());
        }
        for (int[] pair : prerequisites) {
            int course = pair[0], prerequisite = pair[1];
            courseGraph.get(prerequisite).add(course);
        }

        // Step 2: Track visit states (0 = unvisited, 1 = visiting, 2 = processed)
        int[] visitState = new int[numCourses];
        List<Integer> topoOrder = new ArrayList<>();

        // Step 3: Perform DFS to detect cycles and compute topological order
        for (int course = 0; course < numCourses; course++) {
            if (detectCycleDFS(course, courseGraph, visitState, topoOrder)) {
                return new int[0]; // Cycle detected, no valid order
            }
        }

        // Step 4: Convert List to array (reverse order since DFS adds last first)
        Collections.reverse(topoOrder);
        return topoOrder.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Performs DFS to detect cycles and build topological order.
     * @return true if a cycle is detected, false otherwise.
     */
    private boolean detectCycleDFS(int course, List<List<Integer>> courseGraph, int[] visitState, List<Integer> topoOrder) {
        if (visitState[course] == 1) return true;  // Cycle detected
        if (visitState[course] == 2) return false; // Already processed, no cycle

        visitState[course] = 1; // Mark as visiting

        for (int nextCourse : courseGraph.get(course)) {
            if (detectCycleDFS(nextCourse, courseGraph, visitState, topoOrder)) {
                return true;
            }
        }

        visitState[course] = 2; // Mark as fully processed
        topoOrder.add(course);
        return false;
    }

    /**
     * Approach 2: BFS (Kahn’s Algorithm - Topological Sort)
     * @param numCourses    Total number of courses.
     * @param prerequisites Array of prerequisite pairs {course, prerequisite}.
     * @return An array representing a valid course order; if no order exists, returns an empty array.
     */
    public int[] findCourseOrderBFS(int numCourses, int[][] prerequisites) {
        // Step 1: Build adjacency list & compute in-degree for each course
        List<List<Integer>> courseGraph = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) {
            courseGraph.add(new ArrayList<>());
        }
        for (int[] pair : prerequisites) {
            int course = pair[0], prerequisite = pair[1];
            courseGraph.get(prerequisite).add(course);
            inDegree[course]++;
        }

        // Step 2: Initialize queue with courses having zero in-degree
        Queue<Integer> queue = new LinkedList<>();
        for (int course = 0; course < numCourses; course++) {
            if (inDegree[course] == 0) {
                queue.offer(course);
            }
        }

        // Step 3: Process courses in topological order
        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            topoOrder.add(currentCourse);

            for (int nextCourse : courseGraph.get(currentCourse)) {
                inDegree[nextCourse]--;
                if (inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }

        // Step 4: Check if all courses are processed
        return topoOrder.size() == numCourses ? topoOrder.stream().mapToInt(i -> i).toArray() : new int[0];
    }
}

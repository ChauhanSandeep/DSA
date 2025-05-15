package Graph.Kahn;

import java.util.*;

/**
 * LeetCode Problem: Course Schedule
 * https://leetcode.com/problems/course-schedule/
 *
 * Problem Statement:
 * Given `numCourses` and a list of `prerequisites` [course, prerequisite],
 * determine if it's possible to complete all courses without cyclic dependencies.
 *
 */
public class CourseSchedule {

    public static void main(String[] args) {
        int[][] prerequisites = {
            {1, 0},
            {0, 1}
        };

        CourseSchedule cs = new CourseSchedule();
        boolean canFinishDFS = cs.canFinishUsingDFS(2, prerequisites);
        System.out.println("Can finish all courses (DFS): " + canFinishDFS);

        boolean canFinishBFS = cs.canFinishUsingBFS(2, prerequisites);
        System.out.println("Can finish all courses (BFS): " + canFinishBFS);
    }

    /**
     * Approach 1: DFS Cycle Detection
     *  - Model the course dependencies as a directed graph.
     *  - Use DFS to check for cycles (if a node is revisited in the same path, a cycle exists).
     *
     *  - Time Complexity: **O(V + E)** (where V = courses, E = prerequisites)
     *  - Space Complexity: **O(V + E)** (Adjacency list + recursion stack)
     * @param numCourses
     * @param prerequisites
     * @return
     */
    public boolean canFinishUsingDFS(int numCourses, int[][] prerequisites) {
        // Step 1: Build adjacency list (course dependency graph)
        Map<Integer, List<Integer>> courseGraph = new HashMap<>();
        for (int[] pair : prerequisites) {
            int course = pair[0], prerequisite = pair[1];
            courseGraph.computeIfAbsent(prerequisite, k -> new ArrayList<>()).add(course);
        }

        // Step 2: Track visited states (0 = unvisited, 1 = visiting, 2 = visited)
        int[] visitState = new int[numCourses];

        // Step 3: Check for cycles in each course
        for (int course = 0; course < numCourses; course++) {
            if (detectCycleDFS(course, courseGraph, visitState)) {
                return false; // Cycle detected -> can't finish courses
            }
        }

        return true; // No cycle detected -> all courses can be completed
    }

    private boolean detectCycleDFS(int course, Map<Integer, List<Integer>> courseGraph, int[] visitState) {
        if (visitState[course] == 1) return true;  // Cycle detected
        if (visitState[course] == 2) return false; // Already processed, no cycle

        visitState[course] = 1; // Mark as visiting

        if (courseGraph.containsKey(course)) {
            for (int nextCourse : courseGraph.get(course)) {
                if (detectCycleDFS(nextCourse, courseGraph, visitState)) {
                    return true;
                }
            }
        }

        visitState[course] = 2; // Mark as fully visited (safe node)
        return false;
    }

    /**
     * Approach 2: BFS (Kahn’s Algorithm - Topological Sort)
     * - Compute in-degree for each course.
     * - Use a queue to process courses with zero in-degree.
     * - If we can process all courses, return true; otherwise, a cycle exists.
     *
     * - Time Complexity: **O(V + E)**
     * - Space Complexity: **O(V + E)**
     */
    public boolean canFinishUsingBFS(int numCourses, int[][] prerequisites) {
        // Step 1: Build adjacency list & compute in-degree for each course
        Map<Integer, List<Integer>> courseGraph = new HashMap<>();
        int[] inDegree = new int[numCourses];

        for (int[] pair : prerequisites) {
            int course = pair[0], prerequisite = pair[1];
            courseGraph.computeIfAbsent(prerequisite, k -> new ArrayList<>()).add(course);
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
        int completedCourses = 0;
        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            completedCourses++;

            if (courseGraph.containsKey(currentCourse)) {
                for (int nextCourse : courseGraph.get(currentCourse)) {
                    inDegree[nextCourse]--;
                    if (inDegree[nextCourse] == 0) {
                        queue.offer(nextCourse);
                    }
                }
            }
        }

        return completedCourses == numCourses; // True if all courses can be completed
    }
}

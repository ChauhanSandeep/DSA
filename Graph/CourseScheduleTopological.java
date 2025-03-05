package Graph;

import java.util.*;

/**
 * LeetCode Problem: Course Schedule (Topological Sort)
 * https://leetcode.com/problems/course-schedule/
 *
 * Problem Statement:
 * Given `numCourses` and a list of `prerequisites` [course, prerequisite],
 * determine if it's possible to finish all courses (i.e., no cyclic dependencies).
 *
 * Approach: **BFS (Kahn’s Algorithm - Topological Sorting)**
 * 1. **Build a Graph:** Construct an adjacency list and compute in-degree for each course.
 * 2. **Use a Queue:** Add all courses with `in-degree == 0` (no prerequisites) to the queue.
 * 3. **Process in Topological Order:** Remove courses from the queue and update dependencies.
 * 4. **Check for Cycles:** If all courses are processed, return `true`; otherwise, return `false`.
 *
 * Time Complexity: **O(V + E)** (V = courses, E = prerequisites)
 * Space Complexity: **O(V + E)** (Adjacency list + queue)
 */
public class CourseScheduleTopological {

    /**
     * Determines if all courses can be completed without cyclic dependencies.
     *
     * @param numCourses    Total number of courses.
     * @param prerequisites Array of prerequisite pairs {course, prerequisite}.
     * @return True if all courses can be completed, false otherwise.
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Step 1: Initialize adjacency list and in-degree array
        List<List<Integer>> courseGraph = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) {
            courseGraph.add(new ArrayList<>());
        }

        // Step 2: Build the adjacency list and calculate in-degree of each course
        for (int[] pair : prerequisites) {
            int course = pair[0], prerequisite = pair[1];
            courseGraph.get(prerequisite).add(course);
            inDegree[course]++;
        }

        // Step 3: Find all courses with no prerequisites (in-degree 0)
        Queue<Integer> queue = new LinkedList<>();
        for (int course = 0; course < numCourses; course++) {
            if (inDegree[course] == 0) {
                queue.offer(course);
            }
        }

        // Step 4: Process courses using BFS in topological order
        int processedCourses = 0;
        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            processedCourses++;

            for (int nextCourse : courseGraph.get(currentCourse)) {
                if (--inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }

        // Step 5: If all courses are processed, return true; otherwise, a cycle exists
        return processedCourses == numCourses;
    }

    public static void main(String[] args) {
        CourseScheduleTopological scheduler = new CourseScheduleTopological();
        
        // Test Case 1: Cycle present (should return false)
        int numCourses1 = 2;
        int[][] prerequisites1 = { {1, 0}, {0, 1} };
        System.out.println("Can finish courses: " + scheduler.canFinish(numCourses1, prerequisites1)); // false

        // Test Case 2: No cycle, valid order exists (should return true)
        int numCourses2 = 4;
        int[][] prerequisites2 = { {1, 0}, {2, 1}, {3, 2} };
        System.out.println("Can finish courses: " + scheduler.canFinish(numCourses2, prerequisites2)); // true
    }
}

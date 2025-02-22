package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import java.util.*;

/**
 * CourseSchedule2 computes a valid order in which courses can be completed given their prerequisites.
 *
 * Problem Description:
 * Given the total number of courses and an array of prerequisite pairs {course, prerequisite},
 * this class determines a valid order to take courses such that all prerequisites are satisfied.
 * If no valid ordering exists (i.e., if there is a cycle in the prerequisites), an empty array is returned.
 *
 * Algorithm:
 * - Construct an adjacency list representation of the course dependency graph.
 * - Use Depth-First Search (DFS) to perform topological sorting with cycle detection.
 *   - A node (course) is added to the result stack after all courses dependent on it have been processed.
 *   - A Boolean array is used to track the DFS recursion state for cycle detection.
 *
 * Time Complexity: O(V + E), where V is the number of courses and E is the number of prerequisite pairs.
 * Space Complexity: O(V) for the recursion stack and auxiliary data structures.
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
        int[] courseOrder = scheduler.findCourseOrder(numCourses, prerequisites);
        System.out.println("Course order: " + Arrays.toString(courseOrder));
    }

    /**
     * Determines a valid course order based on prerequisites.
     *
     * @param numCourses    Total number of courses.
     * @param prerequisites Array of prerequisite pairs {course, prerequisite}.
     * @return An array representing a valid course order; if no order exists, returns an empty array.
     */
    public int[] findCourseOrder(int numCourses, int[][] prerequisites) {
        // Build the graph: each course maps to a list of courses that depend on it.
        List<List<Integer>> courseGraph = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            courseGraph.add(new ArrayList<>());
        }
        for (int[] pair : prerequisites) {
            int course = pair[0];
            int prerequisite = pair[1];
            courseGraph.get(prerequisite).add(course);
        }

        // Stack to hold the valid course order (topological sort order).
        Stack<Integer> topoStack = new Stack<>();
        // Array to track the DFS visitation state:
        //   null: unvisited, true: currently in recursion stack, false: fully processed.
        Boolean[] visitStatus = new Boolean[numCourses];

        // Run DFS for each course to detect cycles and build the topological order.
        for (int course = 0; course < numCourses; course++) {
            if (dfsTopoSort(course, visitStatus, courseGraph, topoStack)) {
                // Cycle detected; return empty array.
                return new int[]{};
            }
        }

        // Build the result array from the stack.
        int[] courseOrder = new int[topoStack.size()];
        int index = 0;
        while (!topoStack.isEmpty()) {
            courseOrder[index++] = topoStack.pop();
        }
        return courseOrder;
    }

    /**
     * Helper method that performs DFS to build a topological order and detect cycles.
     *
     * @param course      The current course being processed.
     * @param visitStatus Array tracking the DFS state for each course.
     * @param courseGraph The graph representing course prerequisites.
     * @param topoStack   Stack to accumulate the valid topological order.
     * @return true if a cycle is detected starting from this course, false otherwise.
     */
    private boolean dfsTopoSort(int course, Boolean[] visitStatus, List<List<Integer>> courseGraph, Stack<Integer> topoStack) {
        // If this course has been visited before, return its DFS state.
        if (visitStatus[course] != null) {
            return visitStatus[course];
        }

        // Mark the course as currently in the recursion stack.
        visitStatus[course] = true;

        // Explore all dependent courses.
        for (int dependentCourse : courseGraph.get(course)) {
            if (dfsTopoSort(dependentCourse, visitStatus, courseGraph, topoStack)) {
                // Cycle detected in the dependency.
                return true;
            }
        }

        // Mark this course as fully processed.
        visitStatus[course] = false;
        // Push the course onto the topological order stack.
        topoStack.push(course);
        return false;
    }
}

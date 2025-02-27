package Graph;

import java.util.*;

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
        if (numCourses == 0) return new int[0];

        // Step 1: Build adjacency list (courseGraph)
        List<List<Integer>> courseGraph = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            courseGraph.add(new ArrayList<>());
        }
        for (int[] pair : prerequisites) {
            courseGraph.get(pair[1]).add(pair[0]);
        }

        // Step 2: Tracking state (0 = unvisited, 1 = visiting, 2 = processed)
        int[] visitState = new int[numCourses];
        List<Integer> topoOrder = new ArrayList<>();

        // Step 3: Perform DFS to detect cycles and compute topological order
        for (int course = 0; course < numCourses; course++) {
            if (hasCycle(course, courseGraph, visitState, topoOrder)) {
                return new int[0]; // Cycle detected, no valid order exists
            }
        }

        // Step 4: Convert List to array (reverse order since DFS adds last first)
        int[] result = new int[topoOrder.size()];
        for (int i = 0; i < topoOrder.size(); i++) {
            result[i] = topoOrder.get(topoOrder.size() - 1 - i);
        }
        return result;
    }

    /**
     * Performs DFS to detect cycles and build topological order.
     *
     * @param course      The current course being processed.
     * @param courseGraph The graph representing course prerequisites.
     * @param visitState  Tracks the state of each course during DFS.
     * @param topoOrder   List to accumulate the valid topological order.
     * @return true if a cycle is detected, false otherwise.
     */
    private boolean hasCycle(int course, List<List<Integer>> courseGraph, int[] visitState, List<Integer> topoOrder) {
        if (visitState[course] == 1) return true;  // Cycle detected
        if (visitState[course] == 2) return false; // Already processed, no cycle

        visitState[course] = 1; // Mark as visiting

        for (int nextCourse : courseGraph.get(course)) {
            if (hasCycle(nextCourse, courseGraph, visitState, topoOrder)) {
                return true;
            }
        }

        visitState[course] = 2; // Mark as fully processed
        topoOrder.add(course);
        return false;
    }
}

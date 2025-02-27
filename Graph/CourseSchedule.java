package Graph;

import java.util.*;

public class CourseSchedule {
    public static void main(String[] args) {
        int[][] prerequisites = {
            {1, 0},
            {0, 1}
        };

        CourseSchedule cs = new CourseSchedule();
        boolean canFinish = cs.canFinishUsingDFS(2, prerequisites);
        System.out.println("Can finish all courses: " + canFinish);
    }

    public boolean canFinishUsingDFS(int numCourses, int[][] prerequisites) {
        // Step 1: Build adjacency list (courseMap)
        HashMap<Integer, List<Integer>> courseMap = new HashMap<>();
        for (int[] pair : prerequisites) {
            int course = pair[0], pre = pair[1];
            courseMap.computeIfAbsent(pre, k -> new ArrayList<>()).add(course);
        }

        // Step 2: Track visited states (0 = unvisited, 1 = visiting, 2 = visited)
        int[] visited = new int[numCourses];

        // Step 3: Check for cycles using DFS
        for (int course = 0; course < numCourses; course++) {
            if (hasCycle(course, courseMap, visited)) {
                return false; // Cycle detected -> can't finish courses
            }
        }

        return true; // No cycle detected -> can finish courses
    }

    private boolean hasCycle(int course, HashMap<Integer, List<Integer>> courseMap, int[] visited) {
        if (visited[course] == 1) return true;  // Cycle detected
        if (visited[course] == 2) return false; // Already processed, no cycle

        visited[course] = 1; // Mark as visiting

        if (courseMap.containsKey(course)) {
            for (int nextCourse : courseMap.get(course)) {
                if (hasCycle(nextCourse, courseMap, visited)) {
                    return true;
                }
            }
        }

        visited[course] = 2; // Mark as fully visited (no cycle)
        return false;
    }
}

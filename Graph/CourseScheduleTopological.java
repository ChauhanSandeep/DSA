package Graph;

import java.util.*;

public class CourseScheduleTopological {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Step 1: Initialize adjacency list and in-degree array
        List<List<Integer>> graph = new ArrayList<>(numCourses);
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] relation : prerequisites) {
            int course = relation[0], prereq = relation[1];
            graph.get(prereq).add(course);
            inDegree[course]++;
        }

        // Step 2: Find all courses with no prerequisites (in-degree 0)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        // Step 3: Process courses using BFS
        int processedCourses = 0;
        while (!queue.isEmpty()) {
            int current = queue.poll();
            processedCourses++;

            for (int neighbor : graph.get(current)) {
                if (--inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Step 4: If all courses were processed, return true; otherwise, a cycle exists
        return processedCourses == numCourses;
    }

    public static void main(String[] args) {
        CourseScheduleTopological scheduler = new CourseScheduleTopological();
        int numCourses = 2;
        int[][] prerequisites = { {1, 0}, {0, 1} };

        System.out.println("Can finish courses: " + scheduler.canFinish(numCourses, prerequisites));
    }
}

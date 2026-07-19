package dynamicprogramming.scheduling;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Problem: Course Schedule III
 *
 * Choose the maximum number of sequential courses where each course has a duration and must finish by its last day.
 *
 * Leetcode: https://leetcode.com/problems/course-schedule-iii/ (Hard)
 * Rating:   not available (not a contest problem)
 * Pattern:  Greedy | Scheduling by deadline | Max-heap replacement
 *
 * Example:
 *   Input:  courses = [[100,200],[200,1300],[1000,1250],[2000,3200]]
 *   Output: 3
 *   Why:    replacing the longest chosen course keeps total time small enough for deadlines.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Maximum Profit in Job Scheduling (1235).
 */
public class CourseScheduleIII {

    public static void main(String[] args) {
        CourseScheduleIII solution = new CourseScheduleIII();
        int[][][] courseCases = { {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}}, {{1, 2}}, {} };
        int[] expected = {3, 1, 0};
        for (int i = 0; i < courseCases.length; i++) {
            int[][] input = Arrays.stream(courseCases[i]).map(int[]::clone).toArray(int[][]::new);
            int got = solution.scheduleCourse(input);
            System.out.printf("courses=%s -> %d  expected=%d%n", Arrays.deepToString(courseCases[i]), got, expected[i]);
        }
    }


        /**
     * Intuition: after sorting by deadline, the selected courses should have the smallest total duration for their count. If adding a course misses a deadline, removing the longest selected duration frees the most time while losing one course.
     *
     * Algorithm:
     *   1. Sort courses by deadline.
     *   2. Store selected durations in a max-heap.
     *   3. Add each duration to currentTime and the heap.
     *   4. If the deadline is missed, remove the longest selected duration.
     *   5. Return the heap size.
     *
     * Time:  O(n log n) - sorting and heap operations.
     * Space: O(n) - heap of selected durations.
     *
     * @param courses [duration, lastDay] pairs
     * @return maximum courses completed on time
     */
public int scheduleCourse(int[][] courses) {
        // Sort courses by their end times (lastDayi)
        Arrays.sort(courses, (a, b) -> {
            if (a[1] == b[1]) {
                return a[0] - b[0]; // sort by duration if end times are the same
            } else
                return a[1] - b[1]; // sort by end time
        });

        // Max-heap to store the durations of the courses taken
        PriorityQueue<Integer> durationsMaxHeap = new PriorityQueue<>((a, b) -> b - a);

        int currentTime = 0;

        for (int[] course : courses) {
            int duration = course[0];
            int lastDay = course[1];

            // Add the current course's duration to our schedule
            currentTime += duration;
            durationsMaxHeap.offer(duration);

            // If we've exceeded the deadline, remove the longest course to make room for
            // the current one
            // This means that we are replacing a longer course with a shorter one to meet
            // the deadline
            if (currentTime > lastDay) {
                currentTime -= durationsMaxHeap.poll();
            }
        }

        return durationsMaxHeap.size();
    }

    /**
     * Dynamic Programming solution for smaller inputs
     *
     * This solution uses a 2D DP array where dp[i][j] represents the minimum time
     * to take j courses
     * from the first i courses. This approach is less efficient but works for
     * smaller inputs.
     *
     * Time Complexity: O(n²) where n is the number of courses
     * Space Complexity: O(n²)
     */
    public int scheduleCourseDP(int[][] courses) {
        // Sort courses by their end times
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);

        int len = courses.length;

        // dp[courseIndex][numCoursesTaken] = minimum time to take numCoursesTaken
        // courses from first courseIndex courses
        int[][] dp = new int[len + 1][len + 1];

        // Initialize with a large value (impossible state)
        for (int courseIndex = 0; courseIndex <= len; courseIndex++) {
            Arrays.fill(dp[courseIndex], Integer.MAX_VALUE);
        }

        // Base case: 0 courses from any number of available courses takes 0 time
        for (int courseIndex = 0; courseIndex <= len; courseIndex++) {
            dp[courseIndex][0] = 0;
        }

        int maxCourses = 0;

        // For each course (1-indexed)
        for (int courseIndex = 1; courseIndex <= len; courseIndex++) {
            int duration = courses[courseIndex - 1][0];
            int lastDay = courses[courseIndex - 1][1];

            // For each possible number of courses taken (0 to courseIndex)
            for (int numCoursesTaken = 0; numCoursesTaken <= courseIndex; numCoursesTaken++) {
                // Option 1: Don't take the current course
                dp[courseIndex][numCoursesTaken] = dp[courseIndex - 1][numCoursesTaken];

                // Option 2: Take the current course (if possible)
                if (numCoursesTaken > 0 && dp[courseIndex - 1][numCoursesTaken - 1] != Integer.MAX_VALUE) {
                    int timeWithCurrentCourse = dp[courseIndex - 1][numCoursesTaken - 1] + duration;

                    // Only take this course if we can finish it before the deadline
                    if (timeWithCurrentCourse <= lastDay) {
                        dp[courseIndex][numCoursesTaken] = Math.min(dp[courseIndex][numCoursesTaken],
                                timeWithCurrentCourse);
                    }
                }

                // Track the maximum number of courses we can take
                if (dp[courseIndex][numCoursesTaken] != Integer.MAX_VALUE) {
                    maxCourses = Math.max(maxCourses, numCoursesTaken);
                }
            }
        }

        return maxCourses;
    }
}

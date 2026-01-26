package dynamicprogramming.scheduling;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Problem: Course Schedule III (LeetCode 630)
 *
 * Problem Statement:
 * There are n different online courses numbered from 1 to n. You are given an
 * array 'courses' where
 * courses[i] = [durationi, lastDayi] indicate that the ith course should be
 * taken continuously for
 * durationi days and must be finished before or on lastDayi. You will start on
 * day 1. Return the
 * maximum number of courses that you can take.
 *
 * Example 1:
 * Input: courses = [[100, 200], [200, 1300], [1000, 1250], [2000, 3200]]
 * Output: 3
 * Explanation:
 * - Take the 1st course first, 100 days to finish on day 100.
 * - Take the 3rd course next, 1000 days to finish on day 1100.
 * - Take the 2nd course last, 200 days to finish on day 1300.
 * - The 4th course cannot be taken now since you will finish it on day 3300
 * which is beyond the last day.
 *
 * Approach:
 * This problem can be solved using a greedy approach with a max-heap. The key
 * insight is to prioritize
 * courses with earlier deadlines and to replace longer duration courses with
 * shorter ones when necessary.
 *
 * Steps to solve:
 * 1. Sort the courses by their end times (lastDayi). This allows us to consider
 * courses in the order
 * of their deadlines.
 * 2. Use a max-heap to keep track of the durations of the courses we've taken
 * so far.
 * 3. Iterate through each course, and for each course:
 * a. Add the current course's duration to our current time.
 * b. Add the duration to the max-heap.
 * c. If the current time exceeds the course's deadline, remove the course with
 * the longest duration
 * from the heap and subtract its duration from the current time.
 * 4. The size of the heap at the end will be the maximum number of courses that
 * can be taken.
 *
 * Time Complexity: O(n log n) - Sorting takes O(n log n) and each heap
 * operation takes O(log n)
 * Space Complexity: O(n) - For the heap which can store up to n elements
 *
 * Follow-up Questions:
 * 1. What if each course has a weight/priority and we want to maximize the
 * total weight instead of count?
 * Answer: We would need to modify the approach to consider weights when
 * deciding which course to replace.
 * This would require a more complex dynamic programming solution.
 *
 * 2. What if we can take multiple courses in parallel?
 * Answer: The problem would become similar to the interval scheduling problem
 * where we want to find
 * the maximum number of non-overlapping intervals.
 *
 * 3. What if some courses have prerequisites?
 * Answer: We would first need to perform a topological sort to determine a
 * valid course order before
 * applying the greedy approach.
 *
 * LeetCode: https://leetcode.com/problems/course-schedule-iii/
 */
public class CourseScheduleIII {

    /**
     * Calculates the maximum number of courses that can be taken.
     *
     * Steps to solve:
     * 1. Sort the courses by their end times to consider courses with earlier
     * deadlines first.
     * 2. Use a max-heap to keep track of the durations of the courses taken so far.
     * 3. Maintain a running total of the current time taken by the selected
     * courses.
     * 4. For each course:
     * a. Add the current course's duration to the total time.
     * b. Add the duration to the max-heap.
     * c. If the total time exceeds the current course's deadline, remove the
     * longest course from the heap
     * and subtract its duration from the total time.
     * 5. The size of the heap at the end represents the maximum number of courses
     * that can be taken.
     *
     * Time complexity: O(n log n)
     * - sorting the courses takes O(n log n)
     * - each insertion and deletion from the max-heap takes O(log n)
     * and we do this for each of the n courses.
     * Space complexity: O(n) for the max-heap
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

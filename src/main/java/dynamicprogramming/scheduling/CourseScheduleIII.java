package dynamicprogramming.scheduling;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Problem: Course Schedule III (LeetCode 630)
 *
 * Problem Statement:
 * There are n different online courses numbered from 1 to n. You are given an array 'courses' where
 * courses[i] = [durationi, lastDayi] indicate that the ith course should be taken continuously for
 * durationi days and must be finished before or on lastDayi. You will start on day 1. Return the
 * maximum number of courses that you can take.
 *
 * Example 1:
 * Input: courses = [[100, 200], [200, 1300], [1000, 1250], [2000, 3200]]
 * Output: 3
 * Explanation:
 * - Take the 1st course first, 100 days to finish on day 100.
 * - Take the 3rd course next, 1000 days to finish on day 1100.
 * - Take the 2nd course last, 200 days to finish on day 1300.
 * - The 4th course cannot be taken now since you will finish it on day 3300 which is beyond the last day.
 *
 * Approach:
 * This problem can be solved using a greedy approach with a max-heap. The key insight is to prioritize
 * courses with earlier deadlines and to replace longer duration courses with shorter ones when necessary.
 *
 * Steps to solve:
 * 1. Sort the courses by their end times (lastDayi). This allows us to consider courses in the order
 *    of their deadlines.
 * 2. Use a max-heap to keep track of the durations of the courses we've taken so far.
 * 3. Iterate through each course, and for each course:
 *    a. Add the current course's duration to our current time.
 *    b. Add the duration to the max-heap.
 *    c. If the current time exceeds the course's deadline, remove the course with the longest duration
 *       from the heap and subtract its duration from the current time.
 * 4. The size of the heap at the end will be the maximum number of courses that can be taken.
 *
 * Time Complexity: O(n log n) - Sorting takes O(n log n) and each heap operation takes O(log n)
 * Space Complexity: O(n) - For the heap which can store up to n elements
 *
 * Follow-up Questions:
 * 1. What if each course has a weight/priority and we want to maximize the total weight instead of count?
 *    Answer: We would need to modify the approach to consider weights when deciding which course to replace.
 *    This would require a more complex dynamic programming solution.
 *
 * 2. What if we can take multiple courses in parallel?
 *    Answer: The problem would become similar to the interval scheduling problem where we want to find
 *    the maximum number of non-overlapping intervals.
 *
 * 3. What if some courses have prerequisites?
 *    Answer: We would first need to perform a topological sort to determine a valid course order before
 *    applying the greedy approach.
 *
 * LeetCode: https://leetcode.com/problems/course-schedule-iii/
 */
public class CourseScheduleIII {

    /**
     * Calculates the maximum number of courses that can be taken.
     *
     * Steps to solve:
     * 1. Sort the courses by their end times to consider courses with earlier deadlines first.
     * 2. Use a max-heap to keep track of the durations of the courses taken so far.
     * 3. Maintain a running total of the current time taken by the selected courses.
     * 4. For each course:
     *    a. Add the current course's duration to the total time.
     *    b. Add the duration to the max-heap.
     *    c. If the total time exceeds the current course's deadline, remove the longest course from the heap
     *       and subtract its duration from the total time.
     * 5. The size of the heap at the end represents the maximum number of courses that can be taken.
     *
     * @param courses 2D array where courses[i] = [durationi, lastDayi]
     * @return Maximum number of courses that can be taken
     */
    public int scheduleCourse(int[][] courses) {
        // Sort courses by their end times (lastDayi)
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);

        // Max-heap to store the durations of the courses taken
        PriorityQueue<Integer> durationsMaxHeap = new PriorityQueue<>((a, b) -> b - a);

        int currentTime = 0;

        for (int[] course : courses) {
            int duration = course[0];
            int lastDay = course[1];

            // Add the current course's duration to our schedule
            currentTime += duration;
            durationsMaxHeap.offer(duration);

            // If we've exceeded the deadline, remove the longest course to make room for the current one
            // This means that we are replacing a longer course with a shorter one to meet the deadline
            if (currentTime > lastDay) {
                currentTime -= durationsMaxHeap.poll();
            }
        }

        return durationsMaxHeap.size();
    }

    /**
     * Alternative solution with early termination for large inputs
     *
     * This version includes optimizations for edge cases and large inputs:
     * 1. Skips courses that are impossible to complete (duration > lastDay)
     * 2. Early termination if all remaining courses cannot improve the result
     */
    public int scheduleCourseOptimized(int[][] courses) {
        // Sort courses by their end times
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        int currentTime = 0;
        int maxCourses = 0;

        for (int i = 0; i < courses.length; i++) {
            int duration = courses[i][0];
            int lastDay = courses[i][1];

            // Skip courses that cannot be completed even if taken alone
            if (duration > lastDay) {
                continue;
            }

            // Early termination if we can't take any more courses
            if (currentTime + duration > lastDay && !maxHeap.isEmpty() && maxHeap.peek() > duration) {
                // If we can't take this course and it's shorter than the longest course taken,
                // we can skip it since it won't help us take more courses
                continue;
            }

            currentTime += duration;
            maxHeap.offer(duration);

            // If we've exceeded the deadline, remove the longest course
            if (currentTime > lastDay) {
                currentTime -= maxHeap.poll();
            }

            // Update the maximum number of courses taken so far
            maxCourses = Math.max(maxCourses, maxHeap.size());

            // Early termination if we've taken all possible courses
            if (maxCourses == courses.length) {
                break;
            }
        }

        return maxCourses;
    }

    /**
     * Dynamic Programming solution for smaller inputs
     *
     * This solution uses a 2D DP array where dp[i][j] represents the minimum time to take j courses
     * from the first i courses. This approach is less efficient but works for smaller inputs.
     *
     * Time Complexity: O(n * m) where n is the number of courses and m is the maximum number of courses that can be taken
     * Space Complexity: O(n * m)
     */
    public int scheduleCourseDP(int[][] courses) {
        // Sort courses by their end times
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);

        // Find the maximum number of courses that can be taken
        int n = courses.length;
        int maxCourses = 0;

        // dp[i][j] = minimum time to take j courses from first i courses
        // We only need to track the previous row, so we can optimize space
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0; // Base case: 0 courses take 0 time

        for (int i = 0; i < n; i++) {
            int duration = courses[i][0];
            int lastDay = courses[i][1];

            // Iterate backwards to avoid using the same course multiple times
            for (int j = i + 1; j > 0; j--) {
                if (dp[j - 1] != Integer.MAX_VALUE && dp[j - 1] + duration <= lastDay) {
                    dp[j] = Math.min(dp[j], dp[j - 1] + duration);
                    maxCourses = Math.max(maxCourses, j);
                }
            }
        }

        return maxCourses;
    }
}

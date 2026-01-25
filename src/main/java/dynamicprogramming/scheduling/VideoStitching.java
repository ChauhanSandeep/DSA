package dynamicprogramming.scheduling;

import java.util.Arrays;

/**
 * Problem: Video Stitching (LeetCode #1024)
 *
 * Problem Statement:
 * You are given a series of video clips from a sporting event that lasted time seconds.
 * These video clips can be overlapping with each other and have varying lengths.
 * Each video clip is described as an array clips where clips[i] = [starti, endi] indicates
 * that the ith clip started at starti and ended at endi.
 *
 * We can cut these clips into segments freely. For example, a clip [0, 7] can be cut into
 * segments [0, 1] + [1, 3] + [3, 7].
 *
 * Return the minimum number of clips needed so that we can cut the clips into segments that
 * cover the entire sporting event [0, time]. If the task is impossible, return -1.
 *
 * Example 1:
 * Input: clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
 * Output: 3
 * Explanation: We take the clips [0,2], [8,10], [1,9]; a total of 3 clips to cover [0,10].
 *
 * Example 2:
 * Input: clips = [[0,1],[1,2]], time = 5
 * Output: -1
 * Explanation: We can't cover [0,5] with only [0,1] and [1,2].
 *
 * Approaches:
 * 1. Greedy Approach (Optimal): O(n log n) time, O(1) space
 *    - Sort clips by start time
 *    - Use a greedy approach to select the clip that extends the current end the farthest
 *
 * 2. Dynamic Programming: O(n * time) time, O(time) space
 *    - dp[i] represents the minimum clips needed to cover time [0, i]
 *    - Initialize dp[0] = 0, others to infinity
 *    - For each time t, update dp[end] using dp[start] + 1 if it's better
 *
 * Time Complexity: O(n log n) for optimal solution
 * Space Complexity: O(1) for optimal solution
 *
 * Follow-up Questions:
 * 1. What if we need to return the actual clips used instead of just the count?
 *    Answer: We can modify the solution to track the selected clips' indices or ranges.
 *
 * 2. How would you handle very large time values (e.g., 10^9)?
 *    Answer: The greedy approach would be more efficient as it doesn't depend on the time value.
 *
 * 3. What if clips can have negative start or end times?
 *    Answer: We would need to adjust the approach to handle negative indices or normalize them.
 *
 * LeetCode: https://leetcode.com/problems/video-stitching/
 * LeetCode Contest Rating: 1746
 */
public class VideoStitching {

    /**
     * Greedy Solution (Optimal)
     *
     * Approach:
     * 1. Sort clips by their start time
     * 2. Use a greedy approach to select the clip that extends the current end the farthest
     * 3. Keep track of the current end and the farthest end we can reach
     *
     * Time Complexity: O(n log n) due to sorting where n is the number of clips
     * Space Complexity: O(1)
     */
    public int videoStitching(int[][] clips, int targetTime) {
        // Sort clips by start time
        Arrays.sort(clips, (a, b) -> a[0] - b[0]);

        int clipsNeeded = 0;
        int currentEnd = 0;
        int farthest = 0;
        int i = 0;
        int clipsLength = clips.length;

        while (currentEnd < targetTime) {
            // Find the clip that starts before or at currentEnd and extends the farthest
            while (i < clipsLength && clips[i][0] <= currentEnd) {
                farthest = Math.max(farthest, clips[i][1]);
                i++;
            }

            // If we can't extend further
            if (farthest <= currentEnd) {
                return -1;
            }

            // Select the clip that gives the farthest reach
            currentEnd = farthest;
            clipsNeeded++;

            // If we've covered the entire time
            if (currentEnd >= targetTime) {
                return clipsNeeded;
            }
        }

        return -1;
    }

    /**
     * Optimized Greedy Solution
     *
     * Approach:
     * 1. Create an array to track the farthest end for each start time
     * 2. Iterate through the clips to fill this array
     * 3. Use a greedy approach to select clips based on this array
     * 4. Keep track of the current end and the farthest end we can reach
     * 5. Return the number of clips needed or -1 if impossible
     *
     * Time Complexity: O(n + time) where n is the number of clips and time is the target time
     * Because we iterate through the clips once and then through the time array once.
     * Space Complexity: O(time)
     */
    public int videoStitchingOptimized(int[][] clips, int targetTime) {
        // Create an array to track the farthest end for each start time
        int[] maxEnd = new int[targetTime + 1];

        // For each time t, maxEnd[t] is the maximum end time of any clip that starts at t
        for (int[] clip : clips) {
            int start = clip[0];
            int end = clip[1];

            if (start <= targetTime) {
                maxEnd[start] = Math.max(maxEnd[start], Math.min(end, targetTime));
            }
        }

        int clipsNeeded = 0;
        int currentClipEnd = 0;
        int farthestReachable = 0;

        for (int currentTime = 0; currentTime <= targetTime; currentTime++) {
            // Update the farthest we can reach
            farthestReachable = Math.max(farthestReachable, maxEnd[currentTime]);

            // If we can't reach further
            if (currentTime > farthestReachable) {
                return -1;
            }

            // If we've reached the end of current clip, select a new clip
            // which can extend our farthest reach and is found before the clip ends
            if (currentTime == currentClipEnd) {
                clipsNeeded++;
                currentClipEnd = farthestReachable;

                // If we've covered the entire time
                if (currentClipEnd >= targetTime) {
                    return clipsNeeded;
                }
            }
        }

        return -1;
    }

    /**
     * Dynamic Programming Solution
     *
     * Approach:
     * 1. dp[i] represents the minimum clips needed to cover time [0, i]
     * 2. Initialize dp[0] = 0, others to infinity
     * 3. For each clip, update dp[end] using dp[start] + 1 if it's better
     *
     * Time Complexity: O(n * time) where n is the number of clips and time is the target time
     * Space Complexity: O(time)
     */
    public int videoStitchingDP(int[][] clips, int time) {
        // dp[i] = minimum clips needed to cover time [0, i]
        int[] dp = new int[time + 1];
        Arrays.fill(dp, Integer.MAX_VALUE - 1);
        dp[0] = 0;  // Base case: 0 clips needed to cover [0,0]

        for (int t = 1; t <= time; t++) {
            for (int[] clip : clips) {
                int start = clip[0];
                int end = clip[1];

                // If this clip can cover time t
                if (start <= t && t <= end) {
                    // Update dp[t] to be the minimum of current value and dp[start] + 1
                    dp[t] = Math.min(dp[t], dp[start] + 1);
                }
            }

            // If we can't cover time t, return -1
            if (dp[t] == Integer.MAX_VALUE - 1) {
                return -1;
            }
        }

        return dp[time];
    }

    public static void main(String[] args) {
        VideoStitching solution = new VideoStitching();

        // Test cases
        int[][] clips1 = {{0,2},{4,6},{8,10},{1,9},{1,5},{5,9}};
        System.out.println("Test 1: " + solution.videoStitching(clips1, 10));  // Expected: 3

        int[][] clips2 = {{0,1},{1,2}};
        System.out.println("Test 2: " + solution.videoStitching(clips2, 5));   // Expected: -1

        int[][] clips3 = {{0,4},{2,8}};
        System.out.println("Test 3: " + solution.videoStitching(clips3, 5));   // Expected: 2

        // Test DP solution
        System.out.println("\nDP Solution:");
        System.out.println("Test 1: " + solution.videoStitchingDP(clips1, 10));  // Expected: 3
        System.out.println("Test 2: " + solution.videoStitchingDP(clips2, 5));   // Expected: -1

        // Test optimized solution
        System.out.println("\nOptimized Solution:");
        System.out.println("Test 1: " + solution.videoStitchingOptimized(clips1, 10));  // Expected: 3
        System.out.println("Test 2: " + solution.videoStitchingOptimized(clips2, 5));   // Expected: -1
    }
}

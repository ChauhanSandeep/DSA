package dynamicprogramming.scheduling;

import java.util.Arrays;

/**
 * Problem: Video Stitching
 *
 * Given video clips as intervals, return the minimum number needed to cover [0, targetTime], or -1 if coverage is impossible.
 *
 * Leetcode: https://leetcode.com/problems/video-stitching/ (Medium)
 * Rating:   contest Elo 1746
 * Pattern:  Greedy | Interval covering | Farthest reach
 *
 * Example:
 *   Input:  clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
 *   Output: 3
 *   Why:    [0,2], [1,9], and [8,10] cover the whole event.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Jump Game II (45), Minimum Number of Taps (1326).
 */
public class VideoStitching {

        /**
     * Intuition: currentEnd is the covered prefix. Among clips that start within that prefix, choosing the one with farthest end gives the largest possible extension for one clip; if none extends, coverage is impossible.
     *
     * Algorithm:
     *   1. Sort clips by start.
     *   2. While currentEnd is before targetTime, scan reachable clips.
     *   3. Track the farthest endpoint.
     *   4. Return -1 if farthest does not advance.
     *   5. Commit one clip and move currentEnd to farthest.
     *
     * Time:  O(n log n) - sorting dominates.
     * Space: O(1) - only counters are stored.
     *
     * @param clips [start, end] clips
     * @param targetTime target interval end
     * @return minimum clips or -1
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
        int[][][] clipCases = { {{0, 2}, {4, 6}, {8, 10}, {1, 9}, {1, 5}, {5, 9}}, {{0, 1}, {1, 2}}, {{0, 4}, {2, 8}} };
        int[] targetTimes = {10, 5, 5};
        int[] expected = {3, -1, 2};
        for (int i = 0; i < clipCases.length; i++) {
            int[][] input = Arrays.stream(clipCases[i]).map(int[]::clone).toArray(int[][]::new);
            int got = solution.videoStitching(input, targetTimes[i]);
            System.out.printf("clips=%s time=%d -> %d  expected=%d%n", Arrays.deepToString(clipCases[i]), targetTimes[i], got, expected[i]);
        }
    }
}

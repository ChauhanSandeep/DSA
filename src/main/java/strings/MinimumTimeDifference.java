package strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * Problem: Minimum Time Difference
 *
 * Given 24-hour time points in HH:MM format, return the smallest difference in
 * minutes between any two points, including the wraparound gap across midnight.
 *
 * Leetcode: https://leetcode.com/problems/minimum-time-difference/ (Medium)
 * Rating:   acceptance 62.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  String | Time conversion | Sorting circular gaps
 *
 * Example:
 *   Input:  timePoints = ["23:59", "00:00"]
 *   Output: 1
 *   Why:    00:00 is one minute after 23:59 on a circular clock.
 *
 * Follow-ups:
 *   1. Better than sorting? Use a fixed 1440-slot boolean bucket.
 *   2. Include seconds? Convert to seconds and use an 86400-slot bucket.
 *   3. Time zones? Normalize all times before converting to minutes.
 */
public class MinimumTimeDifference {

    public static void main(String[] args) {
        MinimumTimeDifference solver = new MinimumTimeDifference();
        List<List<String>> inputs = Arrays.asList(Arrays.asList("23:59", "00:00"), Arrays.asList("00:00", "23:59", "00:00"), Arrays.asList("01:01", "02:01", "03:00"));
        int[] expected = {1, 0, 59};
        for (int i = 0; i < inputs.size(); i++) {
            int got = solver.findMinDifference(inputs.get(i));
            System.out.printf("timePoints=%s -> %d  expected=%d%n", inputs.get(i), got, expected[i]);
        }
    }


        /**
     * Intuition: clock times are points on a 1440-minute circle. After sorting,
     * the minimum gap must be between adjacent points, including the wraparound
     * pair from the last time back to the first.
     *
     * Algorithm:
     *   1. Return 0 for too few points or more than 1440 points.
     *   2. Convert each HH:MM time to minutes.
     *   3. Sort minutes and append first + 1440 for wraparound.
     *   4. Return the minimum adjacent difference.
     *
     * Time:  O(n log n) - sorting dominates.
     * Space: O(n) - stores converted minutes.
     */
    public int findMinDifference(List<String> timePoints) {
        if (timePoints == null || timePoints.size() < 2) {
            return 0;
        }

        // Early optimization: if more than 1440 time points, must have duplicates
        if (timePoints.size() > 1440) {
            return 0;
        }

        List<Integer> minutes = new ArrayList<>();

        // Convert all time points to minutes
        for (String timePoint : timePoints) {
            minutes.add(convertToMinutes(timePoint));
        }

        Collections.sort(minutes);

        // Add first element + 24 hours to handle circular time
        minutes.add(minutes.get(0) + 1440); // 24 * 60 = 1440 minutes

        int minDifference = Integer.MAX_VALUE;

        // Find minimum difference between consecutive time points
        for (int i = 1; i < minutes.size(); i++) {
            minDifference = Math.min(minDifference, minutes.get(i) - minutes.get(i - 1));
        }

        return minDifference;
    }

    // Helper method to convert "HH:MM" to total minutes
    /** Converts an HH:MM timestamp into minutes since midnight. */
    private int convertToMinutes(String timePoint) {
        String[] parts = timePoint.split(":");
        int hours = Integer.parseInt(parts[0]);
        int mins = Integer.parseInt(parts[1]);
        return hours * 60 + mins;
    }

    /**
     * Alternative approach using boolean array for O(n) time complexity.
     * Steps:
     * 1. Use a boolean array of size 1440 to mark existing time points.
     * 2. If a time point already exists, return 0 immediately.
     * 3. Traverse the boolean array to find minimum difference between consecutive time points.
     * 4. Handle circular difference between last and first time points.
     *
     * Time Complexity: O(n) where n is number of time points
     * Space Complexity: O(1) since the size of boolean array is fixed (1440)
     *
     * @param timePoints List of time strings in "HH:MM" format
     * @return Minimum difference in minutes between any two time points
     */
    public int findMinDifferenceOptimized(List<String> timePoints) {
        boolean[] timeExists = new boolean[1440]; // 24 * 60 minutes in a day

        for (String timePoint : timePoints) {
            int minutes = convertToMinutes(timePoint);

            // If time already exists, minimum difference is 0
            if (timeExists[minutes]) {
                return 0;
            }
            timeExists[minutes] = true;
        }

        int firstTime = -1; // First time point found in array
        int lastTime = -1; // Last time point found in array
        int prevTime = -1; // Previous time point for difference calculation
        int minDiff = Integer.MAX_VALUE;

        for (int i = 0; i < 1440; i++) {
            if (timeExists[i]) {
                if (firstTime == -1) {
                    firstTime = i;
                }
                lastTime = i;

                if (prevTime != -1) {
                    minDiff = Math.min(minDiff, i - prevTime);
                }
                prevTime = i;
            }
        }

        // Check circular difference (last to first + 24 hours)
        minDiff = Math.min(minDiff, (firstTime + 1440) - lastTime);

        return minDiff;
    }
}

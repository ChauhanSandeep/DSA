package strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LeetCode 539. Minimum Time Difference
 *
 * Given a list of 24-hour clock time points in "HH:MM" format, return the minimum
 * minutes difference between any two time-points in the list.
 *
 * Example 1:
 * Input: timePoints = ["23:59","00:00"]
 * Output: 1
 * Explanation: The difference between "23:59" and "00:00" is 1 minute.
 *
 * Example 2:
 * Input: timePoints = ["00:00","23:59","00:00"]
 * Output: 0
 * Explanation: There are duplicate time points, so minimum difference is 0.
 *
 * LeetCode Link: https://leetcode.com/problems/minimum-time-difference/
 *
 * Follow-up Questions:
 * - How would you handle different time formats? (Parse format and convert to minutes)
 * - Can you optimize for very large input with limited time range? (Use boolean array for O(1) lookup)
 * - How would you extend to handle time zones? (Convert all times to UTC before processing)
 * - What if we need k smallest differences instead of just minimum? (Use priority queue or sorting)
 */
public class MinimumTimeDifference {

    /**
     * Finds minimum time difference by converting to minutes and sorting.
     *
     * Algorithm:
     * 1. Convert all time points to minutes since 00:00
     * 2. Sort the minutes array
     * 3. Check consecutive differences in sorted array
     * 4. Handle circular case by adding first time + 24 hours to end
     * 5. Return minimum difference found
     *
     * Time Complexity: O(n log n) where n is number of time points
     * Space Complexity: O(n) for storing converted minutes
     *
     * @param timePoints List of time strings in "HH:MM" format
     * @return Minimum difference in minutes between any two time points
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
    private int convertToMinutes(String timePoint) {
        String[] parts = timePoint.split(":");
        int hours = Integer.parseInt(parts[0]);
        int mins = Integer.parseInt(parts[1]);
        return hours * 60 + mins;
    }

    /**
     * Alternative approach using boolean array for O(n) time complexity.
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

        // Find first and last time points, and minimum consecutive difference
        int firstTime = -1, lastTime = -1, prevTime = -1;
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
        minDiff = Math.min(minDiff, firstTime + 1440 - lastTime);

        return minDiff;
    }
}

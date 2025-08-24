package array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Problem Statement:
 * You are given an array of non-overlapping intervals sorted by their start times, and a new interval to insert.
 * Insert the new interval into the list such that the resulting list remains sorted and non-overlapping.
 * Merge intervals if necessary.
 *
 * Example:
 * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
 * Output: [[1,5],[6,9]]
 * Explanation: [2,5] overlaps with [1,3], so they merge into [1,5].
 *
 * Leetcode URL:
 * https://leetcode.com/problems/insert-interval
 *
 * Follow-up Questions:
 * 1. How would you handle this if the intervals were not sorted initially?
 *    → Sort the intervals by start time first, then use the same logic. Time complexity becomes O(n log n).
 *
 * 2. Can you do this in-place for large datasets?
 *    → Yes, if you’re allowed to modify the input list directly, use a single pass with an output index tracker.
 */

public class InsertInterval {

    /**
     * Inserts a new interval into the sorted list of non-overlapping intervals and merges if necessary.
     *
     * Algorithm:
     * - Add all intervals that end before the new interval starts.
     * - Merge overlapping intervals with the new interval.
     * - Add all intervals that start after the new interval ends.
     *
     * Time Complexity: O(n), where n = number of intervals
     * Space Complexity: O(n), for storing the result
     *
     * @param intervals Array of existing intervals, sorted and non-overlapping
     * @param newInterval Interval to be inserted
     * @return Updated list of merged intervals
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0;
        int length = intervals.length;

        // Add intervals before newInterval starts
        while (i < length && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }

        // Merge all overlapping intervals with newInterval
        while (i < length && intervals[i][0] <= newInterval[1]) {
            // Update the newInterval to include current interval
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }

        // Add the merged newInterval
        result.add(newInterval);

        // Add remaining intervals after newInterval
        while (i < length) {
            result.add(intervals[i]);
            i++;
        }

        // Convert list to array
        return result.toArray(new int[0][]); // 0 indicates dynamic size
    }

    /**
     * Brute-force variation: add the new interval, sort all, and merge all overlaps.
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int[][] insertBruteForce(int[][] intervals, int[] newInterval) {
        List<int[]> all = new ArrayList<>(Arrays.asList(intervals));
        all.add(newInterval);

        // Sort by start time
        all.sort(Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();
        int[] current = all.get(0);

        for (int i = 1; i < all.size(); i++) {
            int[] next = all.get(i);
            if (current[1] >= next[0]) {
                // Overlap detected, merge intervals
                current[1] = Math.max(current[1], next[1]);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged.toArray(new int[merged.size()][]);
    }
}
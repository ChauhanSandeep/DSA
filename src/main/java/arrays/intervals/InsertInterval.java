package arrays.intervals;

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
     * Inserts new interval into sorted non-overlapping intervals and merges if necessary.
     *
     * Algorithm:
     * 1. Add all intervals that end before new interval starts (no overlap)
     * 2. Merge all intervals that overlap with new interval
     * 3. Add all intervals that start after merged interval ends (no overlap)
     *
     * Key insight: Partition intervals into three groups based on their relationship
     * with the new interval: those before, those overlapping, and those after. Process
     * each group sequentially without needing to sort.
     *
     * Time Complexity: O(N) where N is number of intervals. Single pass through intervals.
     *
     * Space Complexity: O(N) for result list (excluding output array).
     *
     * @param intervals array of non-overlapping sorted intervals
     * @param newInterval interval to insert [start, end]
     * @return array of intervals after insertion with merges applied
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();

        int newStart = newInterval[0];
        int newEnd = newInterval[1];

        int currentIndex = 0;
        int length = intervals.length;

        // Phase 1: Add all intervals that end before new interval starts
        while (currentIndex < length && intervals[currentIndex][1] < newStart) {
            result.add(intervals[currentIndex]);
            currentIndex++;
        }

        // Phase 2: Merge all overlapping intervals into new interval
        while (currentIndex < length && intervals[currentIndex][0] <= newEnd) {
            // Extend new interval to encompass current interval
            newStart = Math.min(newStart, intervals[currentIndex][0]);
            newEnd = Math.max(newEnd, intervals[currentIndex][1]);
            currentIndex++;
        }

        // Add the merged interval
        result.add(new int[]{newStart, newEnd});

        // Phase 3: Add all remaining intervals that start after merged interval ends
        while (currentIndex < length) {
            result.add(intervals[currentIndex]);
            currentIndex++;
        }

        return result.toArray(new int[result.size()][]);
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
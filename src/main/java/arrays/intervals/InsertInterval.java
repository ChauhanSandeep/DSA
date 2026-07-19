package arrays.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Problem: Insert Interval
 *
 * Given sorted, non-overlapping intervals and one new interval, insert the new
 * interval so the final list is still sorted and non-overlapping. Any intervals
 * that touch or overlap the new interval must be merged into one interval.
 *
 * Leetcode: https://leetcode.com/problems/insert-interval/
 * Rating:   acceptance 45.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | Intervals | Three-phase merge
 *
 * Example:
 *   Input:  intervals = [[1,3],[6,9]], newInterval = [2,5]
 *   Output: [[1,5],[6,9]]
 *   Why:    [2,5] overlaps [1,3], so they combine into [1,5], while [6,9]
 *           stays separate.
 *
 * Follow-ups:
 *   1. What if the input intervals are unsorted?
 *      Add the interval, sort by start time, then run the standard merge pass.
 *   2. What if intervals are streamed in sorted order?
 *      Emit intervals before the new one, merge overlaps, then stream the rest.
 *   3. What if closed intervals that only touch should not merge?
 *      Change the overlap condition from start <= end to start < end.
 *
 * Related: Merge Intervals (56), Non-overlapping Intervals (435).
 */

public class InsertInterval {

    public static void main(String[] args) {
        InsertInterval solver = new InsertInterval();
        int[][][] intervals = {{{1, 3}, {6, 9}}, {{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}}, {}};
        int[][] newIntervals = {{2, 5}, {4, 8}, {5, 7}};
        String[] expected = {"[[1, 5], [6, 9]]", "[[1, 2], [3, 10], [12, 16]]", "[[5, 7]]"};

        for (int i = 0; i < intervals.length; i++) {
            int[][] input = Arrays.stream(intervals[i]).map(int[]::clone).toArray(int[][]::new);
            int[][] got = solver.insert(input, newIntervals[i]);
            System.out.printf("intervals=%s new=%s -> %s  expected=%s%n",
                Arrays.deepToString(intervals[i]), Arrays.toString(newIntervals[i]),
                Arrays.deepToString(got), expected[i]);
        }
    }


    /**
     * Intuition (interview default): because the existing intervals are already
     * sorted and non-overlapping, every interval falls into exactly one of three
     * groups: completely before the new interval, overlapping it, or completely
     * after it. We can copy the before group unchanged, stretch the new interval
     * across all overlaps, and then copy the after group unchanged. No sorting is
     * needed because we never disturb the original order.
     *
     * Time:  O(n) - each existing interval is visited at most once.
     * Space: O(n) - the returned array stores the merged interval list.
     *
     * @param intervals sorted non-overlapping intervals
     * @param newInterval interval to insert
     * @return intervals after insertion and merging
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

package arrays.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Merge Intervals
 *
 * Given a list of intervals, merge every group of overlapping intervals and
 * return a list of non-overlapping intervals that covers the same ranges. The
 * input may be unsorted, so intervals that belong together may appear far apart.
 *
 * Leetcode: https://leetcode.com/problems/merge-intervals/
 * Rating:   acceptance 52.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | Intervals | Sort then linear merge
 *
 * Example:
 *   Input:  [[1,3],[2,6],[8,10],[15,18]]
 *   Output: [[1,6],[8,10],[15,18]]
 *   Why:    [1,3] and [2,6] overlap and merge into [1,6]; the other intervals
 *           start after the previous merged end.
 *
 * Follow-ups:
 *   1. What if the intervals are already sorted?
 *      Skip sorting and merge in one O(n) scan.
 *   2. What if touching intervals should remain separate?
 *      Use nextStart < currentEnd instead of nextStart <= currentEnd.
 *   3. What if intervals arrive as an infinite sorted stream?
 *      Keep only the current merged interval and emit it when the next one starts after it.
 *
 * Related: Insert Interval (57), Meeting Rooms II (253).
 */
public class MergeIntervals {
    public static void main(String[] args) {
        int[][][] inputs = {{{1, 3}, {2, 6}, {8, 10}, {15, 18}}, {{1, 4}, {4, 5}}, {{1, 4}}};
        String[] expected = {"[[1, 6], [8, 10], [15, 18]]", "[[1, 5]]", "[[1, 4]]"};

        for (int i = 0; i < inputs.length; i++) {
            int[][] input = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
            int[][] got = merge(input);
            System.out.printf("intervals=%s -> %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), Arrays.deepToString(got), expected[i]);
        }
    }

    /**
     * Intuition: overlapping intervals are hard to find in arbitrary order, but
     * sorting by start time makes every possible overlap local. Once sorted, the
     * current interval can only overlap the last interval we have already merged;
     * it cannot reach back past that because earlier merged intervals ended even
     * sooner. So we either stretch the last merged interval or start a new one.
     * Containment is handled by taking the larger end value.
     *
     * Time:  O(n log n) - sorting dominates the single merge scan.
     * Space: O(n) - the returned merged list can contain every interval when none overlap.
     *
     * @param intervals array of [start, end] intervals
     * @return merged non-overlapping intervals
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        // Sort by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> merged = new ArrayList<>();
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);

        for (int i = 1; i < intervals.length; i++) {
            int currentEnd = currentInterval[1];
            int nextStart = intervals[i][0];
            int nextEnd = intervals[i][1];

            // Check if intervals overlap
            if (nextStart <= currentEnd) {
                // Merge: extend end time to maximum of both
                currentInterval[1] = Math.max(currentEnd, nextEnd);
            } else {
                // No overlap: add as new interval
                currentInterval = intervals[i];
                merged.add(currentInterval);
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }
}
package arrays.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Merge Intervals
 * Leetcode: https://leetcode.com/problems/merge-intervals/
 *
 * Given an array of intervals where intervals[i] = [start_i, end_i], merge all
 * overlapping intervals, and return an array of the non-overlapping intervals
 * that cover all the intervals in the input.
 *
 * Example:
 * Input: [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 *
 * Follow-up Questions:
 * 1. What if the input is already sorted?
 *    → You can skip sorting and start merging directly in O(N) time.
 * 2. Can you do this in-place?
 *    → Not easily with primitives (`int[][]`) in Java. You’d need to manage indices manually.
 * 3. How to handle huge data (millions of intervals)?
 *    → Use streaming (e.g., sorted iterator) to avoid keeping everything in memory.
 */
public class MergeIntervals {

    public static void main(String[] args) {
        int[][] intervals = {
            {1, 3},
            {2, 6},
            {8, 10},
            {15, 18}
        };
        int[][] merged = merge(intervals);
        System.out.println("Merged Intervals: " + Arrays.deepToString(merged));
    }

    /**
     * Merges overlapping intervals using sorting and linear scan.
     *
     * Algorithm:
     * 1. Sort intervals by start time to ensure ordered processing
     * 2. Initialize result with first interval
     * 3. For each subsequent interval:
     *    - If it overlaps with last merged interval, extend the end time
     *    - Otherwise, add as new separate interval
     * 4. Return merged intervals
     *
     * Key insight: After sorting by start time, we only need to compare each interval
     * with the previously merged interval. If current interval's start <= previous end,
     * they overlap. We take the maximum of both ends to handle containment cases.
     *
     * Time Complexity: O(N log N) where N is number of intervals. Sorting dominates
     * with O(N log N), followed by O(N) linear scan for merging.
     *
     * Space Complexity: O(log N) for sorting algorithm's stack space (Timsort/Quicksort).
     * Output array not counted as it's required space, not auxiliary space.
     *
     * @param intervals array of intervals where each interval is [start, end]
     * @return array of merged non-overlapping intervals
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
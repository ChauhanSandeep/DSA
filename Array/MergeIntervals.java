package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 🔹 Problem: Merge Intervals
 * 🔗 Leetcode: https://leetcode.com/problems/merge-intervals/
 *
 * Given an array of intervals where intervals[i] = [start_i, end_i], merge all
 * overlapping intervals, and return an array of the non-overlapping intervals
 * that cover all the intervals in the input.
 *
 * 📌 Example:
 * Input: [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 *
 * ✅ Follow-up Questions:
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
        int[][] merged = mergeOverlappingIntervals(intervals);
        System.out.println("Merged Intervals: " + Arrays.deepToString(merged));
    }

    /**
     * Merges all overlapping intervals from the given list.
     *
     * 🔹 Steps:
     * 1. Sort intervals by their starting point.
     * 2. Initialize `currentStart` and `currentEnd` with the first interval.
     * 3. Iterate over remaining intervals:
     *    - If overlapping (interval's start ≤ current end), update current end.
     *    - Else, store the previous merged interval and update start/end.
     * 4. Don't forget to add the last interval at the end.
     *
     * 🔹 Time Complexity: O(N log N)
     *      - Sorting takes O(N log N), merging is O(N)
     * 🔹 Space Complexity: O(N)
     *      - For output list of merged intervals
     *
     * @param intervals 2D array where each element is [start, end] interval
     * @return merged non-overlapping intervals
     */
    public static int[][] mergeOverlappingIntervals(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) return intervals;

        // Sort intervals based on their start time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();
        int currentStart = intervals[0][0];
        int currentEnd = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            int nextStart = intervals[i][0];
            int nextEnd = intervals[i][1];

            if (nextStart <= currentEnd) {
                // Overlapping: Extend the current interval
                currentEnd = Math.max(currentEnd, nextEnd);
            } else {
                // No overlap: Add the previous interval and start a new one
                merged.add(new int[]{currentStart, currentEnd});
                currentStart = nextStart;
                currentEnd = nextEnd;
            }
        }

        // Add the final interval
        merged.add(new int[]{currentStart, currentEnd});

        // Convert the list back to an array
        return merged.toArray(new int[0][]);
    }
}
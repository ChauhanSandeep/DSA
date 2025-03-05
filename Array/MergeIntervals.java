package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {

    public static void main(String[] args) {
        int[][] intervals = {
                {1, 3},
                {2, 6},
                {8, 10},
                {15, 18}
        };
        int[][] mergedIntervals = merge(intervals);
        System.out.println("Merged Intervals: " + Arrays.deepToString(mergedIntervals));
    }

    /**
     * Merges overlapping intervals.
     * Time Complexity: O(N log N) - due to sorting
     * Space Complexity: O(N) - for storing merged intervals
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) return intervals;

        // Sort intervals based on start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> mergedIntervals = new ArrayList<>();
        int start = intervals[0][0];
        int end = intervals[0][1];

        // Iterate and merge overlapping intervals
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= end) {  // Overlapping interval
                end = Math.max(end, intervals[i][1]);
            } else {  // Non-overlapping interval
                mergedIntervals.add(new int[]{start, end});
                start = intervals[i][0];
                end = intervals[i][1];
            }
        }

        // Add the last interval
        mergedIntervals.add(new int[]{start, end});

        // Convert List<int[]> to int[][]
        return mergedIntervals.toArray(new int[0][]);
    }
}

package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Maximum Team Size With Overlapping Intervals
 *
 * Each employee has a closed interval. A team is valid when one captain overlaps every other member, even if non-captains do not overlap each other.
 *
 * Leetcode: https://leetcode.com/problems/maximum-team-size-with-overlapping-intervals/ (Medium)
 * Rating:   acceptance 43.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Intervals | Sorting endpoints | Binary search counts
 *
 * Example:
 *   Input:  startTime = [3,4,6], endTime = [8,5,7]
 *   Output: 3
 *   Why:    interval [3,8] overlaps both [4,5] and [6,7].
 *
 * Follow-ups:
 *   1. Return members? Save the best captain, then collect intervals overlapping it.
 *   2. Half-open intervals? Change the strict endpoint comparisons.
 *   3. Employee weights? Use sorted endpoints with prefix sums.
 *   4. Require pairwise overlap? Sweep endpoints for max concurrent intervals.
 *
 * Related: Meeting Rooms II (253), My Calendar III (732).
 */
public class MaximumTeamSizeWithOverlappingIntervals {

    public static void main(String[] args) {
        MaximumTeamSizeWithOverlappingIntervals solver = new MaximumTeamSizeWithOverlappingIntervals();
        int[][] starts = { {3,4,6}, {1,10,20}, {1} };
        int[][] ends = { {8,5,7}, {2,11,21}, {1} };
        int[] expected = { 3, 1, 1 };
        for (int i = 0; i < starts.length; i++) {
            int got = solver.maximumTeamSize(starts[i], ends[i]);
            System.out.printf("starts=%s ends=%s -> %d  expected=%d%n", Arrays.toString(starts[i]), Arrays.toString(ends[i]), got, expected[i]);
        }
    }


        /**
     * Intuition: For one captain [S, E], valid teammates are all intervals that overlap it. Count non-overlaps with sorted endpoints, then subtract from n.
     *
     * Algorithm:
     *   1. Clone and sort start and end arrays.
     *   2. For each captain, count ends < S and starts > E.
     *   3. Compute overlapping = n - endedBefore - startedAfter.
     *   4. Track the maximum overlapping count.
     *
     * Time:  O(n log n) - sorting plus two binary searches per interval.
     * Space: O(n) - sorted endpoint copies are stored.
     *
     * @param startTime inclusive starts
     * @param endTime inclusive ends
     * @return largest valid team size
     */
    public int maximumTeamSize(int[] startTime, int[] endTime) {
        if (startTime == null || startTime.length == 0) return 0;

        int len = startTime.length;

        // --- Step 1: sort copies of the endpoint arrays ------------------
        int[] sortedStarts = startTime.clone();
        int[] sortedEnds   = endTime.clone();
        Arrays.sort(sortedStarts);
        Arrays.sort(sortedEnds);

        // --- Step 2 & 3: count overlaps for each captain, track best ----
        int best = 0;
        for (int captain = 0; captain < len; captain++) {
            int captainStart = startTime[captain];
            int captainEnd   = endTime[captain];

            int endedBefore  = countSmaller(sortedEnds,    captainStart);     // ends   <  S
            int startedAfter = len - countGreater(sortedStarts, captainEnd); // starts >  E
            int overlapping  = len - endedBefore - startedAfter;

            if (overlapping > best) best = overlapping;
        }
        return best;
    }

    /**
     * Pattern 2 — array shape `F F F T T T T` for predicate `arr[i] >= target`.
     * Returns the smallest index i with arr[i] >= target, or arr.length if none.
     * For [`F F F T T T T` case returns index 3.
     */
    static int countSmaller(int[] arr, int target) {
        int left = 0;
        int right = arr.length;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] >= target) {
                right = mid;          // mid is a candidate, keep it
            } else {
                left = mid + 1;       // mid too small, discard
            }
        }

        return left;
    }

    /**
     * Pattern 2 — array shape `F F F T T T T` for predicate `arr[i] > key`.
     * Returns the smallest index i with arr[i] > key, or arr.length if none.
     * For `[T T T T F F F]` framing this is the first F, e.g. index 4.
     */
    static int countGreater(int[] arr, int key) {
        int left = 0;
        int right = arr.length;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] > key) {
                right = mid;          // mid is a candidate, keep it
            } else {
                left = mid + 1;       // arr[mid] <= key, discard
            }
        }

        return left;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------

}

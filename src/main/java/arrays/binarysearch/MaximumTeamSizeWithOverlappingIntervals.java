package arrays.binarysearch;

import java.util.Arrays;

/**
 * ✅ Problem: Maximum Team Size With Overlapping Intervals
 *
 * Each employee i has a closed interval [startTime[i], endTime[i]]. Two
 * employees can interact iff their intervals share at least one point.
 * A team is valid if there exists a "captain" inside it who interacts
 * with every other member (the others need not pairwise overlap).
 * Return the maximum possible team size.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/maximum-team-size-with-overlapping-intervals/   (Medium)
 * 🏷️ Pattern:  Intervals · Sorting · Binary search (firstTrue / Pattern 2)
 *
 * 🧪 Example:
 *   Input:  startTime = [3,4,6], endTime = [8,5,7]
 *   Output: 3                         // captain = 0 ([3,8]) overlaps both others
 *
 * 🚧 Edge cases to remember:
 *   - n == 1                          → answer is 1 (captain alone)
 *   - all intervals disjoint          → answer is 1
 *   - touching at a single point      → counts as overlap (closed endpoints)
 *   - duplicate intervals             → handled naturally by counting
 *
 * 🔍 Follow-ups:
 *   1. Return the actual team? Re-scan and collect indices overlapping the best captain.
 *   2. Half-open intervals [s, e)? Swap strict / non-strict bounds on the miss counts.
 *   3. Weighted employees? Replace counts with prefix sums of weights on sorted endpoints.
 *   4. "Every pair overlaps" variant (Helly)? Use a sweep line for max concurrent intervals.
 *
 * 🔁 Related: Meeting Rooms II (253), Minimum Arrows to Burst Balloons (452),
 *             Car Pooling (1094), My Calendar III (732).
 */
public class MaximumTeamSizeWithOverlappingIntervals {

    /**
     * 🧠 Intuition: only the captain has constraints, so for a fixed
     * captain `c` the best team is `c` plus everyone whose interval
     * overlaps `c`'s. The answer is therefore the max over all `c` of
     * `#{j : j overlaps c}` (and `c` overlaps itself, so it self-counts).
     *
     * Overlap is an AND of two conditions, which is awkward. Flip it:
     * for captain `[S, E]`, an interval is NON-overlapping iff it ended
     * strictly before `S` OR started strictly after `E` — two disjoint
     * one-sided counts that fall straight out of sorted endpoint arrays
     * via two `firstTrue` searches (Pattern 2):
     *   - `countSmaller(arr, S)` → first i with arr[i] >= S   (predicate `arr[i] >= S`)
     *   - `countGreater(arr, E)` → first i with arr[i] >  E   (predicate `arr[i] >  E`)
     *
     * Algorithm:
     *   1. Sort copies of startTime and endTime ascending.
     *   2. For each captain with interval [S, E]:
     *        endedBefore  = countSmaller(sortedEnds,    S)     // ends   <  S
     *        startedAfter = n − countGreater(sortedStarts, E)  // starts >  E
     *        overlapping  = n − endedBefore − startedAfter
     *   3. Track the maximum `overlapping`.
     *
     * Time:  O(n log n)
     * Space: O(n)
     *
     * @param startTime startTime[i] = employee i's start (inclusive)
     * @param endTime   endTime[i]   = employee i's end   (inclusive)
     * @return maximum valid team size
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
    public static void main(String[] args) {
        MaximumTeamSizeWithOverlappingIntervals solver =
            new MaximumTeamSizeWithOverlappingIntervals();

        int[][] starts   = { {3,4,6}, {1,10,20}, {1,2,3,4}, {1},  {1,9,2}    };
        int[][] ends     = { {8,5,7}, {2,11,21}, {4,4,4,4}, {1},  {10,10,3}  };
        int[]   expected = {       3,         1,         4,   1,          3  };

        for (int i = 0; i < starts.length; i++) {
            int got = solver.maximumTeamSize(starts[i], ends[i]);
            System.out.printf("starts=%s ends=%s  →  %d  expected=%d%n",
                Arrays.toString(starts[i]),
                Arrays.toString(ends[i]),
                got, expected[i]);
        }

        // sanity-check the binary-search primitives on a sorted array
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("countSmaller(arr, 7) = " + countSmaller(arr, 7) + "  expected=6"); // arr[i] >= 7
        System.out.println("countGreater(arr, 7) = " + countGreater(arr, 7) + "  expected=7"); // arr[i] >  7
    }
}

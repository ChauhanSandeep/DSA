package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * Problem: Minimum Number of Arrows to Burst Balloons
 *
 * Balloons are intervals on the x-axis. One vertical arrow shot at position x
 * bursts every balloon whose interval contains x. Return the fewest arrows needed
 * to burst all balloons.
 *
 * Leetcode: https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/ (Medium)
 * Rating:   acceptance 61.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Greedy | Sort by interval end
 *
 * Example:
 *   Input:  points = [[10,16],[2,8],[1,6],[7,12]]
 *   Output: 2
 *   Why:    shooting at 6 bursts [1,6] and [2,8], then shooting at 12 bursts
 *           [7,12] and [10,16].
 *
 * Follow-ups:
 *   1. Return the arrow coordinates too?
 *      Store each chosen interval end whenever a new arrow is needed.
 *   2. Minimize cost when arrows have different prices by coordinate?
 *      This becomes a weighted interval covering problem rather than the simple greedy.
 *   3. Process intervals online as they arrive?
 *      Maintain active overlaps with a heap or balanced tree; the offline greedy no longer applies directly.
 *
 * Related: Non-overlapping Intervals (435), Merge Intervals (56).
 */
public class MinimumArrowsToBurstBalloons {

    public static void main(String[] args) {
        MinimumArrowsToBurstBalloons solver = new MinimumArrowsToBurstBalloons();

        int[][][] inputs = {
            { {10, 16}, {2, 8}, {1, 6}, {7, 12} },
            { {1, 2} },
            { {1, 2}, {3, 4}, {5, 6}, {7, 8} }
        };
        int[] expected = { 2, 1, 4 };

        for (int i = 0; i < inputs.length; i++) {
            int[][] intervals = new int[inputs[i].length][];
            for (int row = 0; row < inputs[i].length; row++) {
                intervals[row] = inputs[i][row].clone();
            }
            int got = solver.findMinArrowShots(intervals);
            System.out.printf("points=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: sort balloons by their ending coordinate and shoot at the earliest end
     * that is still needed. That position bursts the earliest-ending balloon and gives
     * maximum chance to also cover later balloons. When a later balloon starts after
     * the current arrow position, it cannot be covered, so a new arrow is required.
     *
     * Algorithm:
     *   1. Return 0 for null or empty intervals.
     *   2. Sort intervals by end coordinate.
     *   3. Place the first arrow at the first interval's end.
     *   4. Scan remaining intervals and add a new arrow whenever the start is beyond lastArrowPos.
     *
     * Time:  O(n log n) - sorting the intervals dominates the linear scan.
     * Space: O(1) - intervals are sorted in place and only counters are stored.
     *
     * @param intervals balloon intervals as [start, end]
     * @return minimum number of arrows needed to burst every balloon
     */
    public int findMinArrowShots(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        // Sort intervals by end coordinate (ascending)
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

        int arrowCount = 1;
        int lastArrowPos = intervals[0][1]; // First arrow at end of first balloon

        for (int i = 1; i < intervals.length; i++) {
            int[] currentInterval = intervals[i];

            // If currentInterval balloon starts after last arrow’s reach, shoot a new arrow
            if (currentInterval[0] > lastArrowPos) {
                arrowCount++;
                lastArrowPos = currentInterval[1]; // Update arrow position
            }
            // else, currentInterval balloon is already burst by the last arrow
        }

        return arrowCount;
    }

    /**
     * Alternative brute-force implementation (for completeness).
     * Adds balloons one-by-one, and removes overlapping balloons after each shot.
     * Time Complexity: O(n^2)
     * Space Complexity: O(n)
     */
    public int findMinArrowShotsBruteForce(int[][] points) {
        List<int[]> intervals = new ArrayList<>(Arrays.asList(points));
        intervals.sort(Comparator.comparingInt(a -> a[0]));

        int arrows = 0;

        while (!intervals.isEmpty()) {
            int[] first = intervals.get(0);
            int arrow = first[1];
            arrows++;

            Iterator<int[]> it = intervals.iterator();
            while (it.hasNext()) {
                int[] balloon = it.next();
                if (balloon[0] <= arrow) {
                    it.remove(); // burst by this arrow
                }
            }
        }

        return arrows;
    }
}
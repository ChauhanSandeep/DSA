package array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * Problem Statement:
 * Given a list of spherical balloons taped into the wall represented as intervals [xStart, xEnd]
 * You need to burst all balloons using the minimum number of arrows.
 * One arrow can burst all balloons that overlap with the same x-coordinate.
 * Return the minimum number of arrows needed.
 *
 * Example:
 * Input: points = [[10,16],[2,8],[1,6],[7,12]]
 * Output: 2
 * Explanation: Shoot one arrow at x=6 to burst [1,6], [2,8], and [7,12]; another at x=16 to burst [10,16].
 *
 * Leetcode URL:
 * https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons
 *
 * Follow-up Questions:
 * 1. Can you modify the algorithm to return the exact coordinates where arrows should be shot?
 *    → Yes, store each arrow's position (the `end` of current group) in a list and return that.
 *
 * 2. How does the solution change if the balloons are not guaranteed to be non-overlapping?
 *    → No change required; overlapping is already handled by the merging strategy.
 */

public class MinimumArrowsToBurstBalloons {

    /**
     * Returns the minimum number of arrows required to burst all balloons.
     *
     * Algorithm:
     * - Sort all balloon intervals based on their end positions.
     * - Initialize the arrow count to 1 (as at least one balloon exists).
     * - Use a variable `lastArrowPos` to track the x-coordinate of the last shot arrow.
     * - For each balloon, check if its start is beyond the reach of the last arrow:
     *     - If yes, shoot a new arrow and update `lastArrowPos` to current balloon's end.
     *     - Otherwise, it is already covered by the last arrow.
     *
     * Time Complexity: O(n log n) for sorting
     * Space Complexity: O(1) extra space
     *
     * @param intervals Array of intervals representing balloon positions
     * @return Minimum number of arrows needed to burst all balloons
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
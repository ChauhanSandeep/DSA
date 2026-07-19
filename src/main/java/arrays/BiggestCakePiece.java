package arrays;

import java.util.Arrays;

/**
 * Problem: Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
 *
 * A rectangular cake has horizontal and vertical cut positions already chosen.
 * After all cuts are made, return the area of the largest resulting piece modulo
 * 1,000,000,007. The cake can be huge, so the multiplication must avoid int overflow.
 *
 * Leetcode: https://leetcode.com/problems/maximum-area-of-a-piece-of-cake-after-horizontal-and-vertical-cuts/ (Medium)
 * Rating:   1445 (zerotrac Elo)
 * Pattern:  Array | Sorting | Maximum adjacent gap
 *
 * Example:
 *   Input:  h = 5, w = 4, horizontalCuts = [1,2,4], verticalCuts = [1,3]
 *   Output: 4
 *   Why:    the largest vertical span is 2 and the largest horizontal span is 2,
 *           so the largest rectangle has area 2 * 2 = 4.
 *
 * Follow-ups:
 *   1. Return the smallest non-zero piece area instead?
 *      Track minimum adjacent gaps in both directions after sorting the cuts.
 *   2. Handle duplicate cut positions in the input?
 *      Sort first and ignore repeated neighbours, or deduplicate with a set before sorting.
 *   3. Extend this to a 3D block with cuts along three axes?
 *      Find the maximum gap on each axis independently and multiply all three gaps.
 *
 * Related: Maximum Width Ramp (962), Widest Vertical Area Between Two Points (1637).
 */
public class BiggestCakePiece {

    public static void main(String[] args) {
        int[][] horizontalCuts = { {1, 2, 4}, {1} };
        int[][] verticalCuts = { {1, 3}, {1} };
        int[] heights = { 5, 2 };
        int[] widths = { 4, 2 };
        int[] expected = { 4, 1 };

        for (int i = 0; i < heights.length; i++) {
            int got = maxArea(heights[i], widths[i], horizontalCuts[i].clone(), verticalCuts[i].clone());
            System.out.printf("h=%d w=%d horizontal=%s vertical=%s  ->  %d  expected=%d%n",
                heights[i], widths[i], Arrays.toString(horizontalCuts[i]),
                Arrays.toString(verticalCuts[i]), got, expected[i]);
        }
    }
    private static final int MOD = 1_000_000_007;



    /**
     * Intuition: after sorting cut positions, every cake piece dimension is just the
     * gap between neighbouring cuts, including the cake edges. The largest rectangle
     * must use the largest vertical gap and the largest horizontal gap independently,
     * then the area is reduced modulo MOD to avoid overflow in the required answer.
     *
     * Algorithm:
     *   1. Sort horizontalCuts and verticalCuts.
     *   2. Find the maximum gap between adjacent horizontal cuts and cake edges.
     *   3. Find the maximum gap between adjacent vertical cuts and cake edges.
     *   4. Multiply the two gaps as a long and return the modulo result.
     *
     * Time:  O(h log h + v log v) - sorting both cut arrays dominates the scans.
     * Space: O(1) - aside from the in-place sort, only gap values are stored.
     *
     * @param cakeHeight total cake height
     * @param cakeWidth total cake width
     * @param horizontalCuts cut positions measured from the top edge
     * @param verticalCuts cut positions measured from the left edge
     * @return largest piece area modulo 1,000,000,007
     */
    public static int maxArea(int cakeHeight, int cakeWidth, int[] horizontalCuts, int[] verticalCuts) {
        Arrays.sort(horizontalCuts);
        Arrays.sort(verticalCuts);

        int maxHeight = getMaxCut(cakeHeight, horizontalCuts);
        int maxWidth = getMaxCut(cakeWidth, verticalCuts);

        return (int) ((long) maxHeight * maxWidth % MOD);
    }

    /**
     * Finds the maximum gap between consecutive cuts in a sorted array.
     * Considers three types of gaps: start to first cut, between cuts, and last cut to end.
     *
     * @param totalLength The total length (height or width of the cake)
     * @param cuts The sorted array of cut positions
     * @return The maximum segment size
     */
    private static int getMaxCut(int totalLength, int[] cuts) {
        int maxGap = 0;

        // Gap from start to first cut
        maxGap = Math.max(maxGap, cuts[0]);

        // Gaps between consecutive cuts
        for (int i = 1; i < cuts.length; i++) {
            maxGap = Math.max(maxGap, cuts[i] - cuts[i - 1]);
        }

        // Gap from last cut to end
        maxGap = Math.max(maxGap, totalLength - cuts[cuts.length - 1]);

        return maxGap;
    }
}

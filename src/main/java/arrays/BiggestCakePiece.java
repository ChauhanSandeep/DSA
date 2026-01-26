package arrays;

import java.util.Arrays;

/**
 * Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
 *
 * Problem:
 * Given a rectangular cake with height h and width w, and two arrays of integers horizontalCuts and verticalCuts
 * that specify positions where cuts are made, find the maximum area of a single piece of cake after all cuts.
 *
 * Example:
 * Input: h = 5, w = 4, horizontalCuts = [1,2,4], verticalCuts = [1,3]
 * Output: 4
 * Explanation: The largest piece has height 2 (between cuts at 2 and 4) and width 2 (between cuts at 1 and 3).
 * Area = 2 * 2 = 4.
 *
 * Constraints:
 * - 2 <= h, w <= 10^9
 * - 1 <= horizontalCuts.length, verticalCuts.length <= min(h - 1, 10^5)
 * - All cut positions are unique and within bounds
 *
 * LeetCode: https://leetcode.com/problems/maximum-area-of-a-piece-of-cake-after-horizontal-and-vertical-cuts/
 *
 * Follow-up Questions:
 * Q1: What if we need to find the minimum area piece instead of maximum?
 * A1: Use the same approach but track minimum gaps instead of maximum gaps between consecutive cuts.
 *
 * Q2: How would you handle the case where cuts are allowed at the same position multiple times?
 * A2: Use a Set to deduplicate cuts before sorting, or sort first and skip duplicate adjacent values.
 *
 * Q3: Can we optimize space if the cut arrays are extremely large?
 * A3: We can sort in-place and process cuts in a single pass without additional data structures.
 *
 * Q4: What if we want to find the k-th largest piece instead?
 * A4: Generate all possible piece dimensions, sort them by area, and return the k-th element (O(N*M) space).
 *
 * Q5: How would you extend this to 3D (a cubic cake with 3 types of cuts)?
 * A5: Apply the same logic to all three dimensions independently, then multiply the three maximum gaps.
 * LeetCode Contest Rating: 1445
 */
public class BiggestCakePiece {
    private static final int MOD = 1_000_000_007;

    public static void main(String[] args) {
        int cakeHeight = 5;
        int cakeWidth = 4;
        int[] horizontalCuts = {1, 2, 4};
        int[] verticalCuts = {1, 3};
        System.out.println("Maximum cake piece area: " + maxArea(cakeHeight, cakeWidth, horizontalCuts, verticalCuts));
    }

    /**
     * Computes the maximum area of a piece of cake after performing the given cuts.
     *
     * Algorithm:
     * 0. Add boundary cuts (0 and height/width) implicitly
     * 1. Sort horizontal and vertical cuts to process them in order
     * 2. Find maximum gap between consecutive horizontal cuts
     * 3. Find maximum gap between consecutive vertical cuts
     * 4. Multiply the two maximum gaps to get largest piece area
     * 5. Return result modulo 10^9 + 7 to prevent overflow
     *
     * Key Insight: The largest piece is formed by the largest horizontal gap and largest vertical gap.
     *
     * Time Complexity: O(N log N + M log M) - dominated by sorting
     * Space Complexity: O(1) - only uses a few variables
     *
     * @param cakeHeight The height of the cake
     * @param cakeWidth The width of the cake
     * @param horizontalCuts Array containing horizontal cut positions
     * @param verticalCuts Array containing vertical cut positions
     * @return The maximum piece area modulo 10^9 + 7
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

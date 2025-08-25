package arrays;

import java.util.Arrays;

/**
 * Given a rectangular cake with height h and width w, and two arrays of integers horizontalCuts and verticalCuts
 * that specify positions where cuts are made, this program finds the maximum area of a single piece of cake
 * after all cuts.
 *
 * Algorithm:
 * - Sort the horizontal and vertical cuts.
 * - Compute the maximum gap between adjacent horizontal and vertical cuts.
 * - Multiply these maximum gaps to get the largest possible piece of cake.
 * - Return the result modulo 10^9 + 7 to prevent overflow.
 *
 * Time Complexity: O(N log N + M log M), where N and M are the lengths of horizontalCuts and verticalCuts, respectively.
 * Space Complexity: O(1) since we use only a few extra variables.
 *
 * LeetCode Problem: https://leetcode.com/problems/maximum-area-of-a-piece-of-cake-after-horizontal-and-vertical-cuts/
 */
public class BiggestCakePiece {
    private static final int MOD = 1_000_000_007;

    public static void main(String[] args) {
        int h = 5;
        int w = 4;
        int[] horizontalCuts = {1, 2, 4};
        int[] verticalCuts = {1, 3};
        System.out.println(maxArea(h, w, horizontalCuts, verticalCuts));
    }

    /**
     * Computes the maximum area of a piece of cake after performing the given cuts.
     *
     * @param h The height of the cake.
     * @param w The width of the cake.
     * @param horizontalCuts Array containing horizontal cut positions.
     * @param verticalCuts Array containing vertical cut positions.
     * @return The maximum piece area modulo 10^9 + 7.
     */
    public static int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
        Arrays.sort(horizontalCuts); // Sort horizontal cuts
        Arrays.sort(verticalCuts);   // Sort vertical cuts

        // Find the maximum height difference between consecutive horizontal cuts
        int maxH = getMaxCut(h, horizontalCuts);
        // Find the maximum width difference between consecutive vertical cuts
        int maxW = getMaxCut(w, verticalCuts);

        // Compute the largest piece area and return modulo MOD
        return (int) ((long) maxH * maxW % MOD);
    }

    /**
     * Finds the maximum difference between consecutive cuts in a sorted array.
     *
     * @param len The total length (height or width of the cake).
     * @param cuts The sorted array of cut positions.
     * @return The maximum segment size.
     */
    private static int getMaxCut(int len, int[] cuts) {
        int maxDiff = 0;

        // Consider the first segment (from the start of the cake to the first cut)
        maxDiff = Math.max(maxDiff, cuts[0]);

        // Compute the largest gap between consecutive cuts
        for (int i = 1; i < cuts.length; i++) {
            maxDiff = Math.max(maxDiff, cuts[i] - cuts[i - 1]);
        }

        // Consider the last segment (from the last cut to the end of the cake)
        maxDiff = Math.max(maxDiff, len - cuts[cuts.length - 1]);

        return maxDiff;
    }
}

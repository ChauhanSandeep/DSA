package Array;

import java.util.Arrays;

public class BiggestCakePiece {
    public static void main(String[] args) {
        int h = 5;
        int w = 4;
        int[] horizontalCuts = {1, 2, 4};
        int[] verticalCuts = {1, 3};
        System.out.println(maxArea(h, w, horizontalCuts, verticalCuts));
    }

    /**
     * Given a rectangular cake with height h and width w, and two arrays of integers horizontalCuts and verticalCuts where horizontalCuts[i]
     * is the distance from the top of the rectangular cake to the ith horizontal cut and
     * similarly, verticalCuts[j] is the distance from the left of the rectangular cake to the jth vertical cut.
     *
     * Return the maximum area of a piece of cake after you cut at each horizontal and vertical position
     * provided in the arrays horizontalCuts and verticalCuts. Since the answer can be a huge number, return this modulo 10^9 + 7.
     */
    public static int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
        Arrays.sort(horizontalCuts);
        Arrays.sort(verticalCuts);

        int hDiff = getMaxCut(h, horizontalCuts);
        if(hDiff == 0) return 0;

        int vDiff = getMaxCut(w, verticalCuts);
        long result = (long) hDiff * vDiff;
        return (int)(result%(Math.pow(10, 9) + 7));

    }

    private static int getMaxCut(int len, int[] cuts) {
        int diff = 0;

        for(int i=0; i<cuts.length - 1; i++) {
            if(cuts[i+1] - cuts[i] > diff) diff = cuts[i+1] - cuts[i];
        }

        // from beginning to the first cut and end to last cut
        if(cuts.length > 0) {
            if(cuts[0] > diff) diff = cuts[0];
            if(len - cuts[cuts.length - 1] > diff) diff = len - cuts[cuts.length - 1];
        }
        return diff;
    }
}

package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Given an array, find two subsets S1 and S2 such that S1-S2 = diff
 * count all such subsets
 */
public class CountSubsetDiff {
    public static void main(String[] args) {

    }

    public static int countSubsetDiff(int[] arr, int diff) {
        /*
        S1 - S2 = diff
        S1 + S2 = total
    + ------------------
        2S1 = diff+total
        S1 = (diff+total)/2
         */
        int total = Arrays.stream(arr).sum();
        int subsetSum = (diff + total)/2;
        return CountSubsetSum.countSubsetSum(arr, subsetSum, arr.length); // already solved
    }
}

package dynamicprogramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Given an array, find two subsets S1 and S2 such that S1-S2 = diff
 * count all such subsets
 */
public class CountSubsetWithGivenDifference {
    public static void main(String[] args) {

    }

    public static int countSubsetDiff(int[] arr, int diff) {
        /**
         * Problem: Count of Subsets with Given Difference
         *
         * Problem Statement:
         * Given an array of positive integers and a target difference `diff`, find the number of subsets where
         * the difference between the sum of elements of two subsets is exactly `diff`.
         *
         * Leetcode Equivalent: No direct, but related to Target Sum: https://leetcode.com/problems/target-sum/
         *
         * Relation to 0/1 Knapsack:
         * - Another variation where:
         *    -> Instead of maximizing or checking possible, we **count** number of ways.
         *    -> It's based on Subset Sum.
         *    -> Required_sum = (diff + total_sum) / 2
         *
         * Important Concept:
         * S1 - S2 = diff
         * S1 + S2 = total
         *------------------
         * 2S1 = diff+total
         * S1 = (diff+total)/2
         *
         *  So the problem is reduced to:
         *  -> "Count the number of subsets whose sum is exactly (diff + total_sum) / 2."
         *
         *  Very important to remember:
         *  -> If (diff + total_sum) is odd => It is impossible to split, so answer is 0.
         */
        int total = Arrays.stream(arr).sum();
        if ((diff + total) % 2 != 0) return 0;
        int subsetSum = (diff + total)/2;
        return CountSubsetsWithGivenSum.countSubsetsIterative(arr, arr.length, subsetSum); // already solved
    }
}

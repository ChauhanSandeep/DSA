package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: Count Subsets With Given Difference
 *
 * Given positive integers and a difference diff, count the ways to split the
 * elements into two subsets whose sums differ by exactly diff. Each element is
 * assigned to one of the two subsets.
 *
 * Source: GeeksForGeeks Count of Subsets With Given Difference
 * Pattern:  Dynamic Programming | 0/1 knapsack | Reduce difference to subset sum
 *
 * Example:
 *   Input:  arr = [1,1,2,3], diff = 1
 *   Output: 3
 *   Why:    the problem reduces to counting subsets with sum (total + diff) / 2 = 4.
 *
 * Follow-ups:
 *   1. How does this connect to Target Sum?
 *      Positive signs form one subset and negative signs form the other, giving the same equation.
 *   2. What if total + diff is odd?
 *      No integer subset sum can satisfy the equations, so the answer is zero.
 *   3. What if numbers include zero?
 *      The subset-counting DP must double choices for each zero, because include and exclude are distinct.
 *
 * Related: Target Sum (494), Count Subsets With Given Sum.
 */
public class CountSubsetWithGivenDifference {

    /**
     * Intuition: if subset sums are s1 and s2, then s1 - s2 = diff and
     * s1 + s2 = total. Adding the equations gives s1 = (total + diff) / 2,
     * so the problem becomes counting subsets with that exact sum.
     *
     * Algorithm:
     *   1. Sum the array and reject impossible parity or negative target cases.
     *   2. Build dp[itemCount][sum] as the number of ways to make sum from a prefix.
     *   3. For each item, add ways from skipping it and from taking it when possible.
     *   4. Return the count stored for all items and the transformed target sum.
     *
     * Time:  O(n * targetSum) - each item/sum state is filled once.
     * Space: O(n * targetSum) - the DP table stores every prefix/sum count.
     *
     * @param arr positive input values
     * @param diff required difference between the two subset sums
     * @return number of assignments whose subset-sum difference is diff
     */
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


    public static void main(String[] args) {
        int[][] inputs = { {}, {1, 1, 2, 3}, {1, 2, 7} };
        int[] diffs = {0, 1, 4};
        int[] expected = {1, 3, 1};

        for (int i = 0; i < inputs.length; i++) {
            int output = countSubsetDiff(inputs[i], diffs[i]);
            System.out.printf("arr=%s diff=%d  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), diffs[i], output, expected[i]);
        }
    }

}

package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: Target Sum
 *
 * Given integers nums and a target, place either a plus or minus sign before
 * each number. Count how many sign assignments make the final expression equal
 * target.
 *
 * Leetcode: https://leetcode.com/problems/target-sum/
 * Rating:   acceptance 52.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | 0/1 knapsack | Convert signs to subset count
 *
 * Example:
 *   Input:  nums = [1,1,1,1,1], target = 3
 *   Output: 5
 *   Why:    exactly one of the five 1s must receive a minus sign, so there are five choices.
 *
 * Follow-ups:
 *   1. How do zeros affect the count?
 *      Each zero can be +0 or -0, doubling the number of assignments without changing the sum.
 *   2. Can this be solved without the algebra transform?
 *      Yes; use a map from running sum to count after each number.
 *   3. What if nums may contain negative values?
 *      The subset transform no longer applies directly; use offset or hash-map DP over sums.
 *
 * Related: Count Subsets With Given Difference, Partition Equal Subset Sum (416).
 */
public class TargetSum {

        /**
     * Intuition: assigning plus signs to subset P and minus signs to subset N gives
     * sum(P) - sum(N) = target and sum(P) + sum(N) = total. Therefore sum(P) must be
     * (total + target) / 2, so the task becomes counting subsets with that sum.
     *
     * Algorithm:
     *   1. Sum the array and reject impossible target/parity combinations.
     *   2. Use a subset-count DP table over item prefixes and sums.
     *   3. For each value, add counts from skipping it and taking it when possible.
     *   4. Return the count for the transformed positive subset sum.
     *
     * Time:  O(n * subsetSum) - every item/sum state is filled once.
     * Space: O(n * subsetSum) - the DP table stores all prefix counts.
     *
     * @param arr input values
     * @param target desired signed sum
     * @return number of plus/minus assignments that reach target
     */
    public static int findTargetSumWays(int[] arr, int target) {
        int totalSum = Arrays.stream(arr).sum();

        // Check if (target + totalSum) is even and non-negative
        if (totalSum < Math.abs(target) || (target + totalSum) % 2 != 0) {
            return 0;
        }

        int subsetSum = (target + totalSum) / 2;
        return CountSubsetsWithGivenSum.countSubsetsIterative(arr, arr.length, subsetSum); // already solved
    }


    public static void main(String[] args) {
        int[][] inputs = { {}, {1, 1, 1, 1, 1}, {0, 0, 1} };
        int[] targets = {0, 3, 1};
        int[] expected = {1, 5, 4};

        for (int i = 0; i < inputs.length; i++) {
            int output = findTargetSumWays(inputs[i], targets[i]);
            System.out.printf("nums=%s target=%d  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), targets[i], output, expected[i]);
        }
    }

}
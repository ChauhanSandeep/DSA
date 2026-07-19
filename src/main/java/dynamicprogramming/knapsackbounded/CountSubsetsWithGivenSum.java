package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: Count Subsets With Given Sum
 *
 * Given an array of non-negative integers and a target sum, count how many
 * subsets add up exactly to the target. Each array element may be used at most
 * once, so equal values at different indices are still separate choices.
 *
 * Source: GeeksForGeeks Perfect Sum Problem
 * Pattern:  Dynamic Programming | 0/1 knapsack | Count subsets by target sum
 *
 * Example:
 *   Input:  arr = [0,1,2,3], target = 3
 *   Output: 4
 *   Why:    [3] and [1,2] work, and the zero can be either excluded or included for each.
 *
 * Follow-ups:
 *   1. What if the answer can be very large?
 *      Store counts modulo the requested value and apply the modulus after every addition.
 *   2. Can space be reduced to O(target)?
 *      Yes; iterate sums backward for each element so every item is used at most once.
 *   3. What if negative numbers are allowed?
 *      Use an offset over the possible sum range, or switch to a hash map of sum to count.
 *
 * Related: Target Sum (494), Partition Equal Subset Sum (416).
 */
public class CountSubsetsWithGivenSum {

        /**
     * Intuition: every element has two choices, take it or skip it. The repeated
     * question is how many subsets from the first index elements can make target,
     * so memoizing index and target collapses the exponential tree into a table.
     *
     * Algorithm:
     *   1. Stop at index 0: the empty prefix makes only sum 0.
     *   2. Reuse dp[index][target] when the same state appears again.
     *   3. Count ways that skip the current item.
     *   4. If the current item fits, add ways that take it.
     *
     * Time:  O(size * targetSum) - each index/target state is solved once.
     * Space: O(size * targetSum) - memo table plus recursion depth.
     *
     * @param arr non-negative input values
     * @param size number of values to consider
     * @param targetSum desired subset sum
     * @return number of subsets whose sum is targetSum
     */
    public static int countSubsetsRecursive(int[] arr, int size, int targetSum) {
        Integer[][] dp = new Integer[size+1][targetSum+1];
        return countSubsetsHelper(arr, size, targetSum, dp);
    }

    /** Counts target-sum subsets using the first index values. */
    private static int countSubsetsHelper(int[] arr, int index, int target, Integer[][] dp) {
        if (target == 0) return 1;
        if (index == 0) {
            return arr[0] == target ? 1 : 0;
        }
        if (dp[index][target] != null) return dp[index][target];

        int notTake = countSubsetsHelper(arr, index-1, target, dp); // exclude current element
        int take = 0;
        if (arr[index-1] <= target) {
            take = countSubsetsHelper(arr, index-1, target-arr[index-1], dp); // include current element
        }

        dp[index][target] = take + notTake;
        return dp[index][target];
    }

        /**
     * Intuition: the recursive take/skip choices can be read row by row. A cell
     * dp[itemCount][sum] counts subsets from a prefix that make sum; the next item
     * either is absent from those subsets or is present and reduces the needed sum.
     *
     * Algorithm:
     *   1. Seed dp[0][0] = 1 because the empty subset makes sum 0 once.
     *   2. For each prefix size and target sum, copy the count that skips the item.
     *   3. If the item value fits, add the count that takes it.
     *   4. Return dp[size][targetSum].
     *
     * Time:  O(size * targetSum) - every table cell is filled once.
     * Space: O(size * targetSum) - all prefix/sum counts are stored.
     *
     * @param arr non-negative input values
     * @param size number of values to consider
     * @param targetSum desired subset sum
     * @return number of subsets whose sum is targetSum
     */
    public static int countSubsetsIterative(int[] arr, int size, int targetSum) {
        int[][] dp = new int[size+1][targetSum+1];

        for (int i = 0; i < size + 1; i++) {
            dp[i][0] = 1; // sum 0 is possible by empty subset
        }

        for (int elementIndex = 1; elementIndex < size + 1; elementIndex++) {
            for (int currentSum = 0; currentSum < targetSum + 1; currentSum++) {
                int waysIfNotTaken = dp[elementIndex-1][currentSum]; // exclude the current element

                int waysIfTaken = 0;
                if (arr[elementIndex-1] <= currentSum) {
                    waysIfTaken = dp[elementIndex-1][currentSum-arr[elementIndex-1]]; // include current element
                }

                dp[elementIndex][currentSum] = waysIfNotTaken + waysIfTaken;
            }
        }
        return dp[size][targetSum];
    }


    public static void main(String[] args) {
        int[][] inputs = { {}, {1, 2, 3, 3}, {0, 1, 2, 3} };
        int[] targets = {0, 6, 3};
        int[] expected = {1, 3, 4};

        for (int i = 0; i < inputs.length; i++) {
            int output = countSubsetsIterative(inputs[i], inputs[i].length, targets[i]);
            System.out.printf("arr=%s target=%d  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), targets[i], output, expected[i]);
        }
    }

}
package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: Minimum Subset Sum Difference
 *
 * Given positive integers, split them into two subsets so the absolute
 * difference between the subset sums is as small as possible. Return that
 * minimum difference.
 *
 * Source: Classic minimum subset sum difference problem
 * Pattern:  Dynamic Programming | 0/1 knapsack | Reachable subset sums
 *
 * Example:
 *   Input:  arr = [1,6,11,5]
 *   Output: 1
 *   Why:    subsets [1,5,6] and [11] have sums 12 and 11, which differ by only 1.
 *
 * Follow-ups:
 *   1. Can you return the actual subsets?
 *      Store choices or backtrack through the reachable-sum table from the best sum.
 *   2. Can space be reduced to O(totalSum)?
 *      Yes; use a 1D boolean array and iterate sums backward for each element.
 *   3. What if negative numbers are allowed?
 *      Use offset DP or a set of reachable sums instead of indexing only non-negative sums.
 *
 * Related: Partition Equal Subset Sum (416), Last Stone Weight II (1049).
 */
public class MinimumSubsetSumDifference {

        /**
     * Intuition: once one subset has currentSum, the other subset has totalSum - currentSum.
     * The difference at the end is fixed by that one tracked sum, so recursion only
     * needs index and currentSum.
     *
     * Algorithm:
     *   1. Compute totalSum.
     *   2. Recursively decide whether to add each item to the tracked subset.
     *   3. At the end, compare currentSum against totalSum - currentSum.
     *   4. Memoize index/currentSum states and return the minimum difference.
     *
     * Time:  O(size * totalSum) - each index/sum state is solved once.
     * Space: O(size * totalSum) - memo table plus recursion depth.
     *
     * @param arr input values
     * @param size number of values to consider
     * @return minimum possible absolute subset-sum difference
     */
    public static int minSubsetSumDiffRecursive(int[] arr, int size) {
        int totalSum = Arrays.stream(arr).sum();
        Integer[][] dp = new Integer[size+1][totalSum+1];
        return findMinDiff(arr, 0, 0, totalSum, dp);
    }

    /** Returns the best final difference after deciding items up to index. */
    private static int findMinDiff(int[] arr, int index, int currentSum, int totalSum, Integer[][] dp) {
        if (index == arr.length) { // base case
            int remainingSum = totalSum - currentSum; // sum of the remaining subset
            return Math.abs(currentSum - remainingSum); // absolute difference between two subsets
        }

        if (dp[index][currentSum] != null) return dp[index][currentSum];

        // including the current element
        int include = findMinDiff(arr, index+1, currentSum + arr[index], totalSum, dp);
        // excluding the current element
        int exclude = findMinDiff(arr, index+1, currentSum, totalSum, dp);

        // store the minimum difference
        dp[index][currentSum] = Math.min(include, exclude);
        return dp[index][currentSum];
    }

        /**
     * Intuition: the best partition is determined by the subset sum closest to half
     * of the total. A boolean subset-sum table tells which sums are reachable from
     * each prefix, then the closest reachable half gives the minimum difference.
     *
     * Algorithm:
     *   1. Compute totalSum and initialize reachable sum 0 for every prefix.
     *   2. Fill dp[itemCount][sum] using skip or take transitions.
     *   3. Search backward from totalSum / 2 for the largest reachable sum.
     *   4. Convert that sum into totalSum - 2 * sum.
     *
     * Time:  O(size * totalSum) - fill the table and scan half the sum range.
     * Space: O(size * totalSum) - reachability table for prefixes and sums.
     *
     * @param arr input values
     * @param size number of values to consider
     * @return minimum possible absolute subset-sum difference
     */
    public static int minSubsetSumDiffIterative(int[] arr, int size) {
        int totalSum = Arrays.stream(arr).sum();
        boolean[][] dp = new boolean[size+1][totalSum+1];

        for (int i = 0; i < size+1; i++) {
            dp[i][0] = true;
        }

        for (int elementIndex = 1; elementIndex < size+1; elementIndex++) {
            for (int currentSum = 0; currentSum <= totalSum; currentSum++) {
                // If we don't take the current element, can we achieve the current sum?
                dp[elementIndex][currentSum] = dp[elementIndex-1][currentSum];
                if (arr[elementIndex-1] <= currentSum) { // if we can include the current element
                    dp[elementIndex][currentSum] = dp[elementIndex][currentSum] ||
                        dp[elementIndex-1][currentSum-arr[elementIndex-1]]; // if we include the current element, can we achieve the current sum?
                }
            }
        }

        int minDiff = Integer.MAX_VALUE;
        // the last row of dp indicates all possible subset sums which can be formed by the arr,
        // because we can use any element to form a subset. We check all sums from 0 to totalSum/2 (inclusive).
        for (int subsetSum = 0; subsetSum <= totalSum/2; subsetSum++) {
            // dp[size][subsetSum] indicates if we can achieve this subset sum
            if (dp[size][subsetSum]) {
                int otherSum = totalSum - subsetSum;
                minDiff = Math.min(minDiff, Math.abs(otherSum - subsetSum));
            }
        }
        return minDiff;
    }


    public static void main(String[] args) {
        int[][] inputs = { {}, {1, 6, 11, 5}, {1, 2, 7} };
        int[] expected = {0, 1, 4};

        for (int i = 0; i < inputs.length; i++) {
            int output = minSubsetSumDiffIterative(inputs[i], inputs[i].length);
            System.out.printf("arr=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
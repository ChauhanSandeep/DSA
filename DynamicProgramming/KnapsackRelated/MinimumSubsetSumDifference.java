package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Minimum Subset Sum Difference
 *
 * Problem Statement:
 * Given a set of positive integers, partition it into two subsets such that the absolute difference of their sums is minimized.
 *
 * Leetcode Equivalent: Direct Leetcode not available, but it’s a **classic** DP interview problem.
 *
 * Relation to 0/1 Knapsack:
 * - We try to divide the set into two subsets as equally as possible.
 * - Based on subset sums (similar to Subset Sum problem), find minimum possible difference.
 */
public class MinimumSubsetSumDifference {
    public static void main(String[] args) {
        int[] arr = {1, 6, 11, 5};
        System.out.println("Recursive: " + minSubsetSumDiffRecursive(arr, arr.length));
        System.out.println("Iterative: " + minSubsetSumDiffIterative(arr, arr.length));
    }

    /**
     * Recursive Approach (with memoization):
     *
     * Intuition:
     * - Try every subset, calculate sum of one subset, other is (totalSum - subsetSum).
     * - Minimize absolute difference.
     * - Memoize to avoid recalculations.
     *
     * Approach:
     * - Base Case: If index == 0, either take or not take the first element.
     * - Choices: include or exclude current element into subset1.
     *
     * Time Complexity: O(n * totalSum)
     * Space Complexity: O(n * totalSum) + recursion stack O(n)
     */
    public static int minSubsetSumDiffRecursive(int[] arr, int size) {
        int totalSum = Arrays.stream(arr).sum();
        Integer[][] dp = new Integer[size+1][totalSum+1];
        return findMinDiff(arr, 0, 0, totalSum, dp);
    }

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
     * Iterative Tabulation Approach:
     *
     * Intuition:
     * - Find all possible subset sums.
     * - Among those, pick the sum which gives minimum absolute difference with (totalSum - sum).
     *
     * Approach:
     * - Use Subset Sum DP idea to fill possible sums.
     * - Then check minimum difference.
     *
     * Time Complexity: O(size * totalSum)
     * Space Complexity: O(size * totalSum)
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
}
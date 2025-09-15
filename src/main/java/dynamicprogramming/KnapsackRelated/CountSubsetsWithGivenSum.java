package dynamicprogramming.KnapsackRelated;

/**
 * Problem: Count of Subsets with Given Sum
 *
 * Problem Statement:
 * Given an array of positive integers and a target sum, find the number of subsets that add up exactly to the target sum.
 *
 * Leetcode Equivalent: https://leetcode.com/problems/target-sum/ (related, with + and - signs)
 *
 * Relation to 0/1 Knapsack:
 * - Classic variation where instead of checking for possibility, we count the number of ways.
 * - Every element has two choices: include or exclude.
 *
 */
public class CountSubsetsWithGivenSum {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 3};
        int targetSum = 6;
        System.out.println("Recursive: " + countSubsetsRecursive(arr, arr.length, targetSum));
        System.out.println("Iterative: " + countSubsetsIterative(arr, arr.length, targetSum));
    }

    /**
     * Recursive Approach (with memoization):
     *
     * Intuition:
     * - Try including or excluding each number.
     * - Every time we reach target sum 0, it’s a valid subset.
     * - Memoize to avoid re-computation
     * - Base Cases:
     *  - If target is 0, we found a valid subset (count as 1).
     *  - If index is 0, check if arr[0] == target (count as 1 if true, else 0).
     *
     *
     * Time Complexity: O(size * targetSum)
     * Space Complexity: O(size * targetSum) for memo + O(size) recursion stack
     */
    public static int countSubsetsRecursive(int[] arr, int size, int targetSum) {
        Integer[][] dp = new Integer[size+1][targetSum+1];
        return countSubsetsHelper(arr, size, targetSum, dp);
    }

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
     * Iterative Tabulation Approach:
     *
     * Intuition:
     * - dp[i][j] means: number of ways to make sum 'j' using first 'i' elements.
     * - For each element, we can either include it (if it doesn't exceed current sum) or exclude it.
     * - Initialize dp[i][0] = 1 for all i (sum 0 is possible with empty subset).
     * - Fill the dp table iteratively.
     * - Final answer will be in dp[size][targetSum].
     *
     * Time Complexity: O(size * targetSum)
     * Space Complexity: O(size * targetSum)
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
}
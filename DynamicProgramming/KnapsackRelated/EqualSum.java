package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Count the number of subsets (S1, S2) such that S1 - S2 = diff.
 * LeetCode Link: https://leetcode.com/problems/target-sum/ (related problem)
 *
 * Approach:
 * - Given S1 - S2 = diff and S1 + S2 = totalSum.
 * - We derive S1 = (diff + totalSum) / 2.
 * - The problem reduces to counting subsets whose sum equals S1.
 * - Solve using dynamic programming.
 *
 * Time Complexity: O(N * subsetSum), where N is the number of elements in the array.
 * Space Complexity: O(N * subsetSum), as we use a DP table.
 */
public class CountSubsetDiff {
    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 3};
        int diff = 1;
        System.out.println("Count of subsets with given difference: " + countSubsetsWithDifference(arr, diff));
    }

    /**
     * Counts subsets where the difference between two subset sums equals the given difference.
     *
     * @param arr  Input array
     * @param diff Target difference between two subset sums
     * @return The number of valid subset pairs
     */
    public static int countSubsetsWithDifference(int[] arr, int diff) {
        if (arr.length == 0 || diff < 0) {
            return 0; // Edge case: empty array or negative difference
        }

        int totalSum = Arrays.stream(arr).sum();

        // If (diff + totalSum) is odd, we cannot partition into valid subsets
        if ((diff + totalSum) % 2 != 0) {
            return 0;
        }

        int subsetSum = (diff + totalSum) / 2;
        return countSubsetsWithSum(arr, subsetSum);
    }

    /**
     * Utilizes a pre-existing function to count subsets with a given sum.
     */
    private static int countSubsetsWithSum(int[] arr, int sum) {
        return CountSubsetSum.countSubsetSum(arr, sum, arr.length);
    }
}

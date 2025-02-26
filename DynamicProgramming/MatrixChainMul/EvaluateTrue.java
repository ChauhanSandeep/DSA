package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Count the number of subsets (S1, S2) such that S1 - S2 = diff.
 * LeetCode Link: https://leetcode.com/problems/target-sum/ (related problem)
 *
 * Approach:
 * - Given: S1 - S2 = diff and S1 + S2 = totalSum.
 * - By solving, we derive: S1 = (diff + totalSum) / 2.
 * - The problem reduces to finding subsets whose sum equals S1.
 * - Use dynamic programming to count subsets that sum to S1.
 *
 * Time Complexity: O(N * targetSum), where N is the number of elements in the array.
 * Space Complexity: O(N * targetSum), due to the DP table.
 */
public class CountSubsetDiff {
    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 3};
        int diff = 1;
        System.out.println("Count of subsets with given difference: " + countSubsetDiff(arr, diff));
    }

    /**
     * Counts subsets where the difference between two subset sums equals the given difference.
     *
     * @param arr  Input array of positive integers
     * @param diff Target difference between subset sums
     * @return The number of valid subset pairs
     */
    public static int countSubsetDiff(int[] arr, int diff) {
        int totalSum = Arrays.stream(arr).sum();

        // Edge cases: If diff is negative or larger than totalSum, return 0
        if (diff < 0 || diff > totalSum || (diff + totalSum) % 2 != 0) {
            return 0;
        }

        int targetSum = (diff + totalSum) / 2;
        return CountSubsetSum.countSubsetSum(arr, targetSum, arr.length); // Calls helper function
    }
}

package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Count the number of subsets (S1, S2) such that S1 - S2 = diff.
 * LeetCode Link: https://leetcode.com/problems/target-sum/ (related problem)
 *
 * Approach:
 * - Given: S1 - S2 = diff and S1 + S2 = totalSum.
 * - Solving these equations, we derive: S1 = (diff + totalSum) / 2.
 * - The problem then reduces to counting subsets that sum up to S1.
 * - Utilize dynamic programming to count such subsets efficiently.
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

        // If diff is negative or greater than totalSum, partitioning isn't possible
        if (diff < 0 || diff > totalSum || (diff + totalSum) % 2 != 0) {
            return 0;
        }

        int targetSum = (diff + totalSum) / 2;
        return CountSubsetSum.countSubsetSum(arr, targetSum, arr.length); // Calls helper function
    }
}

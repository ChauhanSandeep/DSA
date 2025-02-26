package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Count the number of subsets (S1, S2) such that S1 - S2 = diff.
 * LeetCode Link: https://leetcode.com/problems/target-sum/ (related problem)
 *
 * Approach:
 * - Given the equations:
 *      S1 - S2 = diff
 *      S1 + S2 = totalSum
 *   By solving, we derive: S1 = (diff + totalSum) / 2.
 * - The problem reduces to counting subsets with sum equal to S1.
 * - This is solved using a dynamic programming approach.
 *
 * Time Complexity: O(N * subsetSum), where N is the number of elements in the array.
 * Space Complexity: O(N * subsetSum), due to the DP table.
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
            return 0; // Edge case: Empty array or negative difference
        }

        int subsetSum = calculateSubsetSum(arr, diff);
        if (subsetSum == -1) {
            return 0; // If subsetSum is invalid, return 0
        }

        return CountSubsetSum.countSubsetSum(arr, subsetSum, arr.length);
    }

    /**
     * Calculates the required subset sum S1.
     *
     * @param arr  Input array
     * @param diff Target difference
     * @return The subset sum required or -1 if invalid
     */
    private static int calculateSubsetSum(int[] arr, int diff) {
        int totalSum = Arrays.stream(arr).sum();
        
        // If (diff + totalSum) is odd, a valid partition is impossible
        if ((diff + totalSum) % 2 != 0) {
            return -1;
        }

        return (diff + totalSum) / 2;
    }
}

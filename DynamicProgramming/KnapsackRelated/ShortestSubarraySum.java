package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Count the number of subsets (S1, S2) such that S1 - S2 = diff.
 * LeetCode Link: https://leetcode.com/problems/target-sum/ (related problem)
 *
 * Approach:
 * - Use the equations:
 *      - S1 - S2 = diff
 *      - S1 + S2 = totalSum
 *   By solving, we derive: S1 = (diff + totalSum) / 2.
 * - The problem now reduces to finding subsets whose sum equals S1.
 * - Use dynamic programming to count subsets that sum to S1.
 *
 * Time Complexity: O(N * targetSubsetSum), where N is the number of elements in the array.
 * Space Complexity: O(N * targetSubsetSum), as we use a DP table.
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
        int totalSum = Arrays.stream(arr).sum();
        
        // If diff is greater than totalSum or (diff + totalSum) is odd, partitioning is impossible
        if (diff > totalSum || (diff + totalSum) % 2 != 0) {
            return 0;
        }
        
        int targetSubsetSum = (diff + totalSum) / 2;
        return CountSubsetSum.countSubsetSum(arr, targetSubsetSum); // Uses existing method
    }
}

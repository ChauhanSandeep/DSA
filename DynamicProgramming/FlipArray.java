package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Flip Array (Minimum elements to flip to achieve minimum non-negative sum)
 * 
 * Given an array of positive elements, flip the sign of some elements so that 
 * the resultant sum is as close to zero as possible (non-negative).
 * Return the minimum number of elements that need to be flipped.
 * 
 * Approach:
 * - This problem is a variation of the **Subset Sum** problem, solved using Dynamic Programming.
 * - We aim to split the array into two subsets such that their difference is minimized.
 * - Instead of targeting `0`, we target the closest possible sum to `totalSum / 2` using DP.
 * - We use a **1D DP array**, where `dp[j]` stores the minimum flips needed to form sum `j`.
 * 
 * Time Complexity: **O(N * Sum/2)**
 * Space Complexity: **O(Sum/2)**
 */
public class FlipArray {

    public static void main(String[] args) {
        int[] arr = {8, 4, 5, 7, 6, 2};
        int result = new FlipArray().minFlipsForMinNonNegativeSum(arr);
        System.out.println(result);
    }

    /**
     * Finds the minimum number of elements to flip to achieve the closest non-negative sum.
     */
    public int minFlipsForMinNonNegativeSum(int[] arr) {
        int totalSum = Arrays.stream(arr).sum();
        int target = totalSum / 2;  // Closest possible half-sum
        
        // DP array: dp[i] represents the minimum flips needed to achieve sum 'i'
        int[] dp = new int[target + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;  // Zero flips needed to achieve sum 0

        for (int num : arr) {
            // Traverse backward to avoid overwriting previous DP states
            for (int j = target; j >= num; j--) {
                if (dp[j - num] != Integer.MAX_VALUE) {
                    dp[j] = Math.min(dp[j], dp[j - num] + 1);
                }
            }
        }

        // Find the largest achievable sum closest to 'target'
        for (int sum = target; sum >= 0; sum--) {
            if (dp[sum] != Integer.MAX_VALUE) {
                return dp[sum];  // Minimum flips required
            }
        }

        return -1; // Should never reach here
    }
}

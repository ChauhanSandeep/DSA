package DynamicProgramming;

/**
 * Problem: Minimum Cost to Merge Elements
 * 
 * Given an integer array `elements` of size `N`, merge all elements into one 
 * with the minimum possible cost. 
 * 
 * Rules:
 * - Choose any two adjacent elements X and Y, merge them into (X + Y) with cost (X + Y).
 * - The goal is to minimize the total merging cost.
 * 
 * Approach:
 * - We use **Dynamic Programming** (DP) with a bottom-up approach.
 * - `dp[i][j]` represents the **minimum cost** to merge elements from index `i` to `j`.
 * - The recurrence relation is:
 *      dp[i][j] = min(dp[i][k] + dp[k+1][j] + sum[i][j])  for all `i ≤ k < j`
 * - `prefixSum` array helps compute the sum efficiently.
 * 
 * Time Complexity: **O(N³)** - Since we iterate over all possible subarrays and partitions.
 * Space Complexity: **O(N²)** - Due to the DP table storage.
 * 
 * Similar Problem: https://leetcode.com/problems/minimum-cost-to-merge-stones/
 */
public class MergeElements {

    public static void main(String[] args) {
        int[] input = {1, 2, 3, 4};
        System.out.println(new MergeElements().minMergeCost(input)); // Expected Output: 19
    }

    public int minMergeCost(int[] elements) {
        if (elements == null || elements.length == 0) return 0;

        int n = elements.length;

        // Prefix sum array to quickly calculate sum of any subarray
        int[] prefixSum = new int[n + 1];

        // DP table where dp[i][j] stores the minimum cost to merge elements[i:j]
        int[][] dp = new int[n + 1][n + 1];

        // Compute prefix sums to facilitate quick range sum calculation
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = elements[i - 1] + prefixSum[i - 1];
        }

        // Iterate over different possible lengths of subarrays (bottom-up DP)
        for (int subArrayLen = 2; subArrayLen <= n; subArrayLen++) {
            for (int i = 1; i + subArrayLen - 1 <= n; i++) {
                int j = i + subArrayLen - 1;
                dp[i][j] = Integer.MAX_VALUE;

                // Compute the sum of the subarray elements[i:j]
                int totalSum = prefixSum[j] - prefixSum[i - 1];

                // Try merging at different partition points `k`
                for (int k = i; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j] + totalSum);
                }
            }
        }

        // The minimum cost to merge the entire array is stored at dp[1][n]
        return dp[1][n];
    }
}

package DynamicProgramming.KnapsackRelated;

/**
 * Problem: Given an array, find the number of subsets with a given sum.
 * LeetCode Link: https://leetcode.com/problems/partition-equal-subset-sum/ (related)
 *
 * Approach:
 * - Use Dynamic Programming (Bottom-Up) to count subsets.
 * - Utilize a 2D DP table where dp[i][j] represents the number of ways
 *   to form sum 'j' using the first 'i' elements.
 * - Recurrence Relation:
 *   - If arr[i-1] <= j: dp[i][j] = dp[i-1][j] + dp[i-1][j - arr[i-1]]
 *   - Else: dp[i][j] = dp[i-1][j]
 * 
 * Time Complexity: O(N * sum), where N is the array size.
 * Space Complexity: O(N * sum), due to the DP table.
 */
public class CountSubsetSum {

    public static void main(String[] args) {
        int[] arr = {2, 3, 5, 6, 8, 10};
        int targetSum = 10;
        System.out.println("Count of subsets with sum " + targetSum + ": " + countSubsetSum(arr, targetSum));
    }

    /**
     * Counts the number of subsets with the given sum.
     *
     * @param arr       Input array
     * @param targetSum Target sum to achieve
     * @return Number of subsets that sum up to the targetSum
     */
    public static int countSubsetSum(int[] arr, int targetSum) {
        int n = arr.length;
        if (n == 0) return 0; // Edge case: empty array

        int[][] dp = new int[n + 1][targetSum + 1];

        // Base case: One way to get sum 0 (empty subset)
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }

        // Fill the DP table
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= targetSum; j++) {
                if (arr[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - arr[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[n][targetSum];
    }
}

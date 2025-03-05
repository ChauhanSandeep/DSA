package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Minimum Cuts for Palindromic Partitioning
 * 
 * Given a string `s`, return the minimum number of cuts required to partition 
 * it such that every substring in the partition is a palindrome.
 * 
 * Example:
 * Input: "aab"
 * Output: 1  (["aa", "b"])
 * 
 * Approach:
 * 1. **Recursive + Memoization (Top-Down)**:
 *    - Use recursion with memoization (`dp[start][end]`) to avoid recomputing.
 *    - Try all possible partitions and minimize cuts.
 * 2. **Dynamic Programming (Bottom-Up, O(N²) Optimal Solution)**:
 *    - Use a `dp[i]` array where `dp[i]` represents the **minimum cuts required**
 *      for the substring `s[0:i]`.
 *    - Precompute palindromic substrings using a 2D `isPalindrome` table.
 * 
 * Time Complexity:
 * - **Recursive (Memoized)**: O(N³) (due to repeated palindrome checks)
 * - **Bottom-Up DP (Optimized)**: O(N²)
 * 
 * Space Complexity:
 * - O(N²) for DP table in both approaches.
 * 
 * Related Problem: https://leetcode.com/problems/palindrome-partitioning-ii/
 */
public class MinCutPalindrome {

    public static void main(String[] args) {
        System.out.println(new MinCutPalindrome().minCut("aab")); // Expected Output: 1
    }

    /**
     * Recursive + Memoization Approach (O(N³))
     */
    public int minCut(String s) {
        int n = s.length();
        int[][] dp = new int[n][n]; // Memoization table for substring partitions
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        return minCutsRecursive(s, 0, n - 1, dp);
    }

    private int minCutsRecursive(String s, int start, int end, int[][] dp) {
        if (dp[start][end] != -1) return dp[start][end];

        // If the substring is already a palindrome, no cuts are needed
        if (isPalindrome(s, start, end)) {
            dp[start][end] = 0;
            return 0;
        }

        int minCuts = end - start; // Maximum possible cuts
        for (int i = start + 1; i <= end; i++) {
            if (isPalindrome(s, start, i - 1)) {
                minCuts = Math.min(minCuts, 1 + minCutsRecursive(s, i, end, dp));
            }
        }

        dp[start][end] = minCuts;
        return minCuts;
    }

    /**
     * Bottom-Up Dynamic Programming Approach (Optimized O(N²))
     */
    public int minCutDP(String s) {
        int n = s.length();
        boolean[][] isPalindrome = new boolean[n][n];
        int[] dp = new int[n];

        // Precompute palindrome substrings
        for (int end = 0; end < n; end++) {
            for (int start = 0; start <= end; start++) {
                if (s.charAt(start) == s.charAt(end) && (end - start <= 2 || isPalindrome[start + 1][end - 1])) {
                    isPalindrome[start][end] = true;
                }
            }
        }

        // Fill DP table
        for (int end = 0; end < n; end++) {
            if (isPalindrome[0][end]) {
                dp[end] = 0; // No cut needed if whole substring is a palindrome
            } else {
                dp[end] = end; // Max possible cuts (cut at every character)
                for (int start = 1; start <= end; start++) {
                    if (isPalindrome[start][end]) {
                        dp[end] = Math.min(dp[end], 1 + dp[start - 1]);
                    }
                }
            }
        }

        return dp[n - 1];
    }

    /**
     * Checks if a given substring `s[left:right]` is a palindrome.
     */
    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}

package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Distinct Subsequences
 *
 * Given a source string `s` and a target string `t`, find the number of distinct subsequences of `t` in `s`.
 *
 * Intuition:
 * - A subsequence maintains the relative order of characters but may skip some.
 * - We count the number of ways we can form `t` using characters from `s`.
 *
 * Approach 1 (Recursive + Memoization):
 * - Try including or excluding characters to form the target.
 * - Use memoization to avoid recomputation.
 *
 * Approach 2 (Bottom-Up DP - Optimal):
 * - Use a 2D DP table where `dp[i][j]` represents the count of ways to form `t[0...i]` using `s[0...j]`.
 * - If `s[j-1] == t[i-1]`, then add counts from previous computations.
 *
 * Time Complexity:
 * - **Recursive w/o Memoization:** `O(2^N)` (Exponential, leads to TLE)
 * - **Recursive w/ Memoization:** `O(N*M)` (Efficient)
 * - **Bottom-Up DP:** `O(N*M)` (Best for large inputs)
 *
 * Space Complexity:
 * - **Recursive + Memoization:** `O(N*M)` (Memoization table)
 * - **Bottom-Up DP:** `O(N*M)` (2D DP table)
 *
 * LeetCode Problem Link:
 * https://leetcode.com/problems/distinct-subsequences/
 */
public class DistinctSubsequence {

    public static void main(String[] args) {
        DistinctSubsequence solution = new DistinctSubsequence();
        System.out.println("Distinct Subsequences: " + solution.numDistinctDP("rabbbit", "rabbit")); // Output: 3
    }

    /**
     * Recursive Solution with Memoization (Top-Down DP)
     * @param source The source string
     * @param target The target string
     * @return Number of distinct subsequences
     */
    public int numDistinctMemo(String source, String target) {
        Map<String, Integer> memo = new HashMap<>();
        return countSubsequences(source, target, 0, 0, memo);
    }

    private int countSubsequences(String source, String target, int sIndex, int tIndex, Map<String, Integer> memo) {
        // Base Case: If target is completely matched
        if (tIndex == target.length()) return 1;
        // If source is exhausted before matching target
        if (sIndex == source.length()) return 0;

        // Memoization key
        String key = sIndex + "|" + tIndex;
        if (memo.containsKey(key)) return memo.get(key);

        int count = 0;
        // Option 1: If characters match, we can either take it or skip
        if (source.charAt(sIndex) == target.charAt(tIndex)) {
            count += countSubsequences(source, target, sIndex + 1, tIndex + 1, memo);
        }
        // Option 2: Skip the current character in `source`
        count += countSubsequences(source, target, sIndex + 1, tIndex, memo);

        // Store the result in memo
        memo.put(key, count);
        return count;
    }

    /**
     * Bottom-Up Dynamic Programming Approach
     * @param source The source string
     * @param target The target string
     * @return Number of distinct subsequences
     */
    public int numDistinctDP(String source, String target) {
        int m = target.length();
        int n = source.length();
        int[][] dp = new int[m + 1][n + 1];

        // Base Case: An empty target can be formed by any prefix of `source` in exactly 1 way (by deleting everything)
        for (int j = 0; j <= n; j++) {
            dp[0][j] = 1;
        }

        // Fill the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // If characters match, include both possibilities:
                // 1. Use this matching character (`dp[i-1][j-1]`)
                // 2. Skip this character from `source` (`dp[i][j-1]`)
                if (target.charAt(i - 1) == source.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + dp[i][j - 1];
                } else {
                    // Else, just skip the character in `source`
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }

        return dp[m][n]; // Final answer
    }
}

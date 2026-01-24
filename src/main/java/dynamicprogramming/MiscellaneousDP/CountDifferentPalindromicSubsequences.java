package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * 90. Count Different Palindromic Subsequences
 *
 * Problem: Given a string s, return the number of different non-empty palindromic
 * subsequences in s. The answer may be large; return modulo 10^9+7.
 *
 * Example:
 * Input: s = "bccb"
 * Output: 6
 * Subsequences: "b","c","bb","cc","bcb","bccb"
 *
 * LeetCode: https://leetcode.com/problems/count-different-palindromic-subsequences
 *
 * Follow-up questions:
 * Q: Can we optimize space?
 * A: Use rolling arrays to reduce DP table size.
 *
 * Q: How to handle larger alphabet?
 * A: Increase dimension for character map or compress string.
 *
 * Q: What about distinct substrings instead?
 * A: Use suffix automaton or suffix array techniques.
 */
public class CountDifferentPalindromicSubsequences {

    private static final int MOD = 1_000_000_007;

    /**
     * DP solution: dp[i][j] is count of distinct palindromic subsequences in s[i..j]
     * - If s[i]==s[j], include new palindromes formed by s[i]...s[j]
     * - Else merge results from dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1]
     *
     * Time Complexity: O(n^2)
     * Space Complexity: O(n^2)
     */
    public int countPalindromicSubsequences(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];

        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    int low = i + 1, high = j - 1;
                    while (low <= high && s.charAt(low) != s.charAt(i)) low++;
                    while (low <= high && s.charAt(high) != s.charAt(j)) high--;

                    if (low > high) {
                        dp[i][j] = (dp[i+1][j-1] * 2 + 2) % MOD;
                    } else if (low == high) {
                        dp[i][j] = (dp[i+1][j-1] * 2 + 1) % MOD;
                    } else {
                        dp[i][j] = ((dp[i+1][j-1] * 2 - dp[low+1][high-1]) % MOD + MOD) % MOD;
                    }
                } else {
                    dp[i][j] = ((dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1]) % MOD + MOD) % MOD;
                }
            }
        }

        return dp[0][n-1];
    }

    /**
     * Optimized with rolling array to reduce space to O(n).
     */
    public int countPalindromicSubsequencesOptimized(String s) {
        int n = s.length();
        int[] dp = new int[n];
        int[] nextDp = new int[n];

        for (int i = n - 1; i >= 0; i--) {
            Arrays.fill(nextDp, 0);
            nextDp[i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    int low = i + 1, high = j - 1;
                    while (low <= high && s.charAt(low) != s.charAt(i)) low++;
                    while (low <= high && s.charAt(high) != s.charAt(j)) high--;

                    if (low > high) {
                        nextDp[j] = (dp[j-1] * 2 + 2) % MOD;
                    } else if (low == high) {
                        nextDp[j] = (dp[j-1] * 2 + 1) % MOD;
                    } else {
                        nextDp[j] = ((dp[j-1] * 2 - dp[high-1] + MOD) % MOD);
                    }
                } else {
                    nextDp[j] = ((dp[j] + nextDp[j-1] - dp[j-1]) % MOD + MOD) % MOD;
                }
            }
            dp = nextDp.clone();
        }

        return dp[n-1];
    }
}
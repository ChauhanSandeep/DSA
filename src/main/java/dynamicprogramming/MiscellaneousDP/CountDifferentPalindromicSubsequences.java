package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * Problem: Count Different Palindromic Subsequences
 *
 * Given a string, count how many distinct non-empty palindromic subsequences it
 * contains. Subsequences can skip characters, but equal text should be counted
 * only once. Return the answer modulo 1,000,000,007.
 *
 * Leetcode: https://leetcode.com/problems/count-different-palindromic-subsequences/
 * Rating:   acceptance 48.1% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | Interval DP | Distinct subsequences
 *
 * Example:
 *   Input:  s = "bccb"
 *   Output: 6
 *   Why:    the distinct palindromic subsequences are b, c, bb, cc, bcb, and bccb.
 *
 * Follow-ups:
 *   1. Can the O(n^2) search for matching inner characters be sped up?
 *      Precompute next and previous occurrence arrays for each character.
 *   2. What if the alphabet is much larger than lowercase letters?
 *      Use maps of character positions instead of fixed-size occurrence arrays.
 *   3. Can we list the subsequences instead of counting them?
 *      Only for small inputs; the output itself can be exponential.
 *
 * Related: Palindromic Substrings (647), Distinct Subsequences II (940).
 */
public class CountDifferentPalindromicSubsequences {

    private static final int MOD = 1_000_000_007;

    /**
     * Intuition: dp[left][right] stores the number of distinct palindromic
     * subsequences inside s[left..right]. If the ends differ, we combine the two
     * intervals that drop one end and subtract their overlap. If the ends match,
     * every inner palindrome can be wrapped by that character, plus the one-letter
     * and two-letter palindromes made from the ends; repeated copies inside the
     * interval decide how much of that wrapped work was already counted.
     *
     * Algorithm:
     *   1. Fill intervals from shorter suffix starts to longer right endpoints.
     *   2. If s[i] and s[j] differ, add the two smaller intervals and subtract their overlap.
     *   3. If they match, scan inward for duplicate boundary characters and apply the matching case.
     *   4. Normalize each value modulo MOD and return dp[0][n-1].
     *
     * Time:  O(n^3) - there are O(n^2) intervals and this version may scan inside each one.
     * Space: O(n^2) - the interval DP table stores one value for each substring range.
     *
     * @param s input string
     * @return number of distinct non-empty palindromic subsequences modulo 1,000,000,007
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

    public static void main(String[] args) {
        CountDifferentPalindromicSubsequences solver = new CountDifferentPalindromicSubsequences();
        String[] inputs = {"a", "bccb", "aaa"};
        int[] expected = {1, 6, 3};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countPalindromicSubsequences(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }
}
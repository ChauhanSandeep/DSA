package dynamicprogramming.longestcommonsubsequence;

import java.util.Arrays;


/**
 * Problem: Longest Common Substring
 *
 * Given two strings, return the length of the longest contiguous block of
 * characters that appears in both strings. Unlike subsequences, substrings must
 * stay continuous in both inputs.
 *
 * Leetcode: https://leetcode.com/problems/maximum-length-of-repeated-subarray/ (Medium)
 * Rating:   not available for this string variant
 * Pattern:  Dynamic Programming | 2D table | Contiguous suffix length
 *
 * Example:
 *   Input:  text1 = "abcde", text2 = "abfce"
 *   Output: 2
 *   Why:    "ab" is the longest block that appears contiguously in both strings.
 *
 * Follow-ups:
 *   1. Return the substring itself?
 *      Track the ending index whenever maxLength improves.
 *   2. Can space be reduced to O(min(m, n))?
 *      Keep only the previous row because matches use the diagonal value.
 *   3. How would you solve many queries quickly?
 *      Use suffix automata, suffix arrays, or rolling hashes depending on constraints.
 *
 * Related: Longest Common Subsequence (1143), Maximum Length of Repeated Subarray (718).
 */
public class LongestCommonSubstring {

    public static void main(String[] args) {
        String[][] cases = { {"abc", "ac"}, {"abcde", "abfce"}, {"abc", "def"} };
        int[] expected = { 1, 2, 0 };

        for (int i = 0; i < cases.length; i++) {
            int got = longestCommonSubstringRecursive(cases[i][0], cases[i][1]);
            System.out.printf("texts=%s -> %d  expected=%d%n",
                Arrays.toString(cases[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: for each pair of starting positions, the only contiguous match
     * length comes from walking both strings forward together until a mismatch.
     * Memoization stores that common-prefix length, then scanning every pair picks
     * the longest substring without accidentally joining separated matches.
     *
     * Algorithm:
     *   1. Create a 2D memo table for common-prefix lengths at each index pair.
     *   2. Try every pair of starting positions as a possible substring start.
     *   3. Recursively extend only along the diagonal while characters match.
     *   4. Return the maximum memoized match length seen.
     *
     * Time:  O(m * n) - each index pair is computed once.
     * Space: O(m * n) - memo table plus recursion stack.
     *
     * @param text1 first input string
     * @param text2 second input string
     * @return length of the longest common substring
     */
    public static int longestCommonSubstringRecursive(String text1, String text2) {
        int len1 = text1.length();
        int len2 = text2.length();
        int[][] dp = new int[len1][len2];

        for (int i = 0; i < len1; i++) {
            Arrays.fill(dp[i], -1);
        }

        int maxLength = 0;
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                maxLength = Math.max(maxLength, findLCSRecursive(text1, text2, i, j, dp));
            }
        }

        return maxLength;
    }

    private static int findLCSRecursive(String text1, String text2, int i, int j, int[][] dp) {
        if (i >= text1.length() || j >= text2.length()) {
            return 0;
        }

        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        if (text1.charAt(i) != text2.charAt(j)) {
            dp[i][j] = 0;
            return 0;
        }

        dp[i][j] = 1 + findLCSRecursive(text1, text2, i + 1, j + 1, dp);
        return dp[i][j];
    }

        /**
     * Intuition: dp[i][j] stores the length of the common suffix ending at
     * text1[i - 1] and text2[j - 1]. If those two characters match, the suffix
     * extends the diagonal suffix dp[i - 1][j - 1] by one. If they differ, no
     * common substring can end at both positions, so the cell resets to zero.
     *
     * Algorithm:
     *   1. Allocate a (length1 + 1) by (length2 + 1) table initialized to zero.
     *   2. For every character pair, extend the diagonal value on a match or reset to zero on mismatch.
     *   3. Track the maximum cell value seen during the fill.
     *
     * Time:  O(m * n) - every pair of character positions is checked once.
     * Space: O(m * n) - the DP table stores one suffix length per prefix pair.
     *
     * @param text1 first input string
     * @param text2 second input string
     * @return length of the longest common substring
     */
    public static int longestCommonSubstringIterative(String text1, String text2) {
        int length1 = text1.length();
        int length2 = text2.length();
        int[][] dp = new int[length1 + 1][length2 + 1];
        int maxLength = 0;

        // Build dp table
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                // Match found
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                    maxLength = Math.max(maxLength, dp[i][j]);
                } else {
                    dp[i][j] = 0; // Reset on mismatch
                }
            }
        }

        return maxLength;
    }


}
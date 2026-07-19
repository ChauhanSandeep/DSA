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
        String[][] cases = { {"abcde", "abfce"}, {"abc", "def"} };
        int[] expected = { 2, 0 };

        for (int i = 0; i < cases.length; i++) {
            int got = longestCommonSubstringIterative(cases[i][0], cases[i][1]);
            System.out.printf("texts=%s -> %d  expected=%d%n",
                Arrays.toString(cases[i]), got, expected[i]);
        }
    }

    /**
     * Recursive approach with memoization (Top-Down DP)
     *
     * Intuition:
     * - Try matching characters at i and j.
     * - If they match, we can extend the current length by +1 and move forward.
     * - If they don't match, we reset current length to 0.
     * - Explore all possibilities and track the maximum substring length found so far.
     *
     * Steps:
     * 1. Create a 2D dp array to store results of subproblems.
     * 2. Use recursion to explore all character matches.
     * 3. Use memoization to avoid recomputation.
     * 4. Return the maximum length found.
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(m * n) (for dp table) + recursion stack
     */
    public static int longestCommonSubstringRecursive(String text1, String text2) {
        int len1 = text1.length();
        int len2 = text2.length();
        // dp[i][j] will store the length of longest common substring ending at text1[i] and text2[j]
        int[][] dp = new int[len1][len2];

        // Initialize dp with -1
        for (int i = 0; i < len1; i++) {
            Arrays.fill(dp[i], -1);
        }

        return findLCSRecursive(text1, text2, 0, 0, dp);
    }

    private static int findLCSRecursive(String text1, String text2, int i, int j, int[][] dp) {
        if (i >= text1.length() || j >= text2.length()) {
            return 0;
        }

        // Already computed
        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        int matchLength = 0;
        if (text1.charAt(i) == text2.charAt(j)) {
            // If characters match, extend the length and check for rest of the characters in both strings
            matchLength = 1 + findLCSRecursive(text1, text2, i + 1, j + 1, dp);
        }
        // If characters don't match, we can either skip one character from text1 or text2 and check for the rest of the strings
        int skipText1 = findLCSRecursive(text1, text2, i + 1, j, dp);
        int skipText2 = findLCSRecursive(text1, text2, i, j + 1, dp);

        dp[i][j] = Math.max(matchLength, Math.max(skipText1, skipText2));
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
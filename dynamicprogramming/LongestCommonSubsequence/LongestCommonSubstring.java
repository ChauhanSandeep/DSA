package dynamicprogramming.LongestCommonSubsequence;

import java.util.Arrays;


/**
 * Problem: Longest Common Substring
 *
 * Given two strings, find the length of the longest substring that appears in both strings.
 * (Substring: continuous sequence of characters, unlike subsequence which can be discontinuous.)
 *
 * Example:
 * Input:  text1 = "abcde", text2 = "abfce"
 * Output: 2
 * Explanation: The longest common substring is "ab" with length 2.
 *
 * LeetCode Link: https://leetcode.com/problems/longest-common-substring/ (not direct but similar to "Maximum Length of Repeated Subarray")
 */
public class LongestCommonSubstring {

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
     * Iterative approach (Bottom-Up DP)
     *
     * Intuition:
     * - Create a 2D dp array where dp[i][j] represents the length of the longest common substring
     *   ending at text1[i-1] and text2[j-1].
     * - If characters match, extend the substring length from previous diagonal dp[i-1][j-1].
     * - If characters don't match, reset dp[i][j] to 0 (because substrings must be continuous).
     *
     * Steps:
     * 1. Initialize a dp array of size (m+1) x (n+1) with 0s.
     * 2. Update dp[i][j] based on character match.
     * 3. Track the maximum value during the iteration.
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(m * n)
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

    public static void main(String[] args) {
        String text1 = "abcde";
        String text2 = "abfce";

        System.out.println("Recursive with Memoization: Length of Longest Common Substring = " +
            longestCommonSubstringRecursive(text1, text2));

        System.out.println("Iterative DP: Length of Longest Common Substring = " +
            longestCommonSubstringIterative(text1, text2));
    }
}
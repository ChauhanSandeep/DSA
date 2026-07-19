package dynamicprogramming.LongestCommonSubsequence;

import java.util.Arrays;

/**
 * Problem: Shortest Common Supersequence
 *
 * Given two strings, return the length of the shortest string that contains both
 * input strings as subsequences. Shared matching characters can be used once;
 * non-matching characters from either string must still be included.
 *
 * Leetcode: https://leetcode.com/problems/shortest-common-supersequence/ (Hard)
 * Rating:   zerotrac 1977 (Q4, weekly-contest-141)
 * Pattern:  Dynamic Programming | 2D table | Supersequence reconstruction
 *
 * Example:
 *   Input:  str1 = "abac", str2 = "cab"
 *   Output: 5
 *   Why:    "cabac" contains both strings as subsequences, and no length-4
 *           string can contain all required characters in order.
 *
 * Follow-ups:
 *   1. Return the actual shortest supersequence?
 *      Backtrack through the dp table, appending a matching character once.
 *   2. Can the length be derived from LCS?
 *      Yes, length is str1.length + str2.length - LCS(str1, str2).
 *   3. How would you solve for three strings?
 *      Use a 3D DP table, though memory grows quickly.
 *
 * Related: Longest Common Subsequence (1143), Delete Operation for Two Strings (583).
 */
public class ShortestCommonSupersequence {

    public static void main(String[] args) {
        String[][] cases = { {"abac", "cab"}, {"abc", "abc"} };
        int[] expected = { 5, 3 };

        for (int i = 0; i < cases.length; i++) {
            int got = shortestCommonSupersequenceRecursive(cases[i][0], cases[i][1]);
            System.out.printf("strings=%s -> %d  expected=%d%n",
                Arrays.toString(cases[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: dp[i][j] means the minimum supersequence length needed for the
     * suffixes str1[i..] and str2[j..]. If one suffix is empty, every remaining
     * character of the other suffix must be appended. If the current characters
     * match, one character covers both suffixes and both indices move. Otherwise,
     * the supersequence must take either str1[i] or str2[j], then pay the cheaper
     * remaining cost.
     *
     * Algorithm:
     *   1. Allocate a length1 by length2 memo table and initialize every cell to -1.
     *   2. Recursively solve from indices (0, 0), returning remaining length when one string ends.
     *   3. On a match, add one diagonal step; otherwise add one plus the smaller skip choice.
     *
     * Time:  O(m * n) - each pair of suffix indices is solved once.
     * Space: O(m * n) - the memo table stores one result per suffix pair.
     *
     * @param str1 first input string
     * @param str2 second input string
     * @return length of the shortest common supersequence
     */
    public static int shortestCommonSupersequenceRecursive(String str1, String str2) {
        int length1 = str1.length();
        int length2 = str2.length();
        int[][] dp = new int[length1][length2];

        // Initialize with -1 for memoization
        for (int i = 0; i < length1; i++) {
            for (int j = 0; j < length2; j++) {
                dp[i][j] = -1;
            }
        }

        return findSCSLength(str1, str2, 0, 0, dp);
    }

    private static int findSCSLength(String str1, String str2, int i, int j, int[][] dp) {
        if (i == str1.length()) {
            return str2.length() - j; // Add remaining characters from str2
        }
        if (j == str2.length()) {
            return str1.length() - i; // Add remaining characters from str1
        }

        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        if (str1.charAt(i) == str2.charAt(j)) {
            // If characters match, move both
            dp[i][j] = 1 + findSCSLength(str1, str2, i + 1, j + 1, dp);
        } else {
            // Otherwise, take minimum by adding one character
            int skipI = 1 + findSCSLength(str1, str2, i + 1, j, dp); // add char for ith character and calculate for rest of the string
            int skipJ = 1 + findSCSLength(str1, str2, i, j + 1, dp); // add char for jth character and calculate for rest of the string
            dp[i][j] = Math.min(skipI, skipJ);
        }
        return dp[i][j];
    }

    /**
     * Iterative Bottom-up Dynamic Programming approach.
     *
     * Intuition:
     * - Similar to LCS (Longest Common Subsequence) table filling.
     * - If characters match, move diagonally and add 1.
     * - Otherwise, minimum of (left, top) + 1.
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(m * n)
     */
    public static int shortestCommonSuperSequenceIterative(String str1, String str2) {
        int length1 = str1.length();
        int length2 = str2.length();
        int[][] dp = new int[length1 + 1][length2 + 1];

        // Fill base cases
        for (int i = 0; i <= length1; i++) {
            dp[i][0] = i; // If str2 is empty then we need to add all characters of str1
        }
        for (int j = 0; j <= length2; j++) {
            dp[0][j] = j; // If str1 is empty then we need to add all character of str2
        }

        // Fill the dp table
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    // add single character in supersequence and move the index in both the strings
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    // add single character from one of the string and take minimum of the previous indices in both string
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        printSuperSequence(dp, str1, str2);

        return dp[length1][length2];
    }

    private static void printSuperSequence(int[][] dp, String str1, String str2) {
        // Step 2: Backtrack to build the actual SCS string
        int i = str1.length(), j = str2.length();
        StringBuilder builder = new StringBuilder();

        while (i > 0 && j > 0) {
            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                // If characters match, include once and move diagonally
                builder.append(str1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] < dp[i][j - 1]) {
                // Move where cost is lesser (up)
                builder.append(str1.charAt(i - 1));
                i--;
            } else {
                // Move left
                builder.append(str2.charAt(j - 1));
                j--;
            }
        }

        // If any characters are left in str1 or str2
        while (i > 0) {
            builder.append(str1.charAt(i - 1));
            i--;
        }
        while (j > 0) {
            builder.append(str2.charAt(j - 1));
            j--;
        }

        // Since we built the string backwards, reverse it
        System.out.println(builder.reverse().toString());
    }


}

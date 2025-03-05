package DynamicProgramming;

/**
 * LeetCode Problem: (Similar Concept) https://leetcode.com/problems/longest-common-subsequence/
 *
 * Problem Statement:
 * Given two strings, find the longest common substring between them.
 * 
 * Approach:
 * - Use **Dynamic Programming** with a 2D table to track substring lengths.
 * - If characters match, extend the previous substring length.
 * - Keep track of the maximum length and its ending index.
 * 
 * Complexity:
 * - **Time Complexity: O(m * n)** (where `m` and `n` are the lengths of the strings)
 * - **Space Complexity: O(m * n)** (due to the DP table)
 */
public class LongestCommonSubstring {
    public static void main(String[] args) {
        String str1 = "ABCDGH";
        String str2 = "ACDGHR";
        String lcs = findLongestCommonSubstring(str1, str2);
        System.out.println("Longest Common Substring: " + lcs); // Output: "CDGH"
    }

    /**
     * Finds the longest common substring between two given strings using Dynamic Programming.
     * 
     * @param str1 First string
     * @param str2 Second string
     * @return The longest common substring
     */
    public static String findLongestCommonSubstring(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        int[][] dp = new int[m + 1][n + 1]; // DP table to store substring lengths

        int maxLength = 0; // Stores length of the longest common substring
        int endIndex = 0;  // Stores the ending index of longest common substring in str1

        // Build the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;

                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        endIndex = i; // Store end position in str1
                    }
                }
            }
        }

        // Extract the longest common substring
        return str1.substring(endIndex - maxLength, endIndex);
    }
}

package DynamicProgramming.LongestCommonSubsequence;

import java.util.Arrays;

/**
 * Problem: Minimum Window Subsequence
 *
 * Problem Statement:
 * Given two strings source (S) and target (T), find the minimum window in S which has T appearing as a subsequence (in order).
 * If no such window exists, return an empty string.
 *
 * For example:
 * - S = "abcdebdde", T = "bde"
 * - Output: "bcde"
 * - Explanation: The minimum window is "bcde" which contains T as a subsequence.
 *
 * Leetcode Link: https://leetcode.com/problems/minimum-window-subsequence/
 *
 * Intuition:
 * - We want to match characters of T in S **in order** but **not necessarily consecutively**.
 * - Among all such windows, we need the **smallest** one.
 *
 * Approach:
 * - dp[i][j] represents: **Starting index** of the window in source[0..j-1] that matches target[0..i-1].
 * - If characters match, inherit the starting index from dp[i-1][j-1].
 * - If they don't match, inherit from dp[i][j-1] (skip current source character).
 * - Base case: dp[0][j] = j + 1 (empty target can always match at any position).
 *
 * Time Complexity: O(sourceLen * targetLen)
 * Space Complexity: O(sourceLen * targetLen)
 */
public class MinWindowSubsequence {

    public static void main(String[] args) {
        String source = "abcdebdde";
        String target = "bde";
        System.out.println(findMinWindowSubsequence(source, target));
    }

    public static String findMinWindowSubsequence(String source, String target) {
        if (source.length() < target.length()) return "";

        int sourceLen = source.length();
        int targetLen = target.length();
        // dp[i][j] signifies the starting index of the window, if found else -1
        int[][] dp = new int[targetLen + 1][sourceLen + 1];
        for (int[] row : dp) {
            Arrays.fill(row, -1); // Initialize with -1 meaning "no match"
        }

        // Base Case: Empty target ("") can match anywhere in the source
        for (int j = 0; j < sourceLen + 1; j++) {
            dp[0][j] = j + 1; // j+1 because dp table is 1-based
        }

        // Fill the DP table
        for (int targetIndex = 1; targetIndex <= targetLen; targetIndex++) {
            for (int sourceIndex = 1; sourceIndex <= sourceLen; sourceIndex++) {
                if (target.charAt(targetIndex - 1) == source.charAt(sourceIndex - 1)) {
                    dp[targetIndex][sourceIndex] = dp[targetIndex - 1][sourceIndex - 1]; // Match found: inherit start index
                } else {
                    dp[targetIndex][sourceIndex] = dp[targetIndex][sourceIndex - 1]; // Skip source[sourceIndex-1] and inherit previous
                }
            }
        }

        // Find the minimum window by scanning the last row
        int minLen = sourceLen + 1;
        int startIdx = -1;
        for (int sourceIndex = 1; sourceIndex <= sourceLen; sourceIndex++) {
            if (dp[targetLen][sourceIndex] != -1) { // Valid window found
                int windowStart = dp[targetLen][sourceIndex] - 1; // Convert to 0-based index
                int windowEnd = sourceIndex - 1;
                int windowLen = windowEnd - windowStart + 1;

                if (windowLen < minLen) {
                    minLen = windowLen;
                    startIdx = windowStart;
                }
            }
        }

        if (startIdx == -1) return ""; // No valid window found
        return source.substring(startIdx, startIdx + minLen);
    }
}
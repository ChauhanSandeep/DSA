package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Wildcard Pattern Matching
 * LeetCode: https://leetcode.com/problems/wildcard-matching/
 *
 * Given an input string (str) and a pattern (pattern), implement
 * wildcard pattern matching with support for '?' and '*' where:
 * - '?' Matches any single character.
 * - '*' Matches any sequence of characters (including the empty sequence).
 *
 * Approach:
 * 1. **Dynamic Programming (Bottom-Up)**:
 *    - Use a `dp[i][j]` table where:
 *      - `dp[i][j]` = true if `str[0...i-1]` matches `pattern[0...j-1]`
 * 2. **Base Cases**:
 *    - Empty pattern only matches an empty string.
 *    - '*' at the beginning of the pattern can match an empty sequence.
 * 3. **Transition**:
 *    - If pattern[j-1] is '*':
 *      - Ignore '*' (`dp[i][j-1]`) OR match '*' with `str[i-1]` (`dp[i-1][j]`).
 *    - If pattern[j-1] is '?' or matches `str[i-1]`, take `dp[i-1][j-1]`.
 * 4. **Result**: `dp[str.length()][pattern.length()]` gives the final answer.
 *
 * Complexity:
 * - Time: O(N * M) where N = str length, M = pattern length.
 * - Space: O(N * M) (can be optimized to O(M) using a rolling array).
 */
public class WildcardMatching {

    public static void main(String[] args) {
        String str = "baaababac";
        String pattern = "ba?a*ac";
        WildcardMatching solver = new WildcardMatching();
        System.out.println(solver.isMatch(str, pattern)); // Expected: true
    }

    public boolean isMatch(String str, String pattern) {
        int strLen = str.length();
        int patternLen = pattern.length();

        // If the pattern is empty, it only matches an empty string
        if (patternLen == 0) return strLen == 0;

        // DP table: dp[i][j] represents if str[0...i-1] matches pattern[0...j-1]
        boolean[][] dp = new boolean[strLen + 1][patternLen + 1];

        // Empty pattern matches empty string
        dp[0][0] = true;

        // Handling leading '*' in the pattern, as '*' can match an empty string
        for (int j = 1; j <= patternLen; j++) {
            if (pattern.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1]; // '*' can represent an empty sequence
            }
        }

        // Fill the DP table
        for (int i = 1; i <= strLen; i++) {
            for (int j = 1; j <= patternLen; j++) {
                char pChar = pattern.charAt(j - 1);
                char sChar = str.charAt(i - 1);

                if (pChar == '*') {
                    // '*' can match zero (`dp[i][j-1]`) or more characters (`dp[i-1][j]`)
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else if (pChar == '?' || sChar == pChar) {
                    // '?' matches any character, or exact character match
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Characters don't match
                    dp[i][j] = false;
                }
            }
        }

        return dp[strLen][patternLen];
    }
}

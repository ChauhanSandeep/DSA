package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Regular Expression Matching
 * 
 * Given an input string `text` and a pattern `pattern`, implement regular expression matching 
 * with support for '.' and '*' where:
 * 
 * - '.' Matches any single character.
 * - '*' Matches zero or more of the preceding element.
 *
 * Approach:
 * - Use **Dynamic Programming** with a `dp` table where `dp[i][j]` represents whether the first `i` characters 
 *   of `text` match the first `j` characters of `pattern`.
 * - Handle cases where '*' allows zero occurrences (ignoring the previous character) or extends previous matches.
 * - The time complexity is **O(m * n)**, where `m` is the length of `text` and `n` is the length of `pattern`.
 * - The space complexity is also **O(m * n)** due to the DP table.
 * 
 * LeetCode Link: https://leetcode.com/problems/regular-expression-matching/
 */
public class RegexMatching {
    public static void main(String[] args) {
        RegexMatching matcher = new RegexMatching();
        System.out.println(matcher.isMatch("mississippi", "mis*is*p*."));  // false
        System.out.println(matcher.isMatch("mississippi", "mis*is*ip*.")); // true
    }

    public boolean isMatch(String text, String pattern) {
        int textLen = text.length();
        int patternLen = pattern.length();

        // DP table where dp[i][j] means text[0..i-1] matches pattern[0..j-1]
        boolean[][] dp = new boolean[textLen + 1][patternLen + 1];

        // Empty string matches empty pattern
        dp[0][0] = true;

        // Pre-fill the first row: Handling cases where '*' allows empty matching
        for (int j = 2; j <= patternLen; j++) {
            if (pattern.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2]; // '*' eliminates previous character
            }
        }

        // Fill the DP table
        for (int i = 1; i <= textLen; i++) {
            for (int j = 1; j <= patternLen; j++) {
                char patternChar = pattern.charAt(j - 1);
                char textChar = text.charAt(i - 1);

                if (patternChar == '*') {
                    // '*' can mean zero occurrences (ignore prev char) or extend previous matches
                    dp[i][j] = dp[i][j - 2]; // Ignore '*' and preceding char
                    char precedingPatternChar = pattern.charAt(j - 2);
                    if (precedingPatternChar == textChar || precedingPatternChar == '.') {
                        dp[i][j] = dp[i][j] || dp[i - 1][j]; // Extend previous match
                    }
                } else if (patternChar == textChar || patternChar == '.') {
                    // Exact match or '.' wildcard match
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }

        return dp[textLen][patternLen];
    }
}

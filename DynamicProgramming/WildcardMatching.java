package DynamicProgramming;

import java.util.Arrays;

/***
 * Given an input string (str) and a pattern (pattern), implement
 * wildcard pattern matching with support for '?' and '*' where:
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 */
public class WildcardMatching {
    public static void main(String[] args) {
        String str =     "baaababac";
        String pattern = "ba?a*ac";
        System.out.println(new WildcardMatching().isMatch(str, pattern));
    }
/*
    [   -     b       a      ?      a      *       a     c
    - [true, false,  false, false, false, false, false, false],
    b [false, true,  false, false, false, false, false, false],
    a [false, false, true,  false, false, false, false, false],
    a [false, false, false, true,  false, false, false, false],
    a [false, false, false, false, true,  true,  false, false],
    b [false, false, false, false, false, true,  false, false],
    a [false, false, false, false, false, true,  true,  false],
    b [false, false, false, false, false, true,  false, false],
    a [false, false, false, false, false, true,  true,  false],
    c [false, false, false, false, false, true,  false, true]
    ]
 */
    public boolean isMatch(String str, String pattern) {
        int strLen = str.length();
        int patternLen = pattern.length();
        if (patternLen == 0) return (strLen == 0);
        boolean[][] dp = new boolean[strLen + 1][patternLen + 1];

        // empty pattern can match with empty string
        dp[0][0] = true;

        for (int j = 1; j <= patternLen; j++) {
            if (pattern.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1]; // if last character match then ignore *
            }
        }

        for (int i = 1; i <= strLen; i++) {
            for (int j = 1; j <= patternLen; j++) {
                char pChar = pattern.charAt(j-1);
                char sChar = str.charAt(i-1);
                if (pChar == '*') {
                    dp[i][j] = dp[i][j - 1] // ignore *
                            || dp[i - 1][j];// take * and extend from previous
                } else if (pChar == '?' || sChar == pChar) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                else {
                    dp[i][j] = false;
                }
            }
        }
        return dp[strLen][patternLen];
    }
}

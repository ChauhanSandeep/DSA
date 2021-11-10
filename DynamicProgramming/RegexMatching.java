package DynamicProgramming;

import java.util.Arrays;

/**
 * Given an input string str and a pattern pattern, implement
 * regular expression matching with support for '.' and '*' where:
 *
 * '.' Matches any single character
 * '*' Matches zero or more of the preceding element.
 */
public class RegexMatching {
    public static void main(String[] args) {
        System.out.println(new RegexMatching().isMatch("mississippi", "mis*is*p*."));
        System.out.println(new RegexMatching().isMatch("mississippi", "mis*is*ip*."));
    }
    /*
    https://youtu.be/DJvw8jCmxUU?t=1419
    [          m      i      s       s      i      s       s     i     p        p     i
      [true, false, false,  false, false, false, false, false, false, false, false, false],
    m [false, true,  false, false, false, false, false, false, false, false, false, false],
    i [false, false, true,  false, true,  false, false, false, false, false, false, false],
    s [false, false, false, true,  true,  false, false, false, false, false, false, false],
    s [false, false, false, false, true,  false, false, false, false, false, false, false],
    i [false, false, false, false, false, true,  false, true,  false, false, false, false],
    s [false, false, false, false, false, false, true,  true,  false, false, false, false],
    s [false, false, false, false, false, false, false, true,  false, false, false, false],
    i [false, false, false, false, false, false, false, false, true,  false, true,  false],
    p [false, false, false, false, false, false, false, false, false, true,  true,  true],
    p [false, false, false, false, false, false, false, false, false, false, true,  true],
    i [false, false, false, false, false, false, false, false, false, false, false, true]
    ]
    */
    public boolean isMatch(String str, String pattern) {
        int strLen = str.length();
        int patternLen = pattern.length();
        boolean[][] dp = new boolean[strLen + 1][patternLen + 1];
        dp[0][0] = true;

        for (int i = 2; i <= patternLen; ++i) {
            if (pattern.charAt(i - 1) == '*') {
                dp[0][i] = dp[0][i - 2]; // if last to last char matches then ignore * and last character
            }
        }

        for (int i = 1; i <= strLen; ++i) {
            for (int j = 1; j <= patternLen; ++j) {
                char pChar = pattern.charAt(j - 1);
                char sChar = str.charAt(i - 1);
                if (pChar == '*') {
                    dp[i][j] = dp[i][j - 2]; // take last to last char and ignore * and last character
                    if (pattern.charAt(j - 2) == sChar || pattern.charAt(j - 2) == '.') {
                        dp[i][j] = dp[i][j] || dp[i - 1][j];
                    }
                } else if (pChar == sChar || pChar == '.') {
                    dp[i][j] = dp[i - 1][j - 1];
                }else{
                    dp[i][j] = false;
                }
            }
        }
        return dp[strLen][patternLen];
    }
}

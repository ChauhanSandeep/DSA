package dynamicprogramming;

/**
 * Problem: Regular Expression Matching
 *
 * Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:
 * - '.' Matches any single character
 * - '*' Matches zero or more of the preceding element
 *
 * The matching should cover the entire input string (not partial).
 *
 * Example 1:
 * Input: s = "aa", p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa"
 *
 * Example 2:
 * Input: s = "aa", p = "a*"
 * Output: true
 * Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa"
 *
 * Example 3:
 * Input: s = "ab", p = ".*"
 * Output: true
 * Explanation: ".*" means "zero or more (*) of any character (.)"
 *
 * LeetCode Link: https://leetcode.com/problems/regular-expression-matching/
 *
 * Follow-up Questions:
 *
 * 1. How would you extend this to support '+' (one or more of preceding element)?
 *    Answer: Add a case for '+' similar to '*' but require at least one match of the
 *    preceding character before considering it matched. Check firstMatch && dp[i+1][j+2].
 *
 * 2. What if you need to support '?' (zero or one of preceding element)?
 *    Answer: For '?', check dp[i][j+2] (zero occurrences) or (firstMatch && dp[i+1][j+2])
 *    (one occurrence). Similar logic to '*' but without the repetition case.
 *
 * 3. How would you handle case-insensitive matching?
 *    Answer: Convert both string and pattern to lowercase before matching, or modify
 *    character comparison to use case-insensitive comparison like Character.toLowerCase().
 *
 * 4. Can you optimize space to O(n) instead of O(m*n)?
 *    Answer: Yes, use rolling array technique. Since we only need the previous row to
 *    compute current row, maintain two 1D arrays and alternate between them.
 *
 * 5. How would you support character classes like [a-z] or [0-9]?
 *    Answer: Parse the pattern to identify character classes and modify the matching logic
 *    to check if current character belongs to the specified class range.
 *    Related problem: https://leetcode.com/problems/wildcard-matching/
 */
public class RegularExpressionMatching {

  public static void main(String[] args) {
    RegularExpressionMatching matcher = new RegularExpressionMatching();
    System.out.println(matcher.isMatch("mississippi", "mis*is*p*."));  // false
    System.out.println(matcher.isMatch("mississippi", "mis*is*ip*.")); // true
    System.out.println(matcher.isMatch("aa", "a"));                    // false
    System.out.println(matcher.isMatch("aa", "a*"));                   // true
    System.out.println(matcher.isMatch("ab", ".*"));                   // true
  }

  /**
   * Determines if string matches pattern using bottom-up dynamic programming.
   *
   * Algorithm:
   * 1. Create DP table where dp[i][j] = true if s[0..i-1] matches p[0..j-1]
   * 2. Initialize dp[0][0] = true (empty string matches empty pattern)
   * 3. Handle patterns starting with * (a* can match empty string)
   * 4. For each cell, check:
   *    - If pattern char is letter or '.': must match current character
   *    - If pattern char is '*': can match zero or more of preceding element
   * 5. Result is in dp[m][n]
   *
   * Key insight: '*' has two choices:
   * - Match zero occurrences: ignore current pattern char and '*', use dp[i][j-2]
   * - Match one or more: if current chars match, move in string, keep pattern at '*'
   *
   * Time Complexity: O(M * N) where M is length of string and N is length of pattern.
   * Each cell is computed once with constant time operations.
   *
   * Space Complexity: O(M * N) for the DP table.
   *
   * @param s input string to match
   * @param p pattern with '.' and '*' support
   * @return true if entire string matches pattern
   */
  public boolean isMatch(String s, String p) {
    int m = s.length();
    int n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];

    // Empty string matches empty pattern
    dp[0][0] = true;

    // Handle patterns like a*, a*b*, etc. that can match empty string
    for (int j = 2; j <= n; j++) {
      if (p.charAt(j - 1) == '*') {
        dp[0][j] = dp[0][j - 2];
      }
    }

    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        char sChar = s.charAt(i - 1);
        char pChar = p.charAt(j - 1);

        if (pChar == '*') {
          // '*' matches zero or more of preceding element
          char prevPatternChar = p.charAt(j - 2);  // Get the character BEFORE '*'

          // Option 1: Match ZERO occurrences - ignore both the character and '*'
          dp[i][j] = dp[i][j - 2];

          // Option 2: Match ONE OR MORE occurrences
          if (charMatches(sChar, prevPatternChar)) {
            dp[i][j] = dp[i][j] || dp[i - 1][j];
          }
        } else {
          // Current pattern char is letter or '.'
          if (charMatches(sChar, pChar)) {
            dp[i][j] = dp[i - 1][j - 1];
          }
        }
      }
    }

    return dp[m][n];
  }

  // Helper method to check if character matches pattern character
  private boolean charMatches(char sChar, char pChar) {
    return pChar == '.' || sChar == pChar;
  }
}

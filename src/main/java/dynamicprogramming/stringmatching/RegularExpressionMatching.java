package dynamicprogramming.stringmatching;

/**
 * Problem: Regular Expression Matching
 *
 * Match an entire string against a pattern containing normal characters, '.' for any one character, and '*' for zero or more of the previous element.
 *
 * Leetcode: https://leetcode.com/problems/regular-expression-matching/ (Hard)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | String matching | Regex operators
 *
 * Example:
 *   Input:  str = "aa", pattern = "a*"
 *   Output: true
 *   Why:    '*' repeats the preceding 'a' enough times to cover the full string.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Wildcard Matching (44), Edit Distance (72).
 */
public class RegularExpressionMatching {

    public static void main(String[] args) {
    RegularExpressionMatching matcher = new RegularExpressionMatching();
    String[][] inputs = { {"aa", "a"}, {"aa", "a*"}, {"ab", ".*"}, {"", "c*"} };
    boolean[] expected = {false, true, true, true};
    for (int i = 0; i < inputs.length; i++) {
      boolean got = matcher.isMatch(inputs[i][0], inputs[i][1]);
      System.out.printf("str=%s pattern=%s -> %s  expected=%s%n", inputs[i][0], inputs[i][1], got, expected[i]);
    }
  }

    /**
   * Intuition: dp[strIndex][patternIndex] means the two prefixes fully match. Normal characters and dot consume one from both sides. Star either deletes the preceding pattern element or consumes one matching string character while staying on the same pattern prefix.
   *
   * Algorithm:
   *   1. Create dp and set dp[0][0] = true.
   *   2. Initialize empty-string matches for a*, a*b*, and similar prefixes.
   *   3. Iterate string and pattern prefixes.
   *   4. For star, combine zero-occurrence and one-or-more transitions.
   *   5. For normal or dot, copy the diagonal state when characters match.
   *
   * Time:  O(stringLength * patternLength) - every cell is computed once.
   * Space: O(stringLength * patternLength) - stores the table.
   *
   * @param str input string
   * @param pattern regex pattern
   * @return true if the whole string matches
   */
public boolean isMatch(String str, String pattern) {
    int stringLength = str.length();
    int patternLength = pattern.length();
    boolean[][] dp = new boolean[stringLength + 1][patternLength + 1];

    // Empty string matches empty pattern
    dp[0][0] = true;

    // Handle patterns like a*, a*b*, etc. that can match empty string
    for (int j = 2; j <= patternLength; j++) {
      if (pattern.charAt(j - 1) == '*') {
        dp[0][j] = dp[0][j - 2];
      }
    }

    for (int strIndex = 1; strIndex <= stringLength; strIndex++) {
      for (int patternIndex = 1; patternIndex <= patternLength; patternIndex++) {
        char sChar = str.charAt(strIndex - 1);
        char pChar = pattern.charAt(patternIndex - 1);

        if (pChar == '*') {
          // '*' matches zero or more of preceding element
          char prevPatternChar = pattern.charAt(patternIndex - 2);  // Get the character BEFORE '*'

          // Option 1: Match ZERO occurrences - ignore both the character and '*'
          dp[strIndex][patternIndex] = dp[strIndex][patternIndex - 2];

          // Option 2: Match ONE OR MORE occurrences
          if (charMatches(sChar, prevPatternChar)) {
            // if current character matches the previous character, then we can match one or more occurrences
            dp[strIndex][patternIndex] = dp[strIndex][patternIndex] || dp[strIndex - 1][patternIndex];
          }
        } else {
          // Current pattern char is letter or '.'
          if (charMatches(sChar, pChar)) {
            dp[strIndex][patternIndex] = dp[strIndex - 1][patternIndex - 1];
          }
        }
      }
    }

    return dp[stringLength][patternLength];
  }

  // Helper method to check if character matches pattern character
  /** Checks whether a string character matches one pattern character. */
  private boolean charMatches(char sChar, char pChar) {
    return pChar == '.' || sChar == pChar;
  }
}

package DynamicProgramming;

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
 * Follow-up Questions for FAANG Interviews:
 * 1. Q: Can you optimize space complexity? A: Yes, use rolling array or recursion with memoization O(min(m,n))
 * 2. Q: How would you handle case-insensitive matching? A: Convert both strings to lowercase before processing
 * 3. Q: What if we add '+' operator (one or more)? A: Similar to '*' but ensure at least one match before extending
 * 4. Q: Can you implement using recursion with memoization? A: Yes, top-down approach with HashMap for caching
 * 5. Q: Wildcard Matching variation? A: https://leetcode.com/problems/wildcard-matching/ - similar DP approach
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
   * Determines if the entire input string matches the regular expression pattern using Dynamic Programming.
   *
   * Algorithm Steps:
   * 1. Create a 2D DP table where dp[i][j] represents if text[0...i-1] matches pattern[0...j-1]
   * 2. Initialize base case: empty string matches empty pattern
   * 3. Handle patterns that can match empty string (patterns with '*')
   * 4. Fill DP table by considering three cases:
   *    - Character match or '.' wildcard
   *    - '*' operator for zero or more occurrences
   *    - No match case
   *
   * Time Complexity: O(m * n) where m = length of text, n = length of pattern
   * Space Complexity: O(m * n) for the DP table
   *
   * @param inputText the input string to match
   * @param regexPattern the regular expression pattern
   * @return true if the entire input string matches the pattern, false otherwise
   */
  public boolean isMatch(String inputText, String regexPattern) {
    if (inputText == null || regexPattern == null) {
      return false;
    }

    int textLength = inputText.length();
    int patternLength = regexPattern.length();

    // dp[i][j] represents if inputText[0...i-1] matches regexPattern[0...j-1]
    boolean[][] matchTable = new boolean[textLength + 1][patternLength + 1];

    // Base case: empty string matches empty pattern
    matchTable[0][0] = true;

    // Handle patterns that can match empty string (contains '*' operators)
    for (int patternIndex = 2; patternIndex <= patternLength; patternIndex++) {
      if (regexPattern.charAt(patternIndex - 1) == '*') {
        // '*' can eliminate the preceding character, making it match empty string
        matchTable[0][patternIndex] = matchTable[0][patternIndex - 2];
      }
    }

    // Fill the DP table
    for (int textIndex = 1; textIndex <= textLength; textIndex++) {
      for (int patternIndex = 1; patternIndex <= patternLength; patternIndex++) {
        char currentPatternChar = regexPattern.charAt(patternIndex - 1);
        char currentTextChar = inputText.charAt(textIndex - 1);

        if (currentPatternChar == '*') {
          // Handle '*' operator: zero or more of the preceding element
          char precedingPatternChar = regexPattern.charAt(patternIndex - 2);

          // Case 1: Use '*' to match zero occurrences (ignore preceding char and '*')
          matchTable[textIndex][patternIndex] = matchTable[textIndex][patternIndex - 2];

          // Case 2: Use '*' to match one or more occurrences
          if (isCharacterMatch(currentTextChar, precedingPatternChar)) {
            matchTable[textIndex][patternIndex] =
                matchTable[textIndex][patternIndex] || matchTable[textIndex - 1][patternIndex];
          }
        } else if (isCharacterMatch(currentTextChar, currentPatternChar)) {
          // Direct character match or '.' wildcard match
          matchTable[textIndex][patternIndex] = matchTable[textIndex - 1][patternIndex - 1];
        }
        // If no match, matchTable[textIndex][patternIndex] remains false (default)
      }
    }

    return matchTable[textLength][patternLength];
  }

  /**
   * Checks if a text character matches a pattern character.
   *
   * @param textChar character from the input text
   * @param patternChar character from the pattern ('.' matches any character)
   * @return true if characters match, false otherwise
   */
  private boolean isCharacterMatch(char textChar, char patternChar) {
    return patternChar == '.' || textChar == patternChar;
  }
}

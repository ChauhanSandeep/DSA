package stacksandqueues;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Minimum Remove to Make Valid Parentheses
 *
 * Remove the fewest parentheses from a string so every remaining '(' has a
 * later matching ')' and every ')' has an earlier unmatched '('. Letters and
 * other non-parenthesis characters must keep their original relative order.
 *
 * Leetcode: https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/ (Medium)
 * Rating:   zerotrac 1657
 * Pattern:  Stack | Parentheses matching | Mark invalid indices
 *
 * Example:
 *   Input:  s = "lee(t(c)o)de)"
 *   Output: "lee(t(c)o)de"
 *   Why:    the trailing ')' has no matching opening parenthesis, so removing it balances the string.
 *
 * Follow-ups:
 *   1. Return all valid strings after minimum removals?
 *      Use BFS by deletion level, stopping at the first level that contains valid strings.
 *   2. Support multiple bracket types like [] and {}?
 *      Track opening bracket indices and require type-compatible closings.
 *   3. Need minimum insertions instead of removals?
 *      Count unmatched closings and leftover openings, or construct insertions greedily.
 *   4. Stream the string without storing all characters?
 *      You can drop invalid closings online, but unmatched openings require buffering positions.
 *
 * Related: Valid Parentheses (20), Remove Invalid Parentheses (301).
 */
public class MinimumRemoveToMakeValidParentheses {
  public static void main(String[] args) {
    String[] inputs = {"", "lee(t(c)o)de)", "a)b(c)d", "))(("};
    String[] expected = {"", "lee(t(c)o)de", "ab(c)d", ""};
    for (int i = 0; i < inputs.length; i++) {
      String got = minRemoveToMakeValidInSinglePass(inputs[i]);
      System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
    }
  }

  /**
   *
   * Approach:
   * - First pass (left to right): Skip invalid closing parentheses ')'.
   * - Second pass (right to left): Skip invalid opening parentheses '('.
   * - Only valid parentheses and letters are kept in the final result.
   *
   * Time Complexity: O(N) where N is the length of the input string.
   * Space Complexity: O(N) for storing intermediate characters.
   *
   * @param str Input string containing '(', ')' and lowercase letters.
   * @return A valid string with balanced parentheses.
   */
  public String minRemoveToMakeValidWith2Pass(String str) {
    StringBuilder afterFirstPass = new StringBuilder();
    int openCount = 0;

    // First pass: Remove unmatched closing ')'
    for (char ch : str.toCharArray()) {
      if (ch == '(') {
        openCount++;
        afterFirstPass.append(ch);
      } else if (ch == ')') {
        if (openCount > 0) {
          openCount--;
          afterFirstPass.append(ch);
        }
        // Else skip unmatched ')'
      } else {
        afterFirstPass.append(ch); // append letter
      }
    }

    // Second pass: Remove unmatched opening '('
    StringBuilder finalResult = new StringBuilder();
    int closeCount = 0;

    for (int i = afterFirstPass.length() - 1; i >= 0; i--) {
      char ch = afterFirstPass.charAt(i);
      if (ch == ')') {
        closeCount++;
        finalResult.append(ch);
      } else if (ch == '(') {
        if (closeCount > 0) {
          closeCount--;
          finalResult.append(ch);
        }
        // Else skip unmatched '('
      } else {
        finalResult.append(ch); // append letter
      }
    }

    return finalResult.reverse().toString();
  }

/**
   * Intuition: invalid parentheses are exactly those that cannot find a match.
   * `invalidOpenIndices` stores opens waiting for a later close; a close either
   * consumes the latest open or is invalid immediately. Leftover opens after the
   * scan are also invalid.
   *
   * Algorithm:
   *   1. Scan and track unmatched '(' indices.
   *   2. Match valid ')' characters or record invalid close indices.
   *   3. Combine leftover opens with invalid closes and sort them.
   *   4. Rebuild the string while skipping indices to remove.
   *
   * Time:  O(n log n) - sorting invalid indices dominates in the worst case.
   * Space: O(n) - index lists and output builder can grow with the input.
   *
   * @param str input string containing parentheses and other characters
   * @return a valid string after minimum removals
   */
public static String minRemoveToMakeValidInSinglePass(String str) {
    // opening parenthesis which are not able to closed are stored in invalidOpenIndices
    List<Integer> invalidOpenIndices = new ArrayList<>();
    // closing parenthesis which do not have corresponding opening parenthesis are stored in invalidCloseIndices
    List<Integer> invalidCloseIndices = new ArrayList<>();

    // Identify misplaced parentheses
    for (int i = 0; i < str.length(); i++) {
      char currentChar = str.charAt(i);
      if (currentChar == '(') {
        invalidOpenIndices.add(i);
      } else if (currentChar == ')') {
        if (!invalidOpenIndices.isEmpty()) {
          invalidOpenIndices.remove(invalidOpenIndices.size() - 1);
        } else {
          invalidCloseIndices.add(i);
        }
      }
    }

    // Combine invalid indices and sort them
    List<Integer> indicesToRemove = new ArrayList<>(invalidOpenIndices);
    indicesToRemove.addAll(invalidCloseIndices);
    indicesToRemove.sort(Integer::compareTo);

    // Construct the valid string by skipping invalid indices
    StringBuilder validString = new StringBuilder();
    int removalIndex = 0;

    for (int i = 0; i < str.length(); i++) {
      if (removalIndex < indicesToRemove.size() && i == indicesToRemove.get(removalIndex)) {
        removalIndex++;
      } else {
        validString.append(str.charAt(i));
      }
    }

    return validString.toString();
  }
}

package String;

/**
 * Leetcode: https://leetcode.com/problems/process-string-with-special-operations-ii
 *
 * Problem Statement:
 * You are given a string `s` consisting of lowercase English letters and the special characters: '*', '#', and '%'.
 * You are also given an integer `k`.
 *
 * Process the string from left to right to build a result string following these rules:
 * - If the character is a lowercase letter, append it to the result.
 * - If the character is '*', remove the last character from the result if it exists.
 * - If the character is '#', duplicate the current result and append it to itself.
 * - If the character is '%', reverse the current result string.
 *
 * Your task is to return the kth (0-indexed) character of the final result string. If k is out of bounds, return '.'.
 *
 * Example:
 * Input: s = "cd%#*#", k = 3
 * Output: "d"
 *
 * Explanation:
 * - Start: ""
 * - 'c' → "c"
 * - 'd' → "cd"
 * - '%' → "dc"
 * - '#' → "dcdc"
 * - '*' → "dcd"
 * - '#' → "dcddcd"
 * - Return char at index 3 → "d"
 *
 * Follow-Up Questions:
 * 1. Can we construct the final string and return the k-th char? (Not feasible for large strings - O(n) space)
 * 2. Can we simulate this in reverse to avoid full construction? (Yes, and that’s what this optimal solution does)
 * 3. How to handle huge values of `k` (up to 10^15)? (Avoid string construction, use length simulation)
 *
 * Time Complexity: O(n) where n = length of input string
 * Space Complexity: O(1) extra space
 */
public class SpecialOperationsString {

  private static final char REMOVE_LAST = '*';
  private static final char DUPLICATE = '#';
  private static final char REVERSE = '%';

  /**
   * Returns the k-th character in the final string after processing, or '.' if k is out of bounds.
   *
   * @param inputStr The input string containing lowercase letters and special characters '*', '#', '%'.
   * @param k The 0-indexed position to fetch from the processed result.
   * @return The k-th character or '.' if out of bounds.
   *
   * Steps:
   * 1. First pass (Left to Right): Calculate only the length of the final string without building it.
   * 2. Second pass (Right to Left): Trace back the operations to find the k-th character using reverse simulation.
   */
  public char processStringAndFindKthChar(String inputStr, long k) {
    // Used to simulate result string's length
    long simulatedLength = 0;

    // Forward pass: simulate length without constructing the final string
    for (char ch : inputStr.toCharArray()) {
      if (Character.isLowerCase(ch)) {
        simulatedLength++;
      } else if (ch == REMOVE_LAST && simulatedLength > 0) {
        simulatedLength--;
      } else if (ch == DUPLICATE) {
        simulatedLength *= 2;
      } else if (ch == REVERSE) {
        // Length remains same
      }
    }

    if (k >= simulatedLength) {
      // Not enough characters in the final string
      return '.';
    }

    /**
     * Backward pass: simulate backwards to figure out which char ends up at position k.
     * We traverse the string in reverse order, and for each character:
     * - If it's a lowercase letter, we decrease the simulated length.
     * - If it's a '*', we increase the simulated length (as it removes the last character).
     * - If it's a '#', we check if k is in the first half or second half of the duplicated string.
     * * - If it's a '%', we flip the index k to its mirrored position.
     * * This way, we can find the character at position k without constructing the full string.
     */
    for (int i = inputStr.length() - 1; i >= 0; i--) {
      char ch = inputStr.charAt(i);

      if (ch == REVERSE) {
        // Reverse flips the index
        // after flipping, k is at the mirrored position
        k = simulatedLength - 1 - k;
      } else if (ch == DUPLICATE) {
        simulatedLength /= 2;
        if (k >= simulatedLength) {
          // k lies in second half → shift to mirror of first half
          k -= simulatedLength;
        }
      } else if (ch == REMOVE_LAST) {
        simulatedLength++;
      } else if (Character.isLowerCase(ch)) {
        simulatedLength--;
        if (simulatedLength == k) {
          return ch;
        }
      }
    }

    return '.';
  }
}
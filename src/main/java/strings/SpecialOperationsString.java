package strings;

/**
 * Problem: Process String with Special Operations II
 *
 * Process lowercase letters plus '*', '#', and '%' where letters append, '*'
 * deletes, '#' duplicates, and '%' reverses. Return the kth final character or '.'.
 *
 * Leetcode: https://leetcode.com/problems/process-string-with-special-operations-ii/ (Hard)
 * Rating:   zerotrac 2011 (Q3, weekly-458)
 * Pattern:  String | Reverse simulation | Length-only processing
 *
 * Example:
 *   Input:  inputStr = "cd%#*#", k = 3
 *   Output: 'd'
 *   Why:    the final string is "dcddcd", whose index 3 is 'd'.
 *
 * Follow-ups:
 *   1. Why not build the string? Duplication can make it too large.
 *   2. Huge k? Use long lengths and cap values above the queried range.
 *   3. Return a range? Map requested indices backward through the operations.
 */
public class SpecialOperationsString {

  public static void main(String[] args) {
    SpecialOperationsString solver = new SpecialOperationsString();
    String[] inputs = {"cd%#*#", "abc*", "a#"};
    long[] indices = {3, 2, 1};
    char[] expected = {'d', '.', 'a'};
    for (int i = 0; i < inputs.length; i++) {
      char got = solver.processStringAndFindKthChar(inputs[i], indices[i]);
      System.out.printf("inputStr=%s k=%d -> %c  expected=%c%n", inputs[i], indices[i], got, expected[i]);
    }
  }


  private static final char REMOVE_LAST = '*';
  private static final char DUPLICATE = '#';
  private static final char REVERSE = '%';

    /**
   * Intuition: the final string can be too large, but each operation has a simple
   * effect on length and on an index. Compute final length, then walk operations
   * backward to map k into the previous virtual string until it reaches a letter.
   *
   * Algorithm:
   *   1. Simulate only the final length from left to right.
   *   2. Return '.' if k is outside that length.
   *   3. Walk right to left and undo reverse, duplicate, and delete operations.
   *   4. Return the lowercase character that maps to k.
   *
   * Time:  O(n) - two scans of the operation string.
   * Space: O(1) - only length and index state are stored.
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
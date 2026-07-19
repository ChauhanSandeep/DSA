package strings;

/**
 * Problem: String to Integer (atoi)
 *
 * Convert a string to a 32-bit signed integer by skipping leading spaces,
 * reading one optional sign, consuming digits, and clamping overflow.
 *
 * Leetcode: https://leetcode.com/problems/string-to-integer-atoi/ (Medium)
 * Rating:   acceptance 21.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  String | Parsing | Overflow-safe accumulation
 *
 * Example:
 *   Input:  input = "   -42"
 *   Output: -42
 *   Why:    spaces are ignored, '-' sets the sign, and parsing stops after digits.
 *
 * Follow-ups:
 *   1. Unicode whitespace? Use Character.isWhitespace instead of only ' '.
 *   2. Floating-point input? Add parser states for dot and exponent.
 *   3. Different bases? Use Character.digit and radix-specific overflow checks.
 *
 * Related: Valid Number (65).
 */
public class AtoI {

  public static void main(String[] args) {
    String[] inputs = {"42", "   -42", "4193 with words", "words and 987", "-91283472332"};
    int[] expected = {42, -42, 4193, 0, Integer.MIN_VALUE};
    for (int i = 0; i < inputs.length; i++) {
      int got = aToI(inputs[i]);
      System.out.printf("input=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
    }
  }


    /**
   * Intuition: parse only the prefix that atoi accepts. After whitespace and an
   * optional sign, each digit is appended to the running value only after checking
   * that the append will not overflow a 32-bit signed integer.
   *
   * Algorithm:
   *   1. Return 0 for null or empty input.
   *   2. Skip leading spaces and read one optional sign.
   *   3. Consume digits until a non-digit appears, checking overflow before append.
   *   4. Return the signed value or the correct clamp on overflow.
   *
   * Time:  O(n) - the parser scans at most one prefix of the string.
   * Space: O(1) - only parser state and the running value are stored.
   */
  public static int aToI(String input) {
      if (input == null || input.isEmpty()) {
          return 0;
      }

    int index = 0;
    int length = input.length();
    int sign = 1;
    int result = 0;

    // Step 1: Skip leading whitespaces
    while (index < length && input.charAt(index) == ' ') {
      index++;
    }

    // Step 2: Handle optional sign character
    if (index < length && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
      sign = (input.charAt(index) == '-') ? -1 : 1;
      index++;
    }

    // Step 3: Process digits and check for overflow
    while (index < length && Character.isDigit(input.charAt(index))) {
      int digit = input.charAt(index) - '0';

      // Check if adding the digit will cause overflow
      // if (result * 10 + digit > Integer.MAX_VALUE) is not safe due to overflow risk
      if (result > (Integer.MAX_VALUE - digit) / 10) {
        return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
      }

      result = (result * 10) + digit;
      index++;
    }

    return result * sign;
  }
}

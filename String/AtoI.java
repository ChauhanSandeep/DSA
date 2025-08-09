package String;

/**
 * LeetCode Problem: https://leetcode.com/problems/string-to-integer-atoi/
 *
 * Problem Statement:
 * Implement the `atoi` function that converts a string to an integer, replicating the behavior of the C `atoi` function.
 *
 * The function must handle:
 * - Leading whitespace
 * - Optional '+' or '-' sign
 * - Consecutive digits
 * - Characters after the number (ignored)
 * - Integer overflow or underflow
 *
 * Example:
 * Input: "   -42"
 * Output: -42
 *
 * Follow-up Questions:
 * 1. How would you handle locale-specific number formats (e.g., commas, currency symbols)?
 *    - Use `NumberFormat` class in Java with appropriate locale.
 * 2. How would you handle floating point inputs like "3.14"?
 *    - Use `Double.parseDouble()` or write a custom parser to handle decimal point.
 * 3. Can you support scientific notation like "1.2e3"?
 *    - Not with this implementation; would require a separate parser or regex-based tokenizer.
 */

public class AtoI {

  public static void main(String[] args) {
    System.out.println(aToI("42"));                 // 42
    System.out.println(aToI("   -42"));             // -42
    System.out.println(aToI("4193 with words"));    // 4193
    System.out.println(aToI("words and 987"));      // 0
    System.out.println(aToI("-91283472332"));       // Integer.MIN_VALUE
  }

  /**
   * Converts a string to a 32-bit signed integer (similar to C's atoi).
   *
   * Steps:
   * - Trim leading whitespaces
   * - Parse optional '+' or '-' sign
   * - Parse digits until non-digit encountered
   * - Clamp result to Integer range if overflow/underflow occurs
   *
   * Time Complexity: O(N), where N is length of input string
   * Space Complexity: O(1), uses constant extra space
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

package strings;

/**
 * Valid Number Checker
 *
 * -------------------------------------
 * 🧠 Problem Statement:
 * -------------------------------------
 * Given a string `s`, determine if it represents a valid number. Valid numbers include:
 * - Integers (e.g., "2", "0089", "-90", "+6")
 * - Decimals (e.g., "-0.1", "3.14", "4.", "-.9")
 * - Scientific notation (e.g., "2e10", "-90E3", "3e+7", "53.5e93")
 *
 * Invalid numbers include those with:
 * - Alphabetic characters: "abc", "1a", "95a54e53"
 * - Misused exponent: "1e", "e3", "99e2.5"
 * - Misplaced or extra signs: "--6", "-+3"
 *
 * 🔗 Leetcode Link:
 * https://leetcode.com/problems/valid-number/
 *
 * 🔍 Example:
 * Input: "53.5e93" → true
 * Input: "1e" → false
 *
 * -------------------------------------
 * ❓ Follow-Up Questions:
 * -------------------------------------
 * 1. Can this be implemented using a regex?
 *    - Yes, though harder to maintain and debug. Regex pattern exists for compact solution.
 * 2. How would you write a parser for floating-point numbers?
 *    - Split into tokens, handle sign, digits, dot, and exponent using a state machine.
 * 3. Is this a valid use case for a finite state machine (FSM)?
 *    - Yes. You can model each parsing state explicitly for maintainability and extensibility.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */

public class ValidNumberChecker {

  public static void main(String[] args) {
    String[] validNumbers =
        {"2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789"};

    String[] invalidNumbers = {"abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53"};

    ValidNumberChecker validator = new ValidNumberChecker();

    System.out.println("✅ Valid Numbers:");
    for (String number : validNumbers) {
      System.out.println(number + " → " + validator.isValidNumber(number));
    }

    System.out.println("\n❌ Invalid Numbers:");
    for (String number : invalidNumbers) {
      System.out.println(number + " → " + validator.isValidNumber(number));
    }
  }

  /**
   * Determines whether a given string is a valid number.
   *
   * Steps:
   * 1. Track flags for digit, decimal, exponent, and sign.
   * 2. Iterate over characters and apply rules based on current state.
   * 3. Handle special rules for signs and exponent placement.
   * 4. Ensure at least one digit is present at the end.
   *
   * Time Complexity: O(N) — Single pass through string.
   * Space Complexity: O(1)
   *
   * @param str The string to validate.
   * @return true if valid number; false otherwise.
   */
  public boolean isValidNumber(String str) {
    boolean seenDigit = false;
    boolean seenDot = false;
    boolean seenExp = false;
    boolean seenSign = false;

    for (int i = 0; i < str.length(); i++) {
      char current = str.charAt(i);

      if (Character.isDigit(current)) {
        seenDigit = true;
      } else if (current == '.') {
        // Invalid if dot appears after exponent or already exists
          if (seenDot || seenExp) {
              return false;
          }
        seenDot = true;
      } else if (current == 'e' || current == 'E') {
        // Exponent must follow a digit and appear only once
          if (seenExp || !seenDigit) {
              return false;
          }
        seenExp = true;

        // Reset digit & sign flags for the exponent part
        seenDigit = false;
        seenSign = false;
      } else if (current == '+' || current == '-') {
        // Sign must appear at start or after an exponent
        if (seenSign || (i > 0 && str.charAt(i - 1) != 'e' && str.charAt(i - 1) != 'E')) {
          return false;
        }
        seenSign = true;
      } else {
        return false; // Invalid character
      }
    }

    return seenDigit; // Valid number must end with a digit
  }
}

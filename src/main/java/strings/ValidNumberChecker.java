package strings;

/**
 * Problem: Valid Number
 *
 * Determine whether a string is a valid decimal number with optional sign,
 * decimal point, and exponent with its own optional sign.
 *
 * Leetcode: https://leetcode.com/problems/valid-number/ (Hard)
 * Rating:   acceptance 23.2% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  String | Parser state | Flag validation
 *
 * Example:
 *   Input:  str = "53.5e93"
 *   Output: true
 *   Why:    the base and exponent both contain digits in valid positions.
 *
 * Follow-ups:
 *   1. Regex solution? Possible, but a state parser is easier to explain and extend.
 *   2. Hex literals? Add prefix states and base-specific digit checks.
 *   3. Error reporting? Return the first invalid index and failed rule.
 */
public class ValidNumberChecker {

  public static void main(String[] args) {
    ValidNumberChecker validator = new ValidNumberChecker();
    String[] inputs = {"2", "53.5e93", "1e", "--6"};
    boolean[] expected = {true, true, false, false};
    for (int i = 0; i < inputs.length; i++) {
      boolean got = validator.isValidNumber(inputs[i]);
      System.out.printf("str=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
    }
  }


    /**
   * Intuition: validation is a small state machine represented by flags. Digits
   * make the current part valid, dot is allowed only before exponent, exponent is
   * allowed only after digits, and sign is allowed only at the start of a part.
   *
   * Algorithm:
   *   1. Track whether digit, dot, exponent, and sign have appeared.
   *   2. Scan each character and update the matching state.
   *   3. Reject invalid dot, exponent, sign, or unknown character placements.
   *   4. Return true only if the final part has a digit.
   *
   * Time:  O(n) - one pass over the string.
   * Space: O(1) - only flags are stored.
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

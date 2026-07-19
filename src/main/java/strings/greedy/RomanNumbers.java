package strings.greedy;

import java.util.HashMap;
import java.util.Map;


/**
 * Problem: Roman Numbers
 *
 * Convert Roman numerals to integers and convert integers in the range 1..3999
 * back to canonical Roman numerals. The same symbol table supports additive and
 * subtractive notation such as IV, IX, XL, and CM.
 *
 * Leetcode: https://leetcode.com/problems/roman-to-integer/ (Easy)
 * Leetcode: https://leetcode.com/problems/integer-to-roman/ (Medium)
 * Rating:   no contest Elo (pre-contest problems)
 * Pattern:  Greedy | Symbol values | Subtractive notation
 *
 * Example:
 *   Input:  roman = "MCMIV", number = 58
 *   Output: 1904 and "LVIII"
 *   Why:    MCMIV is 1000 + 900 + 4, while 58 is L + V + III.
 *
 * Follow-ups:
 *   1. Validate that a Roman numeral is canonical?
 *      Convert it to an integer and back, then compare with the original string.
 *   2. Support values above 3999?
 *      Add an overline or parenthesized multiplier convention to the symbol table.
 *   3. Parse lowercase input?
 *      Normalize the string to uppercase before reading symbols.
 *
 * Related: Roman to Integer (13), Integer to Roman (12).
 */
public class RomanNumbers {

    public static void main(String[] args) {
        String[] romanInputs = {"MCMIV", "LVIII"};
        int[] romanExpected = {1904, 58};
        int[] numberInputs = {58, 1994};
        String[] numberExpected = {"LVIII", "MCMXCIV"};

        for (int i = 0; i < romanInputs.length; i++) {
            int got = romanToInteger(romanInputs[i]);
            System.out.printf("roman=%s -> %d  expected=%d%n", romanInputs[i], got, romanExpected[i]);
        }
        for (int i = 0; i < numberInputs.length; i++) {
            String got = intToRoman(numberInputs[i]);
            System.out.printf("number=%d -> %s  expected=%s%n", numberInputs[i], got, numberExpected[i]);
        }
    }


  /**
   * Intuition: a Roman symbol usually adds its value, but a smaller symbol before
   * a larger one is subtractive. Scanning from right to left makes that decision
   * local: subtract only when the current value is less than the value already
   * seen to its right.
   *
   * Algorithm:
   *   1. Reject null or empty input.
   *   2. Build the Roman-symbol-to-value map.
   *   3. Scan from right to left, subtracting values smaller than previousValue and adding the rest.
   *   4. Update previousValue after each symbol and return the total.
   *
   * Time:  O(n) - each Roman symbol is processed once.
   * Space: O(1) - the symbol map has a fixed seven entries.
   *
   * @param roman Roman numeral string such as "MCMIV"
   * @return integer value represented by the Roman numeral
   * @throws IllegalArgumentException when the input is empty or contains an unknown symbol
   */
  public static int romanToInteger(String roman) {
    if (roman == null || roman.isEmpty()) {
      throw new IllegalArgumentException("Input Roman numeral cannot be null or empty");
    }

    Map<Character, Integer> romanToIntMap = getRomanToIntMap();

    int total = 0;
    int previousValue = 0;

    for (int i = roman.length() - 1; i >= 0; i--) {
      char symbol = roman.charAt(i);
      Integer currentValue = romanToIntMap.get(symbol);

      if (currentValue == null) {
        throw new IllegalArgumentException("Invalid Roman numeral character: " + symbol);
      }

      if (currentValue < previousValue) {
        total -= currentValue; // Subtractive case (e.g., IV = 5 - 1)
      } else {
        total += currentValue;
      }

      previousValue = currentValue;
    }

    return total;
  }

  /**
   * Intuition: canonical Roman numerals always use the largest possible symbol or
   * subtractive pair first. A descending value table lets the code repeatedly take
   * the biggest symbol that still fits until the number reaches zero.
   *
   * Algorithm:
   *   1. Reject values outside the standard 1..3999 range.
   *   2. Walk descending Roman values and their symbols.
   *   3. While the current value fits, append its symbol and subtract it from number.
   *   4. Return the accumulated Roman numeral.
   *
   * Time:  O(1) - the table has a fixed size and output length is bounded for 1..3999.
   * Space: O(1) - only the fixed tables and output builder are used.
   *
   * @param number integer value between 1 and 3999
   * @return canonical Roman numeral representation
   * @throws IllegalArgumentException when number is outside 1..3999
   */
  public static String intToRoman(int number) {
    if (number <= 0 || number > 3999) {
      throw new IllegalArgumentException("Input must be in range [1, 3999]");
    }

    final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    StringBuilder result = new StringBuilder();

    for (int i = 0; i < VALUES.length && number > 0; i++) {
      while (number >= VALUES[i]) {
        result.append(SYMBOLS[i]);
        number -= VALUES[i];
      }
    }

    return result.toString();
  }

  /** Returns the fixed Roman symbol to integer value mapping. */
  private static Map<Character, Integer> getRomanToIntMap() {
    Map<Character, Integer> romanToInt = new HashMap<>();
    romanToInt.put('I', 1);
    romanToInt.put('V', 5);
    romanToInt.put('X', 10);
    romanToInt.put('L', 50);
    romanToInt.put('C', 100);
    romanToInt.put('D', 500);
    romanToInt.put('M', 1000);
    return romanToInt;
  }
}

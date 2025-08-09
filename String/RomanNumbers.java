package String;

import java.util.HashMap;
import java.util.Map;


/**
 * Problem: Convert Roman numerals to integers and vice versa.
 *
 * LeetCode Links:
 * - Roman to Integer: https://leetcode.com/problems/roman-to-integer/
 * - Integer to Roman: https://leetcode.com/problems/integer-to-roman/
 *
 * Problem Statement:
 * - You are given a Roman numeral and need to convert it to an integer.
 * - You are given an integer (1 to 3999) and need to convert it to a Roman numeral.
 *
 * Example:
 * - romanToInteger("MCMIV") → 1904
 * - intToRoman(58) → "LVIII"
 *
 * Follow-up Questions (FAANG interview-style):
 * 1. What if you had to support values > 3999?
 *    - Roman numerals historically didn’t support large values, but you could use overlines or modern extensions.
 * 2. How to validate whether a Roman numeral string is valid?
 *    - Use regex: ^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$
 * 3. Can you design this as a bi-directional converter using a shared abstraction?
 *    - Yes, using an Enum or Symbol table class to store mapping and invertibility.
 */
public class RomanNumbers {

  public static void main(String[] args) {
    String roman = "MCMIV";
    int num = 58;

    System.out.println("Roman to Integer → " + roman + ": " + romanToInteger(roman));
    System.out.println("Integer to Roman → " + num + ": " + intToRoman(num));
  }

  /**
   * Converts a Roman numeral to an integer.
   *
   * Steps:
   * - Traverse from right to left
   * - Subtract if current value is less than the previous one (e.g., IV = 5 - 1)
   * - Otherwise, add it to the result
   *
   * Algorithm: Greedy with reverse traversal
   * Time Complexity: O(N)
   * Space Complexity: O(1)
   *
   * @param roman Roman numeral string (e.g., "MCMIV")
   * @return Integer equivalent
   * @throws IllegalArgumentException for invalid input
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
   * Converts an integer to a Roman numeral.
   *
   * Steps:
   * - Use predefined Roman symbols and values
   * - While num >= symbol value, subtract and append symbol
   *
   * Algorithm: Greedy
   * Time Complexity: O(1) – number of Roman symbols is constant
   * Space Complexity: O(1)
   *
   * @param number Integer between 1 and 3999
   * @return Roman numeral string
   * @throws IllegalArgumentException if number is out of range
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

  // Utility method to return Roman symbol to integer value mapping
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

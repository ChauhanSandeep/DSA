package String;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for converting between Roman numerals and integers.
 * 
 * Problem:
 * - Convert Roman numerals to an integer.
 * - Convert an integer to Roman numerals.
 * 
 * Approaches:
 * 1. **Roman to Integer:** 
 *    - Use a HashMap to store Roman numeral values.
 *    - Traverse the string, checking for subtraction cases (e.g., IV = 4, IX = 9).
 *    - Add or subtract values based on their relative magnitudes.
 * 
 * 2. **Integer to Roman:**
 *    - Use predefined values and corresponding symbols.
 *    - Iteratively subtract the largest possible value while appending the symbol.
 * 
 * Time Complexity:
 * - `romanToInteger()`: **O(N)** (Single pass through the string)
 * - `intToRoman()`: **O(1)** (Fixed set of Roman numeral values)
 * 
 * Space Complexity:
 * - **O(1)** (Constant extra space usage)
 * 
 * LeetCode Equivalent: https://leetcode.com/problems/integer-to-roman/ and https://leetcode.com/problems/roman-to-integer/
 */
public class RomanNumbers {
    public static void main(String[] args) {
        String romanNumeral = "MCMIV";
        int integerValue = romanToInteger(romanNumeral);
        System.out.println("Integer value for " + romanNumeral + " is " + integerValue);

        int number = 58;
        String romanResult = intToRoman(number);
        System.out.println("Roman numeral for " + number + " is " + romanResult);
    }

    /**
     * Converts a Roman numeral string to an integer.
     * @param roman The Roman numeral string (e.g., "MCMIV").
     * @return The corresponding integer value.
     * @throws IllegalArgumentException if input is invalid.
     */
    public static int romanToInteger(String roman) {
        if (roman == null || roman.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Roman numeral input.");
        }

        // Mapping of Roman numerals to integer values
        Map<Character, Integer> romanValues = new HashMap<>();
        romanValues.put('I', 1);
        romanValues.put('V', 5);
        romanValues.put('X', 10);
        romanValues.put('L', 50);
        romanValues.put('C', 100);
        romanValues.put('D', 500);
        romanValues.put('M', 1000);

        int result = 0;
        int prevValue = 0;

        // Traverse the Roman numeral string
        for (int i = roman.length() - 1; i >= 0; i--) {
            int currValue = romanValues.get(roman.charAt(i));

            // If current value is less than the previous one, subtract it (e.g., IV = 4)
            if (currValue < prevValue) {
                result -= currValue;
            } else {
                result += currValue;
            }
            prevValue = currValue;
        }

        return result;
    }

    // Predefined values and corresponding Roman symbols for conversion
    private static final int[] VALUES =    {1000, 900,  500, 400,  100,  90,  50,  40,   10,   9,   5,  4,   1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX","V","IV","I"};

    /**
     * Converts an integer to a Roman numeral.
     * @param num The integer value (1 ≤ num ≤ 3999).
     * @return The corresponding Roman numeral string.
     */
    public static String intToRoman(int num) {
        if (num <= 0 || num > 3999) {
            throw new IllegalArgumentException("Input must be between 1 and 3999.");
        }

        StringBuilder roman = new StringBuilder();

        // Convert number to Roman numeral by iterating through predefined values
        for (int i = 0; i < VALUES.length && num > 0; i++) {
            while (num >= VALUES[i]) {
                num -= VALUES[i];
                roman.append(SYMBOLS[i]);
            }
        }

        return roman.toString();
    }
}

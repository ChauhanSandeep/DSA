package strings.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Roman to Integer
 *
 * Convert a Roman numeral into its integer value. Roman symbols usually add
 * their values, but a smaller symbol before a larger one represents subtraction,
 * as in IV, IX, XL, XC, CD, and CM.
 *
 * Leetcode: https://leetcode.com/problems/roman-to-integer/ (Easy)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Hash map | Reverse scan | Subtractive notation
 *
 * Example:
 *   Input:  s = "MCMIV"
 *   Output: 1904
 *   Why:    M is 1000, CM is 900, and IV is 4.
 *
 * Follow-ups:
 *   1. Validate canonical Roman numerals?
 *      Convert to integer, convert back with Integer to Roman, and compare strings.
 *   2. Support lowercase input?
 *      Normalize with toUpperCase before scanning.
 *   3. Avoid building the map on every call?
 *      Use a static final map or switch expression for the seven symbols.
 *
 * Related: Integer to Roman (12).
 */
public class RomanToInt {
    public static void main(String[] args) {
        String[] inputs = {"MCMIV", "LVIII", "III"};
        int[] expected = {1904, 58, 3};

        for (int i = 0; i < inputs.length; i++) {
            int got = romanToInteger(inputs[i]);
            System.out.printf("roman=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


    /**
     * Intuition: when scanning from right to left, the largest symbol to the right
     * tells whether the current symbol should add or subtract. A smaller current
     * value means it belongs to a subtractive pair; otherwise it contributes normally.
     *
     * Algorithm:
     *   1. Reject null or blank input.
     *   2. Store Roman symbol values in a map.
     *   3. Scan from right to left, subtracting values smaller than lastSeenValue and adding the rest.
     *   4. Update lastSeenValue after each symbol and return the total.
     *
     * Time:  O(n) - one pass over the Roman numeral.
     * Space: O(1) - the value map contains seven fixed symbols.
     *
     * @param roman Roman numeral string to convert
     * @return integer represented by roman
     */
    public static int romanToInteger(String roman) {
        if (roman == null || roman.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Roman numeral input.");
        }

        // Map storing Roman numeral values
        Map<Character, Integer> romanValues = new HashMap<>();
        romanValues.put('I', 1);
        romanValues.put('V', 5);
        romanValues.put('X', 10);
        romanValues.put('L', 50);
        romanValues.put('C', 100);
        romanValues.put('D', 500);
        romanValues.put('M', 1000);

        int result = 0;
        int lastSeenValue = 0;

        // Traverse the string from right to left
        for (int i = roman.length() - 1; i >= 0; i--) {
            char currentChar = roman.charAt(i);

            // Validate character
            if (!romanValues.containsKey(currentChar)) {
                throw new IllegalArgumentException("Invalid character in Roman numeral: " + currentChar);
            }

            int currValue = romanValues.get(currentChar);

            // If current value is smaller than the last seen value, subtract it
            if (currValue < lastSeenValue) {
                result -= currValue;
            } else {
                result += currValue;
            }

            lastSeenValue = currValue;
        }

        return result;
    }
}

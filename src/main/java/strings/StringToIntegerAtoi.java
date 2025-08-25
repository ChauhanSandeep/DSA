package strings;

import java.util.*;

/**
 * Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer.
 *
 * The algorithm for myAtoi(string s) is as follows:
 * 1. Read in and ignore any leading whitespace.
 * 2. Check if the next character (if not already at the end of the string) is '-' or '+'.
 * 3. Read in next the characters until the next non-digit character or the end of the input is reached.
 * 4. Convert these digits into an integer (i.e., "123" -> 123, "0032" -> 32).
 * 5. If the integer is out of the 32-bit signed integer range [-2^31, 2^31 - 1],
 *    clamp the integer so that it remains in the range.
 *
 * Example 1:
 * Input: s = "42"
 * Output: 42
 *
 * Example 2:
 * Input: s = "   -42"
 * Output: -42
 *
 * Example 3:
 * Input: s = "4193 with words"
 * Output: 4193
 *
 * LeetCode: https://leetcode.com/problems/string-to-integer-atoi/
 *
 * Follow-up Questions:
 * 1. How would you handle non-ASCII whitespace characters?
 *    - We can use Character.isWhitespace() to handle all Unicode whitespace.
 * 2. What if we need to parse numbers in different bases (e.g., hexadecimal)?
 *    - We can extend the solution to handle different number bases.
 * 3. How would you handle localized number formats (e.g., commas as thousand separators)?
 *    - We would need to preprocess the string to handle locale-specific formats.
 *
 * Related Problems:
 * - Integer to Roman (https://leetcode.com/problems/integer-to-roman/)
 * - Valid Number (https://leetcode.com/problems/valid-number/)
 */
public class StringToIntegerAtoi {

    /**
     * Converts a string to a 32-bit signed integer.
     *
     * @param s The input string
     * @return The converted 32-bit signed integer
     */
    public int myAtoi(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int index = 0;
        int n = s.length();
        int sign = 1;
        int result = 0;

        // 1. Skip leading whitespace
        while (index < n && Character.isWhitespace(s.charAt(index))) {
            index++;
        }

        if (index == n) {
            return 0; // String contains only whitespace
        }

        // 2. Handle optional sign
        if (s.charAt(index) == '+' || s.charAt(index) == '-') {
            sign = (s.charAt(index) == '-') ? -1 : 1;
            index++;
        }

        // 3. Process digits
        while (index < n && Character.isDigit(s.charAt(index))) {
            int digit = s.charAt(index) - '0';

            // Check for overflow before actually adding the digit
            if (result > Integer.MAX_VALUE / 10 ||
                (result == Integer.MAX_VALUE / 10 && digit > Integer.MAX_VALUE % 10)) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }

            result = result * 10 + digit;
            index++;
        }

        return result * sign;
    }

    /**
     * Alternative approach using Java's built-in parsing with error handling.
     * Note: This is not the expected solution for LeetCode but demonstrates an alternative.
     */
    public int myAtoiWithExceptionHandling(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }

        s = s.trim();
        int sign = 1;
        int start = 0;

        // Handle sign
        if (s.charAt(0) == '+' || s.charAt(0) == '-') {
            sign = (s.charAt(0) == '-') ? -1 : 1;
            start++;
        }

        // Process digits
        long result = 0;
        for (int i = start; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                break;
            }

            result = result * 10 + (s.charAt(i) - '0');

            // Check for overflow
            if (sign * result > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            if (sign * result < Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }
        }

        return (int) (sign * result);
    }

    /**
     * Converts a string to a long integer with a specified radix.
     *
     * @param s The input string
     * @param radix The radix (e.g., 10 for decimal, 16 for hexadecimal)
     * @return The converted long integer
     */
    public long stringToLong(String s, int radix) {
        if (s == null || s.isEmpty() || radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
            return 0L;
        }

        int index = 0;
        int n = s.length();
        int sign = 1;
        long result = 0;

        // Skip leading whitespace
        while (index < n && Character.isWhitespace(s.charAt(index))) {
            index++;
        }

        if (index == n) {
            return 0L;
        }

        // Handle optional sign
        if (s.charAt(index) == '+' || s.charAt(index) == '-') {
            sign = (s.charAt(index) == '-') ? -1 : 1;
            index++;
        }

        // Process digits
        while (index < n) {
            int digit = Character.digit(s.charAt(index), radix);
            if (digit < 0) {
                break; // Invalid character for the given radix
            }

            // Check for overflow
            if (result > (Long.MAX_VALUE - digit) / radix) {
                return (sign == 1) ? Long.MAX_VALUE : Long.MIN_VALUE;
            }

            result = result * radix + digit;
            index++;
        }

        return result * sign;
    }

    /**
     * Validates if a string represents a valid number.
     * This is the solution to "Valid Number" (LeetCode #65).
     */
    public boolean isNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        boolean numberSeen = false;
        boolean pointSeen = false;
        boolean eSeen = false;
        boolean numberAfterE = true;

        s = s.trim();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c >= '0' && c <= '9') {
                numberSeen = true;
                numberAfterE = true;
            } else if (c == '.') {
                if (pointSeen || eSeen) {
                    return false;
                }
                pointSeen = true;
            } else if (c == 'e' || c == 'E') {
                if (eSeen || !numberSeen) {
                    return false;
                }
                eSeen = true;
                numberAfterE = false;
            } else if (c == '+' || c == '-') {
                if (i != 0 && s.charAt(i - 1) != 'e' && s.charAt(i - 1) != 'E') {
                    return false;
                }
            } else {
                return false;
            }
        }

        return numberSeen && numberAfterE;
    }

    /**
     * Converts an integer to a Roman numeral.
     * This is the solution to "Integer to Roman" (LeetCode #12).
     */
    public String intToRoman(int num) {
        if (num < 1 || num > 3999) {
            return "";
        }

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                result.append(symbols[i]);
            }
        }

        return result.toString();
    }

    /**
     * Converts a Roman numeral to an integer.
     * This is the solution to "Roman to Integer" (LeetCode #13).
     */
    public int romanToInt(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> romanToInt = new HashMap<>();
        romanToInt.put('I', 1);
        romanToInt.put('V', 5);
        romanToInt.put('X', 10);
        romanToInt.put('L', 50);
        romanToInt.put('C', 100);
        romanToInt.put('D', 500);
        romanToInt.put('M', 1000);

        int result = 0;
        int prevValue = 0;

        // Process the string from right to left
        for (int i = s.length() - 1; i >= 0; i--) {
            int currentValue = romanToInt.get(s.charAt(i));

            // If current value is less than the previous value, subtract it
            // Otherwise, add it
            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }

            prevValue = currentValue;
        }

        return result;
    }
}

package strings;

import java.util.*;

/**
 * Problem: String to Integer (atoi)
 *
 * Convert a string to a 32-bit signed integer by parsing the numeric prefix and
 * clamping overflow. This class also contains related number parsing helpers.
 *
 * Leetcode: https://leetcode.com/problems/string-to-integer-atoi/ (Medium)
 * Rating:   acceptance 21.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  String | Parser state | Overflow-safe accumulation
 *
 * Example:
 *   Input:  s = "4193 with words"
 *   Output: 4193
 *   Why:    parsing stops at the first space after the digits.
 *
 * Follow-ups:
 *   1. Arbitrary radix? Use Character.digit and radix-specific overflow checks.
 *   2. Full numeric validation? Track sign, dot, exponent, and digit states.
 *   3. Localized formats? Normalize locale-specific separators before parsing.
 *
 * Related: Valid Number (65), Integer to Roman (12), Roman to Integer (13).
 */
public class StringToIntegerAtoi {

    public static void main(String[] args) {
        StringToIntegerAtoi solver = new StringToIntegerAtoi();
        String[] inputs = {"42", "   -42", "4193 with words", "words and 987", "-91283472332"};
        int[] expected = {42, -42, 4193, 0, Integer.MIN_VALUE};
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.myAtoi(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: parse the numeric prefix only. Keep the magnitude positive and
     * check the int boundary before appending the next digit, so overflow is
     * detected before it happens.
     *
     * Algorithm:
     *   1. Return 0 for null or empty input, then skip whitespace.
     *   2. Read one optional sign.
     *   3. Consume digits while checking overflow.
     *   4. Return the signed result or the correct clamp.
     *
     * Time:  O(n) - at most one prefix of the string is scanned.
     * Space: O(1) - only parser state is stored.
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

package String;

/**
 * Implement atoi (String to Integer conversion).
 *
 * This function converts a given string representation of an integer into an actual integer.
 * It follows the same behavior as `atoi()` in C, handling:
 * - Leading/trailing spaces
 * - Optional '+' or '-' sign
 * - Integer overflow/underflow
 *
 * Time Complexity: O(N), where N = length of input string
 * Space Complexity: O(1), since we use only a few extra variables
 *
 * Problem Link: https://leetcode.com/problems/string-to-integer-atoi/
 */
public class AtoI {
    public static void main(String[] args) {
        System.out.println(aToI("42"));           // Output: 42
        System.out.println(aToI("   -42"));       // Output: -42
        System.out.println(aToI("4193 with words")); // Output: 4193
        System.out.println(aToI("words and 987")); // Output: 0
        System.out.println(aToI("-91283472332")); // Output: Integer.MIN_VALUE
    }

    public static int aToI(String input) {
        if (input == null || input.isEmpty()) return 0;

        int index = 0, length = input.length();
        int sign = 1, result = 0;

        // Step 1: Ignore leading whitespaces
        while (index < length && input.charAt(index) == ' ') {
            index++;
        }

        // Step 2: Check optional '+' or '-' sign
        if (index < length && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
            sign = (input.charAt(index) == '-') ? -1 : 1;
            index++;
        }

        // Step 3: Process numeric digits and prevent overflow
        while (index < length && Character.isDigit(input.charAt(index))) {
            int digitValue = input.charAt(index) - '0';

            // Check for integer overflow before adding the digit
            if (result > (Integer.MAX_VALUE - digitValue) / 10) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }

            result = result * 10 + digitValue;
            index++;
        }

        return result * sign;
    }
}

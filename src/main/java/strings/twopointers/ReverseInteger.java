package strings.twopointers;

/**
 * LeetCode 7. Reverse Integer
 *
 * Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes
 * the value to go outside the signed 32-bit integer range [-2^31, 2^31 - 1], then return 0.
 *
 * Example 1:
 * Input: x = 123
 * Output: 321
 *
 * Example 2:
 * Input: x = -123
 * Output: -321
 *
 * Example 3:
 * Input: x = 120
 * Output: 21
 *
 * LeetCode Link: https://leetcode.com/problems/reverse-integer/
 *
 * Follow-up Questions:
 * - How would you detect overflow without using long? (Check before multiplication and addition)
 * - Can you optimize for numbers with many trailing zeros? (Current approach naturally handles this)
 * - How would you extend to 64-bit integers? (Use BigInteger or similar overflow detection)
 * - What if we need to preserve leading zeros in output? (Return as string instead of integer)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ReverseInteger {

    /**
     * Reverses digits of integer with overflow detection.
     *
     * Algorithm:
     * 1. Handle sign separately by working with absolute values conceptually
     * 2. Extract digits from right to left using modulo and division
     * 3. Build reversed number by multiplying result by 10 and adding digit
     * 4. Check for overflow before each operation to prevent integer overflow
     * 5. Return 0 if overflow would occur, otherwise return reversed number
     *
     * Time Complexity: O(log n) where n is the input number (number of digits)
     * Space Complexity: O(1) - only uses constant extra space
     *
     * @param input Input integer to reverse
     * @return Reversed integer, or 0 if overflow would occur
     */
    public int reverse(int input) {
        int result = 0;

        while (input != 0) {
            int lastDigit = input % 10;
            input /= 10;

            // Check for overflow before updating result
            // Integer.MAX_VALUE = 2147483647
            // So if current result > 214748364 or (result == 214748364 and lastDigit > 7) then overflow
            if (result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && lastDigit > 7)) {
                return 0; // Positive overflow
            }

            // Integer.MIN_VALUE = -2147483648
            // So if current result < -214748364 or (result == -214748364 and lastDigit < -8) then overflow
            if (result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && lastDigit < -8)) {
                return 0; // Negative overflow
            }

            result = result * 10 + lastDigit;
        }

        return result;
    }

    /**
     * Alternative approach using long for overflow detection.
     * Note: This is less efficient due to use of long, but simpler logic.
     * Not recommended for interviews
     */
    public int reverseWithLong(int x) {
        long result = 0;

        while (x != 0) {
            result = result * 10 + x % 10;
            x /= 10;
        }

        // Check if result fits in 32-bit signed integer range
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            return 0;
        }

        return (int) result;
    }

    /**
     * String-based approach for comparison (less efficient but clear logic).
     */
    public int reverseString(int x) {
        boolean isNegative = x < 0;
        String str = Integer.toString(Math.abs(x));

        StringBuilder reversed = new StringBuilder(str).reverse();

        try {
            long result = Long.parseLong(reversed.toString());
            if (isNegative) {
                result = -result;
            }

            if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
                return 0;
            }

            return (int) result;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

package strings.twopointers;

/**
 * Problem: Reverse Integer
 *
 * Given a signed 32-bit integer, reverse its decimal digits. If the reversed
 * value would fall outside the signed 32-bit range, return 0 instead.
 *
 * Leetcode: https://leetcode.com/problems/reverse-integer/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Math | Digit extraction | Overflow guard
 *
 * Example:
 *   Input:  x = -123
 *   Output: -321
 *   Why:    digits reverse from 123 to 321 and the sign is preserved by arithmetic.
 *
 * Follow-ups:
 *   1. How do you detect overflow without using long?
 *      Check against Integer.MAX_VALUE / 10 and Integer.MIN_VALUE / 10 before appending.
 *   2. What if leading zeros must be preserved?
 *      Return a string representation instead of an integer.
 *   3. How would this extend to 64-bit integers?
 *      Use long bounds or arbitrary-precision arithmetic with equivalent checks.
 */
public class ReverseInteger {

    public static void main(String[] args) {
        ReverseInteger solver = new ReverseInteger();

        int[] inputs = {123, -123, 120, 1534236469};
        int[] expected = {321, -321, 21, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.reverse(inputs[i]);
            System.out.printf("x=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: repeatedly peel the last digit from input and append it to the
     * reversed result. Before multiplying by 10 and adding the digit, compare the
     * current result with the last safe boundary to avoid overflow.
     *
     * Algorithm:
     *   1. Start result at 0.
     *   2. While input is nonzero, pop its last digit with modulo 10.
     *   3. Check whether appending that digit would overflow int bounds.
     *   4. Append the digit and continue with input divided by 10.
     *
     * Time:  O(log n) - one iteration handles one decimal digit.
     * Space: O(1) - only the running result and current digit are stored.
     *
     * @param input Integer whose digits should be reversed.
     * @return Reversed integer, or 0 if reversing would overflow.
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

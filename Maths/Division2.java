package maths;

/**
 * Problem: Divide two integers without using multiplication, division, or modulus.
 * 
 * Given two integers (dividend and divisor), return the quotient after division.
 * If the quotient overflows the 32-bit signed integer range, return Integer.MAX_VALUE.
 * 
 * Approach:
 * - Use **bitwise shifting** and **subtraction** to simulate division.
 * - Convert both numbers to negative to avoid overflow issues.
 * - Use **exponential search (doubling method)** to speed up subtraction.
 * 
 * Time Complexity: O(log N) - The dividend is reduced exponentially.
 * Space Complexity: O(1) - Only a few variables are used.
 * 
 * LeetCode Link: https://leetcode.com/problems/divide-two-integers/
 */
public class Division2 {
    private static final int HALF_INT_MIN = Integer.MIN_VALUE / 2; // To prevent overflow

    public static void main(String[] args) {
        Division2 division = new Division2();
        System.out.println(division.divide(10, 3));         // Expected output: 3
        System.out.println(division.divide(7, -3));        // Expected output: -2
        System.out.println(division.divide(Integer.MIN_VALUE, -1)); // Expected output: Integer.MAX_VALUE
    }

    /**
     * Performs integer division using bitwise shifting and subtraction.
     *
     * @param dividend the numerator
     * @param divisor the denominator
     * @return the quotient after division
     */
    public int divide(int dividend, int divisor) {
        // Edge Case: Integer overflow scenario
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }

        // Determine sign of quotient: (+) or (-)
        int sign = (dividend > 0 ? 1 : -1) * (divisor > 0 ? 1 : -1);

        // Convert both numbers to negative to handle overflow safely
        dividend = dividend > 0 ? -dividend : dividend;
        divisor = divisor > 0 ? -divisor : divisor;

        int quotient = 0;

        // Perform bitwise division using exponential search
        while (dividend <= divisor) {
            int powerOfTwo = 1;
            int value = divisor;

            // Double `value` until it's smaller than `dividend`
            while (value >= HALF_INT_MIN && value + value >= dividend) {
                value += value;
                powerOfTwo += powerOfTwo;
            }

            // Reduce the dividend and accumulate quotient
            dividend -= value;
            quotient += powerOfTwo;
        }

        return sign * quotient;
    }
}

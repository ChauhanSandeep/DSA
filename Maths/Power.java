package maths;

/**
 * Implementation of a function to calculate power (x^n) using fast exponentiation.
 *
 * Algorithm:
 * - Uses iterative exponentiation by squaring for efficiency.
 * - Handles negative exponents by taking the reciprocal.
 * - Uses a long variable to prevent integer overflow when handling Integer.MIN_VALUE.
 *
 * Edge Cases Considered:
 * - n == 0 (Any number to the power of 0 is 1)
 * - n < 0 (Handles negative exponents correctly)
 * - x == 0 (0 raised to any positive exponent is 0)
 * - Large exponents (Uses O(log n) time complexity)
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 *
 * LeetCode Reference: https://leetcode.com/problems/powx-n/
 */
public class Power {
    public static void main(String[] args) {
        Power powerCalculator = new Power();

        // Test cases
        System.out.println("2^10: " + powerCalculator.myPow(2, 10)); // Expected: 1024
        System.out.println("2^-3: " + powerCalculator.myPow(2, -3)); // Expected: 0.125
        System.out.println("2^0: " + powerCalculator.myPow(2, 0)); // Expected: 1
        System.out.println("0^5: " + powerCalculator.myPow(0, 5)); // Expected: 0
        System.out.println("2^Integer.MIN_VALUE: " + powerCalculator.myPow(2, Integer.MIN_VALUE)); // Handle large exponent
    }

    /**
     * Computes x raised to the power of n (x^n) using iterative exponentiation by squaring.
     *
     * @param x The base number.
     * @param n The exponent.
     * @return The computed power x^n.
     */
    public double myPow(double x, int n) {
        if (x == 0) return 0; // Special case: 0 raised to any positive power is 0
        if (n == 0) return 1; // Base case: any number raised to 0 is 1

        long exponent = n; // Convert to long to handle Integer.MIN_VALUE safely
        if (exponent < 0) {
            x = 1 / x;
            exponent = -exponent;
        }

        double result = 1.0;
        double multiplier = x;

        while (exponent > 0) {
            if ((exponent & 1) == 1) { // Check if exponent is odd
                result *= multiplier;
            }
            multiplier *= multiplier; // Square the base
            exponent >>= 1; // Divide exponent by 2 using bit shifting
        }

        return result;
    }
}

package maths;

/**
 * Implementation of a function to calculate power (x^n) using fast exponentiation.
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
     * Algorithm:
     * - Use the method of exponentiation by squaring to compute the power.
     * - If the exponent is even, square the base and halve the exponent.
     * - If the exponent is odd, multiply the result by the base and reduce the exponent by 1.
     * - Continue until the exponent becomes 0.
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
                // if the exponent is odd, multiply the result by the current power
                result *= multiplier;
            }
            // If exponent is even, we can square the base
            multiplier *= multiplier; // Square the base
            exponent >>= 1; // Divide exponent by 2 using bit shifting
        }

        return result;
    }

    /**
     * Computes x raised to the power of n (x^n) using recursive exponentiation by squaring.
     * Algorithm:
     * - If n is 0, return 1.
     * - If n is negative, compute the power of the reciprocal.
     * - If n is even, compute the power of half the exponent and square it.
     * - If n is odd, compute the power of half the exponent, square it, and multiply by x.
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(log n) due to recursion stack.
     *
     * @param x The base number.
     * @param n The exponent.
     * @return The computed power x^n.
     */
    public double myPowRecursive(double x, int n) {
        if (x == 0) return 0; // Special case
        if (n == 0) return 1; // Base case

        long exponent = n; // Use long to handle Integer.MIN_VALUE safely
        if (exponent < 0) {
            x = 1 / x;
            exponent = -exponent;
        }
        return power(x, exponent);
    }

    private double power(double x, long n) {
        if (n == 0) return 1; // Base case
        double half = power(x, n / 2);

        if (n % 2 == 0) {
            return half * half; // Even exponent
        } else {
            return half * half * x; // Odd exponent
        }
    }
}

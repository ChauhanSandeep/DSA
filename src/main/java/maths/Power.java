package maths;

/**
 * Implementation of a function to calculate power (x^n) using fast exponentiation.
 * LeetCode Reference: https://leetcode.com/problems/powx-n/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class Power {
    public static void main(String[] args) {
        Power powerCalculator = new Power();

        // Test cases
        System.out.println("2^10: " + powerCalculator.myPowRecursive(2, 10)); // Expected: 1024
        System.out.println("2^-3: " + powerCalculator.myPowRecursive(2, -3)); // Expected: 0.125
        System.out.println("2^0: " + powerCalculator.myPowRecursive(2, 0)); // Expected: 1
        System.out.println("0^5: " + powerCalculator.myPowRecursive(0, 5)); // Expected: 0
        System.out.println("2^Integer.MIN_VALUE: " + powerCalculator.myPowRecursive(2, Integer.MIN_VALUE)); // Handle large exponent
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

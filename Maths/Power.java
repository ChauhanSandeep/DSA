package maths;

/**
 * Implementation of a function to calculate power (x^n) using fast exponentiation.
 *
 * Algorithm:
 * - Uses iterative exponentiation by squaring for efficiency.
 * - Handles negative exponents by taking the reciprocal.
 * - Uses a long variable to prevent integer overflow.
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 *
 * LeetCode Problem: https://leetcode.com/problems/powx-n/
 */
public class Power {

    public static void main(String[] args) {
        Power power = new Power();
        double result = power.myPow(2, Integer.MIN_VALUE);
        System.out.println("Result: " + result);
    }

    /**
     * Computes x raised to the power of n (x^n) using iterative exponentiation by squaring.
     *
     * @param x the base number.
     * @param n the exponent.
     * @return the computed power x^n.
     */
    public double myPow(double x, int n) {
        long exponent = n;
        if (exponent == 0) return 1;
        if (exponent < 0) {
            x = 1 / x;
            exponent = -exponent;
        }

        double result = 1;
        double currentMultiplier = x;

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result *= currentMultiplier;
            }
            currentMultiplier *= currentMultiplier;
            exponent /= 2;
        }
        return result;
    }
}

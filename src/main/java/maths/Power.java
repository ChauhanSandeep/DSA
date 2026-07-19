package maths;

/**
 * Problem: Pow(x, n)
 *
 * Given a base x and integer exponent n, compute x raised to n. Negative
 * exponents are handled by inverting the base, and exponentiation by squaring
 * avoids multiplying x one step at a time.
 *
 * Leetcode: https://leetcode.com/problems/powx-n/ (Medium)
 * Rating:   acceptance 39.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Fast exponentiation | Divide and conquer
 *
 * Example:
 *   Input:  x = 2.0, n = -3
 *   Output: 0.125
 *   Why:    2^-3 is 1 / (2^3), which equals 1 / 8.
 *
 * Follow-ups:
 *   1. How would you implement this iteratively?
 *      Scan exponent bits and multiply the answer whenever the current bit is set.
 *   2. How would you compute power under a modulo?
 *      Apply modulo after each multiplication while using the same squaring idea.
 *   3. Why promote n to long?
 *      Negating Integer.MIN_VALUE overflows as an int, but it is safe as a long.
 *
 * Related: Super Pow (372), Sqrt(x) (69).
 */

public class Power {
    public static void main(String[] args) {
        Power solver = new Power();
        double[] bases = { 2.0, 2.0, 0.0 };
        int[] exponents = { 10, -3, 5 };
        double[] expected = { 1024.0, 0.125, 0.0 };

        for (int i = 0; i < bases.length; i++) {
            double got = solver.myPowRecursive(bases[i], exponents[i]);
            System.out.printf("x=%.1f n=%d -> %.3f  expected=%.3f%n",
                bases[i], exponents[i], got, expected[i]);
        }
    }

        /**
     * Intuition: x^n can be split in half because x^n = (x^(n / 2))^2, with one
     * extra x when n is odd. That reduces the exponent by half at each recursive
     * level. Negative exponents become positive after replacing x with 1 / x.
     *
     * Algorithm:
     *   1. Return 0 for base 0 and 1 for exponent 0.
     *   2. Promote n to long so Integer.MIN_VALUE can be negated safely.
     *   3. For negative exponents, invert x and negate exponent.
     *   4. Recursively compute power(x, exponent) by squaring the half result.
     *
     * Time:  O(log n) - each recursive call halves the exponent.
     * Space: O(log n) - recursion depth follows the exponent bit length.
     *
     * @param x base value
     * @param n integer exponent
     * @return x raised to n
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

        /** Recursively computes a non-negative exponent by squaring. */
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

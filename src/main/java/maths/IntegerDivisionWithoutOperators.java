package maths;

/**
 * Problem: Divide Two Integers
 *
 * Given two integers dividend and divisor, divide two integers without using
 * multiplication, division, and mod operator.
 *
 * The integer division should truncate toward zero, which means losing its fractional part.
 * For example, 8.345 would be truncated to 8, and -2.7335 would be truncated to -2.
 *
 * Return the quotient after dividing dividend by divisor.
 *
 * Note: Assume we are dealing with an environment that could only store integers
 * within the 32-bit signed integer range: [−2^31, 2^31 − 1]. For this problem,
 * if the quotient is strictly greater than 2^31 - 1, then return 2^31 - 1,
 * and if the quotient is strictly smaller than -2^31, then return -2^31.
 *
 * Example 1:
 * Input: dividend = 10, divisor = 3
 * Output: 3
 * Explanation: 10/3 = 3.33333.. which is truncated to 3.
 *
 * Example 2:
 * Input: dividend = 7, divisor = -3
 * Output: -2
 * Explanation: 7/-3 = -2.33333.. which is truncated to -2.
 *
 * LeetCode: https://leetcode.com/problems/divide-two-integers/
 *
 * Follow-up Questions (FAANG-style):
 * 1. How would you handle division with floating point precision?
 *    - Use binary search on the result with epsilon-based precision checks.
 * 2. What if we need to handle arbitrarily large numbers?
 *    - Implement using string-based arithmetic or BigInteger approach.
 * 3. How would you optimize for multiple divisions with same divisor?
 *    - Precompute powers of divisor and reuse across multiple operations.
 * 4. What if the divisor can be zero?
 *    - Handle division by zero case explicitly and throw appropriate exception.
 * 5. How to implement modulus operation without using % operator?
 *    - Use: a % b = a - (a/b) * b, implementing multiplication via repeated addition.
 * 6. What if you need to support negative number representations in different systems?
 *    - Handle two's complement, sign-magnitude, and one's complement representations.
 *    - Related: https://leetcode.com/problems/multiply-strings/
 */
public class IntegerDivisionWithoutOperators {

    private static final int INTEGER_MAX = Integer.MAX_VALUE;
    private static final int INTEGER_MIN = Integer.MIN_VALUE;

    public static void main(String[] args) {
        IntegerDivisionWithoutOperators calculator = new IntegerDivisionWithoutOperators();

        // Test basic cases
        System.out.println("10 / 3 = " + calculator.divide(10, 3));         // Expected: 3
        System.out.println("7 / -3 = " + calculator.divide(7, -3));        // Expected: -2
        System.out.println("-7 / 3 = " + calculator.divide(-7, 3));        // Expected: -2
        System.out.println("-7 / -3 = " + calculator.divide(-7, -3));      // Expected: 2

        // Test edge cases
        System.out.println("MIN_VALUE / -1 = " + calculator.divide(INTEGER_MIN, -1)); // Expected: MAX_VALUE
        System.out.println("MAX_VALUE / 1 = " + calculator.divide(INTEGER_MAX, 1));   // Expected: MAX_VALUE
        System.out.println("1 / 2 = " + calculator.divide(1, 2));          // Expected: 0
        System.out.println("0 / 5 = " + calculator.divide(0, 5));          // Expected: 0
        System.out.println("12 / 12 = " + calculator.divide(12, 12));      // Expected: 1
    }

    /**
     * Performs integer division without using multiplication, division, or modulus operators.
     *
     * Algorithm: Bit Manipulation with Power-of-2 Optimization
     * Steps:
     * 1. Handle edge case where dividend equals divisor (result is 1)
     * 2. Determine result sign using XOR logic on sign bits
     * 3. Convert to unsigned values to avoid overflow during operations
     * 4. Use bit shifting to find largest power of 2 multiples
     * 5. Accumulate quotient and reduce dividend iteratively
     * 6. Handle overflow case and apply final sign
     *
     * Mathematical Insight:
     * - Division finds how many times divisor fits into dividend
     * - Optimization: find largest 2^k such that divisor * 2^k <= dividend
     * - Subtract (divisor * 2^k) and repeat with remainder
     * - Use bit shifting for efficient power-of-2 multiplication
     *
     * Time Complexity: O(log^2 n) where n is the quotient
     * Space Complexity: O(1) - constant extra space
     *
     * @param dividend the number to be divided
     * @param divisor the number to divide by
     * @return quotient of dividend/divisor, clamped to 32-bit integer range
     */
    public int divide(int dividend, int divisor) {
        // Quick check: if dividend equals divisor, result is always 1
        if (dividend == divisor) {
            return 1;
        }

        // Determine if result should be positive or negative
        // XOR of sign bits: same signs -> positive, different signs -> negative
        boolean isResultPositive = (dividend > 0) ^ (divisor > 0);

        // Convert to unsigned long to handle absolute values safely
        // This prevents overflow when dealing with Integer.MIN_VALUE
        long numerator = Math.abs((long) dividend);
        long denominator = Math.abs((long) divisor);

        long quotient = 0;

        // Continue division while dividend is larger than or equal to divisor
        while (numerator >= denominator) {
            // Find the largest power of 2 such that divisor * 2^powerOfTwo <= dividend
            int powerOfTwo = findLargestPowerOfTwo(numerator, denominator);

            // Add the power of 2 to our quotient
          quotient = quotient + (1L << powerOfTwo);

            // Reduce dividend by divisor * 2^powerOfTwo
            numerator -= (denominator << powerOfTwo);
        }

        // Handle overflow case: if result exceeds Integer.MAX_VALUE and should be positive
        if (quotient == (1L << 31) && isResultPositive) {
            return INTEGER_MAX;
        }

        // Apply sign to final result
        return isResultPositive ? (int) quotient : -(int) quotient;
    }

    /**
     * Finds the largest power of 2 such that divisor * 2^power <= dividend.
     *
     * Algorithm: Binary Search for Optimal Power
     * Steps:
     * 1. Start with power = 0
     * 2. Keep incrementing power while divisor * 2^(power+1) <= dividend
     * 3. Use bit shifting to efficiently check: divisor << (power+1) <= dividend
     * 4. Return the largest valid power found
     *
     * This optimization reduces repeated subtraction by finding the largest
     * chunk we can subtract in each iteration.
     *
     * Time Complexity: O(log n) where n is dividend/divisor ratio
     * Space Complexity: O(1)
     *
     * @param dividend current remaining dividend
     * @param divisor the divisor value
     * @return largest power of 2 for optimal subtraction
     */
    private int findLargestPowerOfTwo(long dividend, long divisor) {
        int power = 0;

        // Keep doubling the divisor while it fits within the dividend
        // Use (power + 1) to check the next power level
        while (dividend >= (divisor << (power + 1))) {
            power++;
        }

        return power;
    }

    /**
     * Alternative approach using iterative bit manipulation for educational purposes.
     *
     * Algorithm: Direct Bit-by-Bit Division
     * Steps:
     * 1. Process quotient from most significant bit to least significant bit
     * 2. For each bit position, check if divisor fits into current remainder
     * 3. Set quotient bit if divisor fits, subtract divisor from remainder
     * 4. Continue until all bits are processed
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     *
     * @param dividend the number to be divided
     * @param divisor the number to divide by
     * @return quotient using bit-by-bit approach
     */
    public int divideUsingBitManipulation(int dividend, int divisor) {
        if (dividend == divisor) {
            return 1;
        }

        boolean isResultPositive = (dividend < 0) == (divisor < 0);
        long absDividend = Math.abs((long) dividend);
        long absDivisor = Math.abs((long) divisor);

        long quotient = 0;

        // Process each bit position from most significant to least significant
        for (int bitPosition = 31; bitPosition >= 0; bitPosition--) {
            // Check if divisor shifted by bitPosition fits into remaining dividend
            if ((absDivisor << bitPosition) <= absDividend) {
                absDividend -= (absDivisor << bitPosition);
                quotient |= (1L << bitPosition); // Set the bit at current position
            }
        }

        // Handle overflow and apply sign
        if (quotient == (1L << 31) && isResultPositive) {
            return INTEGER_MAX;
        }

        return isResultPositive ? (int) quotient : -(int) quotient;
    }
}

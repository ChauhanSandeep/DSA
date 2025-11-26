package maths;

/**
 * Problem: Divide Two Integers
 *
 * Given two integers dividend and divisor, divide two integers without using multiplication,
 * division, and mod operator. The integer division should truncate toward zero, which means
 * losing its fractional part. For example, 8.345 would be truncated to 8, and -2.7335 would
 * be truncated to -2.
 *
 * Return the quotient after dividing dividend by divisor.
 *
 * Note: Assume we are dealing with an environment that could only store integers within the
 * 32-bit signed integer range: [−2^31, 2^31 − 1]. For this problem, if the quotient is strictly
 * greater than 2^31 - 1, then return 2^31 - 1, and if the quotient is strictly less than -2^31,
 * then return -2^31.
 *
 * Example 1:
 * Input: dividend = 10, divisor = 3
 * Output: 3
 * Explanation: 10/3 = 3.33333.. which truncates to 3.
 * Step-by-step using bit manipulation:
 * - 3 * 2^0 = 3 ≤ 10, subtract: 10 - 3 = 7, quotient += 1
 * - 3 * 2^1 = 6 ≤ 7, subtract: 7 - 6 = 1, quotient += 2
 * - 3 * 2^0 = 3 > 1, cannot subtract
 * - Final quotient: 1 + 2 = 3
 *
 * LeetCode Problem: https://leetcode.com/problems/divide-two-integers/
 *
 * Follow-up Questions:
 *
 * 1. What if we need to return the remainder as well?
 *    Answer: Track the remaining dividend after all subtractions. The final value of
 *    dividend after the algorithm completes is the remainder. Return both quotient and
 *    remainder as a pair or array.
 *
 * 2. How would you handle division with decimal precision (not just truncation)?
 *    Answer: After finding the integer quotient, continue the division algorithm with
 *    the remainder scaled by 10 repeatedly to get decimal places. Use long to avoid
 *    overflow during scaling. Track desired precision level.
 *
 * 3. Can we optimize for specific divisor values (powers of 2)?
 *    Answer: Yes! If divisor is a power of 2, division can be done with a single right
 *    shift operation. For example, dividing by 4 (2^2) is equivalent to right shift by 2.
 *    Check if divisor is power of 2: (divisor & (divisor - 1)) == 0
 *
 * 4. What if we're allowed to use multiplication but not division/mod?
 *    Answer: Use binary search approach. Search for quotient q such that q * divisor <= dividend
 *    but (q+1) * divisor > dividend. Time complexity would be O(log(dividend) * log(quotient)).
 *    Related: https://leetcode.com/problems/divide-chocolate/
 *
 * 5. How would you handle very large numbers beyond 64-bit integers?
 *    Answer: Implement division using strings or BigInteger-like structures. Process digit
 *    by digit similar to long division taught in elementary school. This becomes a string
 *    manipulation problem with carry handling.
 */
public class IntegerDivisionWithoutOperators {

    /**
     * Simple version using only subtraction (very slow but easiest to understand).
     * Counts how many times we can subtract divisor from dividend.
     *
     * Algorithm:
     * 1. Keep subtracting divisor from dividend
     * 2. Count each subtraction
     * 3. The count is the quotient
     *
     * Time Complexity: O(dividend/divisor) - can be very slow
     * Space Complexity: O(1)
     *
     * WARNING: This is too slow for large inputs but shows the basic concept
     * without any optimization or bit manipulation.
     *
     * @param dividend the number to be divided
     * @param divisor the number to divide by
     * @return the quotient after division
     */
    public int divideSimple(int dividend, int divisor) {
        // Edge case: Overflow
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        
        // Determine sign
        boolean isNegative = (dividend < 0) != (divisor < 0);
        
        // Convert to positive
        long absDividend = Math.abs((long) dividend);
        long absDivisor = Math.abs((long) divisor);
        
        long quotient = 0;
        
        // Simple repeated subtraction - count how many times we can subtract
        while (absDividend >= absDivisor) {
            absDividend = absDividend - absDivisor; // Subtract divisor
            quotient = quotient + 1;                 // Increment count
        }
        
        // Apply sign
        return isNegative ? (int) -quotient : (int) quotient;
    }

     /**
     *
     * Optimized division using addition to double the divisor.
     * 
     * Algorithm:
     * - Use addition to double the divisor repeatedly until it exceeds the dividend.
     * - Keep track of how many times we've added the divisor (this forms the quotient).
     * - When we can no longer add the doubled divisor, reset to the original divisor and
     *  continue the process until the dividend is less than the divisor.
     *
     * Time Complexity: O(log²(dividend))
     * Space Complexity: O(1) - no lists needed
     *
     * @param dividend the number to be divided
     * @param divisor the number to divide by
     * @return the quotient after division
     */
    public int divideOptimized(int dividend, int divisor) {
        // Edge case: Overflow
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        
        // Determine sign
        boolean isNegative = (dividend < 0) != (divisor < 0);
        
        // Convert to positive
        long absDividend = Math.abs((long) dividend);
        long absDivisor = Math.abs((long) divisor);
        
        long quotient = 0;
        
        while (absDividend >= absDivisor) {
            // Find the largest multiple of divisor that fits in current dividend
            long tempDivisor = absDivisor;
            long tempQuotient = 1;
            
            // Keep doubling using addition until next double would exceed dividend
            // Build: divisor, 2*divisor, 4*divisor, 8*divisor, ...
            // Using only addition: divisor+divisor, then result+result, etc.
            while (tempDivisor <= absDividend) {
                // Check if we can safely double
                // Need to check: tempDivisor + tempDivisor <= absDividend
                long nextDivisor = tempDivisor + tempDivisor;
                
                // If next double would exceed dividend, stop
                if (nextDivisor > absDividend) {
                    break;
                }
                
                // Double both values using addition
                tempDivisor = nextDivisor;
                tempQuotient = tempQuotient + tempQuotient;
            }
            
            // Subtract the largest multiple we found
            absDividend = absDividend - tempDivisor;
            
            // Add corresponding quotient contribution
            quotient = quotient + tempQuotient;
        }
        
        // Apply sign
        return isNegative ? (int) -quotient : (int) quotient;
    }

    /**
     * Division using bit manipulation (shifts).
     *
     * Algorithm:
     * - Use bit manipulation to double the divisor until it exceeds the dividend.
     * - Keep track of how many times we've doubled (this forms the quotient).
     * - When we can no longer double, subtract the largest found multiple from the dividend
     * and continue the process until the dividend is less than the divisor.
     * 
     *
     * Time Complexity: O(log(dividend)) - each iteration reduces dividend by at least half
     * Space Complexity: O(1)
     *
     * @param dividend the number to be divided
     * @param divisor the number to divide by
     * @return the quotient after division
     */
    public int divideUsingBitManipulation(int dividend, int divisor) {
        // Handle overflow case
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        
        // Determine sign
        boolean isNegative = (dividend < 0) ^ (divisor < 0);
        
        // Work with absolute values using long
        long numerator = Math.abs((long) dividend);
        long denominator = Math.abs((long) divisor);
        
        long quotient = 0;
        
        // Exponential search approach
        while (numerator >= denominator) {
            // Start with base divisor
            long currentDenominator = denominator;
            long currentQuotient = 1;
            
            // Keep doubling until we exceed the dividend
            // Build up: divisor, 2*divisor, 4*divisor, 8*divisor, ...
            while (numerator >= (currentDenominator << 1)) {
                currentDenominator <<= 1;  // Double the divisor
                currentQuotient <<= 1; // Double the quotient contribution
            }
            
            // Subtract the largest multiple we found
            numerator -= currentDenominator;
            quotient += currentQuotient;
        }
        
        // Apply sign
        return isNegative ? (int) -quotient : (int) quotient;
    }

}

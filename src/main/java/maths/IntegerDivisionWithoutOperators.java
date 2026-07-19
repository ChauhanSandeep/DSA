package maths;

/**
 * Problem: Divide Two Integers
 *
 * Divide dividend by divisor without using multiplication, division, or the
 * modulo operator. The quotient must truncate toward zero and clamp the single
 * overflowing case to Integer.MAX_VALUE.
 *
 * Leetcode: https://leetcode.com/problems/divide-two-integers/ (Medium)
 * Rating:   acceptance 20.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Bit manipulation | Exponential subtraction
 *
 * Example:
 *   Input:  dividend = 10, divisor = 3
 *   Output: 3
 *   Why:    3 fits into 10 three times, and integer division drops the remainder 1.
 *
 * Follow-ups:
 *   1. How would you also return the remainder?
 *      Return the leftover dividend after subtracting the chosen divisor multiples.
 *   2. How would you produce decimal digits?
 *      Continue long division by scaling the remainder by 10 each step.
 *   3. How would you optimize division by powers of two?
 *      Detect a power-of-two divisor and use shifts with the same sign handling.
 *
 * Related: Fraction to Recurring Decimal (166), Pow(x, n) (50).
 */

public class IntegerDivisionWithoutOperators {

    public static void main(String[] args) {
        IntegerDivisionWithoutOperators solver = new IntegerDivisionWithoutOperators();
        int[][] inputs = { {10, 3}, {7, -3}, {Integer.MIN_VALUE, -1}, {43, 8} };
        int[] expected = { 3, -2, Integer.MAX_VALUE, 5 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.divideUsingBitManipulation(inputs[i][0], inputs[i][1]);
            System.out.printf("dividend=%d divisor=%d -> %d  expected=%d%n",
                inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }


    /**
     * Simple version using only subtraction (very slow but easiest to understand).
     * Counts how many times we can subtract divisor from dividend.
     *
     * Algorithm:
     * 1. Keep subtracting divisor from dividend
     * 2. Count each subtraction
     * 3. The count is the quotient
     *
     * Implementation Note: Works with negative numbers to avoid Integer.MIN_VALUE
     * overflow.
     * Since Integer.MIN_VALUE cannot be converted to positive (abs would overflow),
     * we convert both to negative and work in negative space.
     *
     * Time Complexity: O(dividend/divisor) - can be very slow
     * Space Complexity: O(1)
     *
     * WARNING: This is too slow for large inputs but shows the basic concept
     * without any optimization or bit manipulation.
     *
     * @param dividend the number to be divided
     * @param divisor  the number to divide by
     * @return the quotient after division
     */
    public int divideSimple(int dividend, int divisor) {
        // Edge case: Overflow
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }

        // Determine sign of result
        boolean isNegative = (dividend < 0) != (divisor < 0);

        // Convert both to negative to avoid overflow with Integer.MIN_VALUE
        // All positive ints can be represented as negative, but not vice versa
        int negDividend = dividend > 0 ? -dividend : dividend;
        int negDivisor = divisor > 0 ? -divisor : divisor;

        int quotient = 0;

        // Simple repeated subtraction - count how many times we can subtract
        // Working with negatives: we subtract (make more negative) until we go below
        // divisor
        while (negDividend <= negDivisor) {
            negDividend = negDividend - negDivisor; // Subtract divisor (makes more negative)
            quotient = quotient + 1; // Increment count
        }

        // Apply sign
        return isNegative ? -quotient : quotient;
    }

    /**
     *
     * Optimized division using addition to double the divisor.
     * 
     * Algorithm:
     * - Use addition to double the divisor repeatedly until it exceeds the
     * dividend.
     * - Keep track of how many times we've added the divisor (this forms the
     * quotient).
     * - When we can no longer add the doubled divisor, reset to the original
     * divisor and
     * continue the process until the dividend is less than the divisor.
     *
     * Implementation Note: Works with negative numbers to avoid Integer.MIN_VALUE
     * overflow.
     * Since Integer.MIN_VALUE cannot be converted to positive (abs would overflow),
     * we convert both to negative and work in negative space.
     *
     * Time Complexity: O(log²(dividend))
     * Space Complexity: O(1) - no lists needed
     *
     * @param dividend the number to be divided
     * @param divisor  the number to divide by
     * @return the quotient after division
     */
    public int divideOptimized(int dividend, int divisor) {
        // Edge case: Overflow
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }

        // Determine sign of result
        boolean isNegative = (dividend < 0) != (divisor < 0);

        // Convert both to negative to avoid overflow with Integer.MIN_VALUE
        // All positive ints can be represented as negative, but not vice versa
        int negDividend = dividend > 0 ? -dividend : dividend;
        int negDivisor = divisor > 0 ? -divisor : divisor;

        int quotient = 0;

        // Working with negatives: more negative means smaller value
        while (negDividend <= negDivisor) {
            // Find the largest multiple of divisor that fits in current dividend
            int tempDivisor = negDivisor;
            int tempQuotient = 1;

            // Keep doubling using addition until next double would exceed dividend
            // Build: divisor, 2*divisor, 4*divisor, 8*divisor, ...
            // Using only addition: divisor+divisor, then result+result, etc.
            // Note: with negatives, we need to check if doubling makes it TOO negative
            while (tempDivisor >= (Integer.MIN_VALUE >> 1) && negDividend <= tempDivisor + tempDivisor) {
                // Double both values using addition
                tempDivisor = tempDivisor + tempDivisor;
                tempQuotient = tempQuotient + tempQuotient;
            }

            // Subtract the largest multiple we found (makes negDividend more negative)
            negDividend = negDividend - tempDivisor;

            // Add corresponding quotient contribution
            quotient = quotient + tempQuotient;
        }

        // Apply sign
        return isNegative ? -quotient : quotient;
    }

        /**
     * Intuition: repeated subtraction is too slow, so subtract the largest
     * doubled divisor that still fits. The original code works in negative
     * space because Integer.MIN_VALUE has no positive counterpart, then applies
     * the sign at the end.
     *
     * Algorithm:
     *   1. Clamp Integer.MIN_VALUE / -1 to Integer.MAX_VALUE.
     *   2. Record the result sign and convert both operands to negative values.
     *   3. While the divisor still fits, double currentDivisor and currentQuotient with shifts.
     *   4. Subtract that largest multiple and add its quotient contribution.
     *   5. Apply the recorded sign to the quotient.
     *
     * Time:  O(log^2 n) - each outer pass removes a large multiple, with inner doubling.
     * Space: O(1) - only scalar counters and current multiples are stored.
     *
     * @param dividend number to divide
     * @param divisor number to divide by
     * @return quotient truncated toward zero, clamped on overflow
     */

    public int divideUsingBitManipulation(int dividend, int divisor) {
        // Handle overflow case
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }

        // Determine sign of result
        boolean isNegative = (dividend < 0) ^ (divisor < 0);

        // Convert both to negative to avoid overflow with Integer.MIN_VALUE
        // All positive ints can be represented as negative, but not vice versa
        int negDividend = dividend > 0 ? -dividend : dividend;
        int negDivisor = divisor > 0 ? -divisor : divisor;

        int quotient = 0;

        // Exponential search approach - working with negatives
        while (negDividend <= negDivisor) {
            // Start with base divisor
            int currentDivisor = negDivisor;
            int currentQuotient = 1;

            // Keep doubling until we exceed the dividend
            // Build up: divisor, 2*divisor, 4*divisor, 8*divisor, ...
            // With negatives: more negative means smaller, so we check if doubling is still
            // >= dividend
            // Also check for overflow: ensure we don't go below Integer.MIN_VALUE/2
            while (currentDivisor >= (Integer.MIN_VALUE >> 1) && negDividend <= (currentDivisor << 1)) {
                currentDivisor <<= 1; // Double the divisor (makes more negative)
                currentQuotient <<= 1; // Double the quotient contribution
            }

            // Subtract the largest multiple we found (makes negDividend more negative)
            negDividend -= currentDivisor;
            quotient += currentQuotient;
        }

        // Apply sign
        return isNegative ? -quotient : quotient;
    }

}

package Maths;

/**
 * Divide two integers without using multiplication, division, and mod operator.
 * Note: Assume we are dealing with an environment that could only store integers within the 32-bit signed integer
 * range: [−231, 231 − 1]. For this problem, assume that your function returns 231 − 1 when the division result overflows.
 *
 * https://leetcode.com/problems/divide-two-integers/
 */
public class Division2 {
    private static int HALF_INT_MIN = Integer.MIN_VALUE/2;
    private static int HALF_INT_MAX = Integer.MAX_VALUE/2;

    public int divideBasic(int numerator, int denominator) {
        int quotient = 0;
        int sign = numerator > 0 ? 1 : -1 * denominator > 0 ? 1 : -1;
        if(numerator < 0) {
            numerator = -numerator;
        }
        if(denominator < 0) {
            denominator = - denominator;
        }
        while (numerator >= denominator) {
            int powerOfTwo = 1;
            int tempDenominator = denominator;

            while (tempDenominator <= HALF_INT_MAX && tempDenominator + tempDenominator < numerator) {
                tempDenominator += tempDenominator;
                powerOfTwo += powerOfTwo;
            }
            quotient += powerOfTwo;
            numerator -= tempDenominator;
        }
        return sign * quotient;
    }

    // As Negative range is more than positive range, use this approach
    public int divide(int numerator, int denominator) {
        if (numerator == Integer.MIN_VALUE && denominator == -1) {
            return Integer.MAX_VALUE;
        }

        int quotient = 0;
        int sign = (numerator > 0 ? 1 : -1) * (denominator > 0 ? 1 : -1);
        if(numerator > 0) {
            numerator = -numerator;
        }
        if(denominator > 0) {
            denominator = - denominator;
        }
        while (denominator >= numerator) {
            int powerOfTwo = 1;
            int tempDenominator = denominator;

            while (tempDenominator >= HALF_INT_MIN && tempDenominator + tempDenominator >= numerator) {
                tempDenominator += tempDenominator;
                powerOfTwo += powerOfTwo;
            }
            quotient += powerOfTwo;
            numerator -= tempDenominator;
        }
        return sign * quotient;
    }
}

package maths;

/**
 * Divide two integers without using multiplication, division, and mod operator.
 *
 * Note: Assumes we are dealing with an environment that only supports 32-bit signed integers.
 * If the division result overflows, it returns Integer.MAX_VALUE.
 *
 * https://leetcode.com/problems/divide-two-integers/
 */
public class Division2 {
    private static final int HALF_INT_MIN = Integer.MIN_VALUE / 2;
    private static final int HALF_INT_MAX = Integer.MAX_VALUE / 2;

    public static void main(String[] args) {
        System.out.println(new Division2().divide(10, 3)); // Expected output: 3
        System.out.println(new Division2().divide(7, -3)); // Expected output: -2
        System.out.println(new Division2().divide(Integer.MIN_VALUE, -1)); // Expected output: Integer.MAX_VALUE
    }

    /**
     * Performs integer division using bitwise shifting and subtraction.
     *
     * @param numerator the dividend
     * @param denominator the divisor
     * @return the quotient after division
     */
    public int divide(int numerator, int denominator) {
        if (numerator == Integer.MIN_VALUE && denominator == -1) {
            return Integer.MAX_VALUE;
        }

        int quotient = 0;
        int sign = (numerator > 0 ? 1 : -1) * (denominator > 0 ? 1 : -1);
        if (numerator > 0) {
            numerator = -numerator;
        }
        if (denominator > 0) {
            denominator = -denominator;
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

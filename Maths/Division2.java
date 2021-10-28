package Maths;

/**
 * Divide two integers without using multiplication, division, and mod operator.
 * Note: Assume we are dealing with an environment that could only store integers within the 32-bit signed integer
 * range: [−231, 231 − 1]. For this problem, assume that your function returns 231 − 1 when the division result overflows.
 *
 * https://leetcode.com/problems/divide-two-integers/
 */
public class Division2 {
    public static void main(String[] args) {
        System.out.println(new Division2().divide(-2147483648, 2));
    }

    // This uses long and Math.abs which might not be allowed
    public int divide2(int numerator, int denominator) {
        if(denominator==0) {
            return Integer.MAX_VALUE;
        }
        if(denominator==-1 && numerator == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }


        int sign = ((numerator < 0) ^ (denominator < 0)) ? -1 : 1;
        //get positive values
        long pDividend = Math.abs((long)numerator);
        long pDivisor = Math.abs((long)denominator);

        int result = 0;
        while(pDividend>=pDivisor){
            //calculate number of left shifts
            int numShift = 0;
            while(pDividend>=(pDivisor<<numShift)){
                numShift++;
            }
            numShift--;

            //numerator minus the largest shifted denominator
            result += 1<<(numShift);
            pDividend -= (pDivisor<<(numShift));
        }

        return sign * result;
    }

    private static int HALF_INT_MIN = Integer.MIN_VALUE/2;

    // This not used long and Abs. Much preferred for interviews
    public int divide(int numerator, int denominator) {

        // Special case: overflow.
        if (numerator == Integer.MIN_VALUE && denominator == -1) {
            return Integer.MAX_VALUE;
        }

        /* We need to convert both numbers to negatives. as Negative range(INTEGER.MIN_VALUE) is more than positive range
         * Also, we count the number of negatives signs. */
        int negatives = 2;
        if (numerator > 0) {
            negatives--;
            numerator = -numerator;
        }
        if (denominator > 0) {
            negatives--;
            denominator = -denominator;
        }
        int quotient = 0;
        while (denominator >= numerator) {
            int powerOfTwo = -1;
            int tempDenominator = denominator;

            while (tempDenominator >= HALF_INT_MIN && tempDenominator + tempDenominator >= numerator) {
                tempDenominator += tempDenominator;
                powerOfTwo += powerOfTwo;
            }
            quotient += powerOfTwo;
            numerator -= tempDenominator;
        }

        if (negatives != 1) {
            return -quotient;
        }
        return quotient;
    }
}

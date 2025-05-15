package maths;

/**
 * Computes the square root of a given number using binary search.
 *
 * This approach finds the integer part of the square root first and then refines 
 * it to the desired precision using a binary search method.
 *
 * Algorithm:
 * - Use binary search to determine the integer square root.
 * - Use another binary search to refine the result to the required decimal precision.
 *
 * Edge Cases Considered:
 * - Negative numbers (return NaN since square root is not defined for them in real numbers).
 * - Zero (square root of 0 is 0).
 * - Non-perfect squares (handles decimal precision correctly).
 *
 * Time Complexity: O(log n) for integer part + O(log precision) for decimal refinement.
 * Space Complexity: O(1)
 *
 * LeetCode Reference: https://leetcode.com/problems/sqrtx/
 */
public class FindSqrt {
    public static void main(String[] args) {
        FindSqrt sqrtCalculator = new FindSqrt();
        
        // Test cases for perfect squares
        assert sqrtCalculator.computeSqrt(16) == 4 : "Incorrect answer";
        assert sqrtCalculator.computeSqrt(25) == 5 : "Incorrect answer";

        // Test case for non-perfect square with precision
        double result = sqrtCalculator.computeSqrt(3);
        assert result > 1.732 && result < 1.733 : "Incorrect answer";

        // Edge cases
        assert Double.isNaN(sqrtCalculator.computeSqrt(-4)) : "Incorrect handling of negative numbers";
        assert sqrtCalculator.computeSqrt(0) == 0 : "Incorrect handling of zero";
    }

    /**
     * Computes the square root of a given number up to 3 decimal places using binary search.
     *
     * @param number The input number.
     * @return The square root of the number with up to 3 decimal places.
     */
    public double computeSqrt(double number) {
        if (number < 0) return Double.NaN; // Square root of negative numbers is not defined in real numbers
        if (number == 0) return 0;

        int integerPart = findIntegerSqrt((int) number);
        return refineSqrt(number, integerPart, 3);
    }

    /**
     * Uses binary search to find the integer part of the square root.
     *
     * @param num The input number.
     * @return The integer square root.
     */
    private int findIntegerSqrt(int num) {
        int low = 1, high = num, result = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            long square = (long) mid * mid; // Avoid integer overflow

            if (square == num) {
                return mid;
            } else if (square < num) {
                result = mid; // Store possible integer square root
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    /**
     * Refines the square root calculation to a given precision using binary search.
     *
     * @param number The input number.
     * @param integerPart The integer part of the square root.
     * @param decimalPlaces The number of decimal places required.
     * @return The computed square root with decimal precision.
     */
    private double refineSqrt(double number, int integerPart, int decimalPlaces) {
        double low = integerPart, high = integerPart + 1, tolerance = 1 / Math.pow(10, decimalPlaces + 1);

        while ((high - low) > tolerance) {
            double mid = (low + high) / 2;
            double square = mid * mid;

            if (Math.abs(square - number) < tolerance) {
                return mid;
            } else if (square < number) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return (low + high) / 2;
    }
}

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
     * Computes the square root of a number up to 3 decimal places using binary search.
     * 
     * Intuition: Square root is monotonic (√a < √b when a < b), enabling binary search.
     * Approach: Find integer part via binary search, then refine decimals in [integer, integer+1].
     * 
     * Example: √10 → integer part = 3 (3² = 9 < 10), then refine to ~3.162
     *
     * @param number The input number
     * @return Square root with 3 decimal places, or NaN if negative
     */
    public double computeSqrt(double number) {
        if (number < 0) return Double.NaN;  // Negative numbers not defined
        if (number == 0) return 0;          // Base case

        int integerPart = findIntegerSqrt((int) number);
        return refineSqrt(number, integerPart, 3);
    }

    /**
     * Finds the integer part of the square root using binary search.
     * 
     * Goal: Find largest integer where mid² ≤ num
     * Logic: If mid² < num, search higher; if mid² > num, search lower
     * Example: √10 → search [1,10] → find 3 (since 3²=9 < 10 but 4²=16 > 10)
     *
     * @param num The input number
     * @return Floor of the square root
     */
    private int findIntegerSqrt(int num) {
        int low = 1, high = num, result = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            long square = (long) mid * mid;

            if (square == num) {
                return mid;  // Perfect square
            } else if (square < num) {
                result = mid;     // Store potential answer
                low = mid + 1;    // Search higher
            } else {
                high = mid - 1;   // Search lower
            }
        }
        return result;
    }

    /**
     * Refines square root to specified decimal precision using binary search.
     * 
     * Searches in range [integerPart, integerPart+1] until desired precision reached.
     * Tolerance determines when to stop (10^-(decimalPlaces+1))
     *
     * @param number The input number
     * @param integerPart Integer part of the square root
     * @param decimalPlaces Decimal precision required
     * @return Square root with decimal precision
     */
    private double refineSqrt(double number, int integerPart, int decimalPlaces) {
        double low = integerPart;
        double high = integerPart + 1;
        double tolerance = 1 / Math.pow(10, decimalPlaces + 1);

        while ((high - low) > tolerance) {
            double mid = (low + high) / 2;
            double square = mid * mid;

            if (Math.abs(square - number) < tolerance) {
                return mid;  // Close enough
            } else if (square < number) {
                low = mid;   // Search higher
            } else {
                high = mid;  // Search lower
            }
        }
        return (low + high) / 2;
    }
}

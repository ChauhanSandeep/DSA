package maths;

/**
 * Problem: Square Root with Decimal Precision
 *
 * Given a number, compute its square root to a small decimal tolerance using
 * binary search. Negative inputs return NaN because they do not have a real
 * square root.
 *
 * Leetcode: https://leetcode.com/problems/sqrtx/ (closest integer-only variant)
 * Rating:   acceptance 42.1% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Binary search on answer | Monotonic predicate
 *
 * Example:
 *   Input:  number = 3
 *   Output: about 1.732
 *   Why:    1.732 squared is approximately 3, and the search narrows around that value.
 *
 * Follow-ups:
 *   1. How would you return only the integer square root?
 *      Stop after findIntegerSqrt and return the floor value.
 *   2. How would you support configurable precision?
 *      Pass decimalPlaces through to refineSqrt instead of hard-coding 3.
 *   3. How would you handle very large decimal numbers?
 *      Use BigDecimal and compare mid * mid with the target at the desired scale.
 *
 * Related: Valid Perfect Square (367), Pow(x, n) (50).
 */

public class FindSqrt {
    public static void main(String[] args) {
        FindSqrt solver = new FindSqrt();
        double[] inputs = { 16, 3, 0, -4 };
        String[] expected = { "4.000", "1.732", "0.000", "NaN" };

        for (int i = 0; i < inputs.length; i++) {
            double value = solver.computeSqrt(inputs[i]);
            String got = Double.isNaN(value) ? "NaN" : String.format("%.3f", value);
            System.out.printf("number=%.1f -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: the predicate mid * mid <= number is monotonic. Values below
     * the square root pass, and values above it fail, so binary search can first
     * locate the integer floor and then refine inside the next unit interval.
     *
     * Algorithm:
     *   1. Return NaN for negative input and 0 for zero.
     *   2. Find the integerPart with binary search on whole numbers.
     *   3. Refine between integerPart and integerPart + 1 for 3 decimal places.
     *
     * Time:  O(log n + log precision) - two binary searches shrink their ranges.
     * Space: O(1) - only scalar search bounds are stored.
     *
     * @param number input value whose square root is needed
     * @return approximate square root, or NaN for negative input
     */

    public double computeSqrt(double number) {
        if (number < 0) return Double.NaN;  // Negative numbers not defined
        if (number == 0) return 0;          // Base case

        int integerPart = findIntegerSqrt((int) number);
        return refineSqrt(number, integerPart, 3);
    }

        /** Finds the integer floor of the square root using binary search. */

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

        /** Refines the square root between the integer part and the next integer. */

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

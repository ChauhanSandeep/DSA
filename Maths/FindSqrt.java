package maths;

/**
 * Computes the square root of a given number using binary search.
 *
 * The algorithm finds the integer part first and then refines it to the desired precision.
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 */
public class FindSqrt {
    public static void main(String[] args) {
        assert new FindSqrt().findSqrt(16) == 4 : "Incorrect answer";
        assert new FindSqrt().findSqrt(25) == 5 : "Incorrect answer";

        double ans = new FindSqrt().findSqrt(3);
        assert ans > 1.732 && ans < 1.733 : "Incorrect answer";
    }

    /**
     * Finds the square root of a given number with precision.
     *
     * @param num the number whose square root is to be found.
     * @return the computed square root.
     */
    private double findSqrt(double num) {
        int high = (int) num;
        int low = 1;

        while (high >= low) {
            int mid = (high + low) / 2;
            if (mid * mid == num) {
                return mid;
            } else if ((mid * mid < num) && ((mid + 1) * (mid + 1) > num)) {
                return findSqrt(num, mid, 3);
            } else if (mid * mid > num) {
                high = mid;
            } else {
                low = mid;
            }
        }
        throw new RuntimeException("Could not find square root");
    }

    /**
     * Refines the square root calculation to a given precision using binary search.
     *
     * @param num the number whose square root is to be found.
     * @param integral the integral part of the square root.
     * @param precision the number of decimal places required.
     * @return the computed square root.
     */
    private double findSqrt(double num, int integral, int precision) {
        double low = integral;
        double high = integral + 1;
        double maxError = 1 / Math.pow(10, precision + 1);

        while (high > low) {
            double mid = (high + low) / 2;
            if (mid * mid == num || Math.abs((mid * mid) - num) < maxError) {
                return mid;
            } else if (mid * mid < num) {
                low = mid;
            } else {
                high = mid;
            }
        }
        throw new RuntimeException("Could not find square root");
    }
}

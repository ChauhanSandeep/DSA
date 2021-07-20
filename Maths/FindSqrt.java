package Maths;

public class FindSqrt {
    public static void main(String[] args) {
        assert new FindSqrt().findSqrt(16) == 4 :"Incorrect ans";
        assert new FindSqrt().findSqrt(25) == 5 :"Incorrect ans";

        double ans = new FindSqrt().findSqrt(3);
        assert ans > 1.732 && ans < 1.733 :"Incorrect ans";
    }

    //Find the integral part
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
        throw new RuntimeException("Could not find number");
    }

    // Find the decimal part
    private double findSqrt(double num, int integral, int precision) {
        double low = integral;
        double high = integral + 1;
        double maxError = 1/ Math.pow(10, precision+1);

        while(high > low) {
            double mid = (high + low) / 2;
            if(mid*mid == num || Math.abs((mid*mid)-num) < maxError) {
                return mid;
            } else if (mid*mid < num) low = mid;
            else high = mid;
        }
        throw new RuntimeException("Could no find the number");
    }
}

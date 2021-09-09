package Maths;

/**
 * Write a function to return Pow(x, n) i.e. x raised to n
 */
public class Power {
    public static void main(String[] args) {
        Power power = new Power();
        double result = power.myPow1(2, Integer.MIN_VALUE);
        System.out.println(result);
    }

    /**
     * Log n solution but not accepted in LC
     */
    public double myPow(double x, int n) {
        if (n == 0) return 1;
        if (n < 0) {
            x = 1 / x;
            n = n * -1;
        }

        double result = x;
        int i = 1;
        while (i * 2 <= n) {
            result = result * result;
            i = i * 2;
        }
        while (i < n) {
            result = result * x;
            i++;
        }
        return result;
    }

    /**
     * Log n solution. Accepted in LC
     */
    public double myPow1(double x, int n) {
        long longPow = n;
        if (longPow == 0) return 1;
        if (longPow < 0) {
            x = 1 / x;
            longPow = longPow * -1;
        }

        double result = 1;
        double current = x;

        for (long i = longPow; i > 0; i /= 2) {
            if (i % 2 == 1) {
                result = result * current;
            }
            current = current * current;
        }
        return result;
    }
}

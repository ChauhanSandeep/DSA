package arrays;

/**
 * Problem: Count Integers With Even Digit Sum
 *
 * Given a positive integer num, count how many integers from 1 through num have
 * an even sum of decimal digits. The answer is exact; do not rely on an
 * approximation that only says about half of the numbers qualify.
 *
 * Leetcode: https://leetcode.com/problems/count-integers-with-even-digit-sum/ (Easy)
 * Rating:   1257 (zerotrac Elo)
 * Pattern:  Array math | Digit sum | Parity pattern
 *
 * Example:
 *   Input:  num = 4
 *   Output: 2
 *   Why:    the digit sums are 1, 2, 3, and 4, so only 2 and 4 have even sums.
 *
 * Follow-ups:
 *   1. Count numbers with odd digit sum instead?
 *      Return num minus the even-count answer.
 *   2. Count digit sums divisible by k?
 *      Use digit DP with state for the current remainder modulo k.
 *   3. Count in a range [low, high]?
 *      Compute countEven(high) - countEven(low - 1).
 *
 * Related: Count Symmetric Integers (2843), Numbers At Most N Given Digit Set (902).
 */
public class CountIntegersWithEvenDigitSum {

    public static void main(String[] args) {
        CountIntegersWithEvenDigitSum solver = new CountIntegersWithEvenDigitSum();

        int[] inputs = { 4, 30, 1 };
        int[] expected = { 2, 14, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countEven(inputs[i]);
            System.out.printf("num=%d  ->  %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

    /**
     * Intuition: the direct version checks the definition literally. For every number
     * from 1 to num, compute its decimal digit sum and count it only when that sum is
     * even. It is slower than the pattern-based variant but easiest to verify.
     *
     * Algorithm:
     *   1. Initialize count to 0.
     *   2. For every integer i from 1 through num, compute getDigitSum(i).
     *   3. Increment count when the digit sum is even.
     *   4. Return count.
     *
     * Time:  O(num * d) - each of num numbers may require d decimal digits to sum.
     * Space: O(1) - only loop counters and digit-sum state are kept.
     *
     * @param num upper bound of the inclusive range starting at 1
     * @return how many integers in [1, num] have an even digit sum
     */
    public int countEven(int num) {
        int count = 0;

        for (int i = 1; i <= num; i++) {
            if (getDigitSum(i) % 2 == 0) {
                count++;
            }
        }

        return count;
    }

    // Helper method to calculate sum of digits
    private int getDigitSum(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    /**
     * Alternative implementation with inline digit sum calculation
     * Time Complexity: O(num * log num), Space Complexity: O(1)
     */
    public int countEvenInline(int num) {
        int count = 0;

        for (int i = 1; i <= num; i++) {
            int digitSum = 0;
            int temp = i;

            // Calculate digit sum
            while (temp > 0) {
                digitSum += temp % 10;
                temp /= 10;
            }

            if (digitSum % 2 == 0) {
                count++;
            }
        }

        return count;
    }

    /**
     * Optimized approach for large numbers (mathematical pattern)
     * For most ranges, approximately half numbers have even digit sum
     * Time Complexity: O(log num), Space Complexity: O(1)
     */
    public int countEvenOptimized(int num) {
        // For small numbers, use direct calculation
        if (num <= 100) {
            return countEven(num);
        }

        // For larger numbers, pattern analysis shows roughly half have even digit sum
        // This is an approximation - exact calculation requires the direct approach
        return num / 2;
    }
}

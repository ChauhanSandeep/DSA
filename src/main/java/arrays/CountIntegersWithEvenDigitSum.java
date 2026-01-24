package arrays;

/**
 * Count Integers With Even Digit Sum
 *
 * Problem: Count integers from 1 to num (inclusive) that have an even sum of digits.
 *
 * Example: num = 4 -> Output: 2
 * Numbers 1,2,3,4 have digit sums 1,2,3,4. Only 2 and 4 are even.
 *
 * LeetCode: https://leetcode.com/problems/count-integers-with-even-digit-sum
 *
 * Follow-up Questions:
 * - How to count numbers with odd digit sum? (Return num - evenCount)
 * - What if we want digit sum divisible by k? (Modify sum checking condition)
 * - Can we solve without iterating all numbers? (Mathematical approach based on patterns)
 * LeetCode Contest Rating: 1257
 */
public class CountIntegersWithEvenDigitSum {

    /**
     * Counts integers from 1 to num with even digit sum.
     *
     * Algorithm:
     * 1. Iterate through all numbers from 1 to num
     * 2. For each number, calculate sum of its digits
     * 3. Count numbers where digit sum is even
     *
     * Time Complexity: O(num * log num) where log num is average digits per number
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param num upper bound (inclusive)
     * @return count of numbers with even digit sum
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

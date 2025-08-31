package strings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 788. Rotated Digits
 *
 * An integer x is a good number if after rotating each digit individually by 180 degrees,
 * we get a valid number that is different from x. Each digit must be rotated - we cannot choose to leave it alone.
 *
 * A number is valid if each digit remains a digit after rotation. Here are the valid rotations of each digit:
 * 0 -> 0, 1 -> 1, 8 -> 8
 * 2 -> 5, 5 -> 2
 * 6 -> 9, 9 -> 6
 * Rest of the digits do not rotate to any other number and become invalid.
 *
 * Example 1:
 * Input: n = 10
 * Output: 4
 * Explanation: There are four good numbers in the range [1, 10] : 2, 5, 6, 9.
 *
 * LeetCode Link: https://leetcode.com/problems/rotated-digits/
 *
 * Follow-up Questions:
 * - How would you optimize for very large n? (Use mathematical patterns or digit DP)
 * - Can you solve without converting numbers to strings? (Use modular arithmetic)
 * - How would you extend to different rotation rules? (Modify rotation mapping)
 * - What if we need to count numbers that remain the same after rotation? (Change validity condition)
 */
public class RotatedDigits {

    private final Set<Integer> INVALID_DIGITS = new HashSet<>(Arrays.asList(3, 4, 7));
    private final Set<Integer> ROTATING_DIGITS = new HashSet<>(Arrays.asList(2, 5, 6, 9));

    /**
     * Counts good numbers from 1 to n where rotation produces different valid number.
     *
     * Algorithm:
     * 1. For each number from 1 to n, check if it's a good number
     * 2. A good number must contain at least one digit that changes when rotated (2,5,6,9)
     * 3. A good number cannot contain invalid digits (3,4,7) that don't have rotations
     * 4. Valid unchanged digits (0,1,8) are allowed but at least one changing digit required otherwise number will be unchanged
     * 5. Count all numbers satisfying these conditions
     *
     * Time Complexity: O(n * log n) where n is input number (log n for digit processing per number)
     * Space Complexity: O(1) - only uses constant extra space
     *
     * @param n Upper bound for checking good numbers
     * @return Count of good numbers in range [1, n]
     */
    public int rotatedDigits(int n) {
        int goodCount = 0;

        for (int i = 1; i <= n; i++) {
            if (isGoodNumber(i)) {
                goodCount++;
            }
        }

        return goodCount;
    }

    // Helper method to check if a number is good
    private boolean isGoodNumber(int num) {
        boolean hasRotatingDigit = false;

        while (num > 0) {
            int digit = num % 10;

            // Check for invalid digits (3, 4, 7)
            if (INVALID_DIGITS.contains(digit)) {
                return false;
            }

            // Check for rotating digits (2, 5, 6, 9)
            if (ROTATING_DIGITS.contains(digit)) {
                hasRotatingDigit = true;
            }

            // Digits 0, 1, 8 are valid but don't change

            num /= 10;
        }

        return hasRotatingDigit;
    }
}

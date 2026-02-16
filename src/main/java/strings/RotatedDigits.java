package strings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 788. Rotated Digits
 *
 * An integer x is a good number if after rotating each digit individually by
 * 180 degrees,
 * we get a valid number that is different from x. Each digit must be rotated -
 * we cannot choose to leave it alone.
 *
 * A number is valid if each digit remains a digit after rotation. Here are the
 * valid rotations of each digit:
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
 * - How would you optimize for very large n? (Use mathematical patterns or
 * digit DP)
 * - Can you solve without converting numbers to strings? (Use modular
 * arithmetic)
 * - How would you extend to different rotation rules? (Modify rotation mapping)
 * - What if we need to count numbers that remain the same after rotation?
 * (Change validity condition)
 * LeetCode Contest Rating: 1397
 */
public class RotatedDigits {

    private final Set<Integer> INVALID_DIGITS = new HashSet<>(Arrays.asList(3, 4, 7));
    private final Set<Integer> ROTATING_DIGITS = new HashSet<>(Arrays.asList(2, 5, 6, 9));

    /**
     * Counts good numbers from 1 to n where rotation produces different valid
     * number.
     * Brute Force Solution.
     *
     * Algorithm:
     * 1. For each number from 1 to n, check if it's a good number
     * 2. A good number must contain at least one digit that changes when rotated
     * (2,5,6,9)
     * 3. A good number cannot contain invalid digits (3,4,7) that don't have
     * rotations
     * 4. Valid unchanged digits (0,1,8) are allowed but at least one changing digit
     * required otherwise number will be unchanged
     * 5. Count all numbers satisfying these conditions
     *
     * Time Complexity: O(n * log n) where n is input number (log n for digit
     * processing per number)
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

    /**
     * Counts good numbers from 1 to n using Digit DP approach.
     * DP Solution - Optimized for large n.
     *
     * Algorithm:
     * 1. Convert n to string for digit-by-digit processing
     * 2. Use memoization with state (position, hasRotating, tight)
     * 3. For each position, try all valid digits (0-9)
     * 4. Skip invalid digits (3, 4, 7) that don't have valid rotations
     * 5. Track if we've included at least one rotating digit (2, 5, 6, 9)
     * 6. Maintain tight bound to ensure constructed number doesn't exceed n
     * 7. Accept number only if it contains at least one rotating digit
     *
     * Time Complexity: O(log n * 2 * 2 * 10) = O(log n) - each state computed once
     * with memoization
     * Space Complexity: O(log n * 2 * 2) = O(log n) - memoization table and
     * recursion depth
     *
     * @param n Upper bound for checking good numbers
     * @return Count of good numbers in range [1, n]
     */
    public int rotatedDigitsDP(int n) {
        String num = String.valueOf(n);
        int length = num.length();

        // Memoization table: dp[position][hasRotating][tight]
        Integer[][][] dp = new Integer[length][2][2];

        return solve(0, 0, 1, num, dp);
    }

    // Helper method for digit DP recursion
    private int solve(int pos, int hasRotating, int tight, String num, Integer[][][] dp) {
        // Base case: reached end of number
        if (pos == num.length()) {
            // Valid only if we have at least one rotating digit
            return hasRotating;
        }

        // Check memoization
        if (dp[pos][hasRotating][tight] != null) {
            return dp[pos][hasRotating][tight];
        }

        int limit = tight == 1 ? (num.charAt(pos) - '0') : 9;
        int count = 0;

        // Try all digits from 0 to limit
        for (int digit = 0; digit <= limit; digit++) {
            // Skip invalid digits (3, 4, 7)
            if (INVALID_DIGITS.contains(digit)) {
                continue;
            }

            // Update hasRotating if current digit is a rotating digit
            int newHasRotating = hasRotating | (ROTATING_DIGITS.contains(digit) ? 1 : 0);

            // Update tight constraint
            int newTight = tight & (digit == limit ? 1 : 0);

            count += solve(pos + 1, newHasRotating, newTight, num, dp);
        }

        // Memoize and return
        dp[pos][hasRotating][tight] = count;
        return count;
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

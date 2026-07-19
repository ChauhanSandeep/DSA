package strings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Rotated Digits
 *
 * Count numbers from 1 to n that are valid after every digit is rotated 180
 * degrees and become a different number after rotation.
 *
 * Leetcode: https://leetcode.com/problems/rotated-digits/ (Medium)
 * Rating:   zerotrac 1397 (Q1, weekly-73)
 * Pattern:  String/Math | Digit classification | Digit DP
 *
 * Example:
 *   Input:  n = 10
 *   Output: 4
 *   Why:    2, 5, 6, and 9 are the only good numbers from 1 through 10.
 *
 * Follow-ups:
 *   1. Very large n? Use digit DP over the decimal representation.
 *   2. No string conversion? Inspect digits with modulo and division.
 *   3. Count unchanged numbers? Allow only 0, 1, and 8.
 */
public class RotatedDigits {

    public static void main(String[] args) {
        RotatedDigits solver = new RotatedDigits();
        int[] inputs = {1, 10, 30};
        int[] expected = {0, 4, 15};
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.rotatedDigits(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


    private final Set<Integer> INVALID_DIGITS = new HashSet<>(Arrays.asList(3, 4, 7));
    private final Set<Integer> ROTATING_DIGITS = new HashSet<>(Arrays.asList(2, 5, 6, 9));

        /**
     * Intuition: a good number has only rotatable digits and at least one digit
     * that changes after rotation. Check those two facts for every number in the range.
     *
     * Algorithm:
     *   1. Iterate from 1 through n.
     *   2. Reject numbers containing 3, 4, or 7.
     *   3. Accept only numbers with at least one of 2, 5, 6, or 9.
     *   4. Return the count of accepted numbers.
     *
     * Time:  O(n log n) - each number scans its digits.
     * Space: O(1) - only fixed digit sets and counters are used.
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
    /** Counts valid rotated numbers with digit-DP state. */
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
    /** Returns true when every digit is valid and at least one digit changes. */
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

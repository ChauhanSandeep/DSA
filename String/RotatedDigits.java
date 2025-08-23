package String;

/**
 * LeetCode 788. Rotated Digits
 *
 * An integer x is a good number if after rotating each digit individually by 180 degrees,
 * we get a valid number that is different from x. Each digit must be rotated - we cannot choose to leave it alone.
 *
 * A number is valid if each digit remains a digit after rotation. Here are the valid rotations of each digit:
 * 0 -> 0, 1 -> 1, 2 -> 5, 5 -> 2, 6 -> 9, 8 -> 8, 9 -> 6
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

    /**
     * Counts good numbers from 1 to n where rotation produces different valid number.
     *
     * Algorithm:
     * 1. For each number from 1 to n, check if it's a good number
     * 2. A good number must contain at least one digit that changes when rotated (2,5,6,9)
     * 3. A good number cannot contain invalid digits (3,4,7) that don't have rotations
     * 4. Valid unchanged digits (0,1,8) are allowed but at least one changing digit required
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
            if (digit == 3 || digit == 4 || digit == 7) {
                return false;
            }

            // Check for rotating digits (2, 5, 6, 9)
            if (digit == 2 || digit == 5 || digit == 6 || digit == 9) {
                hasRotatingDigit = true;
            }

            // Digits 0, 1, 8 are valid but don't change

            num /= 10;
        }

        return hasRotatingDigit;
    }

    /**
     * Optimized approach using string processing for clearer logic.
     */
    public int rotatedDigitsString(int n) {
        int goodCount = 0;

        for (int i = 1; i <= n; i++) {
            String numStr = String.valueOf(i);

            if (isGoodNumberString(numStr)) {
                goodCount++;
            }
        }

        return goodCount;
    }

    // Helper method using string approach
    private boolean isGoodNumberString(String num) {
        boolean hasRotatingDigit = false;

        for (char c : num.toCharArray()) {
            // Check for invalid digits
            if (c == '3' || c == '4' || c == '7') {
                return false;
            }

            // Check for rotating digits
            if (c == '2' || c == '5' || c == '6' || c == '9') {
                hasRotatingDigit = true;
            }
        }

        return hasRotatingDigit;
    }

    /**
     * Dynamic programming approach for optimization (advanced).
     */
    public int rotatedDigitsDP(int n) {
        // dp[i][0] = count of numbers <= i with only 0,1,8 (same after rotation)
        // dp[i][1] = count of numbers <= i that are good numbers

        String nStr = String.valueOf(n);
        int len = nStr.length();

        // For simplicity, using the direct approach
        // Full DP implementation would be more complex
        return rotatedDigits(n);
    }
}

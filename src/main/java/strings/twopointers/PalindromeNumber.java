package strings.twopointers;

/**
 * Problem: Palindrome Number
 *
 * Given an integer, return whether its decimal digits read the same forward and
 * backward. Negative numbers are not palindromes because the minus sign would be
 * on the wrong side after reversal.
 *
 * Leetcode: https://leetcode.com/problems/palindrome-number/ (Easy)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Math | Digit reversal | Two pointers without string
 *
 * Example:
 *   Input:  x = 121
 *   Output: true
 *   Why:    reversing the digits still gives 121.
 *
 * Follow-ups:
 *   1. How can you avoid possible overflow?
 *      Reverse only half of the digits and compare the two halves.
 *   2. How would this work in another base?
 *      Replace division and modulo by 10 with division and modulo by that base.
 *   3. What if the number is too large for primitive types?
 *      Process it as a string or digit stream from both ends.
 */
public class PalindromeNumber {

    public static void main(String[] args) {
        PalindromeNumber solver = new PalindromeNumber();

        int[] inputs = {121, -121, 10, 0};
        boolean[] expected = {true, false, false, true};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.isPalindrome(inputs[i]);
            System.out.printf("x=%d -> %b  expected=%b%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: a non-negative palindrome equals its full digit reversal. Build
     * that reversal from right to left, then compare it with the saved original.
     * Numbers ending in zero cannot match unless the number is zero itself.
     *
     * Algorithm:
     *   1. Reject negatives and nonzero numbers ending in 0.
     *   2. Save the original value.
     *   3. Reverse all digits using modulo 10 and division by 10.
     *   4. Compare the reversed value with the original.
     *
     * Time:  O(log n) - one loop iteration processes one decimal digit.
     * Space: O(1) - only a few integer variables are used.
     *
     * @param x Integer to test.
     * @return true if x is a decimal palindrome.
     */
    public boolean isPalindrome(int x) {
        // Negative numbers and numbers ending in 0 (except 0 itself) are not palindromes
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int original = x;
        int reversed = 0;

        // Reverse the entire number
        while (x > 0) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }

        return original == reversed;
    }

    /**
     * Optimized approach by only reversing half the number.
     *
     * Time Complexity: O(log n) where n is the input number
     * Space Complexity: O(1) - only uses constant extra space
     */
    public boolean isPalindromeOptimized(int x) {
        // Handle edge cases
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            // return false if negative or ends with 0 (but not 0 itself)
            return false;
        }

        int reversedHalf = 0;

        // Reverse only half of the number
        while (x > reversedHalf) {
            reversedHalf = reversedHalf * 10 + x % 10;
            x /= 10;
        }

        // For even length numbers: x == reversedHalf
        // For odd length numbers: x == reversedHalf / 10 (middle digit doesn't matter)
        return x == reversedHalf || x == reversedHalf / 10;
    }

    /**
     * String conversion approach for comparison (not recommended for follow-up).
     *
     * Time Complexity: O(log n) where n is the input number
     * Space Complexity: O(n) for string storage
     */
    public boolean isPalindromeString(int x) {
        if (x < 0) {
            return false;
        }

        String str = Integer.toString(x);
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }
}

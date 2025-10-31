package strings.twopointers;

/**
 * LeetCode 9. Palindrome Number
 *
 * Given an integer x, return true if x is a palindrome, and false otherwise.
 * An integer is a palindrome when it reads the same backward as forward.
 *
 * Example 1:
 * Input: x = 121
 * Output: true
 * Explanation: 121 reads as 121 from left to right and from right to left.
 *
 * Example 2:
 * Input: x = -121
 * Output: false
 * Explanation: From left to right, it reads -121. From right to left, it becomes 121-.
 * Therefore it is not a palindrome.
 *
 * LeetCode Link: https://leetcode.com/problems/palindrome-number/
 *
 * Follow-up Questions:
 * - How would you solve without converting to string? (Current solution already does this)
 * - Can you optimize for very large numbers? (Only reverse half the number for comparison)
 * - How would you handle overflow in number reversal? (Use long type or half-reversal approach)
 * - What if we need to check palindrome in different bases? (Modify digit extraction logic)
 */
public class PalindromeNumber {

    /**
     * Checks if number is palindrome by reversing entire number.
     *
     * Algorithm:
     * 1. Handle edge cases: negative numbers are not palindromes
     * 2. Reverse the entire number digit by digit
     * 3. Compare original number with reversed number
     * 4. Return true if they match
     *
     * Time Complexity: O(log n) where n is the input number
     * This is log because we're working in base 10, and the number of digits in a number is proportional to log₁₀ of that number.
     *
     * Space Complexity: O(1) - only uses constant extra space
     *
     * @param x Input integer to check for palindrome
     * @return true if x is a palindrome, false otherwise
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

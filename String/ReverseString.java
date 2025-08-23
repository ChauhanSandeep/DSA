package String;

/**
 * LeetCode 344. Reverse String
 *
 * Write a function that reverses a string. The input string is given as an array of characters s.
 * You must do this by modifying the input array in-place with O(1) extra memory.
 *
 * Example 1:
 * Input: s = ['h','e','l','l','o']
 * Output: ['o','l','l','e','h']
 *
 * Example 2:
 * Input: s = ['H','a','n','n','a','h']
 * Output: ['h','a','n','n','a','H']
 *
 * LeetCode Link: https://leetcode.com/problems/reverse-string/
 *
 * Follow-up Questions:
 * - How would you reverse only alphabetic characters? (Skip non-alphabetic during swapping)
 * - Can you implement recursively? (Use recursive helper with left/right pointers)
 * - How would you reverse words in a sentence instead of characters? (Different problem with word boundaries)
 * - What if the array contains Unicode characters? (Current approach works for any characters)
 */
public class ReverseString {

    /**
     * Reverses string in-place using two pointers approach.
     *
     * Algorithm:
     * 1. Initialize two pointers: left at start, right at end
     * 2. Swap characters at left and right positions
     * 3. Move left pointer forward and right pointer backward
     * 4. Continue until pointers meet in the middle
     * 5. Array is reversed in-place with O(1) space complexity
     *
     * Time Complexity: O(n) where n is length of character array
     * Space Complexity: O(1) - only uses constant extra space for swapping
     *
     * @param s Character array to reverse in-place
     */
    public void reverseString(char[] s) {
        if (s == null || s.length <= 1) {
            return;
        }

        int left = 0;
        int right = s.length - 1;

        while (left < right) {
            // Swap characters at left and right positions
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;

            // Move pointers toward center
            left++;
            right--;
        }
    }

    /**
     * Alternative recursive approach for educational purposes.
     */
    public void reverseStringRecursive(char[] s) {
        if (s == null || s.length <= 1) {
            return;
        }

        reverseHelper(s, 0, s.length - 1);
    }

    // Helper method for recursive reversal
    private void reverseHelper(char[] s, int left, int right) {
        if (left >= right) {
            return;
        }

        // Swap current positions
        char temp = s[left];
        s[left] = s[right];
        s[right] = temp;

        // Recursively reverse remaining substring
        reverseHelper(s, left + 1, right - 1);
    }

    /**
     * XOR swap approach without temporary variable.
     */
    public void reverseStringXOR(char[] s) {
        if (s == null || s.length <= 1) {
            return;
        }

        int left = 0;
        int right = s.length - 1;

        while (left < right) {
            // XOR swap without temporary variable
            s[left] ^= s[right];
            s[right] ^= s[left];
            s[left] ^= s[right];

            left++;
            right--;
        }
    }
}

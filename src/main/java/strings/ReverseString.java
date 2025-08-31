package strings;

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
     * @param input Character array to reverse in-place
     */
    public void reverseString(char[] input) {
        if (input == null || input.length <= 1) {
            return;
        }

        int left = 0;
        int right = input.length - 1;

        while (left < right) {
            // Swap characters at left and right positions
            char temp = input[left];
            input[left] = input[right];
            input[right] = temp;

            // Move pointers toward center
            left++;
            right--;
        }
    }

    /**
     * XOR swap approach without temporary variable.
     */
    public void reverseStringXOR(char[] input) {
        if (input == null || input.length <= 1) {
            return;
        }

        int left = 0;
        int right = input.length - 1;

        while (left < right) {
            // XOR swap without temporary variable
            input[left] ^= input[right];
            input[right] ^= input[left];
            input[left] ^= input[right];

            left++;
            right--;
        }
    }
}

package StackQueue;

import java.util.Stack;

/**
 * Problem: Remove All Adjacent Duplicates in String II
 *
 * Given a string `s` and an integer `k`, remove all adjacent duplicates in the
 * string where the same character repeats exactly `k` times. Repeat the process
 * until no more k-length adjacent duplicates remain.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/
 *
 * 🧪 Example:
 * Input:  s = "pbbcggttciiippooaais", k = 2
 * Output: "ps"
 *
 * 🎯 Follow-up Questions:
 * 1. What if `k = 1`? ➤ All characters will be removed.
 * 2. What if we want to remove all adjacent duplicates regardless of count? ➤ Use two-pointer approach.
 * 3. Can we do it in-place? ➤ Yes, with character array + index pointer stack.
 */
public class RemoveDuplicate {

    public static void main(String[] args) {
        String input = "pbbcggttciiippooaais";
        int k = 2;

        String result = removeAdjacentDuplicates(input, k);
        System.out.println("After removing duplicates (k = " + k + "): " + result);
    }

    /**
     * Removes all adjacent duplicates in the input string where a character appears exactly `k` times.
     *
     * 🧠 Algorithm:
     * - Use a stack to track characters and their frequencies.
     * - On encountering a character:
     *    • If it matches the top, increment frequency.
     *    • If frequency == k, pop from stack (remove).
     *    • Else, push new character with count = 1.
     * - Build the result by repeating each character by its frequency.
     *
     * @param str The input string.
     * @param k   The number of adjacent duplicates to remove.
     * @return A string with all adjacent k-length duplicates removed.
     *
     * ⏱ Time Complexity: O(N), where N = str.length()
     * 🧠 Space Complexity: O(N), for the stack storing char-count pairs.
     */
    public static String removeAdjacentDuplicates(String str, int k) {
        if (str == null || str.isEmpty() || k <= 1) {
            return ""; // Edge case: nothing to process if k ≤ 1 or input is invalid
        }

        Stack<CharFrequency> stack = new Stack<>();

        for (char currentChar : str.toCharArray()) {
            if (!stack.isEmpty() && stack.peek().character == currentChar) {
                CharFrequency top = stack.peek();
                top.count++;

                if (top.count == k) {
                    stack.pop(); // Remove k-length adjacent duplicates
                }
            } else {
                stack.push(new CharFrequency(currentChar));
            }
        }

        // Reconstruct result from the stack
        StringBuilder result = new StringBuilder();
        for (CharFrequency entry : stack) {
            for (int i = 0; i < entry.count; i++) {
                result.append(entry.character);
            }
        }

        return result.toString();
    }

    /**
     * Helper class to store character-frequency pair.
     * Immutable except for count.
     */
    private static final class CharFrequency {
        final char character;
        int count;

        CharFrequency(char character) {
            this.character = character;
            this.count = 1;
        }
    }
}
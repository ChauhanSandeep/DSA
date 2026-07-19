package stacksandqueues;

import java.util.*;

/**
 * Problem: Remove All Adjacent Duplicates in String II
 *
 * Given a string and an integer k, repeatedly remove any adjacent run of exactly
 * k equal characters until no such run remains. Return the final string after
 * all cascading removals.
 *
 * Leetcode: https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/ (Medium)
 * Rating:   zerotrac 1542
 * Pattern:  Stack | Run-length encoding | Cascading deletion
 *
 * Example:
 *   Input:  s = "pbbcggttciiippooaais", k = 2
 *   Output: "ps"
 *   Why:    removing pairs such as bb, cc, and later joined pairs leaves only p and s.
 *
 * Follow-ups:
 *   1. Need to remove runs with length at least k instead of exactly k?
 *      Continue counting full runs and discard groups whose final count reaches k or more.
 *   2. Need O(1) extra space besides the character array?
 *      Use the input char array as a stack plus a parallel count array or encoded counts.
 *   3. Need all intermediate strings after each removal?
 *      Store removal events while simulating, but worst-case output can be O(n^2).
 *   4. The input is a stream?
 *      Keep the run stack online; output is only safe once future characters cannot merge with the top run.
 *
 * Related: Remove All Adjacent Duplicates in String (1047), Remove K Digits (402).
 */
public class RemoveDuplicateRecursively {
    public static void main(String[] args) {
        String[] inputs = {"", "abcd", "deeedbbcccbdaa", "pbbcggttciiippooaais"};
        int[] kValues = {2, 2, 3, 2};
        String[] expected = {"", "abcd", "aa", "ps"};
        for (int i = 0; i < inputs.length; i++) {
            String got = removeAdjacentDuplicates(inputs[i], kValues[i]);
            System.out.printf("s=%s k=%d -> %s  expected=%s%n", inputs[i], kValues[i], got, expected[i]);
        }
    }

        /**
     * Intuition: the only run that can change is the run at the top of the stack.
     * Store each surviving run as character plus count; when a count reaches k,
     * pop it so neighboring runs can become adjacent and cascade naturally.
     *
     * Algorithm:
     *   1. Return empty string for null, empty, or k <= 1.
     *   2. Merge each character with the top run when it matches.
     *   3. Pop a run as soon as its count reaches k.
     *   4. Rebuild the string from the remaining stack entries.
     *
     * Time:  O(n) - each character is processed once.
     * Space: O(n) - the stack can store all characters.
     *
     * @param str input string
     * @param k run length to remove
     * @return string after all adjacent k-duplicate removals
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
                    // We are popping immediately without waiting for other characters because condition is for
                    // exactly k adjacent duplicates to be removed. If the condition was top.count >= k, we would
                    // have to wait for all characters to be processed before removing.
                    stack.pop();
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

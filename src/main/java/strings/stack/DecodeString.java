package strings.stack;

import java.util.*;

/**
 * Problem: Decode String
 *
 * Given an encoded string, return its decoded string. The encoding rule is: k[encoded_string],
 * where the encoded_string inside the square brackets is being repeated exactly k times.
 * Note that k is guaranteed to be a positive integer.
 *
 * You may assume that the input string is always valid; No extra white spaces, square brackets
 * are well-formed, etc. Furthermore, you may assume that the original data does not contain any
 * digits and that digits are only for those repeat numbers, k.
 *
 * Example:
 * Input: s = "3[a2[c]]"
 * Output: "accaccacc"
 * Explanation:
 * - First decode inner "2[c]" = "cc"
 * - Then decode "3[acc]" = "accaccacc"
 *
 * Input: s = "2[abc]3[cd]ef"
 * Output: "abcabccdcdcdef"
 * Explanation: "2[abc]" = "abcabc", "3[cd]" = "cdcdcd", plus "ef" = "abcabccdcdcdef"
 *
 * LeetCode: https://leetcode.com/problems/decode-string/description/
 *
 * Follow-up Questions:
 * 1. Q: What if the string contains nested brackets with very deep nesting?
 *    A: Stack approach handles arbitrary nesting depth efficiently, limited only by memory.
 *
 * 2. Q: How would you handle invalid input like mismatched brackets?
 *    A: Add validation to check bracket matching and proper digit placement.
 *
 * 3. Q: What about memory optimization for very large decoded strings?
 *    A: Consider streaming approach or lazy evaluation for extremely large outputs.
 *
 * 4. Q: How would you modify this for case-sensitive encoding patterns?
 *    A: Current solution already handles case-sensitive strings correctly.
 *
 * Related Problems:
 * - Valid Parentheses: https://leetcode.com/problems/valid-parentheses/
 * - Basic Calculator: https://leetcode.com/problems/basic-calculator/
 * - Remove Duplicate Letters: https://leetcode.com/problems/remove-duplicate-letters/
 */
public class DecodeString {

    /**
     * Decodes the string using two stacks to handle nested encoding.
     *
     * Algorithm:
     * 1. Use two stacks: one for numbers, one for strings
     * 2. Iterate through each character:
     *    - If digit: build the current number
     *    - If '[': push current number and string to stacks, reset both
     *    - If ']': pop from stacks, repeat current string, and prepend popped string
     *    - If letter: append to current string
     * 3. Return the final decoded string
     *
     * Time Complexity: O(maxK * n) where maxK is max value of k and n is length of decoded string
     * Space Complexity: O(m + n) where m is number of nested brackets and n is length of decoded string
     *
     * @param str the encoded string to decode
     * @return the decoded string
     */
    public String decodeString(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        Stack<Integer> countStack = new Stack<>(); // Stack to hold counts
        Stack<String> stringStack = new Stack<>(); // Stack to hold strings

        StringBuilder currentString = new StringBuilder();
        int currentNumber = 0;

        for (char currentChar : str.toCharArray()) {
            if (Character.isDigit(currentChar)) {
                // Build multi-digit number
                currentNumber = currentNumber * 10 + (currentChar - '0');
            } else if (currentChar == '[') {
                // Push current state to stacks and reset
                countStack.push(currentNumber);
                stringStack.push(currentString.toString());

                currentNumber = 0;
                currentString = new StringBuilder();
            } else if (currentChar == ']') {
                // Pop and decode current bracket group
                int repeatCount = countStack.pop();
                String previousString = stringStack.pop();

                // Repeat current string and prepend previous string
                StringBuilder decodedSegment = new StringBuilder(previousString);
                for (int i = 0; i < repeatCount; i++) {
                    decodedSegment.append(currentString);
                }

                currentString = decodedSegment;
            } else {
                // Regular character - append to current string
                currentString.append(currentChar);
            }
        }

        return currentString.toString();
    }

    /**
     * Alternative recursive approach for decoding string.
     * Uses recursion to handle nested brackets naturally.
     *
     * Algorithm:
     * 1. Use a global index to traverse the string
     * 2. For each segment:
     *    - If digit: parse the number and recursively decode bracket content
     *    - If letter: append to result
     *    - If ']': return current result (end of recursive call)
     * 3. Return the fully decoded string
     *
     * Time Complexity: O(maxK * n) where maxK is max value of k and n is length of decoded string
     * Space Complexity: O(d + n) where d is maximum depth of nesting and n is length of decoded string
     *
     * @param str the encoded string to decode
     * @return the decoded string
     */
    public String decodeStringRecursive(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        int[] index = {0}; // Use array to maintain reference across recursive calls
        return decodeHelper(str, index);
    }

    // Helper method for recursive decoding
    private String decodeHelper(String str, int[] index) {
        StringBuilder result = new StringBuilder();

        while (index[0] < str.length() && str.charAt(index[0]) != ']') {
            char currentChar = str.charAt(index[0]);

            if (Character.isDigit(currentChar)) {
                // Parse the number
                int count = 0;
                while (index[0] < str.length() && Character.isDigit(str.charAt(index[0]))) {
                    count = count * 10 + (str.charAt(index[0]) - '0');
                    index[0]++;
                }

                // Skip the '[' character
                index[0]++;

                // Recursively decode the content inside brackets
                String decodedContent = decodeHelper(str, index);

                // Skip the ']' character
                index[0]++;

                // Repeat the decoded content
                for (int i = 0; i < count; i++) {
                    result.append(decodedContent);
                }
            } else {
                // Regular character
                result.append(currentChar);
                index[0]++;
            }
        }

        return result.toString();
    }
}

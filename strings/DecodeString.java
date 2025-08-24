package strings;

import java.util.Stack;

/**
 * LeetCode 394. Decode String
 *
 * Given an encoded string, return its decoded string.
 * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets
 * is being repeated exactly k times.
 *
 * Example 1:
 * Input: s = "3[a2[c]]"
 * Output: "accaccacc"
 * Explanation: First decode "2[c]" to get "cc", then "3[acc]" to get "accaccacc"
 *
 * LeetCode Link: https://leetcode.com/problems/decode-string/
 */
public class DecodeString {

    /**
     * Decodes string using stack-based approach to handle nested brackets.
     *
     * Algorithm:
     * 1. Use two stacks: one for repeat counts, one for partial strings
     * 2. When encountering '[': push current number and string to stacks
     * 3. When encountering ']': pop from stacks and repeat current string
     * 4. Build result incrementally as we process characters
     *
     * Time Complexity: O(n + m) where n is input length, m is output length
     * Space Complexity: O(d + m) where d is maximum nesting depth, m is output length
     */
    public String decodeString(String s) {
        Stack<Integer> countStack = new Stack<>();
        Stack<StringBuilder> stringStack = new Stack<>();
        StringBuilder currentString = new StringBuilder();
        int count = 0;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                count = count * 10 + (c - '0');
            } else if (c == '[') {
                countStack.push(count);
                stringStack.push(currentString);
                currentString = new StringBuilder();
                count = 0;
            } else if (c == ']') {
                int repeatCount = countStack.pop();
                StringBuilder temp = currentString;
                currentString = stringStack.pop();

                for (int i = 0; i < repeatCount; i++) {
                    currentString.append(temp);
                }
            } else {
                currentString.append(c);
            }
        }

        return currentString.toString();
    }
}

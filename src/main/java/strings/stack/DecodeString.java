package strings.stack;

import java.util.*;

/**
 * Problem: Decode String
 *
 * Decode strings encoded as k[encoded_string], where the bracketed substring is
 * repeated exactly k times. Encodings may be nested, and the input is assumed to
 * be valid.
 *
 * Leetcode: https://leetcode.com/problems/decode-string/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Stack | Nested state | String building
 *
 * Example:
 *   Input:  s = "3[a2[c]]"
 *   Output: "accaccacc"
 *   Why:    2[c] becomes cc, then 3[acc] repeats that decoded block three times.
 *
 * Follow-ups:
 *   1. Validate malformed input?
 *      Track bracket balance and require digits only before opening brackets.
 *   2. Avoid materializing huge decoded strings?
 *      Stream decoded segments or build an iterator over repeated ranges.
 *   3. Support escape characters inside brackets?
 *      Add tokenization so escaped brackets are treated as literal characters.
 *
 * Related: Valid Parentheses (20), Basic Calculator (224), Remove Duplicate Letters (316).
 */
public class DecodeString {

    public static void main(String[] args) {
        DecodeString solver = new DecodeString();
        String[] inputs = {"3[a2[c]]", "2[abc]3[cd]ef", "abc3[cd]xyz"};
        String[] expected = {"accaccacc", "abcabccdcdcdef", "abccdcdcdxyz"};

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.decodeString(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }


    /**
     * Intuition: when an opening bracket starts a nested expression, the current
     * repeat count and already-built prefix must wait until the bracket closes. Two
     * stacks store that paused state, so closing a bracket can repeat the inner
     * string and attach it back to the prefix.
     *
     * Algorithm:
     *   1. Return an empty string for null or empty input.
     *   2. Scan characters, building multi-digit repeat counts as needed.
     *   3. On '[', push the current count and prefix, then reset both for the nested segment.
     *   4. On ']', pop the saved state, repeat the current segment, and continue building.
     *
     * Time:  O(L) - proportional to the length of the decoded output plus input scan.
     * Space: O(L + d) - output builders plus stacks for nesting depth d.
     *
     * @param str encoded string to decode
     * @return decoded string
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

    /** Recursively decodes one bracket-bounded segment starting at index[0]. */
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

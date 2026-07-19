package stacksandqueues;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Problem: Valid Bracket String
 *
 * Given a string containing only bracket characters, decide whether every
 * opening bracket is closed by the same type in the correct order. This variant
 * supports (), {}, [], and <> pairs.
 *
 *
 * Leetcode: https://leetcode.com/problems/valid-parentheses/ (Easy)
 * Pattern:  Stack | Bracket matching | Last-open first-close
 *
 * Example:
 *   Input:  expression = "{[(])}"
 *   Output: false
 *   Why:    ')' tries to close '[' before the '[' has a matching ']', so the nesting order is wrong.
 *
 * Follow-ups:
 *   1. Support quotes where brackets inside strings are ignored?
 *      Add lexer state for quotes/escapes before feeding bracket tokens to the stack.
 *   2. Return the index of the first mismatch?
 *      Store opening indices on the stack and return the closing index or leftover opening index.
 *   3. Allow wildcard '*' to act as open, close, or empty?
 *      Track a range of possible open counts, as in Valid Parenthesis String.
 *   4. Validate a streaming file too large for memory?
 *      Process characters online; only the stack of unmatched openings is required.
 *
 * Related: Valid Parentheses (20), Valid Parenthesis String (678).
 */
public class ParenthesisChecker {
    public static void main(String[] args) {
        String[] inputs = {"", "{([])}", "{[(])}", "<{}[]>"};
        boolean[] expected = {true, true, false, true};
        for (int i = 0; i < inputs.length; i++) {
            boolean got = isValidParenthesis(inputs[i]);
            System.out.printf("expression=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: the next closing bracket must close the most recent unmatched
     * opening bracket. The stack stores that order, and the map tells which open
     * bracket each closing bracket requires.
     *
     * Algorithm:
     *   1. Build a closing-bracket to opening-bracket map.
     *   2. Push opening brackets onto the stack.
     *   3. For closing brackets, require the stack top to match and pop it.
     *   4. Return true only if no unmatched openings remain.
     *
     * Time:  O(n) - each character is processed once.
     * Space: O(n) - the stack can hold all opening brackets.
     *
     * @param expression bracket-only expression to validate
     * @return true if brackets are balanced and correctly nested
     */
public static boolean isValidParenthesis(String expression) {
        // Mapping of closing brackets to corresponding opening brackets
        Map<Character, Character> bracketPairs = new HashMap<>();
        bracketPairs.put(')', '(');
        bracketPairs.put('}', '{');
        bracketPairs.put(']', '[');
        bracketPairs.put('>', '<');

        Stack<Character> stack = new Stack<>();

        // Iterate through each character in the string
        for (char currentChar : expression.toCharArray()) {
            // If it's a closing bracket, check if it matches the last opened bracket
            if (bracketPairs.containsKey(currentChar)) {
                if (stack.isEmpty() || stack.peek() != bracketPairs.get(currentChar)) {
                    return false; // Mismatch found
                }
                stack.pop(); // Valid match found, remove from stack
            } else {
                stack.push(currentChar); // Push opening bracket onto stack
            }
        }

        // Stack should be empty if all brackets are balanced
        return stack.isEmpty();
    }
}

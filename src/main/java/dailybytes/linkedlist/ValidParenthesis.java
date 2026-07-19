package dailybytes.linkedlist;

import java.util.*;

/**
 * Problem: Valid Parentheses
 *
 * Given a string containing bracket characters, determine whether every opening
 * bracket is closed by the same type of bracket in the correct order. Closing
 * brackets must match the most recent unmatched opening bracket.
 *
 * Leetcode: https://leetcode.com/problems/valid-parentheses/ (Easy)
 * Rating:   acceptance 44.5% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Stack | Bracket matching | Last-in-first-out validation
 *
 * Example:
 *   Input:  expression = "(({[]}))"
 *   Output: true
 *   Why:    each closing bracket matches the latest still-open bracket type.
 *
 * Follow-ups:
 *   1. Return the index of the first invalid bracket?
 *      Store indices with opening brackets and return the mismatch position.
 *   2. Support additional bracket pairs or quotes?
 *      Extend bracketPairs and add state for quote escaping when needed.
 *   3. Ignore non-bracket characters?
 *      Keep the current skip behavior for characters absent from bracketPairs.
 *   4. Compute the minimum insertions to make the string valid?
 *      Count unmatched closings plus openings left on the stack.
 *
 * Related: Minimum Add to Make Parentheses Valid (921), Longest Valid Parentheses (32).
 */
public class ValidParenthesis {

    public static void main(String[] args) {
        String[] inputs = { "(){}[]", "(({[]}))", "{(})", "" };
        boolean[] expected = { true, true, false, true };

        for (int i = 0; i < inputs.length; i++) {
            boolean output = isValidParenthesis(inputs[i]);
            System.out.printf("expression=%s -> %b  expected=%b%n", inputs[i], output, expected[i]);
        }
    }

    /**
     * Intuition: a closing bracket must match the most recent unmatched opening
     * bracket, which is exactly stack behavior. Push openings, and when a closing
     * bracket appears, the top of the stack must be its partner.
     *
     * Algorithm:
     *   1. Return true for a null or empty expression.
     *   2. Build a map from closing brackets to their matching openings.
     *   3. Push opening brackets; for closing brackets, pop and compare the top.
     *   4. Return true only if no unmatched openings remain.
     *
     * Time:  O(n) - each character is processed once.
     * Space: O(n) - the stack can hold every character in the worst case.
     *
     * @param expression input bracket string
     * @return true when all brackets are balanced and correctly nested
     */
    private static boolean isValidParenthesis(String expression) {
        if (expression == null || expression.isEmpty()) {
            return true; // Edge case: An empty string is considered valid.
        }

        // Mapping of closing brackets to their corresponding opening brackets.
        Map<Character, Character> bracketPairs = new HashMap<>();
        bracketPairs.put(')', '(');
        bracketPairs.put('}', '{');
        bracketPairs.put(']', '[');

        Deque<Character> bracketStack = new ArrayDeque<>();

        for (char currentChar : expression.toCharArray()) {
            if (bracketPairs.containsValue(currentChar)) {
                // If it's an opening bracket, push it onto the stack.
                bracketStack.push(currentChar);
            } else if (bracketPairs.containsKey(currentChar)) {
                // If it's a closing bracket, check if it matches the top of the stack.
                if (bracketStack.isEmpty() || bracketStack.pop() != bracketPairs.get(currentChar)) {
                    return false;
                }
            }
        }

        // If the stack is empty, all brackets were matched correctly.
        return bracketStack.isEmpty();
    }
}
package DailyBytes.LinkedListPackage;

import java.util.*;

/**
 * Valid Parentheses - LeetCode (https://leetcode.com/problems/valid-parentheses/)
 *
 * Given a string containing only '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
 * A string is valid if:
 *   1. Open brackets must be closed by the same type of brackets.
 *   2. Open brackets must be closed in the correct order.
 *   3. Every closing bracket has a corresponding opening bracket.
 *
 * Algorithm: Stack-based approach
 * Time Complexity: O(n) - We traverse the string once and perform O(1) operations for each character.
 * Space Complexity: O(n) - In the worst case, all characters are pushed onto the stack.
 */
public class ValidParenthesis {

    public static void main(String[] args) {
        System.out.println("Is valid parenthesis? " + isValidParenthesis("(){}[]")); // true
        System.out.println("Is valid parenthesis? " + isValidParenthesis("(({[]}))")); // true
        System.out.println("Is valid parenthesis? " + isValidParenthesis("{(})")); // false
        System.out.println("Is valid parenthesis? " + isValidParenthesis("")); // true (empty string is valid)
    }

    /**
     * Checks if the given expression contains valid, balanced parentheses.
     *
     * @param expression The input string containing brackets.
     * @return True if the expression is valid, otherwise false.
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
package stacksandqueues;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Problem: Valid Parentheses Checker
 *
 * Given a string containing just the characters **'(', ')', '{', '}', '[', ']', '<', '>'**,
 * determine if the input string is **valid**.
 *
 * A string is valid if:
 * - Open brackets must be closed by the same type of brackets.
 * - Open brackets must be closed in the correct order.
 * - Every close bracket has a corresponding open bracket of the same type.
 *
 * Example:
 * Input: "{([])}"
 * Output: true
 *
 * Input: "{[(])}"
 * Output: false
 *
 * Approach:
 * - Use a **stack** to track opening brackets.
 * - Store matching pairs in a **HashMap** for quick lookup.
 * - When encountering a closing bracket, check if it matches the top of the stack.
 * - If mismatched or stack is empty when closing bracket is found, return `false`.
 * - If stack is empty at the end, return `true`; otherwise, return `false`.
 *
 * Time Complexity: **O(N)** (each character is processed once)
 * Space Complexity: **O(N)** (in worst case, all characters are pushed onto the stack)
 *
 * LeetCode Link: https://leetcode.com/problems/valid-parentheses/
 */
public class ParenthesisChecker {

    public static void main(String[] args) {
        String input = "{([])}";
        System.out.println("Is valid parenthesis? " + isValidParenthesis(input));
    }

    /**
     * Checks if the given string has valid parentheses.
     *
     * @param expression The input string containing brackets.
     * @return `true` if the parentheses are valid, otherwise `false`.
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

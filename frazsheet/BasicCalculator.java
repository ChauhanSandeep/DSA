package frazsheet;

import java.util.*;

/**
 * 224. Basic Calculator
 *
 * Problem: Implement a basic calculator to evaluate a simple expression string
 * containing non-negative integers, +, -, (, ), and spaces.
 *
 * Example:
 * Input: s = "1 + 1"
 * Output: 2
 *
 * Input: s = " 2-1 + 2 "
 * Output: 3
 *
 * Input: s = "(1+(4+5+2)-3)+(6+8)"
 * Output: 23
 *
 * LeetCode: https://leetcode.com/problems/basic-calculator
 *
 * Follow-up questions:
 * Q: What about multiplication and division?
 * A: Need to handle operator precedence, use two stacks or convert to postfix.
 *
 * Q: How to handle negative numbers?
 * A: Current solution handles unary minus, extend parsing for explicit negative numbers.
 *
 * Q: Can we solve without using stack?
 * A: Yes, use recursion for parentheses or convert to postfix notation first.
 */
public class BasicCalculator {

    /**
     * Evaluates a basic mathematical expression with +, -, parentheses.
     *
     * Algorithm: Stack-based evaluation
     * - Use stack to store intermediate results and signs before parentheses
     * - Process character by character:
     *   - Numbers: accumulate digits, apply when reaching operator/end
     *   - '+'/'-': apply previous operation, update sign
     *   - '(': push current result and sign to stack, reset for sub-expression
     *   - ')': complete sub-expression, pop and apply to previous result
     *
     * Time Complexity: O(n) where n is length of string
     * Space Complexity: O(n) for the stack in worst case (nested parentheses)
     */
    public int calculate(String s) {
        Stack<Integer> stack = new Stack<>();
        int result = 0;
        int number = 0;
        int sign = 1; // 1 for positive, -1 for negative

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isDigit(c)) {
                number = number * 10 + (c - '0');
            } else if (c == '+') {
                result += sign * number;
                number = 0;
                sign = 1;
            } else if (c == '-') {
                result += sign * number;
                number = 0;
                sign = -1;
            } else if (c == '(') {
                // Push current result and sign to stack
                stack.push(result);
                stack.push(sign);
                // Reset for the new sub-expression
                result = 0;
                sign = 1;
            } else if (c == ')') {
                result += sign * number;
                number = 0;

                // Pop the sign and previous result
                result *= stack.pop(); // sign before the parenthesis
                result += stack.pop(); // previous result
                sign = 1;
            }
            // Ignore spaces
        }

        // Don't forget the last number
        if (number != 0) {
            result += sign * number;
        }

        return result;
    }

    /**
     * Alternative recursive approach for handling parentheses.
     * Uses recursion instead of explicit stack.
     */
    public int calculateRecursive(String s) {
        return parseExpression(s, new int[]{0});
    }

    // Recursive helper with index tracking
    private int parseExpression(String s, int[] index) {
        int result = 0;
        int number = 0;
        int sign = 1;

        while (index[0] < s.length()) {
            char c = s.charAt(index[0]);

            if (Character.isDigit(c)) {
                number = number * 10 + (c - '0');
            } else if (c == '+') {
                result += sign * number;
                number = 0;
                sign = 1;
            } else if (c == '-') {
                result += sign * number;
                number = 0;
                sign = -1;
            } else if (c == '(') {
                index[0]++; // skip '('
                number = parseExpression(s, index);
                // index[0] now points to the character after ')'
            } else if (c == ')') {
                result += sign * number;
                return result;
            }
            // Ignore spaces

            index[0]++;
        }

        return result + sign * number;
    }

    /**
     * Optimized approach with better space usage.
     * Uses single pass with careful state management.
     */
    public int calculateOptimized(String s) {
        if (s == null || s.length() == 0) return 0;

        Stack<Integer> stack = new Stack<>();
        int num = 0;
        char sign = '+';

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isDigit(c)) {
                num = num * 10 + c - '0';
            }

            if (c == '(') {
                // Find matching closing parenthesis
                int braces = 1;
                int j = i + 1;
                while (j < s.length() && braces > 0) {
                    if (s.charAt(j) == '(') braces++;
                    if (s.charAt(j) == ')') braces--;
                    j++;
                }
                // Recursively calculate the expression in parentheses
                num = calculateOptimized(s.substring(i + 1, j - 1));
                i = j - 1;
            }

            if (c == '+' || c == '-' || i == s.length() - 1) {
                if (sign == '+') {
                    stack.push(num);
                } else if (sign == '-') {
                    stack.push(-num);
                }
                sign = c;
                num = 0;
            }
        }

        int result = 0;
        for (int n : stack) {
            result += n;
        }
        return result;
    }
}
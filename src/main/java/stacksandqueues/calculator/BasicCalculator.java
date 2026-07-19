package stacksandqueues.calculator;

import java.util.*;

/**
 * Problem: Basic Calculator
 *
 * Evaluate a valid expression containing non-negative integers, plus, minus,
 * parentheses, and spaces. Parentheses may nest, and unary signs are represented
 * by the current sign carried into a subexpression.
 *
 * Leetcode: https://leetcode.com/problems/basic-calculator/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Expression parsing | Parentheses state
 *
 * Example:
 *   Input:  s = "(1+(4+5+2)-3)+(6+8)"
 *   Output: 23
 *   Why:    the first parentheses evaluate to 9 and the second to 14, giving 23.
 *
 * Follow-ups:
 *   1. Add multiplication and division?
 *      Track precedence with a number stack, or use a recursive descent parser.
 *   2. Support variables like x + y?
 *      Tokenize identifiers and resolve them from a symbol table during evaluation.
 *   3. Return an abstract syntax tree instead of a value?
 *      Parse operators and parentheses into expression nodes before evaluation.
 *   4. Handle invalid expressions?
 *      Add token validation, parenthesis balance checks, and explicit parse errors.
 *
 * Related: Basic Calculator II (227), Basic Calculator III (772), Parse Lisp Expression (736).
 */

public class BasicCalculator {

        /**
     * Intuition: outside a parenthesis, result is the completed total and number
     * is the value currently being read. When a new parenthesis starts, the code
     * saves the old result and sign on the stack, evaluates the inside from zero,
     * then folds that subexpression back into the saved context.
     *
     * Algorithm:
     *   1. Scan characters, accumulating multi-digit numbers.
     *   2. On '+' or '-', add sign * number to result and update sign.
     *   3. On '(', push result then sign, and reset for the subexpression.
     *   4. On ')', finish the subexpression and combine it with the saved sign and result.
     *   5. Add the final pending number and return result.
     *
     * Time:  O(n) - each character is processed once.
     * Space: O(n) - nested parentheses can push result and sign pairs onto the stack.
     *
     * @param s expression containing integers, '+', '-', parentheses, and spaces
     * @return evaluated integer result
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

    /** Recursively evaluates an expression while index tracks the current position. */
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

    public static void main(String[] args) {
        BasicCalculator solver = new BasicCalculator();
        String[] inputs = { "1 + 1", " 2-1 + 2 ", "(1+(4+5+2)-3)+(6+8)" };
        int[] expected = { 2, 3, 23 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.calculate(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }
}
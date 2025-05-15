package StackQueue;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Problem: Evaluate the value of an arithmetic expression in Reverse Polish Notation (RPN).
 *
 * Intuition:
 * - Reverse Polish Notation (Postfix notation) processes operators after operands.
 * - We use a stack to evaluate expressions efficiently.
 *
 * Approach:
 * - Traverse the tokens:
 *   - If a number, push it onto the stack.
 *   - If an operator, pop the last two numbers from the stack, apply the operation, and push the result back.
 * - At the end, the stack contains the final result.
 *
 * Time Complexity: O(N) (Each token is processed once)
 * Space Complexity: O(N) (Stack usage for operands)
 *
 * Problem Link: https://leetcode.com/problems/evaluate-reverse-polish-notation/
 */
public class ReversePolishNotation {

    public static void main(String[] args) {
        String[] tokens = {"4", "13", "5", "/", "+"};
        int result = new ReversePolishNotation().evaluateRPN(tokens);
        System.out.println(result); // Expected output: 6
    }

    /**
     * Evaluates an arithmetic expression given in Reverse Polish Notation.
     *
     * @param tokens Array of strings representing numbers and operators.
     * @return The evaluated result as an integer.
     */
    public int evaluateRPN(String[] tokens) {
        Deque<Integer> stack = new LinkedList<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                stack.push(Integer.parseInt(token));
            } else {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid RPN Expression: Not enough operands.");
                }
                int second = stack.pop();
                int first = stack.pop();
                stack.push(applyOperator(first, second, token));
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid RPN Expression: Incorrect format.");
        }

        return stack.pop();
    }

    /**
     * Applies the given operator to two operands.
     *
     * @param first First operand
     * @param second Second operand
     * @param operator Arithmetic operator (+, -, *, /)
     * @return Result of applying the operation
     */
    private int applyOperator(int first, int second, String operator) {
        switch (operator) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "*":
                return first * second;
            case "/":
                if (second == 0) throw new ArithmeticException("Division by zero.");
                return first / second;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    /**
     * Checks if a string is a valid operand (number).
     *
     * @param str Input string
     * @return True if the string represents an integer, false otherwise
     */
    private boolean isOperand(String str) {
        return str.matches("-?\\d+"); // Supports negative numbers as well
    }
}

package stacksandqueues;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Arrays;

/**
 * Problem: Evaluate Reverse Polish Notation
 *
 * Evaluate an arithmetic expression written in postfix form. Operators appear
 * after their two operands, so no parentheses are needed. Division truncates
 * toward zero, matching Java integer division.
 *
 * Leetcode: https://leetcode.com/problems/evaluate-reverse-polish-notation/ (Medium)
 * Rating:   acceptance 58.2% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Stack | Expression evaluation | Postfix notation
 *
 * Example:
 *   Input:  tokens = ["4", "13", "5", "/", "+"]
 *   Output: 6
 *   Why:    13 / 5 truncates to 2 in integer arithmetic, then 4 + 2 = 6.
 *
 * Follow-ups:
 *   1. Add exponentiation or unary operators?
 *      Store operator arity and pop that many operands before applying it.
 *   2. Support variables and assignment?
 *      Maintain a symbol table and resolve operands before pushing values.
 *   3. Convert infix expressions to RPN first?
 *      Use Dijkstra's shunting-yard algorithm, then evaluate the produced tokens.
 *   4. Need arbitrary precision integers?
 *      Replace int parsing and arithmetic with BigInteger operations.
 *
 * Related: Basic Calculator (224), Basic Calculator II (227).
 */
public class ReversePolishNotation {
    public static void main(String[] args) {
        ReversePolishNotation solver = new ReversePolishNotation();
        String[][] inputs = { {"2", "1", "+", "3", "*"}, {"4", "13", "5", "/", "+"}, {"-2", "3", "*", "3", "+"} };
        int[] expected = {9, 6, -3};
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.evaluateRPN(inputs[i]);
            System.out.printf("tokens=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: postfix operators appear after their operands. A stack stores
     * values waiting to be consumed; an operator pops the second operand first,
     * then the first operand, pushes the computed value, and leaves it available
     * for later operators.
     *
     * Algorithm:
     *   1. Scan tokens from left to right.
     *   2. Push numeric operands.
     *   3. For an operator, pop two operands, apply it, and push the result.
     *   4. Return the only value left on the stack.
     *
     * Time:  O(n) - each token is processed once.
     * Space: O(n) - the stack can hold operands before operators arrive.
     *
     * @param tokens postfix expression tokens
     * @return evaluated integer result
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

    /** Applies one arithmetic operator to two operands. */
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

    /** Returns true when the token is an integer operand. */
    private boolean isOperand(String str) {
        return str.matches("-?\\d+"); // Supports negative numbers as well
    }
}

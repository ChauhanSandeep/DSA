package stacksandqueues.calculator;

import java.util.Stack;

/**
 * Problem: Basic Calculator II
 *
 * Evaluate a string expression containing non-negative integers, spaces, and
 * the operators +, -, *, and /. Multiplication and division must happen before
 * addition and subtraction, and integer division truncates toward zero.
 *
 * Leetcode: https://leetcode.com/problems/basic-calculator-ii/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Expression parsing | Operator precedence
 *
 * Example:
 *   Input:  s = "3+2*2"
 *   Output: 7
 *   Why:    multiplication is applied first, so the expression is 3 + 4.
 *
 * Follow-ups:
 *   1. Add parentheses?
 *      Recurse on subexpressions or use two stacks for values and operators.
 *   2. Support unary negative numbers cleanly?
 *      Tokenize first and treat a sign after an operator or '(' as part of the number.
 *   3. Add exponentiation?
 *      Introduce a higher-precedence, right-associative operator level.
 *   4. Evaluate many expressions quickly?
 *      Parse to an AST or bytecode once, then evaluate repeatedly.
 *
 * Related: Basic Calculator (224), Basic Calculator III (772), Evaluate Reverse Polish Notation (150).
 */

public class BasicCalculator2 {

        public static void main(String[] args) {
        BasicCalculator2 solver = new BasicCalculator2();
        String[] inputs = { "3+2*2", " 3/2 ", "14-3/2" };
        int[] expected = { 7, 1, 13 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.calculate(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

    /**
     * Intuition: addition and subtraction can wait because they only affect the
     * final sum, but multiplication and division must immediately modify the
     * previous number. The stack stores signed terms that are safe to add once
     * all high-precedence work has been applied.
     *
     * Algorithm:
     *   1. Return 0 for a null or empty expression.
     *   2. Scan characters, building currentNumber from consecutive digits.
     *   3. When an operator or the end is reached, apply lastOperator to numberStack.
     *   4. Save the current operator as lastOperator and reset currentNumber.
     *   5. Sum numberStack to produce the final value.
     *
     * Time:  O(n) - each character and each stack entry is processed once.
     * Space: O(n) - the stack can hold one signed term per number.
     *
     * @param expression expression containing integers, spaces, '+', '-', '*', and '/'
     * @return evaluated integer result
     */
    public int calculate(String expression) {
        if (expression == null || expression.isEmpty()) {
            return 0; // Handle edge case of empty input
        }

        Stack<Integer> numberStack = new Stack<>();
        int currentNumber = 0;
        char lastOperator = '+'; // Default to addition for first number

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            if (Character.isDigit(currentChar)) {
                // Construct the current number from consecutive digits
                currentNumber = currentNumber * 10 + (currentChar - '0');
            }

            // Process the current operator or if it's the last character
            if ((!Character.isDigit(currentChar) && (currentChar != ' ')) || (i == (expression.length() - 1))) {
                switch (lastOperator) {
                    case '+':
                        numberStack.push(currentNumber);
                        break;
                    case '-':
                        numberStack.push(-currentNumber);
                        break;
                    case '*':
                        numberStack.push(numberStack.pop() * currentNumber);
                        break;
                    case '/':
                        numberStack.push(numberStack.pop() / currentNumber);
                        break;
                }
                lastOperator = currentChar; // Update last operator
                currentNumber = 0; // Reset number
            }
        }

        // Sum up all values in the stack
        int result = 0;
        while (!numberStack.isEmpty()) {
            result += numberStack.pop();
        }

        return result;
    }
}

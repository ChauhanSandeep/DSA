package stacksandqueues.calculator;

import java.util.Stack;

/**
 * Problem: Basic Calculator III
 *
 * Evaluate a valid expression containing non-negative integers, +, -, *, /,
 * parentheses, and spaces. The evaluator must combine normal precedence with
 * recursive evaluation of parenthesized subexpressions.
 *
 * Leetcode: https://leetcode.com/problems/basic-calculator-iii/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Recursion | Operator precedence
 *
 * Example:
 *   Input:  s = "2*(5+5*2)/3+(6/2+8)"
 *   Output: 21
 *   Why:    the expression becomes 2 * 15 / 3 + 11, which is 10 + 11.
 *
 * Follow-ups:
 *   1. Support floating point values?
 *      Parse decimals and use double while defining rounding rules explicitly.
 *   2. Add variables and assignment?
 *      Tokenize identifiers and evaluate against a symbol table or environment map.
 *   3. Avoid substring allocation for parentheses?
 *      Pass a mutable index through recursive calls instead of slicing strings.
 *   4. Support custom operators?
 *      Use a precedence table or shunting-yard parser to make operators configurable.
 *
 * Related: Basic Calculator (224), Basic Calculator II (227), Basic Calculator IV (770).
 */


public class BasicCalculator3 {

        public static void main(String[] args) {
        BasicCalculator3 solver = new BasicCalculator3();
        String[] inputs = { "2*(5+5*2)/3+(6/2+8)", "(1-(3*2+2)-3)+(9+8)" };
        int[] expected = { 21, 7 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.calculate(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: the stack stores signed terms exactly like Basic Calculator II.
     * Parentheses are handled by finding the matching close, recursively reducing
     * that inner expression to one number, and then applying the pending
     * lastOperator to that number.
     *
     * Algorithm:
     *   1. Return 0 for a null or empty expression, then remove whitespace.
     *   2. Parse full numbers and apply them to numberStack with lastOperator.
     *   3. On '(', find its matching ')' and recursively evaluate the inside.
     *   4. Update lastOperator whenever an operator character is seen.
     *   5. Sum numberStack to produce the final result.
     *
     * Time:  O(n^2) - recursive substring calls can rescan/copy nested portions of the expression.
     * Space: O(n) - stack entries and recursive calls grow with expression nesting.
     *
     * @param expression valid arithmetic expression
     * @return evaluated integer result
     */

    public int calculate(String expression) {
        if (expression == null || expression.isEmpty()) {
            return 0; // Handle edge case of empty input
        }

        // Remove all whitespace to simplify processing
        expression = expression.replaceAll("\\s+", "");
        Stack<Integer> numberStack = new Stack<>();

        char lastOperator = '+'; // Default to addition for first number
        int currIndex = 0;

        while (currIndex < expression.length()) {
            char currentChar = expression.charAt(currIndex);

            if (Character.isDigit(currentChar)) {
                // Parse full integer value
                int number = 0;
                while (currIndex < expression.length() && Character.isDigit(expression.charAt(currIndex))) {
                    number = number * 10 + (expression.charAt(currIndex) - '0');
                    currIndex++;
                }
                applyToStack(numberStack, lastOperator, number);
                continue; // Skip incrementing `currIndex` since it's already moved
            }

            if (currentChar == '(') {
                int closingParCurrIndex = findClosingParenthesis(expression, currIndex);
                int evaluatedSubExpression = calculate(expression.substring(currIndex + 1, closingParCurrIndex - 1));
                currIndex = closingParCurrIndex; // Move index to character after closing parenthesis
                applyToStack(numberStack, lastOperator, evaluatedSubExpression);
                continue;
            }

            // If the character is an operator, update lastOperator
            if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                lastOperator = currentChar;
            }

            currIndex++; // Move to the next character
        }

        // Sum up all values in the stack
        int result = 0;
        while (!numberStack.isEmpty()) {
            result += numberStack.pop();
        }
        return result;
    }

        /** Applies one pending operator to the number stack. */

    private void applyToStack(Stack<Integer> stack, char operator, int value) {
        switch (operator) {
            case '+':
                stack.push(value);
                break;
            case '-':
                stack.push(-value);
                break;
            case '*':
                stack.push(stack.pop() * value);
                break;
            case '/':
                stack.push(stack.pop() / value);
                break;
        }
    }

        /** Finds the index just after the matching closing parenthesis. */

    private int findClosingParenthesis(String expression, int startIndex) {
        int parenthesisDepth = 1;
        int index = startIndex + 1;
        while (index < expression.length() && parenthesisDepth > 0) {
            if (expression.charAt(index) == '(') parenthesisDepth++;
            else if (expression.charAt(index) == ')') parenthesisDepth--;
            index++;
        }
        return index;
    }
}

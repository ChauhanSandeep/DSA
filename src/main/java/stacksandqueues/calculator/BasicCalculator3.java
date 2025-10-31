package stacksandqueues.calculator;

import java.util.Stack;

/**
 * Basic Calculator III - LeetCode Problem 772
 *
 * Problem Statement:
 * Implement a basic calculator to evaluate a simple expression string.
 * The expression string contains non-negative integers, +, -, *, /, (, and ).
 * Integer division truncates towards zero. The expression is always valid.
 * All intermediate results are within 32-bit signed integer range.
 *
 * Example:
 * Input: s = "2*(5+5*2)/3+(6/2+8)"
 * Output: 21
 * Explanation: ((2*(5+10))/3) + (3+8) = (2*15)/3 + 11 = 30/3 + 11 = 10 + 11 = 21
 * This combines operator precedence (* and / before + and -) with parentheses handling.
 *
 * LeetCode Link: https://leetcode.com/problems/basic-calculator-iii/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you handle floating point numbers?
 *    Answer: Modify parsing to handle decimal points, use double instead of int.
 *
 * 2. What if we need to support additional operators like ^ (exponentiation)?
 *    Answer: Add new precedence level in recursive descent parser or extend stack approach.
 *    Related: Build expression evaluator with custom operators
 *
 * 3. How would you optimize for repeated evaluation of similar expressions?
 *    Answer: Parse once into AST/bytecode, then evaluate multiple times.
 *
 * 4. What if expressions can have variables (like "2*x + 3*y")?
 *    Answer: Symbol table for variables, modify parser to handle identifiers.
 *    Related: LeetCode 736 - Parse Lisp Expression
 *
 * 5. How would you mplement a version that supports variables and evaluation via a map?
 *    Answer: https://leetcode.com/problems/basic-calculator-iv/
 *
 * 6. How would you handle floating point numbers and operator precedence?
 *    Answer: Consider implementing a Shunting Yard algorithm.
 *
 */

public class BasicCalculator3 {

    public static void main(String[] args) {
        String expression = "(1-(3*2+2)-3)+(9+8)";
        System.out.println("Result: " + new BasicCalculator3().calculate(expression));
    }

    /**
     * Evaluates mathematical expression with +, -, *, /, and parentheses.
     *
     * Algorithm: Stack-based approach with operator precedence handling
     * - Use stack to defer lower precedence operations like +, -
     * - Handle high precedence operations like *, / immediately
     * - Process parentheses recursively by finding matching closing parenthesis
     * - Final result is sum of all stack elements
     *
     * Time Complexity: O(n) - single pass through string
     * Space Complexity: O(n) - stack space for numbers and recursion
     *
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

    /**
     * Applies the given operator to the stack, modifying it in place.
     */
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

    /**
     * Finds the index just after the matching closing parenthesis for the opening parenthesis at startIndex.
     */
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

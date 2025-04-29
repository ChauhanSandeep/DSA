package StackQueue;

import java.util.Stack;

/**
 * Problem: Basic Calculator III
 * LeetCode Link: https://leetcode.com/problems/basic-calculator-iii/
 *
 * Given a mathematical expression containing non-negative integers, '+', '-', '*', '/',
 * and parentheses '(' and ')', evaluate the expression following standard mathematical precedence.
 *
 * Approach:
 * - Use a stack to store values and compute based on the last encountered operator.
 * - Use recursion to evaluate subexpressions enclosed in parentheses.
 * - Handle integer division by truncating toward zero.
 *
 * Time Complexity: O(N), where N is the length of the expression.
 * Space Complexity: O(N), due to recursion depth and stack storage.
 */
public class BasicCalculator3 {

    public static void main(String[] args) {
        String expression = "(1-(3*2+2)-3)+(9+8)";
        System.out.println("Result: " + new BasicCalculator3().calculate(expression));
    }

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
                // Handle subexpression recursively
                int parenthesisDepth = 1;
                int innerIndex = currIndex + 1;
                while (innerIndex < expression.length() && parenthesisDepth > 0) {
                    if (expression.charAt(innerIndex) == '(') parenthesisDepth++;
                    else if (expression.charAt(innerIndex) == ')') parenthesisDepth--;
                    innerIndex++;
                }

                // Recursively evaluate subexpression
                int evaluatedSubExpression = calculate(expression.substring(currIndex + 1, innerIndex - 1));
                currIndex = innerIndex; // Move `currIndex` past the closing parenthesis
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
}

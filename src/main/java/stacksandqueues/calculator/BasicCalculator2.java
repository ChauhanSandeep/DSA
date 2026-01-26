package stacksandqueues.calculator;

import java.util.Stack;

/**
 * Problem: Basic Calculator II
 * LeetCode Link: https://leetcode.com/problems/basic-calculator-ii/
 *
 * Given a string `s` representing a mathematical expression with integers and operators (+, -, *, /),
 * evaluate the expression and return the result. The operators follow standard precedence rules:
 * multiplication and division take precedence over addition and subtraction.
 *
 * Algorithm:
 * - Use a stack to handle operations in the correct order.
 * - Traverse the string while processing numbers and operators.
 * - Apply multiplication and division immediately, while addition and subtraction are deferred.
 *
 * Time Complexity: O(N), where N is the length of the input string.
 * Space Complexity: O(N), for storing numbers in the stack.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class BasicCalculator2 {

    public static void main(String[] args) {
        String expression = "13+2*2";
        System.out.println("Result: " + new BasicCalculator2().calculate(expression));
    }

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

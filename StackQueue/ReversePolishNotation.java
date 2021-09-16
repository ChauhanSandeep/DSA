package StackQueue;

import java.util.Stack;

/**
 * Evaluate the value of an arithmetic expression in Reverse Polish Notation.
 */
public class ReversePolishNotation {

    public static void main(String[] args) {
        String[] arr = {"4", "13", "5", "/", "+"};
        int result = new ReversePolishNotation().evalRPN(arr);
        System.out.println(result);
    }

    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Integer.parseInt(token));
            } else {
                int second = stack.pop();
                int first = stack.pop();
                stack.push(operate(first, second, token));
            }
        }
        return stack.pop();
    }

    public int operate(int first, int second, String operator) {
        switch (operator) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "*":
                return first * second;
            default:
                return first / second;
        }
    }

    public boolean isNumber(String str) {
        return !str.equals("+") && !str.equals("-") && !str.equals("*") && !str.equals("/");
    }
}

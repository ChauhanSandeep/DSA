package StackQueue;

import java.util.Stack;

public class BasicCalculator2 {

    public static void main(String[] args) {
        String str = "13+2*2";
        System.out.println(new BasicCalculator2().calculate(str));
    }

    public int calculate(String str) {
        int len = str.length();
        Stack<Integer> stack = new Stack<>();

        int current = 0;
        int op = '+';
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                current = current * 10 + (c - '0');
            }
            if ((!Character.isDigit(c) && (c != ' ')) || (i == (len - 1))) {
                if (op == '/') {
                    stack.push(stack.pop() / current);
                } else if (op == '*') {
                    stack.push(stack.pop() * current);
                } else if (op == '+') {
                    stack.push(current);
                } else if (op == '-') {
                    stack.push(-1 * current);
                }
                current = 0;
                op = c;
            }
        }

        int result = 0;
        while (!stack.isEmpty()) {
            result += stack.pop();
        }
        return result;
    }
}

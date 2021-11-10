package StackQueue;

import java.util.Stack;

/**
 * The string contains only non-negative integers, '+', '-', '*', '/',
 * and open '(' and closing parentheses ')'.
 * The integer division should truncate toward zero.
 */
public class BasicCalculator3 {

    public static void main(String[] args) {
        String str = "(1-(3*2+2)-3)+(9+8)";
        System.out.println(new BasicCalculator3().calculate(str));
    }

    public int calculate(String str) {
        str = str.replaceAll("\\s+", "");
        Stack<Integer> stack = new Stack<>();

        char op = '+';
        for(int i = 0 ; i < str.length();) {
            char c = str.charAt(i);
            if (c == '(') {
                // find the block and use the recursive to solve
                int level = 1;
                int j = i+1;
                while (j < str.length() && level > 0) {
                    if(str.charAt(j) == '(') level ++;
                    else if(str.charAt(j) == ')') level --;
                    j++;
                }
                int blockValue = calculate(str.substring(i + 1, j-1));
                i = j;
                if (op == '+') {
                    stack.push(blockValue);
                } else if (op == '-') {
                    stack.push(-blockValue);
                } else if (op == '*') {
                    stack.push(stack.pop() * blockValue);
                } else if (op == '/') {
                    stack.push(stack.pop() / blockValue);
                }
            } else if (Character.isDigit(c)) {
                int j = i;
                int value = 0;
                while (j < str.length() && Character.isDigit(str.charAt(j))) {
                    value = 10 * value + (str.charAt(j) - '0');
                    j++;
                }
                i = j;
                if (op == '+') {
                    stack.push(value);
                } else if (op == '-') {
                    stack.push(-value);
                } else if (op == '*') {
                    stack.push(stack.pop() * value);
                } else if (op == '/') {
                    stack.push(stack.pop() / value);
                }
            } else {
                op = c;
                i++;
            }
        }
        int result = 0;
        while (!stack.isEmpty()) {
            result += stack.pop();
        }
        return result;
    }

}

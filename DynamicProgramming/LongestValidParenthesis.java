package DynamicProgramming;

import java.util.Stack;

/**
 * Return the length of the longest valid (well-formed) parentheses substring.
 */
public class LongestValidParenthesis {
    public static void main(String[] args) {
        LongestValidParenthesis lvp = new LongestValidParenthesis();
        System.out.println(lvp.longestValidParentheses("(()"));
        System.out.println(lvp.longestValidParentheses("(())"));
        System.out.println(lvp.longestValidParentheses("))()()(()()()"));
    }

    public int longestValidParentheses(String str) {
        int size = str.length();
        int result = 0;
        Stack<Integer> stack = new Stack<>();

        for(int i=0; i<size; i++) {
            char c = str.charAt(i);
            if(!stack.isEmpty() && c == ')' && str.charAt(stack.peek()) == '(') {
                stack.pop();
                int tentative;
                if(stack.isEmpty()) tentative = i + 1;
                else tentative = i - stack.peek();
                result = Math.max(result, tentative);
            }else {
                stack.push(i);
            }
        }
        return result;
    }
}

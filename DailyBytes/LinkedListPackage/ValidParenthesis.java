package DailyBytes.LinkedListPackage;
import java.util.*;

public class ValidParenthesis {

    public static void main(String[] args) {
        System.out.println("is valid parenthesis ? " + isValidParenthesis("(){}[]"));
        System.out.println("is valid parenthesis ? " + isValidParenthesis("(({[]}))"));
        System.out.println("is valid parenthesis ? " + isValidParenthesis("{(})"));

    }

    private static boolean isValidParenthesis(String str) {
        Stack<Character> stack = new Stack<>();
        for(char c: str.toCharArray()) {
            switch(c) {
                case '(':
                    stack.push('(');
                    break;
                case '{':
                    stack.push('{');
                    break;
                case '[':
                    stack.push('[');
                    break;
                case ')':
                    if(stack.isEmpty() || stack.peek() != '(') return false;
                    stack.pop();
                    break;
                case '}':
                    if(stack.isEmpty() || stack.peek() != '{') return false;
                    stack.pop();
                    break;
                case ']':
                    if(stack.isEmpty() || stack.peek() != '[') return false;
                    stack.pop();
                    break;
            }
        }
        return stack.isEmpty();

    }
}

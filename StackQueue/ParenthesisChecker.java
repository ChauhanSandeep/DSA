package StackQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ParenthesisChecker {
    public static void main(String[] args) {
        String str = "{([])}";
        System.out.println("Valid parenthesis? " + isValidParenthesis(str));
    }

    public static boolean isValidParenthesis(String str) {
        Map<Character, Character> map = new HashMap<>();
        map.put('}', '{');
        map.put(')', '(');
        map.put(']', '[');
        map.put('>', '<');

        Stack<Character> stack = new Stack<>();
        for(Character c: str.toCharArray()) {
            if(map.containsKey(c)) {
                if(stack.isEmpty() || stack.peek() != map.get(c)) return false;
                stack.pop();
            }else{
                stack.push(c);
            }
        }
        return stack.isEmpty();

    }
}

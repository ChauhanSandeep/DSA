package DailyBytes.LinkedListPackage;

import java.util.Stack;

/**
 * This class contains a method to determine if a given string of parentheses is valid.
 * 
 * Algorithm:
 * - Use a stack to keep track of opening parentheses.
 * - For each closing parenthesis, check if it matches the top of the stack.
 * - Time Complexity: O(n)
 * - Space Complexity: O(n)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/valid-parentheses/
 */
public class ValidParenthesis {

    public static void main(String[] args) {
        System.out.println("Is valid parenthesis? " + isValidParenthesis("(){}[]"));
        System.out.println("Is valid parenthesis? " + isValidParenthesis("(({[]}))"));
        System.out.println("Is valid parenthesis? " + isValidParenthesis("{(})"));
    }

    /**
     * Determines if the input string of parentheses is valid.
     * @param str The input string containing parentheses.
     * @return True if the string is valid, false otherwise.
     */
    private static boolean isValidParenthesis(String str) {
        Stack<Character> stack = new Stack<>();

        for (char c : str.toCharArray()) {
            switch (c) {
                case '(':
                case '{':
                case '[':
                    stack.push(c);
                    break;
                case ')':
                    if (stack.isEmpty() || stack.peek() != '(') return false;
                    stack.pop();
                    break;
                case '}':
                    if (stack.isEmpty() || stack.peek() != '{') return false;
                    stack.pop();
                    break;
                case ']':
                    if (stack.isEmpty() || stack.peek() != '
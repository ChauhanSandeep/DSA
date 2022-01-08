package Backtracking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Remove min number of parenthesis from string to make parenthesis balanced
 */
public class RemoveParenthesis {

    public static void main(String[] args) {
        String str = "(a)())()";
        List<String> result = new RemoveParenthesis().removeInvalidParentheses(str);
        System.out.println(result);
    }

    public List<String> removeInvalidParentheses(String str) {
        List<String> result = new ArrayList<>();
        if (str == null) return result;

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(str);
        visited.add(str);
        boolean found = false;

        while (!queue.isEmpty()) {
            String curr = queue.poll();

            if (isValid(curr)) {
                result.add(curr);
                found = true;
            }

            if (found) continue;

            for (int i = 0; i < curr.length(); i++) {
                if (curr.charAt(i) != '(' && curr.charAt(i) != ')') continue;

                String temp = curr.substring(0, i) + curr.substring(i + 1);
                if (!visited.contains(temp)) {
                    queue.add(temp);
                    visited.add(temp);
                }
            }
        }

        return result;
    }

    boolean isValid(String str) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') count++;
            if (c == ')' && count-- == 0) return false;
        }

        return count == 0;
    }

}

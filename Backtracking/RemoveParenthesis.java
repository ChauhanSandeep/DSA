package Backtracking;

import java.util.*;

/**
 * LeetCode: https://leetcode.com/problems/remove-invalid-parentheses/
 *
 * Problem:
 * Given a string containing parentheses and characters, remove the **minimum number**
 * of invalid parentheses to make the expression valid. Return all possible results.
 *
 * Approach:
 * - We use **BFS (Breadth-First Search)** to explore all possible valid expressions.
 * - At each step, we remove **one** parenthesis and check if the result is valid.
 * - The first valid expression found will be of **minimum edits** (shortest distance).
 *
 * Time Complexity: **O(2^N)** → Worst case, we generate all subsets of parentheses.
 * Space Complexity: **O(2^N)** → Storing results & visited states.
 */
public class RemoveParenthesis {

    public static void main(String[] args) {
        String input = "(a)())()";
        List<String> validExpressions = removeInvalidParentheses(input);
        System.out.println(validExpressions);
    }

    /**
     * Finds all valid expressions by removing the fewest invalid parentheses.
     *
     * @param str The input string containing parentheses.
     * @return A list of all possible valid expressions.
     */
    public static List<String> removeInvalidParentheses(String str) {
        List<String> validResults = new ArrayList<>();
        if (str == null) return validResults;

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(str);
        visited.add(str);
        boolean foundValidExpression = false;

        while (!queue.isEmpty()) {
            String currentExpression = queue.poll();

            if (isBalancedParentheses(currentExpression)) {
                validResults.add(currentExpression);
                foundValidExpression = true;
            }

            // If we've found valid expressions, we stop processing further removals
            if (foundValidExpression) continue;

            // Generate all possible strings by removing one parenthesis at a time
            for (int i = 0; i < currentExpression.length(); i++) {
                char currentChar = currentExpression.charAt(i);
                if (currentChar != '(' && currentChar != ')') continue;

                String nextCandidate = currentExpression.substring(0, i) + currentExpression.substring(i + 1);
                if (!visited.contains(nextCandidate)) {
                    queue.add(nextCandidate);
                    visited.add(nextCandidate);
                }
            }
        }
        return validResults;
    }

    /**
     * Checks if a string contains a balanced set of parentheses.
     *
     * @param str The string to check.
     * @return True if balanced, otherwise False.
     */
    private static boolean isBalancedParentheses(String str) {
        int openCount = 0;
        for (char c : str.toCharArray()) {
            if (c == '(') openCount++;
            if (c == ')') {
                if (openCount == 0) return false;
                openCount--;
            }
        }
        return openCount == 0;
    }
}

package Backtracking;

import java.util.*;

/**
 * LeetCode: https://leetcode.com/problems/remove-invalid-parentheses/
 *
 * Problem:
 * Given a string containing parentheses and characters, remove the **minimum number**
 * of invalid parentheses to make the expression valid. Return all possible results.
 *
 * Example
 *
 * Input: s = "()())()"
 * Output: ["(())()","()()()"]
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

    private Set<String> validExpressions = new HashSet<>();

    /**
     * Main method to be called to remove invalid parentheses.
     */
    public List<String> removeInvalidParenthesesImproved(String input) {
        int misplacedOpen = 0, misplacedClose = 0;

        // Calculate how many '(' and ')' are misplaced
        for (char ch : input.toCharArray()) {
            if (ch == '(') {
                misplacedOpen++;
            } else if (ch == ')') {
                if (misplacedOpen > 0) {
                    misplacedOpen--; // Match a previous '('
                } else {
                    misplacedClose++; // No matching '(' found
                }
            }
        }

        // Start DFS traversal to generate valid expressions
        dfs(input, 0, 0, 0, misplacedOpen, misplacedClose, new StringBuilder());

        return new ArrayList<>(validExpressions);
    }

    /**
     * Recursive DFS to try out all combinations by removing misplaced parentheses.
     *
     * @param input            Original string
     * @param index            Current index in the string
     * @param openCount        Count of open '(' in the current path
     * @param closeCount       Count of close ')' in the current path
     * @param openToRemove     Number of '(' left to remove
     * @param closeToRemove    Number of ')' left to remove
     * @param currentExpression Current state of the expression being built
     */
    private void dfs(String input, int index, int openCount, int closeCount,
        int openToRemove, int closeToRemove, StringBuilder currentExpression) {

        // If we've processed all characters
        if (index == input.length()) {
            if (openToRemove == 0 && closeToRemove == 0) {
                validExpressions.add(currentExpression.toString());
            }
            return;
        }

        char currentChar = input.charAt(index);
        int currentLength = currentExpression.length();

        // Option 1: Discard current character if it's a parenthesis and can be removed
        if (currentChar == '(' && openToRemove > 0) {
            dfs(input, index + 1, openCount, closeCount,
                openToRemove - 1, closeToRemove, currentExpression);
        } else if (currentChar == ')' && closeToRemove > 0) {
            dfs(input, index + 1, openCount, closeCount,
                openToRemove, closeToRemove - 1, currentExpression);
        }

        // Option 2: Include current character
        currentExpression.append(currentChar);

        if (currentChar != '(' && currentChar != ')') {
            // Just add the character and move on
            dfs(input, index + 1, openCount, closeCount, openToRemove, closeToRemove, currentExpression);

        } else if (currentChar == '(') {
            // Include it and increase open count
            dfs(input, index + 1, openCount + 1, closeCount, openToRemove, closeToRemove, currentExpression);

        } else if (currentChar == ')' && closeCount < openCount) {
            // Include it only if we have more '(' to balance
            dfs(input, index + 1, openCount, closeCount + 1, openToRemove, closeToRemove, currentExpression);
        }

        // Backtrack to previous state
        currentExpression.deleteCharAt(currentLength);
    }
}

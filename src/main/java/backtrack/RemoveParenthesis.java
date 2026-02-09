package backtrack;

import java.util.*;

/**
 * LeetCode: https://leetcode.com/problems/remove-invalid-parentheses/
 *
 * Problem:
 * Given a string containing parentheses and characters, remove the minimum number
 * of invalid parentheses to make the expression valid. Return all possible results.
 *
 * Example
 *
 * Input: s = "()())()"
 * Output: ["(())()","()()()"]
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RemoveParenthesis {

    public static void main(String[] args) {
        String input = "(a)())()";
        List<String> validExpressions = removeInvalidParentheses(input);
        System.out.println(validExpressions);
    }

    /**
     * Finds all valid expressions by removing the fewest invalid parentheses.
     * Steps:
     * 1. Use BFS to explore all possible strings by removing one parenthesis at a time.
     * 2. Check if the current string is valid (balanced parentheses).
     * 3. If valid, add to results and mark that we found a valid expression
     * 4. If valid expression found at current level, do not explore further levels.
     * 5. Use a set to avoid processing the same string multiple times.
     * 
     * Time complexity: O(2^N) in the worst case.
     *      Because for each character, we have two choices: include it or exclude it.
     * Space complexity: O(2^N) for storing results and visited states.
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
     * DFS based main method to be called to remove invalid parentheses.
     * 
     * Steps:
     * 1. Calculate the number of misplaced '(' and ')' parentheses.
     * 2. Use DFS to explore all possible strings by either including or excluding each character.
     * 3. Only include a ')' if it does not exceed the number of '(' included
     * 4. When the end of the string is reached, check if all misplaced parentheses have been removed.
     5. If valid, add to results.
     6. Use backtracking to explore other possibilities.
     7. Return all valid expressions found.
     * 
     * Time complexity: O(2^N) in the worst case.
     *     Because for each character, we have two choices: include it or exclude it.
     * Space complexity: O(2^N) for storing results and recursion stack.
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

package backtrack;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Problem: Remove Invalid Parentheses
 *
 * Given a string containing parentheses and lowercase letters, remove the
 * minimum number of invalid parentheses so every returned string is balanced.
 * Return all possible results after that minimum number of removals.
 *
 * Leetcode: https://leetcode.com/problems/remove-invalid-parentheses/
 * Rating:   acceptance 50.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Minimum removals | DFS with removal quotas
 *
 * Example:
 *   Input:  "(a)())()"
 *   Output: ["(a())()", "(a)()()"]
 *   Why:    removing one extra closing parenthesis is enough, and the two
 *           different removals produce exactly these balanced strings.
 *
 * Follow-ups:
 *   1. Return one valid string with minimum removals?
 *      BFS can stop at the first valid node, or DFS can stop after the first quota-valid leaf.
 *   2. Count all minimum-removal outputs without storing them all?
 *      Use DFS with quotas and increment a counter for unique leaves.
 *   3. Handle multiple bracket types like (), [], {}?
 *      Validity requires a stack, and DFS state must track unmatched opens by type.
 *   4. Minimize weighted removal cost instead of number of removals?
 *      Use shortest-path search over strings or DP with costs, not plain BFS by depth.
 *
 * Related: Valid Parentheses (20), Minimum Remove to Make Valid Parentheses (1249).
 *
 *   Approach               Method                        Time    Space (extra)
 *   ---------------------  ----------------------------  ------  -------------
 *   BFS by removal level   removeInvalidParenthesesBFS   O(2^n)  O(2^n)
 *   DFS with quotas        removeInvalidParentheses      O(2^n)  O(2^n)
 */
public class RemoveInvalidParenthesis {

    /**
     * Intuition: removing fewer parentheses is better, so BFS is a natural fit.
     * Level 0 is the original string, level 1 removes one parenthesis, level 2
     * removes two, and so on. The first level containing any valid expression must
     * therefore be the minimum-removal level. Once that level is found, we collect
     * its valid strings but do not go deeper.
     *
     * Algorithm:
     *   1. Return an empty answer for null input.
     *   2. Start BFS from the original string and remember visited strings to avoid repeats.
     *   3. For each string, add it to the answer if its parentheses are balanced.
     *   4. If a valid string has been found at this level, skip generating children
     *      so deeper, non-minimal removals are not explored.
     *   5. Otherwise, remove each parenthesis position once to create the next level of candidates.
     *
     * Time:  O(2^n) - each parenthesis may be kept or removed across the search tree.
     * Space: O(2^n) for the queue, visited set, and results.
     *
     * @param str input string containing parentheses and letters
     * @return all balanced strings after the minimum number of removals
     */
    public static List<String> removeInvalidParenthesesBFS(String str) {
        List<String> validExpressions = new ArrayList<>();
        if (str == null) return validExpressions;

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.add(str);
        visited.add(str);

        boolean foundValidLevel = false;
        while (!queue.isEmpty()) {
            String currentExpression = queue.remove();
            if (isBalancedParentheses(currentExpression)) {
                validExpressions.add(currentExpression);
                foundValidLevel = true;
            }

            if (foundValidLevel) continue;

            for (int i = 0; i < currentExpression.length(); i++) {
                char currentChar = currentExpression.charAt(i);
                if (currentChar != '(' && currentChar != ')') continue;

                String nextExpression = currentExpression.substring(0, i) + currentExpression.substring(i + 1);
                if (visited.add(nextExpression)) queue.add(nextExpression);
            }
        }
        return validExpressions;
    }

    /** Returns true when parentheses are balanced, ignoring letters. */
    private static boolean isBalancedParentheses(String str) {
        int openCount = 0;
        for (char ch : str.toCharArray()) {
            if (ch == '(') {
                openCount++;
            } else if (ch == ')') {
                if (openCount == 0) return false;
                openCount--;
            }
        }
        return openCount == 0;
    }

    /**
     * Intuition (interview default): first determine the exact number of '(' and
     * ')' characters that must be removed. Then DFS does not have to guess how
     * many removals are enough; it only explores strings that spend those quotas.
     * While scanning left to right, letters are always kept, '(' can be kept or
     * removed, and ')' can be kept only when there is an unmatched '(' before it.
     * That prefix rule guarantees every completed string is balanced.
     *
     * Algorithm:
     *   1. Return an empty answer for null input and count how many open and close parentheses must be removed.
     *   2. DFS through the string with the current expression, kept open/close counts, and remaining removal quotas.
     *   3. When a parenthesis still has removal quota, branch once by deleting it.
     *   4. Keep letters automatically, keep '(' by increasing open count, and keep
     *      ')' only when it would not make close count exceed open count.
     *   5. At the end, record the built string only if both removal quotas are zero.
     *
     * Time:  O(2^n) - each removable parenthesis can branch into keep or delete choices.
     * Space: O(2^n) for unique results plus O(n) recursion depth.
     *
     * @param input input string containing parentheses and letters
     * @return all balanced strings after the minimum number of removals
     */
    public static List<String> removeInvalidParentheses(String input) {
        List<String> validExpressions = new ArrayList<>();
        if (input == null) return validExpressions;

        int openToRemove = 0;
        int closeToRemove = 0;
        for (char ch : input.toCharArray()) {
            if (ch == '(') {
                openToRemove++;
            } else if (ch == ')') {
                if (openToRemove > 0) {
                    openToRemove--;
                } else {
                    closeToRemove++;
                }
            }
        }

        Set<String> uniqueExpressions = new LinkedHashSet<>();
        dfs(input, 0, 0, 0, openToRemove, closeToRemove, new StringBuilder(), uniqueExpressions);
        validExpressions.addAll(uniqueExpressions);
        return validExpressions;
    }

    /** Explores keep/delete choices using the exact number of parentheses that must be removed. */
    private static void dfs(String input, int index, int openCount, int closeCount,
                            int openToRemove, int closeToRemove,
                            StringBuilder currentExpression, Set<String> validExpressions) {
        if (index == input.length()) {
            if (openToRemove == 0 && closeToRemove == 0) validExpressions.add(currentExpression.toString());
            return;
        }

        char currentChar = input.charAt(index);
        int currentLength = currentExpression.length();

        if (currentChar == '(' && openToRemove > 0) {
            dfs(input, index + 1, openCount, closeCount, openToRemove - 1,
                closeToRemove, currentExpression, validExpressions);
        } else if (currentChar == ')' && closeToRemove > 0) {
            dfs(input, index + 1, openCount, closeCount, openToRemove,
                closeToRemove - 1, currentExpression, validExpressions);
        }

        currentExpression.append(currentChar);
        if (currentChar != '(' && currentChar != ')') {
            dfs(input, index + 1, openCount, closeCount, openToRemove,
                closeToRemove, currentExpression, validExpressions);
        } else if (currentChar == '(') {
            dfs(input, index + 1, openCount + 1, closeCount, openToRemove,
                closeToRemove, currentExpression, validExpressions);
        } else if (closeCount < openCount) {
            dfs(input, index + 1, openCount, closeCount + 1, openToRemove,
                closeToRemove, currentExpression, validExpressions);
        }
        currentExpression.setLength(currentLength);
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        String[] inputs = {"(a)())()", "()())()", ")("};
        String[] expected = {
            "[(a())(), (a)()()]",
            "[(())(), ()()()]",
            "[]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<String> got = removeInvalidParentheses(inputs[i]);
            System.out.printf("s=%s  ->  %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }
}

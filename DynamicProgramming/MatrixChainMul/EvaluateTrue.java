package DynamicProgramming.MatrixChainMul;

import java.util.Arrays;

/**
 * LeetCode Problem: Boolean Parenthesization (Similar to https://leetcode.com/problems/parsing-a-boolean-expression/)
 *
 * Given a boolean expression containing 'T' (true), 'F' (false), and operators '&' (AND), '|' (OR), and '^' (XOR),
 * this program calculates the number of ways to parenthesize the expression so that it evaluates to true.
 *
 * Approach:
 * - Uses memoized recursion (top-down dynamic programming) to optimize overlapping subproblems.
 * - The `dp` array stores precomputed results to avoid redundant calculations.
 *
 * Time Complexity: O(N^3) (Since each subproblem takes O(N) and there are O(N^2) subproblems)
 * Space Complexity: O(N^2) (For memoization storage)
 */
public class EvaluateTrue {
    public static void main(String[] args) {
        String expression = "F&F^T^F";
        EvaluateTrue evaluator = new EvaluateTrue();
        int waysToEvaluateTrue = evaluator.countWaysToEvaluateTrue(expression);
        System.out.println("Number of ways to evaluate to true: " + waysToEvaluateTrue);
    }

    /**
     * Calculates the number of ways to parenthesize the boolean expression so that it evaluates to true.
     *
     * @param expression Boolean expression containing 'T', 'F', '&', '|', and '^'.
     * @return Number of ways to evaluate the expression to true.
     */
    public int countWaysToEvaluateTrue(String expression) {
        int length = expression.length();
        int[][][] dp = new int[length][length][2]; // Memoization table

        // Initialize memoization table with -1 (indicating uncomputed states)
        for (int[][] subTable : dp) {
            for (int[] row : subTable) {
                Arrays.fill(row, -1);
            }
        }

        return countWays(expression, 0, length - 1, true, dp);
    }

    /**
     * Recursive function with memoization to count the number of ways to evaluate the expression.
     *
     * @param expression The boolean expression.
     * @param left       Start index of the expression segment.
     * @param right      End index of the expression segment.
     * @param evaluateTo True if evaluating for true cases, false otherwise.
     * @param dp         Memoization table to store results.
     * @return Number of ways the expression evaluates to the desired value.
     */
    private int countWays(String expression, int left, int right, boolean evaluateTo, int[][][] dp) {
        if (left > right) return 0;

        // Base case: single character (either 'T' or 'F')
        if (left == right) {
            if (evaluateTo) return expression.charAt(left) == 'T' ? 1 : 0;
            return expression.charAt(left) == 'F' ? 1 : 0;
        }

        int isTrue = evaluateTo ? 1 : 0;
        if (dp[left][right][isTrue] != -1) return dp[left][right][isTrue];

        int ways = 0;

        // Iterate over operators (odd indices)
        for (int operatorIndex = left + 1; operatorIndex < right; operatorIndex += 2) {
            char operator = expression.charAt(operatorIndex);

            // Compute left and right segment counts for both true and false cases
            int leftTrue = countWays(expression, left, operatorIndex - 1, true, dp);
            int leftFalse = countWays(expression, left, operatorIndex - 1, false, dp);
            int rightTrue = countWays(expression, operatorIndex + 1, right, true, dp);
            int rightFalse = countWays(expression, operatorIndex + 1, right, false, dp);

            // Compute the number of ways based on the operator
            switch (operator) {
                case '|': // OR Operator
                    if (evaluateTo) {
                        ways += (leftTrue * rightTrue) + (leftTrue * rightFalse) + (leftFalse * rightTrue);
                    } else {
                        ways += leftFalse * rightFalse;
                    }
                    break;

                case '&': // AND Operator
                    if (evaluateTo) {
                        ways += leftTrue * rightTrue;
                    } else {
                        ways += (leftTrue * rightFalse) + (leftFalse * rightTrue) + (leftFalse * rightFalse);
                    }
                    break;

                case '^': // XOR Operator
                    if (evaluateTo) {
                        ways += (leftTrue * rightFalse) + (leftFalse * rightTrue);
                    } else {
                        ways += (leftTrue * rightTrue) + (leftFalse * rightFalse);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        }

        dp[left][right][isTrue] = ways;
        return ways;
    }
}
package DynamicProgramming.MatrixChainMul;

import java.util.Arrays;

/**
 * LeetCode Problem: Boolean Parenthesization (Similar to https://leetcode.com/problems/parsing-a-boolean-expression/)
 *
 * Given a boolean expression containing 'T' (true), 'F' (false), and operators '&' (AND), '|' (OR), and '^' (XOR),
 * this program calculates the number of ways to parenthesize the expression so that it evaluates to true.
 * For example:
 * - Input: "T|F&T^F"
 * - Output: 4
 * - Explanation: The expression can be parenthesized in 4 different ways to evaluate to true.
 * - Parenthesizations: 1) ((T|F)&T^F), 2) (T|(F&T^F)), 3) (T|((F&T)^F)), 4) ((T|F)&(T^F))
 *
 * Approach:
 * - The expression is evaluated using a recursive function with memoization.
 * - Each operator is processed separately, and the number of ways to evaluate the left and right segments
 *   are combined based on the operator's behavior.
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
        int[][][] dp = new int[length][length][2]; // Here dp[i][j][0] = false, dp[i][j][1] = true; in the range i to j

        // Initialize memoization table with -1 (indicating un computed states)
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
            if (evaluateTo) {
                // If we want to evaluate to true, return 1 if it's 'T', else 0
              return expression.charAt(left) == 'T' ? 1 : 0;
            }
            // If we want to evaluate to false, return 1 if it's 'F', else 0
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
                        // True if either side is true
                        ways += (leftTrue * rightTrue) + (leftTrue * rightFalse) + (leftFalse * rightTrue);
                    } else {
                        // False only if both sides are false
                        ways += leftFalse * rightFalse;
                    }
                    break;

                case '&': // AND Operator
                    if (evaluateTo) {
                        // True only if both sides are true
                        ways += leftTrue * rightTrue;
                    } else {
                        // False if either side is false
                        ways += (leftTrue * rightFalse) + (leftFalse * rightTrue) + (leftFalse * rightFalse);
                    }
                    break;

                case '^': // XOR Operator
                    if (evaluateTo) {
                        // True if one side is true and the other is false
                        ways += (leftTrue * rightFalse) + (leftFalse * rightTrue);
                    } else {
                        // False if both sides are true or both sides are false
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
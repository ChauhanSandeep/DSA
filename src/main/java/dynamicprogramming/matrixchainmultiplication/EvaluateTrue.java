package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Problem: Boolean Parenthesization
 *
 * Given an expression made of T, F, and the binary operators &, |, and ^, count
 * how many ways to add parentheses so the whole expression evaluates to true.
 * The order of operands and operators must stay unchanged.
 *
 * Source: GeeksForGeeks Boolean Parenthesization
 * Pattern:  Dynamic Programming | Interval DP | Count true and false outcomes
 *
 * Example:
 *   Input:  expression = "T|F&T"
 *   Output: 2
 *   Why:    (T|F)&T and T|(F&T) both evaluate to true, so both valid binary
 *           parenthesizations count.
 *
 * Follow-ups:
 *   1. What if the count can be huge?
 *      Return counts modulo a given value and apply the modulus after every multiplication and addition.
 *   2. Can this be written bottom-up?
 *      Yes; fill true/false counts for increasing expression lengths and split on every operator.
 *   3. What if operators include implication or equivalence?
 *      Keep the same interval state and add a truth-table combination rule for each new operator.
 *
 * Related: Different Ways to Add Parentheses (241), Parsing A Boolean Expression (1106).
 */
public class EvaluateTrue {

        /**
     * Intuition: each operator splits the expression into left and right parts.
     * A parenthesization is counted by combining how many ways each side becomes
     * true or false, according to the operator truth table.
     *
     * Algorithm:
     *   1. Use interval DP with a boolean target: true or false.
     *   2. For every operator position, solve left and right intervals for both outcomes.
     *   3. Add combinations that make the requested outcome.
     *   4. Memoize by left index, right index, and desired truth value.
     *
     * Time:  O(n^3) - intervals try every operator split and truth combination.
     * Space: O(n^2) - memo states for true and false outcomes.
     *
     * @param expression boolean expression with T, F, &, |, and ^
     * @return number of ways to parenthesize expression to true
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

        /** Counts parenthesizations for an interval and desired truth value. */
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


    public static void main(String[] args) {
        EvaluateTrue solver = new EvaluateTrue();
        String[] inputs = {"T", "T|F&T", "F&F^T^F"};
        int[] expected = {1, 2, 2};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.countWaysToEvaluateTrue(inputs[i]);
            System.out.printf("expression=%s  ->  %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }

}
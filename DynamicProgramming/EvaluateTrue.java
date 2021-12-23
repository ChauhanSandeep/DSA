package DynamicProgramming;

import java.util.Arrays;

/**
 * Find the max number of ways to put parenthesis in boolean expression to evaluate to true
 */
public class EvaluateTrue {
    public static void main(String[] args) {
        System.out.println(new EvaluateTrue().cnttrue("F&F^T^F"));
    }

    public int cnttrue(String str) {
        int size = str.length();
        int[][][] dp = new int[size + 1][size + 1][2];

        for (int[][] matrix : dp) {
            for (int[] row : matrix) {
                Arrays.fill(row, -1);
            }
        }

        return countRec(str, 0, size - 1, 1, dp);
    }

    private int countRec(String str, int left, int right, int isTrue, int[][][] dp) {
        if (left > right) return 0;
        if (left == right) {
            char curr = str.charAt(left);
            if (isTrue == 1) return curr == 'T' ? 1 : 0;
            return curr == 'F' ? 1 : 0;
        }
        if (dp[left][right][isTrue] != -1) return dp[left][right][isTrue];

        int count = 0;
        for (int k = left + 1; k <= right - 1; k = k + 2) {
            int leftTrue = countRec(str, left, k - 1, 1, dp);
            int leftFalse = countRec(str, left, k - 1, 0, dp);
            int rightTrue = countRec(str, k + 1, right, 1, dp);
            int rightFalse = countRec(str, k + 1, right, 0, dp);

            char operator = str.charAt(k);
            switch (operator) {
                case '|':
                    if (isTrue == 1)
                        count += (leftTrue * rightTrue) + (leftTrue * rightFalse) + (leftFalse * rightTrue);
                    else
                        count += leftFalse * rightFalse;
                    break;
                case '&':
                    if (isTrue == 1)
                        count += (leftTrue * rightTrue);
                    else
                        count += (leftTrue * rightFalse) + (leftFalse * rightTrue) + (leftFalse * rightFalse);
                    break;
                case '^':
                    if (isTrue == 1)
                        count += (leftTrue * rightFalse) + (leftFalse * rightTrue);
                    else
                        count += (leftTrue * rightTrue) + (leftFalse * rightFalse);
                    break;
                default:
                    throw new RuntimeException("Invalid operator");
            }
        }
        dp[left][right][isTrue] = count;
        System.out.println(str.substring(left, right + 1) + " " + (isTrue == 1 ? "true" : "false") + ": " + count);
        return count;
    }
}

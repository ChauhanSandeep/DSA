package BinarySearch;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/guess-number-higher-or-lower-ii/
 */
public class GuessingGame2 {

    public static void main(String[] args) {
        System.out.println(new GuessingGame2().getMoneyAmount(10));
    }

    public int getMoneyAmount(int n) {
        int[][] dp = new int[n + 1][n + 1];

        for (int len = 2; len <= n; len++) { // Length of range
            for (int left = 1; left <= n - len + 1; left++) {
                int right = left + len - 1;
                dp[left][right] = Integer.MAX_VALUE;

                for (int pivot = left + (right - left) / 2; pivot <= right; pivot++) {
                    int cost = pivot + Math.max(
                        (pivot > left) ? dp[left][pivot - 1] : 0, 
                        (pivot < right) ? dp[pivot + 1][right] : 0
                    );
                    dp[left][right] = Math.min(dp[left][right], cost);
                }
            }
        }

        return dp[1][n];
    }
}

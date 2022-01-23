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
        int[][] dp = new int[n+1][n+1];
        for(int[] row: dp) {
            Arrays.fill(row, -1);
        }
        return getMoney(1, n, dp);
    }

    public int getMoney(int left, int right, int[][] dp) {
        if(left >= right) return 0;
        if(dp[left][right] != -1) return dp[left][right];

        int min = Integer.MAX_VALUE;

        for(int i=left; i<=right; i++) {
            int ls = getMoney(left, i-1, dp);
            int rs = getMoney(i+1, right, dp);
            min = Math.min(i + Math.max(ls, rs) , min);
        }
        dp[left][right] = min;
        return dp[left][right];
    }
}

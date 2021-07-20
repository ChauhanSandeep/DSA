package DynamicProgramming;

import java.util.Arrays;

/**
 * Minimum coins required to reach a target amount
 */
public class  CoinChange {
    public static void main(String[] args) {
        int[] coins = {2, 3};
        int target = 7;
        System.out.printf("Min coins required to reach sum %s is %s%n", target, minCoins(coins, target));
    }

    public static int minCoins(int[] coins, int target) {
        if(coins == null || coins.length == 0) return -1;

        int[] dp = new int[target + 1];
        Arrays.fill(dp, target + 1);
        dp[0] = 0;

        for(int i=1; i<dp.length; i++) {
            for(int j=0; j<coins.length; j++) {
                if(i == coins[j]){
                    dp[i] = 1;
                } else if(i - coins[j] >= 0) {
                    if(dp[i - coins[j]] + 1 < dp[i]){
                        dp[i] = dp[i - coins[j]] + 1;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(dp));
        return dp[dp.length - 1] > target ? -1 : dp[dp.length - 1];
    }
}
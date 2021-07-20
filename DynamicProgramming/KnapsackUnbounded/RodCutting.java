package DynamicProgramming.KnapsackUnbounded;

import java.util.Arrays;

/**
 * Given a rod and an array that contains the prices of all the pieces smaller than length,
 * determine the maximum profit you could obtain from cutting up the rod and selling its pieces.
 */
public class RodCutting {
    public static void main(String[] args) {
        int[] length = { 1, 2, 3, 4 };
        int[] prices = { 1, 5, 8, 9};
        int maxProfit = cutRod(prices, length, prices.length);
        System.out.println(maxProfit);
    }

    public static int cutRod(int[] prices, int[] length,  int size) {
        int[][] dp = new int[size+1][size+1];

        for(int i=1; i<size+1; i++) {
            for(int j=1; j<size+1; j++) {
                if(length[i-1] <= j){
                    dp[i][j] = Math.max(prices[i-1] + dp[i][j-length[i-1]], dp[i-1][j]);
//                  dp[i][j] = Math.max(prices[i-1] + dp[i-1][j-length[i-1]], dp[i-1][j]); <-- 0/1 knapsack
//                  only diff with 01 knapsack is in unbounded we can include the taken value again
                }else{
                    dp[i][j] = dp[i-1][j];
                }

            }
        }

        System.out.println(Arrays.deepToString(dp));
        return dp[size][size];

    }
}

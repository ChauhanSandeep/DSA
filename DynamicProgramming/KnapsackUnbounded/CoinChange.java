package DynamicProgramming.KnapsackUnbounded;

/**
 * Find the max combinations of coins that can create the required target sum.
 */
public class CoinChange {

    public static void main(String[] args) {
        int[] coins = new int[]{1, 2, 5, 10, 20, 50, 100};
        int target = 6;
        System.out.printf("Maximum combination to reach sum %s is %s%n", target, coinsCombinationItr(coins, target));
    }

    /**
    Recursive solution
     */
    static int coinsCombination(int[] coins, int target) {
        return coinsCombination(target, coins, 0);
    }

    static int coinsCombination(int target, int[] coins, int checkFromIndex) {
        if (target == 0) return 1;
        if (target < 0 || coins.length == checkFromIndex) return 0;

        int withFirstCoin = coinsCombination(target - coins[checkFromIndex], coins, checkFromIndex);
        int withoutFirstCoin = coinsCombination(target, coins, checkFromIndex + 1);
        return withFirstCoin + withoutFirstCoin;

    }

    /**
     * Iterative solution
     */
    public static int coinsCombinationItr(int[] coins, int target) {
        int[][] dp = new int[coins.length+1][target+1];

        for(int i=0; i<coins.length+1; i++) {
            dp[i][0] = 1;
        }

        for(int i=1; i<coins.length+1; i++) {
            for(int j=1; j<target+1; j++) {
                if(coins[i-1] <= j) {
                    dp[i][j] = Math.max(
                            dp[i-1][j] + dp[i][j-coins[i-1]],
                            dp[i-1][j]);
                }else{
                    dp[i][j]= dp[i-1][j];
                }

            }
        }
        return dp[coins.length][target];
    }
}

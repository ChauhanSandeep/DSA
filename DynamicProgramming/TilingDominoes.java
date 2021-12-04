package DynamicProgramming;

/**
 * Given an integer `num` you have to find the number of ways to fill a 2 x num
 * board with 2 x 1 dominoes.
 */
public class TilingDominoes {
    public static void main(String[] args) {
        System.out.println(new TilingDominoes().solve(2));
    }

    public int solve(int num) {
        int[] dp = new int[num+1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 2;

        for(int i=3; i<num+1; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }

        return dp[num];
    }
}

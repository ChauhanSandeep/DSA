package DynamicProgramming;

/**
 * Given an integer `num` you have to find the number of ways to fill a 3 x num
 * board with 2 x 1 dominoes. (Interviewbit)
 * Return the answer modulo 109 + 7 .
 * https://www.youtube.com/watch?v=yn2jnmlepY8
 */
public class TilingDominoes2 {

    public static void main(String[] args) {
        System.out.println(new TilingDominoes2().solve(50));
    }

    public int solve(int num) {
        int[][] dp = new int[num+1][8];
        int mod = (int)Math.pow(10, 9) + 7;
        dp[0][7] = 1;

        for(int i=1; i<num+1; i++) {
            dp[i][0] = (dp[i][0] + dp[i - 1][7]) % mod;
            // 110 -> 001
            dp[i][1] = (dp[i][1] + dp[i - 1][6]) % mod;

             // 101 -> 010
            dp[i][2] = (dp[i][2] + dp[i - 1][5]) % mod;

            // 100 -> 011
            dp[i][3] = (dp[i][3] + dp[i - 1][4]) % mod;
            // 111 -> 011 (stack 2X1 vertically)
            dp[i][3] = (dp[i][3] + dp[i - 1][7]) % mod;

            // 011 -> 100
            dp[i][4] = (dp[i][4] + dp[i - 1][3]) % mod;

             // 010 -> 101
            dp[i][5] = (dp[i][5] + dp[i - 1][2]) % mod;

            // 001 -> 110
            dp[i][6] = (dp[i][6] + dp[i - 1][1]) % mod;
            // 111 -> 110 (stack 2X1 vertically)
            dp[i][6] = (dp[i][6] + dp[i - 1][7]) % mod;

            // 011 -> 111
            dp[i][7] = (dp[i][7] + dp[i - 1][3]) % mod;
            // 110 -> 111
            dp[i][7] = (dp[i][7] + dp[i - 1][6])% mod;
            // 000 -> 111 (3 1X2 stacks horizontally)
            dp[i][7] = (dp[i][7] + dp[i - 1][0]) % mod;
        }

        return dp[num][7];
    }
}

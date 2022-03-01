package DynamicProgramming;

import java.util.Arrays;

/**
 * find max length of chain of pairs
 * (c, d) can form pair with (a,b) if b < c
 */
public class ChainOfPairs {

    public static void main(String[] args) {
        int[][] grid = {
                {5, 14},
                {39, 60},
                {15, 28},
                {27, 40},
                {50, 90}
        };
        int maxChainLength = new ChainOfPairs().solve(grid);
        System.out.println(maxChainLength);
    }

    public int solve(int[][] grid) {
        int size = grid.length;
        int result = 1;
        int[] dp = new int[size];
        Arrays.fill(dp, 1);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i; j++) {
                if (grid[j][1] < grid[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
                result = Math.max(result, dp[i]);
            }
        }

        return result;
    }
}

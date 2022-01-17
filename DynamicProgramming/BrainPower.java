package DynamicProgramming;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/solving-questions-with-brainpower/
 */
public class BrainPower {

    public static void main(String[] args) {
        int[][] questions = {
                {1,1},
                {2,2},
                {3,3},
                {4,4},
                {5,5}
        };
        long res = new BrainPower().mostPoints(questions);
        System.out.println(res);
    }

    public long mostPoints(int[][] questions) {

        long[] dp = new long[questions.length];
        Arrays.fill(dp, -1);
        return solve(questions, 0, dp);
    }

    public long solve(int[][] questions, int start, long[] dp) {
        if(start >= questions.length) return 0;
        if(dp[start] != -1) return dp[start];

        long profit = questions[start][0];
        int nStart = start + questions[start][1]+1;

        dp[start] = Math.max(profit + solve(questions, nStart, dp), solve(questions, start+1, dp));
        return dp[start];
    }
}

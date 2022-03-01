package DynamicProgramming.KnapsackUnbounded;

import java.util.Arrays;

/**
 * Given are the eating capacity of each friend, filling capacity of each dish and cost of each dish.
 * A friend is satisfied if the sum of the filling capacity of dishes he ate is equal to his capacity.
 * Find the minimum cost such that all of friends are satisfied (reached their eating capacity).
 * https://www.interviewbit.com/problems/tushars-birthday-party/
 *
 */
public class BirthdayParty {

    /**
     * @param limits eating capacity of each friend
     * @param values capacity of dish
     * @param costs  cost of dish
     * @return min cost in which all friends are full
     */
    public int solve(final int[] limits, final int[] values, final int[] costs) {
        int res = 0;
        for (Integer limit : limits) {
            res += solveRec(limit, values, costs);
        }
        return res;
    }

    // solve for a single friend
    private int solveRec(int limit, int[] values, int[] costs) {
        int[] dp = new int[limit + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= limit; i++) {
            for (int j = 0; j < values.length; j++) {
                if (i - values[j] >= 0) {
                    dp[i] = Math.min(dp[i], costs[j] + dp[i - values[j]]);
                }
            }
        }
        return dp[limit];
    }
}

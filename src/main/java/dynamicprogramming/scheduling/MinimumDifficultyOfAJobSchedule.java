package dynamicprogramming.scheduling;

import java.util.Arrays;

/**
 * Problem: Minimum Difficulty of a Job Schedule
 *
 * Schedule ordered jobs over exactly d days, at least one job per day. Minimize the sum of daily maximum difficulties.
 *
 * Leetcode: https://leetcode.com/problems/minimum-difficulty-of-a-job-schedule/ (Hard)
 * Rating:   contest Elo 2035
 * Pattern:  Dynamic programming | Sequence partitioning | Min over split
 *
 * Example:
 *   Input:  jobDifficulty = [6,5,4,3,2,1], d = 2
 *   Output: 7
 *   Why:    first five jobs on day 1 and the last job on day 2 costs 6 + 1.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Split Array Largest Sum (410), Minimum Cost to Cut a Stick (1547).
 */
public class MinimumDifficultyOfAJobSchedule {

    public static void main(String[] args) {
        MinimumDifficultyOfAJobSchedule solution = new MinimumDifficultyOfAJobSchedule();
        int[][] jobCases = { {6, 5, 4, 3, 2, 1}, {9, 9, 9}, {1, 1, 1} };
        int[] dayCases = {2, 4, 3};
        int[] expected = {7, -1, 3};
        for (int i = 0; i < jobCases.length; i++) {
            int got = solution.minDifficulty(jobCases[i], dayCases[i]);
            System.out.printf("jobs=%s days=%d -> %d  expected=%d%n", Arrays.toString(jobCases[i]), dayCases[i], got, expected[i]);
        }
    }

        /**
     * Intuition: dp[i][k] is the minimum difficulty for the first i jobs in k days. Choosing j as the first job on day k makes jobs j..i the last day, costing their maximum plus dp[j - 1][k - 1].
     *
     * Algorithm:
     *   1. Return -1 when jobs are fewer than days.
     *   2. Initialize dp with a large sentinel and dp[0][0] = 0.
     *   3. Iterate job prefix i and day count k.
     *   4. Walk j backward to try every last-day start.
     *   5. Update dp[i][k] with previous days plus last-day maximum.
     *
     * Time:  O(n^2 * d) - each prefix/day scans split points.
     * Space: O(n * d) - stores the table.
     *
     * @param jobDifficulty ordered job difficulties
     * @param d number of days
     * @return minimum difficulty or -1
     */
public int minDifficulty(int[] jobDifficulty, int d) {
        int n = jobDifficulty.length;
        // If we have more days than jobs, it's not possible
        if (n < d) {
            return -1;
        }

        // dp[i][j] represents the minimum difficulty to schedule first i jobs in j days
        int[][] dp = new int[n + 1][d + 1];

        // Initialize with infinity
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= d; j++) {
                dp[i][j] = Integer.MAX_VALUE / 2;
            }
        }
        dp[0][0] = 0; // Base case: 0 jobs, 0 days -> 0 difficulty

        for (int i = 1; i <= n; i++) {
            for (int k = 1; k <= d; k++) {
                int maxDifficulty = 0;
                // Try all possible partitions for the k-th day
                // j is the first job done on day k
                for (int j = i; j >= k; j--) {
                    // Update the maximum difficulty for the current day
                    maxDifficulty = Math.max(maxDifficulty, jobDifficulty[j - 1]);
                    // Update dp[i][k] with the minimum difficulty
                    if (dp[j - 1][k - 1] + maxDifficulty < dp[i][k]) {
                        dp[i][k] = dp[j - 1][k - 1] + maxDifficulty;
                    }
                }
            }
        }

        return dp[n][d] < Integer.MAX_VALUE / 2 ? dp[n][d] : -1;
    }

    /**
     * Optimized solution with space optimization (O(nd) time, O(n) space)
     */
    public int minDifficultyOptimized(int[] jobDifficulty, int d) {
        int n = jobDifficulty.length;
        if (n < d) {
            return -1;
        }

        // dp[i] represents the minimum difficulty to schedule jobs[0..i-1] in k days
        int[] dp = new int[n + 1];

        // Initialize with infinity
        for (int i = 1; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE / 2;
        }

        for (int k = 1; k <= d; k++) {
            // To compute dp[k][...], we only need dp[k-1][...]
            int[] newDp = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                newDp[i] = Integer.MAX_VALUE / 2;
            }

            for (int i = k; i <= n; i++) {
                int maxDifficulty = 0;
                // Try all possible partitions for the k-th day
                for (int j = i; j >= k; j--) {
                    maxDifficulty = Math.max(maxDifficulty, jobDifficulty[j - 1]);
                    newDp[i] = Math.min(newDp[i], dp[j - 1] + maxDifficulty);
                }
            }
            dp = newDp;
        }

        return dp[n] < Integer.MAX_VALUE / 2 ? dp[n] : -1;
    }
}

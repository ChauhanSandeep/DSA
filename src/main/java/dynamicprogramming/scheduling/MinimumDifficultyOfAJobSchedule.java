package dynamicprogramming.scheduling;

/**
 * You want to schedule a list of jobs in d days. Jobs are dependent (i.e., to work on the ith job, you have to finish all the jobs j where 0 <= j < i).
 *
 * You have to finish at least one task every day. The difficulty of a job schedule is the sum of difficulties of each day of the d days.
 * The difficulty of a day is the maximum difficulty of a job done on that day.
 *
 * Given an array jobDifficulty and an integer d. The difficulty of the ith job is jobDifficulty[i].
 *
 * Return the minimum difficulty of a job schedule. If you cannot find a schedule for the jobs, return -1.
 *
 * Example 1:
 * Input: jobDifficulty = [6,5,4,3,2,1], d = 2
 * Output: 7
 * Explanation: First day you can finish the first 5 jobs, total difficulty = 6.
 *              Second day you can finish the last job, total difficulty = 1.
 *              The difficulty of the schedule = 6 + 1 = 7
 *
 * Example 2:
 * Input: jobDifficulty = [9,9,9], d = 4
 * Output: -1
 * Explanation: If you finish a job per day you will still have a free day. You cannot find a schedule for the given jobs.
 *
 * LeetCode: https://leetcode.com/problems/minimum-difficulty-of-a-job-schedule/
 *
 * Follow-up Questions:
 * 1. What if the job difficulties can be negative?
 *    - The solution already handles both positive and negative numbers correctly.
 * 2. How would you handle very large input sizes (e.g., n = 1000, d = 100)?
 *    - The O(n²d) time complexity should handle this within reasonable time limits.
 * 3. How would you modify the solution to also return the optimal schedule?
 *    - We could track the partition points during the DP to reconstruct the schedule.
 *
 * Related Problems:
 * - Minimum Number of Days to Eat N Oranges (https://leetcode.com/problems/minimum-number-of-days-to-eat-n-oranges/)
 * - Minimum Cost to Cut a Stick (https://leetcode.com/problems/minimum-cost-to-cut-a-stick/)
 */
public class MinimumDifficultyOfAJobSchedule {
    /**
     * Calculates the minimum difficulty of a job schedule.
     *
     * @param jobDifficulty Array of job difficulties
     * @param d Number of days
     * @return Minimum difficulty or -1 if not possible
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

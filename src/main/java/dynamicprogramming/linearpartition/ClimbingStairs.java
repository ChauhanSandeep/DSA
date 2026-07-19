package dynamicprogramming.linearpartition;

/**
 * Problem: Climbing Stairs
 *
 * A staircase has totalSteps steps. Each move climbs either 1 or 2 steps, and
 * the task is to count how many distinct move sequences reach exactly the top.
 *
 * Leetcode: https://leetcode.com/problems/climbing-stairs/ (Easy)
 * Rating:   acceptance 54.2% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Fibonacci recurrence | Rolling variables
 *
 * Example:
 *   Input:  totalSteps = 3
 *   Output: 3
 *   Why:    the valid sequences are 1+1+1, 1+2, and 2+1.
 *
 * Follow-ups:
 *   1. What if steps of size 1, 2, or 3 are allowed?
 *      Use dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3].
 *   2. What if some steps are broken?
 *      Set the ways for broken steps to 0 before using the recurrence.
 *   3. What if each step size has a cost?
 *      Switch from counting ways to minimizing cost, similar to Min Cost Climbing Stairs.
 *
 * Related: Min Cost Climbing Stairs (746), Fibonacci Number (509).
 */
public class ClimbingStairs {

    public static void main(String[] args) {
        ClimbingStairs solver = new ClimbingStairs();
        int[] inputs = { 1, 3, 5 };
        int[] expected = { 1, 3, 8 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.climbStairs(inputs[i]);
            System.out.printf("totalSteps=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: to land on step n, the final move must come from step n - 1
     * with a one-step move or from step n - 2 with a two-step move. These two
     * sets of sequences do not overlap, so the number of ways is their sum. The
     * method keeps only the last two counts instead of the whole DP array.
     *
     * Algorithm:
     *   1. Return 1 for totalSteps 0 or 1.
     *   2. Store the known counts for steps 1 and 2 in rolling variables.
     *   3. For each later step, add the previous two counts and shift the window forward.
     *
     * Time:  O(n) - one iteration for each step from 3 through totalSteps.
     * Space: O(1) - only two previous counts and the current count are stored.
     *
     * @param totalSteps number of staircase steps
     * @return number of distinct ways to reach the top
     */
    public int climbStairs(int totalSteps) {
        // Base cases: 0 or 1 step has only 1 way to climb
        if (totalSteps <= 1) {
            return 1;
        }

        // Initialize variables to store the number of ways to reach the previous two steps
        int waysToPreviousStep = 2;  // ways to reach step 2 (1+1 or 2)
        int waysToTwoStepsBack = 1;  // ways to reach step 1 (only 1 way)

        // Calculate number of ways for each step from 3 to totalSteps
        for (int currentStep = 3; currentStep <= totalSteps; currentStep++) {
            // Current step's ways = (ways to previous step) + (ways to step before previous)
            int currentWays = waysToPreviousStep + waysToTwoStepsBack;

            // Update for next iteration
            waysToTwoStepsBack = waysToPreviousStep;
            waysToPreviousStep = currentWays;
        }

        return waysToPreviousStep;
    }

    /**
     * Alternative solution using memoization (top-down DP approach)
     *
     * Time Complexity: O(n) - Each subproblem is solved once
     * Space Complexity: O(n) - For the recursion stack and memoization table
     */
    public int climbStairsMemoization(int totalSteps) {
        // Initialize memoization array with -1 to indicate uncomputed values
        int[] memo = new int[totalSteps + 1];
        return climbStairsHelper(totalSteps, memo);
    }

    /**
     * Helper method for memoization approach
     */
    private int climbStairsHelper(int remainingSteps, int[] memo) {
        // Base cases
        if (remainingSteps <= 1) {
            return 1;
        }

        // Return memoized result if already computed
        if (memo[remainingSteps] > 0) {
            return memo[remainingSteps];
        }

        // Recursively compute and memoize the result
        memo[remainingSteps] = climbStairsHelper(remainingSteps - 1, memo) +
                             climbStairsHelper(remainingSteps - 2, memo);

        return memo[remainingSteps];
    }
}

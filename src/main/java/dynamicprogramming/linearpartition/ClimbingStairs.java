package dynamicprogramming.linearpartition;

/**
 * Problem: Climbing Stairs (LeetCode #70)
 *
 * Problem Statement:
 * You are climbing a staircase that has 'totalSteps' steps. Each time you can either climb 1 or 2 steps.
 * In how many distinct ways can you climb to the top?
 *
 * Example 1:
 * Input: totalSteps = 2
 * Output: 2
 * Explanation: There are two ways to climb to the top:
 * 1. 1 step + 1 step
 * 2. 2 steps
 *
 * Example 2:
 * Input: totalSteps = 3
 * Output: 3
 * Explanation: There are three ways to climb to the top:
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 *
 * Approach:
 * This is a classic dynamic programming problem that can be solved using the Fibonacci sequence pattern.
 * The key observation is that the number of ways to reach step n is equal to the sum of ways to reach
 * step (n-1) and step (n-2), since you can take either 1 or 2 steps at a time.
 *
 * Time Complexity: O(n) - We iterate through n steps once
 * Space Complexity: O(1) - We only use constant extra space
 *
 * Follow-up Questions:
 * 1. What if you can climb 1, 2, or 3 steps at a time?
 *    Answer: The recurrence relation would change to dp[n] = dp[n-1] + dp[n-2] + dp[n-3].
 *    The base cases would be dp[0]=1, dp[1]=1, dp[2]=2.
 *
 * 2. What if some steps are broken and cannot be stepped on?
 *    Answer: We can modify the DP approach to skip the broken steps by setting dp[brokenStep] = 0.
 *    The recurrence would then be dp[i] = 0 if step i is broken, else dp[i-1] + dp[i-2].
 *
 * 3. Can you solve it using O(1) space?
 *    Answer: Yes, we can optimize space by only keeping track of the last two values (prevStep and prevPrevStep)
 *    instead of maintaining a full DP array, as shown in the solution below.
 *
 * LeetCode: https://leetcode.com/problems/climbing-stairs/
 */
public class ClimbingStairs {

    /**
     * Calculates the number of distinct ways to climb 'totalSteps' steps
     * using 1 or 2 steps at a time.
     *
     * @param totalSteps The total number of steps in the staircase
     * @return The number of distinct ways to climb to the top
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

package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;

/**
 * Problem: Solving Questions With Brainpower
 *
 * Each question gives some points, but solving it forces you to skip the next few
 * questions. You may also skip any question freely. Return the maximum points you
 * can collect from left to right.
 *
 * Leetcode: https://leetcode.com/problems/solving-questions-with-brainpower/
 * Rating:   1709 (zerotrac Elo)
 * Pattern:  Dynamic programming | 1-D DP | Take-or-skip recurrence
 *
 * Example:
 *   Input:  questions = [[3,2],[4,3],[4,4],[2,5]]
 *   Output: 5
 *   Why:    solving question 0 gives 3 points and jumps to the end, but skipping it
 *           lets us solve question 1 for 4 and still question 3 for 2 is not reachable;
 *           the best valid total is 5 from questions 0 and 3 in the original example.
 *
 * Follow-ups:
 *   1. Return which questions were solved, not just the score?
 *      Store the chosen branch for each index and walk the decisions after DP finishes.
 *   2. What if each question has a deadline instead of a fixed skip count?
 *      Sort by deadline and use interval scheduling DP over compatible questions.
 *   3. What if memory must be constant?
 *      Use bottom-up DP only when future lookups are bounded; arbitrary brainpower needs O(n).
 *
 * Related: House Robber (198), Delete and Earn (740), Maximum Profit in Job Scheduling (1235).
 */

public class BrainPower {

    public static void main(String[] args) {
        BrainPower solver = new BrainPower();
        int[][][] inputs = {
            {{3, 2}, {4, 3}, {4, 4}, {2, 5}},
            {{1, 1}},
            {{21, 5}, {92, 3}, {74, 2}, {39, 4}, {58, 2}}
        };
        long[] expected = {5, 1, 92};

        for (int i = 0; i < inputs.length; i++) {
            long got = solver.mostPoints(inputs[i]);
            System.out.printf("questions=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: dp[index] means the maximum score we can still earn starting at
     * that question. From one index there are only two honest choices: solve it,
     * collect its points, and continue after the forced skip, or ignore it and try
     * the next question. Those future positions are smaller suffix problems, so
     * memoizing each suffix lets the recursion reuse the same answer whenever it
     * is reached by different earlier choices.
     *
     * Algorithm:
     *   1. Fill memo with -1 so each starting index is computed once.
     *   2. From start, return 0 past the end or the cached memo value.
     *   3. Compare taking questions[start] plus the next legal index with skipping to start + 1.
     *   4. Store and return the larger value for that start index.
     *
     * Time:  O(n) - each question index is solved once, and each solve does O(1) work.
     * Space: O(n) - the memo array and recursion stack can both grow with the number of questions.
     *
     * @param questions questions[i] is [points, brainpower]
     * @return maximum points that can be earned
     */
    public long mostPoints(int[][] questions) {
        long[] memo = new long[questions.length];
        Arrays.fill(memo, -1);
        return solve(questions, 0, memo);
    }

    /**
     * Recursively calculates the maximum points collectible from `start` index.
     *
     * @param questions The questions array
     * @param start Current index being considered
     * @param memo Memoization array to store results
     * @return Maximum points that can be collected from `start` onwards
     */
    private long solve(int[][] questions, int start, long[] memo) {
        if (start >= questions.length) return 0; // Base case: No more questions left
        if (memo[start] != -1) return memo[start]; // Return cached result if already computed

        long takeCurrent = questions[start][0] + solve(questions, start + questions[start][1] + 1, memo);
        long skipCurrent = solve(questions, start + 1, memo);

        // Store and return the maximum of both choices
        return memo[start] = Math.max(takeCurrent, skipCurrent);
    }
}

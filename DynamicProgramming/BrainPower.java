package DynamicProgramming;

import java.util.Arrays;

/**
 * LeetCode Problem: Solving Questions With Brainpower
 * Link: https://leetcode.com/problems/solving-questions-with-brainpower/
 *
 * Problem Statement:
 * Given an array `questions`, where `questions[i] = [points, brainpower]`,
 * you can either:
 *  - Solve the question to gain `points` and then skip `brainpower` questions.
 *  - Skip the question and move to the next.
 * Find the maximum points that can be earned.
 *
 * Approach:
 * - Uses **top-down memoization (recursion with caching)**.
 * - `dp[i]` stores the maximum points collectible starting from index `i`.
 * - Recursively compute:
 *    1. Taking the current question and skipping `brainpower` steps.
 *    2. Skipping the current question.
 *
 * Time Complexity: **O(n)** (Each index is computed only once)
 * Space Complexity: **O(n)** (Memoization table `dp`)
 */

public class BrainPower {

    public static void main(String[] args) {
        int[][] questions = {
                {1, 1},
                {2, 2},
                {3, 3},
                {4, 4},
                {5, 5}
        };

        BrainPower solver = new BrainPower();
        long result = solver.mostPoints(questions);
        System.out.println("Maximum points: " + result);
    }

    /**
     * Wrapper function to initialize memoization table and start solving.
     * 
     * @param questions 2D array where questions[i] = [points, brainpower]
     * @return Maximum points that can be earned
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

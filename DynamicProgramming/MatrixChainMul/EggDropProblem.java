package DynamicProgramming.MatrixChainMul;

import java.util.Arrays;

/**
 * Problem: Egg Drop Problem
 * LeetCode Link: https://leetcode.com/problems/super-egg-drop/
 *
 * Description:
 * - Given `e` eggs and `f` floors, find the minimum number of attempts required
 *   to determine the highest floor from which the egg can be dropped without breaking.
 * - The goal is to minimize the worst-case number of trials.
 *
 */
public class EggDropProblem {

    public static void main(String[] args) {
        EggDropProblem solver = new EggDropProblem();

        int eggs = 2, floors = 10;

        System.out.println("Minimum Trials (Recursive DP): " + solver.minTrialsRecursive(eggs, floors));
        System.out.println("Minimum Trials (Iterative DP): " + solver.minTrialsIterative(eggs, floors));
        System.out.println("Minimum Trials (Optimized DP - Binary Search): " + solver.superEggDrop(eggs, floors));
    }

    /**
     * **Recursive DP (Top-down with Memoization)**
     *  - Uses recursion and memoization to compute results.
     *  - **Time Complexity**: O(e * f²) (Leads to TLE for large inputs).
     *  - **Space Complexity**: O(e * f) (Memoization table).
     *
     * @param eggs   Number of eggs.
     * @param floors Number of floors.
     * @return Minimum number of attempts required.
     */
    public int minTrialsRecursive(int eggs, int floors) {
        int[][] memo = new int[eggs + 1][floors + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return minTrialsRecHelper(eggs, floors, memo);
    }

    /**
     * Recursive function with memoization.
     */
    private int minTrialsRecHelper(int eggs, int floors, int[][] memo) {
        // Base cases
        if (floors == 0 || floors == 1) return floors;
        if (eggs == 1) return floors; // If only 1 egg, check all floors linearly.

        if (memo[eggs][floors] != -1) return memo[eggs][floors];

        int minAttempts = Integer.MAX_VALUE;

        for (int dropFloor = 1; dropFloor <= floors; dropFloor++) {
            // if the egg breaks in current floor then, number of egg decreases by 1, and we now need to check the floors below
            int breakCase = minTrialsRecHelper(eggs - 1, dropFloor - 1, memo);
            // if the egg doesn't break in current floor then, number of eggs remains same, and we now need to check the floors above
            int noBreakCase = minTrialsRecHelper(eggs, floors - dropFloor, memo); // Egg doesn't break

            //added 1 for the current trial and taking the maximum of both cases in worst case
            int worstCase = 1 + Math.max(breakCase, noBreakCase);
            minAttempts = Math.min(minAttempts, worstCase);
        }

        return memo[eggs][floors] = minAttempts;
    }

    /**
     * **Iterative DP (Bottom-up)**
     *  - Uses a DP table to compute the minimum number of trials.
     *  - **Time Complexity**: O(e * f²) (Leads to TLE for large inputs).
     *  - **Space Complexity**: O(e * f).
     *
     * @param eggs   Number of eggs.
     * @param floors Number of floors.
     * @return Minimum number of attempts required.
     */
    public int minTrialsIterative(int eggs, int floors) {
        int[][] dp = new int[eggs + 1][floors + 1];

        // Base cases
        for (int i = 1; i <= eggs; i++) {
            dp[i][1] = 1; // 1 trial required for 1 floor
            dp[i][0] = 0; // 0 trials required for 0 floors
        }
        for (int i = 1; i <= floors; i++) {
            dp[1][i] = i; // If only 1 egg, worst case is checking each floor
        }

        // Compute DP table
        for (int currentEgg = 2; currentEgg <= eggs; currentEgg++) {
            for (int currentFloor = 2; currentFloor <= floors; currentFloor++) {
                dp[currentEgg][currentFloor] = Integer.MAX_VALUE;
                for (int dropFloor = 1; dropFloor <= currentFloor; dropFloor++) {
                    // if the egg breaks in current floor then, number of egg decreases by 1, and we now need to check the floors below
                    int breakCase = dp[currentEgg - 1][dropFloor - 1];
                    // if the egg doesn't break in current floor then, number of eggs remains same, and we now need to check the floors above
                    int noBreakCase = dp[currentEgg][currentFloor - dropFloor];

                    int worstCase = 1 + Math.max(breakCase, noBreakCase);
                    dp[currentEgg][currentFloor] = Math.min(dp[currentEgg][currentFloor], worstCase);
                }
            }
        }
        return dp[eggs][floors];
    }

    /**
     * **Optimized DP (Binary Search)**
     *  - Uses binary search to optimize finding the critical floor.
     *  - **Time Complexity**: O(e * f log f) (Most efficient).
     *  - **Space Complexity**: O(e * f).
     *
     * @param eggs   Number of eggs.
     * @param floors Number of floors.
     * @return Minimum number of attempts required.
     */
    public int superEggDrop(int eggs, int floors) {
        int[][] dp = new int[eggs + 1][floors + 1];

        // Base cases
        for (int currentEgg = 1; currentEgg <= eggs; currentEgg++) {
            dp[currentEgg][1] = 1; // 1 trial required for 1 floor
            dp[currentEgg][0] = 0; // 0 trials required for 0 floors
        }
        for (int currentFloor = 1; currentFloor <= floors; currentFloor++) {
            dp[1][currentFloor] = currentFloor; // If only 1 egg, worst case is checking each floor
        }

        // Compute DP using binary search optimization
        for (int currentEgg = 2; currentEgg <= eggs; currentEgg++) {
            for (int currentFloor = 1; currentFloor <= floors; currentFloor++) {
                int minAttempts = Integer.MAX_VALUE;
                int lowFloor = 1, highFloor = currentFloor;

                while (lowFloor <= highFloor) {
                    int dropFloor = lowFloor + (highFloor - lowFloor) / 2;
                    // if the egg breaks in current floor then, number of egg decreases by 1, and we now need to check the floors below
                    int breakCase = dp[currentEgg - 1][dropFloor - 1];
                    // if the egg doesn't break in current floor then, number of eggs remains same, and we now need to check the floors above
                    int noBreakCase = dp[currentEgg][currentFloor - dropFloor];

                    int worstCase = 1 + Math.max(breakCase, noBreakCase);
                    minAttempts = Math.min(minAttempts, worstCase);

                    // Adjust search range based on the worst-case comparison
                    if (breakCase == noBreakCase) break;
                    if (breakCase < noBreakCase) lowFloor = dropFloor + 1;
                    else highFloor = dropFloor - 1;
                }

                dp[currentEgg][currentFloor] = minAttempts;
            }
        }
        return dp[eggs][floors];
    }
}

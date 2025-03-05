package DynamicProgramming;

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
 * Approaches:
 * 1. **Recursive DP (Top-down with Memoization)**
 *    - Uses recursion and memoization to compute results.
 *    - **Time Complexity**: O(e * f²) (Leads to TLE for large inputs).
 *    - **Space Complexity**: O(e * f) (Memoization table).
 *
 * 2. **Iterative DP (Bottom-up)**
 *    - Uses a DP table to compute the minimum number of trials.
 *    - **Time Complexity**: O(e * f²) (Leads to TLE for large inputs).
 *    - **Space Complexity**: O(e * f).
 *
 * 3. **Optimized DP (Binary Search)**
 *    - Uses binary search to optimize finding the critical floor.
 *    - **Time Complexity**: O(e * f log f) (Most efficient).
 *    - **Space Complexity**: O(e * f).
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
     * **Recursive Approach** (Top-down with Memoization)
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

        for (int floor = 1; floor <= floors; floor++) {
            int breakCase = minTrialsRecHelper(eggs - 1, floor - 1, memo); // Egg breaks
            int noBreakCase = minTrialsRecHelper(eggs, floors - floor, memo); // Egg doesn't break

            int worstCase = 1 + Math.max(breakCase, noBreakCase);
            minAttempts = Math.min(minAttempts, worstCase);
        }

        return memo[eggs][floors] = minAttempts;
    }

    /**
     * **Iterative Dynamic Programming Approach**
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
        for (int e = 2; e <= eggs; e++) {
            for (int f = 2; f <= floors; f++) {
                dp[e][f] = Integer.MAX_VALUE;
                for (int x = 1; x <= f; x++) {
                    int breakCase = dp[e - 1][x - 1];
                    int noBreakCase = dp[e][f - x];

                    int worstCase = 1 + Math.max(breakCase, noBreakCase);
                    dp[e][f] = Math.min(dp[e][f], worstCase);
                }
            }
        }
        return dp[eggs][floors];
    }

    /**
     * **Optimized DP using Binary Search**
     * (Most Efficient Approach)
     *
     * @param eggs   Number of eggs.
     * @param floors Number of floors.
     * @return Minimum number of attempts required.
     */
    public int superEggDrop(int eggs, int floors) {
        int[][] dp = new int[eggs + 1][floors + 1];

        // Base cases
        for (int i = 1; i <= eggs; i++) {
            dp[i][1] = 1; // 1 trial required for 1 floor
            dp[i][0] = 0; // 0 trials required for 0 floors
        }
        for (int i = 1; i <= floors; i++) {
            dp[1][i] = i; // If only 1 egg, worst case is checking each floor
        }

        // Compute DP using binary search optimization
        for (int e = 2; e <= eggs; e++) {
            for (int f = 1; f <= floors; f++) {
                int minAttempts = Integer.MAX_VALUE;
                int left = 1, right = f;

                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    int breakCase = dp[e - 1][mid - 1]; // Egg breaks
                    int noBreakCase = dp[e][f - mid]; // Egg doesn't break

                    int worstCase = 1 + Math.max(breakCase, noBreakCase);
                    minAttempts = Math.min(minAttempts, worstCase);

                    // Adjust search range based on the worst-case comparison
                    if (breakCase == noBreakCase) break;
                    if (breakCase < noBreakCase) left = mid + 1;
                    else right = mid - 1;
                }

                dp[e][f] = minAttempts;
            }
        }
        return dp[eggs][floors];
    }
}

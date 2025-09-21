package dynamicprogramming.MatrixChainMul;

import java.util.Arrays;


/**
 * Problem: Egg Drop Problem (Super Egg Drop)
 *
 * Leetcode Link:
 * https://leetcode.com/problems/super-egg-drop/
 *
 * Problem Statement:
 * Given `k` eggs and `n` floors, determine the minimum number of trials required
 * to find out the highest floor from which an egg can be dropped without breaking.
 *
 * You want to minimize the number of trials in the worst case.
 *
 * Example:
 * Input: k = 3, n = 14
 * Output: 4
 * Explanation: With 3 eggs and 14 floors, the minimum number of trials in the worst case is 4.
 * One optimal strategy is:
 * - Drop from floor 4: If it breaks, check floors 1-3 with 2 eggs (3 trials).
 * - If it doesn't break, drop from floor 8: If it breaks, check floors 5-7 with 2 eggs (3 trials).
 * - If it doesn't break, drop from floor 11: If it breaks, check floors 9-10 with 2 eggs (3 trials).
 * - If it doesn't break, drop from floor 14: If it breaks, check floors 12-13 with 2 eggs (3 trials).
 * In all scenarios, the worst-case number of trials is 4.
 *
 * Follow-up Questions:
 * 1. Can we optimize using Binary Search in the recursion?
 *    → Yes. Use binary search on the floor to reduce time complexity.
 *    → Problem Link: https://leetcode.com/problems/super-egg-drop/
 *
 * 2. Can we solve it using moves instead of floors?
 *    → Yes. Another approach uses DP with states (moves, eggs).
 *    → Problem Link: https://leetcode.com/problems/super-egg-drop/solutions/123831/dp-using-binary-search-over-answer/
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
   * 1. Try dropping the egg from every floor and recursively solve the subproblems.
   * 2. Use memoization to cache results for overlapping subproblems.
   * 3. Choose the minimum number of trials from all worst-case outcomes.
   *
   * **Time Complexity**: O(eggs * floors^2) (Leads to TLE for large inputs).
   * **Space Complexity**: O(eggs * floors^2) (Memoization table).
   *
   * @param eggs   Number of eggs.
   * @param floors Number of floors.
   * @return Minimum number of attempts required.
   */
  public int minTrialsRecursive(int eggs, int floors) {
    int[][] memo = new int[eggs + 1][floors + 1]; // memo[i][j] = min trials for i eggs and j floors
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
    if (floors == 0 || floors == 1) {
      return floors;
    }
    if (eggs == 1) {
      return floors; // If only 1 egg, check all floors linearly.
    }

    if (memo[eggs][floors] != -1) {
      return memo[eggs][floors];
    }

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
   *  1. Initialize base cases for 0 and 1 floor.
   *  2. Fill DP table bottom-up by simulating dropping eggs from each floor.
   *  3. Track worst-case trials and take the minimum among all drop choices.
   *
   *  Time Complexity: O(eggs * floors^2)
   *  Space Complexity: O(eggs * floors)
   *
   * @param eggs   Number of eggs.
   * @param floors Number of floors.
   * @return Minimum number of attempts required.
   */
  public int minTrialsIterative(int eggs, int floors) {
    int[][] dp = new int[eggs + 1][floors + 1]; // dp[i][j] = min trials for i eggs and j floors

    // Base cases
    for (int egg = 1; egg <= eggs; egg++) {
      dp[egg][1] = 1; // 1 trial required for 1 floor
      dp[egg][0] = 0; // 0 trials required for 0 floors
    }
    for (int floor = 1; floor <= floors; floor++) {
      dp[1][floor] = floor; // If only 1 egg, worst case is checking each floor
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
   * Optimized DP using Binary Search
   *
   * Steps:
   * 1. Replace linear scan with binary search to find optimal drop floor.
   * 2. Compare break vs no-break cases and adjust range accordingly.
   * 3. Use memoization table to avoid recomputation.
   *
   * Time Complexity: O(eggs * floors * log (floors))
   * Space Complexity: O(eggs * floors)
   *
   * @param eggs   Number of eggs
   * @param floors Number of floors
   * @return Minimum number of trials in worst case
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
          if (breakCase == noBreakCase) {
            break;
          }
          if (breakCase < noBreakCase) {
            lowFloor = dropFloor + 1;
          } else {
            highFloor = dropFloor - 1;
          }
        }

        dp[currentEgg][currentFloor] = minAttempts;
      }
    }
    return dp[eggs][floors];
  }
}

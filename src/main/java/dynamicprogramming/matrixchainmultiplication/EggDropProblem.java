package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Problem: Egg Drop Problem (Super Egg Drop)
 *
 * https://leetcode.com/problems/super-egg-drop/
 *
 * Problem Statement:
 * Given `k` eggs and `n` floors, determine the minimum number of trials
 * required
 * to find out the highest floor from which an egg can be dropped without
 * breaking.
 *
 * You want to minimize the number of trials in the worst case.
 *
 * Example:
 * Input: k = 3, n = 14
 * Output: 4
 * Explanation: With 3 eggs and 14 floors, the minimum number of trials in the
 * worst case is 4.
 * One optimal strategy is:
 * - Drop from floor 4: If it breaks, check floors 1-3 with 2 eggs (3 trials).
 * - If it doesn't break, drop from floor 8: If it breaks, check floors 5-7 with
 * 2 eggs (3 trials).
 * - If it doesn't break, drop from floor 11: If it breaks, check floors 9-10
 * with 2 eggs (3 trials).
 * - If it doesn't break, drop from floor 14: If it breaks, check floors 12-13
 * with 2 eggs (3 trials).
 * In all scenarios, the worst-case number of trials is 4.
 *
 * Follow-up Questions:
 * 1. Can we optimize using Binary Search in the recursion?
 * → Yes. Use binary search on the floor to reduce time complexity.
 * → Problem Link: https://leetcode.com/problems/super-egg-drop/
 *
 * 2. Can we solve it using moves instead of floors?
 * → Yes. Another approach uses DP with states (moves, eggs).
 * → Problem Link:
 * https://leetcode.com/problems/super-egg-drop/solutions/123831/dp-using-binary-search-over-answer/
 * LeetCode Contest Rating: 2377
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
   * Recursive DP (Top-down with Memoization and Binary Search)
   * 1. Use binary search to find optimal drop floor instead of linear scan.
   * 2. Use memoization to cache results for overlapping subproblems.
   * 3. Choose the minimum number of trials from all worst-case outcomes.
   *
   * Time Complexity: O(eggs * floors * log(floors))
   * Space Complexity: O(eggs * floors) (Memoization table + recursion stack).
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
   * Recursive function with memoization and binary search optimization.
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
    int lowFloor = 1, highFloor = floors;

    // Use binary search to find optimal drop floor
    while (lowFloor <= highFloor) {
      int dropFloor = lowFloor + (highFloor - lowFloor) / 2;

      // if the egg breaks in current floor then, number of egg decreases by 1, and we
      // now need to check the floors below
      int breakCaseTrials = minTrialsRecHelper(eggs - 1, dropFloor - 1, memo);
      // if the egg doesn't break in current floor then, number of eggs remains same,
      // and we now need to check the floors above
      int noBreakCaseTrials = minTrialsRecHelper(eggs, floors - dropFloor, memo);

      // Added 1 for the current trial and taking the maximum of both cases in worst
      // case.
      int worstCase = 1 + Math.max(breakCaseTrials, noBreakCaseTrials);
      minAttempts = Math.min(minAttempts, worstCase);

      // Adjust search range based on the worst-case comparison
      if (breakCaseTrials == noBreakCaseTrials) {
        // If both cases require the same number of trials, we've found the optimal
        // floor
        break;
      }
      if (noBreakCaseTrials > breakCaseTrials) {
        // Move to higher floors because we need to go for the case with more trials
        lowFloor = dropFloor + 1;
      } else {
        // Move to lower floors because we need to go for the case with more trials
        highFloor = dropFloor - 1;
      }
    }

    return memo[eggs][floors] = minAttempts;
  }

  /**
   * Iterative DP (Bottom-up with Linear Scan)
   * Steps:
   * 1. Create DP table: dp[i][j] represents the minimum trials needed with i eggs
   * and j floors.
   * 2. Base cases:
   * - 0 floors → 0 trials (nothing to check)
   * - 1 floor → 1 trial (just drop once)
   * - 1 egg → j trials (must check linearly from bottom: floor 1, 2, ..., j)
   * 3. For each state (currentEgg, currentFloor):
   * - Try dropping from every possible floor (dropFloor = 1 to currentFloor)
   * - For each drop, calculate worst-case trials:
   * a. Egg breaks: We lose 1 egg, check floors below (1 to dropFloor-1)
   * → Subproblem: dp[currentEgg - 1][dropFloor - 1]
   * b. Egg survives: Keep same eggs, check floors above (dropFloor+1 to
   * currentFloor)
   * → Subproblem: dp[currentEgg][currentFloor - dropFloor]
   * c. Worst case: max(breakCase, noBreakCase) + 1 (for current drop)
   * - Track the minimum across all possible drop floors
   *
   * 4. Result: dp[eggs][floors] gives the answer.
   *
   * Time Complexity: O(eggs * floors²) - For each state, we try all floors
   * linearly.
   * Space Complexity: O(eggs * floors) - DP table storage.
   *
   * @param eggs   Number of eggs available.
   * @param floors Number of floors in the building.
   * @return Minimum number of trials required in the worst case.
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
          // if the egg breaks in current floor then, number of egg decreases by 1, and we
          // now need to check the floors below.
          // Count of remaining floors is dropFloor - 1.
          int breakCaseTrials = dp[currentEgg - 1][dropFloor - 1];

          // if the egg doesn't break in current floor then, number of eggs remains same,
          // and we now need to check the floors above.
          // Remaining floors are dropFloor + 1 to currentFloor. Count of remaining floors
          // is currentFloor - dropFloor.
          int remainingFloors = currentFloor - dropFloor;
          int noBreakCaseTrials = dp[currentEgg][remainingFloors];

          int worstCaseTrials = 1 + Math.max(breakCaseTrials, noBreakCaseTrials);
          dp[currentEgg][currentFloor] = Math.min(dp[currentEgg][currentFloor], worstCaseTrials);
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
          // if the egg breaks in current floor then, number of egg decreases by 1, and we
          // now need to check the floors below
          int breakCase = dp[currentEgg - 1][dropFloor - 1];
          // if the egg doesn't break in current floor then, number of eggs remains same,
          // and we now need to check the floors above
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

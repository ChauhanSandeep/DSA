package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Problem: Super Egg Drop
 *
 * Given eggs and floors, find the minimum number of drops needed in the worst
 * case to identify the highest safe floor. If an egg breaks, it cannot be used
 * again; if it survives, it can be used on higher floors.
 *
 * Leetcode: https://leetcode.com/problems/super-egg-drop/
 * Rating:   2377 (zerotrac Elo)
 * Pattern:  Dynamic Programming | Interval decision DP | Binary-search transition
 *
 * Example:
 *   Input:  eggs = 2, floors = 10
 *   Output: 4
 *   Why:    four drops are enough with the classic 4, 7, 9, 10 style strategy,
 *           while three drops can distinguish at most six floors with two eggs.
 *
 * Follow-ups:
 *   1. Can the state be based on moves instead of floors?
 *      Yes; dp[moves][eggs] counts how many floors can be tested, then find the
 *      first moves where that coverage reaches floors.
 *   2. Why can binary search be used inside the recurrence?
 *      As the drop floor rises, the break case grows and the no-break case shrinks,
 *      so the worst case is minimized near their crossing point.
 *   3. How would you return an actual dropping plan?
 *      Store the selected drop floor for each state and follow the branch based on
 *      whether the egg breaks during execution.
 */
public class EggDropProblem {

    /**
   * Intuition: after dropping from a floor, the egg either breaks and the answer
   * is below with one fewer egg, or it survives and the answer is above with the
   * same eggs. We minimize the worst of those two outcomes.
   *
   * Algorithm:
   *   1. Memoize states by eggs and floors.
   *   2. Handle base cases for zero/one floor and one egg.
   *   3. Binary search the drop floor because break cost rises while survive cost falls.
   *   4. Store the minimum worst-case attempts.
   *
   * Time:  O(eggs * floors * log floors) - each state binary-searches floors.
   * Space: O(eggs * floors) - memo table plus recursion depth.
   *
   * @param eggs number of eggs available
   * @param floors number of floors to test
   * @return minimum worst-case attempts
   */
  public int minTrialsRecursive(int eggs, int floors) {
    int[][] memo = new int[eggs + 1][floors + 1]; // memo[i][j] = min trials for i eggs and j floors
    for (int[] row : memo) {
      Arrays.fill(row, -1);
    }
    return minTrialsRecHelper(eggs, floors, memo);
  }

    /** Solves the memoized worst-case state for eggs and floors. */
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
   * Intuition: the recursive worst-case choice can be tabulated for every smaller
   * egg/floor state. For each possible first drop, the cost is one attempt plus
   * the worse of the break and no-break subproblems.
   *
   * Algorithm:
   *   1. Initialize base rows for zero/one floor and one egg.
   *   2. For each egg count and floor count, try every dropFloor.
   *   3. Combine break and no-break states with max plus one current drop.
   *   4. Keep the minimum worst-case value in dp[eggs][floors].
   *
   * Time:  O(eggs * floors^2) - each state scans all possible drop floors.
   * Space: O(eggs * floors) - DP table for all states.
   *
   * @param eggs number of eggs available
   * @param floors number of floors to test
   * @return minimum worst-case attempts
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
   * Intuition: in a fixed egg/floor state, the break branch grows as the drop
   * floor rises while the no-break branch shrinks. Binary search moves toward the
   * balance point instead of scanning every floor.
   *
   * Algorithm:
   *   1. Initialize base cases in the DP table.
   *   2. For each state, binary-search the candidate drop floor.
   *   3. Compare break and no-break costs to move the search range.
   *   4. Store the smallest worst-case attempt count.
   *
   * Time:  O(eggs * floors * log floors) - each state binary-searches floors.
   * Space: O(eggs * floors) - DP table for all states.
   *
   * @param eggs number of eggs available
   * @param floors number of floors to test
   * @return minimum worst-case attempts
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


    public static void main(String[] args) {
        EggDropProblem solver = new EggDropProblem();
        int[][] cases = { {1, 5}, {2, 10}, {3, 14} };
        int[] expected = {5, 4, 4};

        for (int i = 0; i < cases.length; i++) {
            int output = solver.superEggDrop(cases[i][0], cases[i][1]);
            System.out.printf("eggs=%d floors=%d  ->  %d  expected=%d%n",
                cases[i][0], cases[i][1], output, expected[i]);
        }
    }

}

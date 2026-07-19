package dynamicprogramming.statemachine;

import java.util.Arrays;

/**
 * Problem: Delete and Earn
 *
 * Choose values from nums to earn points. Taking value x earns x points for each
 * occurrence of x, but deletes every x-1 and x+1. Return the maximum points that
 * can be earned.
 *
 * Leetcode: https://leetcode.com/problems/delete-and-earn/
 * Rating:   acceptance 57.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | State machine | House Robber on values
 *
 * Example:
 *   Input:  nums = [2,2,3,3,3,4]
 *   Output: 9
 *   Why:    taking all 3s earns 9 and deletes 2s and 4; any plan using 2 or 4 earns less.
 *
 * Follow-ups:
 *   1. Can this be solved with memoized recursion?
 *      Yes; recurse on value v with choices take v and skip v-1, or skip v.
 *   2. What if choosing x also deletes x-2 and x+2?
 *      Change the recurrence so the take transition jumps back three value positions.
 *   3. What if values are huge but sparse?
 *      Sort the distinct values and run a compressed House Robber DP over gaps.
 *
 * Related: House Robber (198), Target Sum (494).
 */
public class DeleteAndEarn {

    /**
   * Intuition: taking value x deletes x - 1 and x + 1, just like robbing adjacent
   * houses in value order. First compress all equal values into total points for
   * that value, then run the take/skip recurrence across values.
   *
   * Algorithm:
   *   1. Accumulate total points available for each value.
   *   2. Sweep values from low to high with include and exclude states.
   *   3. Include the current value only after excluding the previous value.
   *   4. Return the better final state.
   *
   * Time:  O(n + maxValue) - count input values and scan the value range.
   * Space: O(maxValue) - points stored by numeric value.
   *
   * @param nums input values
   * @return maximum points obtainable
   */
  public static int getMaxPoints(int[] nums) {
    if (nums == null || nums.length == 0) {
      return 0;
    }

    int maxVal = 0;
    int[] points = new int[10001]; // problem constraint: nums[i] <= 10^4

    // Step 1: Preprocess - convert array to frequency-weighted points
    for (int num : nums) {
      points[num] += num;
      maxVal = Math.max(maxVal, num);
    }

    // Step 2: Apply House Robber DP on the points array itself
    points[1] = Math.max(points[0], points[1]);

    for (int i = 2; i <= maxVal; i++) {
      points[i] = Math.max(points[i - 1], points[i - 2] + points[i]);
    }

    return points[maxVal];
  }


    public static void main(String[] args) {
        int[][] inputs = { {}, {3, 4, 2}, {2, 2, 3, 3, 3, 4} };
        int[] expected = {0, 6, 9};

        for (int i = 0; i < inputs.length; i++) {
            int output = getMaxPoints(inputs[i]);
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
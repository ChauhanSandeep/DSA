package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: Minimum Flips for Non-negative Sum Closest to Zero
 *
 * Given positive integers, flip the signs of some elements so the final array
 * sum is non-negative and as small as possible. Among choices that achieve that
 * closest non-negative sum, return the fewest flipped elements.
 *
 * Source: InterviewBit Flip Array
 * Pattern:  Dynamic Programming | 0/1 knapsack | Closest half-sum with minimum item count
 *
 * Example:
 *   Input:  arr = [8,4,5,7,6,2]
 *   Output: 3
 *   Why:    flipping values with sum 16 makes the final sum 32 - 2*16 = 0,
 *           and one way to do that uses three elements: 4, 5, and 7.
 *
 * Follow-ups:
 *   1. What if exactly zero is required?
 *      Check whether total is even and whether target total / 2 is reachable.
 *   2. Can space be reduced?
 *      Use a 1D dp array of minimum flip counts and iterate target sums backward.
 *   3. What if elements may be negative already?
 *      Normalize the choice as selecting values whose sign changes, or use offset DP over possible sums.
 */
public class MinimumFlipsForNonNegativeSum {

    /**
   * Intuition: flipping a value subtracts twice that value from the total sum.
   * To make the final sum as close to zero as possible without going negative,
   * choose flipped values with sum as close as possible to total / 2; among ties,
   * choose the fewest values.
   *
   * Algorithm:
   *   1. Compute total and set target to total / 2.
   *   2. Use dp[sum] as the minimum number of flips needed to reach that sum.
   *   3. Process each value backward so it is used at most once.
   *   4. Scan downward from target and return the first reachable flip count.
   *
   * Time:  O(n * total) - each value updates sums up to total / 2.
   * Space: O(total) - one minimum-flip array for reachable half sums.
   *
   * @param arr non-negative values
   * @return minimum flips that produce the smallest non-negative final sum
   */
  public static int minFlipsToNonNegativeClosestZero(int[] arr) {
    int size = arr.length;
    int totalSum = 0;
    for (int num : arr) totalSum += num;

    int target = totalSum / 2; // we want subset sum as close to this as possible

    // dp[i][j] = min elements needed to make sum j using first i items
    int[][] dp = new int[size + 1][target + 1];

    // Initialize: large value = not possible
    for (int i = 0; i <= size; i++) {
      for (int j = 0; j <= target; j++) {
        dp[i][j] = Integer.MAX_VALUE / 2; // avoid overflow
      }
    }
    dp[0][0] = 0; // zero elements to make sum 0

    // Fill DP
    for (int itemIndex = 1; itemIndex <= size; itemIndex++) {
      int currVal = arr[itemIndex - 1];
      for (int currTarget = 0; currTarget <= target; currTarget++) {
        // Option 1: don't take this element
        dp[itemIndex][currTarget] = dp[itemIndex - 1][currTarget];
        // Option 2: take this element (if fits)
        if (currTarget >= currVal) {
          dp[itemIndex][currTarget] = Math.min(dp[itemIndex][currTarget],  // don't take this element
              1 + dp[itemIndex - 1][currTarget - currVal] // take this element
          );
        }
      }
    }

    // Find the best achievable sum ≤ target
    int minFlips = Integer.MAX_VALUE;
    for (int currTarget = target; currTarget >= 0; currTarget--) {
      if (dp[size][currTarget] < Integer.MAX_VALUE / 2) {
        minFlips = dp[size][currTarget];
        break;
      }
    }

    return minFlips;
  }


    public static void main(String[] args) {
        int[][] inputs = { {}, {1}, {8, 4, 5, 7, 6, 2} };
        int[] expected = {0, 0, 3};

        for (int i = 0; i < inputs.length; i++) {
            int output = minFlipsToNonNegativeClosestZero(inputs[i]);
            System.out.printf("arr=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}

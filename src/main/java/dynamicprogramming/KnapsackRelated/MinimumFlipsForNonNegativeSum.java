package dynamicprogramming.KnapsackRelated;

/**
 * Problem: Minimum Flips to Make Sum Non-negative and Closest to Zero
 *
 * Given an array of positive integers, you need to flip the sign of some elements such that
 * the resultant sum of the array is minimum non-negative (as close to zero as possible).
 * Return the minimum number of elements that need to be flipped.
 *
 * Example:
 * Input: arr = [8, 4, 5, 7, 6, 2]
 * Output: 3
 * Explanation: We can flip [8, 4, 5] to get [-8, -4, -5, 7, 6, 2] = -2
 *              But we want non-negative, so we flip [4, 5, 7] to get [8, -4, -5, -7, 6, 2] = 0
 *              Minimum flips needed = 3
 *
 * GeeksforGeeks Problem Link: https://www.geeksforgeeks.org/minimum-flips-required-such-that-sum-of-array-is-non-negative/
 * InterviewBit Problem Link: https://www.interviewbit.com/problems/flip-array/
 *
 * Follow-up Questions:
 * 1. Q: What if we want exactly zero sum instead of closest to zero?
 *    A: Check if totalSum is even and if we can achieve exactly totalSum/2 using subset sum
 * 2. Q: What if we can add elements instead of just flipping signs?
 *    A: This becomes unbounded knapsack where we can add any positive number
 * 3. Q: Find all possible ways to achieve minimum flips?
 *    A: Use backtracking with DP to enumerate all valid combinations
 * 4. Q: What if elements can be negative initially?
 *    A: Separate positive and negative elements, handle them differently in DP transitions
 * 5. Q: Maximize the sum while using minimum flips?
 *    A: Two-phase DP: first minimize flips, then among minimum flips find maximum sum
 */
public class MinimumFlipsForNonNegativeSum {

  /**
   * Returns the minimum number of flips required to make the sum of the array
   * non-negative and as close to zero as possible.
   *
   * Intuition:
   * Flipping an element `x` changes the total sum by `-2*x`. To minimize the non-negative sum,
   * we want to flip a subset of elements whose total is as close as possible to `totalSum/2`.
   *
   * Approach:
   * 1. Calculate total sum of the array.
   * 2. Set target as `totalSum / 2`.
   * 3. Use a DP table where `dp[i][j]` represents the minimum number of elements needed
   *    to achieve sum `j` using the first `i` elements.
   * 4. Fill the DP table considering both taking and not taking each element.
   * 5. Finally, find the largest achievable sum ≤ target and return the corresponding minimum flips.
   *
   * Time Complexity: O(n * target) where n is number of elements and target is totalSum/2
   * Space Complexity: O(n * target) for the DP table
   * @param arr
   * @return
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

    int[] testArray = {8, 4, 5, 7, 6, 2};
    System.out.println("Minimum flips needed: " + minFlipsToNonNegativeClosestZero(testArray));
  }

}

package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: Partition Equal Subset Sum
 *
 * Given positive integers, decide whether they can be split into two subsets
 * with equal sum. Each element must go into exactly one of the two subsets.
 *
 * Leetcode: https://leetcode.com/problems/partition-equal-subset-sum/
 * Rating:   acceptance 49.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | 0/1 knapsack | Subset sum feasibility
 *
 * Example:
 *   Input:  nums = [1,5,11,5]
 *   Output: true
 *   Why:    [11] and [1,5,5] both sum to 11, so the array can be split evenly.
 *
 * Follow-ups:
 *   1. How do you minimize the difference instead of requiring equality?
 *      Compute all reachable subset sums and choose the one closest to total / 2.
 *   2. How do you partition into k equal-sum subsets?
 *      Check divisibility by k, then use backtracking with pruning or bitmask DP.
 *   3. What if negative numbers are allowed?
 *      Use an offset or hash-set based DP over reachable sums instead of a simple non-negative table.
 *
 * Related: Target Sum (494), Last Stone Weight II (1049).
 */
public class EqualSum {

    /**
   * Intuition: an equal partition exists only when the total sum is even. Then one
   * side must have sum total / 2, so the task is the classic subset-sum decision
   * problem with take/skip choices for each number.
   *
   * Algorithm:
   *   1. Compute total and reject odd sums.
   *   2. Search for a subset that reaches total / 2 from the end of the array.
   *   3. Memoize each index/target state to avoid recomputing branches.
   *
   * Time:  O(n * target) - each index/target state is solved once.
   * Space: O(n * target) - memo table plus recursion depth.
   *
   * @param nums input values
   * @return true if nums can be split into two equal-sum subsets
   */
  public static boolean canPartitionRecursive(int[] nums) {
    int total = 0;
    for (int num : nums) total += num;
    if (total % 2 != 0) return false;

    int target = total / 2;
    Boolean[][] dp = new Boolean[nums.length][target + 1];
    return isSubsetSum(nums, nums.length - 1, target, dp);
  }

  /** Returns whether nums[0..index] can form target using memoized take/skip choices. */
  private static boolean isSubsetSum(int[] nums, int index, int target, Boolean[][] dp) {
    if (target == 0) return true;
    if (index == 0) return nums[0] == target;
    if (dp[index][target] != null) return dp[index][target];

    boolean notTake = isSubsetSum(nums, index - 1, target, dp);
    boolean take = false;
    if (nums[index] <= target) {
      take = isSubsetSum(nums, index - 1, target - nums[index], dp);
    }

    return dp[index][target] = take || notTake;
  }

    /**
   * Intuition: after seeing some prefix of the array, all that matters is which
   * sums up to total / 2 are reachable. Each new number either keeps an old sum
   * reachable or extends a smaller reachable sum by its value.
   *
   * Algorithm:
   *   1. Compute total and reject odd sums.
   *   2. Initialize dp[0][0] as reachable.
   *   3. For every item and sum, carry forward skip and take transitions.
   *   4. Return whether the target half-sum is reachable after all items.
   *
   * Time:  O(n * target) - every item/sum state is checked once.
   * Space: O(n * target) - the table stores reachability for each prefix.
   *
   * @param nums input values
   * @return true if nums can be split into two equal-sum subsets
   */
  public static boolean canPartitionIterative(int[] nums) {
    int total = 0;
    for (int num : nums) total += num;
    if (total % 2 != 0) return false;

    int size = nums.length;
    int sum = total / 2;
    boolean[][] dp = new boolean[size][sum + 1];

    for (int i = 0; i < size; i++) {
      dp[i][0] = true;
    }
    // Initialize first row
    if (nums[0] <= sum) {
      dp[0][nums[0]] = true;
    }

    for (int elementIndex = 1; elementIndex < size; elementIndex++) {
      for (int currentSum = 1; currentSum <= sum; currentSum++) {
        boolean notTake = dp[elementIndex - 1][currentSum]; // check if sum can be formed without current element

        boolean take = false;
        if (nums[elementIndex] <= currentSum) {
          take = dp[elementIndex - 1][currentSum - nums[elementIndex]]; // check if remaining sum can be formed by excluding current element
        }
        dp[elementIndex][currentSum] = take || notTake;
      }
    }

    return dp[size - 1][sum];
  }


    public static void main(String[] args) {
        int[][] inputs = { {1}, {1, 5, 11, 5}, {1, 2, 3, 5} };
        boolean[] expected = {false, true, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean output = canPartitionIterative(inputs[i]);
            System.out.printf("nums=%s  ->  %b  expected=%b%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
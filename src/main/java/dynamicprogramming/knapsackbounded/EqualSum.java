package dynamicprogramming.knapsackbounded;

/**
 * Problem Statement:
 * Given an array of positive integers nums, return true if you can partition the array into two subsets
 * such that the sum of the elements in both subsets is equal, or false otherwise.
 *
 * Example:
 * Input: nums = [1,5,11,5]
 * Output: true
 * Explanation: The array can be partitioned as [1,5,5] and [11], both summing to 11.
 *
 * LeetCode Link: https://leetcode.com/problems/partition-equal-subset-sum/
 *
 * Follow-up Questions:
 * 1. What if we need to minimize the absolute difference between the two subset sums instead of making them equal?
 *    - Use DP to find all possible subset sums and find the one closest to total/2, then compute |total - 2*closest|.
 *      Relevant problem: https://leetcode.com/problems/partition-array-into-two-arrays-to-minimize-sum-difference/
 * 2. How to partition into k subsets with equal sums?
 *    - First check if total sum % k == 0, then use backtracking to assign elements to k subsets each summing to sum/k.
 *      Relevant problem: https://leetcode.com/problems/partition-to-k-equal-sum-subsets/
 * 3. What if negative numbers are allowed?
 *    - The problem becomes NP-hard in general, but for small ranges, DP can still work with offset for negatives.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class EqualSum {
  public static void main(String[] args) {
    int[] nums = {1, 5, 11, 5};

    System.out.println("Recursive: " + canPartitionRecursive(nums));  // true
    System.out.println("Iterative: " + canPartitionIterative(nums));  // true
  }

  /**
   * Recursive Approach:
   *
   * Intuition:
   * - If total sum is odd, we can't split into two equal parts.
   * - Else, reduce the problem to checking if subset sum = total / 2 exists (use Subset Sum logic).
   *
   * Algorithm:
   * 1. Calculate total sum; if odd, return false.
   * 2. Set target = total / 2.
   * 3. Use a recursive function with memoization to check if a subset with sum
   *  equal to target can be formed.
   *  4. The recursive function explores two choices at each step:
   *   - Include the current number in the subset (if it doesn't exceed target).
   *   - Exclude the current number and move to the next.
   *   5. Base cases:
   *   - If target is 0, return true (found a valid subset).
   *   - If index is 0, check if nums[0] equals target.
   *   6. Store results in a memo table to avoid recomputation.
   *
   * Time Complexity: O(n * sum)
   * Space Complexity: O(n * sum) for memo + O(n) recursion stack
   */
  public static boolean canPartitionRecursive(int[] nums) {
    int total = 0;
    for (int num : nums) total += num;
    if (total % 2 != 0) return false;

    int target = total / 2;
    Boolean[][] dp = new Boolean[nums.length][target + 1];
    return isSubsetSum(nums, nums.length - 1, target, dp);
  }

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
   * Determines if the array can be partitioned into two subsets with equal sum using space-optimized DP.
   * This is the optimal approach for O(n * target) time and O(target) space.
   *
   * Step-by-step explanation:
   * 1. Compute total sum; if odd or zero, return false (or true for empty array).
   * 2. Target = sum / 2.
   * 3. Use a 2D DP array where dp[i][j] indicates if sum j can be formed using first i elements.
   * 4. Initialize dp[i][0] = true (sum 0 is always
   * possible).
   * 5. Fill the DP table iteratively:
   *   - For each element, for each possible sum from 1 to target:
   *   - Check if we can form the sum by either taking or not taking the current element.
   *   6. The answer will be in dp[n-1][target].
   *
   * Algorithm: 0/1 Knapsack DP (Subset Sum)
   * Time Complexity: O(n * target) - n elements, target up to sum/2.
   * Space Complexity: O(target) - DP array.
   *
   * @param nums the array of positive integers
   * @return true if partition is possible, false otherwise
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


}
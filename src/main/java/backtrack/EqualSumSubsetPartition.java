package backtrack;

import java.util.Arrays;
import java.util.Comparator;


/**
 * Problem:
 * Given an array of integers `nums` and an integer `k`, determine if it can be partitioned
 * into `k` subsets such that each subset has the same sum.
 *
 * Example:
 * Input: nums = [4,3,2,3,5,2,1], k = 4
 * Output: true
 * Explanation: It is possible to divide it into 4 subsets (5), (1, 4), (2,3), (2,3) with equal sums.
 *
 * LeetCode Link: https://leetcode.com/problems/partition-to-k-equal-sum-subsets/
 *
 * Follow-up Questions:
 * 1. How can we optimize further? → Use bitmask DP to reduce repeated states.
 * 2. Can we solve without sorting? → Yes, but sorting helps prune the search space.
 * 3. How to count the number of valid partitions instead of just checking existence?
 */
public class EqualSumSubsetPartition {

  public static void main(String[] args) {
    int[] nums1 = {2, 2, 2, 2, 3, 4, 5};
    int k1 = 4;
    System.out.println(canPartitionKSubsets(nums1, k1)); // true

    int[] nums2 = {2, 3, 1, 2, 3, 4, 5};
    int k2 = 4;
    System.out.println(canPartitionKSubsets(nums2, k2)); // false
  }

  /**
   * Determines if the array can be partitioned into k subsets of equal sum.
   *
   * Intuition & Steps:
   * 1. **Total Sum Check**:
   *    - If the sum of all numbers is not divisible by k, partitioning is impossible.
   * 2. **Target Subset Sum**:
   *    - Each subset must sum to `totalSum / k`.
   * 3. **Greedy Optimization via Sorting**:
   *    - Sort in descending order so that larger numbers are placed earlier.
   *    - This helps prune branches early when sums exceed the target.
   * 4. **Backtracking**:
   *    - Use a `boolean[] used` to track which elements are already placed.
   *    - Fill subsets one by one.
   *    - If current subset reaches target, start forming the next subset.
   * 5. **Early Exit**:
   *    - If only 1 subset remains, it's guaranteed to be valid.
   *
   * Time Complexity: O(k * 2^N) in worst case (NP-Hard)
   * Because each element has two choices: include or exclude (resulting in 2^N combinations) and overall there are k subsets
   * Space Complexity: O(N) for `used` array and recursion stack.
   *
   * @param nums Input array
   * @param k    Number of subsets
   * @return True if partition possible, false otherwise
   */
  public static boolean canPartitionKSubsets(int[] nums, int k) {
    int totalSum = Arrays.stream(nums).sum();
    if (totalSum % k != 0) {
      return false;
    }

    int target = totalSum / k;

    // sort in descending order.
    // This helps in pruning branches early. However, the worst case time complexity is still O(2^N)
    nums = Arrays.stream(nums)
        .boxed()
        .sorted(Comparator.reverseOrder())
        .mapToInt(Integer::intValue)
        .toArray();
    boolean[] used = new boolean[nums.length]; // used[i] = true if nums[i] is used
    return backtrack(nums, used, 0, 0, k, target);
  }

  /**
   * Recursive backtracking to fill subsets.
   */
  private static boolean backtrack(int[] nums, boolean[] used, int startIndex, int currentSum, int remainingGroups,
      int target) {
    if (remainingGroups == 1) {
      // Only 1 subset left
      return true;
    }

    if (currentSum == target) {
      // Start filling the next subset
      return backtrack(nums, used, 0, 0, remainingGroups - 1, target);
    }

    for (int i = startIndex; i < nums.length; i++) {
      if (used[i] || currentSum + nums[i] > target) {
        continue;
      }

      used[i] = true;
      if (backtrack(nums, used, i + 1, currentSum + nums[i], remainingGroups, target)) {
        return true;
      }
      used[i] = false; // backtrack
    }

    return false;
  }
}

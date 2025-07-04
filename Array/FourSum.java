package Array;

import java.util.*;


/**
 * 🔢 Problem: 4Sum
 * https://leetcode.com/problems/4sum/
 *
 * Given an array `nums` of n integers, return all unique quadruplets [a, b, c, d] such that:
 *    a + b + c + d == target
 * Each quadruplet should be sorted and appear only once in the result.
 *
 * ✅ Constraints:
 * - No duplicates in the result
 * - Can have negative and positive numbers
 * - Order of numbers in result doesn't matter
 *
 * 🧠 Example:
 * Input: nums = [-2,-1,-1,1,1,2,2], target = 0
 * Output: [[-2,-1,1,2], [-1,-1,1,1]]
 *
 * 🔁 Follow-up:
 * - Can you generalize this to k-sum? (Yes, this solution does that!)
 * - Can you optimize further for space? (Using streaming logic or iterative control)
 */
public class FourSum {

  public static void main(String[] args) {
    int[] nums = {-2, -1, -1, 1, 1, 2, 2};
    int target = 0;

    List<List<Integer>> result = fourSum(nums, target);
    System.out.println("Quadruplets that sum to " + target + ": " + result);
  }

  /**
   * ✅ Main function to compute all unique quadruplets that sum to the target.
   *
   * @param nums   Input array of integers
   * @param target Target sum
   * @return List of unique quadruplets
   *
   * Time Complexity: O(n^(k-1)) => O(n^3) for 4Sum
   * Space Complexity: O(k * number of combinations) for recursion and result storage
   */
  public static List<List<Integer>> fourSum(int[] nums, int target) {
    Arrays.sort(nums); // Sort to handle duplicates and apply two-pointer
    return kSum(nums, target, 0, 4);
  }

  /**
   * Generalized recursive kSum function (k ≥ 2).
   */
  private static List<List<Integer>> kSum(int[] nums, int target, int start, int k) {
    List<List<Integer>> result = new ArrayList<>();

    // Base case optimizations
    if (start == nums.length || nums[start] * k > target || nums[nums.length - 1] * k < target) {
      return result;
    }

    if (k == 2) {
      return twoSum(nums, target, start);
    }

    for (int i = start; i < nums.length; i++) {
      // Skip duplicates because we want unique quadruplets
        if (i > start && nums[i] == nums[i - 1]) {
            continue;
        }

      int currentNum = nums[i];
      List<List<Integer>> subLists = kSum(nums, target - currentNum, i + 1, k - 1);

      for (List<Integer> sublist : subLists) {
        sublist.add(0, currentNum);  // Add current element to front
        result.add(sublist);
      }
    }

    return result;
  }

  /**
   * Two-pointer based helper for 2Sum in sorted array, starting from `start` index.
   * Skips duplicate pairs to ensure uniqueness.
   */
  private static List<List<Integer>> twoSum(int[] nums, int target, int start) {
    List<List<Integer>> result = new ArrayList<>();
    int low = start, high = nums.length - 1;

    while (low < high) {
      int sum = nums[low] + nums[high];

      if (sum < target || (low > start && nums[low] == nums[low - 1])) {
        low++;  // Skip duplicate or move forward
      } else if (sum > target || (high < nums.length - 1 && nums[high] == nums[high + 1])) {
        high--;  // Skip duplicate or move backward
      } else {
        result.add(Arrays.asList(nums[low++], nums[high--]));
      }
    }

    return result;
  }
}
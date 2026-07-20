package arrays.twopointers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Problem: 4Sum
 *
 * Given an integer array, return all unique quadruplets whose values add up to
 * target. This implementation sorts the values and uses a recursive k-sum that
 * falls back to two pointers at k = 2.
 *
 * Leetcode: https://leetcode.com/problems/4sum/ (Medium)
 * Rating:   acceptance 41.2% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Sorting | Recursive k-sum plus two pointers
 *
 * Example:
 *   Input:  nums = [-2,-1,-1,1,1,2,2], target = 0
 *   Output: [[-2,-1,1,2], [-1,-1,1,1]]
 *   Why:    these are the only unique sorted quadruplets whose four values sum to 0.
 *
 * Follow-ups:
 *   1. Generalize to k-sum for any k?
 *      Keep the recursive reduction and use the two-pointer base case at k = 2.
 *   2. How do you avoid integer overflow in pruning?
 *      Promote target and arithmetic to long before multiplying or subtracting.
 *   3. Return index quadruplets instead of values?
 *      Store original indices before sorting and add duplicate-handling rules for equal values.
 *
 * Related: Two Sum (1), 3Sum (15), 4Sum II (454).
 */
public class FourSum {

  public static void main(String[] args) {
    int[][] cases = {
        {1, 0, -1, 0, -2, 2},
        {2, 2, 2, 2, 2},
        {-2, -1, -1, 1, 1, 2, 2}
    };
    int[] targets = {0, 8, 0};
    String[] expected = {
        "[[-2, -1, 1, 2], [-2, 0, 0, 2], [-1, 0, 0, 1]]",
        "[[2, 2, 2, 2]]",
        "[[-2, -1, 1, 2], [-1, -1, 1, 1]]"
    };

    for (int i = 0; i < cases.length; i++) {
      List<List<Integer>> got = fourSum(cases[i].clone(), targets[i]);
      System.out.printf("nums=%s target=%d -> %s  expected=%s%n",
          Arrays.toString(cases[i]), targets[i], got, expected[i]);
    }
  }

  /**
 * Intuition: 4Sum is just k-sum with k = 4. Sorting gives duplicate control and
 * lets the final two numbers be found by an inward two-pointer scan. Each
 * recursive level fixes one value, then asks for the remaining k - 1 values.
 *
 * Algorithm:
 *   1. Sort nums so duplicates are adjacent and two pointers can be used.
 *   2. Call kSum with target, start index 0, and k = 4.
 *   3. In kSum, fix one unique value and recurse for the smaller sum.
 *   4. At k = 2, collect unique pairs with low and high pointers.
 *
 * Time:  O(n^3) - three effective dimensions remain after sorting for 4Sum.
 * Space: O(k) - recursion depth is bounded by k, excluding the output list.
 *
 * @param nums input array, sorted in place by this method
 * @param target desired quadruplet sum
 * @return unique quadruplets whose values sum to target
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
    if (start == nums.length) {
      return result;
    }
    if (nums[start] * k > target || nums[nums.length - 1] * k < target) {
      // If the smallest number in the remaining array is too large or the largest number is too small, return empty result
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
        result.add(new ArrayList<>(Arrays.asList(nums[low++], nums[high--])));
      }
    }

    return result;
  }
}
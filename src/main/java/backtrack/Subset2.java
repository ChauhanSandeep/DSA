package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Problem:
 * Given an integer array `nums` that may contain duplicates, return all possible subsets (the power set),
 * ensuring that each subset is unique.
 *
 * Example:
 * Input: nums = [2, 1, 2]
 * Output: [[], [1], [1, 2], [1, 2, 2], [2], [2, 2]]
 *
 * LeetCode Link: https://leetcode.com/problems/subsets-ii/
 *
 * Follow-up Questions:
 * 1. How to return subsets in lexicographical order? → Sorting handles this automatically.
 * 2. How to solve without sorting? → Use a `Set<List<Integer>>` but sorting is more efficient.
 * 3. How to solve iteratively instead of backtracking? → Use iterative subset generation with duplicate check.
 */
public class Subset2 {

  public static void main(String[] args) {
    int[] nums = {2, 1, 2};
    List<List<Integer>> subsets = subsetsWithDup(nums);
    System.out.println(subsets);
  }

  /**
   * Generates all unique subsets from the given array, even when it contains duplicates.
   *
   * Intuition & Steps:
   * 1. Sort the array → Ensures duplicates are adjacent, making them easy to skip.
   * 2. Backtracking:
   *    - At each step, decide whether to include the current element.
   *    - Add the current subset to the result list at every call.
   * 3. Skip duplicates:
   *    - When encountering the same element at the same recursion level, skip it.
   *    - Condition: `if (i > index && nums[i] == nums[i - 1]) continue;`
   * 4. Backtrack:
   *    - After exploring one path, remove the last added element before exploring the next.
   *
   * Time Complexity: O(2^N) — Each element has two choices: include or exclude.
   * Space Complexity: O(N) — Recursion stack depth and temporary subset storage.
   *
   * @param nums Input array containing possible duplicates.
   * @return List of unique subsets.
   */
  public static List<List<Integer>> subsetsWithDup(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums); // Sorting helps in easily detecting duplicates
    generateUniqueSubsets(nums, 0, new ArrayList<>(), result);
    return result;
  }

  /**
   * Recursive helper to generate subsets, skipping duplicates.
   *
   * @param nums Sorted input array.
   * @param index Current index to process.
   * @param current Current subset being built.
   * @param result Final list of unique subsets.
   */
  private static void generateUniqueSubsets(int[] nums, int index, List<Integer> current, List<List<Integer>> result) {
    result.add(new ArrayList<>(current));

    for (int i = index; i < nums.length; i++) {
      // Skip duplicates in the same recursion branch, because the same number is already in subset using nums[i - 1]
      if (i > index && nums[i] == nums[i - 1]) {
        continue;
      }

      // Include nums[i] in current subset
      current.add(nums[i]);

      // Recurse for the next element
      generateUniqueSubsets(nums, i + 1, current, result);

      // Backtrack: remove last added element
      current.remove(current.size() - 1);
    }
  }
}

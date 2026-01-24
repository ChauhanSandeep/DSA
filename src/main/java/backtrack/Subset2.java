package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Given an integer array nums that may contain duplicates, return all possible subsets
 * (the power set). The solution set must not contain duplicate subsets. Return the
 * answer in any order.
 *
 * The key challenge is handling duplicate elements in the input array to avoid
 * generating duplicate subsets. Unlike the basic subsets problem, we need special
 * logic to skip consecutive duplicates during backtracking.
 *
 * Example:
 * Input: nums = [1,2,2]
 * Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
 * Explanation: Notice we don't have two [1,2] subsets even though there are two 2's.
 * The algorithm groups duplicates and skips them appropriately to avoid redundant subsets.
 *
 * LeetCode: https://leetcode.com/problems/subsets-ii/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you modify this to find subsets of a specific size k with duplicates?
 *    Answer: Add size parameter to backtracking and stop when currentSubset.size() == k.
 * 2. What if you need to return subsets in lexicographic order?
 *    Answer: Current approach already provides lexicographic order due to sorting and systematic exploration.
 * 3. How to optimize memory usage when input contains many duplicates?
 *    Answer: Use count-based approach or iterative generation with space-efficient representations.
 * 4. What if we need to generate subsets with specific constraints (sum, product, etc.)?
 *    Answer: Add constraint validation during backtracking with early pruning for invalid branches.
 *
 * Related Problems:
 * - LeetCode 78: Subsets (Without duplicates)
 * - LeetCode 40: Combination Sum II (Sum with duplicates)
 * - LeetCode 47: Permutations II (Permutations with duplicates)
 * LeetCode Contest Rating: Not available (not a contest problem)
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
    List<List<Integer>> allSubsets = new ArrayList<>();
    Arrays.sort(nums); // Sorting helps in easily detecting duplicates
    generateUniqueSubsets(nums, 0, new ArrayList<>(), allSubsets);
    return allSubsets;
  }

  /**
   * Recursive helper to generate subsets, skipping duplicates.
   */
  private static void generateUniqueSubsets(int[] nums, int index, List<Integer> currentSubset, List<List<Integer>> allSubsets) {
    allSubsets.add(new ArrayList<>(currentSubset));

    for (int i = index; i < nums.length; i++) {
      // Skip duplicates in the same recursion branch, because the same number is already in subset using nums[i - 1]
      if (i > index && nums[i] == nums[i - 1]) {
        continue;
      }

      // Include nums[i] in current subset
      currentSubset.add(nums[i]);

      // Recurse for the next element
      generateUniqueSubsets(nums, i + 1, currentSubset, allSubsets);

      // Backtrack: remove last added element
      currentSubset.remove(currentSubset.size() - 1);
    }
  }

  /**
   * Iterative approach to generate all unique subsets from the given array with duplicates.
   * Steps:
   * 1. Sort the array to group duplicates together.
   * 2. Start with an empty subset.
   * 3. For each number, add it to all existing subsets to create new subsets.
   * 4. If the current number is a duplicate of the previous, only add it to subsets
   *    created in the previous step to avoid duplicates.
   *
   * Example
   * input [1, 2, 2]
   * 	1.	Sort → [1, 2, 2]
   * 	2.	Start: [[]]
   * 	3.	Add 1 → [[], [1]]
   * 	4.	Add 2 (first 2): expand all [[], [1]] → [[], [1], [2], [1,2]]
   * 	5.	Add 2 (second 2): it’s duplicate, so expand only subsets created in last step ([2], [1,2]) → [[], [1], [2], [1,2], [2,2], [1,2,2]]
   *
   * Time Complexity: O(2^N) - Each element can either be included or excluded.
   * Space Complexity: O(2^N) - Storing all subsets.
   */
  public List<List<Integer>> subsetsWithDupIterative(int[] nums) {
    Arrays.sort(nums); // Step 1: sort to group duplicates together

    List<List<Integer>> allSubsets = new ArrayList<>();
    allSubsets.add(new ArrayList<>());

    int startIndex = 0, endIndex = 0;

    for (int i = 0; i < nums.length; i++) {
      startIndex = 0;

      // Step 2: if current element is duplicate, only expand subsets created in the previous iteration
      if (i > 0 && nums[i] == nums[i - 1]) {
        startIndex = endIndex + 1;
      }

      endIndex = allSubsets.size() - 1;

      for (int j = startIndex; j <= endIndex; j++) {
        List<Integer> newSubset = new ArrayList<>(allSubsets.get(j));
        newSubset.add(nums[i]);
        allSubsets.add(newSubset);
      }
    }
    return allSubsets;
  }
}

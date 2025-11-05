package Recursion;

import java.util.ArrayList;
import java.util.List;


/**
 * Combination Sum
 *
 * Given an array of distinct integers candidates and a target integer, return all unique combinations
 * of candidates where the chosen numbers sum to target. The same number may be chosen unlimited times.
 *
 * Example:
 * Input: candidates = [2,3,6,7], target = 7
 * Output: [[2,2,3],[7]]
 * Explanation: 2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
 *              7 is a candidate, and 7 = 7.
 *
 * Constraints:
 * - 1 <= candidates.length <= 30
 * - 2 <= candidates[i] <= 40
 * - All elements of candidates are distinct
 * - 1 <= target <= 40
 *
 * LeetCode: https://leetcode.com/problems/combination-sum/
 *
 * Follow-up Questions:
 * Q1: What if each number can be used only once instead of unlimited times?
 * A1: This becomes Combination Sum II - modify to increment index after picking a number.
 *
 * Q2: How can we optimize if candidates are sorted?
 * A2: Sort candidates and break early when current candidate exceeds remaining target (pruning).
 *
 * Q3: What if we only want combinations of exactly k numbers?
 * A3: Add a count parameter and only add to result when count == k and target == 0.
 *
 * Q4: How would you find the number of combinations instead of listing them all?
 * A4: Use dynamic programming with dp[i] = number of ways to make sum i, or modify backtracking to count.
 *
 * Q5: What if negative numbers are allowed in candidates?
 * A5: The problem becomes more complex - need to handle potential infinite loops and use memoization.
 */
public class CombinationSum {
  public static void main(String[] args) {
    int[] candidates = {2, 3, 6, 7};
    int target = 7;

    List<List<Integer>> result = new ArrayList<>();
    backtrack(candidates, 0, target, new ArrayList<>(), result);

    System.out.println("Combinations that sum to " + target + ": " + result);
  }

  /**
   * Recursive backtracking helper to find all combinations that sum to target.
   *
   * Algorithm:
   * 1. Base case: If target is 0, we found a valid combination
   * 2. For each candidate at current index, we have two choices:
   *    a. Include the candidate (can be reused, so stay at same index)
   *    b. Skip the candidate (move to next index)
   * 3. Backtrack by removing the last added element before exploring next option
   *
   * Time Complexity: O(2^T) where T is the target value (worst case)
   * Space Complexity: O(T) for recursion depth
   *
   * @param candidates Array of distinct candidate numbers
   * @param startIndex Current position in candidates array
   * @param remainingTarget Remaining sum needed to reach target
   * @param currentCombination Current combination being built
   * @param result List to store all valid combinations
   */
  private static void backtrack(int[] candidates, int startIndex, int remainingTarget,
      List<Integer> currentCombination, List<List<Integer>> result) {
    // Base case: found a valid combination
    if (remainingTarget == 0) {
      result.add(new ArrayList<>(currentCombination));
      return;
    }

    // Base case: exceeded bounds or target
    if (startIndex >= candidates.length || remainingTarget < 0) {
      return;
    }

    // Choice 1: Include current candidate (can reuse it, so stay at same index)
    currentCombination.add(candidates[startIndex]);
    backtrack(candidates, startIndex, remainingTarget - candidates[startIndex], currentCombination, result);
    currentCombination.remove(currentCombination.size() - 1); // Backtrack

    // Choice 2: Skip current candidate and move to next
    backtrack(candidates, startIndex + 1, remainingTarget, currentCombination, result);
    // No need to remove here since we didn't add anything in this branch
  }
}

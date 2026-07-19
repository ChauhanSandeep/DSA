package Recursion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Problem: Combination Sum
 *
 * Given distinct positive integers and a target, return every unique combination
 * whose values add up to target. A candidate may be chosen unlimited times, but
 * the same multiset should not be repeated in a different order.
 *
 * Leetcode: https://leetcode.com/problems/combination-sum/ (Medium)
 * Rating:   acceptance 76.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Recursion | Backtracking | Include current candidate or skip forward
 *
 * Example:
 *   Input:  candidates = [2,3,6,7], target = 7
 *   Output: [[2,2,3], [7]]
 *   Why:    2 can be reused to make 2 + 2 + 3, and 7 alone also reaches
 *           the target; the other choices either overshoot or cannot finish.
 *
 * Follow-ups:
 *   1. What changes if each number can be used at most once?
 *      Recurse with the next index after choosing a value, which is Combination Sum II.
 *   2. How would you prune faster when candidates are sorted?
 *      Stop exploring a branch as soon as the current candidate exceeds the remaining sum.
 *   3. How would you count combinations instead of listing them?
 *      Use dynamic programming over sums, or keep the same recursion and return counts.
 *   4. What if negative candidates are allowed?
 *      Add usage bounds or visited states; otherwise reuse can create infinite recursion.
 *
 * Related: Combination Sum II (40), Combination Sum III (216), Combination Sum IV (377).
 */
public class CombinationSum {
  public static void main(String[] args) {
    int[][] candidates = { {2, 3, 6, 7}, {5}, {2, 3, 5} };
    int[] targets = { 7, 3, 8 };
    String[] expected = {
        "[[2, 2, 3], [7]]",
        "[]",
        "[[2, 2, 2, 2], [2, 3, 3], [3, 5]]"
    };

    for (int i = 0; i < candidates.length; i++) {
      List<List<Integer>> result = new ArrayList<>();
      backtrack(candidates[i], 0, targets[i], new ArrayList<>(), result);

      System.out.printf("candidates=%s target=%d -> %s  expected=%s%n",
          Arrays.toString(candidates[i]), targets[i], result, expected[i]);
    }
  }

  /**
   * Intuition: think of the recursion as a take-or-skip decision tree. At
   * startIndex, taking candidates[startIndex] keeps the index fixed so that the
   * same value can be reused, while skipping it moves to the next value so the
   * same combination cannot appear in a different order. A branch becomes an
   * answer when remainingTarget reaches 0, and it dies when the target is
   * negative or there are no candidates left.
   *
   * Algorithm:
   *   1. Add currentCombination to result when remainingTarget is exactly 0.
   *   2. Return when startIndex is past the array or remainingTarget is negative.
   *   3. Choose candidates[startIndex], recurse at the same index, then un-choose it.
   *   4. Skip candidates[startIndex] by recursing at startIndex + 1.
   *
   * Time:  O(2^t) - in the worst case each remaining target state can branch
   *        into taking the current value or skipping it.
   * Space: O(t) - the recursion stack and currentCombination grow with the
   *        longest chain of chosen positive values.
   *
   * @param candidates array of distinct candidate numbers
   * @param startIndex current position in candidates
   * @param remainingTarget remaining sum needed to reach the target
   * @param currentCombination current combination being built
   * @param result list that stores valid combinations
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

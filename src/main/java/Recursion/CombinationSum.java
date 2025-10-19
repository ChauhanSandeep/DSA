package Recursion;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Find all unique combinations in the given array where the numbers sum up to a target.
 * Each number can be used multiple times.
 *
 * Approach:
 * - Use **backtracking** to explore all possible combinations.
 * - Start from a given index and recursively pick elements while the sum is within the target.
 * - Use **DFS (Depth-First Search)** with a decision tree.
 * - **Prune unnecessary calls** when the current sum exceeds the target.
 *
 * Time Complexity: O(2^N) (Exponential in the worst case due to multiple choices at each step)
 * Space Complexity: O(N) (Recursion stack depth)
 *
 * LeetCode Link: https://leetcode.com/problems/combination-sum/
 */
public class CombinationSum {
  public static void main(String[] args) {
    int[] candidates = {2, 3, 6, 7};
    int target = 7;

    List<List<Integer>> result = new ArrayList<>();
    backtrack(candidates, 0, target, new ArrayList<>(), result);

    System.out.println(result);
  }

  private static void backtrack(int[] candidates, int index, int target, List<Integer> current,
      List<List<Integer>> result) {
    // Base case: valid combination
    if (target == 0) {
      result.add(new ArrayList<>(current));
      return;
    }

    // Base case: index out of bounds or target exceeded
    if (index >= candidates.length || target < 0) {
      return;
    }

    // 1. Pick the current number and stay at same index (allow reuse)
    current.add(candidates[index]);
    backtrack(candidates, index, target - candidates[index], current, result);
    current.remove(current.size() - 1); // backtrack

    // 2. Skip the current number and move to next index
    backtrack(candidates, index + 1, target, current, result);
  }
}

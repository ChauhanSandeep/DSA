package recursion;

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
        
        List<List<Integer>> result = findCombinationSum(candidates, target);
        System.out.println(result);
    }

    /**
     * Finds all unique combinations that sum up to the target.
     *
     * @param candidates Array of positive integers (no duplicates).
     * @param target     Target sum to achieve.
     * @return List of all possible unique combinations.
     */
    public static List<List<Integer>> findCombinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * Recursive backtracking function to generate valid combinations.
     *
     * @param candidates Input array of numbers.
     * @param target     Remaining sum to be achieved.
     * @param index      Current index in the array.
     * @param current    Temporary list storing the current combination.
     * @param result     Final list containing all valid combinations.
     */
    private static void backtrack(int[] candidates, int target, int index, List<Integer> current, List<List<Integer>> result) {
        // Base Case: If target is 0, add the current combination to the result.
        if (target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        // Iterate over candidates starting from 'index' to avoid duplicate subsets.
        for (int i = index; i < candidates.length; i++) {
            if (candidates[i] > target) continue; // Prune unnecessary calls.

            // Choose the number
            current.add(candidates[i]);
            // Recur with reduced target (candidates[i] can be used again)
            backtrack(candidates, target - candidates[i], i, current, result);
            // Undo the choice (Backtrack)
            current.remove(current.size() - 1);
        }
    }
}

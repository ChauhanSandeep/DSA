package Recursion;

import java.util.*;

/**
 * Problem: Find all unique combinations where each number in the array may be used only once, and
 * the sum of the selected numbers equals the target.
 *
 * LeetCode: https://leetcode.com/problems/combination-sum-ii/
 *
 * Intuition:
 * - This is a variation of the subset-sum problem with two constraints:
 *     1. Each number may be used at most once.
 *     2. No duplicate combinations allowed (input can have duplicates).
 *
 * Approach:
 * - Sort the input array to bring duplicates together.
 * - Recursively explore the decision tree using **explicit branching**:
 *     - PICK the current number → move to next index (index + 1), subtract from target.
 *     - SKIP the current number → move to next index that has a different number to avoid duplicates.
 * - Base cases:
 *     - If target becomes 0 → add current path to result.
 *     - If index goes out of bounds or target becomes negative → terminate branch.
 * - This solution avoids using a `for` loop and clearly separates the decision branches for better traceability.
 *
 * Time Complexity: O(2^N) in the worst case (exponential combinations)
 * Space Complexity: O(N) for the recursion stack + current list
 *
 * Trade-offs:
 * - + More control and clarity in the recursion flow by modeling it as binary decisions.
 * - - Slightly more verbose than a loop-based backtracking solution.
 */
public class CombinationSum2 {

    public static void main(String[] args) {
        int[] candidates = {10, 1, 2, 7, 6, 1, 5};
        int target = 8;

        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates); // Sorting is essential for duplicate-skipping
        backtrack(candidates, 0, target, new ArrayList<>(), result);

        System.out.println(result);
    }

    /**
     * Recursively explores the combinations by choosing to include or exclude the current number.
     *
     * @param candidates Sorted array of input numbers.
     * @param index      Current position in the array.
     * @param target     Remaining sum to reach.
     * @param current    Current combination being built.
     * @param result     Final list of all valid unique combinations.
     */
    private static void backtrack(int[] candidates, int index, int target, List<Integer> current, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (index >= candidates.length || target < 0) return;

        // ---- PICK current number ----
        current.add(candidates[index]);
        backtrack(candidates, index + 1, target - candidates[index], current, result);
        current.remove(current.size() - 1); // backtrack

        // ---- SKIP current number and all its duplicates ----
        int next = index + 1;
        while (next < candidates.length && candidates[next] == candidates[index]) {
            next++;
        }

        backtrack(candidates, next, target, current, result);
    }
}
package Recursion;

import java.util.*;

/**
 * Problem: Combination Sum II
 *
 * Given candidate numbers that may contain duplicates and a target, return all
 * unique combinations that add up to target. Each array element may be used at
 * most once, even when another element has the same value.
 *
 * Leetcode: https://leetcode.com/problems/combination-sum-ii/ (Medium)
 * Rating:   acceptance 59.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Recursion | Backtracking | Sort-to-dedupe with one-use choices
 *
 * Example:
 *   Input:  candidates = [10,1,2,7,6,1,5], target = 8
 *   Output: [[1,1,6], [1,2,5], [1,7], [2,6]]
 *   Why:    each listed group uses array entries once and sums to 8; sorting
 *           lets equal sibling choices be skipped so duplicates are emitted once.
 *
 * Follow-ups:
 *   1. How is this different from Combination Sum I?
 *      After choosing a value, recurse from the next index instead of the same index.
 *   2. Can dynamic programming list all combinations more cleanly?
 *      DP can count or test reachability, but generating deduplicated lists is usually messier.
 *   3. How would you return only combinations with minimum length?
 *      Track the best length seen and keep only results matching that length.
 *   4. How do you handle a huge input with a small target?
 *      Sort, drop values above target, and stop a loop once the candidate exceeds the remainder.
 *
 * Related: Combination Sum (39), Subsets II (90), Combination Sum III (216).
 */
public class CombinationSum2 {

    /**
     * Intuition: sorting turns duplicates into adjacent choices. The recursion
     * builds a combination from left to right, and each chosen element recurses
     * with i + 1 so that exact array position cannot be reused. Duplicate values
     * are only dangerous when they are sibling choices at the same recursion
     * level, because they would start identical subtrees; skipping those siblings
     * still allows duplicates at deeper levels, such as [1,1,6].
     *
     * Algorithm:
     *   1. Sort candidates so duplicates are adjacent and larger values can stop a loop.
     *   2. From index, try each remaining position as the next chosen value.
     *   3. Break when candidates[i] is larger than target.
     *   4. Skip duplicate sibling values, then choose, recurse from i + 1, and un-choose.
     *
     * Time:  O(2^n * n) - the subset tree can be exponential, and copying an
     *        answer may touch up to n values.
     * Space: O(n) - the recursion stack and current combination can grow to the input length.
     *
     * @param candidates values that may contain duplicates and may be used once each
     * @param target target sum to form
     * @return all unique combinations that add up to target
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.length == 0) return result;
        
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    /** Explores all combinations using backtracking with duplicate skipping. */
    private void backtrack(int[] candidates, int target, int index, 
                          List<Integer> curr, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(curr));
            return;
        }
        if (target < 0 || index >= candidates.length) return;
        
        for (int i = index; i < candidates.length; i++) {
            // Early termination: sorted array, all subsequent values will be too large
            if (candidates[i] > target) break;
            
            // Skip duplicates at same recursion level to avoid duplicate combinations
            if (i > index && candidates[i] == candidates[i - 1]) continue;
            
            curr.add(candidates[i]);
            backtrack(candidates, target - candidates[i], i + 1, curr, result);
            curr.remove(curr.size() - 1);
        }
    }

    public static void main(String[] args) {
        CombinationSum2 solver = new CombinationSum2();

        int[][] candidates = { {10, 1, 2, 7, 6, 1, 5}, {1}, {2, 5, 2, 1, 2} };
        int[] targets = { 8, 2, 5 };
        String[] expected = {
            "[[1, 1, 6], [1, 2, 5], [1, 7], [2, 6]]",
            "[]",
            "[[1, 2, 2], [5]]"
        };

        for (int i = 0; i < candidates.length; i++) {
            int[] input = candidates[i].clone();
            List<List<Integer>> got = solver.combinationSum2(input, targets[i]);
            System.out.printf("candidates=%s target=%d -> %s  expected=%s%n",
                Arrays.toString(candidates[i]), targets[i], got, expected[i]);
        }
    }
}
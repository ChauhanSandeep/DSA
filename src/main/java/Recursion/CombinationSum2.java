package Recursion;

import java.util.*;

/**
 * Combination Sum II
 *
 * Problem:
 * Given a collection of candidate numbers and a target number, find all unique combinations
 * where the candidate numbers sum to target. Each number in candidates may only be used once.
 *
 * Example:
 * Input: candidates = [10,1,2,7,6,1,5], target = 8
 * Output: [[1,1,6],[1,2,5],[1,7],[2,6]]
 * Explanation: These are the only unique combinations that sum to 8.
 *
 * Constraints:
 * - 1 <= candidates.length <= 100
 * - 1 <= candidates[i] <= 50
 * - 1 <= target <= 30
 * - The input array may contain duplicates
 *
 * LeetCode: https://leetcode.com/problems/combination-sum-ii/
 *
 * Follow-up Questions:
 * Q1: How does this differ from Combination Sum I?
 * A1: In Sum I, each number can be reused unlimited times. In Sum II, each can be used at most once.
 *
 * Q2: Why is sorting necessary for this problem?
 * A2: Sorting groups duplicate values together, making it easier to skip duplicates and avoid duplicate combinations.
 *
 * Q3: What if we want to find combinations with the minimum number of elements?
 * A3: Track combination size and update result only when a smaller size is found, or sort final results by size.
 *
 * Q4: Can we use dynamic programming instead of backtracking?
 * A4: DP can count combinations but won't efficiently generate all actual combinations due to state complexity.
 *
 * Q5: How would you optimize if the array is very large but target is small?
 * A5: Pre-filter candidates to include only those <= target, and prune branches early when target becomes negative.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class CombinationSum2 {

    /**
     * Finds all unique combinations that sum to target using backtracking.
     * 
     * Algorithm:
     * 1. Sort candidates to group duplicates and enable early termination
     * 2. Use backtracking to explore combinations
     * 3. Skip duplicates at same recursion level to avoid duplicate combinations
     * 4. Each element used at most once (pass i+1 in recursive call)
     * 
     * Time Complexity: O(2^n) - explore all subsets with pruning
     * Space Complexity: O(k) - recursion depth where k = target/min(candidates)
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.length == 0) return result;
        
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    // Explores all combinations using backtracking with duplicate skipping
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
}
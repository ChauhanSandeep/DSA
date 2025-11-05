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
 */
public class CombinationSum2 {

    public static void main(String[] args) {
        int[] candidates = {10, 1, 2, 7, 6, 1, 5};
        int target = 8;

        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates); // Essential: brings duplicates together for proper skipping
        backtrack(candidates, 0, target, new ArrayList<>(), result);

        System.out.println("Unique combinations that sum to " + target + ": " + result);
    }

    /**
     * Recursively explores combinations using explicit branching to avoid duplicates.
     *
     * Algorithm:
     * 1. Base case: If target is 0, we found a valid combination
     * 2. Base case: If index out of bounds or target negative, terminate
     * 3. Two choices at each step:
     *    a. PICK current number (move to next index, each number used at most once)
     *    b. SKIP current number AND all its duplicates to avoid duplicate combinations
     * 4. Backtrack after exploring each branch
     *
     * Key Insight: Sorting brings duplicates together. When skipping, skip all duplicate values
     * to ensure we don't generate duplicate combinations.
     *
     * Time Complexity: O(2^N) - exponential due to exploring all combinations
     * Space Complexity: O(N) - recursion stack depth
     *
     * @param candidates Sorted array of candidate numbers
     * @param currentIndex Current position in candidates array
     * @param remainingTarget Remaining sum needed to reach target
     * @param currentCombination Current combination being built
     * @param result List storing all valid unique combinations
     */
    private static void backtrack(int[] candidates, int currentIndex, int remainingTarget,
        List<Integer> currentCombination, List<List<Integer>> result) {
        // Base case: found valid combination
        if (remainingTarget == 0) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        // Base case: bounds check and early termination
        if (currentIndex >= candidates.length || remainingTarget < 0) return;

        // Choice 1: Pick current number (use once, move to next index)
        currentCombination.add(candidates[currentIndex]);
        backtrack(candidates, currentIndex + 1, remainingTarget - candidates[currentIndex], currentCombination, result);
        currentCombination.remove(currentCombination.size() - 1); // Backtrack

        // Choice 2: Skip current number and all its duplicates, because this number would already be considered in the pick branch
        int nextUniqueIndex = currentIndex + 1;
        while (nextUniqueIndex < candidates.length && candidates[nextUniqueIndex] == candidates[currentIndex]) {
            nextUniqueIndex++;
        }

        backtrack(candidates, nextUniqueIndex, remainingTarget, currentCombination, result);
    }
}
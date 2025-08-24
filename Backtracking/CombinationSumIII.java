package Backtracking;

import java.util.*;

/**
 * 216. Combination Sum III
 *
 * Problem: Find all valid combinations of k numbers that sum up to n such that:
 * - Only numbers 1-9 are used
 * - Each number is used at most once
 * - Return a list of all possible valid combinations
 *
 * Example:
 * Input: k = 3, n = 7
 * Output: [[1,2,4]]
 * Explanation: 1 + 2 + 4 = 7, there are no other valid combinations.
 *
 * LeetCode: https://leetcode.com/problems/combination-sum-iii
 *
 * Follow-up questions:
 * Q: What if we allow numbers to be used multiple times?
 * A: Modify backtracking to not increment start index when recursing.
 *
 * Q: How to handle larger ranges (e.g., 1-100)?
 * A: Same algorithm works, just adjust the upper bound in the loop.
 *
 * Q: Can we optimize using mathematical constraints?
 * A: Yes, early termination when sum exceeds target or remaining numbers can't reach target.
 */
public class CombinationSumIII {

    /**
     * Finds all combinations of k unique numbers from 1-9 that sum to n.
     *
     * Algorithm: Backtracking
     * - Try each number from 1-9 as potential candidates
     * - For each number, recursively find combinations with remaining count and target
     * - Backtrack by removing current number and trying next
     * - Prune branches when impossible to reach target
     *
     * Time Complexity: O(C(9,k)) - combinations of k items from 9 numbers
     * Space Complexity: O(k) for recursion stack and current combination
     */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        backtrack(result, current, k, n, 1);
        return result;
    }

    // Backtracking helper
    private void backtrack(List<List<Integer>> result, List<Integer> current, int k, int target, int start) {
        // Base cases
        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (k == 0 || target < 0) {
            return;
        }

        // Try numbers from start to 9
        for (int i = start; i <= 9; i++) {
            // Early termination: if current number exceeds target, no point continuing
            if (i > target) {
                break;
            }

            // Early termination: check if remaining numbers can reach target
            if (!canReachTarget(i + 1, k - 1, target - i)) {
                break;
            }

            current.add(i);
            backtrack(result, current, k - 1, target - i, i + 1);
            current.remove(current.size() - 1);
        }
    }

    // Check if it's possible to reach target with remaining numbers
    private boolean canReachTarget(int start, int count, int target) {
        if (count == 0) {
            return target == 0;
        }

        // Minimum possible sum with 'count' numbers starting from 'start'
        int minSum = 0;
        for (int i = 0; i < count && start + i <= 9; i++) {
            minSum += start + i;
        }

        // Maximum possible sum with 'count' numbers ending at 9
        int maxSum = 0;
        for (int i = 0; i < count; i++) {
            maxSum += 9 - i;
        }

        return target >= minSum && target <= maxSum;
    }

    /**
     * Optimized version with better pruning conditions.
     * Includes more aggressive early termination.
     */
    public List<List<Integer>> combinationSum3Optimized(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();

        // Early check: minimum possible sum is 1+2+...+k, maximum is (10-k)+...+9
        int minPossible = k * (k + 1) / 2;
        int maxPossible = k * (19 - k) / 2;

        if (n < minPossible || n > maxPossible) {
            return result;
        }

        backtrackOptimized(result, new ArrayList<>(), k, n, 1);
        return result;
    }

    // Optimized backtracking with better pruning
    private void backtrackOptimized(List<List<Integer>> result, List<Integer> current, int k, int target, int start) {
        if (k == 0) {
            if (target == 0) {
                result.add(new ArrayList<>(current));
            }
            return;
        }

        // Calculate bounds for remaining numbers
        int minRemaining = (2 * start + k - 1) * k / 2;
        int maxRemaining = (2 * 9 - k + 1) * k / 2;

        if (target < minRemaining || target > maxRemaining) {
            return;
        }

        for (int i = start; i <= 9 && i <= target; i++) {
            // Skip if impossible to complete with remaining slots
            int remainingTarget = target - i;
            int remainingCount = k - 1;

            if (remainingCount > 0) {
                int minForRemaining = (2 * (i + 1) + remainingCount - 1) * remainingCount / 2;
                int maxForRemaining = (2 * 9 - remainingCount + 1) * remainingCount / 2;

                if (remainingTarget < minForRemaining || remainingTarget > maxForRemaining) {
                    continue;
                }
            }

            current.add(i);
            backtrackOptimized(result, current, k - 1, target - i, i + 1);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Alternative iterative approach using bit manipulation.
     * Generates all possible combinations and filters valid ones.
     */
    public List<List<Integer>> combinationSum3Iterative(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();

        // Generate all possible combinations using bitmask
        for (int mask = 0; mask < (1 << 9); mask++) {
            if (Integer.bitCount(mask) == k) {
                List<Integer> combination = new ArrayList<>();
                int sum = 0;

                for (int i = 0; i < 9; i++) {
                    if ((mask & (1 << i)) != 0) {
                        combination.add(i + 1);
                        sum += i + 1;
                    }
                }

                if (sum == n) {
                    result.add(combination);
                }
            }
        }

        return result;
    }
}
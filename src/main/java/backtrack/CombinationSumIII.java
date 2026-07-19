package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Combination Sum III
 *
 * Choose exactly k distinct numbers from 1..9 so their sum is n. Each number
 * may be used at most once, and the answer contains combinations, not ordered
 * permutations.
 *
 * Leetcode: https://leetcode.com/problems/combination-sum-iii/
 * Rating:   acceptance 73.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Fixed-size combinations | Sum pruning
 *
 * Example:
 *   Input:  k = 3, n = 9
 *   Output: [[1, 2, 6], [1, 3, 5], [2, 3, 4]]
 *   Why:    each listed triple uses distinct digits from 1..9 and sums to 9;
 *           every other 3-digit choice is either too small or too large.
 *
 * Follow-ups:
 *   1. Return the k-th valid combination in lexicographic order?
 *      Count feasible suffixes with bounded subset-sum DP and skip whole blocks.
 *   2. Expand the digit range to 1..m where m is large?
 *      Keep the same DFS, but prune with arithmetic-series min/max bounds.
 *   3. Count valid combinations without listing them?
 *      Use DP over (next number, remaining picks, remaining sum).
 *   4. Allow repeated numbers like Combination Sum?
 *      Recurse without advancing the start after choosing a candidate.
 *
 * Related: Combination Sum (39), Combination Sum II (40), Combinations (77).
 */
public class CombinationSumIII {
    private static final int MIN_DIGIT = 1;
    private static final int MAX_DIGIT = 9;

    /**
     * Intuition: this is a fixed-size combination search with a running sum.
     * We choose digits in increasing order so the same set is never produced in
     * a different order. The key pruning is arithmetic: if the smallest possible
     * remaining digits already exceed the target, or the largest possible
     * remaining digits still fall short, that branch can never recover. Because
     * digits are positive and limited to 1..9, these bounds are cheap and exact.
     *
     * Algorithm:
     *   1. Reject k/n pairs that are outside the global smallest and largest sums
     *      any k distinct digits from 1..9 can make.
     *   2. Start a DFS at digit 1, carrying how many slots remain and how much sum
     *      is still needed.
     *   3. Before expanding a DFS state, compare the remaining target with the
     *      smallest and largest suffix sums possible from the current start digit.
     *   4. Try each candidate digit that leaves enough larger digits for the rest;
     *      add it, recurse with one fewer slot, then remove it before trying the next.
     *
     * Time:  O(C(9,k) * k) - valid combinations are copied, and pruning cuts dead branches.
     * Space: O(k) recursion depth and current-combination buffer, excluding output.
     *
     * @param k number of distinct digits to choose
     * @param n target sum
     * @return all valid combinations of k digits summing to n
     */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> allCombinations = new ArrayList<>();
        if (k < 0 || k > MAX_DIGIT) return allCombinations;

        int minPossible = arithmeticSum(MIN_DIGIT, k);
        int maxPossible = arithmeticSum(MAX_DIGIT - k + 1, k);
        if (n < minPossible || n > maxPossible) return allCombinations;

        backtrack(k, n, MIN_DIGIT, new ArrayList<>(), allCombinations);
        return allCombinations;
    }

    /** Builds valid digit combinations while tracking remaining slots and target sum. */
    private void backtrack(int remainingSlots, int remainingTarget, int startDigit,
                           List<Integer> currentCombination,
                           List<List<Integer>> allCombinations) {
        if (remainingSlots == 0) {
            if (remainingTarget == 0) allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        if (!canReachTarget(startDigit, remainingSlots, remainingTarget)) return;

        int lastUsefulDigit = MAX_DIGIT - remainingSlots + 1;
        for (int digit = startDigit; digit <= lastUsefulDigit && digit <= remainingTarget; digit++) {
            // select -> work -> un-select : the backtracking template
            currentCombination.add(digit);
            backtrack(remainingSlots - 1, remainingTarget - digit, digit + 1,
                currentCombination, allCombinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    /** Checks whether the smallest and largest possible suffix sums can still hit the target. */
    private boolean canReachTarget(int startDigit, int count, int target) {
        int minSum = arithmeticSum(startDigit, count);
        int maxSum = arithmeticSum(MAX_DIGIT - count + 1, count);
        return target >= minSum && target <= maxSum;
    }

    /** Returns the sum of count consecutive integers starting at firstValue. */
    private int arithmeticSum(int firstValue, int count) {
        return (2 * firstValue + count - 1) * count / 2;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        CombinationSumIII solver = new CombinationSumIII();

        int[] ks = {3, 4, 3};
        int[] targets = {9, 1, 7};
        String[] expected = {
            "[[1, 2, 6], [1, 3, 5], [2, 3, 4]]",
            "[]",
            "[[1, 2, 4]]"
        };

        for (int i = 0; i < ks.length; i++) {
            List<List<Integer>> got = solver.combinationSum3(ks[i], targets[i]);
            System.out.printf("k=%d n=%d  ->  %s  expected=%s%n",
                ks[i], targets[i], got, expected[i]);
        }
    }
}

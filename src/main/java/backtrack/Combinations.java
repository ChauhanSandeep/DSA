package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Combinations
 *
 * Given integers n and k, return every size-k combination chosen from the
 * numbers 1..n. Order inside a combination is increasing, and [1,2] is the
 * same choice as [2,1], so each set of k numbers appears once.
 *
 * Leetcode: https://leetcode.com/problems/combinations/
 * Rating:   acceptance 74.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Combinations | Increasing start index
 *
 * Example:
 *   Input:  n = 5, k = 3
 *   Output: [[1, 2, 3], [1, 2, 4], [1, 2, 5], [1, 3, 4], [1, 3, 5],
 *            [1, 4, 5], [2, 3, 4], [2, 3, 5], [2, 4, 5], [3, 4, 5]]
 *   Why:    there are exactly 10 ways to choose 3 numbers from 1..5, and
 *           each list is written in increasing order so duplicates like [2,1,3] do not appear.
 *
 * Follow-ups:
 *   1. Generate the k-th combination directly without building all C(n,k)?
 *      Use combinatorial ranking: count blocks that start with each candidate
 *      and skip whole blocks until the desired rank lands inside one.
 *   2. Allow each number to be used more than once?
 *      Recurse with the same candidate as the next start, then bound by sum or
 *      length depending on the variant.
 *   3. Stream combinations lazily for huge n?
 *      Keep the current increasing index vector and advance it like an odometer.
 *   4. Count combinations under extra constraints, such as sum <= target?
 *      Add pruning from sorted candidates, or use DP if only the count is needed.
 *
 * Related: Subsets (78), Combination Sum (39), Combination Sum III (216).
 */
public class Combinations {

    /**
     * Intuition: a combination is about which numbers are chosen, not the order
     * they are chosen in. The simple way to enforce that is to build every list
     * in increasing order: after choosing x, later choices must be greater than x.
     * That single start pointer prevents duplicates like [2,1]. We also stop the
     * loop early when there are not enough numbers left to fill the remaining
     * slots, because no later candidate can fix that shortage.
     *
     * Algorithm:
     *   1. Return an empty answer for impossible input, and return the single
     *      empty combination when k is zero.
     *   2. Keep a current combination plus the first number still allowed to be chosen.
     *   3. At each level, compute the last candidate that still leaves enough
     *      larger numbers to complete a size-k combination.
     *   4. For each useful candidate, add it, recurse with candidate + 1 as the
     *      next start, then remove it before trying the next candidate.
     *
     * Time:  O(C(n,k) * k) - each valid combination is copied once.
     * Space: O(k) recursion depth and current-combination buffer, excluding output.
     *
     * @param n upper bound of the range 1..n
     * @param k size of each combination
     * @return all size-k combinations from 1..n
     */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> allCombinations = new ArrayList<>();
        if (n < 0 || k < 0 || k > n) return allCombinations;
        if (k == 0) {
            allCombinations.add(new ArrayList<>());
            return allCombinations;
        }

        backtrack(n, k, 1, new ArrayList<>(), allCombinations);
        return allCombinations;
    }

    /** Grows combinations by choosing the next larger candidate until the target size is reached. */
    private void backtrack(int maxNumber, int combinationSize, int start,
                           List<Integer> currentCombination,
                           List<List<Integer>> allCombinations) {
        if (currentCombination.size() == combinationSize) {
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        int numbersStillNeeded = combinationSize - currentCombination.size();
        int lastUsefulCandidate = maxNumber - numbersStillNeeded + 1;
        for (int candidate = start; candidate <= lastUsefulCandidate; candidate++) {
            // select -> work -> un-select : the backtracking template
            currentCombination.add(candidate);
            backtrack(maxNumber, combinationSize, candidate + 1, currentCombination, allCombinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        Combinations solver = new Combinations();

        int[] ns = {4, 1, 5};
        int[] ks = {2, 1, 0};
        String[] expected = {
            "[[1, 2], [1, 3], [1, 4], [2, 3], [2, 4], [3, 4]]",
            "[[1]]",
            "[[]]"
        };

        for (int i = 0; i < ns.length; i++) {
            List<List<Integer>> got = solver.combine(ns[i], ks[i]);
            System.out.printf("n=%d k=%d  ->  %s  expected=%s%n",
                ns[i], ks[i], got, expected[i]);
        }
    }
}

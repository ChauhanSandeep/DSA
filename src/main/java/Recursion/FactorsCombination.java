package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Factor Combinations
 *
 * Given an integer n, return all ways to write it as a product of factors bigger
 * than 1 and smaller than n itself. Each combination is listed in nondecreasing
 * order so the same factorization is not repeated in a different order.
 *
 * Leetcode: https://leetcode.com/problems/factor-combinations/ (Medium)
 * Rating:   acceptance 50.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Recursion | Backtracking | Nondecreasing factor choices
 *
 * Example:
 *   Input:  n = 12
 *   Output: [[2,2,3], [2,6], [3,4]]
 *   Why:    these are exactly the non-trivial products of factors greater than 1
 *           that multiply to 12, with permutations like [6,2] left out.
 *
 * Follow-ups:
 *   1. How do you avoid duplicates such as [2,6] and [6,2]?
 *      Pass the previous factor as the minimum allowed next factor.
 *   2. How would you speed up factor discovery?
 *      Try divisors only through sqrt(remaining) and pair each divisor with its quotient.
 *   3. How would you count factorizations without listing them?
 *      Memoize count(remaining, minFactor) and sum counts from valid next factors.
 *   4. What changes if the trivial factorization [n] should be included?
 *      Add it separately or allow a one-element current factor list to be recorded.
 *
 * Related: Combination Sum (39), Combination Sum II (40).
 */
public class FactorsCombination {
    public static void main(String[] args) {
        int[] inputs = { 1, 12, 16 };
        String[] expected = {
            "[]",
            "[[2, 2, 3], [2, 6], [3, 4]]",
            "[[2, 2, 2, 2], [2, 2, 4], [2, 8], [4, 4]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Integer>> got = getFactorCombinations(inputs[i]);
            System.out.printf("n=%d -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

    /**
     * Intuition: view a factorization as a sorted product built one factor at a
     * time. A recursive call owns the remainingTarget and the smallest factor it
     * is still allowed to use. Passing the chosen factor back as minFactor keeps
     * combinations nondecreasing, so [2,6] can appear but [6,2] cannot be reached
     * as a duplicate branch.
     *
     * Algorithm:
     *   1. Start the search with minFactor = 2 and an empty currentFactors list.
     *   2. If remainingTarget becomes 1, record currentFactors only when it has multiple factors.
     *   3. Try every factor from minFactor through remainingTarget.
     *   4. When factor divides remainingTarget, choose it, recurse on the quotient, and un-choose it.
     *
     * Time:  O(n * b) - each partial factor chain may scan up to the remaining
     *        number for its next divisor.
     * Space: O(log n) - the deepest chain repeatedly divides by at least 2.
     *
     * @param targetNumber number to factorize
     * @return all non-trivial factor combinations in nondecreasing order
     */
    public static List<List<Integer>> getFactorCombinations(int targetNumber) {
        List<List<Integer>> result = new ArrayList<>();
        if (targetNumber <= 1) return result;
        backtrack(targetNumber, 2, new ArrayList<>(), result);
        return result;
    }

    /** Builds sorted factor chains whose product equals the original number. */
    private static void backtrack(int remainingTarget, int minFactor, List<Integer> currentFactors, List<List<Integer>> result) {
        // Base case: fully factorized and has more than one factor (exclude trivial [n])
        if (remainingTarget == 1 && currentFactors.size() > 1) {
            result.add(new ArrayList<>(currentFactors));
            return;
        }

        for (int factor = minFactor; factor <= remainingTarget; factor++) {
            if (remainingTarget % factor == 0) {
                // Factor found - include it and continue with quotient
                currentFactors.add(factor);
                backtrack(remainingTarget / factor, factor, currentFactors, result);
                currentFactors.remove(currentFactors.size() - 1); // Backtrack
            }
        }
    }
}

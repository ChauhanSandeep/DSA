package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Factor Combinations
 *
 * Numbers can be regarded as product of its factors. Write a function that takes an integer n
 * and returns all possible combinations of its factors (excluding 1 and n itself).
 *
 * Example:
 * Input: n = 12
 * Output: [[2,6],[2,2,3],[3,4]]
 * Explanation: 2*6 = 12, 2*2*3 = 12, 3*4 = 12
 *
 * Example:
 * Input: n = 8
 * Output: [[2,4],[2,2,2]]
 *
 * Constraints:
 * - 1 <= n <= 10^7
 * - Output should not contain duplicate combinations
 * - Factors should be in non-decreasing order within each combination
 *
 * LeetCode: https://leetcode.com/problems/factor-combinations/
 *
 * Follow-up Questions:
 * Q1: How do you avoid duplicate factor combinations like [2,3] and [3,2]?
 * A1: Only consider factors >= previous factor (maintain non-decreasing order in recursion).
 *
 * Q2: What if we want to include trivial factorization [n] itself?
 * A2: Remove the condition `currentChain.size() > 1` in the base case.
 *
 * Q3: How would you optimize for very large numbers?
 * A3: Only iterate up to sqrt(n) for factors, then compute complementary factor n/i.
 *
 * Q4: What if we want factors in descending order instead?
 * A4: Change the iteration order and comparison to maintain descending order throughout.
 *
 * Q5: How can we find just the number of factorizations without listing them?
 * A5: Use dynamic programming with memoization counting ways to factorize each number.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class FactorsCombination {
    public static void main(String[] args) {
        int number = 12;
        List<List<Integer>> factors = getFactorCombinations(number);
        System.out.println("Factor combinations of " + number + ": " + factors);
    }

    /**
     * Finds all unique factor combinations of a given number (excluding 1 and n itself).
     *
     * Time Complexity: O(2^log n)
     * Space Complexity: O(log n)
     *
     * @param targetNumber The number to factorize
     * @return List of all unique factor combinations
     */
    public static List<List<Integer>> getFactorCombinations(int targetNumber) {
        List<List<Integer>> result = new ArrayList<>();
        if (targetNumber <= 1) return result;
        backtrack(targetNumber, 2, new ArrayList<>(), result);
        return result;
    }

    /**
     * Recursive backtracking function to generate all factor combinations.
     *
     * Algorithm:
     * 1. Base case: When remaining number is 1 and we have multiple factors, add to result
     * 2. Try all factors from minFactor to remaining number
     * 3. If a number is a factor, add it and recurse with quotient
     * 4. Backtrack by removing the added factor
     *
     * Key Insight: Start from minFactor (not 2 each time) to maintain non-decreasing order
     * and avoid duplicates like [2,3] and [3,2].
     *
     * Time Complexity: O(2^log n)
     * - Time complexity is not 2^n because the depth is not n. It's divided in each step by factors.
     * Space Complexity: O(log n) - recursion depth limited by number of prime factors
     *
     * @param remainingTarget The number still to be factorized
     * @param minFactor Smallest factor to consider (maintains non-decreasing order)
     * @param currentFactors Current list of factors being built
     * @param result List storing all valid factor combinations
     */
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

package recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Find all unique combinations of factors (excluding 1 and n itself) 
 * that multiply to give `n`.
 *
 * Approach:
 * - Use **backtracking** to explore factor combinations.
 * - Start from 2 and only consider factors that are **≥ previous factor** to avoid duplicate permutations.
 * - Stop recursion when `n == 1` and a valid combination has been found.
 *
 * Time Complexity: O(2^logN) (Sub-exponential due to factor tree)
 * Space Complexity: O(logN) (Recursion depth)
 *
 * LeetCode Link: https://leetcode.com/problems/factor-combinations/
 */
public class FactorsCombination {
    public static void main(String[] args) {
        List<List<Integer>> factors = getFactorCombinations(12);
        System.out.println(factors);
    }

    /**
     * Finds all unique factor combinations of a given number.
     *
     * @param n The target number.
     * @return A list of factor combinations.
     */
    public static List<List<Integer>> getFactorCombinations(int n) {
        List<List<Integer>> result = new ArrayList<>();
        if (n <= 1) return result; // Edge case: No valid factors for 1 or negative numbers
        backtrack(n, 2, new ArrayList<>(), result);
        return result;
    }

    /**
     * Recursive backtracking function to generate factor combinations.
     *
     * @param n       The remaining number to factorize.
     * @param start   The smallest factor to consider (prevents duplicate permutations).
     * @param current The temporary list storing the current factor combination.
     * @param result  The final list containing all valid factor combinations.
     */
    private static void backtrack(int n, int start, List<Integer> current, List<List<Integer>> result) {
        if (n == 1 && current.size() > 1) { // Valid combination found (excluding trivial cases)
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i <= n; i++) {
            if (n % i == 0) { // Check if `i` is a factor of `n`
                current.add(i);
                backtrack(n / i, i, current, result); // Continue with the reduced value
                current.remove(current.size() - 1); // Backtrack to try other factors
            }
        }
    }
}

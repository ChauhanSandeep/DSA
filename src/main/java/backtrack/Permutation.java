package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Permutations
 *
 * Given an array of distinct integers, return every possible ordering of those
 * integers. Each input value must appear exactly once in each permutation.
 *
 * Leetcode: https://leetcode.com/problems/permutations/
 * Rating:   acceptance 82.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Permutations | Prune-on-used
 *
 * Example:
 *   Input:  [0,1,2]
 *   Output: [[0, 1, 2], [0, 2, 1], [1, 0, 2], [1, 2, 0], [2, 0, 1], [2, 1, 0]]
 *   Why:    three distinct values have 3! = 6 possible orderings, and each
 *           output uses 0, 1, and 2 exactly once.
 *
 * Follow-ups:
 *   1. Input can contain duplicates?
 *      Sort and skip duplicate siblings, or use a frequency map (Permutations II).
 *   2. Generate permutations lazily instead of storing n! results?
 *      Implement an iterator over next-permutation order or keep a DFS stack.
 *   3. Return the k-th permutation directly?
 *      Use factorial unranking instead of backtracking through earlier permutations.
 *   4. Restrict permutations by adjacency constraints?
 *      Add a validity check before choosing the next value, or model it as Hamiltonian path DFS.
 *
 * Related: Permutations II (47), Permutation Sequence (60), Next Permutation (31).
 */
public class Permutation {

    /**
     * Intuition: a permutation is built by filling positions from left to right.
     * For each position, any value not already used can go there. A boolean array
     * remembers which input indexes are already in the current path, so every
     * value appears exactly once. When the path length reaches n, it is a complete
     * ordering; because each level tries all unused values, every possible ordering
     * is reached exactly once.
     *
     * Algorithm:
     *   1. Return an empty answer for null input and start with an empty current permutation.
     *   2. If the current permutation has length n, copy it into the answer.
     *   3. Otherwise, scan every input index and skip indexes already marked used.
     *   4. Add the unused value, mark it used, recurse to fill the next position,
     *      then unmark and remove it before trying another value.
     *
     * Time:  O(n*n!) - there are n! permutations, and copying each one takes n values.
     * Space: O(n) recursion depth, used array, and current permutation, excluding output.
     *
     * @param nums distinct integers
     * @return all permutations of nums
     */
    public static List<List<Integer>> generateAllPermutations(int[] nums) {
        List<List<Integer>> allPermutations = new ArrayList<>();
        if (nums == null) return allPermutations;

        backtrack(nums, new boolean[nums.length], new ArrayList<>(), allPermutations);
        return allPermutations;
    }

    /** Builds permutations by filling the next slot with each unused input value. */
    private static void backtrack(int[] nums, boolean[] used,
                                  List<Integer> currentPermutation,
                                  List<List<Integer>> allPermutations) {
        if (currentPermutation.size() == nums.length) {
            allPermutations.add(new ArrayList<>(currentPermutation));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;

            // select -> mark -> work -> unmark -> un-select
            currentPermutation.add(nums[i]);
            used[i] = true;
            backtrack(nums, used, currentPermutation, allPermutations);
            used[i] = false;
            currentPermutation.remove(currentPermutation.size() - 1);
        }
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        int[][] inputs = {
            {0, 1, 2},
            {},
            {1}
        };
        String[] expected = {
            "[[0, 1, 2], [0, 2, 1], [1, 0, 2], [1, 2, 0], [2, 0, 1], [2, 1, 0]]",
            "[[]]",
            "[[1]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Integer>> got = generateAllPermutations(inputs[i]);
            System.out.printf("nums=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}

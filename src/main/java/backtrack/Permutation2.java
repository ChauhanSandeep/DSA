package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Problem: Permutations II
 *
 * Given an integer array that may contain duplicate values, return all unique
 * permutations. The answer must not contain the same ordering more than once.
 *
 * Leetcode: https://leetcode.com/problems/permutations-ii/
 * Rating:   acceptance 63.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Unique permutations | Sort-to-skip duplicates
 *
 * Example:
 *   Input:  [1,1,2]
 *   Output: [[1, 1, 2], [1, 2, 1], [2, 1, 1]]
 *   Why:    the two 1 values are interchangeable, so the only unique choices
 *           are where the 2 appears: first, second, or third.
 *
 * Follow-ups:
 *   1. Return the k-th unique permutation with duplicates?
 *      Use multinomial block counts while choosing each next value.
 *   2. Count unique permutations without generating them?
 *      Return n! divided by the factorial of each value frequency.
 *   3. Generate unique string permutations lazily?
 *      Keep a sorted char array plus used flags, yielding one leaf at a time.
 *   4. Avoid sorting when values are streamed with counts?
 *      Use a frequency map and recurse over keys with positive remaining count.
 *
 * Related: Permutations (46), Next Permutation (31), Subsets II (90).
 *
 *   Approach             Method                       Time    Space (extra)
 *   -------------------  ---------------------------  ------  -------------
 *   Frequency map        permuteUniqueUsingFreqMap    O(n*n!) O(n)
 *   Sort + used (best)   generateUniquePermutations   O(n*n!) O(n)
 */
public class Permutation2 {

    /**
     * Intuition: duplicates become simple when we count how many copies of each
     * value remain. Instead of choosing an index, we choose a value with positive
     * frequency and spend one copy. That means equal values are never treated as
     * separate sibling choices, so duplicate permutations are never created. A
     * sorted map keeps the output in a stable, easy-to-read order.
     *
     * Algorithm:
     *   1. Return an empty answer for null input and count how many times each value appears.
     *   2. Build the permutation one slot at a time from values whose remaining count is positive.
     *   3. When the current permutation reaches the original length, copy it into the answer.
     *   4. Choose a value by decrementing its count and appending it, recurse, then
     *      restore the count and remove the value before trying the next key.
     *
     * Time:  O(n * U) - U unique permutations are produced, and copying each one takes n values.
     * Space: O(n + d) for recursion/current path and d distinct counts, excluding output.
     *
     * @param nums integers that may contain duplicates
     * @return all unique permutations
     */
    public List<List<Integer>> permuteUniqueUsingFreqMap(int[] nums) {
        List<List<Integer>> allPermutations = new ArrayList<>();
        if (nums == null) return allPermutations;

        Map<Integer, Integer> valueToCount = new TreeMap<>();
        for (int num : nums) valueToCount.merge(num, 1, Integer::sum);

        backtrackWithCounts(nums.length, valueToCount, new ArrayList<>(), allPermutations);
        return allPermutations;
    }

    /** Builds unique permutations by spending one remaining count of a value at a time. */
    private void backtrackWithCounts(int targetLength, Map<Integer, Integer> valueToCount,
                                     List<Integer> currentPermutation,
                                     List<List<Integer>> allPermutations) {
        if (currentPermutation.size() == targetLength) {
            allPermutations.add(new ArrayList<>(currentPermutation));
            return;
        }

        for (Map.Entry<Integer, Integer> entry : valueToCount.entrySet()) {
            int value = entry.getKey();
            int remainingCount = entry.getValue();
            if (remainingCount == 0) continue;

            currentPermutation.add(value);
            valueToCount.put(value, remainingCount - 1);
            backtrackWithCounts(targetLength, valueToCount, currentPermutation, allPermutations);
            valueToCount.put(value, remainingCount);
            currentPermutation.remove(currentPermutation.size() - 1);
        }
    }

    /**
     * Intuition (interview default): sorting puts equal values next to each other,
     * which lets us skip duplicate sibling branches. If nums[i] equals nums[i-1]
     * and the previous copy is not already used in the current path, choosing this
     * later copy first would produce the same permutations that the earlier copy
     * will produce. So we only allow duplicates to be used in a fixed left-to-right
     * order, which preserves all unique permutations and removes repeats.
     *
     * Algorithm:
     *   1. Return an empty answer for null input, then sort nums so equal values are adjacent.
     *   2. Build permutations by trying each unused index for the next position.
     *   3. Skip a duplicate value when its previous equal copy has not been used in
     *      the current path, because that would repeat a sibling branch.
     *   4. Add the chosen value, mark its index used, recurse, then unmark and
     *      remove it before trying the next index.
     *
     * Time:  O(n * U) - U unique permutations are produced, and copying each one takes n values.
     * Space: O(n) recursion depth, used array, and current permutation, excluding output.
     *
     * @param nums integers that may contain duplicates
     * @return all unique permutations
     */
    public static List<List<Integer>> generateUniquePermutations(int[] nums) {
        List<List<Integer>> allPermutations = new ArrayList<>();
        if (nums == null) return allPermutations;

        Arrays.sort(nums);
        backtrackSorted(nums, new boolean[nums.length], new ArrayList<>(), allPermutations);
        return allPermutations;
    }

    /** Builds unique permutations from a sorted array while skipping duplicate sibling branches. */
    private static void backtrackSorted(int[] nums, boolean[] used,
                                        List<Integer> currentPermutation,
                                        List<List<Integer>> allPermutations) {
        if (currentPermutation.size() == nums.length) {
            allPermutations.add(new ArrayList<>(currentPermutation));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            // If the previous equal value is unused, this equal value would duplicate its sibling branch.
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;

            currentPermutation.add(nums[i]);
            used[i] = true;
            backtrackSorted(nums, used, currentPermutation, allPermutations);
            used[i] = false;
            currentPermutation.remove(currentPermutation.size() - 1);
        }
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        int[][] inputs = {
            {1, 1, 2},
            {3, 3, 0, 3},
            {}
        };
        String[] expected = {
            "[[1, 1, 2], [1, 2, 1], [2, 1, 1]]",
            "[[0, 3, 3, 3], [3, 0, 3, 3], [3, 3, 0, 3], [3, 3, 3, 0]]",
            "[[]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            int[] nums = inputs[i].clone();
            List<List<Integer>> got = generateUniquePermutations(nums);
            System.out.printf("nums=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}

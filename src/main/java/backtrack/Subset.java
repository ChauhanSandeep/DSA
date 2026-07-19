package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Subsets
 *
 * Given an integer array with unique elements, return the full power set: every
 * subset from the empty subset to the full array. The answer may be returned in
 * any order, and no duplicate subsets are possible because input values are unique.
 *
 * Leetcode: https://leetcode.com/problems/subsets/
 * Rating:   acceptance 82.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Power set | Include next choices
 *
 * Example:
 *   Input:  [1,2,3]
 *   Output: [[], [1], [1,2], [1,2,3], [1,3], [2], [2,3], [3]]
 *   Why:    each of the three values is either included or excluded, giving
 *           2^3 = 8 subsets including the empty set and the full set.
 *
 * Follow-ups:
 *   1. Input contains duplicates?
 *      Sort first and skip equal sibling choices at the same recursion depth.
 *   2. Return only subsets of size k?
 *      Add a target size and record only when currentSubset.size() == k.
 *   3. Generate the k-th subset in lexicographic order?
 *      Use combinatorial counts to skip include/exclude blocks.
 *   4. Count subsets whose sum equals target?
 *      Use subset-sum DP for counts, or DFS with pruning when all numbers are non-negative.
 *
 * Related: Subsets II (90), Combinations (77), Combination Sum (39).
 *
 *   Approach           Method                   Time      Space (extra)
 *   -----------------  -----------------------  --------  -------------
 *   Iterative build    subsetsIterativeApproach O(n*2^n) O(1)
 *   Backtracking       subsets                  O(n*2^n) O(n)
 */
public class Subset {

    /**
     * Intuition: the power set doubles every time we introduce a new number. Any
     * subset built so far has two futures: keep it as-is, or make a copy that also
     * includes the new number. Starting from just the empty subset and repeating
     * that copy-and-append round creates every include/exclude combination exactly
     * once. Because the input values are unique, no duplicate-subset cleanup is needed.
     *
     * Algorithm:
     *   1. Return an empty answer for null input; otherwise seed the answer with the empty subset.
     *   2. For each number, remember how many subsets existed before processing it.
     *   3. Copy each of those existing subsets, append the current number to the copy, and add it to the answer.
     *   4. Leave the newly added subsets for future rounds, so later numbers can be
     *      combined with both old and newly extended choices.
     *
     * Time:  O(n*2^n) - each of 2^n subsets may copy up to n values.
     * Space: O(1) extra beyond the output.
     *
     * @param nums unique integers
     * @return all subsets of nums
     */
    public List<List<Integer>> subsetsIterativeApproach(int[] nums) {
        List<List<Integer>> allSubsets = new ArrayList<>();
        if (nums == null) return allSubsets;

        allSubsets.add(new ArrayList<>());
        for (int num : nums) {
            int existingSubsetCount = allSubsets.size();
            for (int i = 0; i < existingSubsetCount; i++) {
                List<Integer> extendedSubset = new ArrayList<>(allSubsets.get(i));
                extendedSubset.add(num);
                allSubsets.add(extendedSubset);
            }
        }
        return allSubsets;
    }

    /**
     * Intuition (interview default): imagine a decision tree where a path is the
     * subset chosen so far. Unlike permutations, we do not need to wait for a leaf:
     * every node is already a valid subset, including the empty subset at the root.
     * From a node, we may append any later value and continue from the next index,
     * which prevents reusing a value or producing the same subset in a different
     * order.
     *
     * Algorithm:
     *   1. Return an empty answer for null input and start DFS with an empty current subset.
     *   2. On entering each recursive call, copy the current subset into the answer.
     *   3. From the current start index, try each remaining value as the next value to include.
     *   4. Add the value, recurse from the next index, then remove it before trying
     *      the next candidate at the same level.
     *
     * Time:  O(n*2^n) - 2^n subsets, each copied in O(n).
     * Space: O(n) recursion depth and current-subset buffer, excluding output.
     *
     * @param nums unique integers
     * @return all subsets of nums
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> allSubsets = new ArrayList<>();
        if (nums == null) return allSubsets;

        backtrack(nums, 0, new ArrayList<>(), allSubsets);
        return allSubsets;
    }

    /** Records the current subset, then grows it with each later value. */
    private void backtrack(int[] nums, int start,
                           List<Integer> currentSubset,
                           List<List<Integer>> allSubsets) {
        allSubsets.add(new ArrayList<>(currentSubset));

        for (int i = start; i < nums.length; i++) {
            currentSubset.add(nums[i]);
            backtrack(nums, i + 1, currentSubset, allSubsets);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        Subset solver = new Subset();

        int[][] inputs = {
            {},
            {1},
            {1, 2, 3}
        };
        String[] expected = {
            "[[]]",
            "[[], [1]]",
            "[[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Integer>> got = solver.subsets(inputs[i]);
            System.out.printf("nums=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}

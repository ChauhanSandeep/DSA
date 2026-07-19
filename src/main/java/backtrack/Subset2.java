package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Subsets II
 *
 * Given an integer array `nums` that MAY contain duplicates, return all possible
 * subsets (the power set). The solution set must not contain duplicate subsets,
 * and may be returned in any order.
 *
 * Leetcode: https://leetcode.com/problems/subsets-ii/
 * Rating:   acceptance 61.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Subsets/Power set | Sort-to-dedupe
 *
 * Example:
 *   Input:  [1,2,2]
 *   Output: [[], [1], [1,2], [1,2,2], [2], [2,2]]
 *   Why:    every distinct choice of elements shows up once; the two 2's are
 *           interchangeable, so [1,2] is listed a single time, not twice.
 *
 * Follow-ups:
 *   1. Return the K-th subset in lexicographic order WITHOUT generating all 2^n?
 *      Walk the sorted distinct values, at each step counting how many subsets
 *      the remaining suffix can form and skipping whole blocks (combinatorial rank).
 *   2. nums has up to 10^5 duplicates of few distinct values - 2^n is impossible.
 *      Collapse to (value,count) pairs; a "subset" is a choice of 0..count per value,
 *      so the answer count is product(count_i + 1) and can be built without recursion.
 *   3. Count distinct subsets whose sum == target with duplicates, memory-bounded?
 *      Group-knapsack DP over (value,count) with a bounded-count transition, not backtracking.
 *   4. Stream subsets lazily (an iterator) instead of materializing the full list?
 *      Encode state as the include/skip decision stack and advance on next().
 *
 * Related: Subsets (78), Combination Sum II (40), Permutations II (47).
 *
 *   Approach              Method                    Time        Space (extra)
 *   --------------------  ------------------------  ----------  -------------
 *   Iterative build       subsetsWithDupIterative   O(n*2^n)    O(1)
 *   Backtracking (best)   subsetsWithDup            O(n*2^n)    O(n)
 */
public class Subset2 {

    /**
     * Intuition: forget duplicates for a moment. The power set has a dead-simple
     * building rule - introduce one new value and every subset you already have
     * splits in two: the old subset, and a copy of it with the new value
     * appended. So from just {} you grow the whole answer one value at a time,
     * doubling the count each round. Duplicates are the only snag. If the same
     * value appears twice and you again append it to EVERY existing subset, you
     * rebuild subsets the first copy already made - a second 2 re-creates {2} on
     * top of the {2} the first 2 produced. Which subsets are those duplicates?
     * Exactly the ones that did not already end in this value - i.e. everything
     * except the batch the previous copy just added. So for a repeat, extend only
     * that last batch. That is the whole trick, and it makes every subset appear
     * once and never twice.
     *
     * Time:  O(n * 2^n) - the answer holds up to 2^n subsets and copying each one
     *        to extend it costs up to O(n).
     * Space: O(1) extra beyond the output list itself.
     *
     * @param nums input array that may contain duplicates
     * @return all unique subsets
     */
    public List<List<Integer>> subsetsWithDupIterative(int[] nums) {
        List<List<Integer>> allSubsets = new ArrayList<>();
        allSubsets.add(new ArrayList<>());
        if (nums == null || nums.length == 0) return allSubsets;

        // --- sort so equal values become neighbours ----------------------
        Arrays.sort(nums);

        // --- grow every subset one value at a time ------------------------
        int previousRoundStart = 0;
        for (int i = 0; i < nums.length; i++) {
            // a duplicate must only extend subsets born in the previous round,
            // otherwise re-expanding older subsets double-counts it
            int extendFrom = (i > 0 && nums[i] == nums[i - 1]) ? previousRoundStart : 0;
            int extendTo = allSubsets.size();

            previousRoundStart = extendTo;
            for (int j = extendFrom; j < extendTo; j++) {
                List<Integer> extended = new ArrayList<>(allSubsets.get(j));
                extended.add(nums[i]);
                allSubsets.add(extended);
            }
        }
        return allSubsets;
    }

    /**
     * Intuition (interview default): picture building a subset as a walk down a
     * decision tree - at each step you pick which of the remaining values to
     * append next. The naive version of this happily produces duplicates, so the
     * whole game is one targeted fix. Two ideas make it work:
     *
     * First, every node you pass through IS a valid subset, not just the leaves
     * (the empty pick, {1}, {1,2}, ...). So we record the current subset the
     * moment we enter a call, not only at the bottom - that is why the very first
     * thing each call does is add what we have so far.
     *
     * Second, the duplicate trap: once the array is sorted, equal values are
     * neighbours. Within a single level of the tree, choosing the second (or
     * third) copy of a value as "the next pick" rebuilds the exact subtree the
     * first copy already explored. So at each level we allow a value in only the
     * first time it appears and skip its equal siblings. That single skip - "not
     * the first choice here AND equal to the previous" - is the entire difference
     * between "all subsets" and "all UNIQUE subsets".
     *
     * Time:  O(n * 2^n) - up to 2^n subsets, and copying each into the result
     *        costs O(n).
     * Space: O(n) for the recursion depth plus the current-subset buffer
     *        (excluding the output).
     *
     * @param nums input array that may contain duplicates
     * @return all unique subsets
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> allSubsets = new ArrayList<>();
        if (nums == null) return allSubsets;

        Arrays.sort(nums);
        backtrack(nums, 0, new ArrayList<>(), allSubsets);
        return allSubsets;
    }

    /** Grows the current subset one index at a time, skipping duplicate siblings so each set is emitted once. */
    private void backtrack(int[] nums, int index,
                           List<Integer> currentSubset,
                           List<List<Integer>> allSubsets) {
        // every node in the recursion tree is itself a valid subset
        allSubsets.add(new ArrayList<>(currentSubset));

        for (int i = index; i < nums.length; i++) {
            // `i > index` means this is not the first choice at this level, so an
            // equal value would repeat a sibling branch already explored -> skip
            if (i > index && nums[i] == nums[i - 1]) continue;

            // select -> work -> un-select : the backtracking template
            currentSubset.add(nums[i]);
            backtrack(nums, i + 1, currentSubset, allSubsets);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        Subset2 solver = new Subset2();

        int[][] inputs = { {}, {0}, {1, 2, 2}, {2, 2, 2} };
        String[] expected = {
            "[[]]",
            "[[], [0]]",
            "[[], [1], [1, 2], [1, 2, 2], [2], [2, 2]]",
            "[[], [2], [2, 2], [2, 2, 2]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Integer>> got = solver.subsetsWithDup(inputs[i].clone());
            System.out.printf("nums=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}

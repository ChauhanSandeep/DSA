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
     * Intuition: the power set doubles every time you introduce a new value -
     * every existing subset can either take the new value or skip it. So we build
     * the answer in rounds: start with just the empty subset, and for each value
     * copy every subset we already have and append the value to the copy. The
     * only wrinkle is duplicates: if the same value appears again and we extend
     * ALL current subsets, we recreate subsets the previous copy of that value
     * already made. The fix is to remember where last round's new subsets began
     * and, for a repeated value, extend only those - so each duplicate adds
     * exactly the genuinely new subsets and nothing already seen.
     *
     * Algorithm:
     *   1. Sort the array so equal values sit next to each other (this is what
     *      lets us recognise a repeat by comparing with the previous value).
     *   2. Seed the result with the empty subset.
     *   3. For each value, copy a range of existing subsets and append the value
     *      to each copy.
     *   4. If this value repeats the previous one, start that range at the first
     *      subset created last round; otherwise extend every existing subset.
     *
     * Time:  O(n*2^n) - there are up to 2^n subsets and copying each one to
     *        extend it costs up to O(n).
     * Space: O(1) extra beyond the output.
     *
     * @param nums input array that may contain duplicates
     * @return all unique subsets
     */
    public List<List<Integer>> subsetsWithDupIterative(int[] nums) {
        List<List<Integer>> allSubsets = new ArrayList<>();
        allSubsets.add(new ArrayList<>());
        if (nums == null || nums.length == 0) return allSubsets;

        // --- Step 1: sort to group duplicates -----------------------------
        Arrays.sort(nums);

        // --- Step 2 & 3: extend subsets value by value --------------------
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
     * Intuition (interview default): think of building a subset as a walk down a
     * decision tree - at each index you decide which value to append next, and
     * every node you pass through is itself a valid subset (that is why we record
     * on entry, not just at the leaves). The trap is duplicates: once the array
     * is sorted, equal values are neighbours, and choosing the second, third, ...
     * copy of a value at the SAME position in the tree would rebuild the exact
     * subsets the first copy already produced. So at each level we let a value in
     * only the first time it appears and skip its equal siblings - that single
     * skip is what turns "all subsets" into "all UNIQUE subsets".
     *
     * Algorithm:
     *   1. Sort so duplicates are adjacent (this is what makes the skip check work).
     *   2. On entering each recursive call, record the current subset - every node
     *      of the tree is a valid answer, including the empty one.
     *   3. From the current index, try each remaining value as the next pick, but
     *      skip a value equal to its left neighbour within this same loop, since
     *      the first sibling already covered those subsets.
     *   4. For each kept value, add it, recurse from the next index, then remove
     *      it before trying the next candidate.
     *
     * Time:  O(n*2^n) - there are 2^n subsets and copying each into the result
     *        costs O(n).
     * Space: O(n) recursion depth + current-subset buffer (excluding output).
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

package arrays.sorting;

import java.util.*;

/**
 * Make Lexicographically Smallest Array by Swapping Elements
 *
 * You are given a 0-indexed array of positive integers nums and a positive integer limit.
 * In one operation, you can swap any two indices i and j if
 * |nums[i] - nums[j]| <= limit.
 * Return the lexicographically smallest array that can be obtained by performing
 * the operation any number of times.
 *
 * Example:
 *   nums = [1,5,3,9,8], limit = 2  ->  [1,3,5,8,9]
 *   nums = [1,7,6,18,2,1], limit = 3  ->  [1,6,7,18,1,2]
 *
 * Key Insight:
 *   Swapping is transitive: if a~b and b~c (where x~y means |x-y|<=limit), then a, b, c
 *   are all reachable from each other via a chain of swaps. So sort nums and group
 *   consecutive elements whose adjacent difference is <= limit. All elements in such a
 *   group share the same set of original indices and can be freely permuted among them.
 *   To get the lex smallest result, place the sorted group values into the sorted indices
 *   of that group.
 *
 * LeetCode Link: https://leetcode.com/problems/make-lexicographically-smallest-array-by-swapping-elements/
 *
 * Follow-up Questions:
 * 1. What if limit could be 0?
 *    Answer: Only equal values can be swapped; the answer is essentially nums itself
 *    (equal values swapping does not change the array).
 * 2. What if the array contained negatives or required absolute equivalence classes
 *    defined differently?
 *    Answer: The grouping logic still holds as long as the equivalence is defined by
 *    proximity after sorting.
 * 3. How would you support online updates (insertions/deletions)?
 *    Answer: Maintain a TreeMap/Union-Find keyed on values and recompute affected groups.
 *
 * Related Problems:
 * - 1202. Smallest String With Swaps
 * - 2948. Make Lexicographically Smallest Array by Swapping Elements (this problem)
 *
 * LeetCode Contest Rating: 2046 (Weekly Contest 373)
 */
public class MakeLexicographicallySmallestArrayBySwappingElements {

    /**
     * Sort + group-by-limit approach.
     *
     * Algorithm:
     * 1. Pair each value with its original index and sort by value.
     * 2. Walk through the sorted pairs; whenever the gap between consecutive values
     *    exceeds limit, close the current group and start a new one.
     * 3. Within a group, collect the original indices, sort them, and assign the
     *    sorted values back to those sorted indices.
     *
     * Time Complexity: O(n log n) for sorting values and indices within groups.
     * Space Complexity: O(n) for the auxiliary value-index pairs and result array.
     *
     * @param nums  input array of positive integers
     * @param limit maximum allowed absolute difference for a single swap
     * @return lexicographically smallest array achievable
     */
    public int[] lexicographicallySmallestArray(int[] nums, int limit) {
        int len = nums.length;

        // Pair (value, originalIndex) and sort by value
        int[][] valueIndex = new int[len][2];
        for (int i = 0; i < len; i++) {
            valueIndex[i][0] = nums[i];
            valueIndex[i][1] = i;
        }
        Arrays.sort(valueIndex, Comparator.comparingInt(a -> a[0]));

        int[] result = new int[len];

        int start = 0;
        while (start < len) {
            // Extend group while consecutive sorted values are within limit
            int end = start + 1;
            while (end < len && valueIndex[end][0] - valueIndex[end - 1][0] <= limit) {
                end++;
            }

            // Collect indices of this group and sort them ascending
            int size = end - start;
            Integer[] sortedIndex = new Integer[size];

            for (int i = 0; i < size; i++) {
                sortedIndex[i] = valueIndex[start + i][1];
            }
            Arrays.sort(sortedIndex);

            // Sorted values go to sorted indices to minimize lexicographic order
            for (int k = 0; k < size; k++) {
                result[sortedIndex[k]] = valueIndex[start + k][0];
            }

            start = end;
        }

        return result;
    }
}


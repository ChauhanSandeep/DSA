package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Remove Duplicates from Sorted Array
 *
 * Given a sorted array, keep one copy of each distinct value in the front of the
 * same array. Return the number of unique values; content after that prefix is
 * irrelevant.
 *
 * Leetcode: https://leetcode.com/problems/remove-duplicates-from-sorted-array/ (Easy)
 * Rating:   acceptance 63.3% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Unique prefix write
 *
 * Example:
 *   Input:  nums = [0,0,1,1,1,2,2,3,3,4]
 *   Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
 *   Why:    each distinct sorted value is written once into the valid prefix.
 *
 * Follow-ups:
 *   1. Allow each value at most k times?
 *      Compare the candidate with the value k positions before the write index.
 *   2. Handle unsorted input while preserving order?
 *      Use a HashSet of seen values and the same write-prefix idea.
 *   3. Count how many duplicates were removed?
 *      Return nums.length - writeIndex or track skipped elements during the scan.
 *
 * Related: Remove Duplicates from Sorted Array II (80), Remove Element (27).
 */
public class RemoveDuplicatesFromSortedArray {

public static void main(String[] args) {
    RemoveDuplicatesFromSortedArray solver = new RemoveDuplicatesFromSortedArray();
    int[][] inputs = { {1, 1, 2}, {} };
    int[] expectedLengths = { 2, 0 };
    int[][] expectedPrefixes = { {1, 2}, {} };

    for (int i = 0; i < inputs.length; i++) {
        int[] nums = inputs[i].clone();
        int gotLength = solver.removeDuplicates(nums);
        int[] gotPrefix = Arrays.copyOf(nums, gotLength);
        System.out.printf("nums=%s -> len=%d prefix=%s  expected=len=%d prefix=%s%n",
            Arrays.toString(inputs[i]), gotLength, Arrays.toString(gotPrefix),
            expectedLengths[i], Arrays.toString(expectedPrefixes[i]));
    }
}

    /**
 * Intuition: in a sorted array, every duplicate sits next to the value already
 * written into the unique prefix. writeIndex points to the next prefix slot,
 * so a value is copied only when it differs from nums[writeIndex - 1].
 *
 * Algorithm:
 *   1. Return 0 for null or empty input.
 *   2. Start writeIndex at 0.
 *   3. Scan every currentElement in nums.
 *   4. Write it when it is the first value or differs from the last written value.
 *
 * Time:  O(n) - one pass reads each element once.
 * Space: O(1) - the valid prefix is written in the original array.
 *
 * @param nums sorted array to compress in place
 * @return number of unique values in the prefix
 */
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // Index to write next unique element
        int writeIndex = 0;

        // Process each element in the array
        for (int currentElement : nums) {
            // Write element if it's first element or different from previous unique element
            if (writeIndex == 0 || currentElement != nums[writeIndex - 1]) {
                nums[writeIndex] = currentElement;
                writeIndex++;
            }
        }

        return writeIndex;
    }
}

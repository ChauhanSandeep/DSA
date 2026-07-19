package arrays.twopointers;

import java.util.*;

/**
 * Problem: Remove Element
 *
 * Given an array and a value, remove every occurrence of that value in place.
 * Return the number of remaining values; elements beyond the returned length do
 * not matter.
 *
 * Leetcode: https://leetcode.com/problems/remove-element/ (Easy)
 * Rating:   acceptance 62.1% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Filtered prefix write
 *
 * Example:
 *   Input:  nums = [3,2,2,3], val = 3
 *   Output: 2, nums = [2,2,_,_]
 *   Why:    both 3s are skipped and the two 2s are written to the front.
 *
 * Follow-ups:
 *   1. What if order does not need to be preserved and val is common?
 *      Swap matches with the end to reduce writes.
 *   2. Remove multiple values at once?
 *      Store banned values in a set and keep the same writeIndex scan.
 *   3. Return removed indices too?
 *      Collect indices whenever currentElement == val before skipping it.
 *
 * Related: Remove Duplicates from Sorted Array (26), Move Zeroes (283).
 */
public class RemoveElement {

public static void main(String[] args) {
    RemoveElement solver = new RemoveElement();
    int[][] inputs = { {3, 2, 2, 3}, {0, 1, 2, 2, 3, 0, 4, 2} };
    int[] vals = { 3, 2 };
    int[] expectedLengths = { 2, 5 };
    int[][] expectedPrefixes = { {2, 2}, {0, 1, 3, 0, 4} };

    for (int i = 0; i < inputs.length; i++) {
        int[] nums = inputs[i].clone();
        int gotLength = solver.removeElement(nums, vals[i]);
        int[] gotPrefix = Arrays.copyOf(nums, gotLength);
        System.out.printf("nums=%s val=%d -> len=%d prefix=%s  expected=len=%d prefix=%s%n",
            Arrays.toString(inputs[i]), vals[i], gotLength, Arrays.toString(gotPrefix),
            expectedLengths[i], Arrays.toString(expectedPrefixes[i]));
    }
}

    /**
 * Intuition: this is a stable filter written into the same array. writeIndex
 * always marks the next slot in the kept prefix. Values equal to val are
 * ignored; every other value is copied forward and expands the prefix.
 *
 * Algorithm:
 *   1. Return 0 when nums is null.
 *   2. Start writeIndex at 0.
 *   3. Scan each currentElement in nums.
 *   4. Copy non-val elements to nums[writeIndex] and advance writeIndex.
 *
 * Time:  O(n) - each element is inspected once.
 * Space: O(1) - filtering happens in the original array.
 *
 * @param nums array to filter in place
 * @param val value to remove
 * @return number of values not equal to val
 */
    public int removeElement(int[] nums, int val) {
        if (nums == null) {
            return 0;
        }

        // Index to write next valid element (not equal to val)
        int writeIndex = 0;

        // Process each element in the array
        for (int currentElement : nums) {
            // Keep element if it's not equal to val
            if (currentElement != val) {
                nums[writeIndex] = currentElement;
                writeIndex++;
            }
            // Skip elements equal to val (don't write them)
        }

        return writeIndex;
    }
}

package arrays.twopointers;

import java.util.*;

/**
 * Problem: Remove Element
 *
 * Given an integer array nums and an integer val, remove all occurrences of val in nums
 * in-place. The order of the elements may be changed. Return the number of elements in
 * nums which are not equal to val.
 *
 * Consider the number of elements in nums which are not equal to val be k, to get accepted,
 * you need to do the following things:
 * - Change the array nums such that the first k elements of nums contain the elements
 *   which are not equal to val. The remaining elements of nums are not important as well
 *   as the size of nums.
 * - Return k.
 *
 * Example:
 * Input: nums = [3,2,2,3], val = 3
 * Output: 2, nums = [2,2,_,_]
 * Explanation: Your function should return k = 2, with the first two elements of nums being 2.
 * It does not matter what you leave beyond the returned k.
 *
 * Input: nums = [0,1,2,2,3,0,4,2], val = 2
 * Output: 5, nums = [0,1,3,0,4,_,_,_]
 * Explanation: The first 5 elements contain all non-2 values: [0,1,3,0,4]
 *
 * LeetCode: https://leetcode.com/problems/remove-element
 *
 * Follow-up Questions:
 * 1. Q: What if the order of remaining elements must be preserved?
 *    A: Current solution already preserves relative order of non-val elements.
 *
 * 2. Q: How would you optimize when most elements equal val?
 *    A: Use swap-from-end approach to avoid unnecessary writes.
 *
 * 3. Q: What if you needed to remove multiple different values?
 *    A: Could use HashSet to check if element should be removed, same two-pointer approach.
 *
 * 4. Q: How would you handle very large arrays where val is rare?
 *    A: Current O(n) solution is optimal, but could add early termination if tracking remaining val count.
 *
 * Related Problems:
 * - Remove Duplicates from Sorted Array: https://leetcode.com/problems/remove-duplicates-from-sorted-array/
 * - Move Zeroes: https://leetcode.com/problems/move-zeroes/
 * - Remove Duplicates from Sorted Array II: https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RemoveElement {

    /**
     * Removes all occurrences of val using two-pointer technique.
     *
     * Algorithm:
     * 1. Use writeIndex to track position for next valid element
     * 2. Iterate through array with enhanced for loop
     * 3. When element != val, place it at writeIndex and increment writeIndex
     * 4. When element == val, skip it (don't write, don't increment writeIndex)
     * 5. Return writeIndex as count of remaining valid elements
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(1) using constant extra space
     *
     * @param nums array to remove elements from
     * @param val value to remove from array
     * @return number of elements remaining after removal
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

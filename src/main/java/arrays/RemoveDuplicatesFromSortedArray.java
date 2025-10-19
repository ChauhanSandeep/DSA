package arrays;

/**
 * Problem: Remove Duplicates from Sorted Array
 *
 * Given an integer array nums sorted in non-decreasing order, remove the duplicates
 * in-place such that each unique element appears only once. The relative order of
 * the elements should be kept the same.
 *
 * Since it is impossible to change the length of the array in some languages, you must
 * instead have the result be placed in the first part of the array nums. More formally,
 * if there are k elements after removing the duplicates, then the first k elements of
 * nums should hold the final result. It does not matter what you leave beyond the first k elements.
 *
 * Return k after placing the final result in the first k slots of nums.
 *
 * Example:
 * Input: nums = [1,1,2]
 * Output: 2, nums = [1,2,_]
 * Explanation: Your function should return k = 2, with first two elements being 1 and 2.
 * It does not matter what you leave beyond the returned k (hence they are underscores).
 *
 * Input: nums = [0,0,1,1,1,2,2,3,3,4]
 * Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
 * Explanation: First 5 elements contain unique values: [0,1,2,3,4]
 *
 * LeetCode: https://leetcode.com/problems/remove-duplicates-from-sorted-array
 *
 * Follow-up Questions:
 * 1. Q: What if the array allowed duplicates up to k times?
 *    A: Modify condition to check nums[writeIndex - k] instead of nums[writeIndex - 1].
 *
 * 2. Q: How would you handle an unsorted array?
 *    A: Would need to sort first O(n log n) or use HashSet to track seen elements O(n) space.
 *
 * 3. Q: What if you needed to maintain count of removed duplicates?
 *    A: Track duplicateCount++ whenever nums[i] == nums[writeIndex - 1].
 *
 * 4. Q: How would you handle very large arrays with memory constraints?
 *    A: Current O(1) space solution is already optimal for memory usage.
 *
 * Related Problems:
 * - Remove Duplicates from Sorted Array II: https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
 * - Remove Duplicates from Sorted List: https://leetcode.com/problems/remove-duplicates-from-sorted-list/
 * - Remove Element: https://leetcode.com/problems/remove-element/
 */
public class RemoveDuplicatesFromSortedArray {

    /**
     * Removes duplicates using two-pointer technique for in-place modification.
     *
     * Algorithm:
     * 1. Use writeIndex to track position for next unique element
     * 2. Iterate through array with read pointer (enhanced for loop)
     * 3. If current element is different from last written element, write it
     * 4. Always write first element (writeIndex == 0 case)
     * 5. Return writeIndex which represents count of unique elements
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(1) using constant extra space
     *
     * @param nums sorted array to remove duplicates from
     * @return number of unique elements after removing duplicates
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

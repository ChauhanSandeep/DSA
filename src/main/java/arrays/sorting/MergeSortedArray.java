package arrays.sorting;

/**
 * Merge Sorted Array
 *
 * Problem: Merge two sorted arrays into first array in-place. First array has enough space
 * to hold all elements from both arrays.
 *
 * Example: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3 -> nums1 = [1,2,2,3,5,6]
 * Merge nums2 into nums1 maintaining sorted order.
 *
 * LeetCode: https://leetcode.com/problems/merge-sorted-array
 *
 * Follow-up Questions:
 * - How to merge k sorted arrays? (Use priority queue or divide-and-conquer)
 * - What if arrays are not sorted? (Sort first or use different merging strategy)
 * - Can we solve without extra space? (Current solution is already in-place)
 */
public class MergeSortedArray {

    /**
     * Merges two sorted arrays into the first array in-place.
     *
     * Algorithm:
     * 1. Use three pointers: end of nums1, end of nums2, and merge position
     * 2. Work backwards from end to avoid overwriting unprocessed elements
     * 3. Compare elements and place larger one at merge position
     * 4. Continue until all elements from nums2 are merged
     * 5. Remaining elements in nums1 are already in correct position
     *
     * Time Complexity: O(len1 + len2) where len1, len2 are lengths of arrays
     * Space Complexity: O(1) - in-place merging
     *
     * @param nums1 first sorted array with extra space at end
     * @param len1 number of elements in nums1
     * @param nums2 second sorted array
     * @param len2 number of elements in nums2
     */
    public void merge(int[] nums1, int len1, int[] nums2, int len2) {
        int index1 = len1 - 1;      // Last element in nums1
        int index2 = len2 - 1;      // Last element in nums2
        int writeIndex = len1 + len2 - 1;  // Last position in merged array

        // Merge from back to front
        while (index1 >= 0 && index2 >= 0) {
            if (nums1[index1] > nums2[index2]) {
                nums1[writeIndex] = nums1[index1];
                index1--;
            } else {
                nums1[writeIndex] = nums2[index2];
                index2--;
            }
            writeIndex--;
        }

        // Copy remaining elements from nums2 (if any)
        while (index2 >= 0) {
            nums1[writeIndex] = nums2[index2];
            index2--;
            writeIndex--;
        }

        // No need to copy remaining from nums1 as they're already in place
    }
}

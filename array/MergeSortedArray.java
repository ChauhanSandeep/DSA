package array;

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
     * Time Complexity: O(m + n) where m, n are lengths of arrays
     * Space Complexity: O(1) - in-place merging
     *
     * @param nums1 first sorted array with extra space at end
     * @param m number of elements in nums1
     * @param nums2 second sorted array
     * @param n number of elements in nums2
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;      // Last element in nums1
        int j = n - 1;      // Last element in nums2
        int k = m + n - 1;  // Last position in merged array

        // Merge from back to front
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k] = nums1[i];
                i--;
            } else {
                nums1[k] = nums2[j];
                j--;
            }
            k--;
        }

        // Copy remaining elements from nums2 (if any)
        while (j >= 0) {
            nums1[k] = nums2[j];
            j--;
            k--;
        }

        // No need to copy remaining from nums1 as they're already in place
    }

    /**
     * Alternative implementation with clearer variable names
     * Time Complexity: O(m + n), Space Complexity: O(1)
     */
    public void mergeAlternative(int[] nums1, int m, int[] nums2, int n) {
        int nums1Pointer = m - 1;
        int nums2Pointer = n - 1;
        int mergePointer = m + n - 1;

        // Process all elements from nums2
        while (nums2Pointer >= 0) {
            // If nums1 has more elements and current element is larger
            if (nums1Pointer >= 0 && nums1[nums1Pointer] > nums2[nums2Pointer]) {
                nums1[mergePointer] = nums1[nums1Pointer];
                nums1Pointer--;
            } else {
                nums1[mergePointer] = nums2[nums2Pointer];
                nums2Pointer--;
            }
            mergePointer--;
        }
    }

    /**
     * Forward merging approach using extra space (for comparison)
     * Time Complexity: O(m + n), Space Complexity: O(m + n)
     */
    public void mergeWithExtraSpace(int[] nums1, int m, int[] nums2, int n) {
        int[] temp = new int[m + n];
        int i = 0, j = 0, k = 0;

        // Merge both arrays into temp
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                temp[k++] = nums1[i++];
            } else {
                temp[k++] = nums2[j++];
            }
        }

        // Copy remaining elements
        while (i < m) temp[k++] = nums1[i++];
        while (j < n) temp[k++] = nums2[j++];

        // Copy back to nums1
        System.arraycopy(temp, 0, nums1, 0, m + n);
    }

    /**
     * Insertion-style merging (less efficient but intuitive)
     * Time Complexity: O(m * n), Space Complexity: O(1)
     */
    public void mergeInsertion(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i < n; i++) {
            int elementToInsert = nums2[i];
            int insertPos = m + i;

            // Find correct position for current element
            while (insertPos > 0 && nums1[insertPos - 1] > elementToInsert) {
                nums1[insertPos] = nums1[insertPos - 1];
                insertPos--;
            }

            nums1[insertPos] = elementToInsert;
        }
    }
}

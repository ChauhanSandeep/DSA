package arrays.sorting;

import java.util.Arrays;
/**
 * Problem: Merge Sorted Array
 *
 * Given two sorted arrays, merge the second into the first in non-decreasing order.
 * The first array has enough trailing capacity to hold all values, so the merge
 * must finish in place.
 *
 * Leetcode: https://leetcode.com/problems/merge-sorted-array/ (Easy)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Two pointers | Reverse merge | In-place array fill
 *
 * Example:
 *   Input:  nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
 *   Output: [1,2,2,3,5,6]
 *   Why:    filling from the back avoids overwriting unmerged nums1 values.
 *
 * Follow-ups:
 *   1. Merge k sorted arrays?
 *      Use a min-heap or divide-and-conquer merging.
 *   2. nums1 has no extra capacity?
 *      Allocate an output array or shift values with extra cost.
 *   3. Merge descending arrays?
 *      Mirror the comparisons or fill from the front when free space is at the front.
 *
 * Related: Merge Two Sorted Lists (21), Merge k Sorted Lists (23).
 */
public class MergeSortedArray {

    public static void main(String[] args) {
        MergeSortedArray solver = new MergeSortedArray();

        int[][] nums1Cases = { {1, 2, 3, 0, 0, 0}, {0}, {2, 0} };
        int[] len1Cases = { 3, 0, 1 };
        int[][] nums2Cases = { {2, 5, 6}, {1}, {1} };
        int[] len2Cases = { 3, 1, 1 };
        int[][] expected = { {1, 2, 2, 3, 5, 6}, {1}, {1, 2} };

        for (int i = 0; i < nums1Cases.length; i++) {
            int[] input = nums1Cases[i].clone();
            solver.merge(input, len1Cases[i], nums2Cases[i], len2Cases[i]);
            System.out.printf("nums1=%s nums2=%s -> output=%s  expected=%s%n",
                Arrays.toString(nums1Cases[i]), Arrays.toString(nums2Cases[i]),
                Arrays.toString(input), Arrays.toString(expected[i]));
        }
    }

/**
 * Intuition: the unused space is at the end of nums1, so write the largest
 * remaining value there first. This preserves all unprocessed nums1 values because
 * the write pointer never overwrites anything still needed.
 *
 * Algorithm:
 *   1. Validate input arrays and logical lengths.
 *   2. Handle empty nums2 or empty nums1 prefixes.
 *   3. Compare the last unmerged values from nums1 and nums2.
 *   4. Write the larger value at writeIndex and move that pointer left.
 *   5. Copy any remaining nums2 values.
 *
 * Time:  O(len1 + len2) - each logical value is moved at most once.
 * Space: O(1) - merging writes directly into nums1.
 *
 * @param nums1 first sorted array with trailing capacity
 * @param len1 number of real values currently in nums1
 * @param nums2 second sorted array
 * @param len2 number of values to merge from nums2
 */
    public void merge(int[] nums1, int len1, int[] nums2, int len2) {
        validateInputs(nums1, len1, nums2, len2);

        if (len2 == 0) {
            return;
        }

        if (len1 == 0) {
            System.arraycopy(nums2, 0, nums1, 0, len2);
            return;
        }

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

    private void validateInputs(int[] nums1, int len1, int[] nums2, int len2) {
        if (nums1 == null || nums2 == null) {
            throw new IllegalArgumentException("Input arrays must not be null");
        }

        if (len1 < 0 || len2 < 0) {
            throw new IllegalArgumentException("Array lengths must be non-negative");
        }

        if (len1 > nums1.length) {
            throw new IllegalArgumentException("len1 cannot exceed nums1 length");
        }

        if (len2 > nums2.length) {
            throw new IllegalArgumentException("len2 cannot exceed nums2 length");
        }

        if (len1 + len2 > nums1.length) {
            throw new IllegalArgumentException("nums1 does not have enough capacity for merge");
        }
    }
}

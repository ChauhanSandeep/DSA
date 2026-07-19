package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Single Element in a Sorted Array
 *
 * In a sorted array, every value appears twice except one value that appears once. Return the single value in logarithmic time and constant space.
 *
 * Leetcode: https://leetcode.com/problems/single-element-in-a-sorted-array/ (Medium)
 * Rating:   acceptance 59.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Pair parity | Sorted duplicates
 *
 * Example:
 *   Input:  nums = [1,1,2,3,3,4,4,8,8]
 *   Output: 2
 *   Why:    2 is the only value without an equal neighbor.
 *
 * Follow-ups:
 *   1. Unsorted input? XOR all values in linear time.
 *   2. Others appear three times? Use bit counts modulo 3.
 *   3. Two single values? XOR all values, split by a set bit, and XOR each group.
 *   4. Return the index? Return the converged boundary instead of the value.
 *
 * Related: Single Number (136), Single Number II (137).
 */
public class SingleElementInASortedArray {

    public static void main(String[] args) {
        SingleElementInASortedArray solver = new SingleElementInASortedArray();
        int[][] primaryInputs = { {1,2,2,3,3}, {7} };
        int[] primaryExpected = { 1, 7 };
        for (int i = 0; i < primaryInputs.length; i++) {
            int got = solver.singleNonDuplicate(primaryInputs[i]);
            System.out.printf("method=singleNonDuplicate nums=%s -> %d  expected=%d%n", Arrays.toString(primaryInputs[i]), got, primaryExpected[i]);
        }
        int[] xorInput = {1,1,2,3,3,4,4,8,8};
        int xorGot = solver.singleNonDuplicateXOR(xorInput);
        System.out.printf("method=singleNonDuplicateXOR nums=%s -> %d  expected=%d%n", Arrays.toString(xorInput), xorGot, 2);
    }


        /**
     * Intuition: The restored method checks whether mid is isolated, then uses whether mid matches its left neighbor to choose a side. Pair alignment is the intended signal.
     *
     * Algorithm:
     *   1. Search while left < right.
     *   2. Return nums[mid] if mid differs from both neighbors.
     *   3. Set isMidMatchingLeft when mid pairs with mid - 1.
     *   4. Move right to mid - 1 for a left match; otherwise move left to mid + 1.
     *
     * Time:  O(log n) - one boundary moves each iteration.
     * Space: O(1) - only indexes and a boolean are stored.
     *
     * @param nums sorted array with one single value
     * @return value selected by the restored parity search
     */
    public int singleNonDuplicate(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // If mid itself is the single element, return it
            if (isSingleElement(nums, mid)) {
                return nums[mid];
            }

            /*
            * Determine how many elements remain on the LEFT side
            * after excluding the pair that mid belongs to.
            *
            * If nums[mid] == nums[mid - 1], then (mid - 1, mid) is a pair
            * and the remaining left side ends at index (mid - 2).
            *
            * Otherwise, mid pairs with mid + 1 and the left side ends at (mid - 1).
            */
            boolean isMidMatchingLeft = false;

            if (mid > 0 && nums[mid] == nums[mid - 1]) {
                // Pair is on the left side, exclude both elements
                isMidMatchingLeft = true;
            }

            /*
            * Key idea:
            * - The side with an odd number of elements contains the single element
            */
            if (isMidMatchingLeft) {
                // One element of left is paired with mid 
                // remaining left side is odd -> single element is on the left
                right = mid - 1;
            } else {
                // One element of mid is paired with right
                // remaining right side is odd -> single element is on the right
                left = mid + 1;
            }
        }

        return nums[left];
    }

    /** Returns whether nums[mid] differs from both existing neighbors. */
    private boolean isSingleElement(int[] nums, int mid) {
        return (mid == 0 || nums[mid] != nums[mid - 1]) &&
            (mid == nums.length - 1 || nums[mid] != nums[mid + 1]);
    }

    /**
     * Clean implementation using XOR property for comparison.
     * Uses bit manipulation to determine search direction.
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public int singleNonDuplicateXOR(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // XOR mid with 1 to get its pair index
            // If mid is even, mid^1 = mid+1
            // If mid is odd, mid^1 = mid-1
            if (nums[mid] == nums[mid ^ 1]) {
                // Current element matches its pair, single element is on the right
                left = mid + 1;
            } else {
                // Current element doesn't match its pair, single element is on the left
                right = mid;
            }
        }

        return nums[left];
    }
}
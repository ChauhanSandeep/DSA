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
        int[][] primaryInputs = { {1}, {1,1,2}, {3,3,7,7,10,11,11}, {1,1,2,2,3}, {1,2,2,3,3} };
        int[] primaryExpected = { 1, 2, 10, 3, 1 };
        for (int i = 0; i < primaryInputs.length; i++) {
            int got = solver.singleNonDuplicate(primaryInputs[i]);
            System.out.printf("method=singleNonDuplicate input=%s -> output=%d  expected=%d%n", Arrays.toString(primaryInputs[i]), got, primaryExpected[i]);
        }
        int[] xorInput = {1,1,2,3,3,4,4,8,8};
        int xorGot = solver.singleNonDuplicateXOR(xorInput);
        System.out.printf("method=singleNonDuplicateXOR nums=%s -> %d  expected=%d%n", Arrays.toString(xorInput), xorGot, 2);
    }


    /**
     * Intuition: Before the single value, each pair starts at an even index and ends at the next odd index. After the single value, that alignment flips.
     * So binary search only needs to inspect the pair that starts at an even mid: if it is still a valid pair, the single value is to the right; otherwise it is at or to the left.
     *
     * Algorithm:
     *   1. Pick the middle pair start by moving an odd mid one step left.
     *   2. If nums[mid] equals nums[mid + 1], discard that complete pair and everything before it.
     *   3. Otherwise the broken alignment is on the left side, so keep mid in the search range.
     *
     * Time:  O(log n) - each check discards about half of the remaining array.
     * Space: O(1) - only two indexes are stored.
     *
     * @param nums sorted array with one single value
     * @return the value that appears once
     */
    public int singleNonDuplicate(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if ((mid & 1) == 1) {
                mid--;
            }

            if (nums[mid] == nums[mid + 1]) {
                left = mid + 2;
            } else {
                right = mid;
            }
        }

        return nums[left];
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
package Array;

import java.util.Arrays;

/**
 * Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.
 * If such an arrangement is not possible, rearrange it to the lowest possible order (sorted in ascending order).
 */
public class NextPermutation {
    public static void main(String[] args) {
        int[] nums = {1, 5, 8, 4, 7, 6, 5, 3, 1};
        nextPermutation(nums);
        System.out.println(Arrays.toString(nums)); // Output: [1, 5, 8, 5, 7, 6, 4, 3, 1]
    }

    /**
     * Generates the next lexicographical permutation in-place.
     * @param nums The array of numbers
     */
    public static void nextPermutation(int[] nums) {
        int pivotIndex = nums.length - 2;

        // Step 1: Find the pivot where nums[i] < nums[i + 1]
        while (pivotIndex >= 0 && nums[pivotIndex] >= nums[pivotIndex + 1]) {
            pivotIndex--;
        }

        // Step 2: If pivot exists, find the smallest number larger than nums[pivotIndex] and swap
        if (pivotIndex >= 0) {
            int swapIndex = nums.length - 1;
            while (swapIndex > pivotIndex && nums[swapIndex] <= nums[pivotIndex]) {
                swapIndex--;
            }
            swap(nums, pivotIndex, swapIndex);
        }

        // Step 3: Reverse the part of the array after pivotIndex to get the next permutation
        reverseFromIndex(nums, pivotIndex + 1);
    }

    /**
     * Reverses the array from the given start index to the end.
     */
    private static void reverseFromIndex(int[] nums, int start) {
        int left = start, right = nums.length - 1;
        while (left < right) {
            swap(nums, left, right);
            left++;
            right--;
        }
    }

    /**
     * Swaps two elements in the array.
     */
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}

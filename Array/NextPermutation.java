package Array;

import java.util.Arrays;

/**
 * Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.
 * If such an arrangement is not possible, it must rearrange it as the lowest possible order (i.e., sorted in ascending order).
 */
public class NextPermutation {
    public static void main(String[] args) {
        int[] nums = {1,5,8,4,7,6,5,3,1};
        // 1,5,8,5,7,6,4,3,1
        nextPermutation(nums);
        System.out.println(Arrays.toString(nums));

    }

    /**
     * 1. find the index (pivot) where next element is greater the current element
     * 2. find the smallest element on the right side of pivot which is greater than pivot element, and swap these two
     * 3. reverse the right side of the pivot
     *
     * 1,5,8,4,7,6,5,3,1
     * 1,5,8,5,7,6,4,3,1
     * 1,5,8,5,1,3,4,6,7
     *
     * Note: go through each of these steps one by on in interview
     * @param nums
     */
    public static void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        // find the index (pivot) where next element is greater the current element
        while (i >= 0 && nums[i + 1] <= nums[i]) {
            i--;
        }
        // find the smallest element on the right side of pivot which is greater than pivot element
        if (i >= 0) {
            int j = nums.length - 1;
            while (j >= 0 && nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        // reverse the right side of the pivot
        reverse(nums, i + 1);
    }

    private static void reverse(int[] nums, int start) {
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}

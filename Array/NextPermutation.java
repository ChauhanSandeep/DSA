package Array;

import java.util.Arrays;


/**
 * Problem: Next Permutation
 *
 * Given an array of integers `nums`, rearrange the numbers to form the lexicographically
 * next greater permutation of numbers. If such arrangement is not possible (i.e., the array is
 * sorted in descending order), rearrange it as the lowest possible order (i.e., sorted in ascending order).
 *
 * The replacement must be in-place and use only constant extra memory.
 *
 * Example:
 * Input : [1, 3, 5, 4, 2]
 * Output: [1, 4, 2, 3, 5]
 *
 * Leetcode Link: https://leetcode.com/problems/next-permutation/
 */
public class NextPermutation {

  public static void main(String[] args) {
    int[] nums = {1, 3, 5, 4, 2};
    getNextPermutation(nums);
    System.out.println(Arrays.toString(nums));  // Output: [1, 4, 2, 3, 5]
  }

  /**
   * Modifies the input array to its next lexicographical permutation.
   *
   * Steps:
   * 1. Traverse the array from right to left and find the first element (pivot)
   *    which is smaller than its next element.
   * 2. If such a pivot is found:
   *    - Find the smallest element greater than the pivot in the right half.
   *    - Swap them.
   * 3. Reverse the subarray to the right of the pivot index to get the next permutation.
   *
   * Time Complexity: O(n) — where n is the length of the array
   * Space Complexity: O(1) — in-place manipulation
   *
   * @param nums The array to transform to its next permutation.
   */
  public static void getNextPermutation(int[] nums) {
    int pivotIndex = nums.length - 2;

    // Step 1: Find the first decreasing element from the end
    while (pivotIndex >= 0 && nums[pivotIndex] >= nums[pivotIndex + 1]) {
      pivotIndex--;
    }

    // Step 2: If pivot is found, swap it with the next greater element on the right
    if (pivotIndex >= 0) {
      int nextGreaterIndex = nums.length - 1;
      while (nums[nextGreaterIndex] <= nums[pivotIndex]) {
        nextGreaterIndex--;
      }
      swap(nums, pivotIndex, nextGreaterIndex);
    }
    // swapped [1, 3, 5, 4, 2] to [1, 4, 5, 3, 2]
    //             ⬆    ⬆

    // Step 3: Reverse the suffix starting after the pivot index
    reverseSubarray(nums, pivotIndex + 1);
    // swapped [1, 4, 5, 3, 2] to [1, 4, 2, 3, 5]
    //                {      }
  }

  /**
   * Reverses the subarray from the given start index to the end.
   *
   * @param nums The array in which the subarray needs to be reversed
   * @param start The starting index of the subarray to reverse
   */
  private static void reverseSubarray(int[] nums, int start) {
    int left = start, right = nums.length - 1;
    while (left < right) {
      swap(nums, left, right);
      left++;
      right--;
    }
  }

  /**
   * Swaps two elements in an array.
   *
   * @param nums The array
   * @param i Index of the first element
   * @param j Index of the second element
   */
  private static void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
  }
}
package arrays;

import java.util.Arrays;


/**
 * Problem: Next Permutation
 *
 * Rearrange the array into the next lexicographically greater permutation. If the
 * current arrangement is already the greatest possible order, rearrange it into
 * the lowest order instead. The change must happen in place.
 *
 * Leetcode: https://leetcode.com/problems/next-permutation/ (Medium)
 * Rating:   acceptance 45.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Right-to-left greedy
 *
 * Example:
 *   Input:  nums = [1,3,5,4,2]
 *   Output: [1,4,2,3,5]
 *   Why:    the rightmost position that can be increased is 3; swapping it with
 *           4 and minimizing the suffix gives the smallest greater permutation.
 *
 * Follow-ups:
 *   1. Generate the previous permutation?
 *      Mirror the comparisons: find the rightmost drop, swap with the largest smaller value, then reverse the suffix.
 *   2. Return the k-th next permutation directly?
 *      Use factorial number system ranking/unranking when all values are distinct.
 *   3. Generate all unique permutations with duplicates?
 *      Sort first, then backtrack while skipping equal sibling choices.
 *
 * Related: Permutations (46), Permutations II (47), Permutation Sequence (60).
 */
public class NextPermutation {

    public static void main(String[] args) {
        NextPermutation solver = new NextPermutation();

        int[][] inputs = { {1, 2, 3}, {3, 2, 1}, {1, 1, 5}, {1, 3, 5, 4, 2} };
        String[] expected = { "[1, 3, 2]", "[1, 2, 3]", "[1, 5, 1]", "[1, 4, 2, 3, 5]" };

        for (int i = 0; i < inputs.length; i++) {
            int[] nums = inputs[i].clone();
            getNextPermutation(nums);
            System.out.printf("nums=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(nums), expected[i]);
        }
    }



  /**
   * Intuition: to get the next lexicographic order, make the smallest possible
   * increase as far right as possible. The rightmost dip is the pivot that can grow;
   * swapping it with the smallest larger value on its right makes the increase, and
   * reversing the suffix makes the remaining tail as small as possible.
   *
   * Algorithm:
   *   1. Find the rightmost pivot where nums[pivot] < nums[pivot + 1].
   *   2. If a pivot exists, find the rightmost value greater than nums[pivot] and swap.
   *   3. Reverse the suffix after pivot to put it in ascending order.
   *
   * Time:  O(n) - the scans and suffix reversal each touch at most n elements.
   * Space: O(1) - the permutation is rearranged in place.
   *
   * @param nums array mutated to its next permutation
   */
  public static void getNextPermutation(int[] nums) {
    int pivot = nums.length - 2;

    // Step 1: Find the pivot index. First element (from right) that is smaller than its next element
    while (pivot >= 0 && nums[pivot] >= nums[pivot + 1]) {
      pivot--;
    }

    // Step 2: If pivot is found, swap it with the next greater element on the right
    if (pivot >= 0) {
      int nextGreaterIndex = nums.length - 1;
      while (nums[nextGreaterIndex] <= nums[pivot]) {
        nextGreaterIndex--;
      }
      swap(nums, pivot, nextGreaterIndex);
    }
    // swapped [1, 3, 5, 4, 2] to [1, 4, 5, 3, 2]
    //             ⬆    ⬆

    // Step 3: Reverse the suffix starting after the pivot index
    reverseSubarray(nums, pivot + 1);
    // swapped [1, 4, 5, 3, 2] to [1, 4, 2, 3, 5]
    //                {      }
  }

  public static void getPreviousPermutation(int[] nums) {
    int length = nums.length;

    // --- Step 1: Find the pivot ---
    // Pivot = first element (from right) that is greater than its next element.
    // Example: [1, 4, 2, 3, 5]
    //              ↑
    // Pivot = 4 (index 1), because 4 > 2.
    int pivot = length - 2;
    while (pivot >= 0 && nums[pivot] <= nums[pivot + 1]) {
      pivot--;
    }

    if (pivot >= 0) {
      // --- Step 2: Find rightmost element smaller than pivot ---
      // Next smaller element than pivot (4) is 3.
      int nextSmallerIndex = length - 1;
      while (nums[nextSmallerIndex] >= nums[pivot]) {
        nextSmallerIndex--;
      }

      // --- Step 3: Swap pivot with nextSmallerIndex ---
      swap(nums, pivot, nextSmallerIndex);

      // Dry run after swap:
      // [1, 4, 2, 3, 5] → swap(4, 3) → [1, 3, 2, 4, 5]
      //     ↑     ↑
    }

    // --- Step 4: Reverse suffix ---
    // Reverse everything after pivot:
    // [1, 3, 2, 4, 5] → reverse suffix [2,4,5] → [1, 3, 5, 4, 2]
    reverseSubarray(nums, pivot + 1);

    // Final: [1, 3, 5, 4, 2]  ← previous permutation
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
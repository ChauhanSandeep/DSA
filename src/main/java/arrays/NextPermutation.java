package arrays;

import java.util.Arrays;


/**
 * LeetCode Problem 31: Next Permutation
 * https://leetcode.com/problems/next-permutation/
 *
 * Problem Statement:
 * A permutation of an array of integers is an arrangement of its members into a sequence or linear order.
 * For example, for arr = [1,2,3], the following are all the permutations of arr: [1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1].
 * The next permutation of an array of integers is the next lexicographically greater permutation of its integer.
 * More formally, if all the permutations of the array are sorted in one container according to their lexicographical order,
 * then the next permutation of that array is the permutation that follows it in the sorted container.
 * If such an arrangement is not possible, the array must be rearranged as the lowest possible order (i.e., sorted in ascending order).
 *
 * Example:
 * Input: nums = [1,2,3]
 * Output: [1,3,2]
 * Explanation: The next permutation of [1,2,3] is [1,3,2].
 *
 * Input: nums = [3,2,1]
 * Output: [1,2,3]
 * Explanation: [3,2,1] is the largest permutation, so the next permutation wraps around to the smallest permutation [1,2,3].
 *
 * Follow-up Questions (FAANG interview style):
 * 1. How would you handle duplicate elements in the array?
 *    - The algorithm works correctly with duplicates as it finds the rightmost ascending pair and next larger element properly.
 * 2. Can you implement this without modifying the input array?
 *    - You could return a new array, but the problem specifically asks for in-place modification for O(1) space complexity.
 * 3. How would you generate the previous permutation instead?
 *    - Similar algorithm but find the rightmost descending pair and swap with the largest smaller element, then reverse suffix.
 * 4. What if you need to generate the k-th next permutation efficiently?
 *    - You could use factorial number system or repeatedly apply next permutation k times (less efficient).
 * Related Problems:
 * - Permutations (LeetCode 46): https://leetcode.com/problems/permutations/
 * - Permutations II (LeetCode 47): https://leetcode.com/problems/permutations-ii/
 * - Permutation Sequence (LeetCode 60): https://leetcode.com/problems/permutation-sequence/
 *
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class NextPermutation {

  public static void main(String[] args) {
    int[] nums = {1, 3, 5, 4, 2};
    getNextPermutation(nums);
    System.out.println(Arrays.toString(nums));  // Output: [1, 4, 2, 3, 5]
  }

  /**
   * Single Pass Algorithm.
   *
   * Algorithm Overview:
   * 1. Find the rightmost character that is smaller than its next character (pivot point).
   * 2. If no such character exists, the permutation is the last permutation; reverse the entire array.
   * 3. Find the ceiling of the pivot character in the right part of the array (smallest element greater than pivot).
   * 4. Swap the pivot character with this ceiling character.
   * 5. Reverse the suffix after the pivot position to get the next smallest permutation.
   *
   * Time Complexity: O(n) - each element is visited at most twice
   * Space Complexity: O(1) - in-place modification
   *
   * @param nums the input array to be rearranged to next permutation
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
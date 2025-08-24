package array;

import java.util.Arrays;


/**
 * 🔹 Problem: Remove Duplicates from Sorted Array II
 * 🔗 Leetcode: https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
 *
 * Given a sorted integer array, remove duplicates in-place such that each unique
 * element appears at most **twice**. Return the new length of the modified array.
 *
 * You must do this in-place with O(1) extra memory.
 *
 * 📌 Example:
 * Input: nums = [0,0,1,1,1,1,2,3,3]
 * Output: 7, nums = [0,0,1,1,2,3,3,_]
 *
 * ✅ Follow-up:
 * 1. What if at most `k` duplicates are allowed? → Generalize the pattern by tracking last `k` writes.
 */
public class RemoveDuplicate2 {

  public static void main(String[] args) {
    int[] nums = {0, 0, 1, 1, 1, 1, 2, 3, 3};
    int newLength = removeDuplicates(nums);

    System.out.println("New Length: " + newLength);
    System.out.println("Modified Array: " + Arrays.toString(Arrays.copyOf(nums, newLength)));
  }

  /**
   * Removes duplicates in a sorted array while allowing at most two occurrences of each element.
   * Modifies the input array in-place and returns the new valid length.
   *
   * 🔹 Approach:
   * - Start writing from index 1 onward.
   * - Track how many times the current element has occurred using a counter.
   * - If the count is <= 2, write it to the next writeable position.
   *
   * 🔹 Time Complexity: O(N) – single pass
   * 🔹 Space Complexity: O(1) – in-place modification
   *
   * @param nums Sorted array of integers
   * @return New length of the modified array
   */
  public static int removeDuplicates(int[] nums) {
    if (nums == null || nums.length == 0) {
      return 0;
    }

    int writePointer = 1; // Position to write next valid element
    int count = 1;        // Count of current element occurrences

    for (int readPointer = 1; readPointer < nums.length; readPointer++) {
      if (nums[readPointer] == nums[readPointer - 1]) {
        count++;
      } else {
        count = 1; // New number encountered, reset count
      }

      // Allow at most 2 duplicates
      if (count <= 2) {
        nums[writePointer] = nums[readPointer];
        writePointer++;
      }
    }

    return writePointer;
  }
}
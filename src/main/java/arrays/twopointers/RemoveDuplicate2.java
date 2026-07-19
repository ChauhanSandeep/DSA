package arrays.twopointers;

import java.util.Arrays;


/**
 * Problem: Remove Duplicates from Sorted Array II
 *
 * Given a sorted array, remove duplicates in place so each distinct value appears
 * at most twice. Return the length of the valid prefix; values after that prefix
 * do not matter.
 *
 * Leetcode: https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/ (Medium)
 * Rating:   acceptance 65.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Bounded duplicate write
 *
 * Example:
 *   Input:  nums = [0,0,1,1,1,1,2,3,3]
 *   Output: 7, nums = [0,0,1,1,2,3,3,_]
 *   Why:    0, 1, and 3 may appear twice, but the extra 1s are skipped.
 *
 * Follow-ups:
 *   1. Allow at most k copies instead of two?
 *      Generalize the counter or compare against the value k positions behind writePointer.
 *   2. What if the input is not sorted?
 *      Use a frequency map or sort first, depending on whether order must be preserved.
 *   3. Return the removed values too?
 *      Collect skipped values while preserving the write-pointer logic.
 *
 * Related: Remove Duplicates from Sorted Array (26), Remove Element (27).
 */
public class RemoveDuplicate2 {

public static void main(String[] args) {
  int[][] inputs = { {0, 0, 1, 1, 1, 1, 2, 3, 3}, {1, 1, 1} };
  int[] expectedLengths = { 7, 2 };
  int[][] expectedPrefixes = { {0, 0, 1, 1, 2, 3, 3}, {1, 1} };

  for (int i = 0; i < inputs.length; i++) {
    int[] nums = inputs[i].clone();
    int gotLength = removeDuplicates(nums);
    int[] gotPrefix = Arrays.copyOf(nums, gotLength);
    System.out.printf("nums=%s -> len=%d prefix=%s  expected=len=%d prefix=%s%n",
        Arrays.toString(inputs[i]), gotLength, Arrays.toString(gotPrefix),
        expectedLengths[i], Arrays.toString(expectedPrefixes[i]));
  }
}

  /**
 * Intuition: because equal values are adjacent, count tracks the run length of
 * the current value. writePointer marks the next valid prefix slot. The first
 * two copies of a run are written; later copies are skipped until a new value
 * resets count.
 *
 * Algorithm:
 *   1. Return 0 for null or empty input.
 *   2. Start writePointer at 1 and count the first value once.
 *   3. Scan with readPointer, resetting or incrementing count by comparing neighbors.
 *   4. Write nums[readPointer] only while count <= 2, then return writePointer.
 *
 * Time:  O(n) - one scan over the sorted array.
 * Space: O(1) - the array is modified in place with two counters.
 *
 * @param nums sorted array to compress in place
 * @return length of the prefix where each value appears at most twice
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
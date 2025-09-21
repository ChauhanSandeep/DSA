package arrays;

import java.util.Arrays;

/**
 * Sort Colors - LeetCode Problem 75
 *
 * Problem Statement:
 * Given an array nums with n objects colored red, white, or blue, sort them in-place
 * so that objects of the same color are adjacent, with colors in the order red, white, blue.
 * Use integers 0, 1, and 2 to represent red, white, and blue respectively.
 * Must solve without using library's sort function.
 *
 * Example:
 * Input: nums = [2,0,2,1,1,0]
 * Output: [0,0,1,1,2,2]
 * Explanation: All red (0) objects first, then white (1), then blue (2).
 * The Dutch National Flag algorithm partitions in one pass: 0s to left,
 * 2s to right, 1s naturally end up in middle.
 *
 * LeetCode Link: https://leetcode.com/problems/sort-colors/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you extend this to k colors instead of 3?
 *    Answer: Use counting sort O(n+k) or modified quicksort with k-way partitioning.
 *
 * 2. What if we need to maintain relative order of equal elements (stable sort)?
 *    Answer: Dutch Flag isn't stable. Use counting sort or merge sort for stability.
 *    Related: LeetCode 324 - Wiggle Sort II (requires stable partitioning)
 *
 * 3. How to handle this problem if colors have priorities other than 0<1<2?
 *    Answer: Map priorities to indices, apply Dutch Flag, then map back.
 *
 * 4. What if memory access is expensive and we want to minimize swaps?
 *    Answer: Use counting sort - only 2 passes, no swaps needed.
 *    Related: LeetCode 41 - First Missing Positive (cycle sort minimizes writes)
 */
public class SortColors {

  public static void main(String[] args) {
    int[] colors = {2, 0, 2, 1, 1, 0};
    sortColors(colors);
    System.out.println("Sorted array: " + Arrays.toString(colors));
  }

  /**
   * Steps:
   * 1. Count occurrences of 0s, 1s, and 2s using a frequency array.
   * 2. Overwrite the original array based on the counts.
   *    - Fill in 0s first, then 1s, and finally 2s.
   *
   * This method ensures that we sort the colors in two passes:
   * - First pass to count frequencies.
   * - Second pass to overwrite the original array.
   *
   * Time Complexity: O(n) — two passes through the array
   * Space Complexity: O(1) — fixed size frequency array
   * @param nums Array containing only 0, 1, and 2
   */
  public static void sortColorsUsingFreqArray(int[] nums) {
    int[] freq = new int[3];
    for (int num : nums) {
      freq[num]++;
    }

    int index = 0;
    int freqIndex = 0;

    while (index < nums.length) {
      if (freq[freqIndex] == 0) {
        freqIndex++;
      } else {
        nums[index++] = freqIndex;
        freq[freqIndex]--;
      }
    }

  }

  /**
   * Optimal Dutch National Flag Algorithm (Single Pass, In-Place)
   *
   * Sorts the input array containing 0s, 1s, and 2s using three pointers:
   * - `zeroPointer` tracks position to place 0s
   * - `twoPointer` tracks position to place 2s
   * - `current` scans the array
   *
   * Steps:
   * 1. Traverse array with `current` pointer.
   * 2. Swap current with `zeroPointer` if element is 0, then move both forward.
   * 3. Swap current with `twoPointer` if element is 2, then only move `twoPointer` backward.
   * 4. If element is 1, just move `current`.
   *
   * Time Complexity: O(n) — one pass
   * Space Complexity: O(1) — in-place sorting
   *
   * @param nums Array containing only 0, 1, and 2
   */
  public static void sortColors(int[] nums) {
    if (nums == null || nums.length <= 1) return;

    int zeroPointer = 0;              // Where to place next 0
    int twoPointer = nums.length - 1; // Where to place next 2
    int currentPointer = 0;

    while (currentPointer <= twoPointer) {
      switch (nums[currentPointer]) {
        case 0:
          swap(nums, zeroPointer, currentPointer);
          zeroPointer++;
          currentPointer++;
          break;
        case 2:
          swap(nums, currentPointer, twoPointer);
          twoPointer--;
          // Do not increment currentPointer here: the swapped-in element may be 0/1/2
          break;
        case 1:
          currentPointer++;
          break;
        default:
          throw new IllegalArgumentException("Array can only contain 0, 1, or 2");
      }
    }
  }

  /**
   * Utility method to swap two elements in the array.
   */
  private static void swap(int[] nums, int i, int j) {
    if (i == j) return; // Avoid unnecessary swaps
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
  }
}
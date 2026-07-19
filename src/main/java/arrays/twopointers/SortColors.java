package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Sort Colors
 *
 * Given an array containing only 0, 1, and 2, sort it in place so equal colors
 * are adjacent in red-white-blue order. The optimal method is the Dutch national
 * flag partition in one pass.
 *
 * Leetcode: https://leetcode.com/problems/sort-colors/ (Medium)
 * Rating:   acceptance 70.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Dutch national flag | Three-way partition
 *
 * Example:
 *   Input:  nums = [2,0,2,1,1,0]
 *   Output: [0,0,1,1,2,2]
 *   Why:    all 0s are moved left, all 2s right, and 1s remain in the middle.
 *
 * Follow-ups:
 *   1. Extend this to k colors?
 *      Use counting sort for O(n + k), or repeated partitions for small k.
 *   2. Make the sort stable?
 *      Dutch flag is not stable; use counting plus rewrite or stable partitioning.
 *   3. Minimize swaps when memory is cheap?
 *      Count each color, then overwrite the array in two passes with no swaps.
 *
 * Related: Sort Array by Parity II (922), Wiggle Sort II (324).
 */
public class SortColors {

public static void main(String[] args) {
  int[][] inputs = { {2, 0, 2, 1, 1, 0}, {1} };
  int[][] expected = { {0, 0, 1, 1, 2, 2}, {1} };

  for (int i = 0; i < inputs.length; i++) {
    int[] nums = inputs[i].clone();
    sortColors(nums);
    System.out.printf("nums=%s -> %s  expected=%s%n",
        Arrays.toString(inputs[i]), Arrays.toString(nums), Arrays.toString(expected[i]));
  }
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
 * Intuition: maintain three regions: confirmed 0s before zeroPointer,
 * confirmed 2s after twoPointer, and unknown values at currentPointer. A 0 is
 * swapped into the left region; a 2 is swapped into the right region and must
 * be rechecked because the incoming value is unknown; a 1 is already central.
 *
 * Algorithm:
 *   1. Guard null or length <= 1 input.
 *   2. Start zeroPointer at 0, currentPointer at 0, and twoPointer at the end.
 *   3. Swap 0s left and advance zeroPointer and currentPointer.
 *   4. Swap 2s right and only decrement twoPointer; advance currentPointer on 1.
 *
 * Time:  O(n) - each position is processed a constant number of times.
 * Space: O(1) - sorting is in place with three pointers.
 *
 * @param nums array containing only 0, 1, and 2
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
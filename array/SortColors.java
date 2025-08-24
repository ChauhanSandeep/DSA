package array;

import java.util.Arrays;

/**
 * ✅ Problem: Sort Colors (a.k.a. Sort 0s, 1s, and 2s)
 *
 * Given an array containing only 0s, 1s, and 2s, sort the array in-place such that elements of the same value are adjacent,
 * and in the order 0s → 1s → 2s.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/sort-colors/
 *
 * 🧠 Example:
 * Input:  [2, 0, 2, 1, 1, 0]
 * Output: [0, 0, 1, 1, 2, 2]
 *
 * 🚩 This is a classic application of the **Dutch National Flag** problem by Edsger Dijkstra.
 *
 * 🔍 Follow-ups:
 * 1. Can you solve it using one-pass and constant space? ✅ Yes, see below.
 * 2. Can you do this without using counting sort or extra space? ✅ Done here.
 * 3. What if the input is a stream or linked list? ➤ Would need to modify approach (e.g., counting first pass or bucket-based).
 */
public class SortColors {

  public static void main(String[] args) {
    int[] colors = {2, 0, 2, 1, 1, 0};
    sortColors(colors);
    System.out.println("Sorted array: " + Arrays.toString(colors));
  }

  /**
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
    int current = 0;

    while (current <= twoPointer) {
      if (nums[current] == 0) {
        swap(nums, zeroPointer, current);
        zeroPointer++;
        current++;
      } else if (nums[current] == 2) {
        swap(nums, current, twoPointer);
        twoPointer--;
        // Do not increment current here: the swapped-in element may be 0/1/2
      } else {
        current++; // When nums[current] == 1
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
package arrays.sorting;

import java.util.Arrays;
/**
 * Problem: Search in Rotated Sorted Array
 *
 * Given a sorted array rotated at an unknown pivot and a target, return the target
 * index or -1. Values are unique, so each binary-search step can identify one
 * sorted half.
 *
 * Leetcode: https://leetcode.com/problems/search-in-rotated-sorted-array/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Binary search | Rotated array | Sorted-half detection
 *
 * Example:
 *   Input:  nums = [4,5,6,7,0,1,2], target = 0
 *   Output: 4
 *   Why:    0 is found at index 4 after discarding sorted halves that cannot contain it.
 *
 * Follow-ups:
 *   1. Duplicates are allowed?
 *      Shrink equal boundaries when the sorted half is ambiguous.
 *   2. Find the rotation pivot?
 *      Binary search for the minimum value.
 *   3. Return first occurrence with duplicates?
 *      Continue searching left after a match, accepting O(n) worst case.
 *
 * Related: Search in Rotated Sorted Array II (81), Find Minimum in Rotated Sorted Array (153).
 */
public class RotatedSortedSearch {

  public static void main(String[] args) {
    int[][] inputs = { {4, 5, 6, 7, 0, 1, 2}, {1}, {3, 1} };
    int[] targets = { 0, 0, 1 };
    int[] expected = { 4, -1, 1 };

    for (int i = 0; i < inputs.length; i++) {
            int got = searchInRotatedArray(inputs[i], targets[i]);
      System.out.printf("nums=%s target=%d -> output=%d  expected=%d%n",
          Arrays.toString(inputs[i]), targets[i], got, expected[i]);
    }
  }

/**
 * Intuition: rotation breaks global sorted order, but at least one side of any
 * midpoint is still sorted when values are unique. If target fits inside that
 * sorted side, search there; otherwise search the other side.
 *
 * Algorithm:
 *   1. Return -1 for null or empty input.
 *   2. Binary search with left and right boundaries.
 *   3. Return mid immediately when nums[mid] equals target.
 *   4. Detect whether the left or right half is sorted.
 *   5. Keep the half that can contain target and discard the other half.
 *
 * Time:  O(log n) - each iteration halves the search interval.
 * Space: O(1) - only binary-search pointers are stored.
 *
 * @param nums rotated sorted array with unique values
 * @param target value to find
 * @return index of target, or -1 if absent
 */
  public static int searchInRotatedArray(int[] nums, int target) {
      if (nums == null || nums.length == 0) {
          return -1;
      }

    int left = 0;
    int right = nums.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;

      // Target found
        if (nums[mid] == target) {
            return mid;
        }

      // Left half is sorted
      if (nums[left] <= nums[mid]) {
        if (target >= nums[left] && target < nums[mid]) {
          right = mid - 1; // Search in left half
        } else {
          left = mid + 1; // Search in right half
        }
      }
      // Right half is sorted
      else {
        if (target > nums[mid] && target <= nums[right]) {
          left = mid + 1; // Search in right half
        } else {
          right = mid - 1; // Search in left half
        }
      }
    }

    return -1; // Target not found
  }
}

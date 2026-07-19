package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Find First and Last Position of Element in Sorted Array
 *
 * Given a sorted array, return the leftmost and rightmost positions of target. If target is absent, return [-1, -1].
 *
 * Leetcode: https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/ (Medium)
 * Rating:   acceptance 49.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Lower bound | Upper bound
 *
 * Example:
 *   Input:  nums = [5,7,7,8,8,10], target = 8
 *   Output: [3,4]
 *   Why:    the first 8 is at index 3 and the last 8 is at index 4.
 *
 * Follow-ups:
 *   1. Count occurrences? Return last - first + 1 after the two searches.
 *   2. Absent insertion range? Use lower-bound positions.
 *   3. Rotated array? Combine boundary search with rotated sorted-half logic.
 *   4. Many queries? Precompute value ranges or binary search each query.
 *
 * Related: Search Insert Position (35), Binary Search (704).
 */
public class FirstAndLastPositionBinarySearch {

  public static void main(String[] args) {
    int[][] inputs = { {5,7,7,8,8,10}, {5,7,7,8,8,10}, {1} };
    int[] targets = { 8, 6, 1 };
    int[][] expected = { {3,4}, {-1,-1}, {0,0} };
    for (int i = 0; i < inputs.length; i++) {
      int[] got = searchRange(inputs[i], targets[i]);
      System.out.printf("nums=%s target=%d -> %s  expected=%s%n", Arrays.toString(inputs[i]), targets[i], Arrays.toString(got), Arrays.toString(expected[i]));
    }
  }




    /**
   * Intuition: One binary search can find a copy; two biased searches find the boundaries. The helper keeps searching after a match toward the requested side.
   *
   * Algorithm:
   *   1. Return [-1, -1] for null or empty input.
   *   2. Find the first occurrence.
   *   3. Return [-1, -1] if target is absent.
   *   4. Find the last occurrence and return both indexes.
   *
   * Time:  O(log n) - two binary searches.
   * Space: O(1) - only indexes are stored.
   *
   * @param nums sorted input array
   * @param target value to locate
   * @return [firstIndex, lastIndex] or [-1, -1]
   */
  public static int[] searchRange(int[] nums, int target) {
    if (nums == null || nums.length == 0) {
      return new int[]{-1, -1};
    }

    int first = findBoundaryIndex(nums, target, true);
    if (first == -1) {
      return new int[]{-1, -1}; // Target not found
    }

    int last = findBoundaryIndex(nums, target, false);
    return new int[]{first, last};
  }

    /** Finds the requested target boundary index. */
  private static int findBoundaryIndex(int[] nums, int target, boolean findFirst) {
    int left = 0;
    int right = nums.length - 1;
    int result = -1;

    while (left <= right) {
      int mid = left + (right - left) / 2;

      if (nums[mid] == target) {
        result = mid;
        // Adjust search bounds depending on which boundary we want
        if (findFirst) {
          right = mid - 1; // Search left side for first occurrence
        } else {
          left = mid + 1; // Search right side for last occurrence
        }
      } else if (target > nums[mid]) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }

    return result;
  }
}
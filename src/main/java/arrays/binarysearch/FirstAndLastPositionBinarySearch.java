package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Find the first and last position of a given element in a sorted array.
 *
 * ✅ Given a sorted array and a target value, return the starting and ending position of the target.
 *    If the target is not present, return [-1, -1].
 *
 * 🔗 LeetCode Link: https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 *
 * Example:
 * Input: nums = [5, 7, 7, 8, 8, 10], target = 8
 * Output: [3, 4]
 *
 * Follow-up Questions:
 * - Q: Can you do this in O(log n) time?
 *   A: Yes, by using binary search twice.
 * - Q: What if the array is rotated?
 *   A: You need to adapt binary search logic accordingly.
 */
public class FirstAndLastPositionBinarySearch {

  public static void main(String[] args) {
    int[] nums = {5, 7, 7, 8, 8, 10};
    int target = 8;

    int[] result = searchRange(nums, target);
    System.out.println("Start and end index for the target: " + Arrays.toString(result)); // [3, 4]
  }

  /**
   * Finds the first and last position of a target element in a sorted array.
   *
   * Time Complexity: O(log n) for both first and last search
   * Space Complexity: O(1)
   *
   * @param nums   Sorted input array
   * @param target The value to search
   * @return An array containing the first and last position of the target
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

  /**
   * Performs binary search to find either the first or last occurrence of the target.
   *
   * @param nums      Sorted array
   * @param target    Element to search
   * @param findFirst If true, returns index of first occurrence; else, last occurrence
   * @return Index of the boundary occurrence, or -1 if not found
   */
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
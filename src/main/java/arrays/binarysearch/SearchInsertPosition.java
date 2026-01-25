package arrays.binarysearch;

import java.util.Arrays;


/**
 * 35. Search Insert Position
 *
 * Problem Statement:
 * Given a sorted array of distinct integers and a target value, return the index
 * if the target is found. If not, return the index where it would be if it were
 * inserted in order. You must write an algorithm with O(log n) runtime complexity.
 *
 * Example:
 * Input: nums = [1,3,5,6], target = 5
 * Output: 2
 * Explanation: Target 5 is found at index 2.
 *
 * Input: nums = [1,3,5,6], target = 2
 * Output: 1
 * Explanation: Target 2 should be inserted at index 1 to maintain sorted order.
 *
 * Input: nums = [1,3,5,6], target = 7
 * Output: 4
 * Explanation: Target 7 should be inserted at the end (index 4).
 *
 * LeetCode Link: https://leetcode.com/problems/search-insert-position/
 *
 * Follow-up Questions:
 * 1. What if the array contains duplicates and we want the leftmost insertion point?
 *    Answer: The current algorithm already handles this by using >= comparison.
 * 2. What if we want to find the rightmost insertion point for duplicates?
 *    Answer: Modify the condition to use > instead of >= in binary search.
 * 3. How would you handle this if the array was not sorted?
 *    Answer: Either sort first (O(n log n)) or use linear search (O(n)).
 * 4. What if we need to insert multiple elements efficiently?
 *    Answer: Collect all elements, sort them, and use merge-like approach.
 *
 * Related Problems:
 * - 34. Find First and Last Position of Element in Sorted Array: https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 * - 278. First Bad Version: https://leetcode.com/problems/first-bad-version/
 * - 704. Binary Search: https://leetcode.com/problems/binary-search/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class SearchInsertPosition {

  /**
   * Using traditional binary search bounds.
   * This version uses [left, right] inclusive bounds which is more intuitive for some.
   *
   * Algorithm: Inclusive bounds binary search
   * 1. Use inclusive left and right bounds (right = nums.length - 1)
   * 2. Handle three cases in each iteration: equal, less than, greater than
   * 3. When target is found, return immediately
   * 4. When not found, left will point to insertion position
   *
   * Time Complexity: O(log n) - binary search
   * Space Complexity: O(1) - constant space
   *
   * This approach is more explicit about the three comparison cases.
   */
  public int searchInsertInclusive(int[] nums, int target) {
    if (nums == null || nums.length == 0) {
      return 0;
    }

    int left = 0;
    int right = nums.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;

      if (nums[mid] == target) {
        return mid; // Found exact match
      } else if (nums[mid] < target) {
        left = mid + 1; // Search right half
      } else {
        right = mid - 1; // Search left half
      }
    }

    // When loop ends, left points to insertion position
    return left;
  }
}

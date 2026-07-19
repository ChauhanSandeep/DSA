package arrays.binarysearch;

import java.util.Arrays;


/**
 * Problem: Search Insert Position
 *
 * Given a sorted array of distinct integers, return the target index if present or the index where target should be inserted to preserve order.
 *
 * Leetcode: https://leetcode.com/problems/search-insert-position/ (Easy)
 * Rating:   acceptance 51.7% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Insertion point | Inclusive bounds
 *
 * Example:
 *   Input:  nums = [1,3,5,6], target = 2
 *   Output: 1
 *   Why:    2 belongs between 1 and 3, which is index 1.
 *
 * Follow-ups:
 *   1. Rightmost insertion with duplicates? Move left while nums[mid] <= target.
 *   2. Insert many values? Sort them and merge, or binary search independently.
 *   3. Descending array? Reverse the comparison directions.
 *   4. Infinite sorted stream? Expand a window exponentially, then binary search.
 *
 * Related: Binary Search (704), Find First and Last Position of Element in Sorted Array (34).
 */
public class SearchInsertPosition {

  public static void main(String[] args) {
    SearchInsertPosition solver = new SearchInsertPosition();
    int[][] inputs = { {1,3,5,6}, {1,3,5,6}, {1,3,5,6} };
    int[] targets = { 5, 2, 7 };
    int[] expected = { 2, 1, 4 };
    for (int i = 0; i < inputs.length; i++) {
      int got = solver.searchInsertInclusive(inputs[i], targets[i]);
      System.out.printf("nums=%s target=%d -> %d  expected=%d%n", Arrays.toString(inputs[i]), targets[i], got, expected[i]);
    }
  }


    /**
   * Intuition: The insertion point is where left lands after target is proven absent. Exact matches return immediately under inclusive binary-search bounds.
   *
   * Algorithm:
   *   1. Return 0 for null or empty input.
   *   2. Search with left = 0 and right = nums.length - 1.
   *   3. Return mid on equality.
   *   4. Move bounds past mid and return left after the loop.
   *
   * Time:  O(log n) - each comparison halves the range.
   * Space: O(1) - only indexes are stored.
   *
   * @param nums sorted distinct integers
   * @param target value to find or insert
   * @return target index or insertion position
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

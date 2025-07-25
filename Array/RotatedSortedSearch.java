package Array;

/**
 * Problem: Search in Rotated Sorted Array
 *
 * You are given an integer array `nums` that was originally sorted in ascending order,
 * but then rotated at an unknown pivot. Given a target value, return its index if found,
 * else return -1. All the values of `nums` are unique.
 *
 * Leetcode Link:
 * https://leetcode.com/problems/search-in-rotated-sorted-array/
 *
 * Example:
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 *
 * Follow-Up Questions:
 * - What if duplicates are allowed? (Check Leetcode #81)
 * - Can you do this with a recursive binary search?
 * - How to find the rotation pivot index itself?
 */
public class RotatedSortedSearch {

  public static void main(String[] args) {
    int[] nums = {4, 5, 6, 7, 0, 1, 2};
    int target = 0;

    int index = searchInRotatedArray(nums, target);
    System.out.println("Index of target " + target + ": " + index);
  }

  /**
   * Searches for a target in a rotated sorted array using binary search.
   *
   * Steps:
   * 1. At each iteration, check if the left or right half is sorted.
   * 2. If target lies within the sorted half, continue binary search on that side.
   * 3. Otherwise, move to the unsorted half.
   *
   * Time Complexity: O(log N)
   * Space Complexity: O(1)
   *
   * @param nums   The rotated sorted array
   * @param target The element to search for
   * @return Index of target if found, else -1
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
        if (nums[left] <= target && target < nums[mid]) {
          right = mid - 1; // Search in left half
        } else {
          left = mid + 1; // Search in right half
        }
      }
      // Right half is sorted
      else {
        if (nums[mid] < target && target <= nums[right]) {
          left = mid + 1; // Search in right half
        } else {
          right = mid - 1; // Search in left half
        }
      }
    }

    return -1; // Target not found
  }
}
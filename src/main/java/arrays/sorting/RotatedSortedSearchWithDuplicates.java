package arrays.sorting;

import java.util.Arrays;
/**
 * Problem: Search in Rotated Sorted Array II
 *
 * Given a rotated sorted array that may contain duplicates, determine whether a
 * target exists. Duplicates can hide which half is sorted, so the search may need
 * to shrink both ends.
 *
 * Leetcode: https://leetcode.com/problems/search-in-rotated-sorted-array-ii/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Binary search | Duplicates | Ambiguous boundaries
 *
 * Example:
 *   Input:  nums = [2,5,6,0,0,1,2], target = 0
 *   Output: true
 *   Why:    binary search reaches one of the zeros after handling the rotated split.
 *
 * Follow-ups:
 *   1. Can worst-case be better than O(n)?
 *      No; an array of equal values can force boundary shrink one step at a time.
 *   2. Return an index instead of a boolean?
 *      Return mid on match, but worst-case complexity remains O(n).
 *   3. Count all target occurrences?
 *      Find one match, then expand or run boundary searches with duplicate handling.
 *
 * Related: Search in Rotated Sorted Array (33), Find Minimum in Rotated Sorted Array II (154).
 */
public class RotatedSortedSearchWithDuplicates {

    public static void main(String[] args) {
        int[][] inputs = { {2, 5, 6, 0, 0, 1, 2}, {1, 1, 1, 1}, {1, 0, 1, 1, 1} };
        int[] targets = { 0, 2, 0 };
        boolean[] expected = { true, false, true };

        for (int i = 0; i < inputs.length; i++) {
            boolean got = search(inputs[i], targets[i]);
            System.out.printf("nums=%s target=%d -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), targets[i], got, expected[i]);
        }
    }

/**
 * Intuition: the unique-values rotated search works unless equal boundary values
 * make both halves look the same. When nums[left], nums[mid], and nums[right] are
 * equal and mid is not target, shrinking both ends safely removes duplicates.
 *
 * Algorithm:
 *   1. Return false for null or empty input.
 *   2. Binary search while left <= right.
 *   3. Return true when nums[mid] equals target.
 *   4. Shrink both boundaries when duplicates make the sorted half ambiguous.
 *   5. Otherwise keep the sorted half that can contain target.
 *
 * Time:  O(log n) average, O(n) worst case - duplicates can force linear shrinkage.
 * Space: O(1) - only binary-search pointers are stored.
 *
 * @param nums rotated sorted array that may contain duplicates
 * @param target value to find
 * @return true if target exists
 */
    public static boolean search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int left = 0;
        int right = nums.length - 1;

        // Binary search loop
        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Target found
            if (nums[mid] == target) {
              return true;
            }

            // Handle duplicates: shrink the search space
            // This is because we cannot determine which side is sorted, so we move both pointers inward
            // Hope is that while inward movement we will find a non-duplicate due to rotated sorted nature
            // This is safe because we are not skipping any potential target, we confirmed that nums[mid] != target
            if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
                left++;
                right--;
            }

            // Left half is sorted
            else if (nums[left] <= nums[mid]) {
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

        return false; // Target not found
    }
}

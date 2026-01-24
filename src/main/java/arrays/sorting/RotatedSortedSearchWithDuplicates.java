package arrays.sorting;

/**
 * Leetcode Problem: https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
 *
 * Problem Statement:
 * There is an integer array `nums` sorted in non-decreasing order (not necessarily with distinct values)
 * which is rotated at an unknown pivot. Given the array and a target value, determine if the target exists
 * in the array. The array may contain **duplicates**.
 *
 * Example:
 * Input: nums = [2,5,6,0,0,1,2], target = 0
 * Output: true
 *
 * Follow-up Questions:
 * 1. Can you achieve better than O(n) in worst-case?
 *    → No. When duplicates are present, binary search degenerates to linear search in the worst case.
 * 2. Can you find the index of the target instead of just checking existence?
 *    → Yes, but with O(n) worst-case due to duplicates.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RotatedSortedSearchWithDuplicates {

    public static void main(String[] args) {
        int[] nums = {2, 5, 6, 0, 0, 1, 2};
        int target = 0;
        boolean found = search(nums, target);
        System.out.println("Target exists in array: " + found);
    }

    /**
     * Searches for the target in a rotated sorted array that may contain duplicates.
     *
     * Key insight: Duplicates at boundaries make it impossible to determine which half
     * is sorted. When nums[left] == nums[mid] == nums[right], we must shrink boundaries.
     *
     * Steps:
     * 1. Initialize `left` and `right` pointers.
     * 2. Perform binary search.
     * 3. Handle three cases:
     *    a. If nums[mid] == target → return true.
     *    b. If duplicates exist (nums[left] == nums[mid]) → shrink left by one.
     *    c. Determine which half is sorted, then check if target lies in that half.
     *
     * Time Complexity: O(log n) average, O(n) worst-case (when many duplicates)
     * Space Complexity: O(1)
     *
     * @param nums   Rotated sorted array (may contain duplicates)
     * @param target Integer to find
     * @return True if target exists, false otherwise
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
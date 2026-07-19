package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Search in Rotated Sorted Array
 *
 * A strictly increasing array may be rotated at an unknown pivot. Return the target index, or -1 if absent, by keeping the half that can still contain target.
 *
 * Leetcode: https://leetcode.com/problems/search-in-rotated-sorted-array/ (Medium)
 * Rating:   acceptance 45.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Rotated sorted array | Sorted-half elimination
 *
 * Example:
 *   Input:  nums = [4,5,6,7,0,1,2], target = 0
 *   Output: 4
 *   Why:    0 appears at index 4 after rotation.
 *
 * Follow-ups:
 *   1. Allow duplicates? Shrink equal ambiguous boundaries.
 *   2. Find pivot first? Search for the minimum index, then binary search one half.
 *   3. Return first occurrence? Continue searching after matches.
 *   4. Array on disk? Cache probed blocks around binary-search positions.
 *
 * Related: Find Minimum in Rotated Sorted Array (153), Search in Rotated Sorted Array II (81).
 */
public class SearchInRotatedSortedArray {

    public static void main(String[] args) {
        SearchInRotatedSortedArray solver = new SearchInRotatedSortedArray();
        int[][] inputs = { {4,5,6,7,0,1,2}, {4,5,6,7,0,1,2}, {1} };
        int[] targets = { 0, 3, 0 };
        int[] expected = { 4, -1, -1 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.search(inputs[i], targets[i]);
            System.out.printf("nums=%s target=%d -> %d  expected=%d%n", Arrays.toString(inputs[i]), targets[i], got, expected[i]);
        }
    }

        /**
     * Intuition: At least one side of mid is sorted. A range check on that side tells whether target can be there; if not, search the other side.
     *
     * Algorithm:
     *   1. Return -1 for null or empty input.
     *   2. Binary search with inclusive bounds.
     *   3. Use nums[left] <= nums[mid] to detect the sorted left half.
     *   4. Keep the sorted half only when target lies within its range.
     *
     * Time:  O(log n) - distinct values allow half elimination.
     * Space: O(1) - only indexes are stored.
     *
     * @param nums rotated sorted array
     * @param target value to find
     * @return index of target, or -1
     */
    public int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            }

            // Check if the left half is sorted
            if (nums[left] <= nums[mid]) {
                // Check if target is in the left half
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // Otherwise, the right half must be sorted
            else {
                // Check if target is in the right half
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        return -1; // Target not found
    }

    /**
     * Finds the index of the smallest element (pivot) in the rotated sorted array.
     * This is also the solution to "Find Minimum in Rotated Sorted Array" (LeetCode #153).
     */
    public int findPivot(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > nums[right]) {
                // The pivot is in the right half
                left = mid + 1;
            } else {
                // The pivot is in the left half (including mid)
                right = mid;
            }
        }

        return left;
    }

    /**
     * Alternative approach: First find the pivot, then perform binary search on the appropriate half.
     */
    public int searchWithPivot(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int pivot = findPivot(nums);

        // Determine which half to search
        int left, right;
        if (target >= nums[pivot] && target <= nums[nums.length - 1]) {
            // Search in the right half (from pivot to end)
            left = pivot;
            right = nums.length - 1;
        } else {
            // Search in the left half (from start to pivot-1)
            left = 0;
            right = pivot - 1;
        }

        // Perform standard binary search
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1; // Target not found
    }

    /**
     * Solution for when the array may contain duplicates.
     * This is the solution to "Search in Rotated Sorted Array II" (LeetCode #81).
     */
    public boolean searchWithDuplicates(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return true;
            }

            // Handle duplicates
            if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
                left++;
                right--;
            }
            // Left half is sorted
            else if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // Right half is sorted
            else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        return false;
    }

    /**
     * Finds the first occurrence of the target in a rotated sorted array with duplicates.
     * Returns the index of the first occurrence, or -1 if not found.
     */
    public int findFirstOccurrence(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                result = mid;
                right = mid - 1; // Continue searching in the left half
            }
            // Left half is sorted
            else if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // Right half is sorted
            else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        return result;
    }
}

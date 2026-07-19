package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Search in Rotated Sorted Array II
 *
 * A non-decreasing array with duplicates may be rotated. Return whether target exists, shrinking equal ambiguous boundaries when duplicates hide the sorted half.
 *
 * Leetcode: https://leetcode.com/problems/search-in-rotated-sorted-array-ii/ (Medium)
 * Rating:   acceptance 40.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Rotated sorted array | Duplicate-boundary shrinking
 *
 * Example:
 *   Input:  nums = [2,5,6,0,0,1,2], target = 0
 *   Output: true
 *   Why:    0 appears in the rotated lower segment.
 *
 * Follow-ups:
 *   1. Why can it be O(n)? Equal boundaries may force one-step shrinking.
 *   2. Return an index? Return mid on match or keep searching for a boundary.
 *   3. Count occurrences? Explore both sides after a match; worst case is linear.
 *   4. Find the minimum? Reuse the nums[mid] == nums[right] shrink rule.
 *
 * Related: Search in Rotated Sorted Array (33), Find Minimum in Rotated Sorted Array II (154).
 */
public class SearchInRotatedSortedArrayII {

    public static void main(String[] args) {
        SearchInRotatedSortedArrayII solver = new SearchInRotatedSortedArrayII();
        int[][] inputs = { {2,5,6,0,0,1,2}, {2,5,6,0,0,1,2}, {1,0,1,1,1} };
        int[] targets = { 0, 3, 0 };
        boolean[] expected = { true, false, true };
        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.search(inputs[i], targets[i]);
            System.out.printf("nums=%s target=%d -> %b  expected=%b%n", Arrays.toString(inputs[i]), targets[i], got, expected[i]);
        }
    }

        /**
     * Intuition: Duplicates can hide which side is sorted. When left, mid, and right are equal, shrink both boundaries; otherwise use rotated sorted-half logic.
     *
     * Algorithm:
     *   1. Return false for null or empty input.
     *   2. Binary search with inclusive bounds.
     *   3. Shrink both ends for equal ambiguous boundaries.
     *   4. Otherwise keep the half that can contain target.
     *
     * Time:  O(n) - duplicates can force one-step shrinking; O(log n) on clear splits.
     * Space: O(1) - only indexes are stored.
     *
     * @param nums rotated sorted array with duplicates
     * @param target value to search
     * @return true if target exists
     */
    public boolean search(int[] nums, int target) {
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

            // Handle duplicates at the boundaries
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
     * Alternative approach that first finds the pivot and then performs binary search.
     * This approach is more intuitive but has the same time complexity.
     */
    public boolean searchWithPivot(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int pivot = findPivotWithDuplicates(nums);

        // If we found a pivot, we need to determine which part to search
        if (pivot == 0) {
            return binarySearch(nums, 0, nums.length - 1, target);
        }

        // Check which part of the array to search
        if (target >= nums[0]) {
            return binarySearch(nums, 0, pivot - 1, target);
        } else {
            return binarySearch(nums, pivot, nums.length - 1, target);
        }
    }

        /** Searches target inside inclusive bounds. */
    private boolean binarySearch(int[] nums, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return true;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }

        /** Finds a minimum-value index while shrinking duplicate boundaries. */
    private int findPivotWithDuplicates(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // Handle duplicates
            if (nums[mid] == nums[right]) {
                right--;
            }
            // If mid element is greater than the rightmost element,
            // the pivot is in the right half
            else if (nums[mid] > nums[right]) {
                left = mid + 1;
            }
            // Otherwise, the pivot is in the left half (including mid)
            else {
                right = mid;
            }
        }

        return left;
    }

    /**
     * Counts the number of occurrences of the target in the rotated sorted array.
     * This is a follow-up question that extends the original problem.
     */
    public int countOccurrences(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int first = findFirst(nums, target);
        if (first == -1) {
            return 0; // Target not found
        }

        int last = findLast(nums, target);
        return last - first + 1;
    }

        /** Finds a leftmost target candidate with the restored rotated logic. */
    private int findFirst(int[] nums, int target) {
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

        /** Finds a rightmost target candidate with the restored rotated logic. */
    private int findLast(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                result = mid;
                left = mid + 1; // Continue searching in the right half
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

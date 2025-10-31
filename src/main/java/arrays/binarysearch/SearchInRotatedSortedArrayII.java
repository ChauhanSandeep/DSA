package arrays.binarysearch;

/**
 * There is an integer array nums sorted in non-decreasing order (with duplicate values).
 * The array is rotated at an unknown pivot index k (0 <= k < nums.length) such that the resulting array is
 * [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]].
 *
 * Given the array nums after the rotation and an integer target, return true if target is in nums,
 * or false if it is not in nums.
 *
 * Example 1:
 * Input: nums = [2,5,6,0,0,1,2], target = 0
 * Output: true
 *
 * Example 2:
 * Input: nums = [2,5,6,0,0,1,2], target = 3
 * Output: false
 *
 * LeetCode: https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
 *
 * Follow-up Questions:
 * 1. How would you find the pivot index where the array is rotated?
 *    - We can modify the binary search to find the smallest element's index.
 * 2. What if we need to find the first or last occurrence of the target?
 *    - We would need to continue searching even after finding a match.
 * 3. How would you handle very large arrays that don't fit in memory?
 *    - We could use an external binary search approach that works with file chunks.
 *
 * Related Problems:
 * - Search in Rotated Sorted Array (https://leetcode.com/problems/search-in-rotated-sorted-array/)
 * - Find Minimum in Rotated Sorted Array II (https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/)
 */
public class SearchInRotatedSortedArrayII {
    /**
     * Searches for the target in a rotated sorted array with duplicates.
     *
     * @param nums The rotated sorted array with duplicates
     * @param target The value to search for
     * @return true if target is found, false otherwise
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

    /**
     * Helper method to perform binary search in a given range.
     */
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

    /**
     * Finds the pivot index in a rotated sorted array with duplicates.
     * The pivot is the index of the smallest element.
     */
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

    /**
     * Finds the first occurrence of the target in the rotated sorted array.
     */
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

    /**
     * Finds the last occurrence of the target in the rotated sorted array.
     */
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

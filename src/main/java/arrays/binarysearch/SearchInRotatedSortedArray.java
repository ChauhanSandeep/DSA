package arrays.binarysearch;

/**
 * There is an integer array nums sorted in ascending order (with distinct values).
 * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length)
 * such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed).
 *
 * Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums,
 * or -1 if it is not in nums.
 *
 * Example 1:
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 *
 * Example 2:
 * Input: nums = [4,5,6,7,0,1,2], target = 3
 * Output: -1
 *
 * LeetCode: https://leetcode.com/problems/search-in-rotated-sorted-array/
 *
 * Follow-up Questions:
 * 1. What if the array contains duplicates?
 *    - The solution would need to handle duplicates by checking both halves when nums[left] == nums[mid].
 * 2. How would you find the pivot index where the array is rotated?
 *    - We can modify the binary search to find the smallest element's index.
 * 3. What if we need to find the first or last occurrence of the target?
 *    - We would need to continue searching even after finding a match.
 *
 * Related Problems:
 * - Find Minimum in Rotated Sorted Array (https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/)
 * - Search in Rotated Sorted Array II (https://leetcode.com/problems/search-in-rotated-sorted-array-ii/)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class SearchInRotatedSortedArray {
    /**
     * Searches for the target in a rotated sorted array.
     *
     * @param nums The rotated sorted array
     * @param target The value to search for
     * @return The index of target, or -1 if not found
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

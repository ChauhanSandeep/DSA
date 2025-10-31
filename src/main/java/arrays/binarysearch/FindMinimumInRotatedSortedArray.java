package arrays.binarysearch;

/**
 * Suppose an array of length n sorted in ascending order is rotated between 1 and n times.
 * Given the sorted rotated array nums of unique elements, return the minimum element of this array.
 *
 * Example 1:
 * Input: nums = [3,4,5,1,2]
 * Output: 1
 * Explanation: The original array was [1,2,3,4,5] rotated 3 times.
 *
 * Example 2:
 * Input: nums = [4,5,6,7,0,1,2]
 * Output: 0
 * Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
 *
 * LeetCode: https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
 *
 * Follow-up Questions:
 * 1. How would you handle the case when the array contains duplicates?
 *    - We would need to handle cases where nums[mid] == nums[right].
 * 2. How would you find the number of rotations performed on the original sorted array?
 *    - The number of rotations is equal to the index of the minimum element.
 * 3. What if the array is not rotated?
 *    - The first element would be the minimum in that case.
 *
 * Related Problems:
 * - Search in Rotated Sorted Array (https://leetcode.com/problems/search-in-rotated-sorted-array/)
 * - Find Minimum in Rotated Sorted Array II (https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/)
 */
public class FindMinimumInRotatedSortedArray {
    /**
     * Finds the minimum element in a rotated sorted array using binary search.
     *
     * @param nums The rotated sorted array
     * @return The minimum element in the array
     */
    public int findMin(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array is empty");
        }

        int left = 0;
        int right = nums.length - 1;

        // If the array is not rotated or has only one element
        if (nums[left] <= nums[right]) {
            return nums[left];
        }

        while (left < right) {
            int mid = left + (right - left) / 2;

            // Check if mid+1 is the minimum element
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];
            }

            // Check if mid is the minimum element
            if (mid > 0 && nums[mid - 1] > nums[mid]) {
                return nums[mid];
            }

            // Determine which half to search
            if (nums[mid] > nums[right]) {
                // The minimum is in the right half
                left = mid + 1;
            } else {
                // The minimum is in the left half (including mid)
                right = mid - 1;
            }
        }

        return nums[left];
    }

    /**
     * Alternative approach with cleaner code but same time complexity.
     */
    public int findMinAlternative(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // If the middle element is greater than the rightmost element,
            // the minimum must be in the right half
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            }
            // Otherwise, the minimum is in the left half (including mid)
            else {
                right = mid;
            }
        }

        return nums[left];
    }

    /**
     * Solution for when the array may contain duplicates.
     * This is the solution to "Find Minimum in Rotated Sorted Array II" (LeetCode #154).
     */
    public int findMinWithDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array is empty");
        }

        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > nums[right]) {
                // The minimum is in the right half
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                // The minimum is in the left half (including mid)
                right = mid;
            } else {
                // When nums[mid] == nums[right], we can't determine which half to search,
                // so we just reduce the search space by one
                right--;
            }
        }

        return nums[left];
    }

    /**
     * Finds the number of rotations performed on the original sorted array.
     * This is equivalent to finding the index of the minimum element.
     */
    public int findRotationCount(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;

        // If the array is not rotated
        if (nums[left] <= nums[right]) {
            return 0;
        }

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Check if mid+1 is the minimum element
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return mid + 1;
            }

            // Check if mid is the minimum element
            if (mid > 0 && nums[mid - 1] > nums[mid]) {
                return mid;
            }

            // Determine which half to search
            if (nums[mid] > nums[right]) {
                // The minimum is in the right half
                left = mid + 1;
            } else {
                // The minimum is in the left half (including mid)
                right = mid - 1;
            }
        }

        return 0; // This line is theoretically unreachable for valid inputs
    }

    /**
     * Finds the maximum element in the rotated sorted array.
     * This is similar to finding the minimum but looks for the pivot point.
     */
    public int findMax(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array is empty");
        }

        int left = 0;
        int right = nums.length - 1;

        // If the array is not rotated or has only one element
        if (nums[left] <= nums[right]) {
            return nums[right];
        }

        while (left < right) {
            int mid = left + (right - left) / 2;

            // Check if mid is the maximum element
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return nums[mid];
            }

            // Determine which half to search
            if (nums[mid] > nums[right]) {
                // The maximum is in the right half (including mid)
                left = mid;
            } else {
                // The maximum is in the left half
                right = mid - 1;
            }
        }

        return nums[left];
    }
}

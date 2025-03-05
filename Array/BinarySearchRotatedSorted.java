package Array;

/**
 * Searches for a target value in a rotated sorted array and returns its index.
 * If the target is not found, it returns -1.
 *
 * Approach:
 * - Uses **Binary Search** to efficiently locate the target in O(log N) time.
 * - Identifies which half of the array is sorted and narrows the search accordingly.
 * - Runs in **O(log N) time complexity** due to the binary search approach.
 * - Space complexity is **O(1)** as we use only a few variables.
 *
 * LeetCode Problem: https://leetcode.com/problems/search-in-rotated-sorted-array/
 */
public class BinarySearchRotatedSorted {
    public static void main(String[] args) {
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;
        System.out.println("Index of target: " + search(nums, target));
    }

    /**
     * Searches for a target in a rotated sorted array.
     *
     * @param nums   The rotated sorted input array.
     * @param target The element to find.
     * @return The index of the target or -1 if not found.
     */
    public static int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents integer overflow

            if (nums[mid] == target) {
                return mid;
            }

            // Identify which part of the array is sorted
            if (nums[left] <= nums[mid]) { // Left half is sorted
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1; // Target is in the left half
                } else {
                    left = mid + 1; // Target is in the right half
                }
            } else { // Right half is sorted
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1; // Target is in the right half
                } else {
                    right = mid - 1; // Target is in the left half
                }
            }
        }
        return -1; // Target not found
    }
}

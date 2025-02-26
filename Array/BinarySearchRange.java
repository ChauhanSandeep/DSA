package Array;

import java.util.Arrays;

/**
 * Given a sorted array, this program finds the starting and ending position of a given target element.
 * If the target is not found, it returns [-1, -1].
 *
 * Approach:
 * - Uses **Binary Search** to find the first occurrence of `target`.
 * - Uses **Binary Search** again to find the last occurrence of `target`.
 * - Runs in **O(log N) time complexity** due to the binary search approach.
 * - Space complexity is **O(1)** as we use only a few variables.
 *
 * LeetCode Problem: https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 */
public class BinarySearchRange {
    public static void main(String[] args) {
        int[] nums = {5, 7, 7, 8, 8, 10};
        int target = 8;
        System.out.println("Start and end index for the target: " + Arrays.toString(searchRange(nums, target)));
    }

    /**
     * Finds the first and last position of a given target in a sorted array.
     *
     * @param nums   The sorted input array.
     * @param target The element to find.
     * @return An array with start and end indices of the target, or [-1, -1] if not found.
     */
    public static int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }
        
        int first = findPosition(nums, target, true);
        if (first == -1) {
            return new int[]{-1, -1}; // Early exit if target is not found
        }
        int last = findPosition(nums, target, false);
        return new int[]{first, last};
    }

    /**
     * Performs Binary Search to find either the first or last occurrence of `target`.
     *
     * @param nums       The sorted input array.
     * @param target     The element to find.
     * @param findFirst  If true, finds the first occurrence; otherwise, finds the last occurrence.
     * @return The index of the target or -1 if not found.
     */
    private static int findPosition(int[] nums, int target, boolean findFirst) {
        int left = 0, right = nums.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents integer overflow

            if (nums[mid] == target) {
                result = mid;
                if (findFirst) {
                    right = mid - 1; // Move left to find first occurrence
                } else {
                    left = mid + 1; // Move right to find last occurrence
                }
            } else if (nums[mid] < target) {
                left = mid + 1; // Search in the right half
            } else {
                right = mid - 1; // Search in the left half
            }
        }
        return result;
    }
}

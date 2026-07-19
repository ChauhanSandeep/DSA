package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Find Minimum in Rotated Sorted Array
 *
 * A strictly increasing array was rotated, creating at most one order break. Return the minimum value by locating the lower sorted segment.
 *
 * Leetcode: https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/ (Medium)
 * Rating:   acceptance 55.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Rotated sorted array | Compare with right boundary
 *
 * Example:
 *   Input:  nums = [4,5,6,7,0,1,2]
 *   Output: 0
 *   Why:    the rotation drop occurs before 0, making it the smallest value.
 *
 * Follow-ups:
 *   1. Return the rotation count? Return the minimum index instead of the value.
 *   2. Allow duplicates? Shrink the right boundary when nums[mid] == nums[right].
 *   3. Find the maximum? Look for the value just before the drop.
 *   4. Search for a target? Use sorted-half elimination.
 *
 * Related: Search in Rotated Sorted Array (33), Find Minimum in Rotated Sorted Array II (154).
 */
public class FindMinimumInRotatedSortedArray {

    public static void main(String[] args) {
        FindMinimumInRotatedSortedArray solver = new FindMinimumInRotatedSortedArray();
        int[][] inputs = { {3,4,5,1,2}, {11,13,15,17}, {4,5,6,7,0,1,2} };
        int[] expected = { 1, 11, 0 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.findMin(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: Comparing mid with the right boundary reveals whether the rotation drop is to the right. If not, mid stays a valid minimum candidate.
     *
     * Algorithm:
     *   1. Initialize left and right over the whole array.
     *   2. Compute mid while left < right.
     *   3. Move left to mid + 1 when nums[mid] > nums[right].
     *   4. Otherwise move right to mid and return nums[left] after convergence.
     *
     * Time:  O(log n) - the search range is halved.
     * Space: O(1) - only boundary indexes are used.
     *
     * @param nums rotated sorted array with distinct values
     * @return minimum value
     */
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // Right half is unsorted, minimum must be in [mid+1, right]
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } 
            // Left half including mid is sorted, minimum is in [left, mid]
            else {
                right = mid;
            }
        }
        
        // left == right, pointing to minimum element
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
     * Finds the maximum element in a rotated sorted array.
     * 
     * Key Insight:
     * The maximum element is always at the rotation point - 1, or at the end if not rotated.
     * We use binary search similar to finding minimum, but adjust the logic to find the
     * point where the array drops from a larger value to a smaller value.
     * 
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public int findMax(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        // If array is not rotated, last element is maximum
        if (nums[left] <= nums[right]) {
            return nums[right];
        }
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // If mid element is greater than next element, mid is the maximum
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return nums[mid];
            }
            
            // Left half is sorted and contains larger values
            if (nums[mid] >= nums[left]) {
                left = mid + 1;
            } 
            // Right half is sorted, maximum is in left half
            else {
                right = mid;
            }
        }
        
        return nums[left];
    }
}

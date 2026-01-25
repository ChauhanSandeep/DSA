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
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class FindMinimumInRotatedSortedArray {
    /**
     * Binary Search - Compare with Right Boundary
     * 
     * Algorithm:
     * 1. Use binary search with left and right pointers
     * 2. Compare mid element with right element to determine which half is sorted
     * 3. If nums[mid] > nums[right], minimum is in right half (unsorted part)
     * 4. Otherwise, minimum is in left half (including mid)
     * 5. Converge until left == right
     * 
     * Key Insight:
     * In a rotated sorted array, one half is always sorted. The minimum element
     * lies at the rotation point where the sorted order breaks. By comparing mid
     * with the right boundary, we can determine which half contains the break point.
     * 
     * Why Compare with Right Instead of Left:
     * Comparing with right allows us to identify the unsorted portion reliably.
     * If nums[mid] > nums[right], we know rotation happened in [mid+1, right].
     * If nums[mid] <= nums[right], the right portion is sorted, so min is in [left, mid].
     * 
     * Time Complexity: O(log n) - binary search halves the search space each iteration
     * Space Complexity: O(1) - only uses constant extra space
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

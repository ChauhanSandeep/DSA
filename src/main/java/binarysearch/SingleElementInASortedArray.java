package binarysearch;

/**
 * Problem: Single Element in a Sorted Array
 *
 * You are given a sorted array consisting of only integers where every element appears exactly
 * twice, except for one element which appears exactly once.
 * Return the single element that appears only once.
 * Your solution must run in O(log n) time and O(1) space.
 *
 * Example:
 * Input: nums = [1,1,2,3,3,4,4,8,8]
 * Output: 2
 * Explanation: 2 is the single element that appears only once.
 *
 * LeetCode: https://leetcode.com/problems/single-element-in-a-sorted-array
 *
 * Follow-up Questions:
 * 1. What if every element appears exactly thrice except one that appears once?
 *    Answer: Use bit manipulation with 3-state counting or modify binary search logic.
 *
 * 2. How would you handle the case where multiple elements appear only once?
 *    Answer: XOR all elements to find XOR of all unique elements, then separate using bit manipulation.
 *
 * 3. What if the array is not sorted?
 *    Answer: Use XOR operation to find the single element in O(n) time, O(1) space.
 *    Related: https://leetcode.com/problems/single-number/
 *
 * @author Sandeep
 */
public class SingleElementInASortedArray {

    /**
     * Finds single element using binary search with parity analysis.
     *
     * Algorithm:
     * 1. Use binary search on the sorted array
     * 2. Check if middle element is the single element
     * 3. If not, determine which half contains the single element based on index parity.
     *  - The half containing odd number of elements must contain the single element.
     *  - In the other half, elements are perfectly paired.
     * 4. If mid is even and nums[mid] == nums[mid+1], single element is on right
     * 5. If mid is even and nums[mid] != nums[mid+1], single element is on left
     * 6. Apply opposite logic when mid is odd
     *
     * Time Complexity: O(log n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums Sorted array with one single element
     * @return The element that appears exactly once
     */
    public int singleNonDuplicate(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // Check if mid is the single element
            if ((mid == 0 || nums[mid] != nums[mid - 1]) &&
                (mid == nums.length - 1 || nums[mid] != nums[mid + 1])) {
                return nums[mid];
            }

            // Determine which side has the single element
            int leftSize = mid;
            if (mid > 0 && nums[mid] == nums[mid - 1]) { // Left side has a pair
                leftSize = mid - 1; // Don't count the pair
            }

            if (leftSize % 2 == 1) {
                // Left side has odd number of elements, single element is on left
                right = mid - 1;
            } else {
                // Left side has even number of elements, single element is on right
                left = mid + 1;
            }
        }

        return nums[left];
    }

    /**
     * Clean implementation using XOR property for comparison.
     * Uses bit manipulation to determine search direction.
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public int singleNonDuplicateXOR(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // XOR mid with 1 to get its pair index
            // If mid is even, mid^1 = mid+1
            // If mid is odd, mid^1 = mid-1
            if (nums[mid] == nums[mid ^ 1]) {
                // Current element matches its pair, single element is on the right
                left = mid + 1;
            } else {
                // Current element doesn't match its pair, single element is on the left
                right = mid;
            }
        }

        return nums[left];
    }
}
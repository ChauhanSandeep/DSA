package array;

import java.util.Arrays;

/**
 * Problem: 3Sum Closest
 *
 * Given an integer array nums of length n and an integer target,
 * find three integers in nums such that the sum is closest to target.
 * Return the sum of the three integers. You may assume that each input would have exactly one solution.
 *
 * Leetcode link: https://leetcode.com/problems/3sum-closest/
 *
 * Example:
 * Input: nums = [-1, 2, 1, -4], target = 1
 * Output: 2
 * Explanation: The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
 *
 * Follow-up:
 * - What if you need to find the closest sum of 4 integers (4Sum Closest)?
 *   - Approach: Similar two-pointer technique, with an extra outer loop for the fourth number.
 */
public class ThreeSumClosest {

    /**
     * Main driver for testing.
     */
    public static void main(String[] args) {
        int[] nums = {-1, 2, 1, -4};
        int target = 1;
        System.out.println("Three sum closest to target is " + threeSumClosest(nums, target));
    }

    /**
     * Finds the sum of three integers in the array closest to the target.
     *
     * Steps:
     * 1. Sort the array to use two pointers efficiently.
     * 2. Iterate through each element (i) as the first number.
     * 3. Use two pointers (left, right) to find the closest pair for the fixed number.
     * 4. Track the minimum difference and update result if closer to target.
     *
     * Algorithm:
     * - Sort + Two-pointer technique.
     * - Time Complexity: O(n^2), due to outer loop and inner two-pointer traversal.
     * - Space Complexity: O(1), since no extra data structures are used.
     *
     * @param nums   The input array.
     * @param target The target sum to approach.
     * @return The closest sum of three integers to the target.
     */
    public static int threeSumClosest(int[] nums, int target) {
        // Sort the array to use the two-pointer approach.
        Arrays.sort(nums);

        int len = nums.length;
        int minDiff = Integer.MAX_VALUE; // Track the smallest difference
        int closestSum = 0; // Result to hold the closest sum

        // Iterate through each number as the first element of the triplet
        for (int i = 0; i < len - 2; i++) {
            int left = i + 1;
            int right = len - 1;

            // Two-pointer approach to find the best pair
            while (left < right) {
                int currentSum = nums[i] + nums[left] + nums[right];

                // If exact match found, return immediately
                if (currentSum == target) {
                    return currentSum;
                }

                // Update the closest sum if the current one is closer
                int currentDiff = Math.abs(target - currentSum);
                if (currentDiff < minDiff) {
                    minDiff = currentDiff;
                    closestSum = currentSum;
                }

                // Adjust pointers to try to get closer to target
                if (currentSum < target) {
                    left++; // Increase sum
                } else {
                    right--; // Decrease sum
                }
            }
        }

        return closestSum;
    }
}
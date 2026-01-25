package arrays.twopointers;

import java.util.*;

/**
 * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]]
 * such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
 *
 * Notice that the solution set must not contain duplicate triplets.
 *
 * Example 1:
 * Input: nums = [-1,0,1,2,-1,-4]
 * Output: [[-1,-1,2],[-1,0,1]]
 *
 * Example 2:
 * Input: nums = [0,1,1]
 * Output: []
 *
 * Example 3:
 * Input: nums = [0,0,0]
 * Output: [[0,0,0]]
 *
 * LeetCode: https://leetcode.com/problems/3sum/
 *
 * Follow-up Questions:
 * 1. How would you modify your solution to find triplets that sum to a target other than 0?
 *    - We can generalize the solution to accept a target sum as a parameter.
 * 2. What if we need to find all unique quadruplets that sum to zero?
 *    - We can extend the solution to handle 4Sum by adding an additional loop.
 * 3. How would you optimize the solution for very large input arrays?
 *    - We can use a hash map for O(n²) time complexity with O(n) space.
 *
 * Related Problems:
 * - 3Sum Closest (https://leetcode.com/problems/3sum-closest/)
 * - 4Sum (https://leetcode.com/problems/4sum/)
 * - Two Sum (https://leetcode.com/problems/two-sum/)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ThreeSum {

    /**
     * Finds all unique triplets by fixing the last element.
     *
     * Steps:
     * 1. Return empty list if input is null or has <3 elements.
     * 2. Sort array.
     * 3. For each last element (iterate backwards):
     *    a. Skip duplicates.
     *    b. Use two pointers (start, end before last) to find pairs summing to -lastNum:
     *       - If sum matches, add triplet and skip duplicates.
     *       - Move pointers inward.
     * 4. Return unique triplets.
     *
     * Time Complexity: O(n²) - sorting O(n log n) + nested iteration O(n²)
     * Space Complexity: O(1) or O(n) depending on sorting algorithm (excluding result storage)
     *
     * @param nums The input array of integers
     * @return A list of all unique triplets that sum to zero
     */
    public List<List<Integer>> threeSumFixedLast(int[] nums) {
        List<List<Integer>> triplets = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return triplets;
        }

        // Sort the array to handle duplicates and use two pointers
        Arrays.sort(nums);

        // Iterate from right to left, fixing the last element
        for (int targetIndex = nums.length - 1; targetIndex >= 2; targetIndex--) {
            // Skip duplicate elements for the last position
            if (targetIndex < nums.length - 1 && nums[targetIndex] == nums[targetIndex + 1]) {
                continue;
            }

            int leftPointer = 0;
            int rightPointer = targetIndex - 1;
            int remainingTarget = -nums[targetIndex];

            while (leftPointer < rightPointer) {
                int twoSum = nums[leftPointer] + nums[rightPointer];

                if (twoSum == remainingTarget) {
                    triplets.add(Arrays.asList(nums[leftPointer], nums[rightPointer], nums[targetIndex]));

                    // Skip duplicate elements for left and right pointers
                    while (leftPointer < rightPointer && nums[leftPointer] == nums[leftPointer + 1]) leftPointer++;
                    while (leftPointer < rightPointer && nums[rightPointer] == nums[rightPointer - 1]) rightPointer--;

                    leftPointer++;
                    rightPointer--;
                } else if (twoSum < remainingTarget) {
                    leftPointer++;
                } else {
                    rightPointer--;
                }
            }
        }

        return triplets;
    }

    /**
     * General solution to find all unique triplets that sum to a target value.
     *
     * Steps:
     * 1. Return empty list if input is null or has fewer than 3 elements.
     * 2. Sort the array.
     * 3. For each unique first element:
     *    a. Use two pointers to find pairs that sum to (targetSum - firstNum).
     *    b. Add triplets and skip duplicates.
     * 4. Return all unique triplets.
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(1) or O(n) depending on sorting algorithm
     *
     * @param nums The input array of integers
     * @param targetSum The target sum
     * @return A list of all unique triplets that sum to the target
     */
    public List<List<Integer>> threeSumGeneral(int[] nums, int targetSum) {
        List<List<Integer>> triplets = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return triplets;
        }

        Arrays.sort(nums);

        for (int firstIndex = 0; firstIndex < nums.length - 2; firstIndex++) {
            if (firstIndex > 0 && nums[firstIndex] == nums[firstIndex - 1]) {
                continue;
            }

            int leftPointer = firstIndex + 1;
            int rightPointer = nums.length - 1;
            int remainingSum = targetSum - nums[firstIndex];

            while (leftPointer < rightPointer) {
                int pairSum = nums[leftPointer] + nums[rightPointer];

                if (pairSum == remainingSum) {
                    triplets.add(Arrays.asList(nums[firstIndex], nums[leftPointer], nums[rightPointer]));

                    while (leftPointer < rightPointer && nums[leftPointer] == nums[leftPointer + 1]) leftPointer++;
                    while (leftPointer < rightPointer && nums[rightPointer] == nums[rightPointer - 1]) rightPointer--;

                    leftPointer++;
                    rightPointer--;
                } else if (pairSum < remainingSum) {
                    leftPointer++;
                } else {
                    rightPointer--;
                }
            }
        }

        return triplets;
    }
}


package arrays.twopointers;

import java.util.*;

/**
 * Problem: 3Sum
 *
 * Given an integer array, return all unique triplets whose values sum to zero.
 * This file also keeps a generalized method that accepts any target sum.
 *
 * Leetcode: https://leetcode.com/problems/3sum/ (Medium)
 * Rating:   acceptance 39.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Sorting | Fixed value plus two pointers
 *
 * Example:
 *   Input:  nums = [-1,0,1,2,-1,-4]
 *   Output: [[-1,-1,2], [-1,0,1]]
 *   Why:    these are the only unique triplets whose values add to 0.
 *
 * Follow-ups:
 *   1. Find triplets for an arbitrary target?
 *      Use threeSumGeneral(nums, targetSum), which fixes one value and scans for the remainder.
 *   2. Extend to 4Sum?
 *      Add another fixed loop or use the recursive k-sum pattern.
 *   3. Return indices instead of values?
 *      Track original indices before sorting and add duplicate handling for equal values.
 *
 * Related: Two Sum (1), 3Sum Closest (16), 4Sum (18).
 */
public class ThreeSum {

public static void main(String[] args) {
    ThreeSum solver = new ThreeSum();
    int[][] inputs = { {-1, 0, 1, 2, -1, -4}, {0, 1, 1} };
    String[] expected = { "[[-1, -1, 2], [-1, 0, 1]]", "[]" };

    for (int i = 0; i < inputs.length; i++) {
        List<List<Integer>> got = solver.threeSumGeneral(inputs[i].clone(), 0);
        System.out.printf("nums=%s -> %s  expected=%s%n",
            Arrays.toString(inputs[i]), got, expected[i]);
    }
}

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
     * Space Complexity: O(1) or O(n) depending on sorting algorithm
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
            int requiredSum = -nums[targetIndex];

            while (leftPointer < rightPointer) {
                int twoSum = nums[leftPointer] + nums[rightPointer];

                if (twoSum == requiredSum) {
                    triplets.add(Arrays.asList(nums[leftPointer], nums[rightPointer], nums[targetIndex]));

                    // Skip duplicate elements for left and right pointers
                    while (leftPointer < rightPointer && nums[leftPointer] == nums[leftPointer + 1]) leftPointer++;
                    while (leftPointer < rightPointer && nums[rightPointer] == nums[rightPointer - 1]) rightPointer--;

                    leftPointer++;
                    rightPointer--;
                } else if (twoSum < requiredSum) {
                    leftPointer++;
                } else {
                    rightPointer--;
                }
            }
        }

        return triplets;
    }

    /**
 * Intuition: after sorting, fix one value and the remaining problem is Two
 * Sum II on the suffix. Moving leftPointer right increases the pair sum;
 * moving rightPointer left decreases it. Skipping equal values prevents the
 * same triplet from being emitted more than once.
 *
 * Algorithm:
 *   1. Return an empty list for null or too-short input.
 *   2. Sort nums so duplicates are adjacent and two pointers are valid.
 *   3. For each unique firstIndex, set requiredSum = targetSum - nums[firstIndex].
 *   4. Scan the suffix with leftPointer and rightPointer, adding matches and skipping duplicates.
 *
 * Time:  O(n^2) - each fixed firstIndex runs one linear two-pointer scan.
 * Space: O(1) - excluding the output list and sorting implementation storage.
 *
 * @param nums input array, sorted in place by this method
 * @param targetSum desired triplet sum
 * @return unique triplets whose values sum to targetSum
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
            int requiredSum = targetSum - nums[firstIndex];

            while (leftPointer < rightPointer) {
                int pairSum = nums[leftPointer] + nums[rightPointer];

                if (pairSum == requiredSum) {
                    triplets.add(Arrays.asList(nums[firstIndex], nums[leftPointer], nums[rightPointer]));

                    while (leftPointer < rightPointer && nums[leftPointer] == nums[leftPointer + 1]) leftPointer++;
                    while (leftPointer < rightPointer && nums[rightPointer] == nums[rightPointer - 1]) rightPointer--;

                    leftPointer++;
                    rightPointer--;
                } else if (pairSum < requiredSum) {
                    leftPointer++;
                } else {
                    rightPointer--;
                }
            }
        }

        return triplets;
    }
}


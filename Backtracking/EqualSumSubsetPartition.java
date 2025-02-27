package Backtracking;

import java.util.Arrays;
import java.util.Collections;

/**
 * Problem: Partition an array into k subsets with equal sum.
 *
 * LeetCode: https://leetcode.com/problems/partition-to-k-equal-sum-subsets/
 *
 * Approach:
 * - Calculate the total sum of elements.
 * - If the sum is not divisible by k, return false (impossible to partition equally).
 * - Use backtracking with a boolean array to track used elements.
 * - Sort numbers in descending order (greedy optimization).
 * - Try forming subsets recursively, backtracking when necessary.
 *
 * Complexity Analysis:
 * - Sorting: O(N log N)
 * - Backtracking: O(k * 2^N) in worst case
 * - Overall: Exponential time complexity (NP-Hard problem)
 */
public class EqualSumSubsetPartition {
    public static void main(String[] args) {
        int[] nums1 = {2, 2, 2, 2, 3, 4, 5};
        int k1 = 4;
        System.out.println(canPartitionKSubsets(nums1, k1)); // Output: true

        int[] nums2 = {2, 3, 1, 2, 3, 4, 5};
        int k2 = 4;
        System.out.println(canPartitionKSubsets(nums2, k2)); // Output: false
    }

    public static boolean canPartitionKSubsets(int[] nums, int k) {
        int totalSum = Arrays.stream(nums).sum();

        // If total sum is not divisible by k, partitioning is impossible
        if (totalSum % k != 0) return false;

        int targetSubsetSum = totalSum / k;

        // Sort in descending order to optimize backtracking (greedy approach)
        Arrays.sort(nums);
        reverseArray(nums);

        boolean[] used = new boolean[nums.length];

        // Start backtracking from index 0
        return backtrack(nums, used, 0, 0, k, targetSubsetSum);
    }

    /**
     * Backtracking function to check if k subsets with equal sum can be formed.
     */
    private static boolean backtrack(int[] nums, boolean[] used, int startIdx, int currentSum, int remainingGroups, int targetSubsetSum) {
        // If only one group remains, it's guaranteed to be valid
        if (remainingGroups == 1) return true;

        // If the current subset reaches the target sum, start filling the next subset
        if (currentSum == targetSubsetSum) {
            return backtrack(nums, used, 0, 0, remainingGroups - 1, targetSubsetSum);
        }

        for (int i = startIdx; i < nums.length; i++) {
            if (used[i] || currentSum + nums[i] > targetSubsetSum) continue;

            // Choose
            used[i] = true;

            // Explore
            if (backtrack(nums, used, i + 1, currentSum + nums[i], remainingGroups, targetSubsetSum)) {
                return true;
            }

            // Undo (Backtrack)
            used[i] = false;
        }

        return false; // No valid partition found
    }

    /**
     * Helper function to reverse an array in-place.
     */
    private static void reverseArray(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}
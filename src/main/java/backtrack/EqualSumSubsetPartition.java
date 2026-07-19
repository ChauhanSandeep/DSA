package backtrack;

import java.util.Arrays;

/**
 * Problem: Partition to K Equal Sum Subsets
 *
 * Given an array of positive integers and an integer k, decide whether every
 * number can be assigned to exactly one of k non-empty buckets so all buckets
 * have the same sum.
 *
 * Leetcode: https://leetcode.com/problems/partition-to-k-equal-sum-subsets/
 * Rating:   acceptance 38.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | K-way partition | Fill buckets one at a time
 *
 * Example:
 *   Input:  nums = [4,3,2,3,5,2,1], k = 4
 *   Output: true
 *   Why:    the total is 20, so each of the 4 groups must sum to 5;
 *           [5], [1,4], [2,3], and [2,3] use every number exactly once.
 *
 * Follow-ups:
 *   1. Avoid recomputing equivalent used-element states?
 *      Use bitmask DP where state is the used mask and value is current bucket sum.
 *   2. Count the number of distinct k-way partitions?
 *      Canonicalize bucket order or fill buckets in sorted order to avoid k! duplicates.
 *   3. Handle negative numbers?
 *      Sum pruning no longer works; use memoization over used masks and bucket sums.
 *   4. Return one actual partition, not just true/false?
 *      Store bucket lists during DFS and copy them when the final bucket is reached.
 *
 * Related: Matchsticks to Square (473), Can Partition (416), Fair Distribution of Cookies (2305).
 */
public class EqualSumSubsetPartition {

    /**
     * Intuition: if the total sum is divisible by k, every bucket must have the
     * same target sum. Instead of trying all partitions at once, fill one bucket
     * to target, then freeze it and move to the next bucket. Sorting lets us try
     * large numbers first, so overshoots happen early. Once k - 1 buckets are
     * correctly filled, the remaining unused numbers must form the last bucket
     * because the total sum was already checked.
     *
     * Algorithm:
     *   1. Reject invalid k values and arrays whose total sum is not divisible by k.
     *   2. Sort the numbers and fail early if the largest number is bigger than a bucket target.
     *   3. Use DFS to fill the current bucket from unused numbers, scanning larger values first.
     *   4. When the current bucket reaches target, start a fresh bucket and reduce
     *      the number of groups left to fill.
     *   5. Mark a chosen number as used, recurse with the larger current sum, then
     *      unmark it if that branch cannot finish all buckets.
     *
     * Time:  O(k * 2^n) - each number may be tried in many bucket states, and there are k buckets to fill.
     * Space: O(n) for the used array and recursion stack.
     *
     * @param nums positive integers to partition
     * @param partitions number of equal-sum groups to form
     * @return true if such a partition exists
     */
    public static boolean canPartitionKSubsets(int[] nums, int partitions) {
        if (nums == null || nums.length == 0 || partitions <= 0 || partitions > nums.length) return false;

        int totalSum = 0;
        for (int num : nums) totalSum += num;
        if (totalSum % partitions != 0) return false;

        int target = totalSum / partitions;
        Arrays.sort(nums);
        if (nums[nums.length - 1] > target) return false;

        return backtrack(nums, new boolean[nums.length], nums.length - 1, 0, partitions, target);
    }

    /** Tries to fill equal-sum groups with unused numbers, one group at a time. */
    private static boolean backtrack(int[] nums, boolean[] used, int startIndex,
                                     int currentSum, int remainingGroups, int target) {
        if (remainingGroups == 1) return true;

        if (currentSum == target) {
            return backtrack(nums, used, nums.length - 1, 0, remainingGroups - 1, target);
        }

        for (int i = startIndex; i >= 0; i--) {
            if (used[i] || currentSum + nums[i] > target) continue;

            // select -> mark -> work -> unmark
            used[i] = true;
            if (backtrack(nums, used, i - 1, currentSum + nums[i], remainingGroups, target)) return true;
            used[i] = false;

            // If this value cannot start an empty bucket, equal values cannot either.
            if (currentSum == 0) return false;
        }
        return false;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        int[][] inputs = {
            {4, 3, 2, 3, 5, 2, 1},
            {1},
            {1, 2, 3, 4}
        };
        int[] partitions = {4, 1, 3};
        boolean[] expected = {true, true, false};

        for (int i = 0; i < inputs.length; i++) {
            int[] nums = inputs[i].clone();
            boolean got = canPartitionKSubsets(nums, partitions[i]);
            System.out.printf("nums=%s k=%d  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), partitions[i], got, expected[i]);
        }
    }
}

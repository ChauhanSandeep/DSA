package Array;

import java.util.Arrays;

/**
 * Problem: Number of Subsequences That Satisfy the Given Sum Condition
 *
 * Given an array of integers `nums` and an integer `target`, return the number of
 * non-empty subsequences such that the sum of the minimum and maximum elements
 * is less than or equal to target.
 *
 * 🔗 Leetcode Link: https://leetcode.com/problems/number-of-subsequences-that-satisfy-the-given-sum-condition/
 *
 * Example:
 * Input: nums = [3,5,6,7], target = 9
 * Output: 4
 * Explanation: Subsequences are: [3], [3,5], [3,5,6], [3,6]
 *
 * Follow-up Questions:
 * 1. Can this be solved without sorting? ➤ No, sorting is needed to apply greedy bounds
 * 2. Can we precompute powers of 2 to optimize? ➤ Yes, using modular exponentiation
 * 3. What if target is very large? ➤ No change; sorting and logic stays same
 */
public class SubsequenceSumCondition {

    private static final int MOD = 1_000_000_007;

    /**
     * Counts the number of subsequences where min + max ≤ target.
     *
     * 🧠 Algorithm:
     * - Sort the array to apply two-pointer technique.
     * - For each left pointer, move right inward as long as sum <= target.
     * - If nums[left] + nums[right] ≤ target:
     *    • All elements between left and right are eligible
     *    • Number of such subsequences = 2^(right - left)
     * - Use precomputed power-of-2 table to speed up mod operations.
     *
     * ⏱ Time Complexity: O(N log N) for sorting + O(N) traversal
     * 🧠 Space Complexity: O(N) for power table
     */
    public static int numSubseq(int[] nums, int target) {
        Arrays.sort(nums);
        int len = nums.length;
        int[] pow2 = getPow2(len); // Precompute powers of 2 up to len

        int left = 0;
        int right = len - 1;
        int result = 0;

        while (left <= right) {
            if (nums[left] + nums[right] <= target) {
                // All combinations from left to right can be chosen.
                // Here left must be picked so pow2[right - left] else would have pow2[right - left + 1]
                result = (result + pow2[right - left]) % MOD;
                left++;
            } else {
                right--;
            }
        }

        return result;
    }

    private static int[] getPow2(int len) {
        int[] pow2 = new int[len];

        pow2[0] = 1;
        for (int i = 1; i < len; i++) {
            pow2[i] = (pow2[i - 1] * 2) % MOD;
        }
        return pow2;
    }
}
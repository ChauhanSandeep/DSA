package arrays;

import java.util.Arrays;

/**
 * Problem: Number of Subsequences That Satisfy the Given Sum Condition
 *
 * Count non-empty subsequences where the minimum element plus the maximum element
 * is less than or equal to target. Return the count modulo 1,000,000,007 because
 * the number of subsequences can be very large.
 *
 * Leetcode: https://leetcode.com/problems/number-of-subsequences-that-satisfy-the-given-sum-condition/ (Medium)
 * Rating:   2276 (zerotrac Elo)
 * Pattern:  Array | Sorting | Two pointers with powers of two
 *
 * Example:
 *   Input:  nums = [3,5,6,7], target = 9
 *   Output: 4
 *   Why:    the valid subsequences are [3], [3,5], [3,6], and [3,5,6]; any
 *           subsequence with max 7 has min + max greater than 9.
 *
 * Follow-ups:
 *   1. Return the subsequences themselves?
 *      Backtracking can enumerate them, but the output may be exponential.
 *   2. Support many target queries on the same nums?
 *      Sort once and answer each target with the same two-pointer scan, or precompute more structure if queries are dense.
 *   3. Count subsequences with min + max in a range [low, high]?
 *      Compute count(<= high) - count(< low) using the same helper.
 *
 * Related: Two Sum II (167), Boats to Save People (881), Subsets (78).
 */
public class SubsequenceSumCondition {

    public static void main(String[] args) {
        int[][] inputs = { {3, 5, 6, 7}, {3, 3, 6, 8}, {2, 3, 3, 4, 6, 7} };
        int[] targets = { 9, 10, 12 };
        int[] expected = { 4, 6, 61 };

        for (int i = 0; i < inputs.length; i++) {
            int got = numSubseq(inputs[i].clone(), targets[i]);
            System.out.printf("nums=%s target=%d  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), targets[i], got, expected[i]);
        }
    }

    private static final int MOD = 1_000_000_007;

    /**
     * Intuition: after sorting, nums[left] is the minimum candidate and nums[right] is
     * the maximum candidate. If their sum is within target, then every subset of the
     * values between them can join nums[left] while keeping the same minimum and a
     * maximum no larger than nums[right], producing 2^(right-left) valid subsequences.
     * If the sum is too large, the maximum must move left.
     *
     * Algorithm:
     *   1. Sort nums and precompute powers of two modulo MOD.
     *   2. Keep left at the smallest candidate and right at the largest candidate.
     *   3. If nums[left] + nums[right] <= target, add pow2[right - left] and move left.
     *   4. Otherwise move right leftward to reduce the maximum.
     *
     * Time:  O(n log n) - sorting dominates the two-pointer scan.
     * Space: O(n) - pow2 stores one value per possible span length.
     *
     * @param nums input values, mutated by sorting
     * @param target maximum allowed min + max sum
     * @return number of valid non-empty subsequences modulo MOD
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
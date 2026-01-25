package arrays.slidingwindow;

import java.util.*;

/**
 * 523. Continuous Subarray Sum
 *
 * Problem: Given an integer array nums and integer k, return true if nums has a continuous
 * subarray of size at least two whose elements sum is a multiple of k.
 *
 * Example:
 * Input: nums = [23,2,4,6,7], k = 6
 * Output: true
 * Explanation: [2,4] sums to 6.
 *
 * LeetCode: https://leetcode.com/problems/continuous-subarray-sum
 *
 * Follow-up questions:
 * Q: What if k = 0?
 * A: Check for consecutive zeros since only zero multiples work.
 *
 * Q: Can we optimize using prefix sums?
 * A: Already O(n) with hash map.
 *
 * Q: How to handle negative numbers?
 * A: Modulo operation works with negatives by adjusting remainder.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ContinuousSubarraySum {

    /**
     * Uses prefix sum modulo and hash map to detect subarray with sum%k == 0.
     *
     * Algorithm:
     * - Compute prefix sums mod k
     * - If same mod seen before with distance >=2, return true
     * - Handle k=0 separately
     *
     * Time Complexity: O(n)
     * Space Complexity: O(min(n,k))
     */
    public boolean checkSubarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int sum = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (k != 0) {
                sum %= k;
            }

            if (map.containsKey(sum)) {
                if (i - map.get(sum) > 1) {
                    return true;
                }
            } else {
                map.put(sum, i);
            }
        }

        return false;
    }

    /**
     * Handles k=0 by checking for at least two consecutive zeros.
     */
    public boolean checkSubarraySumZero(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == 0 && nums[i+1] == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Alternative two-pointer approach for positive nums and k>0.
     */
    public boolean checkSubarraySumTwoPointer(int[] nums, int k) {
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            int sum = nums[i];
            for (int j = i + 1; j < n; j++) {
                sum += nums[j];
                if (k != 0 && sum % k == 0) {
                    return true;
                }
                if (k == 0 && sum == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
package arrays.slidingwindow;

import java.util.*;

/**
 * Problem: Continuous Subarray Sum
 *
 * Given nums and k, determine whether some continuous subarray of length at
 * least two has a sum that is a multiple of k. Equal prefix remainders reveal
 * that the subarray between them is divisible by k.
 *
 * Leetcode: https://leetcode.com/problems/continuous-subarray-sum/ (Medium)
 * Rating:   acceptance 31.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Prefix sum | Remainder map | Subarray divisibility
 *
 * Example:
 *   Input:  nums = [23,2,4,6,7], k = 6
 *   Output: true
 *   Why:    subarray [2,4] has sum 6, which is a multiple of 6.
 *
 * Follow-ups:
 *   1. What if k is zero?
 *      Look for a length-at-least-two subarray whose sum is exactly zero.
 *   2. What if nums can be negative?
 *      Normalize remainders so equal classes still match correctly.
 *   3. Return the subarray indices?
 *      Store the first index for each remainder and return when distance is at least two.
 *
 * Related: Subarray Sums Divisible by K (974), Subarray Sum Equals K (560).
 */
public class ContinuousSubarraySum {

    public static void main(String[] args) {
        ContinuousSubarraySum solver = new ContinuousSubarraySum();
        int[][] nums = {{23, 2, 4, 6, 7}, {23, 2, 6, 4, 7}, {1, 2, 3}};
        int[] divisors = {6, 13, 7};
        boolean[] expected = {true, false, false};

        for (int i = 0; i < nums.length; i++) {
            boolean got = solver.checkSubarraySum(nums[i], divisors[i]);
            System.out.printf("nums=%s k=%d -> %s  expected=%s%n",
                Arrays.toString(nums[i]), divisors[i], got, expected[i]);
        }
    }


    /**
     * Intuition: if two prefix sums leave the same remainder modulo k, their
     * difference is divisible by k. Storing the first index for each remainder
     * lets the code enforce the required subarray length of at least two.
     *
     * Algorithm:
     *   1. Seed remainder 0 at index -1 to allow subarrays starting at index 0.
     *   2. Scan nums, updating the running sum and reducing it modulo k when k != 0.
     *   3. Return true when a seen remainder is at least two indices away.
     *
     * Time:  O(n) - one pass over nums.
     * Space: O(min(n, k)) - stores first indexes for remainders encountered.
     *
     * @param nums input array
     * @param k divisor whose multiple the subarray sum must match
     * @return true if a length-at-least-two qualifying subarray exists
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

package arrays;

import java.util.Arrays;

/**
 * 396. Rotate Function
 *
 * Problem Statement:
 * Given an integer array nums of length n, define:
 * F(k) = 0 * arrk[0] + 1 * arrk[1] + ... + (n - 1) * arrk[n - 1]
 * where arrk is nums rotated k positions clockwise.
 *
 * Return the maximum value of F(0), F(1), ..., F(n - 1).
 *
 * LeetCode Link: https://leetcode.com/problems/rotate-function/
 *
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RotateFunction {

    /**
     * Brute-force baseline for understanding and validation.
     * Time Complexity: O(n^2), Space Complexity: O(1)
     */
    public int maxRotateFunctionBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int maxValue = Integer.MIN_VALUE;

        for (int k = 0; k < n; k++) {
            int current = 0;
            for (int i = 0; i < n; i++) {
                int originalIndex = (i - k + n) % n;
                current += i * nums[originalIndex];
            }
            maxValue = Math.max(maxValue, current);
        }

        return maxValue;
    }

    /**
     * Optimized recurrence-based solution.
     * Let nums = [a0, a1, ..., a(n-1)].
     *
     * F(0) = 0*a0 + 1*a1 + 2*a2 + ... + (n-1)*a(n-1)
     * F(1) = 0*a(n-1) + 1*a0 + 2*a1 + ... + (n-1)*a(n-2)
     *
     * Subtract:
     * F(1) - F(0)
     * = (a0 + a1 + ... + a(n-2)) - (n-1)*a(n-1)
     * = (totalSum - a(n-1)) - (n-1)*a(n-1)
     * = totalSum - n*a(n-1)
     *
     * So:
     * F(1) = F(0) + totalSum - n*a(n-1)
     *
     * Generalizing for k >= 1:
     * F(k) = F(k-1) + totalSum - n*nums[n-k]
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int maxRotateFunction(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        long totalSum = 0;
        long f0 = 0;

        for (int i = 0; i < n; i++) {
            totalSum += nums[i];
            f0 += (long) i * nums[i];
        }

        long maxValue = f0;
        long previous = f0;

        for (int k = 1; k < n; k++) {
            previous = previous + totalSum - (long) n * nums[n - k];
            maxValue = Math.max(maxValue, previous);
        }

        return (int) maxValue;
    }

    public static void main(String[] args) {
        RotateFunction solver = new RotateFunction();

        int[] nums1 = { 4, 3, 2, 6 };
        int[] nums2 = { 100 };
        int[] nums3 = { -1, -2, -3, -4, -5 };

        System.out.println("Input: " + Arrays.toString(nums1) + ", Max F(k): " + solver.maxRotateFunction(nums1));
        System.out.println("Input: " + Arrays.toString(nums2) + ", Max F(k): " + solver.maxRotateFunction(nums2));
        System.out.println("Input: " + Arrays.toString(nums3) + ", Max F(k): " + solver.maxRotateFunction(nums3));
    }
}

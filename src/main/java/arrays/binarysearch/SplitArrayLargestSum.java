package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Split Array Largest Sum
 *
 * Split a non-negative array into m non-empty contiguous parts. Return the minimum possible value of the largest part sum.
 *
 * Leetcode: https://leetcode.com/problems/split-array-largest-sum/ (Hard)
 * Rating:   acceptance 60.9% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search on answer | Greedy feasibility | Minimize maximum sum
 *
 * Example:
 *   Input:  nums = [7,2,5,10,8], m = 2
 *   Output: 18
 *   Why:    [7,2,5] and [10,8] has largest sum 18, and no better split exists.
 *
 * Follow-ups:
 *   1. Return split points? Reconstruct greedily after finding the limit.
 *   2. Allow negative numbers? Use DP because monotonic greedy feasibility breaks.
 *   3. Minimize squared sums? Use prefix DP for that objective.
 *   4. Many m queries? Precompute DP for small n or search per query.
 *
 * Related: Capacity To Ship Packages Within D Days (1011), Allocate Books.
 */
public class SplitArrayLargestSum {

    public static void main(String[] args) {
        SplitArrayLargestSum solver = new SplitArrayLargestSum();
        int[][] inputs = { {7,2,5,10,8}, {1,2,3,4,5}, {1,4,4} };
        int[] groups = { 2, 2, 3 };
        int[] expected = { 18, 9, 4 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.splitArray(inputs[i], groups[i]);
            System.out.printf("nums=%s m=%d -> %d  expected=%d%n", Arrays.toString(inputs[i]), groups[i], got, expected[i]);
        }
    }

    /**
     * Intuition: A candidate largest sum is monotonic: if it can split nums into at most m pieces, any larger sum can too. Greedy creates a new piece only when needed.
     *
     * Algorithm:
     *   1. Set left to max(nums) and right to sum(nums).
     *   2. Binary search candidate mid.
     *   3. Greedily test whether max subarray sum mid allows at most m parts.
     *   4. Keep feasible mid values; otherwise raise left.
     *
     * Time:  O(n log S) - each check scans nums across the sum range.
     * Space: O(1) - only counters and bounds are stored.
     *
     * @param nums non-negative integers
     * @param m number of subarrays
     * @return minimized largest subarray sum
     */
    public int splitArray(int[] nums, int m) {
        // The minimum possible largest sum is the maximum element in the array
        // The maximum possible largest sum is the sum of all elements
        int left = 0;
        int right = 0;

        for (int num : nums) {
            left = Math.max(left, num);
            right += num;
        }

        // Binary search to find the minimum possible largest sum
        while (left < right) {
            int mid = left + (right - left) / 2;

            // Check if it's possible to split the array into m subarrays
            // where each subarray has sum <= mid
            if (canSplit(nums, m, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    /** Returns whether nums can split into at most m parts under maxSum. */
    private boolean canSplit(int[] nums, int m, int maxSum) {
        int count = 1;
        int currentSum = 0;

        for (int num : nums) {
            currentSum += num;

            if (currentSum > maxSum) {
                currentSum = num;
                count++;

                if (count > m) {
                    return false;
                }
            }
        }

        return true;
    }
}

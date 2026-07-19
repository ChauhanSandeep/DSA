package arrays;

import java.util.Arrays;

/**
 * Problem: Rotate Function
 *
 * For an array nums of length n, let F(k) be the weighted sum of nums rotated k
 * positions clockwise: sum of index * value after that rotation. Return the
 * maximum value among all rotations.
 *
 * Leetcode: https://leetcode.com/problems/rotate-function/ (Medium)
 * Rating:   acceptance 54.2% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Recurrence | Rotation delta
 *
 * Example:
 *   Input:  nums = [4,3,2,6]
 *   Output: 26
 *   Why:    the rotation [3,2,6,4] gives 0*3 + 1*2 + 2*6 + 3*4 = 26, which is
 *           larger than the other rotation scores.
 *
 * Follow-ups:
 *   1. Return the rotation index that gives the maximum score?
 *      Track k whenever the best value improves.
 *   2. Support many point updates to nums?
 *      Maintain total sum and weighted sum in a Fenwick or segment tree over rotations.
 *   3. Minimize F(k) instead of maximizing it?
 *      Use the same recurrence and track the minimum value.
 *
 * Related: Rotate Array (189), Maximum Sum Circular Subarray (918).
 *
 */
public class RotateFunction {

    public static void main(String[] args) {
        RotateFunction solver = new RotateFunction();

        int[][] inputs = { {4, 3, 2, 6}, {100}, {-1, -2, -3} };
        int[] expected = { 26, 0, -5 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.maxRotateFunction(inputs[i]);
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

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
     * Intuition: recomputing every rotation wastes work because adjacent rotations are
     * related. Moving nums[n - k] from the end to the front adds the total array sum to
     * all shifted values, but that moved value loses n times its contribution. This
     * turns each next F(k) into a constant-time update from the previous value.
     *
     * Algorithm:
     *   1. Compute totalSum and F(0).
     *   2. Initialize maxValue and previous to F(0).
     *   3. For each rotation k from 1 to n - 1, apply previous + totalSum - n * nums[n - k].
     *   4. Track and return the maximum rotation value.
     *
     * Time:  O(n) - one pass computes F(0) and one pass evaluates rotations.
     * Space: O(1) - only running sums and maxima are stored.
     *
     * @param nums input array
     * @return maximum rotate-function value
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


}

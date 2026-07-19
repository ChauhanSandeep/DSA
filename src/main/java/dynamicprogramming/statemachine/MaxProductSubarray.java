package dynamicprogramming.statemachine;

import java.util.Arrays;

/**
 * Problem: Maximum Product Subarray
 *
 * Given an integer array, find the largest product of any non-empty contiguous
 * subarray. Negative numbers matter because multiplying by a negative can turn a
 * very small product into a very large one.
 *
 * Leetcode: https://leetcode.com/problems/maximum-product-subarray/
 * Rating:   acceptance 36.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | State machine | Track max and min ending here
 *
 * Example:
 *   Input:  nums = [2,3,-2,4]
 *   Output: 6
 *   Why:    the subarray [2,3] has product 6; extending through -2 flips the sign and becomes worse.
 *
 * Follow-ups:
 *   1. Can you return the actual subarray boundaries?
 *      Track start indices whenever local max or local min restarts at the current element.
 *   2. How would this extend to a 2D max-product rectangle?
 *      Compress row pairs into column products and run a modified 1D state scan, handling zeros carefully.
 *   3. What if products can overflow int?
 *      Use long, BigInteger, or compare via logarithms if exact products are not required.
 */
public class MaxProductSubarray {

    /**
   * Intuition: a negative number can turn the smallest product so far into the
   * largest product after multiplication. Therefore each index must keep both the
   * maximum and minimum product ending at that index.
   *
   * Algorithm:
   *   1. Initialize maxEndingHere, minEndingHere, and maxProduct from nums[0].
   *   2. Swap max and min states before multiplying by a negative value.
   *   3. Extend or restart the best and worst products at each index.
   *   4. Track the best maxEndingHere seen anywhere.
   *
   * Time:  O(n) - one pass over the array.
   * Space: O(1) - only rolling max/min states are stored.
   *
   * @param nums input values
   * @return maximum product of a contiguous subarray
   */
  public int maxProduct(int[] nums) {
    if (nums == null || nums.length == 0) {
      return 0;
    }

    int globalMax = nums[0];
    int localMax = nums[0];
    int localMin = nums[0];

    for (int i = 1; i < nums.length; i++) {
      int curr = nums[i];

      // Swap localMax and localMin if current number is negative
      // because multiplying by a negative flips the sign
      if (curr < 0) {
        int temp = localMax;
        localMax = localMin;
        localMin = temp;
      }

      // Recompute local max/min with current element
      localMax = Math.max(curr, localMax * curr);
      localMin = Math.min(curr, localMin * curr);

      // Update the global maximum
      globalMax = Math.max(globalMax, localMax);
    }

    return globalMax;
  }


    public static void main(String[] args) {
        MaxProductSubarray solver = new MaxProductSubarray();
        int[][] inputs = { {2, 3, -2, 4}, {-2, 0, -1}, {-1, -2, -3, 0} };
        int[] expected = {6, 0, 6};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.maxProduct(inputs[i]);
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
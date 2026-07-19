package arrays.kadane;

import java.util.Arrays;

/**
 * Problem: Maximum Subarray
 *
 * Given an integer array, find a non-empty contiguous subarray with the largest
 * possible sum and return that subarray. This version returns the elements of the
 * best range, not just the sum.
 *
 * Leetcode: https://leetcode.com/problems/maximum-subarray/
 * Rating:   acceptance 53.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | Kadane | Reset negative prefix
 *
 * Example:
 *   Input:  [-2,1,-3,4,-1,2,1,-5,4]
 *   Output: [4,-1,2,1]
 *   Why:    that contiguous range has sum 6, and no other contiguous range has a
 *           larger sum.
 *
 * Follow-ups:
 *   1. What if you only need the maximum sum?
 *      Return maxSum directly and skip copying the subarray.
 *   2. How would you solve it with divide and conquer?
 *      Combine best prefix, suffix, total, and inner best for each segment.
 *   3. What if multiple subarrays tie for max sum?
 *      Track all ranges with the same best sum or define a tie-breaker.
 *
 * Related: Maximum Product Subarray (152), Maximum Score of Spliced Array (2321).
 */
public class KadaneAlgo {
    public static void main(String[] args) {
        int[][] inputs = {{-2, 1, -3, 4, -1, 2, 1, -5, 4}, {-2, -3}, {5}};
        String[] expected = {"[4, -1, 2, 1]", "[-2]", "[5]"};

        for (int i = 0; i < inputs.length; i++) {
            int[] got = findMaxSubarray(inputs[i]);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), expected[i]);
        }
    }

  /**
     * Intuition: a negative running sum hurts any future subarray more than it
     * helps, so once the current sum drops below zero we should start fresh at the
     * next index. While scanning, we remember the best sum seen so far and the range
     * that produced it. This also handles all-negative arrays because the best value
     * is updated before any negative running sum is reset. The temporary start marks
     * where the current candidate range began.
     *
     * Time:  O(n) - each element is visited once, then the best range is copied once.
     * Space: O(n) - the returned subarray copy can contain every input element.
     *
     * @param nums input array
     * @return contiguous subarray with the maximum sum
     */
  public static int[] findMaxSubarray(int[] nums) {
    if (nums == null || nums.length == 0) {
      return new int[0];
    }

    int maxSum = Integer.MIN_VALUE;
    int currentSum = 0;

    int startIndex = 0;
    int endIndex = 0;
    int tempStart = 0;

    for (int i = 0; i < nums.length; i++) {
      currentSum += nums[i];

      if (currentSum > maxSum) {
        maxSum = currentSum;
        startIndex = tempStart;
        endIndex = i;
      }

      // Reset the current subarray if sum becomes negative
      if (currentSum < 0) {
        currentSum = 0;
        tempStart = i + 1;
      }
    }

    return Arrays.copyOfRange(nums, startIndex, endIndex + 1);
  }
}
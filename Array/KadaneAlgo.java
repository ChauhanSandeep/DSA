package Array;

import java.util.Arrays;


/**
 * Problem: Maximum Subarray (Kadane’s Algorithm)
 *
 * Given an integer array `nums`, find the contiguous subarray (containing at least one number)
 * which has the largest sum and return **the subarray itself**.
 *
 * LeetCode Link:
 * https://leetcode.com/problems/maximum-subarray/
 *
 * Example:
 * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
 * Output: [4,-1,2,1]
 * Explanation: The contiguous subarray [4,-1,2,1] has the largest sum = 6.
 *
 * Follow-up Questions:
 * - Can you return the maximum sum instead of the subarray? (Yes: Just return `maxSum`)
 * - How do you solve this using Divide and Conquer? (See Leetcode Hard variant)
 * - What if you needed to return multiple max subarrays of equal sum?
 *  (Requires additional logic to track all max subarrays)
 */
public class KadaneAlgo {

  public static void main(String[] args) {
    int[] input = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    int[] maxSubarray = findMaxSubarray(input);

    System.out.println("Maximum sum subarray: " + Arrays.toString(maxSubarray));
  }

  /**
   * Kadane’s Algorithm to find the contiguous subarray with the maximum sum.
   * Also returns the subarray itself.
   *
   * Steps:
   * 1. Iterate over the array while maintaining:
   *    - currentSum: sum of current subarray being explored
   *    - maxSum: maximum sum encountered so far
   *    - startIndex, endIndex: track the best subarray
   * 2. If currentSum < 0, reset it and move the temporary start forward.
   * 3. If a new maxSum is found, update start and end pointers.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1) — no extra space except indices
   *
   * @param nums Input array
   * @return Subarray with the maximum sum
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
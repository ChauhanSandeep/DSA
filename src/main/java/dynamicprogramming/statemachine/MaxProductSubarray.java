package dynamicprogramming.statemachine;

/**
 * LeetCode #152: Maximum Product Subarray
 * https://leetcode.com/problems/maximum-product-subarray/
 *
 * Problem:
 * Given an integer array `nums`, find the contiguous subarray
 * (containing at least one number) which has the largest product.
 *
 * Approach:
 * - Use dynamic programming with two running variables:
 *   - `localMax`: maximum product ending at current index.
 *   - `localMin`: minimum product ending at current index.
 * - On a negative number, `localMax` and `localMin` swap.
 * - At each step, update the global maximum.
 *
 * Time Complexity: O(N) — single pass through the array
 * Space Complexity: O(1) — constant space used
 *
 * Example:
 * Input: [2, 3, -2, 4]
 * Output: 6 (subarray: [2, 3])
 * Explanation: The maximum product is obtained from the subarray [2, 3].
 *
 * Input: [-2, 0, -1]
 * Output: 0 (single zero is max)
 * Explanation: The maximum product is 0 from the subarray [0].
 *
 * Follow-Up:
 * - How would you return the actual subarray?
 * - Can this be extended to 2D matrix for max product rectangle?
 */
public class MaxProductSubarray {

  public static void main(String[] args) {
    MaxProductSubarray solver = new MaxProductSubarray();

    System.out.println(solver.maxProduct(new int[]{2, 3, -2, 4}));       // 6
    System.out.println(solver.maxProduct(new int[]{2, -5, 3, 1, -4, 0, -10, 2, 8})); // 80
    System.out.println(solver.maxProduct(new int[]{-1, -2, -3, 0}));     // 6
    System.out.println(solver.maxProduct(new int[]{-2, 0, -1}));         // 0
  }

  /**
   * Computes the maximum product of any contiguous subarray.
   *
   * Steps:
   * 1. Initialize `globalMax`, `localMax`, and `localMin` to the first element.
   * 2. Iterate through the array starting from the second element.
   * 3. For each element:
   *  - If the current element is negative, swap `localMax` and `localMin`.
   *  - Update `localMax` to be the maximum of the current element or the product of `localMax` and the current element.
   *  - Update `localMin` to be the minimum of the current element or the product of `localMin` and the current element.
   *  4. Update `globalMax` to be the maximum of itself and `localMax`.
   *
   * @param nums The input array of integers
   * @return Maximum product of a subarray
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
}
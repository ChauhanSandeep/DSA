package arrays.twopointers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Problem: Find a Subarray with a Given Sum
 *
 * Return the start and end indices of a continuous subarray whose sum equals a
 * target. Positive-only arrays can use a sliding window; arrays with negative
 * values need prefix sums and a hash map.
 *
 * Leetcode: Similar to https://leetcode.com/problems/subarray-sum-equals-k/ (Medium)
 * Pattern:  Array | Sliding window | Prefix sum hash map
 *
 * Example:
 *   Input:  nums = [1,2,3,7,5], targetSum = 12
 *   Output: [1,3]
 *   Why:    nums[1] + nums[2] + nums[3] = 2 + 3 + 7 = 12.
 *
 * Follow-ups:
 *   1. Count all subarrays with the target sum?
 *      Store prefix-sum frequencies instead of first indices.
 *   2. Return all matching ranges?
 *      Store a list of indices for each prefix sum and emit every compatible range.
 *   3. Find the shortest matching subarray?
 *      Keep the latest useful prefix indices and minimize length when a match appears.
 *
 * Related: Subarray Sum Equals K (560), Minimum Size Subarray Sum (209).
 */
public class SumSubarrayFinder {

public static void main(String[] args) {
  int[] positiveInput = {1, 2, 3, 7, 5};
  int[] anyInput = {10, 2, -2, -20, 10};

  int[] positiveGot = findSubarrayWithSumPositive(positiveInput, 12);
  System.out.printf("nums=%s target=12 -> %s  expected=%s%n",
      Arrays.toString(positiveInput), Arrays.toString(positiveGot), Arrays.toString(new int[]{1, 3}));

  int[] anyGot = findSubarrayWithSumAny(anyInput, -10);
  System.out.printf("nums=%s target=-10 -> %s  expected=%s%n",
      Arrays.toString(anyInput), Arrays.toString(anyGot), Arrays.toString(new int[]{0, 3}));
}

  /**
 * Intuition: with all positive values, expanding the right end only increases
 * currentSum, and moving start right only decreases it. That monotonic behavior
 * makes a sliding window sufficient to find a target-sum range.
 *
 * Algorithm:
 *   1. Reject null or empty input.
 *   2. Expand end, adding nums[end] to currentSum.
 *   3. While currentSum is too large, subtract nums[start] and advance start.
 *   4. Return [start, end] when currentSum equals targetSum, else [-1, -1].
 *
 * Time:  O(n) - start and end each move forward at most n times.
 * Space: O(1) - only the window sum and indices are stored.
 *
 * @param nums array of positive integers
 * @param targetSum target subarray sum
 * @return start and end indices, or [-1, -1] if no subarray matches
 */
  public static int[] findSubarrayWithSumPositive(int[] nums, int targetSum) {
    if (nums == null || nums.length == 0) {
      throw new IllegalArgumentException("Input array must not be null or empty.");
    }

    int start = 0;
    int currentSum = 0;

    for (int end = 0; end < nums.length; end++) {
      currentSum += nums[end];

      // Shrink window if current sum exceeds target
      while (currentSum > targetSum && start <= end) {
        currentSum -= nums[start++];
      }

      if (currentSum == targetSum) {
        return new int[]{start, end};
      }
    }

    return new int[]{-1, -1}; // No valid subarray found
  }

  /**
   * Finds a subarray whose sum equals the target, allowing for negative numbers.
   *
   * ✅ Algorithm: Prefix Sum with HashMap
   * ✅ Time Complexity: O(N)
   * ✅ Space Complexity: O(N)
   *
   * @param nums  array with possible positive and negative integers
   * @param targetSum the sum to search for
   * @return an array of two integers representing start and end indices (0-based), or [-1, -1] if not found
   */
  public static int[] findSubarrayWithSumAny(int[] nums, int targetSum) {
    if (nums == null || nums.length == 0) {
      throw new IllegalArgumentException("Input array must not be null or empty.");
    }

    // Maps cumulative prefix sum to its index
    Map<Integer, Integer> prefixSumIndexMap = new HashMap<>(); // Mapping between prefix sum and its first occurrence index
    prefixSumIndexMap.put(0, -1); // Base case to handle subarray starting from index 0

    int currentSum = 0;

    for (int i = 0; i < nums.length; i++) {
      currentSum += nums[i];

      // Check if there's a previous prefix sum such that: currentSum - previousSum = targetSum
      if (prefixSumIndexMap.containsKey(currentSum - targetSum)) {
        return new int[]{prefixSumIndexMap.get(currentSum - targetSum) + 1, i};
      }

      // Store current prefix sum with its index (only store first occurrence)
      prefixSumIndexMap.putIfAbsent(currentSum, i);
    }

    return new int[]{-1, -1}; // No valid subarray found
  }
}
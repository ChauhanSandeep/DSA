package array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Problem: Find a continuous subarray that sums to a given target value.
 *
 * ✅ Given an array of integers and a target sum, return the start and end indices of a
 *    continuous subarray whose elements sum up to the target.
 *
 * ✅ Handles both cases:
 *    1. All elements are positive → use Sliding Window
 *    2. Elements may be positive or negative → use Prefix Sum with HashMap
 *
 * 🔗 LeetCode Link: https://leetcode.com/problems/subarray-sum-equals-k/ (for HashMap version)
 *
 * Example:
 * Input: arr = [1, 2, 3, 7, 5], target = 12
 * Output: [1, 3] (because 2 + 3 + 7 = 12)
 *
 * Follow-up Questions:
 * - Q: What if multiple subarrays sum to the target?
 *   A: You can return the first one or all of them (depending on requirement).
 * - Q: What if you need the subarray elements instead of indices?
 *   A: Extract sublist using indices returned.
 * - Q: How do we count all such subarrays?
 *   A: Use a variation of the prefix sum hashmap with count. (Leetcode: https://leetcode.com/problems/subarray-sum-equals-k/)
 */
public class SumSubarrayFinder {

  public static void main(String[] args) {
    int[] inputArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int target = 12;

    System.out.println(
        "For positive elements only: " + Arrays.toString(findSubarrayWithSumPositive(inputArray, target)));

    System.out.println(
        "For any elements (including negatives): " + Arrays.toString(findSubarrayWithSumAny(inputArray, target)));
  }

  /**
   * Finds a subarray whose sum equals the target assuming all array elements are positive.
   *
   * ✅ Algorithm: Sliding Window (Two Pointer)
   * ✅ Time Complexity: O(N)
   * ✅ Space Complexity: O(1)
   *
   * @param nums  array of positive integers
   * @param targetSum  the sum to search for
   * @return an array of two integers representing start and end indices (0-based), or [-1, -1] if not found
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
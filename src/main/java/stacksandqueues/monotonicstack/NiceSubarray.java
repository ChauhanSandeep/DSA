package stacksandqueues.monotonicstack;

import java.util.*;


/**
 * Problem: Count Number of Nice Subarrays
 *
 * Given nums and k, count subarrays that contain exactly k odd numbers. Even
 * numbers may appear anywhere; they only expand how many starts or ends can
 * surround the same k odd positions.
 *
 * Leetcode: https://leetcode.com/problems/count-number-of-nice-subarrays/ (Medium)
 * Rating:   1624
 * Pattern:  Prefix sum | Sliding window with odd indices | Counting subarrays
 *
 * Example:
 *   Input:  nums = [1,1,2,1,1], k = 3
 *   Output: 2
 *   Why:    only [1,1,2,1] and [1,2,1,1] contain exactly three odd values.
 *
 * Follow-ups:
 *   1. Return the actual subarray ranges?
 *      Store odd positions and enumerate valid start/end choices around each k-odd block.
 *   2. Count subarrays with at most k odds?
 *      Use a standard sliding window and subtract atMost(k) - atMost(k - 1) for exactly k.
 *   3. Generalize from odd count to sum equals target?
 *      Convert each value to a contribution and use prefix-sum frequency counts.
 *   4. Handle an online stream?
 *      Maintain recent odd indices and add new counts as each value arrives.
 *
 * Related: Binary Subarrays With Sum (930), Subarray Sum Equals K (560), Count Vowel Substrings (2062).
 */

public class NiceSubarray {

    public static void main(String[] args) {
    int[][] inputs = { {1, 1, 2, 1, 1}, {2, 4, 6}, {2, 1, 1, 2, 1, 1} };
    int[] ks = { 3, 1, 3 };
    int[] expected = { 2, 0, 3 };

    for (int i = 0; i < inputs.length; i++) {
      int got = countNiceSubarraysUsingDeque(inputs[i], ks[i]);
      System.out.printf("nums=%s k=%d -> %d  expected=%d%n",
          Arrays.toString(inputs[i]), ks[i], got, expected[i]);
    }
  }

    /**
   * Intuition: once we know the previous odd index before a block and the first
   * odd index inside that block, every start between them creates a subarray
   * ending at the current index with exactly k odd numbers. The deque keeps that
   * previous boundary plus the k current odd indices.
   *
   * Algorithm:
   *   1. Start oddIndices with sentinel -1 for even prefixes.
   *   2. Scan nums and append index whenever nums[index] is odd.
   *   3. If the deque has more than k + 1 entries, drop the oldest boundary.
   *   4. When the deque has k + 1 entries, add firstOddIndex - previousOddIndex.
   *
   * Time:  O(n) - each index is processed once and each odd index enters/leaves the deque once.
   * Space: O(k) - the deque stores at most k + 1 odd-boundary indices.
   *
   * @param nums input integer array
   * @param k number of odd values required
   * @return count of subarrays with exactly k odd values
   */

  public static int countNiceSubarraysUsingDeque(int[] nums, int k) {
    LinkedList<Integer> oddIndices = new LinkedList<>();
    oddIndices.offer(-1); // Sentinel to handle even prefix

    int subarrayCount = 0;

    for (int index = 0; index < nums.length; index++) {
      if (nums[index] % 2 != 0) {
        oddIndices.offerLast(index); // Track odd index
      }

      // Maintain window of (k + 1) odd indices
      if (oddIndices.size() > k + 1) {
        oddIndices.pollFirst();
      }

      // If valid window exists, count all subarrays that start after previous odd index and end at current
      if (oddIndices.size() == k + 1) {
        int firstOddIndex = oddIndices.get(1);
        int previousOddIndex = oddIndices.get(0);
        subarrayCount += firstOddIndex - previousOddIndex;
      }
    }

    return subarrayCount;
  }

  /**
   * Optimized approach using prefix sum and hashmap.
   *
   * Steps:
   * - Count number of subarrays where count of odd numbers is exactly k.
   * - Use a map to track how many times a particular prefix sum (count of odds so far) has occurred.
   *
   * Time Complexity: O(N)
   * Space Complexity: O(N)
   *
   * @param nums Input integer array
   * @param k    Number of odd numbers required in a subarray
   * @return Number of valid subarrays with exactly k odd numbers
   */
  public static int countNiceSubarraysPrefixSum(int[] nums, int k) {
    Map<Integer, Integer> prefixCountMap = new HashMap<>();
    prefixCountMap.put(0, 1); // Prefix sum 0 has occurred once

    int oddCount = 0;
    int result = 0;

    for (int num : nums) {
      if (num % 2 != 0) {
        oddCount++; // Count odd numbers
      }

      // If (oddCount - k) prefix has occurred, it forms a valid subarray
      result += prefixCountMap.getOrDefault(oddCount - k, 0);

      // Record this prefix count
      prefixCountMap.put(oddCount, prefixCountMap.getOrDefault(oddCount, 0) + 1);
    }

    return result;
  }
}

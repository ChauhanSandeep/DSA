package stacksandqueues.monotonicstack;

import java.util.LinkedList;


/**
 * Problem: Count Number of Nice Subarrays
 * Leetcode Link: https://leetcode.com/problems/count-number-of-nice-subarrays/
 *
 * Given an array of integers `nums` and an integer `k`, return the number of
 * subarrays that contain exactly `k` odd numbers.
 *
 * Example:
 * Input: nums = [1, 1, 2, 1, 1], k = 3
 * Output: 2
 * Explanation: The two valid subarrays with 3 odd numbers are [1,1,2,1] and [1,2,1,1]
 *
 * Edge Case:
 * Input: nums = [2, 1, 1, 2, 1, 1], k = 3
 * Output: 3 → Valid subarrays: [2,1,1,2,1], [1,1,2,1], [1,2,1,1]
 *
 * Follow-up Questions (FAANG-relevant):
 * 1. Can you solve it with prefix sum and hash map for better generality?
 *    - Yes, use a map to count how many times a certain count of odd numbers has occurred.
 *    - Leetcode: https://leetcode.com/problems/count-number-of-nice-subarrays/
 * 2. Can you extend this to return the actual subarrays (start, end indices)?
 *    - This requires nested loops or pre-storing positions of odd numbers.
 */
public class NiceSubarray {

  public static void main(String[] args) {
    int[] nums = {1, 1, 2, 1, 1};
    int k = 3;
    System.out.println("Number of nice subarrays: " + countNiceSubarraysUsingDeque(nums, k)); // Output: 2
    System.out.println("Number of nice subarrays (PrefixSum): " + countNiceSubarraysPrefixSum(nums, k)); // Output: 2
  }

  /**
   * Counts subarrays with exactly k odd numbers using a deque-based sliding window.
   *
   * Steps:
   * - Track indices of odd numbers using a deque.
   * - Maintain a window that contains exactly (k+1) odd indices (start and end boundary).
   * - For each such window, the number of valid subarrays is determined by the number of even elements before
   *   the first odd number in the window.
   *
   * Time Complexity: O(N), where N is the length of the array.
   * Space Complexity: O(K), for the deque storing up to (k+1) indices.
   *
   * @param nums Input integer array
   * @param k    Number of odd numbers required in a subarray
   * @return Number of valid subarrays with exactly k odd numbers
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
    java.util.Map<Integer, Integer> prefixCountMap = new java.util.HashMap<>();
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

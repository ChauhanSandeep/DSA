package DynamicProgramming.KnapsackRelated;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/discuss/143726/C%2B%2BJavaPython-O(N)-Using-Deque
 *
 *  Problem:
 * Given an integer array `nums` and an integer `k`, find the length of the shortest non-empty subarray
 * whose sum is at least `k`. Return -1 if no such subarray exists.
 *
 * - Subarray = contiguous elements.
 * - Array can contain positive and negative integers.
 *
 * Example:
 * Input: nums = [2,-1,2], k = 3
 * Output: 3
 * Explanation: The subarray [2, -1, 2] has a sum of 3, which is >= k.
 */
public class ShortestSubarraySum {

    public static void main(String[] args) {
        int[] arr = {2, -1, 2, 3};
        int target = 4;
        // result : 2 because Subarray [2, 3] sums to 5.
        System.out.println("Min subarray length is " + new ShortestSubarraySum().shortestSubarray(arr, target));
    }

    /**
     * Simple version when array contains only positive integers.
     *
     * Approach: Basic Sliding Window
     * - Use two pointers to maintain a sliding window.
     * - Expand the window by moving the right pointer.
     * - When the sum of the window is >= targetSum, try to shrink it from the left.
     * - Keep track of the minimum length of valid subarrays.
     *
     * Time: O(N), Space: O(1)
     */
  public int minSubArrayWithOnlyPositives(int targetSum, int[] numbers) {
      int windowStart = 0;
      int arrayLength = numbers.length;
      int minLength = arrayLength + 1; // Set to an impossible high value initially

      for (int windowEnd = 0; windowEnd < arrayLength; windowEnd++) {
          targetSum -= numbers[windowEnd]; // Expand the window by including numbers[windowEnd]

          // Shrink the window as much as possible while the sum of the window is >= targetSum
          while (targetSum <= 0) {
              int currentWindowLength = windowEnd - windowStart + 1;
              minLength = Math.min(minLength, currentWindowLength);

              targetSum += numbers[windowStart]; // Remove numbers[windowStart] from the window
              windowStart++; // Move the start of the window forward
          }
      }

      // If no valid subarray was found, return 0; otherwise, return the minimum length
      return minLength == arrayLength + 1 ? 0 : minLength;
  }

    /**
     * Optimized version using only a Deque of Pair<Index, PrefixSum> when array may contain negative numbers.
     *
     * Steps:
     * 1. **Rolling Prefix Sum**:
     *    - Maintain a running `prefixSum` variable as we iterate through the array.
     *    - No need to build a full prefix array.
     *
     * 2. **Use a Deque of Pair<Index, PrefixSum>**:
     *    - Stores potential candidates for start indices of subarrays.
     *    - Maintains a **monotonic increasing order** of prefix sums.
     *
     * 3. **Deque Operations**:
     *    - **Pop from front** if the current prefixSum - deque.front.prefixSum ≥ k.
     *      → We found a valid subarray → update minimum length.
     *    - **Pop from back** if current prefixSum ≤ deque.back.prefixSum.
     *      → These entries are no longer useful as they represent worse starting points.
     *    - **Push current index and prefixSum to the back**.
     *
     * 4. **Return Result**:
     *    - If no valid subarray found, return -1.
     *    - Otherwise, return the shortest valid length.
     *
     * Why this works:
     * - Monotonic queue helps to quickly eliminate suboptimal start points.
     * - Rolling prefix sum avoids building an extra array.
     * - Efficient in both time and space: O(N)
     *
     * Time complexity: O(N)
     * Space complexity: O(N)
     */
    public int shortestSubarray(int[] nums, int k) {
        int length = nums.length;
        long prefixSum = 0;
        int result = length + 1;

        Deque<Pair> monotonicIncreasingQueue = new ArrayDeque<>();

        // Initial dummy prefix sum at index -1 (i.e., before array starts)
        monotonicIncreasingQueue.offerLast(new Pair(-1, 0L));

        for (int i = 0; i < length; i++) {
            prefixSum += nums[i];

            // Step 1: Check if any prefix in monotonicIncreasingQueue can form a valid subarray
            while (!monotonicIncreasingQueue.isEmpty() && prefixSum - monotonicIncreasingQueue.peekFirst().index >= k) {
                int startIndex = monotonicIncreasingQueue.pollFirst().index;
                result = Math.min(result, i - startIndex);
            }

            // Step 2: Maintain monotonic increasing monotonicIncreasingQueue (by prefix sum)
            while (!monotonicIncreasingQueue.isEmpty() && monotonicIncreasingQueue.peekLast().prefixSum >= prefixSum) {
                monotonicIncreasingQueue.pollLast(); // Remove worse candidate
            }

            // Step 3: Add current index and prefix sum as a candidate
            monotonicIncreasingQueue.offerLast(new Pair(i, prefixSum));
        }

        return result == length + 1 ? -1 : result;
    }

    static class Pair {
        int index;
        long prefixSum;

        Pair(int index, long prefixSum) {
            this.index = index;
            this.prefixSum = prefixSum;
        }
    }
}
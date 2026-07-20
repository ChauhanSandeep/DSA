package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Shortest Subarray With Sum at Least K
 *
 * Given an integer array that may contain negative numbers, return the length of
 * the shortest non-empty contiguous subarray with sum at least k. Return -1 when
 * no such subarray exists.
 *
 * Leetcode: https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/
 * Rating:   2307 (zerotrac Elo)
 * Pattern:  Prefix sums | Monotonic deque | Shortest valid window with negatives
 *
 * Example:
 *   Input:  nums = [2,-1,2], k = 3
 *   Output: 3
 *   Why:    the whole array sums to 3, and no shorter subarray reaches 3.
 *
 * Follow-ups:
 *   1. What if all numbers are positive?
 *      A normal sliding window works because expanding only increases the sum.
 *   2. Can you return the subarray boundaries?
 *      Store the best start and end whenever the deque front forms a valid sum.
 *   3. What if k changes across many queries?
 *      Precompute prefix sums, but answering arbitrary k efficiently needs a different offline or data-structure approach.
 *
 * Related: Minimum Size Subarray Sum (209), Constrained Subsequence Sum (1425).
 */
public class ShortestSubarraySum {

      /**
   * Intuition: with only positive numbers, extending the right end never lowers
   * the window sum and removing from the left never raises future right positions.
   * That monotonicity lets a two-pointer window shrink greedily once it is valid.
   *
   * Algorithm:
   *   1. Expand the window by subtracting numbers[windowEnd] from targetSum.
   *   2. While the adjusted target is non-positive, record the window length.
   *   3. Shrink from windowStart by adding that value back.
   *   4. Return 0 when no valid positive-only window was found.
   *
   * Time:  O(n) - each pointer moves across the array once.
   * Space: O(1) - only window boundaries and the best length are stored.
   *
   * @param targetSum required sum for the window
   * @param numbers positive input values
   * @return shortest positive-only subarray length, or 0 if none exists
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
     * Intuition: negatives break the normal sliding window because extending can
     * lower the sum. Prefix sums restore structure: a subarray is valid when the
     * current prefix minus an earlier prefix is at least k, and smaller earlier
     * prefixes are always better starts.
     *
     * Algorithm:
     *   1. Keep a rolling prefixSum and a deque of candidate starts in increasing prefix order.
     *   2. Pop valid starts from the front and update the shortest length.
     *   3. Pop larger or equal prefix sums from the back because they are worse starts.
     *   4. Push the current index and prefixSum, then return -1 if no length was found.
     *
     * Time:  O(n) - each prefix enters and leaves the deque at most once.
     * Space: O(n) - the deque can store all prefix candidates.
     *
     * @param nums input values, possibly negative
     * @param k required minimum subarray sum
     * @return shortest subarray length with sum at least k, or -1 if none exists
     */
    public int shortestSubarray(int[] nums, int k) {
        int length = nums.length;
        long prefixSum = 0;
        int result = length + 1;

        Deque<Pair> monotonicIncreasingQueue = new ArrayDeque<>(); // store Pair<index, prefixSum> increasing by prefixSum

        // Initial dummy prefix sum at index -1 (i.e., before array starts)
        monotonicIncreasingQueue.offerLast(new Pair(-1, 0L));

        for (int i = 0; i < length; i++) {
            prefixSum += nums[i];

            // Step 1: Check if any prefix in monotonicIncreasingQueue can form a valid subarray
            while (!monotonicIncreasingQueue.isEmpty() && prefixSum - monotonicIncreasingQueue.peekFirst().prefixSum >= k) {
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


    public static void main(String[] args) {
        ShortestSubarraySum solver = new ShortestSubarraySum();
        int[][] inputs = { {3}, {1, 2}, {1, 2}, {5, 1, 1} };
        int[] targets = {3, 5, 4, 5};
        int[] expected = {1, -1, -1, 1};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.shortestSubarray(inputs[i], targets[i]);
            System.out.printf("nums=%s k=%d  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), targets[i], output, expected[i]);
        }
    }

}
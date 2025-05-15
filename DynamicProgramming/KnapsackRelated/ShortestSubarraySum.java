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
  public int minSubArrayLen(int targetSum, int[] numbers) {
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
     * Advanced version when array can contain negative numbers also.
     *
     * Steps:
     * 1. **Use Prefix Sum**:
     *    - Create a `prefixSum` array where each element at index `i` stores the sum of all elements from the start of the array up to index `i-1`.
     *    - This helps in quickly calculating the sum of any subarray using the formula:
     *      `sum of subarray [i, j] = prefixSum[j+1] - prefixSum[i]`.
     *
     * 2. **Use a Deque for Optimization**:
     *    - A double-ended queue is used to store indices of the `prefixSum` array.
     *    - The deque helps maintain a **monotonic increasing order** of prefix sums, which ensures efficient calculation of subarray sums.
     *
     * 3. **Iterate Through the Array**:
     *    - For each index `i` in the `prefixSum` array:
     *      - **Remove Larger Prefix Sums**: If the last element in the deque has a larger prefix sum than the current one, remove it. It’s not useful for finding the shortest subarray.
     *      - **Check for Valid Subarray**: If the difference between the current prefix sum and the smallest prefix sum in the deque is at least `k`, calculate the subarray length and update the result.
     *      - **Add Current Index**: Add the current index to the deque.
     *
     * 4. **Return the Result**:
     *    - If no valid subarray is found, return `-1`.
     *    - Otherwise, return the shortest subarray length.
     *
     * Why this works?
     * - When iterating through the array, the deque stores indices of prefix sums in increasing order.
     *   This allows us to quickly check if the current prefix sum (prefixSum[j+1]) minus the smallest prefix sum in the deque (prefixSum[i]) is at least k.
     * - If the condition is satisfied, the difference between the indices (j - i) gives the length of the subarray.
     *   Since the deque maintains increasing order, the first valid subarray found will be the shortest.
     * - If the current prefix sum (prefixSum[j+1]) is smaller than or equal to the last prefix sum in the deque, the last prefix sum is removed.
     *   This is because a larger prefix sum at the same or earlier index will never help in finding a shorter subarray in the future.
     *
     *
     * Time: O(N), Space: O(N)
     */
    public int shortestSubarray(int[] nums, int target) {
        int len = nums.length;
        long[] prefixSum = new long[len + 1];

        // Build prefix sum array: prefixSum[i] = sum of first (i-1) elements
        for (int i = 0; i < len; i++) {
            prefixSum[i + 1] = prefixSum[i] + (long) nums[i];
        }

        int shortestSubarray = len + 1; // Set to an impossible high value initially
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < len + 1; i++) {

            // Maintain monotonic increasing order in deque
            // If the last element has bigger prefix sum, remove it (useless)
            while (!deque.isEmpty() && prefixSum[deque.getLast()] >= prefixSum[i]) {
                deque.pollLast();
            }

            // Check if current prefix sum - oldest prefix sum >= target
            // If yes, update shortestSubarray and remove the oldest
            while (!deque.isEmpty() && prefixSum[i] - prefixSum[deque.getFirst()] >= target) {
                shortestSubarray = Math.min(shortestSubarray, i - deque.pollFirst());
            }

            // Add current index to deque
            deque.addLast(i);
        }

        return shortestSubarray == len + 1 ? -1 : shortestSubarray;
    }
}
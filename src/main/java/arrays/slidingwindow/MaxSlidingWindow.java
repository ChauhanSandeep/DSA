package arrays.slidingwindow;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;


/**
 * Problem: Sliding Window Maximum
 *
 * Given an integer array `nums` and an integer `k`, find the maximum value in each
 * sliding window of size `k` moving from left to right.
 *
 * Example:
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [3,3,5,5,6,7]
 *
 * LeetCode Link: https://leetcode.com/problems/sliding-window-maximum/
 *
 * Follow-up Questions:
 * - Can you do this in O(n) time? (Deque-based solution)
 * - Can you solve it in parallel using multiple threads? (Divide-and-conquer approach)
 * - How would you adapt this to streams? (Use monotonic queue or Segment Tree)
 */
public class MaxSlidingWindow {

  public static void main(String[] args) {
    int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
    int k = 3;
    int[] maxValues = getMaxInSlidingWindow(nums, k);
    System.out.println("Sliding Window Maximum: " + Arrays.toString(maxValues));
  }

  /**
   * Returns an array representing the maximum value of every contiguous subarray of size `k`.
   *
   * Steps:
   * 1. Use a Deque to store indices of elements in the current window.
   * 2. Remove indices from the front if they are outside the window.
   * 3. Maintain a decreasing order in the deque — remove all elements smaller than the current.
   * 4. The front of the deque always represents the max of the current window.
   *
   * Time Complexity: O(n) — each element is added/removed from deque at most once.
   * Space Complexity: O(k) — deque holds at most `k` indices at a time.
   *
   * @param nums Input integer array
   * @param k    Size of the sliding window
   * @return Array of maximums for each sliding window
   */
  public static int[] getMaxInSlidingWindow(int[] nums, int k) {
    if (nums == null || nums.length == 0 || k <= 0) {
      return new int[0];
    }

    int length = nums.length;
    int[] result = new int[length - k + 1];
    // Stores indices of potential max elements.
    // This will always be in decreasing order of their values because we will remove smaller elements.
    Deque<Integer> indexDeque = new LinkedList<>();

    for (int i = 0; i < length; i++) {
      // Step 1: Remove indices outside the current window (from left side)
      while (!indexDeque.isEmpty() && indexDeque.peekFirst() < i - (k - 1)) {
        indexDeque.pollFirst();
      }

      // Step 2: Remove indices of all elements smaller than current element
      // because they cannot be the maximum in the current or future windows
      while (!indexDeque.isEmpty() && nums[indexDeque.peekLast()] <= nums[i]) {
        indexDeque.pollLast();
      }

      // Step 3: Add current index to the deque
      indexDeque.offerLast(i);

      // Step 4: Capture max once first window is formed
      if (i >= k - 1) {
        result[i - (k - 1)] = nums[indexDeque.peekFirst()];
      }
    }

    return result;
  }
}
package arrays.slidingwindow;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeMap;


/**
 * Problem: Sliding Window Maximum
 *
 * Given nums and window size k, return the maximum value in every contiguous
 * window as it slides one step at a time from left to right.
 *
 * Leetcode: https://leetcode.com/problems/sliding-window-maximum/ (Hard)
 * Rating:   acceptance 47.4% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Sliding window | Monotonic deque
 *
 * Example:
 *   Input:  nums = [1,3,-1,-3,5,3,6,7], k = 3
 *   Output: [3,3,5,5,6,7]
 *   Why:    the deque keeps only useful candidates, so its front is each window max.
 *
 * Follow-ups:
 *   1. Can this support a stream?
 *      Keep the same monotonic deque and evict indices as they leave the window.
 *   2. Can this be parallelized?
 *      Use block prefix/suffix maxima and combine overlapping blocks.
 *   3. What if we need both min and max?
 *      Maintain two monotonic deques, one decreasing and one increasing.
 *
 * Related: Minimum Window Substring (76), Constrained Subsequence Sum (1425).
 */
public class MaxSlidingWindow {

  public static void main(String[] args) {
    int[][] nums = {{1, 3, -1, -3, 5, 3, 6, 7}, {1}, {9, 11}};
    int[] windowSizes = {3, 1, 2};
    int[][] expected = {{3, 3, 5, 5, 6, 7}, {1}, {11}};

    for (int i = 0; i < nums.length; i++) {
      int[] got = getMaxInSlidingWindow(nums[i], windowSizes[i]);
      System.out.printf("nums=%s k=%d -> %s  expected=%s%n",
          Arrays.toString(nums[i]), windowSizes[i], Arrays.toString(got), Arrays.toString(expected[i]));
    }
  }



  /**
   * Intuition: in a window, any smaller value to the left of a newer larger value
   * can never become maximum again before it leaves. The deque stores only useful
   * candidate indices in decreasing value order, so the front is always the max.
   *
   * Algorithm:
   *   1. Remove deque indices that have fallen out of the current window.
   *   2. Remove smaller-or-equal values from the back before adding i.
   *   3. Once the first window is formed, write nums[deque front] to result.
   *
   * Time:  O(n) - each index is inserted and removed at most once.
   * Space: O(k) - the deque holds candidates from the current window.
   *
   * @param nums input array
   * @param k sliding window size
   * @return maximum value for every window
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


  /**
   * Returns an array representing the maximum value of every contiguous subarray of size `k`
   * using a TreeMap-based approach.
   *
   * Steps:
   * 1. Use a TreeMap to store elements and their frequencies in the current window.
   * 2. For each position, add the new element to the window.
   * 3. Once the window reaches size k, record the maximum (last key in TreeMap).
   * 4. Remove the leftmost element from the window and update its frequency.
   * 5. If frequency becomes 0, remove the element from the TreeMap.
   *
   * Time Complexity: O(n log k) — TreeMap operations (put, remove, lastKey) are O(log k).
   * Space Complexity: O(k) — TreeMap holds at most `k` unique elements.
   *
   * @param nums Input integer array
   * @param k    Size of the sliding window
   * @return Array of maximums for each sliding window
   */
  public int[] maxSlidingWindowUsingTreeMap(int[] nums, int k) {
    if (nums == null || nums.length == 0 || k <= 0) {
      return new int[0];
    }

    // TreeMap maintains elements in sorted order: key = element value, value = frequency
    TreeMap<Integer, Integer> numFreqMap = new TreeMap<>();
    int[] result = new int[nums.length - k + 1];
    int resultIndex = 0;

    // Slide the window across the array
    for (int right = 0; right < nums.length; right++) {
      // Step 1: Add the new element entering the window
      numFreqMap.put(nums[right], numFreqMap.getOrDefault(nums[right], 0) + 1);
      
      // Step 2: Once window reaches size k, start recording results
      if (right >= k - 1) {
        // The maximum element is the largest key in TreeMap
        result[resultIndex++] = numFreqMap.lastKey();

        // Step 3: Remove the element leaving the window
        int windowStart = right - k + 1;
        int leftElement = nums[windowStart];
        numFreqMap.put(leftElement, numFreqMap.get(leftElement) - 1);
        
        // Step 4: Clean up if frequency reaches 0
        if (numFreqMap.get(leftElement) == 0) {
          numFreqMap.remove(leftElement);
        }
      }
    }
    
    return result;
  }
}

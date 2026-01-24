package arrays.slidingwindow;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeMap;


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
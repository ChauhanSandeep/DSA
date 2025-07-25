package Heap;

import java.util.Arrays;
import java.util.PriorityQueue;


/**
 * 🔹 Problem 2: Kth Largest Element in a Stream
 * LeetCode Link: https://leetcode.com/problems/kth-largest-element-in-a-stream/
 *
 * Design a class to find the kᵗʰ largest element in a stream. You will be given an integer k and a stream of integers.
 * Implement a method `int add(int val)` that returns the element representing the kᵗʰ largest in the stream so far.
 * 🔹 Example:
 * Input: k = 3, stream = [4, 5, 8, 2]
 * Output: [null, null, 4, 5]
 */

public class KthlargestStreamFinder {
  /**
   * 🔸 Method: Streaming Kth Largest
   * Returns an array where each index i contains the kth largest element after processing inputArray[0...i].
   * Returns -1 for indices where fewer than k elements have been seen.
   *
   * ✅ Time Complexity per element: O(log k)
   * ✅ Overall Time Complexity: O(n log k)
   * ✅ Space Complexity: O(k)
   *
   * @param inputArray The stream of integers.
   * @param k The 'kth' largest to track.
   * @return Array of kth largest values at each point in the stream.
   */
  public static int[] findKthLargestInStream(int[] inputArray, int k) {
    if (!isValidInput(inputArray, k)) return new int[]{};

    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    int[] result = new int[inputArray.length]; // stores kth largest at each index
    Arrays.fill(result, -1); // Default value if fewer than k elements processed

    for (int i = 0; i < inputArray.length; i++) {
      minHeap.offer(inputArray[i]);
      if (minHeap.size() > k) {
        minHeap.poll();
      }
      if (minHeap.size() == k) {
        result[i] = minHeap.peek();
      }
    }
    return result;
  }

  /**
   * Validates input for kth largest problems.
   */
  private static boolean isValidInput(int[] arr, int k) {
    return arr != null && arr.length > 0 && k > 0 && k <= arr.length;
  }
}

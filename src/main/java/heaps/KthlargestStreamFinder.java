package heaps;

import java.util.Arrays;
import java.util.PriorityQueue;


/**
 * Problem: Kth Largest Element in a Stream
 *
 * Given a stream of integers and a fixed k, report the kth largest value after
 * each new element is processed. Until at least k values have been seen, this
 * implementation reports -1 for that position.
 *
 * Leetcode: https://leetcode.com/problems/kth-largest-element-in-a-stream/ (Easy)
 * Rating:   acceptance 61.3% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | Streaming top k | Size-k min heap
 *
 * Example:
 *   Input:  inputArray = [4,5,8,2], k = 3
 *   Output: [-1,-1,4,4]
 *   Why:    after the third and fourth values, the 3rd largest among seen values is 4.
 *
 * Follow-ups:
 *   1. What if k changes during the stream?
 *      Use two heaps or an order-statistics tree instead of one fixed-size heap.
 *   2. What if the stream is too large to replay?
 *      Keep only the size-k heap; it is the complete state needed for this query.
 *   3. How do you support deletions from a sliding window?
 *      Add lazy deletion maps or use a balanced tree keyed by value and count.
 *   4. How do you merge streams from many machines?
 *      Merge each machine's top-k candidates through another size-k heap.
 *
 * Related: Kth Largest Element in an Array (215), Last Stone Weight (1046).
 */


public class KthlargestStreamFinder {

  public static void main(String[] args) {
    int[][] inputs = { {4, 5, 8, 2}, {7} };
    int[] kValues = {3, 1};
    int[][] expected = { {-1, -1, 4, 4}, {7} };

    for (int i = 0; i < inputs.length; i++) {
      int[] got = findKthLargestInStream(inputs[i], kValues[i]);
      System.out.printf("stream=%s k=%d -> %s  expected=%s%n",
          Arrays.toString(inputs[i]), kValues[i],
          Arrays.toString(got), Arrays.toString(expected[i]));
    }
  }
    /**
   * Intuition: after each streamed value, the kth largest is the smallest value
   * among the current top k. A min heap stores exactly those k candidates, so its
   * root becomes the answer whenever the heap has reached size k.
   *
   * Algorithm:
   *   1. Return an empty array when the input or k is invalid.
   *   2. Fill the result with -1 for positions before k values are available.
   *   3. Offer each stream value and poll when the heap grows beyond k.
   *   4. Once the heap size is k, write the heap root into the result.
   *
   * Time:  O(n log k) - each stream value updates a heap of at most k elements.
   * Space: O(k) - the heap stores only the top k values, excluding the output array.
   *
   * @param inputArray stream values in arrival order
   * @param k rank to maintain among largest values
   * @return kth largest value after each arrival, or -1 until k values exist
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

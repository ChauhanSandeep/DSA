package heaps;

import java.util.PriorityQueue;
import java.util.Arrays;


/**
 * Problem: Kth Largest Element in an Array
 *
 * Given an integer array nums and an integer k, return the kth largest value in
 * sorted order. Duplicates count as separate elements, so this is not asking for
 * the kth distinct value.
 *
 * Leetcode: https://leetcode.com/problems/kth-largest-element-in-an-array/ (Medium)
 * Rating:   acceptance 69.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | Size-k min heap | QuickSelect alternative
 *
 * Example:
 *   Input:  nums = [3,2,1,5,6,4], k = 2
 *   Output: 5
 *   Why:    sorted descending is [6,5,4,3,2,1], so the 2nd largest value is 5.
 *
 * Follow-ups:
 *   1. Can you get average O(n) time?
 *      Use QuickSelect and target index nums.length - k in ascending order.
 *   2. How do you solve this for an infinite stream?
 *      Maintain a size-k min heap and report its root after each update.
 *   3. What if k changes across many queries?
 *      Sort once, or maintain an order-statistics tree for dynamic data.
 *   4. What if values are distributed across machines?
 *      Keep local top-k heaps and merge those candidates with one final heap.
 *
 * Related: Kth Largest Element in a Stream (703), Top K Frequent Elements (347).
 */

public class KthLargestFinder {

  public static void main(String[] args) {
    int[][] inputs = { {3, 2, 1, 5, 6, 4}, {1} };
    int[] kValues = {2, 1};
    int[] expected = {5, 1};

    for (int i = 0; i < inputs.length; i++) {
      int heapGot = findKthLargestUsingMinHeap(inputs[i].clone(), kValues[i]);
      int quickGot = findKthLargestUsingQuickSelect(inputs[i].clone(), kValues[i]);
      System.out.printf("minHeap nums=%s k=%d -> %d  expected=%d%n",
          Arrays.toString(inputs[i]), kValues[i], heapGot, expected[i]);
      System.out.printf("quickSelect nums=%s k=%d -> %d  expected=%d%n",
          Arrays.toString(inputs[i]), kValues[i], quickGot, expected[i]);
    }
  }

    /**
   * Intuition: the kth largest is the smallest value among the best k values seen
   * so far. A min heap of size k keeps exactly those candidates, and its root is
   * always the current answer after all smaller extras have been evicted.
   *
   * Algorithm:
   *   1. Validate input and return -1 for invalid requests.
   *   2. Offer each number into a min heap.
   *   3. If the heap grows beyond k, poll the smallest value.
   *   4. Return the heap root after all numbers are processed.
   *
   * Time:  O(n log k) - each number may be inserted and one value may be polled.
   * Space: O(k) - the heap keeps only k largest candidates.
   *
   * @param inputArray array of integers
   * @param k rank to find among largest values
   * @return kth largest value, or -1 for invalid input
   */

  public static int findKthLargestUsingMinHeap(int[] inputArray, int k) {
    if (!isValidInput(inputArray, k)) {
      return -1;
    }

    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    for (int num : inputArray) {
      minHeap.offer(num);
      if (minHeap.size() > k) {
        minHeap.poll(); // Remove smallest to keep only k largest
      }
    }
    return minHeap.peek(); // Root is the kth largest
  }

  /**
   * 🔸 Optimized Method: QuickSelect (Average O(n) Time)
   *
   * Appraoch:
   * 1. Choose a pivot (commonly the last element).
   * 2. Partition the array such that elements less than the pivot are on the left and greater on the right.
   * 3. Recursively apply QuickSelect to the left or right partition based on the pivot's index.
   *
   * ✅ Time Complexity: O(n) average, O(n²) worst case
   * ✅ Space Complexity: O(1) auxiliary
   *
   * QuickSelect partitions the array and recursively finds the kth largest element without sorting the whole array.
   *
   * @param nums The input array.
   * @param k The kth largest to find.
   * @return The kth largest element.
   */
  public static int findKthLargestUsingQuickSelect(int[] nums, int k) {
    if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
      return -1;
    }

    int targetIndex = nums.length - k; // kth largest elemente would be at this index in sorted order
    return quickSelect(nums, 0, nums.length - 1, targetIndex);
  }

  /** Runs iterative QuickSelect until targetIndex is placed correctly. */
  private static int quickSelect(int[] nums, int left, int right, int targetIndex) {
    while (left <= right) {
      int pivotIndex = partition(nums, left, right);  // Always chooses right as pivot

      if (pivotIndex == targetIndex) {
        return nums[pivotIndex];
      } else if (pivotIndex > targetIndex) {
        // target index would be in the left partition
        right = pivotIndex - 1;
      } else {
        // target index would be in the right partition
        left = pivotIndex + 1;
      }
    }
    return -1; // Should never hit this line if input is valid
  }

  /** Partitions nums with the last element as the pivot. */
  private static int partition(int[] nums, int left, int right) {
    int pivot = nums[right];
    int storeIndex = left;

    // if element is less than pivot, swap it to the left partition
    // if element is greater than pivot, it stays in the right partition
    for (int i = left; i < right; i++) {
      if (nums[i] < pivot) {
        swap(nums, i, storeIndex++);
      }
    }
    // Place the pivot in its correct position
    swap(nums, storeIndex, right);
    return storeIndex;
  }

  private static void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
  }

  private static boolean isValidInput(int[] inputArray, int k) {
    if (inputArray == null || inputArray.length == 0) {
      System.out.println("Error: Input array cannot be null or empty");
      return false;
    }
    if (k <= 0 || k > inputArray.length) {
      System.out.println("Error: k must be between 1 and the length of the array");
      return false;
    }
    return true;
  }
}
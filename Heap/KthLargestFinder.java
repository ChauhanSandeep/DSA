package Heap;

import java.util.PriorityQueue;
import java.util.Random;


/**
 * 🔹 Problem 1: Kth Largest Element in an Array
 * LeetCode Link: https://leetcode.com/problems/kth-largest-element-in-an-array/
 *
 * Given an integer array `nums` and an integer `k`, return the kᵗʰ largest element in the array.
 * Note that it is the kᵗʰ largest element in **sorted order**, not the kᵗʰ distinct element.
 *
 * 🔹 Example:
 * Input: nums = [3,2,1,5,6,4], k = 2
 * Output: 5
 *
 * 🔹 Follow-ups:
 * - Can you do this with average O(n) time instead of O(n log k)? → Use QuickSelect [Leetcode #215]
 * - What if numbers are huge or streamed in chunks? → Use Min-Heap [Leetcode #703]
 */
public class KthLargestFinder {

  public static void main(String[] args) {
    int k = 3;
    int[] inputArray = {3, 2, 3, 1, 2, 4, 5, 5, 6};

    System.out.println("Kth Largest Using MinHeap: " + findKthLargestUsingMinHeap(inputArray, k));
    System.out.println("Kth Largest Using QuickSelect: " + findKthLargestUsingQuickSelect(inputArray.clone(), k));
  }

  /**
   * 🔸 Method: MinHeap
   * Uses a min-heap of size k to maintain the top-k largest elements seen so far.
   * The root of the min-heap will be the kth largest element.
   *
   * ✅ Time Complexity: O(n log k)
   * ✅ Space Complexity: O(k)
   *
   * @param inputArray The array of integers.
   * @param k The 'kth' largest element to find.
   * @return The kth largest element.
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

  // QuickSelect to find element at the target index (0-based)
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

  // Lomuto partition scheme with last element as pivot
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
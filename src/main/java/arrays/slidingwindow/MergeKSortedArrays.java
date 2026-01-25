package arrays.slidingwindow;

import java.util.*;


/**
 * Problem: Merge K Sorted Arrays.
 * Given K sorted arrays of integers, merge them into a single sorted array.
 *
 * Leetcode Link: https://leetcode.com/problems/merge-k-sorted-lists/ (conceptually similar)
 *
 * Example:
 * Input:
 *   arr1 = [1, 3, 5, 7]
 *   arr2 = [2, 4, 6, 8]
 *   arr3 = [0, 9, 10, 11]
 * Output:
 *   [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
 *
 * Follow-up Questions:
 * 1. Can you do it without a heap? → Yes, by using divide and conquer (merge 2 arrays at a time)
 *    🔗 https://leetcode.com/problems/merge-k-sorted-lists/discuss/10527/A-java-solution-based-on-divide-and-conquer
 * 2. Can you merge arrays in-place? → Only possible if you control the original arrays.
 * 3. What if arrays are streaming in real-time? → Use a sliding window and a heap for real-time merging.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MergeKSortedArrays {

  public static void main(String[] args) {
    int[] arr1 = {1, 3, 5, 7};
    int[] arr2 = {2, 4, 6, 8};
    int[] arr3 = {0, 9, 10, 11};

    int[] merged = mergeKSortedArrays(Arrays.asList(arr1, arr2, arr3));
    System.out.println("Merged Array: " + Arrays.toString(merged));
  }

  /**
   * Merges K sorted arrays into one sorted array using a Min Heap.
   *
   * ✅ Steps:
   * 1. Add the first element of each array to the min heap.
   * 2. Pop the smallest element from the heap and add it to the result.
   * 3. If the array of the popped element has more elements, push the next element into the heap.
   * 4. Repeat until the heap is empty.
   *
   * 🧠 Algorithm: Min Heap (Priority Queue)
   * ⏱ Time Complexity: O(N log K), where N is total number of elements and K is number of arrays.
   * 🧠 Space Complexity: O(K) for the heap.
   *
   * @param arrays List of sorted integer arrays to be merged.
   * @return A single sorted array containing all elements.
   */
  public static int[] mergeKSortedArrays(List<int[]> arrays) {
    if (arrays == null || arrays.isEmpty()) {
      return new int[0];  // Edge case: empty input
    }

    PriorityQueue<ArrayEntry> minHeap = new PriorityQueue<>((a, b) -> {
      int[] arrA = arrays.get(a.arrayIndex);
      int[] arrB = arrays.get(b.arrayIndex);
      return Integer.compare(arrA[a.pointerIndex], arrB[b.pointerIndex]);
    });
    int totalElements = 0;

    // Add the first element of each non-empty array to the heap
    for (int i = 0; i < arrays.size(); i++) {
      int[] array = arrays.get(i);
      if (array != null && array.length > 0) {
        minHeap.offer(new ArrayEntry(i, 0));
        totalElements += array.length;
      }
    }

    int[] result = new int[totalElements];
    int resultIndex = 0;

    while (!minHeap.isEmpty()) {
      ArrayEntry entry = minHeap.poll();
      int[] currentArray = arrays.get(entry.arrayIndex);
      result[resultIndex++] = currentArray[entry.pointerIndex];

      // Push the next element from the same array (if any)
      if (entry.pointerIndex + 1 < currentArray.length) {
        minHeap.offer(new ArrayEntry(entry.arrayIndex, entry.pointerIndex + 1));
      }
    }

    return result;
  }

  /**
   * Represents an entry in the heap that keeps track of:
   * - The source array
   * - The current index within that array
   */
  /**
   * Represents an entry in the heap that keeps track of:
   * - arrayIndex: Index of the array in the original list
   * - pointerIndex: Current position within that array
   */
  private static class ArrayEntry {
    int arrayIndex;    // Index of the array in the original list
    int pointerIndex;  // Current position within the array

    public ArrayEntry(int arrayIndex, int pointerIndex) {
      this.arrayIndex = arrayIndex;
      this.pointerIndex = pointerIndex;
    }
  }
}
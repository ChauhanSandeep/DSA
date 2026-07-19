package arrays.slidingwindow;

import java.util.*;


/**
 * Problem: Merge K Sorted Arrays
 *
 * Given k individually sorted integer arrays, merge all values into one sorted
 * array. This is the array version of the classic merge k sorted lists problem.
 *
 * Leetcode: https://leetcode.com/problems/merge-k-sorted-lists/ (Hard, analogous)
 * Rating:   no direct array problem rating
 * Pattern:  Heap | K-way merge | Priority queue
 *
 * Example:
 *   Input:  [[1,3,5,7],[2,4,6,8],[0,9,10,11]]
 *   Output: [0,1,2,3,4,5,6,7,8,9,10,11]
 *   Why:    the heap always exposes the smallest current head among the k arrays.
 *
 * Follow-ups:
 *   1. Can this be done without a heap?
 *      Merge arrays pairwise with divide and conquer.
 *   2. What if arrays are streams?
 *      Keep one current value per stream in the heap and pull lazily.
 *   3. What if the result must be written in place?
 *      Only possible with spare capacity or by merging from the end for two arrays at a time.
 *
 * Related: Merge k Sorted Lists (23), Merge Sorted Array (88).
 */
public class MergeKSortedArrays {

  public static void main(String[] args) {
    List<List<int[]>> inputs = Arrays.asList(
        Arrays.asList(new int[]{1, 3, 5, 7}, new int[]{2, 4, 6, 8}, new int[]{0, 9, 10, 11}),
        Arrays.asList(new int[]{}, new int[]{1}),
        Collections.emptyList()
    );
    int[][] expected = {
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
        {1},
        {}
    };

    for (int i = 0; i < inputs.size(); i++) {
      int[] got = mergeKSortedArrays(inputs.get(i));
      System.out.printf("arrays=%s -> %s  expected=%s%n",
          Arrays.deepToString(inputs.get(i).toArray(new int[0][])),
          Arrays.toString(got), Arrays.toString(expected[i]));
    }
  }

  /**
   * Intuition: each sorted array contributes only its current smallest unused
   * value. A min heap over those current heads repeatedly reveals the next global
   * value, then advances only the array that supplied it.
   *
   * Algorithm:
   *   1. Push the first element of every non-empty array into the min heap.
   *   2. Pop the smallest heap entry, append its value to result, and advance that array.
   *   3. Push the next value from the same array until the heap is empty.
   *
   * Time:  O(N log k) - N total elements, with heap size at most k.
   * Space: O(k) - the heap stores one entry per non-empty array.
   *
   * @param arrays list of sorted integer arrays
   * @return one sorted array containing all values
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

package arrays;

import java.util.Arrays;

/**
 * Problem: Merge Two Sorted Arrays Without Extra Space
 *
 * Given two sorted arrays, rearrange their values so the first array contains the
 * smaller prefix of the combined sorted order and the second array contains the
 * remaining larger suffix. The in-place version must not allocate a third array.
 *
 * Source: https://www.geeksforgeeks.org/merge-two-sorted-arrays-o1-extra-space/
 * Pattern:  Array | Two pointers | Gap method
 *
 * Example:
 *   Input:  first = [1,3,5,7], second = [0,2,6,8,9]
 *   Output: first = [0,1,2,3], second = [5,6,7,8,9]
 *   Why:    the combined sorted order is [0,1,2,3,5,6,7,8,9], split back across
 *           arrays of lengths 4 and 5.
 *
 * Follow-ups:
 *   1. Merge when the first array has enough trailing buffer, like Leetcode 88?
 *      Fill from the back with three pointers so no gap pass is needed.
 *   2. Return one merged array instead of modifying two arrays?
 *      Use the standard two-pointer merge with O(n + m) output space.
 *   3. Merge k sorted arrays in place?
 *      Usually use a min-heap or divide-and-conquer; true in-place merging is much harder.
 *
 * Related: Merge Sorted Array (88), Merge k Sorted Lists (23).
 *
 */
public class Merge2Arrays {

    public static void main(String[] args) {
      Merge2Arrays solver = new Merge2Arrays();

      int[] firstInPlace = {1, 3, 5, 7};
      int[] secondInPlace = {0, 2, 6, 8, 9};
      solver.mergeArraysSwapAndSort(firstInPlace, secondInPlace);
      System.out.printf("first=[1, 3, 5, 7] second=[0, 2, 6, 8, 9]  ->  first=%s second=%s  expected=[0, 1, 2, 3]/[5, 6, 7, 8, 9]%n",
          Arrays.toString(firstInPlace), Arrays.toString(secondInPlace));

      int[] merged = mergeWithExtraSpace(new int[] {1, 3, 5}, new int[] {2, 4});
      System.out.printf("extra-space input=[1, 3, 5]/[2, 4]  ->  %s  expected=[1, 2, 3, 4, 5]%n",
          Arrays.toString(merged));

      long[] firstGapEdge = {10};
      long[] secondGapEdge = {};
      mergeWithoutExtraSpace(firstGapEdge, secondGapEdge);
      System.out.printf("gap-edge first=[10] second=[]  ->  first=%s second=%s  expected=[10]/[]%n",
          Arrays.toString(firstGapEdge), Arrays.toString(secondGapEdge));

      long[] firstGap = {1, 5, 9, 10, 15, 20};
      long[] secondGap = {2, 3, 8, 13};
      mergeWithoutExtraSpace(firstGap, secondGap);
      System.out.printf("gap first=[1, 5, 9, 10, 15, 20] second=[2, 3, 8, 13]  ->  first=%s second=%s  expected=[1, 2, 3, 5, 8, 9]/[10, 13, 15, 20]%n",
          Arrays.toString(firstGap), Arrays.toString(secondGap));
    }



  /**
   * 🔹 Extra-space merge of two sorted arrays.
   *
   * Time Complexity: O(N + M)
   * Space Complexity: O(N + M)
   */
  public static int[] mergeWithExtraSpace(int[] arr1, int[] arr2) {
    int len1 = arr1.length, len2 = arr2.length;
    int[] result = new int[len1 + len2];
    int i = 0, j = 0, k = 0;

    // Standard merge two-pointer technique
    while (i < len1 && j < len2) {
      result[k++] = arr1[i] <= arr2[j] ? arr1[i++] : arr2[j++];
    }
    while (i < len1) result[k++] = arr1[i++];
    while (j < len2) result[k++] = arr2[j++];

    return result;
  }

  /**
   * Merges two sorted arrays in-place using swap-and-sort approach.
   *
   * Algorithm:
   * 1. Compare largest element of arr1 with smallest element of arr2
   * 2. If arr1's element is greater, swap them
   * 3. Move pointers inward and repeat until arrays are partitioned
   * 4. Sort both arrays to restore sorted order within each array
   *
   * Key insight: After swapping, arr1 will contain all smaller elements and arr2
   * will contain all larger elements. Then we sort each array independently.
   *
   * Time Complexity: O((N + M) log(N + M)) where N and M are array lengths.
   * Swapping phase is O(min(N, M)), sorting is O(N log N + M log M).
   *
   * Space Complexity: O(1) for swapping. O(log N) if counting sorting's stack space.
   *
   * @param arr1 first sorted array (will contain smaller elements after merge)
   * @param arr2 second sorted array (will contain larger elements after merge)
   */
  public void mergeArraysSwapAndSort(int[] arr1, int[] arr2) {
    int pointer1 = arr1.length - 1;  // Start from end of arr1
    int pointer2 = 0;                // Start from beginning of arr2

    // Swap larger elements of arr1 with smaller elements of arr2
    while (pointer1 >= 0 && pointer2 < arr2.length) {
      if (arr1[pointer1] > arr2[pointer2]) {
        // Swap elements
        int temp = arr1[pointer1];
        arr1[pointer1] = arr2[pointer2];
        arr2[pointer2] = temp;
        pointer1--;
        pointer2++;
      } else {
        pointer1--; // Move left in arr1 if current element is in correct order
      }
      
    }

    // Sort both arrays to maintain sorted order
    Arrays.sort(arr1);
    Arrays.sort(arr2);
  }

  /**
   * Intuition: the gap method is Shell-sort style merging over the two arrays as if
   * they were one combined sorted buffer. Compare elements that are gap positions
   * apart, swapping across arr1, between arrays, or inside arr2 when they are out of
   * order. Shrinking the gap eventually checks adjacent positions, leaving both arrays
   * partitioned in sorted order without a third array.
   *
   * Algorithm:
   *   1. Start with nextGap(len1 + len2).
   *   2. Compare and swap pairs inside arr1 that are gap apart.
   *   3. Compare and swap pairs crossing from arr1 into arr2.
   *   4. Compare and swap pairs inside arr2, then shrink the gap until it becomes 0.
   *
   * Time:  O((n + m) log(n + m)) - each gap pass scans the combined length.
   * Space: O(1) - values are swapped in place.
   *
   * @param arr1 first sorted array, mutated to contain the smaller prefix
   * @param arr2 second sorted array, mutated to contain the larger suffix
   */
  public static void mergeWithoutExtraSpace(long[] arr1, long[] arr2) {
    int len1 = arr1.length, len2 = arr2.length;
    int totalLength = len1 + len2;
    int gap = nextGap(totalLength);

    // GAP reduction loop
    while (gap > 0) {
      // 1. Compare elements within arr1
      for (int i = 0; i + gap < len1; i++) {
        if (arr1[i] > arr1[i + gap]) {
          swap(arr1, i, i + gap);
        }
      }

      // 2. Compare elements between arr1 and arr2
      int i = Math.max(0, len1 - gap);
      int secondIndex = i + gap - len1;
      for (; i < len1 && secondIndex < len2; i++, secondIndex++) {
        if (arr1[i] > arr2[secondIndex]) {
          swap(arr1, arr2, i, secondIndex);
        }
      }

      // 3. Compare elements within arr2
      for (int j = 0; j + gap < len2; j++) {
        if (arr2[j] > arr2[j + gap]) {
          swap(arr2, j, j + gap);
        }
      }

      // Reduce the gap
      gap = nextGap(gap);
    }
  }

  /**
   * Computes the next gap for GAP method. Reduces gap by half and rounds up.
   * Ex: gap = 5 → returns 3 → 2 → 1 → 0
   */
  private static int nextGap(int gap) {
    if (gap <= 1) return 0;
    return (gap / 2) + (gap % 2); // round up to the nearest integer
  }

  // Swap within same array
  private static void swap(long[] arr, int i, int j) {
    long temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  // Swap between two arrays
  private static void swap(long[] arr1, long[] arr2, int i, int j) {
    long temp = arr1[i];
    arr1[i] = arr2[j];
    arr2[j] = temp;
  }
}
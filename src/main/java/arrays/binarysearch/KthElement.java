package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: K-th Element of Two Sorted Arrays
 *
 * Given two sorted arrays, return the k-th value in their merged order without explicitly merging them. The optimal method binary searches a valid partition.
 *
 * Leetcode: none (GeeksForGeeks: https://practice.geeksforgeeks.org/problems/k-th-element-of-two-sorted-array/0)
 * Pattern:  Binary search on partition | Sorted arrays | Order statistic
 *
 * Example:
 *   Input:  arr1 = [2,3,6,7,9], arr2 = [1,4,8,10], k = 5
 *   Output: 6
 *   Why:    the merged order is [1,2,3,4,6,7,8,9,10].
 *
 * Follow-ups:
 *   1. Find the median? Query the middle k value or two middle values.
 *   2. Many k queries? Partition per query or merge once offline.
 *   3. K-th unique value? Skip duplicate runs while counting.
 *   4. Many arrays? Use a min-heap and extract k times.
 *
 * Related: Median of Two Sorted Arrays (4), Merge k Sorted Lists (23).
 */
public class KthElement {

  public static void main(String[] args) {
    int[][] first = { {2,3,6,7,9}, {1,2}, {} };
    int[][] second = { {1,4,8,10}, {3,4,5}, {1} };
    int[] kth = { 5, 4, 1 };
    int[] expected = { 6, 4, 1 };
    for (int i = 0; i < kth.length; i++) {
      int got = findKthElement(first[i], second[i], kth[i]);
      System.out.printf("arr1=%s arr2=%s k=%d -> %d  expected=%d%n", Arrays.toString(first[i]), Arrays.toString(second[i]), kth[i], got, expected[i]);
    }
  }


  /**
   * Finds k-th element using two-pointer approach.
   * Most intuitive and efficient for this specific problem.
   *
   * Algorithm:
   * 1. Initialize two pointers at start of both arrays
   * 2. Iterate k-1 times, always moving the pointer pointing to smaller element
   * 3. This ensures we process elements in sorted order
   * 4. After k-1 iterations, return the minimum of current elements
   *
   * Key insight: By maintaining two pointers and always advancing the smaller one,
   * we traverse the merged array in sorted order without actually merging.
   *
   * Time Complexity: O(k) where k is the position we're looking for.
   *
   * Space Complexity: O(1) using only constant extra variables.
   *
   * @param arr1 first sorted array
   * @param arr2 second sorted array
   * @param k position (1-indexed) to find
   * @return k-th element in merged sorted arrays
   */
  public int findKthElementUsingTwoPointers(int[] arr1, int[] arr2, int k) {
    int pointer1 = 0;
    int pointer2 = 0;
    int count = 0;

    // Iterate until we reach (k-1)th element
    while (count < k - 1) {
      // Move pointer pointing to smaller element
      if (pointer1 < arr1.length && (pointer2 >= arr2.length || arr1[pointer1] <= arr2[pointer2])) {
        pointer1++;
      } else {
        pointer2++;
      }
      count++;
    }

    // Return k-th element (minimum of current elements)
    if (pointer1 < arr1.length && (pointer2 >= arr2.length || arr1[pointer1] <= arr2[pointer2])) {
      return arr1[pointer1];
    } else {
      return arr2[pointer2];
    }
  }

    /**
   * Intuition: A valid partition has exactly k values on the left and every left value <= every right value. Binary search how many values the smaller array contributes.
   *
   * Algorithm:
   *   1. Ensure arr1 is the smaller array.
   *   2. Reject k outside the combined range.
   *   3. Binary search cut1 and derive cut2 = k - cut1.
   *   4. Return max(leftMax1, leftMax2) when partition inequalities hold.
   *
   * Time:  O(log min(n, m)) - binary search uses the smaller array.
   * Space: O(1) - only partition values are stored.
   *
   * @param arr1 first sorted array
   * @param arr2 second sorted array
   * @param k 1-indexed merged position
   * @return k-th smallest value
   */
  public static int findKthElement(int[] arr1, int[] arr2, int k) {
    int len1 = arr1.length;
    int len2 = arr2.length;

    // Always binary search on the smaller array
    if (len1 > len2) {
        return findKthElement(arr2, arr1, k);
    }

    // Edge cases: k too small or too large
    if (k < 1 || k > (len1 + len2)) {
        throw new IllegalArgumentException("k is out of bounds for combined array");
    }

    // Binary search boundaries of the smaller array
    int low = 0;
    int high = len1;

    while (low <= high) {
        int cut1 = (low + high) / 2;
        int cut2 = k - cut1;  // We want k elements total in left partition

        // Handle out of bounds for cut2
        if (cut2 < 0) {
            high = cut1 - 1;
            continue;
        }
        if (cut2 > len2) {
            low = cut1 + 1;
            continue;
        }

        int leftMax1 = (cut1 != 0) ? arr1[cut1 - 1] : Integer.MIN_VALUE;
        int leftMax2 = (cut2 != 0) ? arr2[cut2 - 1] : Integer.MIN_VALUE;
        int rightMin1 = (cut1 == len1) ? Integer.MAX_VALUE : arr1[cut1];
        int rightMin2 = (cut2 == len2) ? Integer.MAX_VALUE : arr2[cut2];

        // Found correct partition
        if (leftMax1 <= rightMin2 && leftMax2 <= rightMin1) {
            return Math.max(leftMax1, leftMax2);
        }

        // Move binary search boundaries
        if (leftMax1 > rightMin2) {
            high = cut1 - 1;
        } else {
            low = cut1 + 1;
        }
    }

    return -1; // Should never be reached if input is valid
  }
}
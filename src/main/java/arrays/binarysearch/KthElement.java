package arrays.binarysearch;

/**
 * GeeksForGeeks link: https://practice.geeksforgeeks.org/problems/k-th-element-of-two-sorted-array/0
 *
 * Problem:
 * Given two sorted arrays `arr1` and `arr2`, return the Kth element in the merged sorted array without merging them.
 *
 * Constraints:
 * - Time: O(log(min(n, m)))
 * - Space: O(1)
 *
 * Example:
 * arr1 = [2, 3, 6, 7, 9]
 * arr2 = [1, 4, 8, 10]
 * k = 5
 * Output: 6 (5th smallest in merged array)
 *
 * Follow-up Questions:
 *
 * 1. What if you need to find the median instead of k-th element?
 *    Answer: Use k = (n+m+1)/2 for lower median, k = (n+m+2)/2 for upper median.
 *    For even total length, median is average of two middle elements.
 *
 * 2. How would you handle if one array is much larger than the other?
 *    Answer: Binary search approach scales well. Two-pointer approach would waste time
 *    iterating through smaller array. Complexity remains O(log(min(n,m))).
 *
 * 3. Can you find k-th element in k queries efficiently?
 *    Answer: Precompute using binary search once for each k. Total O(q * log(min(n,m)))
 *    where q is number of queries. Or use two pointers for all k values together.
 *
 * 4. What if arrays have duplicates and you need the k-th unique element?
 *    Answer: Modify comparison to skip duplicates. When advancing pointers, skip over
 *    equal elements. Count only unique elements toward k.
 *
 * 5. How would you handle three or more sorted arrays?
 *    Answer: Use min-heap with (value, array_index, element_index). Extract min k times.
 *    Time: O(k log m) where m is number of arrays.
 */
public class KthElement {

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
   * Finds the Kth element in the union of two sorted arrays using binary search.
   *
   * Steps:
   * 1. Ensure we always perform binary search on the smaller array.
   * 2. Define binary search bounds based on `k`.
   * 3. Partition both arrays such that:
   *   - Left partition contains `k` elements in total.
   *   - Right partition contains the rest.
   * 4. Check if the partition is valid:
   *   - The largest element in the left partition of `arr1` is less than or equal to the smallest element in the right partition of `arr2`.
   *   - The largest element in the left partition of `arr2` is less than or equal to the smallest element in the right partition of `arr1`.
   * 5. If valid, return the maximum of the largest elements in the left partitions.
   * If not valid, adjust the binary search bounds:
   *   - If the largest element in the left partition of `arr1` is greater
   *     than the smallest element in the right partition of `arr2`, move the high pointer left.
   *   - Otherwise, move the low pointer right.
   *
   * Time complexity: O(log(min(n, m))) where n and m are the lengths of the two arrays.
   * Space complexity: O(1).
   */
  public static int findKthElement(int[] arr1, int[] arr2, int k) {
    int len1 = arr1.length, len2 = arr2.length;

    // Always binary search on the smaller array
    if (len1 > len2) {
      return findKthElement(arr2, arr1, k);
    }

    // Edge cases: k too small or too large
    if (k < 1 || k > (len1 + len2)) {
      throw new IllegalArgumentException("k is out of bounds for combined array");
    }

    // Define binary search bounds of the smaller array
    int low = Math.max( // The lowest index we can cut in arr1
        0, // if k is less than len2, then we can take 0 elements from arr1 and all k elements from arr2
        k - len2 // if k is greater than len2, then we have to take at least (k - len2) elements from arr1
    );
    int high = Math.min( // The highest index we can cut in arr1
        k, // when k is less than len1, then we can take all k elements from arr1
        len1 // when k is greater than len1, then we can take at most len1 elements from arr1
    );



    while (low <= high) {
      int cutArr1 = (low + high) / 2;
      int cutArr2 = k - cutArr1;

      int leftMax1 = (cutArr1 != 0) ? arr1[cutArr1 - 1] : Integer.MIN_VALUE; // left partition max of arr1
      int leftMax2 = (cutArr2 != 0) ? arr2[cutArr2 - 1] : Integer.MIN_VALUE; // left partition max of arr2
      int rightMin1 = (cutArr1 == len1) ? Integer.MAX_VALUE : arr1[cutArr1]; // right partition min of arr1
      int rightMin2 = (cutArr2 == len2) ? Integer.MAX_VALUE : arr2[cutArr2]; // right partition min of arr2

      // Found correct partition
      if (leftMax1 <= rightMin2 && leftMax2 <= rightMin1) {
        return Math.max(leftMax1, leftMax2);
      }

      // Move binary search boundaries
      if (leftMax1 > rightMin2) {
        high = cutArr1 - 1;
      } else if (leftMax2 > rightMin1) {
        low = cutArr1 + 1;
      }
    }

    return -1; // Should never be reached if input is valid
  }
}
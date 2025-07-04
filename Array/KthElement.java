package Array;

/**
 * 🔗 Leetcode: https://leetcode.com/problems/kth-smallest-element-in-sorted-matrix/
 * ❗Alternate (more accurate): https://practice.geeksforgeeks.org/problems/k-th-element-of-two-sorted-array/0
 *
 * ✅ Problem:
 * Given two sorted arrays `arr1` and `arr2`, return the Kth element in the merged sorted array without merging them.
 *
 * ✅ Constraints:
 * - Time: O(log(min(n, m)))
 * - Space: O(1)
 *
 * ✅ Example:
 * arr1 = [2, 3, 6, 7, 9]
 * arr2 = [1, 4, 8, 10]
 * k = 5
 * Output: 6 (5th smallest in merged array)
 *
 * ✅ Follow-up:
 * - What if arrays are very large and live on disk? Use external merge pattern.
 * - What if `k` is very small? Consider min-heap solution (not optimal for large k).
 */
public class KthElement {

  public static void main(String[] args) {
    int[] arr1 = {9, 11, 19, 26, 32, 34, 45, 50, 56, 58, 61, 88};
    int[] arr2 =
        {1, 5, 5, 7, 9, 9, 11, 13, 13, 15, 18, 19, 19, 20, 21, 28, 28, 28, 29, 29, 30, 31, 39, 40, 44, 47, 47, 50, 52,
            56, 57, 61, 61, 61, 66, 68, 69, 70, 70, 74, 75, 75, 77, 78, 79, 80, 82, 85, 87, 89, 90, 90, 90, 92, 93, 95,
            97, 98, 98, 100};
    int k = 64;
    System.out.println("The " + k + "th element is: " + findKthElement(arr1, arr2, k));
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
   *   - If the largest element in the left partition of `arr1` is greater than the smallest element in the right partition of `arr2`, move the high pointer left.
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

    // Define binary search bounds
    int low = Math.max(0, k - len2); // because we can't take more than k from arr1
    int high = Math.min(k, len1);    // because we can't take more than k from arr1

    while (low <= high) {
      int partition1 = (low + high) / 2;
      int partition2 = k - partition1;

      int left1 = (partition1 == 0) ? Integer.MIN_VALUE : arr1[partition1 - 1];
      int left2 = (partition2 == 0) ? Integer.MIN_VALUE : arr2[partition2 - 1];
      int right1 = (partition1 == len1) ? Integer.MAX_VALUE : arr1[partition1];
      int right2 = (partition2 == len2) ? Integer.MAX_VALUE : arr2[partition2];

      // ✅ Found correct partition
      if (left1 <= right2 && left2 <= right1) {
        return Math.max(left1, left2);
      }

      // Move binary search boundaries
      if (left1 > right2) {
        high = partition1 - 1;
      } else {
        low = partition1 + 1;
      }
    }

    return -1; // Should never be reached if input is valid
  }
}
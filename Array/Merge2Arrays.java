package Array;

import java.util.Arrays;

/**
 * 🔹 Problem: Merge Two Sorted Arrays Without Extra Space
 * 🔗 GeeksForGeeks: https://www.geeksforgeeks.org/merge-two-sorted-arrays-o1-extra-space/
 *
 * Given two sorted arrays `arr1[]` and `arr2[]`, merge them such that:
 * - The final arrays remain sorted.
 * - No extra space is used (for in-place variant).
 *
 * 📌 Example:
 * Input:
 *   arr1 = [1, 3, 5, 7]
 *   arr2 = [0, 2, 6, 8, 9]
 * Output:
 *   arr1 = [0, 1, 2, 3]
 *   arr2 = [5, 6, 7, 8, 9]
 *
 * ✅ Follow-ups:
 * - Can you merge in-place in O(1) space and better than O(N*M)?
 * - Use GAP method (Shell Sort technique) to solve in O((N+M) log(N+M)) time.
 */
public class Merge2Arrays {

  public static void main(String[] args) {
    long[] arr1 = {1, 3, 5, 7};
    long[] arr2 = {0, 2, 6, 8, 9};

    System.out.println("Before Merge:");
    System.out.println("Arr1: " + Arrays.toString(arr1));
    System.out.println("Arr2: " + Arrays.toString(arr2));

    mergeWithoutExtraSpace(arr1, arr2);

    System.out.println("\nAfter Merge (In-place):");
    System.out.println("Arr1: " + Arrays.toString(arr1));
    System.out.println("Arr2: " + Arrays.toString(arr2));

    // Extra-space version
    int[] a1 = {1, 3, 5, 7};
    int[] a2 = {0, 2, 6, 8, 9};
    System.out.println("\nMerged Array (With Extra Space): " + Arrays.toString(mergeWithExtraSpace(a1, a2)));
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
   * 🔹 In-place merge of two sorted arrays using a two-pointer technique.
   *
   * Intuition:
   * The idea is that we can swap elements in `arr1` that are larger than elements in `arr2` because at the end of the process,
   * `arr1` should contain all the smaller elements and `arr2` should contain larger elements.
   * After the swap is done, we sort both arrays to maintain order.
   *
   * Steps:
   * 1. Compare the largest element of `arr1` with the smallest element of `arr2`.
   * 2. If `arr1`'s element is greater, swap them.
   * 3. Repeat until all elements are in correct order.
   *
   * Time Complexity: O(N + M) for the merge +  O(N log N + M log M) for sorting
   * Space Complexity: O(1) (in-place modification)
   */
  static void mergeArrays(int[] arr1, int[] arr2) {
    int pointer1 = arr1.length - 1, pointer2 = 0;

    // Step 1: Swap larger elements of arr1 with smaller elements of arr2
    while (pointer1 >= 0 && pointer2 < arr2.length) {
      if (arr1[pointer1] > arr2[pointer2]) {
        int temp = arr2[pointer2];
        arr2[pointer2] = arr1[pointer1];
        arr1[pointer1] = temp;
      }
      pointer1--;
      pointer2++;
    }

    // Step 2: Now arr1 contains all the smaller elements, and arr2 contains larger elements.
    // Sort both arrays to maintain order
    Arrays.sort(arr1);
    Arrays.sort(arr2);
  }

  /**
   * Intuition:
   * The GAP method is a variation of the Shell Sort algorithm that allows us to merge two sorted arrays without using extra space.
   * This method works by comparing elements at a certain gap distance and swapping them if they are out of order.
   * The gap is gradually reduced until it reaches 0, at which point the arrays are fully merged.
   * This approach is efficient and avoids the need for additional space, making it suitable for large datasets.
   *
   * Why this works:
   * Instead of merging from left to right (which needs extra space),
   * we repeatedly push bigger elements toward the end and smaller ones to the beginning
   * by comparing elements at a distance (gap).
   *
   * Steps:
   * 1. Let total length = N + M.
   * 2. Set initial gap = ceil((N + M) / 2).
   * 3. While gap > 0:
   *    a. Compare and swap elements within `arr1`:
   *       - For i from 0 to (N - gap), if arr1[i] > arr1[i + gap], swap.
   *    b. Compare and swap elements between `arr1` and `arr2`:
   *       - Let i = max(0, gap - N), and j = 0.
   *       - While i < N and j < M:
   *           - Compare arr1[i] and arr2[j], swap if arr1[i] > arr2[j].
   *           - Increment i and j.
   *    c. Compare and swap elements within `arr2`:
   *       - For j from 0 to (M - gap), if arr2[j] > arr2[j + gap], swap.
   *    d. Update gap = ceil(gap / 2) using nextGap().
   * 4. Repeat until gap == 0. At this point, both arrays are sorted as one.
   *
   * Time Complexity: O((N + M) log(N + M))
   * Space Complexity: O(1)
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
      int i = Math.max(0, gap - len1);  // starting index in arr2
      for (int j = 0; i < len1 && j < len2; i++, j++) {
        if (arr1[i] > arr2[j]) {
          swap(arr1, arr2, i, j);
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
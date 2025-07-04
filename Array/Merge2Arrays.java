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
   * Approach:
   * - Use the GAP method to merge two sorted arrays in-place.
   * - The GAP method reduces the gap between elements to compare and swap them.
   * - Start with a gap equal to the total length of both arrays.
   * * - Reduce the gap until it reaches 0, comparing elements at the current gap distance.
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
      int i, j;

      // Compare within arr1
      for (i = 0; i + gap < len1; i++) {
        if (arr1[i] > arr1[i + gap]) {
          swap(arr1, i, i + gap);
        }
      }

      // Compare between arr1 and arr2
      for (j = gap > len1 ? gap - len1 : 0; i < len1 && j < len2; i++, j++) {
        if (arr1[i] > arr2[j]) {
          swap(arr1, arr2, i, j);
        }
      }

      // Compare within arr2
      for (j = 0; j + gap < len2; j++) {
        if (arr2[j] > arr2[j + gap]) {
          swap(arr2, j, j + gap);
        }
      }

      gap = nextGap(gap);
    }
  }

  /**
   * Computes the next gap for GAP method. Reduces gap by half and rounds up.
   * Ex: gap = 5 → returns 3 → 2 → 1 → 0
   */
  private static int nextGap(int gap) {
    if (gap <= 1) return 0;
    return (gap / 2) + (gap % 2);
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
package arrays.sorting;

import java.util.Arrays;
/**
 * Problem: Merge Sort
 *
 * Sort an integer array with divide and conquer. Split the array into halves, sort
 * each half recursively, then merge the sorted halves back into one sorted range.
 * This implementation mutates and returns the input array.
 *
 * Pattern:  Sorting | Divide and conquer | Stable merge
 *
 * Example:
 *   Input:  [8,7,2,1,0,9,6]
 *   Output: [0,1,2,6,7,8,9]
 *   Why:    each merge combines two already sorted halves in ascending order.
 *
 * Follow-ups:
 *   1. Sort a linked list?
 *      Split with slow and fast pointers, then merge lists without array copying.
 *   2. Count inversions?
 *      During merge, count how many left-side values each right-side value skips.
 *   3. Make it iterative?
 *      Merge runs of size 1, then 2, then 4, doubling each pass.
 *
 * Related: Sort an Array (912), Count of Smaller Numbers After Self (315).
 */
public class MergeSort {

    public static void main(String[] args) {
        int[][] inputs = { {8, 7, 2, 1, 0, 9, 6}, {}, {4, 1, 4, -2} };
        int[][] expected = { {0, 1, 2, 6, 7, 8, 9}, {}, {-2, 1, 4, 4} };

        for (int i = 0; i < inputs.length; i++) {
            int[] input = inputs[i].clone();
            int[] got = mergeSort(input);
            System.out.printf("input=%s -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }

/**
 * Intuition: it is easier to merge two sorted arrays than to sort one unsorted
 * array directly. Recursively sort smaller halves until each segment is trivial,
 * then merge the sorted pieces on the way back up.
 *
 * Algorithm:
 *   1. Return immediately for null or length-one input.
 *   2. Allocate one temporary array reused by all merge calls.
 *   3. Recursively split the full range until single-element ranges.
 *   4. Merge each pair of sorted ranges back into the original array.
 *
 * Time:  O(n log n) - each level merges n elements across log n levels.
 * Space: O(n) - the temporary merge buffer stores one array copy.
 *
 * @param array input array sorted in place
 * @return the same array reference after sorting
 */
    public static int[] mergeSort(int[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }
        int[] temp = new int[array.length];
        mergeSort(array, temp, 0, array.length - 1);
        return array;
    }

    /**
     * Recursively splits and sorts the array segment [left, right].
     */
    private static void mergeSort(int[] array, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(array, temp, left, mid);
        mergeSort(array, temp, mid + 1, right);
        merge(array, temp, left, mid, right);
    }

    /**
     * Merges two sorted subarrays:
     * - First: [left, mid]
     * - Second: [mid + 1, right]
     */
    private static void merge(int[] array, int[] temp, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        for (int index = left; index <= right; index++) {
            array[index] = temp[index];
        }
    }
}

package arrays.sorting;

import java.util.Arrays;

/**
 * Merge Sort Algorithm Implementation
 *
 * Merge Sort is a stable, divide-and-conquer sorting algorithm that:
 * - Splits the array into two halves recursively
 * - Sorts each half independently
 * - Merges the two sorted halves into a single sorted segment
 *
 * Time Complexity:
 * - Best Case: O(n log n)
 * - Average Case: O(n log n)
 * - Worst Case: O(n log n)
 *
 * Space Complexity:
 * - O(n) for temporary array used during merge
 *
 * Key Characteristics:
 * - Stable sort
 * - Predictable performance across all cases
 * - Not in-place for array implementation
 */
public class MergeSort {

    /**
     * Main method demonstrating Merge Sort usage.
     */
    public static void main(String[] args) {
        int[] data = {8, 7, 2, 1, 0, 9, 6};
        System.out.println("Unsorted Array: " + Arrays.toString(data));

        data = mergeSort(data);

        System.out.println("Sorted Array in Ascending Order: " + Arrays.toString(data));
    }

    /**
     * Sorts the input array using Merge Sort and returns a sorted copy.
     * Intuition: Divide the array into two halves, sort each half independently, and then merge the two sorted halves into a single sorted segment.
     * 
     * Algorithm Steps:
     * 1. Divide the array into two halves.
     * 2. Sort each half independently.
     * 3. Merge the two sorted halves into a single sorted segment.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
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

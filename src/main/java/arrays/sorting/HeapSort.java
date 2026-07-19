package arrays.sorting;

import java.util.Arrays;
/**
 * Problem: Heap Sort
 *
 * Sort an integer array by building a max heap and repeatedly moving the largest
 * value to the end of the active heap. This implementation returns a sorted copy
 * and leaves the input unchanged.
 *
 * Pattern:  Sorting | Binary heap | In-place heap operations
 *
 * Example:
 *   Input:  [8,7,2,1,0,9,6]
 *   Output: [0,1,2,6,7,8,9]
 *   Why:    each heap extraction fixes the next largest value from right to left.
 *
 * Follow-ups:
 *   1. Make it sort in place?
 *      Run the same heap operations directly on the input instead of a copy.
 *   2. Is heap sort stable?
 *      No; equal values can be reordered by swaps with the heap root.
 *   3. Use a min heap for descending order?
 *      Yes, or keep a max heap and reverse the final sorted array.
 *
 * Related: Sort an Array (912), Kth Largest Element in an Array (215).
 */
public class HeapSort {

    public static void main(String[] args) {
        int[][] inputs = { {8, 7, 2, 1, 0, 9, 6}, {}, {5, 5, -1, 3} };
        int[][] expected = { {0, 1, 2, 6, 7, 8, 9}, {}, {-1, 3, 5, 5} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = heapSort(inputs[i]);
            System.out.printf("input=%s -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }

/**
 * Intuition: a max heap can reveal the largest remaining value in O(1) at the
 * root. Swap that root to the end, shrink the heap, and restore the heap property
 * so the next largest value rises to the root.
 *
 * Algorithm:
 *   1. Return null or a copy for trivial inputs.
 *   2. Copy the input and heapify all internal nodes bottom-up.
 *   3. Repeatedly swap the root with the heap end.
 *   4. Shrink the heap and heapify the root.
 *
 * Time:  O(n log n) - heap construction is linear and each extraction heapifies.
 * Space: O(n) - this version returns a sorted copy of the input.
 *
 * @param arr input array
 * @return sorted copy of arr, or null for null input
 */
    public static int[] heapSort(int[] arr) {
        if (arr == null)  return null;
        if (arr.length <= 1) return Arrays.copyOf(arr, arr.length);

        int[] result = Arrays.copyOf(arr, arr.length);
        int len = result.length;

        // Build max-heap.
        for (int i = (len / 2) - 1; i >= 0; i--) {
            heapify(result, len, i);
        }

        // Move current max to the end and restore heap.
        for (int end = len - 1; end > 0; end--) {
            swap(result, 0, end);
            heapify(result, end, 0);
        }

        return result;
    }

    /**
     * Restores max-heap property for subtree rooted at index.
     */
    private static void heapify(int[] arr, int heapSize, int index) {
        int largest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        if (leftChild < heapSize && arr[leftChild] > arr[largest]) {
            largest = leftChild;
        }
        if (rightChild < heapSize && arr[rightChild] > arr[largest]) {
            largest = rightChild;
        }

        if (largest != index) {
            swap(arr, index, largest);
            heapify(arr, heapSize, largest);
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

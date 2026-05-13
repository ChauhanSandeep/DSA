package arrays.sorting;

import java.util.Arrays;

/**
 * Heap Sort Algorithm Implementation
 *
 * Heap Sort uses a binary heap (max-heap for ascending order) to sort elements:
 * - Build a max-heap from the input
 * - Repeatedly move the max element (root) to the end
 * - Restore heap property for the reduced heap
 *
 * Time Complexity:
 * - Best Case: O(n log n)
 * - Average Case: O(n log n)
 * - Worst Case: O(n log n)
 *
 * Space Complexity:
 * - O(1) auxiliary space for in-place heap operations
 * - This implementation returns a copied sorted array to keep input unchanged
 *
 * Key Characteristics:
 * - Not stable
 * - In-place heap operations
 * - Guaranteed O(n log n) performance
 */
public class HeapSort {

    /**
     * Main method demonstrating Heap Sort usage.
     */
    public static void main(String[] args) {
        int[] data = {8, 7, 2, 1, 0, 9, 6};
        System.out.println("Unsorted Array: " + Arrays.toString(data));

        int[] sortedData = heapSort(data);

        System.out.println("Original Array (unchanged): " + Arrays.toString(data));
        System.out.println("Sorted Array in Ascending Order: " + Arrays.toString(sortedData));
    }

    /**
     * Sorts the input array using Heap Sort and returns a sorted copy.
     * Intuition: Build a max-heap from the input array and then repeatedly move the max element (root) to the end of the array and restore the heap property for the reduced heap.
     * 
     * Algorithm Steps:
     * 1. Build a max-heap from the input array.
     * 2. Move the max element (root) to the end of the array and restore the heap property for the reduced heap.
     * 3. Repeat the process until the entire array is sorted.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(1)
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

package arrays.sorting;

import java.util.Arrays;

/**
 * QuickSort Algorithm Implementation
 *
 * QuickSort is a highly efficient, divide-and-conquer sorting algorithm that works by
 * selecting a 'pivot' element and partitioning the array around it such that:
 * - Elements smaller than pivot are on the left
 * - Elements greater than pivot are on the right
 * - The pivot is in its final sorted position
 *
 * Algorithm Steps:
 * 1. Choose a pivot element (this implementation uses the rightmost element)
 * 2. Partition the array around the pivot
 * 3. Recursively sort the left and right subarrays
 *
 * Key Characteristics:
 * - Returns a new sorted array without modifying input
 * - Not stable (relative order of equal elements may change)
 * - Generally faster than merge sort and heap sort in practice
 * - Performance depends on pivot selection
 *
 * Time Complexity:
 * - Best Case: O(n log n) - when pivot divides array into equal halves
 * - Average Case: O(n log n) - with random pivot selection
 * - Worst Case: O(n²) - when array is already sorted and pivot is always smallest/largest
 *
 * Space Complexity:
 * - O(log n) - for recursion stack in average case
 * - O(n) - for recursion stack in worst case
 *
 * Optimization Techniques:
 * - Randomized pivot selection to avoid worst case
 * - Median-of-three pivot selection
 * - Insertion sort for small subarrays
 * - Three-way partitioning for arrays with many duplicates
 *
 * Related Problems:
 * - Quick Select for finding kth smallest element
 * - Dutch National Flag problem (3-way partitioning)
 *
 * @author Sandeep Chauhan
 */
public class QuickSort {

    /**
     * Main method demonstrating QuickSort usage.
     * Sorts an example array and prints the result.
     */
    public static void main(String[] args) {
        int[] data = {8, 7, 2, 1, 0, 9, 6};
        System.out.println("Unsorted Array: " + Arrays.toString(data));

        int[] sortedData = sortArray(data);

        System.out.println("Original Array (unchanged): " + Arrays.toString(data));
        System.out.println("Sorted Array in Ascending Order: " + Arrays.toString(sortedData));
    }

    /**
     * Sorts the given array using QuickSort and returns a sorted copy.
     *
     * @param array The input array to sort
     * @return A new sorted array, leaving input array unchanged
     */
    public static int[] sortArray(int[] array) {
        if (array == null) {
            return null;
        }

        int[] result = Arrays.copyOf(array, array.length);
        quickSort(result, 0, result.length - 1);
        return result;
    }

    /**
     * Internal recursive QuickSort that sorts the provided array in-place.
     */
    private static void quickSort(int[] array, int left, int right) {
        if (left >= right) return;

        int partitionIndex = partition(array, left, right);
        quickSort(array, left, partitionIndex - 1);
        quickSort(array, partitionIndex + 1, right);
    }

    /**
     * Partition function - rearranges elements around a pivot.
     * 
     * Algorithm Steps:
     * 1. Choose the rightmost element as the pivot
     * 2. Use partitionIndex to track where next smaller element should go
     * 3. Scan array from left to right
     * 4. When element <= pivot is found, swap it to partitionIndex position
     * 5. After scanning, place pivot in its correct sorted position
     *
     * @param array The array to partition
     * @param left Starting index of subarray
     * @param right Ending index of subarray (pivot position)
     * @return The final sorted position of the pivot element
     */
    private static int partition(int[] array, int left, int right) {
        // Choose the rightmost element as the pivot
        int pivot = array[right];

        // `partitionIndex` tracks the position where the next smaller element should go
        int partitionIndex = left;

        for (int i = left; i < right; i++) {
            // If current element is <= pivot, it belongs on the left side
            if (array[i] <= pivot) {
                // Swap current element with the element at `partitionIndex`
                swap(array, i, partitionIndex);

                // Move the partitionIndex forward to prepare for next smaller element
                partitionIndex++;
            }
        }

        // Finally, place the pivot element in its correct sorted position
        swap(array, partitionIndex, right);

        // Return the index where pivot is now placed
        return partitionIndex;
    }

    /**
     * Helper method to swap two elements in an array.
     *
     * @param array The array containing elements to swap
     * @param i Index of first element
     * @param j Index of second element
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[j];
        array[j] = array[i];
        array[i] = temp;
    }
}

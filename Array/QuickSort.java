package Array;

import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {
        int[] data = {8, 7, 2, 1, 0, 9, 6};
        System.out.println("Unsorted Array: " + Arrays.toString(data));

        quickSort(data, 0, data.length - 1);

        System.out.println("Sorted Array in Ascending Order: " + Arrays.toString(data));
    }

    public static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(array, low, high);
            quickSort(array, low, partitionIndex - 1);
            quickSort(array, partitionIndex + 1, high);
        }
    }

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

    private static void swap(int[] array, int i, int j) {
        int temp = array[j];
        array[j] = array[i];
        array[i] = temp;
    }
}

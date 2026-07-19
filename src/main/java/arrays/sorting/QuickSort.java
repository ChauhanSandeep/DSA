package arrays.sorting;

import java.util.Arrays;
/**
 * Problem: Quick Sort
 *
 * Sort an integer array with divide and conquer. Choose a pivot, partition the
 * array so smaller values are on the left and larger values on the right, then
 * recursively sort both sides. This implementation returns a sorted copy.
 *
 * Pattern:  Sorting | Partitioning | Divide and conquer
 *
 * Example:
 *   Input:  [8,7,2,1,0,9,6]
 *   Output: [0,1,2,6,7,8,9]
 *   Why:    each partition places its pivot in final sorted position.
 *
 * Follow-ups:
 *   1. Avoid worst-case O(n^2)?
 *      Randomize the pivot or use median-of-three selection.
 *   2. Many duplicate values?
 *      Use three-way partitioning to group values equal to the pivot.
 *   3. Find kth smallest only?
 *      Use quickselect and recurse into just one partition.
 *
 * Related: Sort an Array (912), Kth Largest Element in an Array (215).
 */
public class QuickSort {

    public static void main(String[] args) {
        int[][] inputs = { {8, 7, 2, 1, 0, 9, 6}, {}, {3, 3, 2, -1} };
        int[][] expected = { {0, 1, 2, 6, 7, 8, 9}, {}, {-1, 2, 3, 3} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = sortArray(inputs[i]);
            System.out.printf("input=%s -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }

/**
 * Intuition: partitioning around a pivot puts that pivot exactly where it belongs
 * in the sorted order. Once the pivot is fixed, the left and right sides can be
 * sorted independently.
 *
 * Algorithm:
 *   1. Return null for null input.
 *   2. Copy the input so the original array remains unchanged.
 *   3. Quick-sort the full copied range in place.
 *   4. Return the sorted copy.
 *
 * Time:  O(n log n) average, O(n^2) worst case - depends on pivot balance.
 * Space: O(n) - the returned copy dominates the recursion stack.
 *
 * @param array input array
 * @return sorted copy of array, or null for null input
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

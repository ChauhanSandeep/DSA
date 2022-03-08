package Array;

import java.util.Arrays;

public class QuickSort {

    public static void main(String args[]) {
        int[] data = {8, 7, 2, 1, 0, 9, 6};
        System.out.println("Unsorted Array");
        System.out.println(Arrays.toString(data));

        new QuickSort().quickSort(data, 0, data.length - 1);

        System.out.println("Sorted Array in Ascending Order: ");
        System.out.println(Arrays.toString(data));
    }

    void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    // method to find the partition position
    private int partition(int[] array, int left, int right) {
        // choose the rightmost element as pivotElement
        int pivotElement = array[right];

        // element before pointer will contain elements smaller than or equals pivotElement
        int pointer = left;

        for (int i = left; i < right; i++) {
            if (array[i] <= pivotElement) {
                swap(array, i, pointer);
                pointer++;
            }
        }

        // swap the pivotElement element with the greater element specified by pointer
        swap(array, pointer, right);
        return pointer;
    }

    private void swap(int[] array, int i, int pointer) {
        int temp = array[pointer];
        array[pointer] = array[i];
        array[i] = temp;
    }
}

package Array;

import java.util.Arrays;

public class Merge2Arrays {
    public static void main(String[] args) {
        long[] arr1 = {1, 3, 5, 7};
        long[] arr2 = {0, 2, 6, 8, 9};

        System.out.println("Before Merge:");
        System.out.println("Arr1: " + Arrays.toString(arr1));
        System.out.println("Arr2: " + Arrays.toString(arr2));

        mergeWithoutExtraSpace(arr1, arr2);

        System.out.println("\nAfter Merge:");
        System.out.println("Arr1: " + Arrays.toString(arr1));
        System.out.println("Arr2: " + Arrays.toString(arr2));

        // Merging into a new array with extra space
        int[] arr3 = {1, 3, 5, 7};
        int[] arr4 = {0, 2, 6, 8, 9};
        System.out.println("\nMerged Array (with extra space): " + Arrays.toString(mergeArrays(arr3, arr4)));
    }

    /**
     * Merges two sorted arrays into a new array with extra space.
     * Time Complexity: O(N + M)
     * Space Complexity: O(N + M)
     */
    public static int[] mergeArrays(int[] arr1, int[] arr2) {
        int[] result = new int[arr1.length + arr2.length];
        int i = 0, j = 0, k = 0;

        while (i < arr1.length && j < arr2.length) {
            result[k++] = (arr1[i] < arr2[j]) ? arr1[i++] : arr2[j++];
        }

        while (i < arr1.length) {
            result[k++] = arr1[i++];
        }

        while (j < arr2.length) {
            result[k++] = arr2[j++];
        }
        return result;
    }

    /**
     * Merges two sorted arrays without using extra space.
     * Uses the GAP method (Shell Sort-like approach) for efficient merging.
     * Time Complexity: O((N+M) log(N+M))
     * Space Complexity: O(1)
     */
    public static void mergeWithoutExtraSpace(long[] arr1, long[] arr2) {
        int n = arr1.length, m = arr2.length;
        int gap = (n + m) / 2 + ((n + m) % 2); // Initial gap

        while (gap > 0) {
            int i, j;

            // Compare elements within arr1
            for (i = 0; i + gap < n; i++) {
                if (arr1[i] > arr1[i + gap]) {
                    swap(arr1, i, i + gap);
                }
            }

            // Compare elements between arr1 and arr2
            for (j = gap > n ? gap - n : 0; i < n && j < m; i++, j++) {
                if (arr1[i] > arr2[j]) {
                    swap(arr1, arr2, i, j);
                }
            }

            // Compare elements within arr2
            for (j = 0; j + gap < m; j++) {
                if (arr2[j] > arr2[j + gap]) {
                    swap(arr2, j, j + gap);
                }
            }

            // Reduce gap
            gap = (gap == 1) ? 0 : (gap / 2 + gap % 2);
        }
    }

    // Swap elements within the same array
    private static void swap(long[] arr, int i, int j) {
        long temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Swap elements between arr1 and arr2
    private static void swap(long[] arr1, long[] arr2, int i, int j) {
        long temp = arr1[i];
        arr1[i] = arr2[j];
        arr2[j] = temp;
    }
}

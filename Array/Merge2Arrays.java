package Array;

import java.util.Arrays;

public class Merge2Arrays {
    public static void main(String[] args) {
        long[] arr1 = {1, 3, 5, 7};
        long[] arr2 = {0, 2, 6, 8, 9};
        mergeWithoutExtraSpace(arr1, arr2);

        int[] arr3 = {1, 3, 5, 7};
        int[] arr4 = {0, 2, 6, 8, 9};
        System.out.println(Arrays.toString(mergeArrays(arr3, arr4)));
    }

    public static int[] mergeArrays(int[] arr1, int[] arr2) {
        int[] result = new int[arr1.length + arr2.length];
        int i = 0;
        int j=0;
        int k = 0;
        while(i < arr1.length && j < arr2.length) {
            if(arr1[i] < arr2[j]) {
                result[k++] = arr1[i++];
            }else{
                result[k++] = arr2[j++];
            }
        }

        while(i < arr1.length) {
            result[k++] = arr1[i++];
        }

        while(j < arr2.length) {
            result[k++] = arr2[j++];
        }
        return result;
    }

    /**
     * Sort the two sorted arrays without using extra space
     */
    public static void mergeWithoutExtraSpace(long[] arr1, long[] arr2) {
        for(int i=0; i<arr1.length; i++) {
            if(arr1[i] > arr2[0]) {
                long temp = arr1[i];
                arr1[i] = arr2[0];
                arr2[0] = temp;
                rearrangeFirstElement(arr2);
            }
        }

        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
    }

    // place first element of arr in correct position
    private static void rearrangeFirstElement(long[] arr) {
        for(int i=0; i<arr.length-1; i++) {
            if(arr[i] > arr[i+1]) {
                long temp = arr[i];
                arr[i] = arr[i+1];
                arr[i+1] = temp;
            }
        }
    }
}

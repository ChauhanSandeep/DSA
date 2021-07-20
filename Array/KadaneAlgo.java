package Array;

import java.util.Arrays;

/**
 * Find the subarray with maximum sum
 */
public class KadaneAlgo {
    public static void main(String[] args) {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int[] maxSum = kadane(arr);
        System.out.println(Arrays.toString(maxSum));
    }

    private static int[] kadane(int[] arr) {
        int maxSoFar = Integer.MIN_VALUE;
        int maxEndingHere = 0;

        int start =0, end = 0;
        int beg = 0;

        for (int i = 0, arrLength = arr.length; i < arrLength; i++) {
            maxEndingHere += arr[i];

            if(maxEndingHere > maxSoFar) {
                maxSoFar = maxEndingHere;
                start = beg;
                end = i;
            }

            if(maxEndingHere < 0) {
                maxEndingHere = 0;
                beg = i+1;
            }

        }
        System.out.println(maxEndingHere);
        System.out.println(maxSoFar);
        return Arrays.copyOfRange(arr, start, end + 1);
    }


}

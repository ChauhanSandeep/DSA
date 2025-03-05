package Array;

import java.util.Arrays;

/**
 * Find the subarray with the maximum sum using Kadane's Algorithm.
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 * LeetCode Problem: https://leetcode.com/problems/maximum-subarray/
 */
public class KadaneAlgo {
    public static void main(String[] args) {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int[] maxSubarray = kadane(arr);
        System.out.println("Maximum sum subarray: " + Arrays.toString(maxSubarray));
    }

    /**
     * Implements Kadane's Algorithm to find the contiguous subarray with the maximum sum.
     *
     * @param arr Input array of integers.
     * @return The subarray that has the maximum sum.
     */
    private static int[] kadane(int[] arr) {
        int maxSoFar = Integer.MIN_VALUE;
        int maxEndingHere = 0;
        int start = 0, end = 0, tempStart = 0;

        for (int i = 0; i < arr.length; i++) {
            maxEndingHere += arr[i];

            if (maxEndingHere > maxSoFar) {
                maxSoFar = maxEndingHere;
                start = tempStart;
                end = i;
            }

            if (maxEndingHere < 0) {
                maxEndingHere = 0;
                tempStart = i + 1;
            }
        }

        return Arrays.copyOfRange(arr, start, end + 1);
    }
}

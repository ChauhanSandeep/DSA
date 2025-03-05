package Bitwise;

/**
 * This class finds two numbers in an array whose AND operation results in the maximum value.
 * 
 * Algorithm:
 * - Start from the most significant bit and search for matching bits, adding to the result if found.
 * - Time Complexity: O(n * log(max))
 * - Space Complexity: O(1)
 * 
 * LeetCode Problem Link: Currently, there is no specific problem link for this algorithm on LeetCode.
 */
public class MaximumAnd {

    public static void main(String[] args) {
        int[] arr = {5, 8, 7, 2};
        int maxAnd = new MaximumAnd().findMaximumAnd(arr, arr.length);
        System.out.println(maxAnd);
    }

    /**
     * Finds the maximum AND value pair in the given array.
     * @param arr The input array of integers.
     * @param size The size of the input array.
     * @return The maximum AND value of any two numbers in the array.
     */
    public int findMaximumAnd(int[] arr, int size) {
        int maxAndValue = 0;
        int maxSignificantBit = findMaxSignificantBit(arr, size);
        for (int bit = maxSignificantBit; bit >= 0; bit--) {
            int candidate = maxAndValue | (1 << bit);
            if (countMatchingBits(candidate, arr, size) >= 2) {
                maxAndValue = candidate;
            }
        }
        return maxAndValue;
    }

    /**
     * Counts how many numbers in the array have the specified bit pattern.
     * @param pattern The bit pattern to match.
     * @param arr The input array of integers.
     * @param size The size of the input array.
     * @return The count of numbers matching the bit pattern.
     */
    private int countMatchingBits(int pattern, int[] arr, int size) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if ((pattern & arr[i]) == pattern) {
                count++;
            }
        }
        return count;
    }

    /**
     * Finds the most significant bit in the given array.
     * @param arr The input array of integers.
     * @param size The size of the input array.
     * @return The position of the most significant bit.
     */
    private int findMaxSignificantBit(int[] arr, int size) {
        int max = Integer.MIN_VALUE;
        for (int num : arr) {
            max = Math.max(num, max);
        }
        return (int) (Math.log(max) / Math.log(2));
    }
}

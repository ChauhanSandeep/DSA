package Bitwise;

import java.util.HashSet;

/**
 * This class finds two numbers in an array whose XOR operation results in the maximum value.
 * 
 * Algorithm:
 * - Use a bitmask to iteratively check each bit position from the most significant to the least significant.
 * - Track the maximum XOR value by testing potential new maximums and verifying the existence of necessary prefixes.
 * - Time Complexity: O(n * log(max))
 * - Space Complexity: O(n)
 * 
 * LeetCode Problem Link: Currently, there is no specific problem link for this algorithm on LeetCode.
 */
public class MaximumXor {

    public static void main(String[] args) {
        int[] arr = {5, 8, 2};
        int n = arr.length;

        System.out.println(new MaximumXor().findMaximumXor(arr, n));
    }

    /**
     * Finds the maximum XOR value of any two numbers in the given array.
     * @param arr The input array of integers.
     * @param size The size of the input array.
     * @return The maximum XOR value of any two numbers in the array.
     */
    public int findMaximumXor(int[] arr, int size) {
        int maxXorValue = 0, mask = 0;
        HashSet<Integer> prefixes = new HashSet<>();
        int maxSignificantBit = findMaxSignificantBit(arr);

        // Iterate through all bits from the most significant to the least significant
        for (int bit = maxSignificantBit; bit >= 0; bit--) {
            mask |= (1 << bit);
            for (int j = 0; j < size; j++) {
                prefixes.add(arr[j] & mask);
            }

            int newMax = maxXorValue | (1 << bit);
            for (int prefix : prefixes) {
                if (prefixes.contains(newMax ^ prefix)) {
                    maxXorValue = newMax;
                    break;
                }
            }
            prefixes.clear();
        }
        return maxXorValue;
    }

    /**
     * Finds the most significant bit in the given array.
     * @param arr The input array of integers.
     * @return The position of the most significant bit.
     */
    private int findMaxSignificantBit(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int num : arr) {
            max = Math.max(num, max);
        }
        return (int) (Math.log(max) / Math.log(2));
    }
}

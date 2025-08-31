package arrays;

import java.util.Arrays;

/**
 * Duplicate Zeros
 *
 * Problem: Given array, duplicate each zero, shifting remaining elements to the right.
 * Perform operation in-place without changing array length.
 *
 * Example: arr =  [1,0,2,3,0,4,5]
 *              -> [1,0,0,2,3,0,0]
 * First 0 becomes [0,0], second 0 becomes [0,0], elements shift right.
 *
 * LeetCode: https://leetcode.com/problems/duplicate-zeros
 *
 * Follow-up Questions:
 * - How to duplicate other values instead of zeros? (Change condition in algorithm)
 * - What if we can change array length? (Use additional space for simpler implementation)
 * - How to duplicate elements k times instead of once? (Modify duplication logic)
 */
public class DuplicateZeros {

    /**
     * Duplicates zeros in array in-place without changing length.
     *
     * Steps:
     * 1. Count total zeros to compute virtual length (arrLength + zeroCount).
     * 2. Set writeIndex = virtualLength - 1, readIndex = arrLength - 1.
     * 3. Move both indices backward:
     *    - If arr[readIndex] is non-zero → copy it once (if within bounds).
     *    - If arr[readIndex] is zero → write it twice (if within bounds).
     * 4. Continue until readIndex < 0 or writeIndex < 0.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param arr input array to modify in-place
     */
    public void duplicateZeros(int[] arr) {
        int arrLength = arr.length;
        int zeros = (int) Arrays.stream(arr).filter(num -> num == 0).count();

        // Write position considering duplicates. The size is more than the original array size
        // because zeroes will expand the result. this initially may go out of bounds
        int writeIndex = arrLength + zeros - 1;
        // Read position in the original array
        int readIndex = arrLength - 1; // Original array end

        while (readIndex >= 0 && writeIndex >= 0) {
            if (arr[readIndex] != 0) {
                if (writeIndex < arrLength) { // write only if within bounds
                    arr[writeIndex] = arr[readIndex];
                }
            } else {
                if (writeIndex < arrLength) { // write only if within bounds
                    arr[writeIndex] = 0;
                }
                writeIndex--; // move to next position for the duplicate zero
                if (writeIndex < arrLength) {
                    arr[writeIndex] = 0;
                }
            }
            readIndex--;
            writeIndex--;
        }
    }
}

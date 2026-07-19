package arrays.slidingwindow;

import java.util.Arrays;

/**
 * Problem: Duplicate Zeros
 *
 * In a fixed-length array, duplicate every zero and shift following values to
 * the right. Values that would move beyond the array length are discarded, and
 * the modification must happen in place.
 *
 * Leetcode: https://leetcode.com/problems/duplicate-zeros/ (Easy)
 * Rating:   acceptance 52.4% (Easy) - contest rating 1263
 * Pattern:  Two pointers | Reverse write | In-place array editing
 *
 * Example:
 *   Input:  arr = [1,0,2,3,0,4,5,0]
 *   Output: [1,0,0,2,3,0,0,4]
 *   Why:    each zero consumes one extra virtual slot, so the final values are
 *           copied backward into the original fixed-length array.
 *
 * Follow-ups:
 *   1. Duplicate a different target value?
 *      Count that target and use the same backward write logic.
 *   2. What if the array may grow?
 *      Use a dynamic list or allocate a new result array.
 *   3. Duplicate zeros k times?
 *      Add k extra virtual slots per zero and write k + 1 copies backward.
 *
 * Related: Move Zeroes (283), Remove Duplicates from Sorted Array (26).
 */
public class DuplicateZeros {

    public static void main(String[] args) {
        DuplicateZeros solver = new DuplicateZeros();
        int[][] inputs = {{1, 0, 2, 3, 0, 4, 5, 0}, {1, 2, 3}, {0, 0, 1}};
        int[][] expected = {{1, 0, 0, 2, 3, 0, 0, 4}, {1, 2, 3}, {0, 0, 0}};

        for (int i = 0; i < inputs.length; i++) {
            int[] got = inputs[i].clone();
            solver.duplicateZeros(got);
            System.out.printf("arr=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }


    /**
     * Intuition: duplicating zeros expands the array in a virtual copy, but only
     * the first arr.length positions survive. Writing from right to left avoids
     * overwriting values that still need to be copied.
     *
     * Algorithm:
     *   1. Count zeros to know the final virtual writeIndex.
     *   2. Move readIndex from the real end and writeIndex from the virtual end.
     *   3. Copy non-zero values once and zero values twice when the write index is in bounds.
     *
     * Time:  O(n) - one count pass and one backward write pass.
     * Space: O(1) - the array is modified in place.
     *
     * @param arr fixed-length array modified in place
     */
    public void duplicateZeros(int[] arr) {
        int arrLength = arr.length;
        int zeroCount = (int) Arrays.stream(arr).filter(num -> num == 0).count();

        // Write position considering duplicates. The size is more than the original array size
        // because zeroes will expand the result. this initially may go out of bounds
        int writeIndex = arrLength + zeroCount - 1;
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

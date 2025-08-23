package Array;

/**
 * Duplicate Zeros
 * 
 * Problem: Given array, duplicate each zero, shifting remaining elements to the right.
 * Perform operation in-place without changing array length.
 * 
 * Example: arr = [1,0,2,3,0,4,5] -> [1,0,0,2,3,0,0]
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
     * Algorithm:
     * 1. Count zeros to determine final positions
     * 2. Work backwards from end of array
     * 3. For each element, place it at correct final position
     * 4. If element is zero, place it twice (if space allows)
     * 5. Continue until all elements processed
     * 
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - in-place modification
     * 
     * @param arr input array to modify in-place
     */
    public void duplicateZeros(int[] arr) {
        int n = arr.length;
        int zeroCount = 0;

        // Count zeros to determine final length needed
        for (int num : arr) {
            if (num == 0) zeroCount++;
        }

        // Work backwards, placing elements at their final positions
        int writeIndex = n + zeroCount - 1;

        for (int readIndex = n - 1; readIndex >= 0; readIndex--) {
            // Place current element at write position
            if (writeIndex < n) {
                arr[writeIndex] = arr[readIndex];
            }
            writeIndex--;

            // If current element is zero, place duplicate
            if (arr[readIndex] == 0) {
                if (writeIndex < n) {
                    arr[writeIndex] = 0;
                }
                writeIndex--;
            }
        }
    }

    /**
     * Two-pass approach for clarity (same complexity)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public void duplicateZerosTwoPass(int[] arr) {
        int n = arr.length;
        int zeros = 0;

        // First pass: count zeros
        for (int i = 0; i < n; i++) {
            if (arr[i] == 0) {
                zeros++;
            }
        }

        // Second pass: place elements from right to left
        int i = n - 1;
        int write = n + zeros - 1;

        while (i >= 0 && write >= 0) {
            if (arr[i] != 0) {
                // Non-zero element: place once
                if (write < n) {
                    arr[write] = arr[i];
                }
            } else {
                // Zero element: place twice
                if (write < n) {
                    arr[write] = 0;
                }
                write--;
                if (write < n) {
                    arr[write] = 0;
                }
            }
            i--;
            write--;
        }
    }

    /**
     * Simple approach using temporary array (for comparison)
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public void duplicateZerosWithExtraSpace(int[] arr) {
        int n = arr.length;
        int[] temp = new int[n];
        int writeIndex = 0;

        for (int i = 0; i < n && writeIndex < n; i++) {
            temp[writeIndex] = arr[i];
            writeIndex++;

            // Duplicate zero if there's space
            if (arr[i] == 0 && writeIndex < n) {
                temp[writeIndex] = 0;
                writeIndex++;
            }
        }

        // Copy back to original array
        System.arraycopy(temp, 0, arr, 0, n);
    }
}

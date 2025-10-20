package arrays;

import java.util.Arrays;

/**
 * LeetCode Problem 1089: Duplicate Zeros
 *
 * Problem Statement:
 * Given a fixed-length integer array arr, duplicate each occurrence of zero, shifting the
 * remaining elements to the right. Note that elements beyond the length of the original
 * array are not written. Do the above modifications to the input array in-place and do
 * not return anything.
 *
 * Example 1:
 * Input: arr = [1,0,2,3,0,4,5,0]
 * Output: [1,0,0,2,3,0,0,4]
 * Explanation: After calling your function, the input array is modified to [1,0,0,2,3,0,0,4].
 * The first 0 at index 1 is duplicated, pushing elements right. The second 0 at index 4 is
 * also duplicated. The elements 5 and 0 at the end are pushed beyond the array length and
 * are discarded.
 *
 * Example 2:
 * Input: arr = [1,2,3]
 * Output: [1,2,3]
 * Explanation: No zeros to duplicate, array remains unchanged.
 *
 * Constraints:
 * 1 <= arr.length <= 10^4
 * 0 <= arr[i] <= 9
 *
 * LeetCode Link: https://leetcode.com/problems/duplicate-zeros/
 *
 * Follow-up Questions:
 * 1. Q: How would you solve this if you needed to duplicate any specific value, not just zeros?
 *    A: Generalize the solution by parameterizing the target value. The two-pass algorithm
 *    remains the same: count occurrences of the target value in the first pass, then fill
 *    the array backward in the second pass, duplicating the target value when encountered.
 *
 * 2. Q: What if the array can grow dynamically and we don't need to discard elements?
 *    A: Use a simple forward iteration with ArrayList or similar dynamic structure. When a
 *    zero is found, insert another zero. This becomes O(n^2) for array shifting but O(n)
 *    with ArrayList. Related: https://leetcode.com/problems/insert-interval/
 *
 * 3. Q: How would you optimize if zeros are very sparse (few zeros in a large array)?
 *    A: The current optimal solution is already efficient for sparse zeros since we only
 *    process elements once. However, you could maintain a list of zero indices and only
 *    shift segments between zeros, potentially improving cache locality.
 *
 * 4. Q: Can you solve this with a single pass instead of two passes?
 *    A: A true single-pass solution is challenging because we need to know the final position
 *    of each element before moving it. However, you could use extra space with a temporary
 *    array for a single-pass solution, which violates the O(1) space constraint.
 *
 * 5. Q: How would you handle the case where we need to duplicate zeros k times instead of once?
 *    A: Modify the counting logic to multiply zero count by k, and in the backward pass,
 *    insert k copies of zero instead of 2. The algorithm structure remains the same.
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

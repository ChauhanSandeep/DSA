package arrays.twopointers;

import java.util.Arrays;


/**
 * Problem: Rearrange Array Alternately
 *
 * Given a sorted array, rearrange it in max, min, second max, second min order.
 * The file keeps both the direct extra-array version and the in-place encoding
 * version for the same interview pattern.
 *
 * Source: https://www.geeksforgeeks.org/dsa/rearrange-array-maximum-minimum-form
 * Pattern:  Array | Two pointers | Max-min interleaving
 *
 * Example:
 *   Input:  arr = [1,2,3,4,5,6]
 *   Output: [6,1,5,2,4,3]
 *   Why:    take 6 from the right, 1 from the left, then keep alternating extremes.
 *
 * Follow-ups:
 *   1. Can this be done without extra space?
 *      Encode the old and new values in each slot, then decode in a second pass.
 *   2. What breaks if the array contains negative values?
 *      The modulo encoding assumes non-negative values below maxElement, so offset first.
 *   3. How would you support very large values safely?
 *      Use long arithmetic or the extra-array method to avoid integer overflow.
 *
 * Related: Wiggle Sort II (324), Rearrange Array Elements by Sign (2149).
 */
public class AlternateArray {

public static void main(String[] args) {
    AlternateArray solver = new AlternateArray();
    int[][] inputs = { {1, 2, 3, 4, 5, 6}, {7} };
    int[][] expected = { {6, 1, 5, 2, 4, 3}, {7} };

    for (int i = 0; i < inputs.length; i++) {
        int[] extraSpaceInput = inputs[i].clone();
        solver.rearrangeWithExtraSpace(extraSpaceInput);
        System.out.printf("extra nums=%s -> %s  expected=%s%n",
            Arrays.toString(inputs[i]), Arrays.toString(extraSpaceInput), Arrays.toString(expected[i]));

        int[] inPlaceInput = inputs[i].clone();
        solver.rearrangeInPlace(inPlaceInput);
        System.out.printf("inPlace nums=%s -> %s  expected=%s%n",
            Arrays.toString(inputs[i]), Arrays.toString(inPlaceInput), Arrays.toString(expected[i]));
    }
}

    /**
 * Intuition: keep one pointer on the smallest remaining value and one pointer
 * on the largest remaining value. A boolean decides which side supplies the
 * next output slot, so the temporary array is filled in the required max-min
 * order before being copied back.
 *
 * Algorithm:
 *   1. Allocate temp with the same length as arr.
 *   2. Start left at 0, right at n - 1, and pick from right first.
 *   3. Fill each temp slot from right or left, then flip the pick direction.
 *   4. Copy temp back into arr.
 *
 * Time:  O(n) - one fill pass plus one copy pass.
 * Space: O(n) - temp stores the rearranged values.
 *
 * @param arr sorted non-negative input array, mutated in place
 */
    public void rearrangeWithExtraSpace(int[] arr) {
        int n = arr.length;
        int[] temp = new int[n];

        int left = 0, right = n - 1;
        boolean pickFromEnd = true;

        for (int i = 0; i < n; i++) {
            temp[i] = pickFromEnd ? arr[right--] : arr[left++];
            pickFromEnd = !pickFromEnd;
        }

        // Copy temp array back to original array
        System.arraycopy(temp, 0, arr, 0, n);
    }

    /**
 * Intuition: the array is sorted, so maxIndex and minIndex already point to
 * the next values we want. To avoid a second array, each slot temporarily
 * stores oldValue + newValue * maxElement; modulo recovers old values while
 * division extracts the new arrangement at the end.
 *
 * Algorithm:
 *   1. Let maxIndex point to the last value and minIndex point to the first.
 *   2. For even i, encode arr[maxIndex] as the next value and move maxIndex.
 *   3. For odd i, encode arr[minIndex] as the next value and move minIndex.
 *   4. Decode every slot by dividing by maxElement.
 *
 * Time:  O(n) - one encoding pass and one decoding pass.
 * Space: O(1) - only indices and encoded values are stored.
 *
 * @param arr sorted non-negative input array, mutated in place
 */
    public void rearrangeInPlace(int[] arr) {
        int length = arr.length;
        int maxIndex = length - 1, minIndex = 0;
        int maxElement = arr[length - 1] + 1; // Store a value greater than max in array to encode two numbers

        // Encoding values into a single element
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) { // Store max element at even indices
                // we want to use arr[i] += newValue * maxElement;
                // but the newValue might already be changed to a new value
                // applying modulo on that will give us the original value
              int normalizedValue = (arr[maxIndex] % maxElement);
              arr[i] = arr[i] + normalizedValue * maxElement;
              maxIndex--;
            } else {
                // Store min element at odd indices
              int normalizedValue = (arr[minIndex] % maxElement); // Get original value as it might be encoded by now
              arr[i] = arr[i] + normalizedValue * maxElement;
              minIndex++;
            }
        }

        // Decode values back to original range
        for (int i = 0; i < length; i++) {
            /**
             * arr[i] = (oldValue + newValue * maxElement) // set previously encoded value
             * (oldValue + newValue * maxElement) / maxElement
             * → (newValue * maxElement) / maxElement + oldValue / maxElement
             * → newValue + 0
             * → newValue
             */
            arr[i] /= maxElement;
        }
    }
}

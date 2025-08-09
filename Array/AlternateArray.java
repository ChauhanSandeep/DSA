package Array;

import java.util.Arrays;


/**
 * Given a sorted array, rearrange it so that:
 * - The first element is the largest
 * - The second element is the smallest
 * - The third element is the second largest
 * - The fourth element is the second smallest, and so on.
 *
 * Example:
 * Input:  [1, 2, 3, 4, 5, 6]
 * Output: [6, 1, 5, 2, 4, 3]
 *
 * GFG Link: https://www.geeksforgeeks.org/dsa/rearrange-array-maximum-minimum-form/?utm_source=chatgpt.com
 */
public class AlternateArray {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6};
        AlternateArray alternateArray = new AlternateArray();

        // Using extra space approach
        alternateArray.rearrangeWithExtraSpace(arr);
        System.out.println("Rearranged Array (Extra Space): " + Arrays.toString(arr));

        // Using optimized in-place approach
        int[] arr2 = {1, 2, 3, 4, 5, 6};
        alternateArray.rearrangeInPlace(arr2);
        System.out.println("Rearranged Array (In-Place): " + Arrays.toString(arr2));
    }

    /**
     *
     * @param arr Input array (must be sorted)
     * Time Complexity: O(N)
     * Space Complexity: O(N) (due to extra array)
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
     * Optimized approach to rearrange the array in-place using encoding technique.
     * This avoids extra space usage.
     *
     * Logic:
     * - Use two pointers: one from the start (minIndex) and one from the end (maxIndex).
     * - Alternate between picking the maximum and minimum elements.
     * - Encode the values in a way that allows us to decode them later without using extra space.
     *
     * Example:
     * Input:  [1, 2, 3, 4, 5, 6]
     * Output: [6, 1, 5, 2, 4, 3]
     *
     * This section explains how to encode two values into a single array element using a mathematical trick. Here's a clearer explanation:
     *
     * ### Key Idea:
     * We want to rearrange the array in-place without using extra space. To do this, we encode two values (`originalValue` and `newValue`) into one array element using a large number `maxElement` (greater than the maximum value in the array).
     *
     * The encoding formula is: `arr[i] = originalValue + newValue * maxElement`
     *
     * ### Decoding:
     * 1. To extract the originalValue: originalValue = arr[i] % maxElement
     *    This works because `newValue * maxElement` is a multiple of `maxElement`, so it becomes `0` when using modulo.
     *
     * 2. To extract the `newValue`:  newValue = arr[i] / maxElement
     *    This works because integer division drops the `originalValue` part (since `originalValue < maxElement`).
     *
     * ### Why It Works:
     * - Modulo isolates the originalValue:
     *   (originalValue + newValue * maxElement) % maxElement
     *   → (newValue * maxElement) % maxElement + originalValue % maxElement
     *   → 0 + originalValue
     *   → originalValue
     *
     * - Division isolates the newValue:
     *   (originalValue + newValue * maxElement) / maxElement
     *   → (newValue * maxElement) / maxElement + originalValue / maxElement
     *   → newValue + 0
     *   → newValue
     *
     * This trick allows us to store two values in one array slot and rearrange the array in-place.
     *
     * Assumptions:
     *   - Array is sorted in non-decreasing order.
     *   - All elements are non-negative integers.
     *   - The array does not contain values so large that encoding overflows Integer.MAX_VALUE.
     *
     * @param arr Input array (must be sorted)
     * Time Complexity: O(N)
     * Space Complexity: O(1) (in-place modification)
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
              arr[i] = arr[i] + (arr[maxIndex--] % maxElement) * maxElement;
            } else {
                // Store min element at odd indices
              arr[i] = arr[i] + (arr[minIndex++] % maxElement) * maxElement;
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

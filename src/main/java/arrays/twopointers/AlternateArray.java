package arrays.twopointers;

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
     * ═══════════════════════════════════════════════════════════════════════════════
     * ENCODING TECHNIQUE - SIMPLIFIED EXPLANATION
     * ═══════════════════════════════════════════════════════════════════════════════
     *
     * Think of it like storing two pieces of information in a single number, similar to
     * how you might write "hours and minutes" as a single number (e.g., 1430 = 2:30 PM).
     *
     * ───────────────────────────────────────────────────────────────────────────────
     * STEP-BY-STEP EXAMPLE:
     * ───────────────────────────────────────────────────────────────────────────────
     * Array: [1, 2, 3, 4, 5, 6]
     * maxElement = 7 (we use 7 because it's greater than the max value 6)
     *
     * At index 0:
     *   - Original value: 1
     *   - New value we want to store: 6 (the largest element)
     *   - Encoded value: 1 + (6 × 7) = 1 + 42 = 43
     *
     * Now arr[0] = 43, which contains BOTH values:
     *   - To get original value: 43 % 7 = 1 ✓
     *   - To get new value: 43 / 7 = 6 ✓
     *
     * ───────────────────────────────────────────────────────────────────────────────
     * WHY THIS WORKS (Simple Math):
     * ───────────────────────────────────────────────────────────────────────────────
     *
     * Formula: encoded = original + (new × maxElement)
     *
     * 1. EXTRACTING ORIGINAL VALUE (using modulo %):
     *    encoded % maxElement = (original + new × maxElement) % maxElement
     *
     *    Breaking it down:
     *    - (new × maxElement) is perfectly divisible by maxElement, so remainder = 0
     *    - original is smaller than maxElement, so remainder = original
     *    - Result: original ✓
     *
     *    Real example: 43 % 7
     *    = (1 + 42) % 7
     *    = 1 % 7 + 42 % 7
     *    = 1 + 0 = 1
     *
     * 2. EXTRACTING NEW VALUE (using integer division /):
     *    encoded / maxElement = (original + new × maxElement) / maxElement
     *
     *    Breaking it down:
     *    - original / maxElement = 0 (since original < maxElement)
     *    - (new × maxElement) / maxElement = new
     *    - Result: new ✓
     *
     *    Real example: 43 / 7
     *    = (1 + 42) / 7
     *    = 1/7 + 42/7
     *    = 0 + 6 = 6
     * ═══════════════════════════════════════════════════════════════════════════════
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

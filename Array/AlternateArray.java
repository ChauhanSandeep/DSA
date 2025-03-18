package Array;

import java.util.Arrays;

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
     * @param arr Input array (must be sorted)
     * Time Complexity: O(N)
     * Space Complexity: O(1) (in-place modification)
     */
    public void rearrangeInPlace(int[] arr) {
        int n = arr.length;
        int maxIndex = n - 1, minIndex = 0;
        int maxElement = arr[n - 1] + 1; // Store a value greater than max in array to encode two numbers

        // Encoding values into a single element
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                // Store max element at even indices
                arr[i] += (arr[maxIndex--] % maxElement) * maxElement;
            } else {
                // Store min element at odd indices
                arr[i] += (arr[minIndex++] % maxElement) * maxElement;
            }
        }

        // Decode values back to original range
        for (int i = 0; i < n; i++) {
            arr[i] /= maxElement;
        }
    }
}

package StackQueue;

import java.util.Arrays;
import java.util.Stack;

/**
 * Problem: Maximize the sum of the given array after removing valleys.
 * 
 * Intuition:
 * - A valley is an element in the array that is surrounded by greater or equal elements.
 * - We iterate over the array twice (left-to-right and right-to-left) to compute possible maximum sums
 *   assuming the current element is the lowest in its segment.
 * 
 * Approach:
 * - Use a **monotonic increasing stack** to efficiently compute `leftSum` and `rightSum`.
 * - `leftSum[i]`: Maximum sum assuming elements on the left can be reduced to `arr[i]`.
 * - `rightSum[i]`: Maximum sum assuming elements on the right can be reduced to `arr[i]`.
 * - The final result is obtained by maximizing `(leftSum[i] + rightSum[i] - arr[i])`.
 * 
 * Time Complexity: **O(N)** (Each element is pushed and popped at most once)
 * Space Complexity: **O(N)** (For leftSum, rightSum, and stack)
 * 
 * Problem Link: https://www.geeksforgeeks.org/maximize-sum-of-given-array-after-removing-valleys/
 */
public class RemoveValleys {

    public static void main(String[] args) {
        int[] arr = {8, 1, 10, 1, 8};
        System.out.println(getMaxSumAfterRemovingValleys(arr));
    }

    /**
     * Computes the maximum sum after removing valleys from the array.
     * @param arr Input array
     * @return Maximum possible sum
     */
    public static int getMaxSumAfterRemovingValleys(int[] arr) {
        int n = arr.length;
        int[] leftSum = new int[n];  // Stores max sum considering left reductions
        int[] rightSum = new int[n]; // Stores max sum considering right reductions
        Stack<Integer> stack = new Stack<>();

        // Compute leftSum: Traverse left to right
        for (int i = 0; i < n; i++) {
            int currElement = arr[i];

            while (!stack.isEmpty() && arr[stack.peek()] >= currElement) {
                stack.pop();
            }

            if (stack.isEmpty()) {
                // No smaller element on the left; reduce everything to arr[i]
                leftSum[i] = (i + 1) * currElement;
            } else {
                int smallerIndex = stack.peek();
                leftSum[i] = leftSum[smallerIndex] + ((i - smallerIndex) * currElement);
            }

            stack.push(i);
        }

        stack.clear(); // Reset stack for rightSum calculation

        // Compute rightSum: Traverse right to left
        for (int i = n - 1; i >= 0; i--) {
            int currElement = arr[i];

            while (!stack.isEmpty() && arr[stack.peek()] >= currElement) {
                stack.pop();
            }

            if (stack.isEmpty()) {
                // No smaller element on the right; reduce everything to arr[i]
                rightSum[i] = (n - i) * currElement;
            } else {
                int smallerIndex = stack.peek();
                rightSum[i] = rightSum[smallerIndex] + ((smallerIndex - i) * currElement);
            }

            stack.push(i);
        }

        // Compute the maximum possible sum
        int maxSum = 0;
        for (int i = 0; i < n; i++) {
            maxSum = Math.max(maxSum, leftSum[i] + rightSum[i] - arr[i]);
        }

        return maxSum;
    }
}

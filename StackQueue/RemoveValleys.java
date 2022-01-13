package StackQueue;

import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

/**
 * https://www.geeksforgeeks.org/maximize-sum-of-given-array-after-removing-valleys/
 */
public class RemoveValleys {

    public static void main(String[] args) {
        int[] arr = {8, 1, 10, 1, 8 };
        System.out.println(solve(arr));
    }

    static int solve(int[] arr) {
        int size = arr.length;
        int[] leftSum = new int[size]; // sum at index i, considering all elements to left are smaller than arr[i]
        int[] rightSum = new int[size];// sum at index i, considering all elements to right are smaller than arr[i]
        Stack<Integer> stack = new Stack<>();

        // Calculate leftSum array
        for (int i = 0; i < size; i++) {
            int curr = arr[i];
            while (stack.size() != 0 && arr[stack.peek()] >= curr) {
                stack.pop();
            }

            if (stack.size() == 0) {
//              CASE 1. No left elements are smaller. Reduce all to arr[i]
                leftSum[i] = (i + 1) * curr;
            } else {
//              CASE 2. Smaller element found on left. Reduce all elements from smallIndex to i to value arr[i]
                int smallIndex = stack.pop();
                leftSum[i] = leftSum[smallIndex] + ((i - smallIndex) * curr);
            }
            stack.add(i);
        }
        stack.clear();

        // Calculate rightSum array
        for (int i = size - 1; i > -1; i--) {
            int curr = arr[i];
            while (stack.size() != 0 && arr[stack.peek()] >= curr) {
                stack.pop();
            }

            if (stack.size() == 0) {
//              CASE 1. No right elements are smaller. Reduce all to arr[i]
                rightSum[i] = (size - i) * curr;
            } else {
//              CASE 2. Smaller element found on right. Reduce all elements from smallIndex to i to value arr[i]
                int smallIndex = stack.peek();
                rightSum[i] = rightSum[smallIndex] + ((smallIndex - i) * curr);
            }
            stack.add(i);
        }
        System.out.println(Arrays.toString(leftSum));
        System.out.println(Arrays.toString(rightSum));

        int result = 0;
        for (int i = 0; i < size; i++) {
            int curr = leftSum[i] + rightSum[i] - arr[i];
            result = Math.max(result, curr);
        }
        return result;
    }
}

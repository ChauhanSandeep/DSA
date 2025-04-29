package StackQueue.AdityaVermaPlaylist;

import java.util.*;

/**
 * Given an array of integers, for each element, find the nearest smaller element to its right.
 * If no such element exists, output -1 for that element.
 *
 * Example:
 * Input:  [4, 5, 2, 10, 8]
 * Output: [2, 2, -1, 8, -1]
 */
public class NearestSmallerElement {

    /**
     * Finds the nearest smaller elements to the right for each element in the array.
     *
     * Intuition:
     * - We want the closest smaller element *on the right* for each element.
     * - A stack helps us maintain a decreasing sequence when traversing from right to left.
     *
     * Approach:
     * - Traverse the array from end to start (right to left).
     * - Use a stack to keep potential candidates for nearest smaller elements.
     * - For each element:
     *    - Pop elements from the stack that are greater than or equal to current.
     *    - If stack is empty after popping, no smaller element exists → put -1.
     *    - Else, the top of stack is the nearest smaller element.
     *    - Push current element onto the stack for future elements.
     *
     * Time Complexity: O(n)
     * - Each element is pushed and popped at most once from the stack.
     *
     * Space Complexity: O(n)
     * - Stack and result array both consume O(n) space.
     *
     * @param nums Input array of integers
     * @return Array containing nearest smaller elements to the right for each position
     */
    public int[] findNearestSmallerElementsToRight(int[] nums) {
        int length = nums.length;
        int[] result = new int[length];
        Stack<Integer> stack = new Stack<>();

        // Traverse from right to left
        for (int index = length - 1; index >= 0; index--) {
            int currentElement = nums[index];

            // Remove elements from stack that are greater than or equal to current element
            while (!stack.isEmpty() && stack.peek() >= currentElement) {
                stack.pop();
            }

            // If stack is empty, no smaller element exists to the right
            result[index] = stack.isEmpty() ? -1 : stack.peek();

            // Push the current element to stack for future elements
            stack.push(currentElement);
        }

        return result;
    }

    public static void main(String[] args) {
        NearestSmallerElement solver = new NearestSmallerElement();

        int[] inputArray = {4, 5, 2, 10, 8};
        int[] outputArray = solver.findNearestSmallerElementsToRight(inputArray);

        System.out.println("Nearest smaller elements to the right: " + Arrays.toString(outputArray));
    }
}
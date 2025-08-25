package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * LeetCode Problem: Nearest Smaller Element
 *
 * Problem Statement:
 * Given an array of integers, for each element, find the nearest smaller element to its left.
 * If no such element exists, output -1 for that element.
 *
 * Example:
 * Input:  [4, 5, 2, 10, 8]
 * Output: [-1, 4, -1, 2, 2]
 */
public class LeftSmallerElement {

    /**
     * Finds the nearest smaller elements to the left for each element in the array.
     *
     * Intuition:
     * - We need the closest smaller element *on the left* for each element.
     * - A stack helps us maintain a decreasing sequence (monotonic stack) so that
     *   we can efficiently find the nearest smaller element.
     *
     * Approach:
     * - Use a stack to keep potential candidates for nearest smaller elements.
     * - For each element:
     *    - Pop elements from the stack that are greater than or equal to current.
     *    - If stack is empty after popping, no smaller element exists → put -1.
     *    - Else, the top of stack is the nearest smaller element.
     *    - Push current element onto the stack for future use.
     *
     * Time Complexity: O(n)
     * - Each element is pushed and popped at most once from the stack.
     *
     * Space Complexity: O(n)
     * - Stack and result array both consume O(n) space.
     *
     * @param nums Input array of integers
     * @return Array containing nearest smaller elements for each position
     */
    public int[] findNearestSmallerElements(int[] nums) {
        int length = nums.length;
        int[] nearestSmaller = new int[length];
        Stack<Integer> stack = new Stack<>();

        // Process each element in the array
        for (int index = 0; index < length; index++) {
            int currentElement = nums[index];

            // Remove elements from stack that are greater than or equal to current element
            while (!stack.isEmpty() && stack.peek() >= currentElement) {
                stack.pop();
            }

            // If stack is empty, no smaller element exists to the left
            nearestSmaller[index] = stack.isEmpty() ? -1 : stack.peek();

            // Push the current element to stack for future elements
            stack.push(currentElement);
        }

        return nearestSmaller;
    }

    public static void main(String[] args) {
        LeftSmallerElement solver = new LeftSmallerElement();

        int[] inputArray = {4, 5, 2, 10, 8};
        int[] outputArray = solver.findNearestSmallerElements(inputArray);

        System.out.println("Nearest smaller elements to the left: " + Arrays.toString(outputArray));
    }
}